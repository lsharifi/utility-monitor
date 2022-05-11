import java.util.Scanner;


public class TestParser {
    public static void main(String[] args) {
        String s1 = "      1.000173456              34.84 Joules power/energy-pkg/         [100.00%]";
        String s2 = "46.026608265                                                                                              8        page-faults";
        String s3 = "1.000746840            7113975        cycles                    [100.00%]";

        Scanner scanner = new Scanner(s1);
        while (scanner.hasNext())
            System.out.print(scanner.next() + ", ");
        System.out.println();

        scanner = new Scanner(s2);
        while (scanner.hasNext())
            System.out.print(scanner.next() + " ");
        System.out.println();

        scanner = new Scanner(s3);
        while (scanner.hasNext())
            System.out.print(scanner.next() + " ");
        System.out.println();

        String s4 = "cpu MHz         : 1600.000";
        scanner = new Scanner(s4);
        while (scanner.hasNext())
            System.out.print(scanner.next() + " ");
        System.out.println();

    }
}
