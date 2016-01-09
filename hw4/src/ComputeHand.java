import java.util.*;
import foop.*;

public class ComputeHand {
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

	public static boolean isBlackJack(Hand hand) {
		if (hand.getCards().size() == 2 || getHTotalValue(hand) == 21)
			return true;
		return false;
	}


	public static boolean isBusted(Hand hand) {
		return ComputeHand.getLTotalValue(hand) > 21;
	}
}