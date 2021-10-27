package Tiles;

import com.badlogic.gdx.graphics.Color;
import main.Player;
import misc.Coordinate;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.propertytycoonmakers.make.PropertyTycoon.players;

/**
 * the Property class implements functionality relating to a property in the game. It allows for the setting of rent,
 * cost and colour amongst functionality for buying and getting the owner.
 */
public class Property extends Ownable implements PropertyInterface {

    private String colour;
    private ArrayList<Integer> developmentPrices = new ArrayList<>();
    private int housePrice;
    private int hotelPrice;
    private int housesOwned;

    /**
     * The constructor for Property
     */
    public Property(){
        housesOwned = 0;
        colour = "white";
    }

    /**
     * setColour takes a String and sets that as the colour of the property
     * @param colour the colour of a property represented as a String
     */
    @Override
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * getColourAsString returns the colour of the Property as a string
     * @return the colour of the Property as a string
     */
    public String getColourAsString() {
        return colour;
    }

    /**
     * getColour returns the colour of the property as a Color object. This requires finding the Color object with the
     * associated String label in com.badlogic.gdx.graphics.Color
     * @return the Color object associated with the String label, Color.WHITE if not found
     */
    public Color getColor() {
        try {
            Field field = Class.forName("com.badlogic.gdx.graphics.Color").getField(colour);
            return (Color)field.get(null);
        } catch(Exception e) {
            return Color.WHITE;
        }
    }

    /**
     * sets the price of a house for the property
     * @param housePrice the price of the house
     */
    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }

    /**
     * @return returns the price of a house
     */
    public int getHousePrice() {
        return housePrice;
    }

    /**
     * sets the price of a hotel
     * @param hotelPrice the price required
     */
    public void setHotelPrice(int hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    /**
     *
     * @return returns the hotel price
     */
    public int getHotelPrice() {
        return hotelPrice;
    }

    /**
     * develops a house on the property
     */
    public void develop(){
        if(housesOwned < 4){
            housesOwned +=1;
            owner.makePurchase(housePrice);
        } else if(housesOwned == 4){
            housesOwned += 1;
            owner.makePurchase(hotelPrice);
        }
    }

    /**
     * @return returns the current rent for the property
     */
    public int getCurrentRent() {
        return developmentPrices.get(housesOwned);
    }

    /**
     * addDevPrice adds a house/hotel price to the ArrayList development prices
     * @param devPrice the house/hotel price to be added
     */
    @Override
    public void addDevPrice(int devPrice) {
        developmentPrices.add(devPrice);
    }

    /**
     * getDevPrice returns the ArrayList developmentPrices which stores the cost for development in increasing order.
     * I.e. developmentPrices.get(0) returns the cost to add 1 house, developmentPrices.get(4) returns the cost to add
     * a hotel
     * @return the ArrayList developmentPrices
     */
    public ArrayList<Integer> getDevPrices() {
        return developmentPrices;
    }

    /**
     * Checks if a property is owned, and if so, allows it to be sold by the player
     * @param player the player buying the property
     * @param cost the price of the property
     */
    @Override
    public void sellOwnable(Player player, int cost){
        if(owner == player && housesOwned == 0){
            player.payPlayer(cost);
            if (player.getOwnables().contains(this)) {
                player.removeOwnable(this);
            }
            owned = false;
            owner = null;
            setBuyable(true);
        }
        else if(owner == player && housesOwned >0){
            sellHouse();
        }
    }

    /**
     * sells a developed house on the property
     */
    public void sellHouse(){
        if(housesOwned <= 4 && housesOwned > 0){
            owner.payPlayer(housePrice/2);
            housesOwned -=1;
        }
        else if(housesOwned == 5){
            owner.payPlayer(hotelPrice/2);
            housesOwned -=1;
        }
    }

    /**
     * sets the coordinates of tiles on the GameBoard, this allows for mouse interactivity.
     * @param coordinates the arraylist of coordinates for each tile.
     */
    @Override
    public void setCoordinates(ArrayList<Coordinate> coordinates){
        playerPosCoordinates = new ArrayList<>();
        for(int i =0 ; i < players.size();i++) {
            if (i != 3 && i != 7 && i != 11) {
                playerPosCoordinates.add(coordinates.get(i));
            }
        }

        int tilePosition = 6; // used to determine which cell in a card is the label position one (makes it easier for us to change as we go)
        int propertyPosition = 7;// where the property png is rendered

        Coordinate labelCoordinate = new Coordinate(0,0);
        Coordinate tempPropertyCoordinate = new Coordinate(0,0);

        labelCoordinate.setCoordinate(coordinates.get(tilePosition).getX()+32,coordinates.get(tilePosition).getY()+32);
        tempPropertyCoordinate.setCoordinate(coordinates.get(propertyPosition).getX()+32,coordinates.get(propertyPosition).getY()+32);

        centerLabelCoordinate = labelCoordinate;
        ownableSpriteCoordinate = tempPropertyCoordinate;
        allCoordinates = coordinates;
    }

    /**
     * returns the number of houses on the property
     * @return
     */
    public int getHousesOwned(){
        return housesOwned;
    }
}