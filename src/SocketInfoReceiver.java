import sun.rmi.server.InactiveGroupException;

import java.io.*;
import java.net.*;
import java.util.Calendar;

public class SocketInfoReceiver extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private IMonitorParser parser;
    int cores;
    String dirName;

    public void init(Socket socket, String dirName, int cores) throws IOException {
        this.dirName = dirName;
        this.socket = socket;
        this.cores = cores;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        start();
    }

    @Override
    public void run() {
        String line;
        String appName, type;
        BufferedWriter fileOut;

        try {
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            String cmd = reader.readLine();
            String[] parts = cmd.split(" ");
            if (parts.length < 2) {
                System.out.println("Error parsing command <" + cmd + ">");
                socket.close();
            }
            appName = parts[0] + ".csv";
            type = parts[1];
            System.out.println("Dumping <" + type + "> info to file <" + appName + ">");

            if (type.equals("sigar")) {
                parser = new SigarParser();
            } else if (type.equals("perf")) {
                parser = new PerfParser();
            } else if (type.equals("cpufreq")) {
                parser = new CpuInfoFreqParser(cores);
            } else if (type.equals("wattsup")) {
                parser = new WattsupParser();
            } else {
                System.out.println("Error in type <" + type + ">");
            }

            fileOut = new BufferedWriter(new FileWriter(dirName + "/" + appName));
            fileOut.write(parser.getHeader() + "\n");
            while ((line = reader.readLine()) != null) {
                if (line.equals(LocalMonitor.STOP_MARK)) {
                    break;
                }
                if (parser.isHeader(line))
                    continue;
                int nrElements = parser.getNumberOfElements();
                String all = parser.parseLine(line);
                //System.out.println("[InfoReceiver *"+type+"*] received " + line);
                while (nrElements > 1) {
                    all += ";" + parser.parseLine(line = reader.readLine());
                    //System.out.println("[InfoReceiver *"+type+"*] received " + line);
                    nrElements--;
                }
                //System.out.println("[InfoReceiver *"+type+"*] writing " + all);
                long time = System.currentTimeMillis();
                fileOut.write(time+";"+all+"\n");
                fileOut.flush();
            }
            System.out.println("Stopping dump to file <" + appName + ">");
            fileOut.close();
            reader.close();
            System.out.println("Closing connection");
            socket.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Parameters <local port> [number of cores]");
            System.exit(-1);
        }

        String dirName = String.valueOf(Calendar.getInstance().getTime().getTime());
        File dir = new File(dirName);
        if (!dir.mkdir()) {
            System.err.println("Erro creating dir " + dir);
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);
        int cores = 8;
        if (args.length == 2) {
            // get number of cores to monitor
            cores = Integer.parseInt(args[1]);
        }
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        while (true) {
            System.out.println("[Monitor writer] Accepting new requests...");
            Socket child = server.accept();
            SocketInfoReceiver receiver = new SocketInfoReceiver();
            receiver.init(child, dirName, cores);
        }
    }
}
