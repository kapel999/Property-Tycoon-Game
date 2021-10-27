package Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import misc.Coordinate;

import java.util.ArrayList;

import static com.propertytycoonmakers.make.PropertyTycoon.players;

public class Tax extends SmallTile implements TaxInterface {

    int taxAmount;
    protected Sprite icon;

    /**
     * constructor for tax tile
     */
    public Tax() {
        tileName = "Tax";
        Texture texture = new Texture(Gdx.files.internal("tile-images/tax.png"));
        icon = new Sprite(texture);
        icon.setOriginCenter();
        icon.setAlpha(1f);
    }

    /**
     * sets coordinates for sprites within the tax tile
     * @param coordinates the arraylist of coordinates for each tile.
     */
    @Override
    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        playerPosCoordinates = new ArrayList<>();
        for(int i =0 ; i < players.size();i++) {
            if (i != 3 && i != 7 && i != 11) {
                playerPosCoordinates.add(coordinates.get(i));
            }
        }

        int tilePosition = 7; // used to determine which cell in a card is the label position one (makes it easier for us to change as we go)
        int iconPosition = 5;

        Coordinate iconCoordinate = new Coordinate(0,0);
        Coordinate labelCoordinate = new Coordinate(0,0);

        labelCoordinate.setCoordinate(coordinates.get(tilePosition).getX()+32,coordinates.get(tilePosition).getY()+32);
        iconCoordinate.setCoordinate(coordinates.get(iconPosition).getX()-64,coordinates.get(iconPosition).getY()-64);

        centerLabelCoordinate = labelCoordinate;
        allCoordinates = coordinates;

        if (this.getTilePos() < 11){

            icon.rotate(-90);
        }   else   if (this.getTilePos() < 21){

            icon.rotate(-180);
        }   else   if (this.getTilePos() <31){

            icon.rotate(-270);
        }else{

        }
        icon.setPosition(iconCoordinate.getX(),iconCoordinate.getY());
    }


    /**
     * @return returns the icon placed on the tax tile
     */
    public Sprite getIcon(){return icon;}
    public int getTaxAmount(){
        return this.taxAmount;
    }

    public void setTaxAmount(int tax){

       this.taxAmount = tax;
    }
}