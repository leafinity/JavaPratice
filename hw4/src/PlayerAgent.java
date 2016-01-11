import java.util.*;
import foop.*;

public class PlayerAgent {
	private String _name;
	private int _position;
	private Player _player;

	/*information of each round*/
	private int _bet;
	private int _splitedBet;
	private boolean _buyInsurance;
	private boolean _surrender;
	private boolean _double;
	private Card _faceDownCard;
	private ArrayList<Card> _faceUpCards;
	private ArrayList<Card> _splitedCards;

	private boolean _isBroken;
	private boolean _hasBrokenRule;

		
	public PlayerAgent(String name, int position, int nChip) {
		_name = name;
		_position = position;
			
		// new player
		try{
			String className = "Player" + name;
			// String className = "foop.PlayerB01902030";
			_player = (Player) Class.forName(className).getConstructor(Integer.TYPE).newInstance(nChip);
		} catch(Exception e) {
			System.out.println("new player failed: ");
			e.printStackTrace();
		}
		// _player = new PlayerB01902080(nChip);

		//initail settings
		_buyInsurance = false;
		_surrender = false;
		_faceUpCards = new ArrayList<Card>();
		_splitedCards = new ArrayList<Card>();

		_isBroken = false;
		_hasBrokenRule = false;
	}

	public Hand getHand() {
		return new Hand(_faceUpCards);
	}

	public Hand getSplitedHand() {
		return new Hand(_splitedCards);
	}

	public int getBet() {
		return _bet;
	}	

	public int getSplitedBet() {
		return _splitedBet;
	}

	public boolean hasInsurance() {
		return _buyInsurance;
	}

	public boolean hasSurrender() {
		return _surrender;
	}

	public boolean hasSplited() {
		return _splitedCards.size() > 0;
	}

	public boolean beKickOff() {
		return _isBroken || _hasBrokenRule;
	}

	public void setFaceDownCard(Card card) {
		_faceDownCard = card;
	}

	public void openFaceDownCard() {
		assignCard(_faceDownCard, false);
	}

	public void assignCard(Card card, boolean splited) {
		if (!splited) {
			_faceUpCards.add(card);
		} else {
			_splitedCards.add(card);
		}
	}

	public void cleanUp() {
		_faceUpCards.clear();
		_splitedCards.clear();
		_buyInsurance = false;
		_surrender = false;
	}


 	public void make_bet(ArrayList<Hand> last_table, int total_player) {
 		/*make bet*/
 		_bet = _player.make_bet(last_table, total_player, _position);
 		if (_bet == 0) {
			System.out.println(_name + _position + ", you cannot bet 0.");
			System.out.println(_name + _position + " is kick of.");
		} else {
 			System.out.println(_name + _position + " bets " + _bet + ".");
 		}
 	}
	
	public void buy_insurance(Card dealer_open, ArrayList<Hand> current_table) {
		_buyInsurance = _player.buy_insurance(_faceUpCards.get(0), dealer_open, current_table);
		if (_buyInsurance) {
			increase_chips(0.5 * _bet);
 			System.out.println(_name + _position + " buys insurance.");
 		}
	}

	public void	do_surrender(Card dealer_open, ArrayList<Hand> current_table) {
		_surrender = _player.do_surrender(_faceUpCards.get(0), dealer_open, current_table);
 		if (_surrender)
 			System.out.println(_name + _position + " surrenders.");
	}
	
	public boolean do_double(Card dealer_open, ArrayList<Hand> current_table, boolean splited) {
assert splited || getHand().getCards().size() == 2: printHand(false);
assert !splited || getSplitedHand().getCards().size() == 2: "splited: " + printHand(true);
		if (splited) {
			if (_player.do_double(getSplitedHand(), dealer_open, current_table)) {
				_splitedBet = _splitedBet * 2;
 				System.out.println("double down.");
				return true;
			} 
		} else {
			if (_player.do_double(getHand(), dealer_open, current_table)) {
				_bet = _bet * 2;
 				System.out.println("double down.");
				return true;
			} 
		}
		return false;
	}

	public boolean do_split_if_can(Card dealer_open, ArrayList<Hand> current_table) {
		if (_faceUpCards.get(0) != _faceUpCards.get(1))
			return false;
		if (_player.do_split(_faceUpCards, dealer_open, current_table)){
			_splitedCards.add(_faceUpCards.get(1));
			_faceUpCards.remove(1);
			_splitedBet = _bet;
			System.out.println(_name + _position + " splited.");
			return true;
		}
		return false;
	}

	public boolean hit_me(Card dealer_open, ArrayList<Hand> current_table, boolean splited) {
		boolean hit = _player.hit_me(new Hand(_faceUpCards), dealer_open, current_table);
		if (hit) {
 			System.out.println("hit.");
 			return true;
		} else  {
 			System.out.println("stand.");
 			return false;
		}
	}

	public void decrease_chips(double diff) {
		try {
			_player.decrease_chips(diff);
			System.out.println(_name + _position + " lose " + diff + ".");
			System.out.println(printChip());
		} catch(Player.NegativeException e) {
			// the bet is negative -> break rule
			_hasBrokenRule = true;
			System.out.println(_name + _position + " bet negative bet.");
			System.out.println(_name + _position + " is kick of.");
		} catch(Player.BrokeException e) {
			_isBroken = true;
			System.out.println(_name + _position + " has broken.");
			System.out.println(_name + _position + " is kick of.");
		}
	}

	public void increase_chips(double diff) {
		try {
			_player.increase_chips(diff);
			System.out.println(_name + _position + " earn " + diff + ".");
			System.out.println(printChip());
		} catch(Player.NegativeException e) {
			_hasBrokenRule = true;
			System.out.println(_name + _position + " bet negative bet.");
			System.out.println(_name + _position + " is kick of.");
		}
	}

	public boolean checkBroken() {
		// check > 1.0
		try {
			_player.decrease_chips(1.0);
		} catch(Player.BrokeException e) {
			System.out.println(_name + _position + " has broken.");
			System.out.println(_name + _position + " is kick of.");
			return true;
		} catch(Player.NegativeException e) {
			//should nerver happen
		}
		//add back 1.0
		try {
			_player.increase_chips(1.0);
		} catch(Player.NegativeException e) {
			//should nerver happen
		}
		return false;
	}

	public String toString() {
		return _name + _position;
	}

	public String printHand(boolean splited) {
		StringBuilder sb = new StringBuilder();
		for (Card c: splited? _splitedCards: _faceUpCards) {
			sb.append(printCard(c));
			sb.append(" ");
		}

		return "Hand:[ " + sb.toString() + "] " + ComputeHand.getHTotalValue(getHand());
	}

	public String printChip() {
		return _name + _position + " " + _player.toString();
	}

	private String printCard(Card card) {
		StringBuilder sb = new StringBuilder();
		switch(card.getSuit()) {
			case Card.CLUB:
				sb.append("C");
				break;
			case Card.DIAMOND:
				sb.append("D");
				break;
			case Card.HEART:
				sb.append("H");
				break;
			case Card.SPADE:
				sb.append("S");
				break;
		}

		switch(card.getValue()) {
			case 10:
				sb.append("T");
				break;
			case 11:
				sb.append("J");
				break;
			case 12:
				sb.append("Q");
				break;
			case 13:
				sb.append("K");
				break;
			case 1:
				sb.append("A");
				break;
			default:
				sb.append(card.getValue());
		}
		return sb.toString();
	}
}