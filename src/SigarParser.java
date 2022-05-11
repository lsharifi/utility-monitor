
public class SigarParser implements IMonitorParser {

    public String getHeader() {
        return "TIME;CPU;MEM;DISK_R;DISK_W";
    }

    public String parseLine(String line) {
        return line;
    }

    public int getNumberOfElements() {
        return 4; // cpu, mem, reads, writes
    }

    public boolean isHeader(String line) {
        return false;
    }

}
