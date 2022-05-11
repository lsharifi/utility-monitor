import java.util.Scanner;

public class CpuFreqParser implements IMonitorParser {

    public String getHeader() {
        return "Time;Core0;Core1;Core2;Core3;Core4;Core5;Core6;Core7";
    }

    public String parseLine(String line) {
        return line;
    }

    public int getNumberOfElements() {
        return 8;
    }

    public boolean isHeader(String line) {
        return false;
    }
}
