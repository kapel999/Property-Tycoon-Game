package Tiles;
import misc.Coordinate;

public class SmallTile extends Tile{

    protected Coordinate centerLabelCoordinate;

    /**
     * Gets the label position coordinate used to display labels on the board properly.
     * @return coordinate of label position.
     */
    public Coordinate getCenterLabelCoordinate() {
        return centerLabelCoordinate;
    }
}