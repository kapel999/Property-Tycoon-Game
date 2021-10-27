import Tiles.Tile;
import com.badlogic.gdx.graphics.g2d.Sprite;
import main.Player;
import misc.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TileTest {
    Tile tile;
    private Player player;
    ArrayList<Coordinate> coords;

    @Before
    public void setUp() {
        tile = new Tile();
        tile.setTileName("test");
    }

    @Test
    public void testGetTileName(){
        assertEquals("test",tile.getTileName());
    }

    @Test
    public void testSetTilePos(){
        tile.setTilePos(5);
        assertEquals(5,tile.getTilePos());
    }

    @Test
    public void addPlayer(){
        player = new Player("hello",new Sprite());
        tile.addPlayer(player);

        assertEquals(player,tile.getPlayers().get(0));
    }

    @Test
    public void removePlayer(){
        tile.addPlayer(player);
        tile.addPlayer(player);
        tile.removePlayer(player);
        assertEquals(null,tile.getPlayers().get(0));
    }

    @Test
    public void getCoordinates(){
        assertEquals(null,tile.getAllCoordinates());
    }
}