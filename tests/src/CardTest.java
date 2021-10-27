
import misc.Card;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CardTest {
    private Card card;


    @Before
    public void setUp() {
        card = new Card();
        card.setAction("Do this action");
        card.setValue(5);
    }

    @Test
    public void testGetValue(){
        assertEquals(5, card.getValue());
    }

    @Test
    public void testGetAction(){
        assertEquals("Do this action", card.getAction());
    }

    @Test
    public void testGetMessage(){
        assertEquals("Do this action $5", card.getCardMessage());
    }

    @Test
    public void testGetMessage2(){
        card.setAction("Go back to");
        assertEquals("Go back to", card.getCardMessage());
    }

    @Test
    public void testSetActionAndValue(){
        card.setAction("Pay me");
        card.setValue(10);
        assertEquals("Pay me $10", card.getCardMessage());
    }

}
