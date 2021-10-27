import misc.Coordinate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



public class CoordinateTest {

    Coordinate coord;

    @Before
    public void setUp() {
        coord = new Coordinate(3,0);
    }

    @Test
    public void testGetX(){
        assertEquals(3*64, coord.getX());
    }

    @Test
    public void testGetY(){
        coord.setCoordinate(0,1);
        assertEquals(1,coord.getY());
    }
    @Test
    public void testSetCoordinates(){
        coord.setCoordinate(2,1);
        assertEquals(2,coord.getX());
    }

}
