package main;

import Tiles.Property;
import Tiles.Tile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import misc.CellToTileBuilder;
import misc.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import static com.propertytycoonmakers.make.PropertyTycoon.players;

/**
 * Acts as a bridge between game logic and the render phase
 */
public class GameController implements GameControllerInterface {

    private static GameBoard board;
    private HashMap<TiledMapTileLayer.Cell, Tile> cellToTile;
    private Boolean playAgain;
    private int currentAuction;
    private ArrayList<Player> playerOrders;

    /**
     * the constructor for GameController
     * @param layer the TiledMapTileLayer used to create the cell to Tile mapping
     */
    public GameController(TiledMapTileLayer layer) {
        playerOrders = new ArrayList<>(players);
        cellToTile = new HashMap<>();
        board = new GameBoard(players);
        CellToTileBuilder builder = new CellToTileBuilder(layer,board);
        cellToTile = builder.getReferenceList();
        Tile tile = board.getTile(0);
        for (Player p : playerOrders) {
            Coordinate coord =  tile.getAvailableCoordinates();
            p.setCurrentCoordinates(coord);
        }
    }

    /**
     * nextPlayer changes the current player to the next player in the list players
     */
    private void nextPlayer(){
        Player p = playerOrders.remove(0);
        playerOrders.add(p);
    }

    /**
     * retTile takes a cell that has been clicked on the TiledMapTileLayer and returns the associated Tile
     * @param cell the cell clicked
     * @return the Tile associated with the cell clicked
     */
    @Override
    public Tile retTile(TiledMapTileLayer.Cell cell) {
        return cellToTile.get(cell);
    }

    /**
     * getCurrentPlayer provides functionality to return the current player outside of this class
     * @return returns the player who's turn it currently is
     */
    @Override
    public Player getCurrentPlayer() {
        return playerOrders.get(0);
    }

    /**
     * playerTurn is a high level method call to execute players turns. Used by GameScreen to return the new tile the
     * player should be displayed on
     * @return the tile the player has landed on
     */
    @Override
    public Tile playerTurn() {
        playAgain = board.playerTurn(getCurrentPlayer());
        Tile tile = board.getTile(board.getPlayerPos(getCurrentPlayer()));
        return tile;
    }

    /**
     * getAuctionValue returns the current value of the auction
     * @return the current value of the auction as in int
     */
    @Override
    public int getAuctionValue(){
        return currentAuction;
    }

    /**
     * setAuctionValue takes a int value and sets the current value of the auction to that value
     * @param value the current value of the auction
     */
    @Override
    public void setAuctionValue(int value){
        currentAuction = value;
    }

    /**
     * getPlayAgain returns a Boolean judgement as to whether a second turn needs to be executed
     * @return returns true when 1st or 2nd doubles rolled
     */
    @Override
    public boolean getPlayAgain(){
        return playAgain;
    }

    /**
     * endTurn moves to the next player if there is no reason for the current player to continue their turn
     */
    @Override
    public void endTurn(){
        if (!playAgain){
            nextPlayer();
        }
    }

    /**
     * getBoard returns the current gameBoard object
     * @return returns the current GameBoard object
     */
    @Override
    public GameBoard getBoard() {
        return board;
    }

    /**
     * getPlayerOrder returns playerOrders
     * @return returns an ArrayList of players in their current play order (current player is at index 0, next player
     * at index 1 etc.)
     */
    @Override
    public ArrayList<Player> getPlayerOrder(){
        return playerOrders;
    }

    /**
     * developProperty takes a Property object and Player object and develops the property if the player owns all
     * properties of that colour
     * @param prop the Property object
     * @param player the Player object
     * @return true if the player can develop the property, false otherwise
     */
    @Override
    public boolean developProperty(Property prop, Player player) {
        if(player.getOwnables().containsAll(board.getIdentityOwnablesMap().get(prop.getColourAsString())) && prop.getOwner() == player && prop.getHousesOwned() < 5) {
            prop.develop();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * getLastD1 acts as a bridge between GameBoard and GameScreen. There is no direct way for GameScreen to get the
     * last value of the first die, and therefore this method is required
     * @return the last value of the first die as an int
     */
    @Override
    public int getLastD1() {
        return board.getLastD1();
    }

    /**
     * getLastD2 acts as a bridge between GameBoard and GameScreen. There is no direct way for GameScreen to get the
     * last value of the second die, and therefore this method is required
     * @return the last value of the second die as an int
     */
    @Override
    public int getLastD2() {
        return board.getLastD2();
    }

    /**
     * getDevelopedProperties acts as a bridge between GameBoard and GameScreen. It allows GameScreen to access the
     * array list of developed properties as collated in the Board class
     * @return the array list developedProperties
     */
    @Override
    public ArrayList<Property> getDevelopedProperties(){
        return board.getDevelopedProperties();
    }

    /**
     * freePlayerFromJail takes a Player object, sets them to not be in jail and moves their coordinates to the next
     * available cell in just visiting
     * @param player the Player object
     */
    @Override
    public void freePlayerFromJail(Player player){
        player.setInJail(false);
        Coordinate c = board.getTile(10).getAvailableCoordinates();
        player.getPlayerToken().setPosition(c.getX(),c.getY());
    }

    /**
     * getFinalStandings implements a quick sort algorithm for sorting an array list of Player objects by their wealth
     * @param players the array list of Player objects to be sorted
     * @param start the start index of the quick sort algorithm
     * @param end the end index of the quick sort algorithm
     * @return an array list of Player objects ordered by wealth
     */
    @Override
    public ArrayList<Player> getFinalStandings(ArrayList<Player> players, int start, int end) {
        if(start < end) {
            int partitionPoint = partition(players, start, end);
            getFinalStandings(players, start, partitionPoint - 1);
            getFinalStandings(players, partitionPoint + 1, end);
        }
        return players;
    }

    /**
     * partition is a subsidiary method of getFinalStandings that is used for determining the partition point
     * @param players the array list of Player objects to be sorted
     * @param start the start index of the quick sort algorithm
     * @param end the end index of the quick sort algorithm
     * @return an integer that represents the point of partition
     */
    @Override
    public int partition(ArrayList<Player> players, int start, int end) {
        int pivot = players.get(end).getWealth();
        int i = start - 1;
        for(int j=start; j<end; j++) {
            if(players.get(j).getWealth() < pivot) {
                i++;
                Player tempI = players.get(i);
                Player tempJ = players.get(j);
                players.set(i, tempJ);
                players.set(j, tempI);
            }
        }
        Player tempIplus1 = players.get(i+1);
        Player tempEnd = players.get(end);
        players.set(i+1, tempEnd);
        players.set(end, tempIplus1);
        return i+1;
    }
}