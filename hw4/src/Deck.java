import foop.*;
import java.util.*;

public class Deck {
	static int _DECK_SIZE = 52;
	private ArrayList<Card> deck;
	private ArrayList<Card> assignedCards;

	public Deck() {
		deck = new ArrayList<Card>();
		assignedCards = new ArrayList<Card>();

		byte[] suits = { 
    		Card.CLUB, 
			Card.DIAMOND, 
			Card.HEART,
			Card.SPADE 
		};
		
		for (int i = 0; i < _DECK_SIZE; i++) {
			deck.add(new Card(suits[i / 13], (byte)((i % 13) + 1)));
		}
	}

	public void shuffle() {
		deck.addAll(assignedCards);
        assignedCards.clear();
assert deck.size() == _DECK_SIZE:"deck: " + printDeck();
		ArrayList<Card> copy = new ArrayList<Card>(deck);

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        Set<Integer> randomSet = new LinkedHashSet<Integer>();

        while (randomSet.size() < _DECK_SIZE) {
            Integer next = random.nextInt(_DECK_SIZE);
            randomSet.add(next);
        }
        Integer[] nonRepeatArray = randomSet.toArray(new Integer[randomSet.size()]);

        for (int i = 0; i < _DECK_SIZE; i++) {
            deck.set(i,copy.get(nonRepeatArray[i]));
        }
	}

	public Card take() {
		Card card = deck.get(0);
		deck.remove(0);
		assignedCards.add(card);
		return card;
	}

	public int getNRemain() {
		return deck.size();
	}

	private String printDeck() {
		StringBuilder sb = new StringBuilder();
		for(Card card: deck) {
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
			sb.append(" ");
		}
		return sb.toString();
	}
}