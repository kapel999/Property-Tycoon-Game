package Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class OpportunityKnocks extends Chests {

    /**
     * constructor for OpportunityKnocks
     */
    public OpportunityKnocks(){
        Texture texture = new Texture(Gdx.files.internal("tile-images/opportunityknocks.png"));
        icon = new Sprite(texture);
        icon.setOriginCenter();
        setTileName("Opportunity Knocks");
        icon.setAlpha(1f);
    }
}