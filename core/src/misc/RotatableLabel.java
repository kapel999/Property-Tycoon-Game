package misc;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

/**
 * A class that adds more versatility to a Label object (it can be rotated and transformed)
 */
public class RotatableLabel extends Group {

    /**
     * the constructor for RotatableLabel
     * @param label the Label object to be transformed
     * @param x the X coordinate of the position that the label is to be set
     * @param y the Y coordinate of the position that the label is to be set
     * @param rotationAngle the angle by which the label is to be rotated
     * @param scale the float by which the label is to be scaled
     */
    public RotatableLabel(Label label, int x, int y , int rotationAngle, float scale){
        label.setFontScale((float)1.2);
        label.setSize(1,1);
        label.setOrigin(label.getWidth()/2,label.getHeight()/2);
        label.setAlignment(Align.center);
        RotateByAction rotate = new RotateByAction();
        rotate.setAmount(rotationAngle);
        this.addAction(rotate);
        this.setOrigin(label.getWidth()/2,label.getHeight()/2);
        this.addActor(label);
        this.setPosition(x,y);
    }
}