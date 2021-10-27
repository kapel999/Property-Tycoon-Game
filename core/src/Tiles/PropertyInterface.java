package Tiles;

public interface PropertyInterface extends TileInterface {
    void setColour(String colour);
    void addDevPrice(int housePrice);
    void setCost(int cost);
    int getCost();
    boolean getOwned();
}
