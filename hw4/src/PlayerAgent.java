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
		_faceUpCards = new ArrayList();
		_splitedCards = new ArrayList();

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
		_faceUpCards.removeAll(new ArrayList(_faceUpCards));
		_splitedCards.removeAll(new ArrayList(_splitedCards));
		_buyInsurance = false;
		_surrender = false;
	}


 	public void make_bet(ArrayList<Hand> last_table, int total_player) {
 		_bet = _player.make_bet(last_table, total_player, _position);
 	}
	
	public void buy_insurance(Card dealer_open, ArrayList<Hand> current_table) {
		_buyInsurance = _player.buy_insurance(_faceUpCards.get(0), dealer_open, current_table);
		if (_buyInsurance)
			increase_chips(0.5 * _bet);
	}

	public void	do_surrender(Card dealer_open, ArrayList<Hand> current_table) {
		_surrender = _player.do_surrender(_faceUpCards.get(0), dealer_open, current_table);
	}
	
	public boolean do_double(Card dealer_open, ArrayList<Hand> current_table, boolean splited) {
		if (_player.do_double(getHand(), dealer_open, current_table)) {
			if (hasSplited()) {
				_splitedBet = _bet*2;
			} else {
				_bet *= 2;
			}
			return true;
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
			return true;
		}
		return false;
	}

	public boolean hit_me(Card dealer_open, ArrayList<Hand> current_table, boolean splited) {
		return _player.hit_me(new Hand(_faceUpCards), dealer_open, current_table);
	}

	public void decrease_chips(double diff) {
		try {
			_player.decrease_chips(diff);
		} catch(Player.NegativeException e) {
			// the bet is negative -> break rule
			_hasBrokenRule = true;
		} catch(Player.BrokeException e) {
			_isBroken = true;
		}
	}

	public void increase_chips(double diff) {
		try {
			_player.increase_chips(diff);
		} catch(Player.NegativeException e) {
			_hasBrokenRule = true;
		}
	}

	public String toString() {
		return "";
	}
}