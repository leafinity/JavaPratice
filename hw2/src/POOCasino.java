import com.sun.scenario.effect.impl.sw.java.JSWBlend_MULTIPLYPeer;

import java.util.*;

public class POOCasino {
    private static Computer _computer;
    private static Player _player;
    private static Scanner _scanner;
    private static char[] opt = {'a', 'b', 'c', 'd', 'e'};

    public static void main(String[] args) {
	    initialGame();
        startGame();
    }

    private static void initialGame() {
        _computer = new Computer();
        _scanner = new Scanner(System.in);

        System.out.println("POOCasino Jacks or better, written by b01902102 Tzu-Hsuan Yeh");
        System.out.print("Please enter your name:\t");

        String playerName = _scanner.nextLine();
        _player = new Player(playerName, 1000);

        System.out.println("Welcome, " + playerName + ".");
        System.out.println("You have 1000 P-dollars now.");

        _computer.shuffleDeck();
    }

    private static void startGame() {
        int round = 0;
        boolean keep = true;
        while(keep) {
            keep = gameRound(round);
        }
        System.out.print("Good bye, CharlieL.");
        System.out.print(" You played for " + round + " round");
        System.out.println(" and have " + _player.get_PDollarNum() + " P-dollars now.");
    }

    private static boolean gameRound(int round) {
        boolean exit = false;
        System.out.print(
                "Please enter your P-dollar bet for round round (1-5 or 0 for quitting the game):\t");
        int betNum;
        try {
            betNum = _scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Your bet is unacceptable.");
            _scanner.next();
            exit = true;
            return exit;
        }
        if (betNum == 0) {
            exit = true;
        } else if (betNum > 5) {
            System.out.println(betNum + "is unacceptable.");
            exit = true;
        } else {
            /*start game round*/
            exit = false;
            _player.bet(betNum);

            /*deal card*/
            _player.hand = _computer.dealCard(5);
            System.out.print("Your cards are ");
            _showPlayersHand(true);

            /*keep or drop*/
            List<Integer> keeps = new ArrayList<>();
            boolean getKeeps = false;
            while (!getKeeps) {
                System.out.print("\nWhich cards do you want to keep?\t");
                String input = _scanner.next();
                keeps = _parseAnswer(input);
                if (!_answerIsAcceptable(keeps)) {
                    System.out.println("Sorry I cannot recognize yous answer.");
                    System.out.println("These is example of answer: \"abd\".");
                } else {
                    getKeeps = true;
                }
            }

            /*re-deal cards*/
            System.out.print("Okay. I will discard ");
            List<Card> keepCard = new ArrayList<Card>();
            int dropNum = 0;

            for (int i = 0; i < 5; i++) {
                if(keeps.get(i) != 1) {
                    System.out.print("(" + opt[i] + ") "
                            + _player.hand.get(i));
                    dropNum++;
                } else {
                    keepCard.add(_player.hand.get(i));
                }
            }
            System.out.println();

            _player.hand = keepCard;
            _player.hand.addAll(_computer.dealCard(dropNum));

            /*show result*/
            System.out.println("Your new cards are ");
            _showPlayersHand(false);

            Computer.HandKind result = Computer.HandKind.parse(_player.hand);
            Integer payoff = _computer.pay(betNum, result);
            System.out.print("You get a " + result.toString() + ".");
            System.out.println(" The payoff is " + payoff + ".");
            _player.earn(payoff);
            System.out.println("You have " + _player.get_PDollarNum() + " P-dollars now.");
        }

        return (!exit);
    }

    private static void _showPlayersHand(boolean withOption) {
        _player.hand.sort(Comparator.<Card>naturalOrder());
        for(int i = 0; i < 5; i++) {
            if (withOption) {
                System.out.print("(" + opt[i] + ") "
                        + _player.hand.get(i).toString() + ' ');
            } else {
                System.out.print(_player.hand.get(i).toString() + ' ');
            }
        }
    }

    private static List<Integer> _parseAnswer(String ans) {
        /*initialize cnt*/
        List<Integer> cnt = new ArrayList();
        for (int i = 0; i < 6; i++) {
            cnt.add(0);
        }

        /*count*/
        for (int i = 0, len = ans.length(); i < len; i++) {
            switch (ans.charAt(i) - 'a') {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    cnt.set(ans.charAt(i) - 'a', cnt.get(ans.charAt(i) - 'a') + 1);
                    break;
                default:
                    cnt.set(5, cnt.get(5) + 1);
            }
        }
        return cnt;
    }

    private static boolean _answerIsAcceptable(List<Integer> parsedAns) {
        for (int i = 0; i < 5; i++) {
            if (parsedAns.get(i) > 1)
                return false;
        }
        if(parsedAns.get(5) > 0)
            return false;
        return true;
    }

}
