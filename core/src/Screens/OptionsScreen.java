package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.propertytycoonmakers.make.PropertyTycoon;

/**
 * OptionsScreen is a GUI class that allows the user to interactively adjust Music/FX volume, toggle Music/FX on and
 * off, toggle fullscreen mode and return to MainMenu. This Screen is accessed from MainMenu.
 */
public class OptionsScreen implements Screen {

    private PropertyTycoon game;
    private Texture optionsScreenTexture;
    private Skin optionsScreenSkin;
    private Stage stage;
    private Viewport viewport;

    /**
     * The constructor for OptionsScreen
     * @param game The PropertyTycoon parent class upon which the GUI is built
     */
    public OptionsScreen(PropertyTycoon game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.optionsScreenTexture = new Texture(Gdx.files.internal("backgrounds/optionScreenTexture.png"));
        this.optionsScreenSkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
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
        stage.clear();
        stage.addActor(table);

        Label musicVolumeLabel = new Label("Music Volume", optionsScreenSkin, "title");
        Label musicOnOffLabel = new Label("Music On/Off", optionsScreenSkin, "title");
        Label fxVolumeLabel = new Label("FX Volume", optionsScreenSkin, "title");
        Label fxOnOffLabel = new Label("FX On/Off", optionsScreenSkin, "title");
        Label fullscreenOnOffLabel = new Label("Fullscreen", optionsScreenSkin, "title");

        final Slider musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, optionsScreenSkin);
        musicVolumeSlider.setValue(game.getPreferences().getMusicVolume());
        musicVolumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume(musicVolumeSlider.getValue());
                return false;
            }
        });

        final Slider fxVolumeSlider = new Slider(0f, 1f, 0.1f, false, optionsScreenSkin);
        fxVolumeSlider.setValue(game.getPreferences().getFxVolume());
        fxVolumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setFxVolume(fxVolumeSlider.getValue());
                return false;
            }
        });

        final CheckBox musicOnOff = new CheckBox(null, optionsScreenSkin);
        musicOnOff.setChecked(game.getPreferences().isMusicEnabled());
        musicOnOff.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean isOn = musicOnOff.isChecked();
                game.getPreferences().setMusicEnabled(isOn);
                return false;
            }
        });

        final CheckBox fullscreenOnOff = new CheckBox(null, optionsScreenSkin);
        fullscreenOnOff.setChecked(Gdx.graphics.isFullscreen());
        fullscreenOnOff.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

                if (Gdx.graphics.isFullscreen()) {
                    Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
                    game.getPreferences().setPrefsFullscreen(fullscreenOnOff.isChecked());

                } else
                Gdx.graphics.setFullscreenMode(currentMode);
                game.getPreferences().setPrefsFullscreen(fullscreenOnOff.isChecked());

            }
        });

        final CheckBox fxOnOff = new CheckBox(null, optionsScreenSkin);
        fxOnOff.setChecked(game.getPreferences().isFxEnabled());
        fxOnOff.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean isOn = fxOnOff.isChecked();
                game.getPreferences().setFxEnabled(isOn);
                return false;
            }
        });

        final TextButton back = new TextButton("Back", optionsScreenSkin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

        table.row().pad(10, 0, 0, 20);
        table.add(musicVolumeLabel).left();
        table.add(musicVolumeSlider).padLeft(10);
        table.row().pad(10, 0, 0, 20);
        table.add(musicOnOffLabel).left();
        table.add(musicOnOff).padLeft(10);
        table.row().pad(10, 0, 0, 20);
        table.add(fxVolumeLabel).left();
        table.add(fxVolumeSlider).padLeft(10);
        table.row().pad(10, 0, 0, 20);
        table.add(fxOnOffLabel).left();
        table.add(fxOnOff).padLeft(10);
        table.row().pad(10, 0, 0, 20);
        table.add(fullscreenOnOffLabel).left();
        table.add(fullscreenOnOff).padLeft(10);
        table.row().pad(10, 0, 0, 20);
        table.add(back).colspan(2);
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

        game.batch.draw(optionsScreenTexture, 0, 0);
        game.font.getData().setScale(2);
        game.font.draw(game.batch, "Property Tycoon Options", 100, 100);
        game.batch.draw(optionsScreenTexture, 0, 0, viewport.getWorldWidth(),viewport.getWorldHeight());

        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when OptionsScreen() should release all resources
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
     * Called when this OptionsScreen() is no longer the current screen for PropertyTycoon()
     */
    @Override
    public void hide() {}
}