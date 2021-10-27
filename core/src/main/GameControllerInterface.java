package main;

import Tiles.Property;
import Tiles.Tile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

public interface GameControllerInterface {
    Tile retTile(TiledMapTileLayer.Cell cell);

    Player getCurrentPlayer();

    Tile playerTurn();

    int getAuctionValue();

    void setAuctionValue(int value);

    boolean getPlayAgain();

    void endTurn();

    GameBoard getBoard();

    ArrayList<Player> getPlayerOrder();

    boolean developProperty(Property prop, Player player);

    int getLastD1();

    int getLastD2();

    ArrayList<Property> getDevelopedProperties();

    void freePlayerFromJail(Player player);

    ArrayList<Player> getFinalStandings(ArrayList<Player> players, int start, int end);

    int partition(ArrayList<Player> players, int start, int end);
}
