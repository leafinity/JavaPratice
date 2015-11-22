package abby.poker;

/**
 * Created by abby on 11/21/15.
 *
 * Play with unknown joker
 */
public class Donkey extends DonkeyWithNoJoker {
    static {_DECK_SIZE = 54;}
    public Donkey(int playerNum, boolean hasBonusGame, boolean interactive) {
        super(playerNum, hasBonusGame, interactive);
    }
}
