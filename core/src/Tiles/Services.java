package Tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import misc.Coordinate;

import java.util.ArrayList;

import static com.propertytycoonmakers.make.PropertyTycoon.players;

public class Services extends Ownable {

    protected Sprite icon;

    /**
     * sets the coordinates for sprites within the service tiles
     * @param coordinates the arraylist of coordinates for each tile.
     */
    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        playerPosCoordinates = new ArrayList<>();
        for(int i =0 ; i < players.size();i++) {
            if (i != 3 && i != 7 && i != 11) {
                playerPosCoordinates.add(coordinates.get(i));
            }
        }

        int tilePosition = 7; // used to determine which cell in a card is the label position one (makes it easier for us to change as we go)
        int propertyPosition = 7;// where the tile icon is placed
        int iconPosition = 5;

        Coordinate iconCoordinate = new Coordinate(0,0);
        Coordinate labelCoordinate = new Coordinate(0,0);
        Coordinate tempPropertyCoordinate = new Coordinate(0,0);

        labelCoordinate.setCoordinate(coordinates.get(tilePosition).getX()+32,coordinates.get(tilePosition).getY()+32);
        tempPropertyCoordinate.setCoordinate(coordinates.get(propertyPosition).getX()+32,coordinates.get(propertyPosition).getY()+32);
        iconCoordinate.setCoordinate(coordinates.get(iconPosition).getX()-64,coordinates.get(iconPosition).getY()-64);

        centerLabelCoordinate = labelCoordinate;
        ownableSpriteCoordinate = tempPropertyCoordinate;
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
     * @return returns the icon sprite associated with the service tile
     */
    public Sprite getIcon(){return icon;}
}