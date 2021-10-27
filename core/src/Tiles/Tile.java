package Tiles;

import main.Player;
import misc.Coordinate;

import java.util.ArrayList;

/**
 * The tile class is the superclass for all further tiles, this allows all tiles to have in general the same
 * functionality
 */
public class Tile {

    protected String tileName;
    private int tilePosition;
    ArrayList<Player> tilePlayers = new ArrayList<>();

    protected ArrayList<Coordinate> playerPosCoordinates;
    protected ArrayList<Coordinate> allCoordinates;

    /**
     * the constructor for the tile class
     */
    public Tile(){
        tileName = "";
    }

    /**
     * Returns the Players on the current tile.
     * @return List of Players on the tile.
     */
    public ArrayList<Player> getPlayers() {
        return tilePlayers;
    }

    /**
     * Adds player to list of players positioned on the tile.
     * @param player The main.Player to be added to the list of players on the tile.
     */
    public void addPlayer(Player player) {
        tilePlayers.add(player);
    }

    /**
     * Removes player from the list of players positioned on the tile.
     * @param player The main.Player to remove.
     */
    public void removePlayer(Player player) {
        tilePlayers.remove(player);
    }

    /**
     * Returns the position of the tile on the game board.
     * @return int number of position of tile on the board.
     */
    public int getTilePos() {
        return tilePosition;
    }

    /**
     * Sets the value of the position of the tile on the game board.
     * @param position The desired position.
     */
    public void setTilePos(int position) {
        tilePosition = position;
    }

    /**
     * sets the coordinates of tiles on the GameBoard, this allows for mouse interactivity.
     * @param coordinates the arraylist of coordinates for each tile.
     */
    public void setCoordinates(ArrayList<Coordinate> coordinates){
    }

    /**
     * Gets the next available coordinate a player can be positioned.
     * @return the next available coordinate for a player to be positioned.
     */
    public Coordinate getAvailableCoordinates(){
        Coordinate coordinate = playerPosCoordinates.remove(0);
        playerPosCoordinates.add(coordinate);
        return coordinate;
    }

    /**
     * @return returns the arraylist of all player coordinates
     */
    public ArrayList<Coordinate> getAllPlayerCoordinates() {
        return playerPosCoordinates;
    }

    /**
     * @return returns the arraylist of all coordinates
     */
    public ArrayList<Coordinate> getAllCoordinates() {
        return allCoordinates;
    }

    /**
     * Returns the tile name.
     * @return Tiles.Tile name.
     */
    public String getTileName() {
        return tileName;
    }

    /**
     * Sets the tile name.
     * @param name The desired name of the tile.
     */
    public void setTileName(String name) {
        tileName = name;
    }
}