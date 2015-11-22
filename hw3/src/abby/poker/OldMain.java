package abby.poker;

import java.util.*;

/**
 * Created by abby on 9/27/15.
 */
public class OldMain {
    protected static int _DECK_SIZE = 54;
    protected static int _REGULAR_DECK_SIZE = 52;
    protected static boolean _IS_DONKEY = false;

    protected List<Player> _playerList;
    protected int _initPlayerNum;
    private boolean _bonus;
    protected Card[] _deck;
    private Stage _stage;
    private boolean _interactive;
    protected List<Card> _drop;

    private enum Stage {
        BASIC, BONUS, OVER;

        private Stage nextStage(boolean somebodyWin, List<Player> playerList, boolean bonus) {
            if (somebodyWin) {
                if (this.compareTo(BASIC) == 0) {
                    System.out.println("Basic game over");
                    if (bonus) {                    //there is 3 player and two player win at the same time 
                        if (playerList.size() <= 1) {
                            System.out.println("Bonus game over");
                            return OVER;
                        }
                        return BONUS;
                    } else {
                        return OVER;
                    }
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

    public OldMain(int playerNum, boolean hasBonusGame, boolean interactive) {
        _initPlayerNum = playerNum;
        if(playerNum == 1) {
            throw new IllegalArgumentException("there should br more than 1 player");
        }else if(playerNum == 2) {
            _bonus = false;
        } else {
            _bonus = hasBonusGame;
        }
        _playerList = new ArrayList<Player>();
        _deck = new Card[_DECK_SIZE];
        _stage = Stage.BASIC;
        _interactive = interactive;
        _drop = new ArrayList<Card>();
    }

    public void start() {
        _initGame();
        System.out.println("Game start");
        while (_interactive?_gameRoundWithInterActive():_gameRound());

        //test for correction
        _playerList.forEach(player -> {
            _drop.removeAll(player.drop);
            _drop.removeAll(player.get_hand());
        });
        if(!gameRunCorrectly()) {
            System.out.println("There must be some bug");

            System.out.print("_drop: ");
            _drop.forEach(card-> {System.out.print(card.toString()+ " ");});
            System.out.println();
        }
    }

    private boolean _gameRound() {
        Player to = _getPlayerTo();
        Player from = _getPlayerFrom();
        Card draw = to.drawCard(from);
        System.out.println("Player" + to.get_ID()
                + " draws a card from Player" + from.get_ID() + " " +  draw.toString());
        to.showHand();
        from.showHand();
        return _isKeepGoing(to, from);
    }

    private boolean _gameRoundWithInterActive() {
        Player to = _getPlayerTo();
        Player from = _getPlayerFrom();
        int beforeSize = to.getHandSize();
        if (to.get_ID() == 0) {
            Card draw = to.drawSpecifiedCard(from);
            System.out.println("You draw a card from Player" + from.get_ID() + " " +  draw.toString());
            if (beforeSize > to.getHandSize()) {
                System.out.println("You drop a pair of card of " + draw.get_rankString());
            }
        } else if (from.get_ID() == 0) {
            Card draw = to.drawCard(from);
            System.out.println("Player" + to.get_ID()
                    + " draws a card from you " + draw.toString());
            if (beforeSize > to.getHandSize()) {
                System.out.println("Player" + to.get_ID() + " drop card" + draw.get_rank());
                System.out.println("Player" + to.get_ID() + " drop card" + draw.get_rank());
            }
        } else {
            Card draw = to.drawCard(from);
            System.out.println("Player" + to.get_ID()
                    + " draws a card from Player" + from.get_ID());
            if (beforeSize > to.getHandSize()) {
                System.out.println("Player" + to.get_ID() + " drop card" + draw.get_rank());
            }
        }
        to.showHand();
        from.showHand();
        return _isKeepGoing(to, from);
    }


    private Player _getPlayerTo() {
        return _playerList.get(0);
    }

    private Player _getPlayerFrom() {
        return _playerList.get(1);
    }

    private boolean _isKeepGoing(Player to, Player from) {
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
            if (_interactive &&  ID1 == 0)
                System.out.println("Player" + ID2 + " and you" + " win");
            else
                System.out.println("Player" + ID1 + " and Player" + ID2 + " win");

            //remove playerTo and playerFrom
            _popPlayer();
            _popPlayer();
            
            //for test correction
            _drop.removeAll(to.drop);
            _drop.removeAll(from.drop);
        } else if (to.isWin()) {
            if (_interactive &&  to.get_ID() == 0)
                System.out.println("you win");
            else
                System.out.println("Player" + to.get_ID() + " wins");

            //remove playerTo and playerFrom
            _popPlayer();
            
            //for test correction
            _drop.removeAll(to.drop);
        } else if (from.isWin()) {
            if (_interactive &&  from.get_ID() == 0)
                System.out.println("you win");
            else
                System.out.println("Player" + from.get_ID() + " wins");

            //remove playerTo and playerFrom, add back PlayerTo
            Player p = _popPlayer();
            _popPlayer();
            _pushPlayer(p);

            //for test correction
            _drop.removeAll(from.drop);
        } else {
            somebodyWin = false;

            //remove playerTo, and add back PlayerTo
            Player p = _popPlayer();
            _pushPlayer(p);
        }
        return somebodyWin;
    }

    protected void _initGame() {
        _initPlayer();
        _initDeck();
        System.out.println("Deal cards");
        _dealCard(_shuffle());
        _showPlayersHand();
        System.out.println("Drop cards");
        _initHands();
        _showPlayersHand();
    }

    private void _initPlayer() {
        for (int i = 0; i < _initPlayerNum; i++) {
            _playerList.add(new Player(i, _IS_DONKEY, _interactive));
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
        if(_DECK_SIZE > _REGULAR_DECK_SIZE)
            _deck[_REGULAR_DECK_SIZE] = new Card(Card.Suit.RED, 0);
        if(_DECK_SIZE > _REGULAR_DECK_SIZE + 1)
            _deck[_REGULAR_DECK_SIZE + 1] = new Card(Card.Suit.BLACK, 0);

        //for test correction
        _drop.addAll(Arrays.asList(_deck));
    }

    protected void _dealCard(List<Integer> nonRepeatArray) {
        int playerNum = _playerList.size();
        int handSize = _DECK_SIZE / playerNum;
        int restSize = _DECK_SIZE % playerNum;
        for(int i = 0; i < _DECK_SIZE; i++) {
            _playerList.get(i % playerNum).setNewCard(_deck[nonRepeatArray.get(i)]);
        }
    }

    protected void _dealCard(List<Integer> nonRepeatArray, int newDeckSize) {
        int playerNum = _playerList.size();
        int handSize = newDeckSize / playerNum;
        int restSize = newDeckSize % playerNum;
        for(int i = 0; i < newDeckSize; i++) {
            _playerList.get(i % playerNum).setNewCard(_deck[nonRepeatArray.get(i)]);
        }
    }

    private List<Integer> _shuffle() {
        //shuffle: get a non-repeated array(set)
        Random random = new Random();
        Set<Integer> randomSet = new LinkedHashSet<>();
        while (randomSet.size() < _DECK_SIZE) {
            Integer next = random.nextInt(_DECK_SIZE);
            // Set will automatically do a containment check
            randomSet.add(next);
        }
        List<Integer> shuffledSeq = new ArrayList();
        shuffledSeq.addAll(randomSet);
        return shuffledSeq;
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

    protected boolean gameRunCorrectly() {
        return _drop.isEmpty();
    }
}
