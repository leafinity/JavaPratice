import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by abby on 10/26/15.
 */
public class Computer {
    static private int _DECK_SIZE = 52;

    private Shuffler _shuffler;
    private List<Card> _deck;

    static private int[][] PAYOFF_TABLE = {
            {0, 1, 2, 3, 4, 6, 9, 25, 50, 250},
            {0, 2, 4, 6, 8, 12, 18, 50, 100, 500},
            {0, 3, 6, 9, 12, 18, 27, 75, 150, 750},
            {0, 4, 8, 12, 16, 24, 36, 100, 200, 1000},
            {0, 5, 10, 15, 20, 30, 45, 125, 250, 4000}};

    public Computer() {
        _shuffler = new Shuffler();
        _fillDeck();
    }

    public void shuffleDeck() {
        _deck = _shuffler.shuffle(_deck);
    }

    public void shuffleDeckSp(HandKind hk) {
        _deck = _shuffler.shuffleForSpecialCase(hk, _deck);
    }

    public List<Card> dealCard(int num) {
        List<Card> dealCard = new ArrayList<Card>();
        if (_deck.size() < num) {
            _fillDeck();
            _deck = _shuffler.shuffle(_deck);
        } else {
            while ((num--) > 0) {
                dealCard.add(_deck.get(0));
                _deck.remove(0);
            }
        }
        return dealCard;
    }

    public int pay(int bet, HandKind handkind) {
        return PAYOFF_TABLE[bet - 1][handkind.get_value()];
    }

    private void _fillDeck() {
        _deck = new ArrayList<Card>();
        for(int i = 0; i < _DECK_SIZE; i++) {
            _deck.add(new Card(i));
        }
    }

    public enum HandKind {
        OTHERS(0), JACKS_OR_BETTER(1), TOW_PAIR(2), THREE_OF_A_KIND(3),
        STRAIGHT(4), FLUSH(5), FULL_HOUSE(6), FOUR_OF_A_KIND(7),
        STRAIGHT_FLUSH(8), ROYAL_FLUSH(9);

        private int _value;

        public int get_value() {
            return _value;
        }

        private HandKind(int value) {
            _value = value;
        }

        static public HandKind parse(List<Card> hand) {
        /*sort hand*/
            hand.sort(Comparator.<Card>naturalOrder());
        /*check if there are any pair */
            int pairNum = 0;
            int ranksNum = 1;
            int pairValue = 0;
        /*compute pair num*/
            for (int index = 0; index < 4; index++) {
                if (hand.get(index).get_rank()
                        == hand.get(index + 1).get_rank()) {
                    pairNum++;
                    index++;
                    pairValue = hand.get(index).get_rank();//for case JOB
                }
            }

        /*compute rank num*/
            for (int index = 0; index < 4; index++) {
                if (hand.get(index).get_rank()
                        != hand.get(index + 1).get_rank()) {
                    ranksNum++;
                }
            }

            if (pairNum == 2 && ranksNum == 2) {
                if (hand.get(1).get_rank() == hand.get(3).get_rank())
                    return FOUR_OF_A_KIND;
                return FULL_HOUSE;
            } else if (pairNum == 2 && ranksNum == 3) {
                return TOW_PAIR;
            } else if (pairNum == 1) {
                switch (ranksNum) {
                    case 3:
                        return THREE_OF_A_KIND;
                    default:
                        if ((pairValue <= 13 && pairValue >= 11) && pairValue == 1)
                            return JACKS_OR_BETTER;
                        else
                            return OTHERS;
                }
            } else {
            /*check whether the hand is straight*/
            /*take out the rank of each card of hand*/
                List<Integer> ranks = new ArrayList();
                hand.forEach((card) -> {
                    ranks.add(card.get_rank());
                });
            /*check regular case: form 1-5 to 9-13*/
                boolean isStraight = isConsequent(ranks);
            /*check the cross K-1 case*/
                if(!isStraight) {
                    for (int i = 0; i < 5; i++) {
                        if(ranks.get(i) < 5)
                            ranks.set(i, ranks.get(i) + 13);
                    }
                    ranks.sort(Comparator.<Integer>naturalOrder());
                    isStraight = isConsequent(ranks);
                }
            /*already sure whether hand is straight or not*/
            /*yes-> check is Flush*/
            /*no-> others*/
                if (isStraight) {
                    if (isFlush(hand)) {
                        if(ranks.get(0) == 10) {
                            return ROYAL_FLUSH;
                        } else {
                            return STRAIGHT_FLUSH;
                        }
                    } else {
                        return STRAIGHT;
                    }
                } else {
                    if (isFlush(hand))
                        return FLUSH;
                    return OTHERS;
                }
            }
        }

        static private boolean isFlush(List<Card> hand) {
            boolean isFlush = true;
            for (int i = 0; i < 4; i++) {
                if(hand.get(i).get_suit() != hand.get(i + 1).get_suit()) {
                    isFlush = false;
                    break;
                }
            }
            return isFlush;
        }

        static private boolean isConsequent(List<Integer> list) {
            boolean isConsequent = true;
            for (int i = 0; i < 4; i++) {
                if(list.get(i) + 1 != list.get(i + 1)) {
                    isConsequent = false;
                    break;
                }
            }
            return isConsequent;
        }

    }
}

