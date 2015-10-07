import abby.poker.OldMain;

public class PlayGame {
    private static int PLAYER_NUM = 4;

    public static void main(String[] args) {
	    OldMain game = new OldMain(PLAYER_NUM, true);
        game.start();
    }
}
