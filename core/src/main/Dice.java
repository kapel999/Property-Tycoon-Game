package main;

import java.util.Random;

/**
 * The dice class simulates a pair of two six sided dice to roll whenever a player moves
 */
public class Dice implements DiceInterface {

    private int counter;
    private int d1;
    private int d2;
    private boolean wasDouble;

    /**
     * the constructor for Dice
     */
    public Dice() {
        counter = 0;
    }

    /**
     * reset sets all three variables to their initial state
     */
    @Override
    public void reset() {
        counter = 0;
        d1 = 0;
        d2 = 0;
        wasDouble = false;
    }

    /**
     * rollDice returns true if its a double (both rolls come out with the same number) and false otherwise.
     * With each roll counter is incremented by one
     */
    @Override
    public void rollDice() {
        Random r = new Random();
        d1 = r.nextInt(5) + 1;
        d2 = r.nextInt(5) + 1;
        counter++;
        if(d1 == d2) {
            wasDouble =true;
        }
        else {
            wasDouble=false;
        }
    }

    /**
     * jailCheck will return true if both rolls are the same number and the counter is equal to three and false otherwise.
     * @return returns true or false depending if player rolled three doubles or not
     */
    @Override
    public boolean jailCheck() {
        if((d1 == d2) && (counter == 3)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * getValue will return the summed up value of the two values rolled by the dices
     * @return returns the total value of the two dices
     */
    @Override
    public int getValue() {
        return d1 + d2;
    }

    /**
     * getD1 returns the value of the first die as an int
     * @return the value of the first die
     */
    @Override
    public int getD1() {
        return d1;
    }

    /**
     * getD2 returns the value of the second die as an int
     * @return the value of the second die
     */
    @Override
    public int getD2() {
        return d2;
    }

    /**
     * wasItADouble returns a Boolean judgement as to whether a the dice rolled a double
     * @return true if the dice rolled a double, false otherwise
     */
    @Override
    public boolean wasItADouble(){
        return wasDouble;
    }
}