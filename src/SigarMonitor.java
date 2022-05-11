import org.hyperic.sigar.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.Buffer;


public class SigarMonitor implements IMonitorAction {
    private static final int SLEEP_TIME = 1000;
    private final String monitorName;
    private SigarProxy sigar;
    private String diskName;

    public SigarMonitor(String monName, String parameters) {
        Sigar sigarImpl = new Sigar();
        sigar = SigarProxyCache.newInstance(sigarImpl, SLEEP_TIME);

        monitorName = monName;
        diskName = parameters;
        System.out.println("[PerfMonitor] Will monitor disk " + diskName);
    }

    private void sendCPU(BufferedWriter writer, CpuPerc cpu) throws IOException {
        writer.write(cpu.getCombined()+"\n");
    }

    private void sendMemory(BufferedWriter writer, Mem mem) throws IOException {
        writer.write(mem.getActualUsed()+"\n");
    }

    private void sendDiskUsage(BufferedWriter writer, DiskUsage disk) throws IOException {
        writer.write(disk.getReadBytes()+"\n");
        writer.write(disk.getWriteBytes()+"\n");
    }

    public void doInitMonitorAction(BufferedWriter writer) throws IOException {
        writer.write(monitorName + " " + "sigar\n");
        writer.flush();
    }

    public void doPeriodicMonitorAction(BufferedWriter writer) throws SigarException, IOException {
        sendCPU(writer, sigar.getCpuPerc());
        sendMemory(writer, sigar.getMem());
        sendDiskUsage(writer, sigar.getDiskUsage(diskName));
        writer.flush();
    }

    public void doFinishMonitorAction(BufferedWriter writer) throws IOException {

    }

}
