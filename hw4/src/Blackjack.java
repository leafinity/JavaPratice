import java.util.*;
import foop.*;


public class Blackjack {
	/* setting */
	private int _nRound;
	private int _nChip;
	private int _mod;
	private static int nPlayer = 4;
	/*game infos*/
	private ArrayList<PlayerAgent> players;
	private Deck deck;
	private ArrayList<Hand> lastTable;

	/*dealer*/
	Dealer dealer;

	Blackjack(int nRound, int nChip, String playerName1, String playerName2, 
		String playerName3, String playerName4) {

		_nChip = nChip;
		_nRound = nRound;
		_mod = _nRound % 2;

		players = new ArrayList<PlayerAgent>();
		players.add(new PlayerAgent(playerName1, 0, nChip));
		players.add(new PlayerAgent(playerName2, 1, nChip));
		players.add(new PlayerAgent(playerName3, 2, nChip));
		players.add(new PlayerAgent(playerName4, 3, nChip));

		deck = new Deck();

		lastTable  = new ArrayList<Hand>();
		for (int i = 0; i < 4; i++)
			lastTable.add(new Hand(new ArrayList<Card>()));

		dealer = new Dealer();
	}

	public void run() {
		while(--_nRound>=0) {
			if (noPlayer()){
System.out.println("There is no player. Game Over.");
				break;
			}
			if ((_nRound - _mod + 1) %2 == 0)
				deck.shuffle();
			_askBet();
			_assignFirstCardPair();

			/*print current table*/
System.out.println("table:");
			_printTable();
System.out.println("----start----");

			if (dealer.getOpenCard().getValue() == 1) {
System.out.println("Dealer'open is A.'");
				_askInsurance();
				if (dealer.sneak().getValue() >= 10) {
					//dealer get blackjacket
System.out.println("Dealer get blackjack.");
					for (PlayerAgent player: players) {
						player.openFaceDownCard();
						if (!ComputeHand.isBlackJack(player.getHand()))
							player.decrease_chips(1 * player.getBet());
						if (player.hasInsurance())
							player.increase_chips(1 * player.getBet()); //0.5*2 insurance
					}
					_cleapUpPlayersCache();
					continue;
				}
			}
			_askSurrender();
			_playersPlay();
			_dealerPlay();	
			_compareResult();

			_checkBroken();
			_cleapUpPlayersCache();
			lastTable = _getCurrentTable();
			System.out.println();
		}
	}

	private void _askBet() {
		for (PlayerAgent player: players) {
			player.make_bet(_getLastTable(), nPlayer);
		}
	}

	private void _assignFirstCardPair() {
		for (PlayerAgent player: players) {
			player.setFaceDownCard(deck.take());
			player.assignCard(deck.take(), false);
		}
		dealer.faceDownCard = deck.take();
		dealer.faceUpCards.add(deck.take());
	}

	private void _askInsurance() {
		for (PlayerAgent player: players) {
			player.buy_insurance(dealer.getOpenCard(), _getCurrentTable(player));				
		}
	}

	private void _askSurrender() {
		for (PlayerAgent player: players) {
			player.do_surrender(dealer.getOpenCard(), _getCurrentTable(player));
		}
	}

	private void _cleapUpPlayersCache() {
		for (PlayerAgent player: players) {
			player.cleanUp();
		}
		dealer.cleanUp();
	}

	private void _playersPlay() {
		for (PlayerAgent player: players) {
			System.out.println(player + "'s turn:");
			//surrender
			if (player.hasSurrender()) {
				continue;
			}
			player.openFaceDownCard();
System.out.println(player.printHand(false));
			//splite
			boolean splited = player.do_split_if_can(dealer.getOpenCard(), _getCurrentTable(player)); 
			if (splited) {
				player.assignCard(deck.take(), false);
				player.assignCard(deck.take(), true);
System.out.println(player.printHand(false));
System.out.println(player.printHand(true));
			}
			boolean isSplitedRound = false;
			for (int i = 0; i < 2; i++) {
				//double down or hit till stand
				if (player.do_double(dealer.getOpenCard(), _getCurrentTable(player), isSplitedRound)) {
					player.assignCard(deck.take(), isSplitedRound);
				} else {
					while (true) { 
						//hit or stand
						if (player.hit_me(dealer.getOpenCard(), _getCurrentTable(player), isSplitedRound)) {
							player.assignCard(deck.take(), isSplitedRound);
						} else {
							break;
						}
System.out.println(player.printHand(isSplitedRound));
						//check busted
						if (isSplitedRound) {
							if (ComputeHand.isBusted(player.getSplitedHand())){
System.out.println("Bust!");
								break;
							}
						} else {
							if (ComputeHand.isBusted(player.getHand())){
System.out.println("Bust!");
								break;
							}
						}
					}//hit till stand
				}//double down or hit till stand

				//if splited should have next round
				if (splited) {
					isSplitedRound = true;
				} else {
					break;
				}
			}
		}
	}

