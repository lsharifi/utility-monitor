import org.hyperic.sigar.SigarException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;



public class LocalMonitor extends Thread {
    public static final String STOP_MARK = "<END>";

    private IMonitorAction action;
    private int waitMilis;
    private Socket client;
    private BufferedWriter writer;
    private volatile boolean isRunning;

    public LocalMonitor(IMonitorAction action, int waitMiliseconds, String remoteMonitorHost, int remoteMonitorPort) throws IOException {
        this.action = action;
        isRunning = true;
        waitMilis = waitMiliseconds;

        System.out.println("[LocalMon] New monitor connecting to remote receiver ... " + remoteMonitorHost);
        client = new Socket(InetAddress.getByName(remoteMonitorHost), remoteMonitorPort);
        writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    }

    public void requestStop() {
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            action.doInitMonitorAction(writer);
            while (isRunning) {
                action.doPeriodicMonitorAction(writer);
                Thread.sleep(waitMilis);
            }
            action.doFinishMonitorAction(writer);
            writer.write(STOP_MARK + "\n");
            client.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
