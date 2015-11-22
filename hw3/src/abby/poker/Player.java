package abby.poker;

import java.util.*;

/**
 * Created by abby on 9/27/15.
 */
public class Player {
    private List<Card> _hand;
    private int _ID;
    private boolean _isDonkey;
    private boolean _interactive = false;
    public List<Card> drop;

    public Player(int id, boolean isDonkey, boolean interactive) {
        _hand = new ArrayList<Card>();
        _ID = id;
        _isDonkey = isDonkey;
        _interactive = interactive;
        drop = new ArrayList<Card>();
    }

    public int get_ID() {
        return _ID;
    }

    public List<Card> get_hand() {
        return _hand;
    }

    public void setNewCard(List<Card> cards) {
        _hand.addAll(cards);
    }

    public void setNewCard(Card card) {
        _hand.add(card);
    }

    public void initHand() {
        _hand.sort(Comparator.<Card>naturalOrder());
        ArrayList<Card> tmp = new ArrayList(_hand);
        int idx = 0;
        while (idx < tmp.size() - 1) {
            if (!_isDonkey) {
                if (tmp.get(idx).get_suit() == Card.Suit.RED
                        || tmp.get(idx).get_suit() == Card.Suit.BLACK) {
                    idx++;
                }
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
        if (_interactive && _ID == 0 || !_interactive) {
            if (_interactive)
                System.out.print("You: ");
            else
                System.out.println("Player" + _ID + ": ");
            for (Card card : _hand) {
                System.out.print(card);
                if (_hand.indexOf(card) != _hand.size() - 1)
                    System.out.print(" ");
            }
            System.out.println();
        } else {
            System.out.println("Player" + _ID + " has " + getHandSize() + " cards");
        }
    }

    public void showDrop() {
        drop.sort(Comparator.<Card>naturalOrder());
        if (_interactive && _ID == 0)
            System.out.print("You drop: ");
        else
            System.out.println("Player" + _ID + " drop : ");
        for (Card card : drop) {
            System.out.print(card);
            if (drop.indexOf(card) != drop.size() - 1)
                System.out.print(" ");
        }
        System.out.println();
    }

    public int getHandSize() {
        return _hand.size();
    }

    public Card drawCard(Player player2) {
        Random random = new Random();
        int next = random.nextInt(player2._hand.size());
        Card card = player2._hand.get(next);

        player2._beDraw(card);
        matchAndDrop(card);

        return card;
    }

    public Card drawSpecifiedCard(Player player2) {
        int range = player2.getHandSize();
        Scanner scanner = new Scanner(System.in);
        //produce options
        Random random = new Random();
        Set<Integer> randomSet = new LinkedHashSet<>();
        while (randomSet.size() < range) {
            Integer next = random.nextInt(range);
            randomSet.add(next);
        }
        List<Integer> option = new ArrayList();
        option.addAll(randomSet);

        int choice;

        System.out.println("Player" + player2.get_ID() + " have " + range + " cards.");
        System.out.println("Pick which you want to draw.");
        while (true) {
            try {
                choice = scanner.nextInt() - 1;
                if (choice < 0 || choice >= range) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.println("Please enter an Integer between 1~" + range + ".");
                continue;
            }
            break;
        }
        Card card  = player2._hand.get(option.get(choice));
        player2._beDraw(card);
        matchAndDrop(card);

        return card;
    }

    private void matchAndDrop(Card newCard) {
        boolean hasSameCard = false;
        //initialize [sameCard] for compiler
        Card sameCard = new Card(Card.Suit.CLUB, 1);
        for (Card card : _hand) {
            if (!_isDonkey && (card.get_suit() == Card.Suit.RED || card.get_suit() == Card.Suit.BLACK))
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
        drop.add(card1);
        drop.add(card2);
    }

    private void _beDraw(Card removed) {
        _hand.remove(removed);
    }

}