	private void _dealerPlay() {
		System.out.println("dealer's turn.");
		while(true) {
			if (ComputeHand.getLTotalValue(dealer.getHand()) >= 16) {
				// no Ace = 11, and value over 16
				//	if there is any Ace, still can not be 11
				break;
			} else if (ComputeHand.getHTotalValue(dealer.getHand()) >= 17) {
				//one Ace = 11, bou total over 17
				break;
			}
			dealer.faceUpCards.add(deck.take());
		}
		System.out.println(dealer.printHand());
	}

	private void _compareResult() {
		for (PlayerAgent player: players) {
			if (player.hasSurrender()) {
				player.decrease_chips(0.5 * player.getBet());
				continue;
			}
			Hand dealerHand = dealer.getHand();
			Hand playerHand = player.getHand();
			int bet = player.getBet();
			for (int i = 0; i < 2; i++) {
				if (ComputeHand.isBusted(playerHand)) {
					player.decrease_chips(1 * bet);
				} else if (ComputeHand.isBlackJack(playerHand)) {
System.out.println(player + " get blackjack");
					if (!ComputeHand.isBlackJack(dealerHand))
						player.increase_chips(1.5 * bet);
				} else { 
					//player get neither busted or blackjack
					if(ComputeHand.isBlackJack(dealerHand)) {
						player.decrease_chips(1 * bet);
					} else if (ComputeHand.isBusted(dealerHand)){
						player.increase_chips(1 * bet);
					} else {
						int dealerValue = ComputeHand.getHTotalValue(dealerHand);
						int playerValue = ComputeHand.getHTotalValue(playerHand);
						if (dealerValue > playerValue) {
							player.decrease_chips(1 * bet);
						} else if (dealerValue < playerValue) {
							player.increase_chips(1 * bet);
						} else {					
System.out.println(player + " draw.");			
System.out.println(player.printChip());
						}
					}
				}
				if (player.hasSplited()) {
					playerHand = player.getSplitedHand();
					bet = player.getSplitedBet();
				} else {
					break;
				}
			}
		}
	}

	private void _checkBroken() {
		ArrayList<PlayerAgent> copy = new ArrayList<PlayerAgent>(players);
		for (PlayerAgent player: copy) {
			if (player.beKickOff())
				players.remove(player);
		}
		copy = new ArrayList<PlayerAgent>(players);
		for (PlayerAgent player: copy) {
			if (player.checkBroken())
				players.remove(player);
		}

	}

	private ArrayList<Hand> _getCurrentTable(PlayerAgent self) {
		ArrayList<Hand> currentTable = new ArrayList<Hand>();
		for (PlayerAgent player: players) {
			if (player != self) {
				currentTable.add(player.getHand());
			}
		}
		return currentTable;
	}

	private ArrayList<Hand> _getCurrentTable() {
		ArrayList<Hand> currentTable = new ArrayList<Hand>();
		currentTable.add(dealer.getHand());
		for (PlayerAgent player: players) {
			currentTable.add(player.getHand());
		}
		return currentTable;
	}

	private ArrayList<Hand> _getLastTable() {
		return lastTable;
	}

	private boolean noPlayer() {
		return players.size() == 0;
	}

	private void _printTable() {
		for (PlayerAgent player: players) {
assert player.getHand().getCards().size() == 1: "size: " + player.getHand().getCards().size();
			System.out.println(player + player.printHand(false));
		}
		System.out.println(dealer.printHand());
	}
}
