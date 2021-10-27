package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.propertytycoonmakers.make.PropertyTycoon;
import main.Bot;
import main.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * GameSetUpScreen is a GUI class that allows for the user to make choices regarding the number of players, their names
 * and their game pieces. It is displayed before the game starts allowing for user customization.
 */
public class GameSetUpScreen implements Screen {

    private PropertyTycoon game;
    private Texture gameSetUpScreenTexture;
    private Skin gameSetUpScreenSkin;
    private Stage stage;
    private Viewport viewport;

    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    private Sprite sprite4;
    private Sprite sprite5;
    private Sprite sprite6;

    private ArrayList<Sprite> spriteList;
    private SelectBox[] tokenSBList;
    private Image[] tokenImageList;

    private TextField player1Field;
    private TextField player2Field;
    private TextField player3Field;
    private TextField player4Field;
    private TextField player5Field;
    private TextField player6Field;

    private Image token1Image;
    private Image token2Image;
    private Image token3Image;
    private Image token4Image;
    private Image token5Image;
    private Image token6Image;

    private SelectBox<String> token1SB;
    private SelectBox<String> token2SB;
    private SelectBox<String> token3SB;
    private SelectBox<String> token4SB;
    private SelectBox<String> token5SB;
    private SelectBox<String> token6SB;

    private Table table;
    private Container<Table> tableContainer;
    private Label numPlayers;

    private ArrayList<TextField> playerNamesList;
    private SelectBox<Integer> numPlayersBox;
    private TextButton startFullGame;
    private TextButton startAbridgedGame;
    private TextField abridgedLengthField;
    private TextButton back;

