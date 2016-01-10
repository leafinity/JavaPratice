import foop.*;
import java.util.*;

public class Dealer {
	public Card faceDownCard;
	public ArrayList<Card> faceUpCards;

	public Dealer() {
		faceUpCards = new ArrayList<Card>();
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

	public void cleanUp() {
		faceUpCards.clear();
	}

	public String printHand() {
		StringBuilder sb = new StringBuilder();
		for (Card c: faceUpCards) {
			sb.append(printCard(c));
			sb.append(" ");
		}

		return "Dealer Hand:[ " + sb.toString() + "] " + ComputeHand.getHTotalValue(getHand());
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