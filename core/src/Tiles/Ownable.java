package Tiles;

import main.Player;
import misc.Coordinate;

public class Ownable extends SmallTile{
    protected int cost;
    private boolean isBuyable = false;
    protected boolean owned;
    protected Player owner;
    protected Coordinate ownableSpriteCoordinate;
    private boolean isMortgaged;

    /**
     * constructor for ownable
     */
    public Ownable(){
        owned = false;
        owner = null;
    }

    /**
     * getBuyable returns true if ownable is buyable and false if it is not.
     * @return returns isBuyable , Boolean true or false.
     */
    public boolean getBuyable() {
        return isBuyable;
    }

    /**
     * setBuyable sets isBuyable to true or false.
     * @param TrueOrFalse whether the ownable is buyable or not (true if yes, else false).
     */
    public void setBuyable(boolean TrueOrFalse) {
        isBuyable = TrueOrFalse;
    }

    /**
     * method used to sell the ownable
     * @param player the player selling
     * @param cost the cost of the ownable
     */
    public void sellOwnable(Player player, int cost){

        player.payPlayer(cost);
        if (player.getOwnables().contains(this)) {
            player.removeOwnable(this);
        }
        owned = false;
        owner = null;
        setBuyable(true);
    }

    /**
     * setCost will set the cost of a single ownable within game board
     * @param cost will be used to enter the cost of the ownable that player will need to pay
     *             in order to own the ownable
     */
    public void setCost(int cost){ this.cost = cost; }

    /**
     * getCost will return an integer that will represent a cost of the ownable
     * @return returns cost, integer that represents the cost of ownable
     */
    public int getCost(){ return cost; }


    /**
     * getOwned will return a boolean value that will represent if the ownable is owned by any player or not
     * @return returns owned, boolean value that represent if ownable is owned
     */
    public boolean getOwned(){ return owned; }

    /**
     * @return returns the player object that is assigned to the ownable
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @return returns the name assigned to the player object that owns the ownable
     */
    public String getOwnerName(){
        if (owner != null){
            return owner.getName();
        }
        return "Nobody";
    }

    /**
     * method to buy ownable
     * @param player the player buying
     * @param cost the cost of the ownable
     */
    public void buyOwnable(Player player, int cost) {
        if (!getOwned() && player.getMoney()>= cost){
            player.makePurchase(cost);
            player.addOwnable(this);
            owned = true;
            owner = player;
            setBuyable(false);
        }
    }

    /**
     * @return the coordinate of the ownablesprite
     */
    public Coordinate getOwnableSpriteCoordinate(){
        return ownableSpriteCoordinate;
    }

    /**
     * method to mortgage a ownable
     * @param player the player mortgaging
     * @param cost the cost of the ownable
     */
    public void setMortgaged(Player player, int cost) {
        if ((getOwned())) {
            isMortgaged = true;
            player.payPlayer(cost / 2);
        }
    }

    /**
     * method to unmortgage a ownable
     * @param player the player unmortgaging
     * @param cost the cost of the ownable
     */
    public void unmortgage(Player player, int cost){
        if(isMortgaged){
            isMortgaged = false;
            player.makePurchase(cost/2);
        }
    }

    /**
     * @return returns true if the ownable is mortgaged
     */
    public boolean getMortgaged(){
        return isMortgaged;
    }
}