    /**
     * The constructor for GameSetUpScreen
     * @param game The PropertyTycoon parent class upon which the GUI is built
     */
    public GameSetUpScreen(PropertyTycoon game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.gameSetUpScreenTexture = new Texture(Gdx.files.internal("backgrounds/optionScreenTexture.png"));
        this.gameSetUpScreenSkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Texture texture1 = new Texture(Gdx.files.internal("tokens/token1.png"));
        Texture texture2 = new Texture(Gdx.files.internal("tokens/token2.png"));
        Texture texture3 = new Texture(Gdx.files.internal("tokens/token3.png"));
        Texture texture4 = new Texture(Gdx.files.internal("tokens/token4.png"));
        Texture texture5 = new Texture(Gdx.files.internal("tokens/token5.png"));
        Texture texture6 = new Texture(Gdx.files.internal("tokens/token6.png"));

        this.sprite1 = new Sprite(texture1);
        this.sprite2 = new Sprite(texture2);
        this.sprite3 = new Sprite(texture3);
        this.sprite4 = new Sprite(texture4);
        this.sprite5 = new Sprite(texture5);
        this.sprite6 = new Sprite(texture6);

        this.spriteList = new ArrayList<>(Arrays.asList(sprite1, sprite2, sprite3, sprite4, sprite5, sprite6));

        player1Field = new TextField("Player 1", gameSetUpScreenSkin);
        player1Field.setMaxLength(8);
        player2Field = new TextField("Player 2", gameSetUpScreenSkin);
        player2Field.setMaxLength(8);
        player3Field = new TextField("Player 3", gameSetUpScreenSkin);
        player3Field.setMaxLength(8);
        player4Field = new TextField("Player 4", gameSetUpScreenSkin);
        player4Field.setMaxLength(8);
        player5Field = new TextField("Player 5", gameSetUpScreenSkin);
        player5Field.setMaxLength(8);
        player6Field = new TextField("Player 6", gameSetUpScreenSkin);
        player6Field.setMaxLength(8);

        token1Image = new Image(getTokenDrawable(spriteList.get(0)));
        token2Image = new Image(getTokenDrawable(spriteList.get(1)));
        token3Image = new Image(getTokenDrawable(spriteList.get(2)));
        token4Image = new Image(getTokenDrawable(spriteList.get(3)));
        token5Image = new Image(getTokenDrawable(spriteList.get(4)));
        token6Image = new Image(getTokenDrawable(spriteList.get(5)));

        token1SB = new SelectBox(gameSetUpScreenSkin);
        token2SB = new SelectBox(gameSetUpScreenSkin);
        token3SB = new SelectBox(gameSetUpScreenSkin);
        token4SB = new SelectBox(gameSetUpScreenSkin);
        token5SB = new SelectBox(gameSetUpScreenSkin);
        token6SB = new SelectBox(gameSetUpScreenSkin);

        String[] valueList = new String[]{"boot", "smartphone", "goblet", "hat", "cat", "spoon"};

        token1SB.setItems(valueList);
        token1SB.setAlignment(Align.center);
        token2SB.setItems(valueList);
        token2SB.setAlignment(Align.center);
        token3SB.setItems(valueList);
        token3SB.setAlignment(Align.center);
        token4SB.setItems(valueList);
        token4SB.setAlignment(Align.center);
        token5SB.setItems(valueList);
        token5SB.setAlignment(Align.center);
        token6SB.setItems(valueList);
        token6SB.setAlignment(Align.center);

        tokenImageList = new Image[]{token1Image, token2Image, token3Image, token4Image, token5Image, token6Image};
        tokenSBList = new SelectBox[]{token1SB, token2SB, token3SB, token4SB, token5SB, token6SB};

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.2f;
        float ch = sh * 0.5f;

        tableContainer = new Container<Table>();
        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        table = new Table();

        Table numPlayersTable = new Table();
        numPlayers = new Label("Number of players:", gameSetUpScreenSkin, "big");
        numPlayersBox = new SelectBox(gameSetUpScreenSkin, "big");
        numPlayersBox.setItems(new Integer[]{2, 3, 4, 5, 6});
        numPlayersTable.row().fill().expandX();
        numPlayersTable.add(numPlayers).width(cw/3);
        numPlayersTable.add(numPlayersBox).width(cw/3).padLeft(170);

        startFullGame = new TextButton("Start full game", gameSetUpScreenSkin);
        startAbridgedGame = new TextButton("Start abridged game", gameSetUpScreenSkin);

        abridgedLengthField = new TextField("", gameSetUpScreenSkin);
        abridgedLengthField.setMessageText("Length of abridged game in minutes");

        playerNamesList = new ArrayList<TextField>();
        playerNamesList.addAll(Arrays.asList(player1Field, player2Field, player3Field, player4Field, player5Field, player6Field));
        back = new TextButton("Back", gameSetUpScreenSkin);

        numPlayersBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setUIVisibility();
            }
        });

        table.add(numPlayersTable).colspan(3);
        table.row().pad(20, 0, 0, 0);
        table.add(new Label("Name a player 'bot' to make it a CPU", gameSetUpScreenSkin, "title")).colspan(3);
        table.row().pad(20, 0, 0, 0);
        table.add(player1Field).expandX().fillX();
        table.add(token1SB).expandX().fillX().padLeft(10);
        table.add(token1Image).width(64).height(64).padLeft(10);
        table.row().pad(10, 0, 0, 0);
        table.add(player2Field).expandX().fillX();
        table.add(token2SB).expandX().fillX().padLeft(10);
        table.add(token2Image).width(64).height(64).padLeft(10);
        table.row().pad(10, 0, 0, 0);
        table.add(player3Field).expandX().fillX();
        table.add(token3SB).expandX().fillX().padLeft(10);
        table.add(token3Image).width(64).height(64).padLeft(10);
        table.row().pad(10, 0, 0, 0);
        table.add(player4Field).expandX().fillX();
        table.add(token4SB).expandX().fillX().padLeft(10);
        table.add(token4Image).width(64).height(64).padLeft(10);
        table.row().pad(10, 0, 0, 0);
        table.add(player5Field).expandX().fillX();
        table.add(token5SB).expandX().fillX().padLeft(10);
        table.add(token5Image).width(64).height(64).padLeft(10);
        table.row().pad(10, 0, 0, 0);
        table.add(player6Field).expandX().fillX();
        table.add(token6SB).expandX().fillX().padLeft(10);
        table.add(token6Image).width(64).height(64).padLeft(10);
        table.row().pad(20, 0, 0, 0);
        table.add(startFullGame).colspan(3);
        table.row().pad(20, 0, 0, 0);
        table.add(startAbridgedGame).colspan(3);
        table.row();
        table.add(abridgedLengthField).width(275).colspan(3);
        table.row().pad(20, 0, 0, 0);
        table.add(back).colspan(3);

        tableContainer.setActor(table);

        token1SB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                swapSprites(0, token1SB.getSelected());
                updateSB();
                updateTokenImageList();
            }
        });

        token2SB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                swapSprites(1, token2SB.getSelected());
                updateSB();
                updateTokenImageList();
            }
        });

        token3SB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                swapSprites(2, token3SB.getSelected());
                updateSB();
                updateTokenImageList();
            }
        });

        token4SB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                swapSprites(3, token4SB.getSelected());
                updateSB();
                updateTokenImageList();
            }
        });

        token5SB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                swapSprites(4, token5SB.getSelected());
                updateSB();
                updateTokenImageList();
            }
        });

        token6SB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                swapSprites(5, token6SB.getSelected());
                updateSB();
                updateTokenImageList();
            }
        });

        updateSB();
        setUIVisibility();
    }

    /**
     * show() defines the layout, elements and interactivity of the GUI
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.clear();
        stage.addActor(tableContainer);

        abridgedLengthField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.toString(c).matches("^[0-9]");
            }
        });

        startFullGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getPreferences().setAbridged(false);
                Boolean isEmpty = false;
                for(TextField tf : playerNamesList) {
                    if (tf.getText().matches("^$|( )*")) {
                        isEmpty = true;
                    }
                }
                if(isEmpty) {
                    final Window window = new Window("", gameSetUpScreenSkin);
                    final Label label = new Label("Name field left empty", gameSetUpScreenSkin, "big");
                    window.add(label);
                    window.setBounds((Gdx.graphics.getWidth() - 350) / 2, (Gdx.graphics.getHeight() - 100) / 2, 350, 100);
                    stage.addActor(window);
                    window.setVisible(true);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            window.setVisible(false);
                        }
                    }, 1);
                }
                else if(playerNamesList.get(0).getText().toLowerCase().equals("bot")) {
                    final Window window = new Window("", gameSetUpScreenSkin);
                    final Label label = new Label("First player cannot be a bot", gameSetUpScreenSkin, "big");
                    window.add(label);
                    window.setBounds((Gdx.graphics.getWidth() - 400) / 2, (Gdx.graphics.getHeight() - 100) / 2, 500, 100);
                    stage.addActor(window);
                    window.setVisible(true);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            window.setVisible(false);
                        }
                    }, 1);
                }
                else {
                    game.players = null;
                    game.players = new ArrayList<>();
                    for (int i = 0; i < numPlayersBox.getSelected(); i++) {
                        if(playerNamesList.get(i).getText().toLowerCase().equals("bot")) {
                            game.players.add(new Bot(playerNamesList.get(i).getText().toLowerCase()+" "+i, spriteList.get(i)));
                        }
                        else {
                            game.players.add(new Player(playerNamesList.get(i).getText(), spriteList.get(i)));
                        }
                    }
                    game.changeScreen(game.GAME);
                }
            }
        });

        startAbridgedGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Boolean isEmpty = false;
                for(TextField tf : playerNamesList) {
                    if (tf.getText().matches("^$|( )*")) {
                        isEmpty = true;
                    }
                }
                if(isEmpty) {
                    final Window window = new Window("", gameSetUpScreenSkin);
                    final Label label = new Label("Name field left empty", gameSetUpScreenSkin, "big");
                    window.add(label);
                    window.setBounds((Gdx.graphics.getWidth() - 400) / 2, (Gdx.graphics.getHeight() - 100) / 2, 400, 100);
                    stage.addActor(window);
                    window.setVisible(true);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            window.setVisible(false);
                        }
                    }, 1);
                }
                else if(abridgedLengthField.getText().matches("^$|( )*")) {
                    final Window window = new Window("", gameSetUpScreenSkin);
                    final Label label = new Label("Length of game invalid", gameSetUpScreenSkin, "big");
                    window.add(label);
                    window.setBounds((Gdx.graphics.getWidth() - 400) / 2, (Gdx.graphics.getHeight() - 100) / 2, 400, 100);
                    stage.addActor(window);
                    window.setVisible(true);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            window.setVisible(false);
                        }
                    }, 1);
                }
                else if(playerNamesList.get(0).getText().toLowerCase().equals("bot")) {
                    final Window window = new Window("", gameSetUpScreenSkin);
                    final Label label = new Label("First player cannot be a bot", gameSetUpScreenSkin, "big");
                    window.add(label);
                    window.setBounds((Gdx.graphics.getWidth() - 400) / 2, (Gdx.graphics.getHeight() - 100) / 2, 500, 100);
                    stage.addActor(window);
                    window.setVisible(true);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            window.setVisible(false);
                        }
                    }, 1);
                }
                else {
                    game.getPreferences().setAbridged(true);
                    game.getPreferences().setAbridgedLength(Integer.parseInt(abridgedLengthField.getText()));
                    game.players = null;
                    game.players = new ArrayList<>();
                    for (int i = 0; i < numPlayersBox.getSelected(); i++) {
                        if(playerNamesList.get(i).getText().toLowerCase().equals("bot")) {
                            game.players.add(new Bot(playerNamesList.get(i).getText().toLowerCase()+" "+i, spriteList.get(i)));
                        }
                        else {
                            game.players.add(new Player(playerNamesList.get(i).getText(), spriteList.get(i)));
                        }
                    }
                    game.changeScreen(game.GAME);
                }
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });
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
        game.batch.draw(gameSetUpScreenTexture, 0, 0);
        game.font.getData().setScale(2);
        game.font.draw(game.batch, "Property Tycoon Options", 100, 100);
        game.batch.draw(gameSetUpScreenTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * setUIVisibility() makes calls to setUI() according to how many players have been chosen via the drop down box
     */
    private void setUIVisibility(){
        switch (numPlayersBox.getSelected()) {
            case 2:
                setUI(false, false, false, false);
                break;
            case 3:
                setUI(true, false, false, false);
                break;
            case 4:
                setUI(true, true, false, false);
                break;
            case 5:
                setUI(true, true, true, false);
                break;
            case 6:
                setUI(true, true, true, true);
                break;
        }
    }

    /**
     * setUI() is used to define whether or not a player field (consisting of a name box and game piece
     * selector) should be shown
     * @param selection3 true if the third player field should be shown, false if not
     * @param selection4 true if the fourth player field should be shown, false if not
     * @param selection5 true if the fifth player field should be shown, false if not
     * @param selection6 true if the sixth player field should be shown, false if not
     */
    private void setUI(Boolean selection3, Boolean selection4, Boolean selection5, Boolean selection6) {
        player3Field.setVisible(selection3);
        player4Field.setVisible(selection4);
        player5Field.setVisible(selection5);
        player6Field.setVisible(selection6);

        token3SB.setVisible(selection3);
        token4SB.setVisible(selection4);
        token5SB.setVisible(selection5);
        token6SB.setVisible(selection6);

        token3Image.setVisible(selection3);
        token4Image.setVisible(selection4);
        token5Image.setVisible(selection5);
        token6Image.setVisible(selection6);
    }

    /**
     * getTokenDrawable() takes a Sprite object and returns the Drawable object associated with that sprite's texture
     * @param sprite the sprite who's texture is to be returned as a Drawable object
     * @return the required Drawable object
     */
    private Drawable getTokenDrawable(Sprite sprite) {
        return new TextureRegionDrawable(new TextureRegion(sprite.getTexture()));
    }

    /**
     * swapSprites() takes two Sprite objects and swaps their positions within the spriteList
     * @param s1pos the first Sprite object
     * @param s2str the second Sprite object
     */
    private void swapSprites(int s1pos, String s2str) {
        if (s2str.equals("boot")) {
            Collections.swap(spriteList, s1pos, spriteList.indexOf(sprite1));
        } else if (s2str.equals("smartphone")) {
            Collections.swap(spriteList, s1pos, spriteList.indexOf(sprite2));
        } else if (s2str.equals("goblet")) {
            Collections.swap(spriteList, s1pos, spriteList.indexOf(sprite3));
        } else if (s2str.equals("hat")) {
            Collections.swap(spriteList, s1pos, spriteList.indexOf(sprite4));
        } else if (s2str.equals("cat")) {
            Collections.swap(spriteList, s1pos, spriteList.indexOf(sprite5));
        } else if (s2str.equals("spoon")) {
            Collections.swap(spriteList, s1pos, spriteList.indexOf(sprite6));
        }
    }

    /**
     * getSpriteTitle() takes a Sprite object and returns a String that represents the title of the Sprite
     * @param sprite the Sprite object who's title is to be returned
     * @return a String that represents the title of a Sprite
     */
    private String getSpriteTitle(Sprite sprite) {
        if (sprite == sprite1) {
            return "boot";
        } else if (sprite == sprite2) {
            return "smartphone";
        } else if (sprite == sprite3) {
            return "goblet";
        } else if (sprite == sprite4) {
            return "hat";
        } else if (sprite == sprite5) {
            return "cat";
        } else if (sprite == sprite6) {
            return "spoon";
        }
        return "ERROR";
    }

    /**
     * updateSB() updates the selection boxs so to display the correct sprite title as the selected option
     */
    private void updateSB() {
        for (int i = 0; i < spriteList.size(); i++) {
            tokenSBList[i].setSelected(getSpriteTitle(spriteList.get(i)));
        }
    }

    /**
     * updaateTokenImageList() updates the token images so to display the correct image with each player field
     */
    private void updateTokenImageList() {
        for (int i = 0; i < spriteList.size(); i++) {
            tokenImageList[i].setDrawable(getTokenDrawable(spriteList.get(i)));
        }
    }

    /**
     * Called when GameSetUpScreen() should release all resources
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
     * Called when this GameSetUpScreen() is no longer the current screen for PropertyTycoon()
     */
    @Override
    public void hide() {}
}