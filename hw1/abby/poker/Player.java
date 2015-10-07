package abby.poker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by abby on 9/27/15.
 */
public class Player {
    private List<Card> _hand;
    private int _ID;

    public Player(int id) {
        _hand = new ArrayList<Card>();
        _ID = id;
    }

    public int get_ID() {
        return _ID;
    }

    public void setNewCard(List<Card> cards) {
        _hand.addAll(cards);
    }

    public void setNewCard(Card card) {
        _hand.add(card);
    }

    public void initHand() {
        _hand.sort(Comparator.<Card>naturalOrder());
        ArrayList<Card> tmp = new ArrayList<Card>(_hand);
        int idx = 0;
        while (idx < tmp.size() - 1) {
            if (tmp.get(idx).get_suit() == Card.Suit.RED
                    || tmp.get(idx).get_suit() == Card.Suit.BLACK) {
                idx++;
            }
            if (tmp.get(idx).get_rank() == tmp.get(idx + 1).get_rank()) {
                _dropThePair(tmp.get(idx), tmp.get(idx + 1));
                idx += 2;
            } else {
                idx++;
            }
        }
    }

    public void showHand() {
        _hand.sort(Comparator.<Card>naturalOrder());
        System.out.print("Player" + _ID + ": ");
        for (Card card : _hand) {
            System.out.print(card);
            if (_hand.indexOf(card) != _hand.size() - 1)
                System.out.print(" ");
        }
        System.out.println();
    }

    public Card drawCard(Player player2) {
        Random random = new Random();
        int next = random.nextInt(player2._hand.size());
        Card card = player2._hand.get(next);

        player2._beDraw(card);
        matchAndDrop(card);

        return card;
    }

    private void matchAndDrop(Card newCard) {
        boolean hasSameCard = false;
        Card sameCard = new Card(Card.Suit.CLUB, 1);
        for (Card card : _hand) {
            if (card.get_suit() == Card.Suit.RED || card.get_suit() == Card.Suit.BLACK)
                continue;
            if (card.get_rank() == newCard.get_rank()) {
                hasSameCard = true;
                sameCard = card;
                break;
            }
        }

        if (hasSameCard) {
            _dropThePair(sameCard, newCard);
        } else {
            _hand.add(newCard);
        }
    }

    public boolean isWin() {
        if(_hand.size() == 0)
            return true;
        return false;
    }


    private void _dropThePair(Card card1, Card card2) {
        _hand.remove(card1);
        _hand.remove(card2);
    }

    private void _beDraw(Card removed) {
        _hand.remove(removed);
    }

}
