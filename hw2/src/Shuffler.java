import java.util.*;

/**
 * Created by abby on 10/26/15.
 */
public class Shuffler {
    static private int _DECK_SIZE = 52;

    public List<Card> shuffle(List<Card> deck) {
        Random random = new Random();
        Set<Integer> randomSet = new LinkedHashSet<Integer>();

        while (randomSet.size() < _DECK_SIZE) {
            Integer next = random.nextInt(_DECK_SIZE);
            randomSet.add(next);
        }
        Integer[] nonRepeatArray = randomSet.toArray(new Integer[randomSet.size()]);

        List<Card> shuffled = new ArrayList<Card>();
        for (int i = 0; i < _DECK_SIZE; i++) {
            shuffled.add(deck.get(nonRepeatArray[i]));
        }
        return shuffled;
    }

    public List<Card> shuffleForSpecialCase(Computer.HandKind handKind, List<Card> deck) {
        List<Card> shuffled = shuffle(deck);
        int[] firstFive;
        switch (handKind) {
            case ROYAL_FLUSH:
                firstFive = new int[]{0, 9, 10, 11, 12};
                break;
            case STRAIGHT_FLUSH:
                firstFive = new int[]{12, 0, 1, 2, 3};
                break;
            case FOUR_OF_A_KIND:
                firstFive = new int[]{1, 14, 27, 40, 2};
                break;
            case FULL_HOUSE:
                firstFive = new int[]{1, 14, 28, 2, 15};
                break;
            case FLUSH:
                firstFive = new int[]{1,3,5,7,9};
                break;
            case STRAIGHT:
                firstFive = new int[]{1, 28, 42, 4, 18};
                break;
            case THREE_OF_A_KIND:
                firstFive = new int[]{1, 14, 27, 2, 3};
                break;
            case TOW_PAIR:
                firstFive = new int[]{1, 14, 2, 15, 3};
                break;
            case JACKS_OR_BETTER:
                firstFive = new int[]{1, 2, 3, 10, 23};
                break;
            default:
                firstFive = new int[]{1, 14, 3, 4, 5};
        }

        for(int i = 0; i < 5; i++) {
            shuffled.set(i, new Card(firstFive[i]));
        }
        return shuffled;
    }
}
