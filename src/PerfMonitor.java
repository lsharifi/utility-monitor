import org.hyperic.sigar.SigarException;

import java.io.*;

/**
 *  perf stat -a -e power/energy-pkg/,power/energy-cores/,cycles,instructions,cache-references,cache-misses,page-faults -I 1000
 */
public class PerfMonitor implements IMonitorAction {

    static String cmdLine = "sudo perf stat -a -e " +
                            "cycles,instructions,cache-references,cache-misses,page-faults -I 1000";
    private final String monName;
    private Process exec;
    private BufferedReader reader;
    private PerfParser parser;

    public PerfMonitor (String monName, String parameters) {
        this.monName = monName;
        parser = new PerfParser();
    }

    public void doInitMonitorAction(BufferedWriter writer) throws IOException {
        writer.write(monName + " " + "perf\n");
        writer.flush();
        exec = Runtime.getRuntime().exec(
                new String[] { "/bin/bash", "-c", cmdLine }
        );
        System.out.println("[PerfMonitor] Getting error stream to read values");
        InputStream in = exec.getErrorStream();
        reader = new BufferedReader(new InputStreamReader(in));
        //System.out.println("[PerfMonitor] skipping header of perf tool");
        // reader.readLine(); // skip head of perf tool
        //System.out.println("[PerfMonitor] doInitMonitorAction finished");
    }

    public void doPeriodicMonitorAction(BufferedWriter writer) throws SigarException, IOException {
        while (reader.ready()) {
            String line = reader.readLine();
            System.out.println("[PerfMonitor] read from perf tool " + line);
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
