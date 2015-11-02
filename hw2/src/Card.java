/**
 * Created by abby on 10/26/15.
 */
public class Card implements Comparable<Card> {

    public enum Suit {
        RED, BLACK, CLUB, DIAMOND, HEART, SPADE;
        // declaration suits, and which be declared fist is the smallest by default of enum.

        static int num = 4;
        @Override
        public String toString() {
            String string = super.toString();
            return string.substring(0, 1);
        }

        static public Suit getBySequence(int sequence) {
            switch (sequence) {
                case 0:
                    return CLUB;
                case 1:
                    return DIAMOND;
                case 2:
                    return HEART;
                case 3:
                    return SPADE;
                default:
                    throw new IllegalArgumentException("get Suit: "
                            + Integer.toString(sequence));
            }
        }
    }

    private final Suit _suit;
    private final int _rank;

    public Card(Suit suit, int number) {
        if (suit == Suit.BLACK || suit == Suit.RED) {
            if (number != 0)
                throw new IllegalArgumentException("Joker: " + number);
        } else if (number < 1 || number > 13) {
            throw new IllegalArgumentException("Regular Card: " + Integer.toString(number));
        }
        _suit = suit;
        _rank = number == 1? 14: number;
    }

    public Card(int sequence) {
        if (sequence == 52) {
            _suit = Suit.RED;
            _rank = 0;
        } else if (sequence == 53) {
            _suit = Suit.BLACK;
            _rank = 0;
        } else {
            _suit = Suit.getBySequence(sequence / 13);
            _rank = sequence % 13 + 1;
        }
    }


    public Suit get_suit() {
        return _suit;
    }

    public int get_rank() {
        return _rank;
    }

    @Override
    public String toString() {
        String rank;
        if (_rank == 13) {
            rank = "K";
        } else if (_rank == 12) {
            rank = "Q";
        } else if (_rank == 11) {
            rank = "J";
        } else if (_rank == 14) {
            rank = "A";
        }  else if (_rank == 10) {
            rank = "T";
        } else {
            rank = Integer.toString(_rank);
        }
        return _suit.toString() + rank;
    }

    @Override
    public int compareTo(Card anotherCard) {
        if (_rank != anotherCard._rank) {
            return _rank - anotherCard._rank;
        } else {
            return _suit.compareTo(anotherCard._suit);
        }
    }
}
