package misc;

/**
 * This class allows the CellToTileBuilder to work by assigning specific coordinates to each cell, allowing for
 * mouse clicks to be tracked
 */
public class Coordinate {

    private int x;
    private int y;

    /**
     * Constructor for Coordinate. Initialises the ints for each coordinate
     * @param CellX
     * @param CellY
     */
    public Coordinate(int CellX, int CellY) {
        this.x = CellX*64;
        this.y = CellY*64;
    }

    /**
     * sets the x,y of the coordinate
     * @param x x coord
     * @param y y coord
     */
    public void setCoordinate(int x, int y) {
        this.x = x;
        this.y= y;
    }

    /**
     * getX gets the x coord
     * @return the x coord
     */
    public int getX(){
        return x;
    }

    /**
     * getY gets the y coord
     * @return the y coord
     */
    public int getY(){
        return y;
    }
}