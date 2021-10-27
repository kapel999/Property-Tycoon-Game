package main;

import Tiles.*;
import misc.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  The GameBoard class simulates the board and all the physical aspects of the board such as player position, cards and the dice.
 */
public class GameBoard implements GameBoardInterface {

    private static Tile[] board;
    private static ArrayList<Card> potluckCards;
    private static ArrayList<Card> opportunityKnocksCards;
    private static Dice dice;
    private static Map<Player, Integer> playerPos;
    private static Player currentPlayer;
    private int goPayoutAmount;
    private ArrayList<Player> players;
    private Map<String, ArrayList<Ownable>> identityOwnablesMap;
    private ArrayList<Property> developedProperties;
    private int lastD1Rolled;
    private int lastD2Rolled;
    private Card lastCardPulled;

    /**
     * The GameBoard class constructor
     * @param players Holds each player object who is in the game
     */
    public GameBoard(ArrayList<Player> players) {
        goPayoutAmount = 200;
        playerPos = new HashMap<>();
        this.players = players;
        // sets all players position to GO tile at 0
        for (Player player : players) {
            playerPos.put(player, 0);
        }
        ConfigTranslator builder = new ConfigTranslator(); // This ceases to exist after initialization
        board = builder.getTiles();
        identityOwnablesMap = new HashMap<>();
        for(String identity : builder.getTileIdentities()) {
            ArrayList<Ownable> ownables = new ArrayList<>();
            for(Tile tile : board) {
                if(tile instanceof Property && ((Property) tile).getColourAsString().equals(identity)) {
                    ownables.add((Ownable) tile);
                } else if(tile instanceof Station && identity == "STATION"){
                    ownables.add((Ownable) tile);
                } else if(tile instanceof Utility && identity == "UTILITY"){
                    ownables.add((Ownable) tile);
                }
            }
            identityOwnablesMap.put(identity, ownables);
        }
        dice = new Dice();
        potluckCards = builder.getPotluckChestCards();
        Collections.shuffle(potluckCards);
        opportunityKnocksCards = builder.getOpportunityCards();
        Collections.shuffle(opportunityKnocksCards);
        developedProperties = new ArrayList<>();
    }

    /**
     * getIdentityOwnablesMap returns the mapping between an identity and the list of Ownables that have that identity
     * @return the Map object identityOwnablesMap
     */
    @Override
    public Map<String, ArrayList<Ownable>> getIdentityOwnablesMap() {
        return identityOwnablesMap;
    }

    /**
     * handles dice rolling and landing on tile functions.
     * @param player the player to take the turn
     * @return returns checkBoardCircumstances()
     */
    @Override
    public Boolean playerTurn(Player player) {
        currentPlayer = player;

        dice.rollDice();
        if(player.getIsInJail()) {
            if(dice.wasItADouble() || player.hasGetOutOfJailFree()) {
                player.setInJail(false);
                movePlayer(player, dice.getValue());
            }
        }
        else {
            movePlayer(player, dice.getValue());
        }

        lastD1Rolled = dice.getD1();
        lastD2Rolled = dice.getD2();
        return checkBoardCircumstances();
    }

    /**
     * getPlayerPos is used to return the current position of any given player
     * @param player the player who's position is being searched
     * @return the position of Player player
     */
    @Override
    public int getPlayerPos(Player player) {
        return playerPos.get(player);
    }

    /**
     * setPlayerPos is used to set a players position to a point given
     * @param player The player to move
     * @param pos    Where to move the player
     */
    @Override
    public void setPlayerPos(Player player, int pos) {
        board[playerPos.get(player)].removePlayer(player);
        playerPos.put(player, pos);
        board[pos].addPlayer(player);
        player.setTilePosition(pos);
        Tile tile = board[playerPos.get(player)];
        if(player.getIsInJail() && pos == 10 && tile instanceof Jail){
            player.setCurrentCoordinates((((Jail) tile).getNextJailCoordinate()));
        } else{
        player.setCurrentCoordinates(tile.getAvailableCoordinates());
        }
    }

    /**
     * movePlayer uses context to tell how far a player should move, and what space to move them to
     * @param player The player to move
     * @param moves  how many spaces to move the player
     */
    @Override
    public void movePlayer(Player player, int moves) {
        int position = getPlayerPos(player);
        int moveTo = position + moves;

        if (moveTo > 39) {
            //change this based on go tile amount set (for now 200)
            player.payPlayer(goPayoutAmount);
            player.setFirstLap(true);
            this.setPlayerPos(player, moveTo - 40);

            if (moveTo < 0) {
                this.setPlayerPos(player, moveTo + 40);
            }
        }
        else {
            this.setPlayerPos(player, moveTo);
        }
    }

