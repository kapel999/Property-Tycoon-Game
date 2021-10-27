package main;

import Tiles.Ownable;
import Tiles.Property;
import com.badlogic.gdx.graphics.g2d.Sprite;
import misc.Coordinate;

import java.util.ArrayList;

/**
 * PLayer is a class that represents the players of the game. It has functionality for all basic functions a player
 * is able to carry out. A list of players is initialised in GameSetUpScreen.
 */
public class Player implements PlayerInterface {

    private ArrayList<Ownable> ownables;
    private int balance;
    private int tilePosition;
    private int getOutJailCards;
    private boolean isInJail;
    private Sprite gameToken;
    private String name;
    private boolean firstLap;
    private Coordinate currentCoordinates;

    /**
     * The constructor for Player
     * @param name a String that is the name of the player
     * @param token a Sprite that is the game token that represents the player in the GUI
     */
    public Player(String name, Sprite token) {
        this.name = name;
        this.gameToken = token;
        setInJail(false);
        ownables = new ArrayList<>();
        getOutJailCards = 0;
        balance = 1500;
        firstLap = false;
    }

    /**
     * isBot gives a boolean judgement as to whether the instance of the class is Player's subclass Bot, or not
     * @return true if the instance of the class is a Bot, false otherwise
     */
    @Override
    public boolean isBot() {
        return this instanceof Bot;
    }

    /**
     * addGetOutOfJailFreeCard gives the player a get out of jail free card when called
     */
    @Override
    public void addGetOutOfJailFreeCard(){
        getOutJailCards +=1;
    }

    /**
     * removeGetOutOfJailFreeCard removes a get out of jail free card from the player when called
     */
    @Override
    public void removeGetOutOfJailFreeCard(){
        if(getOutJailCards > 0) {
            getOutJailCards -= 1;
        }
    }

    /**
     * getOutOfJailFree will return true if player has at least one card and false otherwise
     * @return returns true or false depending if "get out of jail" card is present or not
     */
    @Override
    public boolean hasGetOutOfJailFree() {
        return getOutJailCards > 0;
    }

    /**
     * setInJail takes a Boolean parameter that defines whether or not a player is in jail
     * @param isInJail true if player is in jail, false otherwise
     */
    @Override
    public void setInJail(boolean isInJail) {
        this.isInJail = isInJail;
    }

    /**
     * getIsInJail returns a Boolean judgement as to whether a player is in jail or not
     * @return true if player is in jail, false otherwise
     */
    @Override
    public boolean getIsInJail() {
        return isInJail;
    }

    /**
     * getName returns the name of a player as a String
     * @return a String representation of the player's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * getPlayerToken will return the Sprite object associated with that player that represents the game piece
     * @return returns the Sprite gameToken
     */
    @Override
    public Sprite getPlayerToken() {
        return gameToken;
    }

    /**
     * setPlayerToken takes a Sprite object and sets the player's token as that Sprite object. Used for GUI display
     * @param token the Sprite object to be set as the player's token
     */
    @Override
    public void setPlayerToken(Sprite token) {
        gameToken = token;
    }

    /**
     * setTilePosition takes an Integer representation of the position of the player on the board and sets that value as
     * tilePosition
     * @param i an Integer representation of where the player is on the board
     */
    @Override
    public void setTilePosition(int i) {
        tilePosition = i;
    }

    /**
     * getTilePosition returns the Integer representation of the player position on the board
     * @return an Integer representation of where the player is on the board
     */
    @Override
    public int getTilePosition() {
        return tilePosition;
    }

    /**
     * getOwnables returns an array list with all the ownables that the player owns
     * @return returns ownables: an ArrayList with all the ownables the player owns
     */
    @Override
    public ArrayList<Ownable> getOwnables() {
        return ownables;
    }

    /**
     * getTotalOwnableValue calculates and returns the total worth of all ownables a player owns
     * @return the total worth of all ownables a player owns as an int
     */
    @Override
    public int getTotalOwnableValue() {
        int sum = 0;
        for(Ownable o: getOwnables()) {
            sum += o.getCost();
        }
        return sum;
    }

    /**
     * addOwnables adds a Ownable object to the array list of Ownable Tile objects
     * @param ownable
     */
    @Override
    public void addOwnable(Ownable ownable) {
        if(!ownables.contains(ownable)) {
            ownable.addPlayer(this);
            ownables.add(ownable);
        }
    }

    /**
     * removeOwnable removes an Ownable object from the array list of Ownable Tile objects
     * @param ownable the Ownable object to be removed
     */
    @Override
    public void removeOwnable(Ownable ownable) {
        if(ownable.getOwner() != null) {
            ownable.removePlayer(this);
        }
        ownables.remove(ownable);
    }

    /**
     * getMoney returns an integer that represents the current balance of the player (how much money they has)
     * @return returns balance as an integer
     */
    @Override
    public int getMoney() {
        return balance;
    }

    /**
     * setMoney takes an amount of money represented by an integer and sets that as the player's balance
     */
    @Override
    public void setMoney(int amount) {
        balance = amount;
    }

    /**
     * payPlayer takes an amount of money represented by an integer and adds this to the player's balance
     */
    @Override
    public void payPlayer(int amount) {
        balance += amount;
    }

    /**
     * makePurchase takes a cost represented as an integer and subtracts this from the player's balance
     * @param cost an Integer representation of the cost of a purchase
     */
    @Override
    public void makePurchase(int cost) {
        balance -= cost;
    }

    /**
     * endFirstLap sets firstLap: a Boolean representation of whether or not the player has gone around the board once,
     * as false.
     */
    @Override
    public void setFirstLap(boolean lap) {
        this.firstLap = lap;
    }

    /**
     * getFirstLap returns a Boolean judgement as to whether the player has gone around the board once
     * @return true if the player is on their first lap, false otherwise
     */
    @Override
    public boolean completedFirstLap() {
        return firstLap;
    }

    /**
     * getCurrentCoordinates returns a Coordinate object that represents the coordinates of a player on the board
     * @return a Coordinate object that represents the coordinates of a player on a board
     */
    @Override
    public Coordinate getCurrentCoordinates() {
        return currentCoordinates;
    }

    /**
     * setCurrentCoordinates takes Coordinate object that represents the new coordinates of a player and sets the
     * player's current coordinates to be that Coordinate object
     * @param currentCoordinate a Coordinate object that represents the new coordinates of a player
     */
    @Override
    public void setCurrentCoordinates(Coordinate currentCoordinate) {
        this.currentCoordinates = currentCoordinate;
    }

    /**
     * getWealth calculates a players total wealth, taking into account the worth of properties, houses/hotels placed
     * on them along with the worth of other ownables such as utilities and stations
     * @return the total wealth of a player as an int
     */
    @Override
    public int getWealth() {
        int wealth = 0;
        for(Ownable o : ownables) {
            wealth += o.getCost();
            if(o instanceof Property){
                wealth += ((Property) o).getHousesOwned() * ((Property) o).getHousePrice();
            }
        }
        wealth += balance;
        return wealth;
    }
}