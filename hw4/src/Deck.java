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
		ArrayList<Card> copy = new ArrayList<Card>(deck);

        Random random = new Random();
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
}