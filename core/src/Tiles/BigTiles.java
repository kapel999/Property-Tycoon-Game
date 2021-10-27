package Tiles;

import misc.Coordinate;

import java.util.ArrayList;

import static com.propertytycoonmakers.make.PropertyTycoon.players;

public class BigTiles extends Tile{

    /**
     * sets the coordinates of where sprites should appear on the tile
     * @param coordinates the arraylist of coordinates for each tile.
     */
    public void setCoordinates(ArrayList<Coordinate> coordinates){
        playerPosCoordinates = new ArrayList<>();
        for(int i =0 ; i < players.size();i++) {

                playerPosCoordinates.add(coordinates.get(i));
        }
        allCoordinates = coordinates;
    }
}
