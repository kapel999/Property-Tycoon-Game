package Tiles;

import misc.Coordinate;

import java.util.ArrayList;

public class Jail extends BigTiles {

    ArrayList<Coordinate> jailCoordinates;
    private int bailPrice = 50;

    /**
     * The constructor for Jail
     */
    public Jail(){
        tileName = "Jail";
    }

    /**
     * sets coordinates of where sprites should appear on the tile
     * @param coordinates the arraylist of coordinates for each tile.
     */
    @Override
    public void setCoordinates(ArrayList<Coordinate> coordinates){
        this.playerPosCoordinates = new ArrayList<>();
        jailCoordinates = new ArrayList<>();
        for (int i =0; i < 15 ; i++) {
            if (i == 0 || i == 4 || i == 8 || i == 12 || i == 13 || i == 14 || i == 15) {
                playerPosCoordinates.add(coordinates.get(i));
            }
            else {
                jailCoordinates.add(coordinates.get(i));
            }
        }
        allCoordinates = coordinates;
    }

    /**
     * gets coordinates within the small orange square in jail that are free
     * @return coordinate within square
     */
    public Coordinate getNextJailCoordinate(){
        Coordinate coordinate = jailCoordinates.remove(0);
        jailCoordinates.add(coordinate);
        return coordinate;
    }

    public int getBailPrice() {
        return bailPrice;
    }

    public void setBailPrice(int bailPrice) {
        this.bailPrice = bailPrice;
    }
}