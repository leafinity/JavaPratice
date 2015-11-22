import java.util.Scanner;
import abby.poker.*;

public class PlayGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //choose game
        while(true) {
            OldMain game;
            Integer playerNum;
            boolean bonusGame;
            boolean interactive;

            System.out.println("Welcome to OldMain playground.");
            System.out.println("How many player you want to play with?");
            while (true) {
                try {
                    playerNum = scanner.nextInt() + 1;
                    if (playerNum < 2) {
                        throw new IllegalArgumentException();
                    }
                } catch (Exception e) {
                    System.out.println("Please enter an Integer which is bigger than 1.");
                    continue;
                }
                break;
            }
            if (playerNum > 2) {
                System.out.println("Do you want to play with bonus game?(y/n)");
                System.out.println("(till the latest player finished)");
                while (true) {
                    String input = scanner.nextLine();
                    switch (input) {
                        case "y":
                        case "Y":
                        case "yes":
                        case "Yes":
                        case "YES":
                            bonusGame = true;
                            break;
                        case "n":
                        case "N":
                        case "no":
                        case "No":
                        case "NO":
                            bonusGame = false;
                            break;
                        default:
                            System.out.println("please enter 'y' or 'n'.");
                            continue;
                    }
                    break;
                }
            } else {
                bonusGame = false;
            }
            System.out.println("Do you want to play Interactive?(y/n)");
            while (true) {
                String input = scanner.nextLine();
                switch (input) {
                    case "y":
                    case "Y":
                    case "yes":
                    case "Yes":
                    case "YES":
                        interactive = true;
                        break;
                    case "n":
                    case "N":
                    case "no":
                    case "No":
                    case "NO":
                        interactive = false;
                        break;
                    default:
                        System.out.println("please enter 'y' or 'n'.");
                        continue;
                }
                break;
            }
            System.out.println("What kind of the game you want to play?");
            System.out.println("[a]Regular oldMain with two jokers");
            System.out.println("[b]OldMain with one joker");
            System.out.println("[c]OldMain with unknown joker");
            System.out.println("[d]OldMain with unknown joker and two jokers");
            while (true) {
                String gameMode = scanner.nextLine();
                switch (gameMode) {
                    case "a":
                        game = new OldMain(playerNum, bonusGame, interactive);
                        break;
                    case "b":
                        game = new Jackass(playerNum, bonusGame, interactive);
                        break;
                    case "c":
                        game = new DonkeyWithNoJoker(playerNum, bonusGame, interactive);
                        break;
                    case "d":
                        game = new Donkey(playerNum, bonusGame, interactive);
                        break;
                    default:
                        System.out.println("please enter 'a', 'b', 'c' or 'd'.");
                        continue;
                }
                break;
            }
            game.start();
            System.out.println("Do you want to play again?(y)");
            boolean quit = true;
            String input = scanner.nextLine();
            switch (input) {
                case "y":
                case "Y":
                case "yes":
                case "Yes":
                case "YES":
                    quit = false;
                    break;
            }
            if (quit) break;
        }
    }
}
