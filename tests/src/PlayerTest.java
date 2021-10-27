import Tiles.Property;
import com.badlogic.gdx.graphics.g2d.Sprite;
import main.Bot;
import main.Player;

import misc.Coordinate;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PlayerTest{
	private static Sprite testSprite;
	private static Player testPlayer;
	private Property testProperty;
	private Coordinate testCoord;

	@Before
	public void setUp(){
		testSprite = new Sprite();
		testPlayer = new Player("testy", testSprite);
		testPlayer.setMoney(1500);
		testProperty = new Property();
		testProperty.setTileName("testHouse");
		testProperty.setCost(200);
	}

	@Test
	public void moneyTest(){
		testPlayer.setMoney(2000);
		testPlayer.payPlayer(200);
		assertEquals(testPlayer.getMoney(),2200);
	}

	@Test
	public void nameTest(){
		assertEquals(testPlayer.getName(), "testy");

	}

	@Test
	public void testBuying(){
		testPlayer.addOwnable(testProperty);
		testPlayer.makePurchase(testProperty.getCost());
		assertEquals(testPlayer.getOwnables().get(0), testProperty);
		assertEquals(testPlayer.getMoney(), 1300);
	}


	@Test
	public void testSelling(){
		testPlayer.addOwnable(testProperty);
		assertEquals(testPlayer.getOwnables().get(0), testProperty);
		testPlayer.removeOwnable(testProperty);
		assertEquals(testPlayer.getOwnables().size(), 0);
	}

	@Test
	public void testInJail(){
		testPlayer.setInJail(true);
		assertEquals(testPlayer.getIsInJail(), true);

	}
	@Test
	public void botTest(){
		assertEquals(false, testPlayer instanceof Bot);

	}

	@Test
	public void testGOJFcards(){
		testPlayer.addGetOutOfJailFreeCard();
		assertTrue(testPlayer.hasGetOutOfJailFree());

	}

	@Test
	public void multipleGOJFcards(){
		testPlayer.addGetOutOfJailFreeCard();
		testPlayer.addGetOutOfJailFreeCard();
		testPlayer.removeGetOutOfJailFreeCard();
		assertTrue(testPlayer.hasGetOutOfJailFree());

	}

	@Test
	public void jailPlayer(){
		testPlayer.setInJail(true);
		assertTrue(testPlayer.getIsInJail());

	}

	@Test
	public void releasePlayer(){
		testPlayer.setInJail(true);
		testPlayer.setInJail(false);
		assertFalse(testPlayer.getIsInJail());

	}

	@Test
	public void getPlayerSpriteTest(){
		assertEquals(testSprite, testPlayer.getPlayerToken());
	}

	@Test
	public void ownablesListTest(){
		testPlayer.addOwnable(testProperty);
		assertEquals(testProperty,testPlayer.getOwnables().get(0));
	}

	@Test
	public void firstLapTest(){
		assertFalse(testPlayer.completedFirstLap());
		testPlayer.setFirstLap(true);
		assertTrue(testPlayer.completedFirstLap());
	}

	@Test
	public void addOwnableTest(){
		testPlayer.addOwnable(testProperty);
		assertEquals(1, testPlayer.getOwnables().size());
	}

	@Test
	public void removeOwnableTest(){
		testPlayer.addOwnable(testProperty);
		testPlayer.removeOwnable(testProperty);
		assertEquals(0, testPlayer.getOwnables().size());

	}

	@Test
	public void coordinateTest(){
		testPlayer.setCurrentCoordinates(testCoord);
		assertEquals(testCoord, testPlayer.getCurrentCoordinates());

	}


}