    /**
     * getTile gets the tile at int i
     * @param i tile position
     * @return tile at int i
     */
    @Override
    public Tile getTile(int i) {
        return board[i];
    }

    /**
     * sendToJail sends the current player to the jail tile and sets their inJail to true
     */
    @Override
    public void sendToJail(Player player) {
        player.setInJail(true);
        setPlayerPos(player, 10);
    }

    /**
     * checkBoardCircumstances checks to see what tile the player is on and handles their functionality also checks if
     * doubles are rolled to roll again.
     * @return returns true if the last roll was a double
     */
    @Override
    public Boolean checkBoardCircumstances() {
        Tile x = board[playerPos.get(currentPlayer)];
        if(x instanceof Property){
                if(((Property) x).getOwned() && !((Property) x).getMortgaged() && !((Property) x).getOwner().getIsInJail()){
                    currentPlayer.makePurchase(((Property) x).getCurrentRent());
                    ((Property) x).getOwner().payPlayer(((Property) x).getCurrentRent());
            }
        }else if(x instanceof Services) {
            if (((Ownable) x).getOwned() && !((Ownable) x).getOwner().getIsInJail()) {
                int rentMultiplier = howManyStationUtilityDoesPlayerOwn(((Ownable) x).getOwner(), (Services) x);
                if (rentMultiplier > 0) {
                    currentPlayer.makePurchase(50 * rentMultiplier);
                    ((Ownable) x).getOwner().payPlayer(50 * rentMultiplier);
                } else {
                    currentPlayer.makePurchase(50);
                    ((Ownable) x).getOwner().payPlayer(50);
                }
            }
        }else if (x instanceof OpportunityKnocks) {
            drawOpportunityKnocksCard();
        }
        else if (x instanceof PotLuck) {
            drawPotluckCard();
        }
        else if (x instanceof FreeParking) {
            currentPlayer.payPlayer(((FreeParking) board[20]).getCurrentValue());
        }
        else if (x instanceof Tax) {
            currentPlayer.makePurchase(((Tax) x).getTaxAmount());
            ((FreeParking) board[20]).addToPot(((Tax) x).getTaxAmount());
        }
        else if (x instanceof GoToJail) {
            sendToJail(currentPlayer);
        }
        else if (x instanceof Go) {
            currentPlayer.payPlayer(goPayoutAmount);
        }
        if (dice.jailCheck()) {
            sendToJail(currentPlayer);
        }
        else if (dice.wasItADouble()) {
            return true;
        }
        dice.reset();
        return false;
    }

    /**
     * performCardAction takes a Card object and handles the action that card should perform using a switch statement
     * @param card the card who's action is being performed
     */
    @Override
    public void performCardAction(Card card) {
        Integer[] counts = getNumberOfHousesOwned(currentPlayer);
        switch (card.getAction()) {
            case "pay": // Bank pays player
            case "You have won 2nd prize in a beauty contest, collect":
            case "You inherit" : // Bank pays player - Inherits
            case "Student loan refund. Collect": // Student loan - bank pays player
            case "Bank error in your favour. Collect": // Bank error - bank pays player
            case "From sale of Bitcoin you get": // Sale - bank pays player
            case "Savings bond matures, collect": // Savings - bank pays player
            case "Received interest on shares of":
            case "Bank pays you dividend of":
            case "You have won a lip sync battle. Collect":
            case "Loan matures, collect":
                currentPlayer.payPlayer(card.getValue());
                break;

            case "Go back to":// Go back to Crapper Street
            case "Advance to": //Advance to Turing Heights
                setPlayerPos(currentPlayer, card.getValue());
                if(card.getValue() == 11 || card.getValue() == 15 || card.getValue() == 24 && currentPlayer.getTilePosition() >= card.getValue()) { // 11 Skywalker Drive Tile Pos - 15 Han Xin Gardens Pos - 24 Hove Station Pos
                    currentPlayer.payPlayer(goPayoutAmount);
                }
                checkBoardCircumstances();
                break;

            case "Pay bill for text books of"://Player pays bill
            case "Mega late night taxi bill pay": // Player pays bank
            case "Pay university fees of":
                currentPlayer.makePurchase(card.getValue());
                break;

            case "Advance to Go": // go to Go tile
                setPlayerPos(currentPlayer,0);
                currentPlayer.payPlayer(goPayoutAmount);
                break;

            case "Go to jail. Do not pass Go": // sends player to jail
                sendToJail(currentPlayer);
                break;

            case "It's your birthday. Each player pays you":// Each player pays current player
                for (Player player: players) {

                    if(player != currentPlayer) {
                        player.makePurchase(card.getValue());
                        currentPlayer.payPlayer(card.getValue());
                    }
                }
                break;

            case "Get out of jail free":
                currentPlayer.addGetOutOfJailFreeCard();
                break;

            case "Pay insurance fee of":
            case "Fined for speeding, pay":
            case "Drunk in charge of a skateboard. Fine":
                currentPlayer.makePurchase(card.getValue());
                ((FreeParking) board[20]).addToPot(card.getValue());
                break;

            case "Go back 3 spaces":
                movePlayer(currentPlayer, -3);
                checkBoardCircumstances();
                break;

            case "You are assessed for repairs, $25/house, $100/hotel":
                currentPlayer.makePurchase(counts[0]*25);
                currentPlayer.makePurchase(counts[1]*100);
                break;

            case "You are assessed for repairs, $40/house, $115/hotel":
                currentPlayer.makePurchase(counts[0]*40);
                currentPlayer.makePurchase(counts[1]*115);
                break;

            default:
        }
    }

