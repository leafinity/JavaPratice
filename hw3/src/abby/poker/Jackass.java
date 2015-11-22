package abby.poker;

/**
 * Created by abby on 11/21/15.
 *
 * Play with one joker
 */
public class Jackass extends OldMain{
    static {_DECK_SIZE = 53;}

    public Jackass(int playerNum, boolean hasBonusGame, boolean interactive) {
        super(playerNum, hasBonusGame, interactive);
    }
}
