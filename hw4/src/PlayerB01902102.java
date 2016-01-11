import foop.*;
import java.util.*;


public class PlayerB01902102 extends Player {
	
	public enum Act {
		HIT(true,false), DOUBLE(true,true), DSTAND(false,true), STAND(false,false);

		public final boolean hit;
		public final boolean doubleDown;

		private Act(boolean doHit, boolean doDoubleDown) {
			hit = doHit;
			doubleDown = doDoubleDown;
		}
	}

	private Random chance;

	private int unitBet;
	private int bet;
	private double lastChips;

	public PlayerB01902102(int nChip) {
		super(nChip);
		
		chance = new Random(System.currentTimeMillis());

		lastChips = nChip;
		unitBet = nChip/100;
		if (unitBet < 1) 
			unitBet = 1;
		bet = unitBet;
	}

	public int make_bet(ArrayList<Hand> last_table, int total_player, int my_position) {
		if (get_chips() < lastChips) {
			bet *= 2;
		} else if (get_chips() > lastChips) {
			bet = unitBet;
		}
		lastChips = get_chips();
		return bet;
	}

	public boolean buy_insurance(Card my_open, Card dealer_open, ArrayList<Hand> current_table) {
		int nTen = 0; 
		for (Hand hand: current_table ) {
			ArrayList<Card> cards = hand.getCards();
			for (Card card: cards) {
				if (card.getValue() >= 10)
					nTen++;
			}
		}
		if (my_open.getValue() >= 10)
			nTen++;
		
		if (nTen > 0) {
			return false;
		}
		if(chance.nextDouble() < 0.7) {
			return false;
		}
		return true;
	}

	public boolean do_double(Hand my_open, Card dealer_open, ArrayList<Hand> current_table) {
		int pos_y = getValue(dealer_open) - 1;
		int myLValue = getLTotalValue(my_open);

		if (getHTotalValue(my_open) != myLValue) {
			//soft case
			return actions_soft[getSoftPos(myLValue)][pos_y].doubleDown;
		}
		//hard case
		return actions_hard[getHardPos(myLValue)][pos_y].doubleDown;
	}

	public boolean do_split(ArrayList<Card> my_open, Card dealer_open, ArrayList<Hand> current_table) {
		int pos_x = getValue(my_open.get(0)) - 1;
		int pos_y = getValue(dealer_open) - 1;
		return  split[pos_x][pos_y];
	}

	public boolean do_surrender(Card my_open, Card dealer_open, ArrayList<Hand> current_table) {
		/*it is wired yo surrender with ony one card open*/
		if (dealer_open.getValue() >= 10 || dealer_open.getValue() == 1) {
			if (my_open.getValue() <= 7 && my_open.getValue() >= 5) {
				if (chance.nextDouble() < 0.3) 
					return true;
			}
		}
		return false;
	}

	public boolean hit_me(Hand my_open, Card dealer_open, ArrayList<Hand> current_table) {
		int pos_y = getValue(dealer_open) - 1;
		int myLValue = getLTotalValue(my_open);

		if (getHTotalValue(my_open) != myLValue) {
			//soft case
			return actions_soft[getSoftPos(myLValue)][pos_y].hit;
		}
		//hard case
		return actions_hard[getHardPos(myLValue)][pos_y].hit;
	}
	
	public String toString() {
		return get_chips() + " chips";
	}

	private int getValue(Card card) {
		if (card.getValue() > 10)
			return 10;
		else
			return card.getValue();
	}

	private int getSoftPos(int lowTotalValue) {
		if (lowTotalValue <= 4)
			return 0;
		else if (lowTotalValue <= 6)
			return 1;
		else if (lowTotalValue <= 8)
			return lowTotalValue - 5;
		else
			return 4;
	}

	private int getHardPos(int lowTotalValue) {
		if (lowTotalValue < 8)
			return 0;
		else if (lowTotalValue <= 12) 
			return lowTotalValue - 8;
		else if (lowTotalValue <= 16)
		 	return 5;
		else 
			return 6;

	}	