    /**
     * getLastD1 returns the last value of the first die. Is used for displaying the individual values of dies in the
     * GameScreen GUI
     * @return the last value of the first die as an int
     */
    @Override
    public int getLastD1() {
        return lastD1Rolled;
    }

    /**
     * getLastD2 returns the last value of the second die. Is used for displaying the individual values of dies in the
     * GameScreen GUI
     * @return the last value of the second die as an int
     */
    @Override
    public int getLastD2() {
        return lastD2Rolled;
    }

    /**
     * checkForDevelopedProperties iterates through each tile on the board and adds the Property objects that have been
     * developed to the array list developed properties
     */
    @Override
    public void checkForDevelopedProperties(){
        developedProperties.clear();
        for (Tile tile : board){
            if (tile instanceof Property) {
                 if (((Property) tile).getHousesOwned() > 0){
                     developedProperties.add((Property) tile);
                 }
            }
        }
    }

    /**
     * getDevelopedProperties returns the list of developed properties as collated in checkForDevelopedProperties
     * @return the array list developedProperties
     */
    @Override
    public ArrayList<Property> getDevelopedProperties(){
        checkForDevelopedProperties();
        return developedProperties;
    }

    /**
     * howManyStationUtilityDoesPlayerOwn takes a player and a Service tile and calculated the total number of Service
     * tiles of that identity that a player owns
     * @param playerOwner the Player object
     * @param tile the Service Tile object
     * @return the number or Service objects of a given identity that a player owns
     */
    @Override
    public int howManyStationUtilityDoesPlayerOwn(Player playerOwner, Services tile){
        int i = 0;
        String type = "UTILITY";
        if (tile instanceof Station) {
            type = "STATION";
        }
        if(identityOwnablesMap.containsKey(type)){
            for (Ownable ownable : identityOwnablesMap.get(type)) {
                if (ownable.getOwner() == playerOwner) {
                    i++;
                }
            }
        }
        return i;
    }

    /**
     * getNumberOfHousesOwned takes a plyer, iterates through all of the Ownables they have, and calculates the total
     * number of houses and hotels. Used to calculate repair costs in the card action method
     * @param player the Player object
     * @return an array of two Integer objects, where the first index holds the number of houses, and the second the
     * number of hotels
     */
    @Override
    public Integer[] getNumberOfHousesOwned(Player player){
        Integer[] counts = new Integer[2];
        int hotels = 0;
        int houses = 0;
        for (Ownable tile:player.getOwnables()) {
            if (tile instanceof Property) {
                int developments = ((Property) tile).getHousesOwned();
                if (developments == 5){
                    hotels += 1;
                } else{
                    houses += developments;
                }
            }
        }
        counts[0]= houses;
        counts[1] = hotels;
        return counts;
    }

    /**
     * getLastCardPulled returns the last Card object that was pulled from the deck by a player
     * @return the last Card object pulled
     */
    @Override
    public Card getLastCardPulled(){
        return lastCardPulled;
    }

    /**
     * drawPotLuckCard draws a Card object from the potLuckCards array list, sets lastCardPulled to be that card,
     * performs the action and then adds it back to the potLuckCards array list
     */
    @Override
    public void drawPotluckCard(){
        lastCardPulled = potluckCards.remove(0);
        performCardAction(lastCardPulled);
        potluckCards.add(lastCardPulled);
    }

    /**
     * drawOpportunityKnocksCard draws a Card object from the opportunityKnocksCards array list, sets lastCardPulled to
     * be that card, performs the action and then adds it back to the opportunityKnocksCards array list
     */
    @Override
    public void drawOpportunityKnocksCard(){
        lastCardPulled = opportunityKnocksCards.remove(0);
        performCardAction(lastCardPulled);
        opportunityKnocksCards.add(lastCardPulled);
    }
}