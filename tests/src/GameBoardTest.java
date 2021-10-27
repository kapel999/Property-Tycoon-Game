
import com.badlogic.gdx.graphics.g2d.Sprite;
import main.GameBoard;
import main.Player;
import org.junit.Before;
import java.util.ArrayList;
import java.util.Arrays;

public class GameBoardTest {
    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    private Sprite sprite4;
    private Sprite sprite5;
    private Sprite sprite6;
    private ArrayList<Sprite> spriteList;

    private GameBoard testBoard;
    private ArrayList<Player> testPlayers;

    @Before
    public void setUp() {

        this.sprite1 = new Sprite();
        this.sprite2 = new Sprite();
        this.sprite3 = new Sprite();
        this.sprite4 = new Sprite();
        this.sprite5 = new Sprite();
        this.sprite6 = new Sprite();
        this.spriteList = new ArrayList<>(Arrays.asList(sprite1, sprite2, sprite3, sprite4, sprite5, sprite6));

        int i = 0;
        for (Sprite x : this.spriteList) {
            testPlayers.add(new Player("player " + i, x));
            i++;
        }

        testBoard = new GameBoard(testPlayers);
    }
}