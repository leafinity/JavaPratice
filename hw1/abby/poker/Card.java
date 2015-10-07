package abby.poker;

/**
 * Created by abby on 9/27/15.
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
        /* for regular compareTo
        if (_suit.compareTo(anotherCard._suit) > 0) {
            return 1;
        } else if (_suit.compareTo(anotherCard._suit) == 0) {
            return _rank - anotherCard._rank;
        } else {
            return -1;
        }

        */
        if (_rank != anotherCard._rank) {
            return _rank - anotherCard._rank;
        } else {
            return _suit.compareTo(anotherCard._suit);
        }
    }



}
