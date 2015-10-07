package abby.poker;

import java.util.*;

/**
 * Created by abby on 9/27/15.
 */
public class OldMain {
    private static int _DECK_SIZE = 54;
    private static int _REGULAR_DECK_SIZE = 52;
    private static int _RED_JOKER_POS = 52;
    private static int _BLACK_JOKER_POS = 53;

    private List<Player> _playerList;
    private int _initPlayerNum;
    private boolean _bonus;
    private Card[] _deck;
    private Stage _stage;
    private int _round;

    private enum Stage {
        BASIC, BONUS, OVER;

        public Stage nextStage(boolean somebodyWin, List<Player> playerList, boolean bonus) {
            if (somebodyWin) {
                if (this.compareTo(BASIC) == 0) {
                    System.out.println("Basic game over");
                    if (bonus) {
                        System.out.println("Continue");
                        return BONUS;
                    }
                    else
                        return OVER;
                } else {
                    if(playerList.size() == 1) {
                        System.out.println("Bonus game over");
                        return OVER;
                    }
                    else
                        return BONUS;
                }
            } else {
                return this;
            }
        }
    }

    public OldMain(int playerNum, boolean hasBonusGame) {
        _initPlayerNum = playerNum;
        _bonus = hasBonusGame;
        _playerList = new ArrayList<Player>();
        _deck = new Card[_DECK_SIZE];
        _stage = Stage.BASIC;
        _round = 0;
    }

    public void start() {
        _initGame();
        System.out.println("Game start");
        while (_gameRound());
    }

    private boolean _gameRound() {
        Player to = _getPlayerTo();
        Player from = _getPlayerFrom();
        Card draw = to.drawCard(from);
        System.out.println("Player" + to.get_ID()
                + " draws a card from Player" + from.get_ID() + " " +  draw.toString());
        to.showHand();
        from.showHand();
        _round++;
        return _keepGoing(to, from);
    }

    private Player _getPlayerTo() {
        return _playerList.get(0);
    }

    private Player _getPlayerFrom() {
        return _playerList.get(1);
    }

    private boolean _keepGoing(Player to, Player from) {
        _stage = _stage.nextStage(_isSomebodyWin(to, from), _playerList, _bonus);
        return _stage.compareTo(Stage.OVER) != 0;
    }

    private boolean _isSomebodyWin(Player to, Player from) {
        boolean somebodyWin = true;
        if (to.isWin() && from.isWin()) {
            int ID1 = to.get_ID() > from.get_ID()?
                    from.get_ID(): to.get_ID();
            int ID2 = to.get_ID() < from.get_ID()?
                    from.get_ID(): to.get_ID();
            System.out.println("Player" + ID1 + " and Player" + ID2 + " win");

            //remove playerTo and playerFrom
            _popPlayer();
            _popPlayer();
        } else if (to.isWin()) {
            System.out.println("Player" + to.get_ID() + " wins");

            //remove playerTo and playerFrom
            _popPlayer();
        } else if (from.isWin()) {
            System.out.println("Player" + from.get_ID() + " wins");

            //remove playerTo and playerFrom, add back PlayerTo
            Player p = _popPlayer();
            _popPlayer();
            _pushPlayer(p);

        } else {
            somebodyWin = false;

            //remove playerTo, and add back PlayerTo
            Player p = _popPlayer();
            _pushPlayer(p);
        }
        return somebodyWin;
    }

    private void _initGame() {
        _initPlayer();
        _initDeck();
        System.out.println("Deal cards");
        _dealCard();
        _showPlayersHand();
        System.out.println("Drop cards");
        _initHands();
        _showPlayersHand();
    }

    private void _initPlayer() {
        for (int i = 0; i < _initPlayerNum; i++) {
            _playerList.add(new Player(i));
        }
    }


    private void _initDeck() {
        ArrayList<Card.Suit> suits = new ArrayList<Card.Suit>(4);
        suits.add(Card.Suit.CLUB);
        suits.add(Card.Suit.DIAMOND);
        suits.add(Card.Suit.HEART);
        suits.add(Card.Suit.SPADE);

        for(int i = 0; i < _REGULAR_DECK_SIZE; i++) {
            _deck[i] = new Card(suits.get(i % Card.Suit.num), (i / Card.Suit.num + 1));
        }
        // add Jokers
        _deck[_RED_JOKER_POS] = new Card(Card.Suit.RED, 0);
        _deck[_BLACK_JOKER_POS] = new Card(Card.Suit.BLACK, 0);
    }

    private void _dealCard(){
        //shuffle: get a non-repeated array(set)
        Random random = new Random();
        Set<Integer> randomSet = new LinkedHashSet<Integer>();
        while (randomSet.size() < _DECK_SIZE) {
            Integer next = random.nextInt(_DECK_SIZE);
            // Set will automatically do a containment check
            randomSet.add(next);
        }
        Integer[] nonRepeatArray = randomSet.toArray(new Integer[randomSet.size()]);

        //deal
        int playerNum = _playerList.size();
        int handSize = _DECK_SIZE / playerNum;
        int restSize = _DECK_SIZE % playerNum;
        for(int i = 0; i < _DECK_SIZE; i++) {
            _playerList.get(i % playerNum).setNewCard(_deck[nonRepeatArray[i]]);
        }

    }

    private Player _popPlayer() {
        Player p = _playerList.get(0);
        _playerList.remove(p);
        return p;
    }

    private void _pushPlayer(Player p) {
        _playerList.add(p);
    }

    private void _initHands() {
        _playerList.forEach(player -> player.initHand());
    }

    private void _showPlayersHand() {
        _playerList.forEach(player -> player.showHand());
    }
}
