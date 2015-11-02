import java.util.List;
import java.util.Scanner;

/**
 * Created by abby on 10/26/15.
 */
public class Player {
    private String _name;
    private Integer _PDollarNum;
    private Scanner usrInput;
    public List<Card> hand;


    private List<Card> _get;

    public Player(String name, int PNum) {
        _name = name;
        _PDollarNum = PNum;
    }

    public String get_name() {
        return _name;
    }

    public void bet(int num) {
        _PDollarNum -= num;
    }

    public void earn(int num) {
        _PDollarNum += num;

    }

    public int get_PDollarNum() {
        return _PDollarNum;
    }
}
