package Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PotLuck extends Chests {

    /**
     * constructor for potluck tile
     */
    public PotLuck(){
        Texture texture = new Texture(Gdx.files.internal("tile-images/potluck.png"));
        icon = new Sprite(texture);
        icon.setOriginCenter();
        setTileName("Potluck");
        icon.setAlpha(1f);
    }
}