import Tiles.Property;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import main.Player;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;

public class PropertyTest {

    private Property p;
    private Player player;

    @Before
    public void setUp(){
        p = new Property();
        player = new Player("Jeff", new Sprite());
    }

    @Test
    public void getColourAsStringTest(){
        p.setColour("BROWN");
        assertEquals("BROWN", p.getColourAsString());
    }

    @Test
    public void getColorTest1() {
        p.setColour("BROWN");
        assert(p.getColor() == Color.BROWN);
    }

    @Test
    public void getColourTest2() {
        p.setColour("BROWN");
        assertFalse(p.getColor() == Color.YELLOW);
    }

    @Test
    public void getColorTest3() {
        p.setColour("BROWN");
        assert(p.getColor() instanceof  Color);
    }

    @Test
    public void getHousePriceTest() {
        p.setHousePrice(50);
        assertEquals(50, p.getHousePrice());
    }

    @Test
    public void getHotelPriceTest() {
        p.setHotelPrice(50);
        assertEquals(50, p.getHotelPrice());
    }

    @Test
    public void testDevelop() {
        p.buyOwnable(player, 50);
        p.develop();
        assertEquals(1, p.getHousesOwned());
    }

    @Test
    public void testDevelop2() {
        p.buyOwnable(player, 50);
        for(int i=0; i<3; i++) {
            p.develop();
        }
        assertEquals(3, p.getHousesOwned());
    }

    @Test
    public void testDevelop3() {
        p.buyOwnable(player, 50);
        assertEquals(0, p.getHousesOwned());
    }

    @Test
    public void testDevelop4() {
        p.buyOwnable(player, 50);
        for(int i=0; i<10; i++) {
            p.develop();
        }
        assertEquals(5, p.getHousesOwned());
    }


    @Test
    public void testGetCurrentRent() {
        p.buyOwnable(player, 50);
        p.develop();
        System.out.println(p.getHousesOwned());
        p.addDevPrice(0); //this is the index where the standard rent price is stored
        p.addDevPrice(50);
        assertEquals(50, p.getCurrentRent());
    }

    @Test
    public void testGetCurrentRent2() {
        p.buyOwnable(player, 50);
        for(int i=0; i<2; i++) {
            p.develop();
        }
        p.addDevPrice(0); //this is the index where the standard rent price is stored
        p.addDevPrice(50);
        p.addDevPrice(500);
        p.addDevPrice(4);
        assertEquals(500, p.getCurrentRent());
    }

    @Test
    public void testAdd_GetDevPrice() {
        p.addDevPrice(0);
        p.addDevPrice(500);
        assert(p.getDevPrices().get(0) == 0);
        assert(p.getDevPrices().get(1) == 500);
    }

    @Test
    public void testAdd_GetDevPrice2() {
        p.addDevPrice(-5000);
        p.addDevPrice(200);
        assert(p.getDevPrices().get(0) == -5000);
        assertFalse(p.getDevPrices().get(0) == 4999);
        assert(p.getDevPrices().get(1) == 200);
    }

    @Test
    public void testSellOwnable(){
        p.buyOwnable(player, 0);
        p.sellOwnable(player, -200);
        assertEquals(1300, player.getMoney());
    }

    @Test
    public void testSellOwnable2(){
        p.sellOwnable(player, 200);
        assertEquals(1500, player.getMoney());
    }

    @Test
    public void testSellOwnable3() {
        p.buyOwnable(player, 200);
        p.sellOwnable(player, 200);
        assertEquals(1500, player.getMoney());
    }

    @Test
    public void testSellHouse() {
        p.buyOwnable(player, 50);
        p.setHousePrice(50);
        p.develop();
        p.sellHouse();
        assertEquals(1425, player.getMoney());
    }

    @Test
    public void testSellHouse2() {
        p.buyOwnable(player, 50);
        p.setHousePrice(50);
        p.develop();
        p.develop();
        p.sellHouse();
        assertEquals(1375, player.getMoney());
    }

    @Test
    public void testGetHousesOwned() {
        p.buyOwnable(player, 50);
        p.setHousePrice(50);
        p.develop();
        p.develop();
        assertEquals(2, p.getHousesOwned());
    }

    @Test
    public void testGetHousesOwned2() {
        p.buyOwnable(player, 50);
        p.setHousePrice(50);
        p.develop();
        p.develop();
        p.sellHouse();
        p.sellHouse();
        p.sellHouse();
        assertEquals(0, p.getHousesOwned());
    }

    @Test
    public void testGetHousesOwned3() {
        p.buyOwnable(player, 50);
        p.setHousePrice(50);
        p.develop();
        p.develop();
        p.sellHouse();
        assertEquals(1, p.getHousesOwned());
    }
}