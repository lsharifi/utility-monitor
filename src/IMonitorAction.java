import org.hyperic.sigar.SigarException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.Buffer;

public interface IMonitorAction {
    void doInitMonitorAction(BufferedWriter writer) throws IOException;
    void doPeriodicMonitorAction(BufferedWriter writer) throws SigarException, IOException;
    void doFinishMonitorAction(BufferedWriter writer) throws IOException;
}
