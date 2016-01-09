import java.util.*;

class POOCasino {
	public static void main(String[] args) {
		if (args.length < 6) {
			throw new IllegalArgumentException("too few arguments");
		}
		int nRound = Integer.parseInt(args[0]);
		int nChip = Integer.parseInt(args[1]);
		String[] playerNames = new String[4];
		for (int i = 0; i < 4; i++) {
			playerNames[i] = args[i + 2];
		}

		Blackjack game = new Blackjack(nRound, nChip, playerNames[0],
			playerNames[1], playerNames[2], playerNames[3]);

		game.run();
	}
}
