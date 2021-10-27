package com.propertytycoonmakers.make;

import Screens.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import main.Player;

import java.util.ArrayList;

/**
 * PropertyTycoon is the orchestrator of all GUI classes. It repeatedly makes calls to the render methods of the super
 * classes along with storing game preferences and playing the game music.
 */
public class PropertyTycoon extends Game {

	private GameOptions options;
	private GameScreen gameScreen;
	private Sound gameMusic;
	private long gameMusicID;
	public SpriteBatch batch;
	public BitmapFont font;
	public static ArrayList<Player> players;
	public final static int GAME = 0;

	/**
	 * create() acts as the constructor for PropertyTycoon. It initialises the key objects along with setting the first
	 * Screen to be shown as MainMenu
	 */
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		options = new GameOptions();
		gameMusic = Gdx.audio.newSound(Gdx.files.internal("sound/game_music.mp3"));
		gameMusicID = gameMusic.play(options.getMusicVolume());
		gameMusic.setLooping(gameMusicID, true);
		this.setScreen(new MainMenu(this));
	}

	/**
	 * render() makes calls to the render methods of the super classes along with constantly updating game music volume
	 */
	@Override
	public void render() {
		if(options.isMusicEnabled()) {
			gameMusic.resume(gameMusicID);
			gameMusic.setVolume(gameMusicID, options.getMusicVolume());
		}
		else {
			gameMusic.pause(gameMusicID);
		}
		super.render();
	}

	/**
	 * getPreferences() returns the instance of GameOptions initialised in create() for use in super classes
	 * @return a GameOptions object
	 */
	public GameOptions getPreferences() {
		return options;
	}

	/**
	 * changeScreen() is used by super classes to switch the current Screen without creating a new instance of that
	 * Screen. This is particularly useful for GameScreen as we only require one instance
	 * @param screen
	 */
	public void changeScreen(int screen) {
		switch(screen){
			case GAME:
				if(gameScreen == null) gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
		}
	}

	/**
	 * newGame() allows for a new GameScreen to be initialised by first setting gameScreen as null
	 */
	public void newGame() {
		gameScreen = null;
	}

	/**
	 * isGameInProgress() returns a Boolean judgement as to whether a game currently is in progress or not - aka, is
	 * gameScreen null or not
	 * @return true if a game is in progress, false otherwise
	 */
	public Boolean isGameInProgress() {
		return gameScreen != null;
	}

	/**
	 * Called when PropertyTycoon() should release all resources
	 */
	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}