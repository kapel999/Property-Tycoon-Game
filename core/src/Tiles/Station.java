package Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Station extends Services implements StationInterface {

    private int currentRent;

    /**
     * constructor for station
     */
    public Station(){
        currentRent = 50;
        Texture texture = new Texture(Gdx.files.internal("tile-images/station.png"));
        icon = new Sprite(texture);
        icon.setOriginCenter();
        icon.setAlpha(1f);
    }

    /**
     *
     * @return rent paid by opponents who land on the tile
     */
    public int getRent(){
        if (this.owner != null) {
            int i = 0;
            for (Ownable prop : this.owner.getOwnables()) {
                if (prop instanceof Station) {
                    i++;
                }
            }
            return currentRent * i;
        }
        return 0;
    }

}
