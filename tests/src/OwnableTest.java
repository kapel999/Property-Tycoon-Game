import Tiles.Ownable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import main.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OwnableTest {

    private Ownable testOwn;
    private Player testPlayer;
    private static Sprite testSprite;

    @Before
    public void setUp(){
        testOwn = new Ownable();
        testPlayer = new Player("testy", testSprite);

    }

    @Test
    public void buyableTest(){
        testOwn.setBuyable(true);
        assertEquals(true, testOwn.getBuyable());
    }

    @Test
    public void costTest(){
        testOwn.setCost(100);
        assertEquals(100, testOwn.getCost());
    }

    @Test
    public void ownerTest(){
        testOwn.buyOwnable(testPlayer, 0);
        assertEquals(testOwn.getOwner(), testPlayer);
        assertEquals(true, testOwn.getOwned());
    }

    @Test
    public void mortgageTest(){
        testOwn.buyOwnable(testPlayer, 0);
        testOwn.setMortgaged(testPlayer, 0);
        assertEquals(true,testOwn.getMortgaged());
        testOwn.unmortgage(testPlayer,0);
        assertEquals(false, testOwn.getMortgaged());
    }

    @Test
    public void priceTest(){
        testOwn.setCost(200);
        assertEquals(200, testOwn.getCost());

    }
}