import org.hyperic.sigar.SigarException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class MonitorWaiter {
    private static final int SLEEP_TIME = 1000 * 1/*1 sec.*/;

    private static HashMap<String, LocalMonitor> monitors = new HashMap<String, LocalMonitor>();

    /*
     * @param args[0] local waiting port
     *        args[1] remote dumper address
     *        args[2] remote dumper port
     *        args[3] disk name to monitor
     */
    private static void processRequest(String[] mainArgs, Socket signalizer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(signalizer.getInputStream()));
        String cmd = reader.readLine();
        /*
         * command from guest "<name> <perf|sigar|cpufreq>"
         */
        if (cmd != null) {
            System.out.println("[LocalMon] Received command { " + cmd + " }");
            String[] parts = cmd.split(" ");
            if (parts.length < 3)
                System.out.println("[LocalMon] Incomplete command <" + cmd + ">");
            String op = parts[0];
            String name = parts[1];
            String type = parts[2].toLowerCase();
            String parameters = parts.length > 3 ? parts[3]: "";
            if (op.equals("start")) {
                // Start
                System.out.println("[LocalMon] Start received");
                IMonitorAction monitorAction;
                int monitoringInterval;
                if (type.equals("sigar"))
                {
                    monitorAction = new SigarMonitor(name, mainArgs[4]);
                    monitoringInterval = 1000;
                } else if (type.equals("perf"))
                {
                    monitorAction = new PerfMonitor(name, mainArgs[4]);
                    monitoringInterval = 1000;
                } else if (type.equals("cpufreq")) {
                    monitorAction = new CpuFreqMonitor(name, mainArgs[4]);
                    monitoringInterval = 1000;
                } else if (type.equals("watts")) {
                    monitorAction = new WattsupMonitor(name, mainArgs[4]);
                    monitoringInterval = 1000;
                } else {
                    System.out.println("[LocalMon] Error parsing action <" + type + ">");
                    return;
                }
                LocalMonitor mon = new LocalMonitor(
                        monitorAction,
                        monitoringInterval, /* monitoring interval */
                        mainArgs[2],
                        Integer.parseInt(mainArgs[3]));
                monitors.put(name, mon);
                mon.start();
            } else if (op.equals("stop")) {
                // Stop
                LocalMonitor monitor = monitors.get(name);
                if (monitor == null) {
                    System.out.println("[LocalMon] Stop received without start");
                } else {
                    System.out.println("[LocalMon] Stop received");
                    monitor.requestStop();
                }
            } else {
                System.out.println("[LocalMon] Error parsing command <" + cmd + ">");
            }
        }

    }

    /**
     *
     * @param args[0] local waiting port
     *        args[1] remote dumper address
     *        args[2] remote dumper port
     *        args[3] disk name to monitor
     * @throws IOException
     */
    public static void main(String[] args) throws SigarException, InterruptedException, IOException {

        if (args.length < 5) {
            System.out.println("Parameters <ip addr> <waiting port> <remote dumper addr> <remote dumper port> <disk to monitor>");
            System.exit(-1);
        }

        String ipAddr = args[0];
        int port = Integer.parseInt(args[1]);
        ServerSocket localMonitorWaiter = new ServerSocket();
        //localMonitorWaiter.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        localMonitorWaiter.bind(new InetSocketAddress(ipAddr, port));
        System.out.println("[LocalMon] socket started @ " + localMonitorWaiter.toString());

        while (true) {
            System.out.println("[LocalMon] Waiting new requests to start monitoring. " +
                               "Protocol options: \n"+
                               " > start <monitor_id> FREQ|PERF|SIGAR|WATTS <parameters> \n" +
                               " > stop <monitor_id> \n" +
                               " <parameters> are comma separated \n"+
                               " ... example: start kvm_u1 PERF p1;p2 \n");
            Socket signalizer = null;
            try {
                signalizer = localMonitorWaiter.accept();
                processRequest(args, signalizer);
                signalizer.close();
            } catch(IOException ex) {
                System.out.println("[LocalMon] Problems with connection");
                ex.printStackTrace();
                continue;
            }
        }
    }

}
