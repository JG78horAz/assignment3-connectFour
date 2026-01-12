package tcd3.connectfour;

import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        run(new ConnectFourImpl(Player.red), new Scanner(System.in), System.out);
    }

    static void run(ConnectFour connectFour, Scanner scanner, PrintStream out) {
        String input;

        out.println(connectFour);

        while (!connectFour.isGameOver()) {
            out.print("command [1 .. 7, (r)estart, (q)uit, (h)elp] > ");
            if (!scanner.hasNextLine()) break;
            input = scanner.nextLine();

            switch (input) {
                case "1": connectFour.drop(0);               break;
                case "2": connectFour.drop(1);               break;
                case "3": connectFour.drop(2);               break;
                case "4": connectFour.drop(3);               break;
                case "5": connectFour.drop(4);               break;
                case "6": connectFour.drop(5);               break;
                case "7": connectFour.drop(6);               break;
                case "r": connectFour.reset(Player.red);         break;
                case "q": out.println("Ok, bye.");               return;
                case "h": printHelp(out);                        break;
                default:  out.println("Unknown command");        break;
            }
            out.println(connectFour);
        }

        out.println("GAME OVER - Winner: " + connectFour.getWinner().toString().toUpperCase());
    }

    private static void printHelp(PrintStream out) {
        out.println();
        out.println("Available commands:");
        out.println("-------------------");
        out.println("1 .. 7 --> drop disc in column");
        out.println("r      --> restart game");
        out.println("q      --> quit game");
        out.println("h      --> show help");
    }
}