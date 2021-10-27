package Tiles;

public class FreeParking extends BigTiles {

    private int currentValue;

    /**
     * the constructor for the FreeParking tile
     */
    public FreeParking() {
        currentValue = 0 ;
        tileName = "Free Parking";
    }

    /**
     * gets how much money is in FreeParking
     * @return currentValue - the current amount of money in FreeParking
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * sets the currentvalue to a certain price - used for testing
     * @param currentValue the value to change FreeParking to
     */
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * adds an amount of money to the pot
     * @param amount the amount of money to add
     */
    public void addToPot(int amount){
        currentValue += amount;
    }

    /**
     * resets the value of FreeParking
     */
    public void reset(){
        currentValue = 0;
    }
}