import foop.*;
import java.util.*;

public class Dealer {
	public Card faceDownCard;
	public ArrayList<Card> faceUpCards;

	public Dealer() {
		faceUpCards = new ArrayList();
	}
	
	public Hand getHand() {
		return new Hand(faceUpCards);
	}

	public void openCard() {
		faceUpCards.add(faceDownCard);
	}

	public Card sneak() {
		return faceDownCard;
	}

	public Card getOpenCard() {
		return faceUpCards.get(0);
	}

}