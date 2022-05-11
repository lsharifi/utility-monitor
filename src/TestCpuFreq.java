import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class TestCpuFreq {

    static String cmdLine = "sudo cat /sys/devices/system/cpu/cpu*/cpufreq/cpuinfo_cur_freq";

    public static void main(String[] args) throws IOException {
        while (true) {
            Process exec = Runtime.getRuntime().exec(
                    new String[]{"/bin/bash", "-c", cmdLine}
            );
            //System.out.println("[CpuFreqMonitor] Getting input stream to read cpu freq values");
            InputStream in = exec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            System.out.println("[CpuFreqMonitor] Open input stream to read cpu freq values");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[CpuFreqMonitor] line=" + line);
            }
            /*while (reader.ready()) {
                String line = reader.readLine();
                System.out.println("[CpuFreqMonitor] line=" + line);
                if (line != null) {
                    System.out.println(line);
                }
            }*/
            reader.close();
            exec.destroy();
            Scanner scanner = new Scanner(System.in);
            System.out.println("\"exit\" to exit");
            String cmd = scanner.next();
            if (cmd.equals("exit"))
                return;
        }
    }
}
