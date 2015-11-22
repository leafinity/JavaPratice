package abby.poker;

import java.util.*;

/**
 * Created by abby on 11/21/15.
 *
 * Play with unknown joker
 */
public class DonkeyWithNoJoker extends OldMain {
    static {_DECK_SIZE = 52;_IS_DONKEY=true;}
    protected Card donkey;

    public DonkeyWithNoJoker(int playerNum, boolean hasBonusGame, boolean interactive) {
        super(playerNum, hasBonusGame, interactive);
    }

    @Override
    protected void _dealCard(List<Integer> nonRepeatArray){
        donkey = _deck[nonRepeatArray.get(0)];
        nonRepeatArray.remove(0);
        super._dealCard(nonRepeatArray, _DECK_SIZE - 1);
    }

    @Override
    public void start() {
        super.start();
        System.out.println("Donkey is " + donkey.toString());
    }
}
