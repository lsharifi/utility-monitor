import java.util.Scanner;

/**
 * - changed on 23-11-2015 to stop using power counters
 */
public class PerfParser implements IMonitorParser {

    public String getHeader() {
        return "TIME;cycles;instructions;cache-references;cache-misses;page-faults";
    }

    /*
     XXX 1.000746840               8.07 Joules power/energy-pkg/         [100.00%]
     XXX 1.000746840               4.08 Joules power/energy-cores/
     1.000746840            7113975        cycles                    [100.00%]
     1.000746840            2446332        instructions              [100.00%]
     1.000746840              87643        cache-references          [100.00%]
     1.000746840              13772        cache-misses
     1.000746840                 13        page-faults
     */
    public String parseLine(String line) {
        Scanner scanner = new Scanner(line);
        scanner.next();
        String result = scanner.next();
        System.out.println("[PerfParser] parsed " + result);
        return result;
    }

    public int getNumberOfElements() {
        return 5;
    }

    /*
    e.g.      #           time             counts   unit events
    */
    public boolean isHeader(String line) {
        return line.startsWith("#");
    }
}
