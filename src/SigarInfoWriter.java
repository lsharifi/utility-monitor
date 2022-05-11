import com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter;
import org.hyperic.sigar.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class SigarInfoWriter {
    private BufferedWriter writer;

    private void sendCPU(CpuPerc cpu) throws IOException {
        writer.write(cpu.getCombined()+"\n");
    }

    private void sendMemory(Mem mem) throws IOException {
        writer.write(mem.getActualUsed()+"\n");
    }

    private void sendDiskUsage(DiskUsage disk) throws IOException {
        writer.write(disk.getReadBytes()+"\n");
        writer.write(disk.getWriteBytes()+"\n");
    }

    public void sendGuestName(String guestName) throws IOException {
        writer.write(guestName + "\n");
        writer.flush();
    }

    public void sendAllInfo(SigarProxy sigar, String diskName) throws SigarException, IOException {
        sendCPU(sigar.getCpuPerc());
        sendMemory(sigar.getMem());
        sendDiskUsage(sigar.getDiskUsage(diskName));
        writer.flush();
    }

}
