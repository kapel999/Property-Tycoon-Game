import main.Dice;
import org.junit.*;
import static org.junit.Assert.*;

public class DiceTest {

    private Dice dice;

    @Before
    public void setUp() {
        dice = new Dice();
        dice.reset();
        dice.rollDice();
    }

    @Test
    public void resetTest() {
        dice.reset();
        assertTrue(dice.getD1() == 0 && dice.getD2() == 0 && dice.wasItADouble() == false);
    }

    @Test
    public void rollDiceTest1() {
        assert(dice.getValue() >= 1 && dice.getValue() <= 12);
    }

    @Test
    public void rollDiceTest2() {
        assertFalse(dice.getValue() < 1 || dice.getValue() > 12);
    }

    /**@Test
    public void jailCheckTestTrue() {
        int localCounter = 0;
        while(localCounter != 3) {
            dice.rollDice();
            if(dice.getD1() == dice.getD2()) {
                localCounter += 1;
            }
        }
        assertTrue(dice.jailCheck());
    }**/

    @Test
    public void jailCheckTestFalse() {
        assertFalse(dice.jailCheck());
    }

    @Test
    public void wasItADoubleTest() {
        if (dice.getD1() == dice.getD2()) {
            assert (dice.wasItADouble() == true);
        } else {
            assert (dice.wasItADouble() == false);
        }
    }
}
