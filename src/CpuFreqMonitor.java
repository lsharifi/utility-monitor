import org.hyperic.sigar.SigarException;

import java.io.*;


public class CpuFreqMonitor implements IMonitorAction {
    //static String cmdLine = "sudo cat /sys/devices/system/cpu/cpu*/cpufreq/cpuinfo_cur_freq";
    static String cmdLine = "sudo cat /proc/cpuinfo | grep \"cpu MHz\"";
    private BufferedReader reader;
    private final String monitorName;

    public CpuFreqMonitor(String monName, String parameters) {
        monitorName = monName;
    }

    public void doInitMonitorAction(BufferedWriter writer) throws IOException {
        writer.write(monitorName + " " + "cpufreq\n");
        writer.flush();
    }

    public void doPeriodicMonitorAction(BufferedWriter writer) throws SigarException, IOException {
        Process exec = Runtime.getRuntime().exec(
                new String[] { "/bin/bash", "-c", cmdLine }
        );
        //System.out.println("[CpuFreqMonitor] Getting input stream to read cpu freq values");
        InputStream in = exec.getInputStream();
        reader = new BufferedReader(new InputStreamReader(in));
        //System.out.println("[CpuFreqMonitor] Open input stream to read cpu freq values");
        String line;
        while ((line = reader.readLine()) != null) {
            //System.out.println("[CpuInfoFreqMonitor] line=" + line);
            writer.write(line+"\n");
            writer.flush();
        }
        reader.close();
        exec.destroy();
    }

    public void doFinishMonitorAction(BufferedWriter writer) throws IOException {

    }
}
