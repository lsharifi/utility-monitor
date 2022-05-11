import java.util.Scanner;

/**
 * - changed on 23-11-2015 to stop using power counters
 */
public class WattsupParser implements IMonitorParser {

    public String getHeader() {
        return "Time;watts";
    }

    public String parseLine(String line) {
        System.out.println("[WattsupParser] after parsing *" + line + "*");
        return line;
    }

    public int getNumberOfElements() {
        return 1;
    }

    public boolean isHeader(String line) {
        return false;
    }
}
