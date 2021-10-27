package Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Utility extends Services {

    /**
     * constructor for utility
     */
    public Utility(){
        tileName = "";
        Texture texture = new Texture(Gdx.files.internal("tile-images/utility.png"));
        icon = new Sprite(texture);
        icon.setOriginCenter();
        icon.setAlpha(1f);
    }

    /**
     * @param dice the player's dice roll
     * @return the rent based on the dice roll
     */
    public int getRent( int dice){
        if (this.owner!=null) {
            int i = 0;
            for (Ownable prop : this.owner.getOwnables()) {
                if (prop instanceof Utility) {
                    i++;
                }
            }
            if (i == 1) {
                return 4 * dice;
            } else {
                return 10 * dice;
            }
        }
        return 0;
    }
}