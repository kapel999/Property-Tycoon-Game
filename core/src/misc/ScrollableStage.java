package misc;

import Screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * ScrollableStage overrides methods in Stage to allow for zoom and pan features
 */
public class ScrollableStage extends Stage {

    private GameScreen gs;
    private int scrollCount;

    /**
     * The constructor for ScrollableStage
     * @param gs the GameScreen object that contains the instance of ScrollableStage & the Camera object
     */
    public ScrollableStage(GameScreen gs) {
        this.gs = gs;
    }

    /**
     * scrolled overrides the scrolled method in Stage. Called when the mouse wheel was scrolled
     * @param amount takes an int that represents the amount scrolled, and zooms the camera accordingly
     * @return whether the input was processed
     */
    @Override
    public boolean scrolled(int amount) {
        if (amount == -1 && scrollCount > -7) {
            gs.getCam().zoom -= .2f;
            scrollCount--;
        }
        else if (amount == 1 && scrollCount < 0) {
            gs.getCam().zoom += .2f;
            scrollCount++;
        }
        return true;
    }

    /**
     * touchedDragged overrides the touchedDragged method in Stage. It is called when a finger/mouse is dragged.
     * @param screenX the X coordinate of the screen
     * @param screenY the Y coordinate of the screen
     * @param pointer the pointer for the event
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        OrthographicCamera camera = gs.getCam();
        camera.translate(-Gdx.input.getDeltaX() * (camera.viewportWidth / Gdx.graphics.getWidth()), Gdx.input.getDeltaY() * (camera.viewportHeight / Gdx.graphics.getHeight()));
        return true;
    }
}