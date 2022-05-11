/**
 * Each parser must keep a state machine to know at each moment 
 * which is the expected value in the line
 */
public interface IMonitorParser {
    String getHeader();
    String parseLine(String line);
    int getNumberOfElements();
    boolean isHeader(String line);
}
