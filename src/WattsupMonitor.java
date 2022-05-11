import org.hyperic.sigar.SigarException;

import java.io.*;

public class WattsupMonitor implements IMonitorAction {

    static String cmdLine = "sudo ./wattsup -c 1000 ttyUSB0 watts";
    private final String monName;
    private Process exec;
    private BufferedReader reader;

    public WattsupMonitor(String monName, String parameters) {
        // parameters are not used in this monitor
        this.monName = monName;
    }

    public void doInitMonitorAction(BufferedWriter writer) throws IOException {
        writer.write(monName + " wattsup\n");
        writer.flush();
        exec = Runtime.getRuntime().exec(
                new String[] { "/bin/bash", "-c", cmdLine }
        );
        System.out.println("[WattsupMonitor] Getting error stream to read values");
        InputStream in = exec.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        //System.out.println("[PerfMonitor] skipping header of perf tool");
        // reader.readLine(); // skip head of perf tool
        //System.out.println("[PerfMonitor] doInitMonitorAction finished");
    }

    public void doPeriodicMonitorAction(BufferedWriter writer) throws SigarException, IOException {
        while (reader.ready()) {
            String line = reader.readLine();
            System.out.println("[WattsupMonitor] read from wattsup tool " + line);
            if (line != null) {
                writer.write(line+"\n");
                writer.flush();
            }
        }
    }

    public void doFinishMonitorAction(BufferedWriter writer) throws IOException {
        exec.destroy();
    }
}
