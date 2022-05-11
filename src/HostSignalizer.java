import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HostSignalizer {
    public static void main(String[] args) throws IOException {
        if (args.length < 5) {
            System.out.println("Parameters <waiter addr> <waiter port> <start|stop> <guest name> <perf|sigar> [<parameters>]");
            System.exit(-1);
        }

        Socket localWaitter = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(localWaitter.getOutputStream()));
        String cmd = args[2] + " " + args[3] + " " + args[4] + (args.length==6? args[5]: "");
        System.out.println("[HostSignalizer] Sending " + cmd);
        bw.write(cmd+"\n");
        bw.close();
        localWaitter.close();
    }
}