	public static int getHTotalValue(Hand hand) {
		int total = 0;
		boolean hasAce = false;
		for(Card card: hand.getCards()) {
			switch(card.getValue()) {
				case 11:
				case 12:
				case 13:
					total += 10;
					break;
				case 1:
					if (hasAce){
						total += 1;
					} else {
						total += 11;
						hasAce = true;
					}
					break;
				default:
					total += card.getValue();
					break;
			}
		}
		if (total > 21 && hasAce)
			total -= 10;
		return total;
	}

	public static int getLTotalValue(Hand hand) {
		int total = 0;
		for(Card card: hand.getCards()) {
			switch(card.getValue()) {
				case 11:
				case 12:
				case 13:
					total += 10;
					break;
				default:
					total += card.getValue();
					break;
			}
		}
		return total;
	}

	private boolean[][] split = {
		//	A, 2, 3, 4, 5, 6, 7, 8, 9, T (dealer)
		{	true,	true,	true,	true,	true,	true,	true,	true,	true,	true	}, //AA
		{	false,	true,	true,	true,	true,	true,	true,	false,	false,	false	}, //22
		{	false,	true,	true,	true,	true,	true,	true,	false,	false,	false	}, //33
		{	false,	false,	false,	false,	true,	true,	false,	false,	false,	false	}, //44
		{	false,	false,	false,	false,	false,	false,	false,	false,	false,	false	}, //55
		{	false,	true,	true,	true,	true,	true,	false,	false,	false,	false	}, //66
		{	false,	true,	true,	true,	true,	true,	true,	false,	false,	false	}, //77
		{	true,	true,	true,	true,	true,	true,	true,	true,	true,	true	}, //88
		{	false,	true,	true,	true,	true,	true,	false,	true,	true,	false	}, //99
		{	false,	false,	false,	false,	false,	false,	false,	false,	false,	false	} //TT
	};
	
	private Act[][] actions_hard = {
		//A, 2, 3, 4, 5, 6, 7, 8, 9, T (dealer)
		{	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT		}, //8
		{	Act.HIT, 	Act.HIT, 	Act.DOUBLE, Act.DOUBLE, Act.DOUBLE, Act.DOUBLE,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT		}, //9
		{ 	Act.HIT, 	Act.DOUBLE, Act.DOUBLE, Act.DOUBLE, Act.DOUBLE, Act.DOUBLE, Act.DOUBLE, Act.DOUBLE, Act.DOUBLE,	Act.HIT		}, //10
		{ 	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE,	Act.DOUBLE	}, //11
		{	Act.HIT,	Act.HIT,	Act.HIT,	Act.STAND,	Act.STAND,	Act.STAND,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT		}, //12
		{	Act.HIT,	Act.HIT,	Act.HIT,	Act.STAND,	Act.STAND,	Act.STAND,	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT		}, //13-16
		{	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND 	} //17+
	};

	private Act[][] actions_soft = {
		//A, 2, 3, 4, 5, 6, 7, 8, 9, T (dealer)
		{	Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT,	Act.DOUBLE,	Act.DOUBLE, Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT 	}, //A1,A2,A3
		{ 	Act.HIT,	Act.HIT,	Act.HIT,	Act.DOUBLE, Act.DOUBLE,	Act.DOUBLE, Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT 	}, //A4,A5
		{	Act.HIT,	Act.HIT,	Act.DOUBLE, Act.DOUBLE, Act.DOUBLE,	Act.DOUBLE, Act.HIT,	Act.HIT,	Act.HIT,	Act.HIT 	}, //A6
		{	Act.HIT,	Act.STAND,	Act.DOUBLE,	Act.DSTAND,	Act.DSTAND,	Act.DSTAND,	Act.STAND,	Act.STAND,	Act.HIT,	Act.HIT 	}, //A7
		{	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND,	Act.STAND 	} //A8+
	};
}