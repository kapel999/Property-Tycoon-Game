import Tiles.FreeParking;
import com.badlogic.gdx.graphics.g2d.Sprite;
import main.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FreeParkingTest {

    private FreeParking testParking;


    private Player testPlayer;
    private static Sprite testSprite;

    @Before
    public void setUp(){
        testPlayer = new Player("testy", testSprite);
        testParking = new FreeParking();

    }

    @Test
    public void payParkingTest(){
        testParking.addToPot(100);
        testParking.addToPot(50);
        assertEquals(150, testParking.getCurrentValue());

    }

    @Test
    public void resetParkingTest(){

        testParking.setCurrentValue(200);
        testParking.reset();
        assertEquals(0,testParking.getCurrentValue());
    }


}
