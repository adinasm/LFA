import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;



public class Main {

    public static void main(String[] argz) {
        if(argz.length != 1) {
            System.err.println("Argument error");
            System.exit(1);
        }
        int mode = -1;
        if(argz[0].equals("-e")) {
            mode = 1;
        } else if(argz[0].equals("-a")) {
            mode = 2;
        } else if(argz[0].equals("-u")) {
            mode = 3;
        } else if(argz[0].equals("-v")) {
            mode = 4;
        } else if(argz[0].equals("-f")) {
            mode = 5;
        } else {
            System.err.println("Argument error");
            System.exit(1);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader("dfa"));
            Flexer scanner = new Flexer(br);
            scanner.yylex();
            br.close();

            // Done parsing

            switch(mode) {
                case 1:
                    // Has e
                    if (scanner.dfa.acceptsE()) {
                        System.out.println("Yes");
                    } else {
                        System.out.println("No");
                    }

                    break;
                case 2:
                    // Accessible states
                    scanner.dfa.printAccesibleStates();

                    break;
                case 3:
                    // Useful states
                    scanner.dfa.printUsefulStates();

                    break;
                case 4:
                    // Empty language
                    if (scanner.dfa.acceptsEmptyLanguage()) {
                        System.out.println("Yes");
                    } else {
                        System.out.println("No");
                    }

                    break;
                case 5:
                    // Finite language
                    if (scanner.dfa.acceptsFiniteLanguage()) {
                        System.out.println("Yes");
                    } else {
                        System.out.println("No");
                    }

                    break;
                default:
                    System.err.println("Argument error");
                    System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Inexistent file.");
            System.exit(1);
        }
    }
}
