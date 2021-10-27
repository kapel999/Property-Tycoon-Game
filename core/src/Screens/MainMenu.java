package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.propertytycoonmakers.make.PropertyTycoon;

/**
 * MainMenu is a GUI class that allows the user to start/resume a game, access options and also exit the Application.
 * It is displayed as the first Screen upon opening the game.
 */
public class MainMenu implements Screen {

    private PropertyTycoon game;
    private Texture mainMenuTexture;
    private Skin mainMenuSkin;
    private Stage stage;
    private Viewport viewport;

    /**
     * The constructor for MainMenu
     * @param game The PropertyTycoon parent class upon which the GUI is built
     */
    public MainMenu(PropertyTycoon game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.mainMenuTexture = new Texture(Gdx.files.internal("backgrounds/mainMenuTexture.png"));
        this.mainMenuSkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
    }

    /**
     * show() defines the layout, elements and interactivity of the GUI
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton resumeButton = new TextButton("Resume",mainMenuSkin);
        Button newGameButton = new TextButton("New Game", mainMenuSkin);
        Button options = new TextButton("Options", mainMenuSkin);
        Button exit = new TextButton("Exit", mainMenuSkin);

        resumeButton.setVisible(game.isGameInProgress());

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameSetUpScreen(game));
                game.newGame();
            }
        });

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(game.GAME);
            }
        });

        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionsScreen(game));
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

        table.add(newGameButton).fillX().uniformY();
        table.row().pad(10, 0, 0, 0);
        if(resumeButton.isVisible()) {
            table.add(resumeButton).fillX().uniformY();
            table.row().pad(10, 0, 0, 0);
        }
        table.add(options).fillX().uniformY();
        table.row().pad(10, 0, 0, 0);
        table.add(exit).fillX().uniformY();
    }

    /**
     * render() is called when the Screen should render itself
     * @param delta the time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(mainMenuTexture, 0, 0);
        game.batch.draw(mainMenuTexture, 0, 0, viewport.getWorldWidth(),viewport.getWorldHeight());

        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when MainMenu() should release all resources
     */
    @Override
    public void dispose() { stage.dispose(); }

    /**
     * Called when the Application is resized. Will never be called before a call to create()
     * @param width the width of the screen
     * @param height the height of the screen
     */
    @Override
    public void resize(int width, int height) { stage.getViewport().update(width, height, true); }

    /**
     * Called when the Application is paused. An Application is paused before it is destroyed
     */
    @Override
    public void pause() {}

    /**
     * Called when the Application is resumed from a paused state
     */
    @Override
    public void resume() {}

    /**
     * Called when this MainMenu() is no longer the current screen for PropertyTycoon()
     */
    @Override
    public void hide() {}
}