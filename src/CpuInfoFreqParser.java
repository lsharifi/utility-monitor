import java.util.Scanner;

public class CpuInfoFreqParser implements IMonitorParser {
    int cores;

    public CpuInfoFreqParser(int cores) {
        this.cores = cores;
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder("Time;");
        for (int i=0; i<cores; ++i) {
            sb.append("Core");
            sb.append(i);
            if (i+1<cores) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    public String parseLine(String line) {
        Scanner scanner = new Scanner(line);
        scanner.next(); // consume
        scanner.next(); // consume
        scanner.next(); // consume
        return scanner.next();
    }

    public int getNumberOfElements() {
        return cores;
    }

    public boolean isHeader(String line) {
        return false;
    }
}
