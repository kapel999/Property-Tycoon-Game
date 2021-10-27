package Screens;

import Tiles.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.propertytycoonmakers.make.PropertyTycoon;
import main.Bot;
import main.GameBoard;
import main.GameController;
import main.Player;
import misc.Card;
import misc.Coordinate;
import misc.RotatableLabel;
import misc.ScrollableStage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * gameScreen is the UI for the main game, this shows the board, the players moving around, and all other information
 * during the game.
 */
public class GameScreen implements Screen {

    private final PropertyTycoon game;
    private OrthographicCamera camera;
    private ScrollableStage stage;
    private Skin gameScreenSkin;
    private TiledMapTileLayer layer;
    private GameController gameCon;
    private SpriteBatch spriteBatch;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private Viewport view;
    private Stage labelStage;

    private TextButton rollDice;
    private Image die1;
    private Image die2;
    private Label currPlayerLabel;

    private Window propertyPopUpWindow;
    private Table propInfoBox;
    private Ownable clickedProperty;
    private TextButton buyPropertyButton;
    private TextButton sellPropertyButton;
    private TextButton mortgagePropertyButton;
    private TextButton auctionPropertyButton;
    private TextButton developPropertyButton;
    private TextButton closePropertyButton;
    private Label propNameLabel;
    private Label propOwnerLabel;
    private Label propCostLabel;
    private Label propHouseCostLabel;
    private Label propHotelCostLabel;
    private ArrayList<Label> developmentPrices;

    private Window servicePopUpWindow;
    private TextButton buyServiceButton;
    private TextButton sellServiceButton;
    private TextButton mortgageServiceButton;
    private TextButton auctionServiceButton;
    private TextButton closeServiceButton;
    private Label serviceNameLabel;
    private Label serviceOwnerLabel;
    private Label serviceCostLabel;
    private Table serviceInfoBox2;
    private Image serviceImg;

    private Window auctionPopUpWindow;
    private Player currBidder;
    private Player highestBidder;
    private ArrayList<Player> bidderList;
    private Label highestBidderNameLabel;
    private Label highestBid;
    private Label currBidderNameLabel;

    private Window jailPopUpWindow;

    private ArrayList<Label> playerBalanceLabels;

    private Texture oneHouseTexture;
    private Texture twoHouseTexture;
    private Texture threeHouseTexture;
    private Texture fourHouseTexture;
    private Texture hotelTexture;

    ArrayList<Sprite> propertyHouseAndHotelSprites;
    private ArrayList<Sprite> ownedProperties;

    ArrayList<Sprite> propertyIcons;

    private Sound rollDiceFX;
    private Sound popupSoundFX;

    private ClickListener clickListener;
    private TextField auctionBid;

    private float gameLength;
    private float reverseTime;
    private Label timerLabel;

    /**
     * the constructor for gameScreen
     * @param game the current game
     */
    public GameScreen(PropertyTycoon game) {
        this.game = game;
        stage = new ScrollableStage(this);

        Gdx.input.setInputProcessor(stage);

        gameLength = 0;
        reverseTime = 0;

        serviceInfoBox2 = new Table();
        serviceImg = new Image();

        die1 = new Image();
        die2 = new Image();

        gameScreenSkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        //SOUNDS
        rollDiceFX = Gdx.audio.newSound(Gdx.files.internal("sound/dice_roll.mp3"));
        popupSoundFX = Gdx.audio.newSound(Gdx.files.internal("sound/pop.mp3"));

        //TILED MAP INITIALIZATION
        tiledMap = new TmxMapLoader().load("board/board.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("Tile Layer 1");

        //GAME CONTROLLER
        gameCon = new GameController(layer);

        //TOKEN ADDED TO GO SCREEN
        spriteBatch = new SpriteBatch();
        for (Player p : game.players) {
            p.getPlayerToken().setPosition(p.getCurrentCoordinates().getX(), p.getCurrentCoordinates().getY());
        }

        // POP UP WINDOW SET UP
        propertyPopUpWindowSetUp();
        servicePopUpWindowSetUp();
        gameInfoTableSetUp();
        jailPopUpWindowSetUp();
        auctionPopUpWindowSetUp();

        // Stores textures for the development houses
        oneHouseTexture = new Texture(Gdx.files.internal("property-icons/1-house.png"));
        twoHouseTexture = new Texture(Gdx.files.internal("property-icons/2-house.png"));
        threeHouseTexture = new Texture(Gdx.files.internal("property-icons/3-house.png"));
        fourHouseTexture = new Texture(Gdx.files.internal("property-icons/4-house.png"));
        hotelTexture = new Texture(Gdx.files.internal("property-icons/hotel.png"));

        ownedProperties = new ArrayList<>();
        propertyIcons = new ArrayList<>();


        propertyHouseAndHotelSprites = new ArrayList<>();
        updatePropertyDevelopmentSprites();

        // Width and height of the monitor
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        view = new FitViewport(w, h, camera);
        labelStage = new Stage(view);
        camera.position.set(new Vector3(layer.getWidth() * layer.getTileWidth() / 2, layer.getHeight() * layer.getTileHeight() / 2, 0));
        camera.zoom = (float) (((64 * 90) / h) / 2);

        int angle = 0;
        for (int i = 0; i < 40; i++) {
            if (i == 1 || i == 11 || i == 21 || i == 31) {
                angle -= 90;
            }
            Tile tile = gameCon.getBoard().getTile(i);

            // handles graphics on tiles
            if (tile instanceof SmallTile) {
                Coordinate c = ((SmallTile) tile).getCenterLabelCoordinate();
                RotatableLabel label = new RotatableLabel(new Label(((SmallTile) tile).getTileName(), gameScreenSkin), c.getX(), c.getY(), angle, 1);
                labelStage.addActor(label);
            }
            if (tile instanceof Services) {
                propertyIcons.add(((Services) tile).getIcon());
            }
            if (tile instanceof Tax) {
                propertyIcons.add(((Tax) tile).getIcon());
            }
            if (tile instanceof PotLuck) {
                propertyIcons.add(((PotLuck) tile).getIcon());
            }
            if (tile instanceof OpportunityKnocks) {
                propertyIcons.add(((OpportunityKnocks) tile).getIcon());
            }
        }

        // Used to prevent board activity with windows open
        stage.addListener(clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (game.getPreferences().isFxEnabled()) {
                    popupSoundFX.play(game.getPreferences().getFxVolume());
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch(Exception e) {
                        e.getMessage();
                    }
                }


                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mouse);
                try {
                    Tile tile = gameCon.retTile(layer.getCell((((int) mouse.x) / 64), (((int) mouse.y) / 64)));
                    if(!(tile instanceof Chests)) {
                    openPopUpWindow(tile);
                    }
                }
                catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        setTileCellColors();
    }

    /**
     * SetTileCellColors looks at the PNG holding the tiles in order to select what tiles should be what colour based on the config
     */
    private void setTileCellColors() {
        TiledMapTileSet set = tiledMap.getTileSets().getTileSet(0);
        for (int i = 0; i < 40; i++) {

            GameBoard board = gameCon.getBoard();
            Tile t = board.getTile(i);

            int id = 0;

            if (t instanceof Property) {

                ArrayList<Coordinate> coords = board.getTile(i).getAllCoordinates();

                switch (((Property) t).getColourAsString().toUpperCase()) {
                    case "BLUE":
                        id = 15;
                        break;
                    case "SKY":
                        id = 16;
                        break;
                    case "YELLOW":
                        id = 17;
                        break;
                    case "GREEN":
                        id = 18;
                        break;
                    case "ORANGE":
                        id = 19;
                        break;
                    case "BROWN":
                        id = 20;
                        break;
                    case "PURPLE":
                        id = 21;
                        break;
                    case "RED":
                        id = 22;
                        break;
                    default:
                        id = 1;
                        break;

                }
                layer.getCell(coords.get(3).getX() / 64, coords.get(3).getY() / 64).setTile(set.getTile(id));
                layer.getCell(coords.get(7).getX() / 64, coords.get(7).getY() / 64).setTile(set.getTile(id));
                layer.getCell(coords.get(11).getX() / 64, coords.get(11).getY() / 64).setTile(set.getTile(id));
            }
        }
    }

    /**
     * show() defines the layout, elements and interactivity of the GUI
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     *  takes a colour and returns a TextureRegionDrawable filled with that colour
     * @param colour the colour to set the background
     * @return a TextureRegionDrawable based on the colour
     */
    private TextureRegionDrawable getColouredBackground(Color colour) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm.setColor(colour);
        pm.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pm)));
    }

    /**
     * openPopUpWindow opens a card styled window in the centre of the gameScreen to show the tile landed on/clicked on
     * @param tile The tile provides the type of window to open and the information to put in the window
     */
    private void openPopUpWindow(Tile tile) {
        if (tile instanceof Property) {
            clickedProperty = (Property) tile;

            // if property isnt owned, show buy and auction buttons
            if (clickedProperty.getPlayers().contains(gameCon.getCurrentPlayer()) && clickedProperty.getBuyable()) {
                buyPropertyButton.setVisible(true);
                auctionPropertyButton.setVisible(true);
            }
            // if property is owned, dont show buy and auction buttons
            else {
                buyPropertyButton.setVisible(false);
                auctionPropertyButton.setVisible(false);
            }

            if (clickedProperty.getOwner() == gameCon.getCurrentPlayer()) {
                sellPropertyButton.setVisible(true);
                // if mortgaged, allow player to unmortgage
                if ((clickedProperty).getMortgaged()) {
                    mortgagePropertyButton.setText("Unmortgage");
                } else {
                    mortgagePropertyButton.setText("Mortgage");
                }
                mortgagePropertyButton.setVisible(true);
                developPropertyButton.setVisible(true);
            } else {
                sellPropertyButton.setVisible(false);
                mortgagePropertyButton.setVisible(false);
                developPropertyButton.setVisible(false);
            }

            // players should be forced to buy or auction properties
            if (clickedProperty.getPlayers().contains(gameCon.getCurrentPlayer())) {
                closePropertyButton.setVisible(false);
            } else {
                closePropertyButton.setVisible(true);
            }

            if (clickedProperty.getOwned()) {
                closePropertyButton.setVisible(true);
            }

            propNameLabel.setText(clickedProperty.getTileName());
            propOwnerLabel.setText(clickedProperty.getOwnerName());
            propCostLabel.setText("$" + clickedProperty.getCost());
            propInfoBox.setBackground(getColouredBackground(((Property)clickedProperty).getColor()));

            for (int i = 0; i < ((Property)clickedProperty).getDevPrices().size(); i++) {
                developmentPrices.get(i).setText("$" + ((Property)clickedProperty).getDevPrices().get(i));
            }
            propHouseCostLabel.setText("$" + ((Property)clickedProperty).getHousePrice());
            propHotelCostLabel.setText("$" + ((Property)clickedProperty).getHotelPrice());

            closeAllWindows();
            propertyPopUpWindow.setVisible(true);
        }
        // tile information and styling handling
        else if (tile instanceof Services) {
            if (tile instanceof Station) {
                serviceInfoBox2.clear();
                serviceInfoBox2.add(new Label("Rent with one station owned:", gameScreenSkin)).left().width(350);
                serviceInfoBox2.add(new Label("$50", gameScreenSkin)).right();
                serviceInfoBox2.row().pad(20, 0, 0, 0);
                serviceInfoBox2.add(new Label("Rent with two stations owned:", gameScreenSkin)).left();
                serviceInfoBox2.add(new Label("$100", gameScreenSkin)).right();
                serviceInfoBox2.row().pad(20, 0, 0, 0);
                serviceInfoBox2.add(new Label("Rent with three stations owned:", gameScreenSkin)).left();
                serviceInfoBox2.add(new Label("$150", gameScreenSkin)).right();
                serviceInfoBox2.row().pad(20, 0, 0, 0);
                serviceInfoBox2.add(new Label("Rent with four stations owned:", gameScreenSkin)).left();
                serviceInfoBox2.add(new Label("$200", gameScreenSkin)).right();
                serviceInfoBox2.row().pad(20, 0, 0, 0);
                serviceImg.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("serviceImages/trainImage.png")))));
                servicePopUpWindowSetUp();
            }
            else if (tile instanceof Utility){
                serviceInfoBox2.clear();
                serviceInfoBox2.add(new Label("Rent with 1 utility owned:", gameScreenSkin)).left().width(270);
                serviceInfoBox2.add(new Label("4 times dice value", gameScreenSkin)).right();
                serviceInfoBox2.row().pad(20, 0, 0, 0);
                serviceInfoBox2.add(new Label("Rent with > 1 utility owned:", gameScreenSkin)).left();
                serviceInfoBox2.add(new Label("10 times dice value", gameScreenSkin)).right();
                serviceInfoBox2.row();
                serviceInfoBox2.add(new Label("", gameScreenSkin)).height(30);
                serviceImg.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("serviceImages/utilityImage.png")))));
                servicePopUpWindowSetUp();
            }

            clickedProperty = (Services) tile;
            if (clickedProperty.getPlayers().contains(gameCon.getCurrentPlayer()) && clickedProperty.getBuyable()) {
                buyServiceButton.setVisible(true);
                auctionServiceButton.setVisible(true);
            }
            else {
                buyServiceButton.setVisible(false);
                auctionServiceButton.setVisible(false);
            }

            if (clickedProperty.getOwner() == gameCon.getCurrentPlayer()) {
                sellServiceButton.setVisible(true);
                if (clickedProperty.getMortgaged()) {
                    mortgageServiceButton.setText("Unmortgage");
                }
                else {
                    mortgageServiceButton.setText("Mortgage");
                }
                mortgageServiceButton.setVisible(true);
            }
            else {
                sellServiceButton.setVisible(false);
                mortgageServiceButton.setVisible(false);
            }

            if (clickedProperty.getPlayers().contains(gameCon.getCurrentPlayer())) {
                closeServiceButton.setVisible(false);
            }
            else {
                closeServiceButton.setVisible(true);
            }

            if (clickedProperty.getOwned()) {
                closeServiceButton.setVisible(true);
            }

            serviceNameLabel.setText(clickedProperty.getTileName());
            serviceOwnerLabel.setText(clickedProperty.getOwnerName());
            serviceCostLabel.setText("$" + clickedProperty.getCost());

            closeAllWindows();
            servicePopUpWindow.setVisible(true);
        }
        else if(tile instanceof Jail &&gameCon.getCurrentPlayer().getIsInJail()) {
            closeAllWindows();
            jailPopUpWindow.setVisible(true);
        }
        else if(tile instanceof OpportunityKnocks) {
            closeAllWindows();
            if (tile.getPlayers().contains(gameCon.getCurrentPlayer())) {
                Card card = gameCon.getBoard().getLastCardPulled();
                if(card.getAction().equals("Advance to")) {
                    quickPopUpWindow(card.getAction() + " " + gameCon.getBoard().getTile(card.getValue()).getTileName(), 100, 400, 2);
                }
                else {
                    quickPopUpWindow(card.getCardMessage(), 100, 400, 2);
                }
            }
        }
        else if(tile instanceof PotLuck) {
            closeAllWindows();
            if (tile.getPlayers().contains(gameCon.getCurrentPlayer())) {
                Card card = gameCon.getBoard().getLastCardPulled();
                if(card.getAction().equals("Go back to")) {
                    quickPopUpWindow(card.getAction() + " " + gameCon.getBoard().getTile(card.getValue()).getTileName(), 100, 400, 2);
                }
                else if(card.getAction().equals("Take Opportunity Knocks card or pay a fine of")) {
                    choiceWindow(card);
                }
                else {
                    quickPopUpWindow(card.getCardMessage(), 100, 400, 2);
                }
            }
        }
        else if(tile instanceof Tax &&tile.getPlayers().contains(gameCon.getCurrentPlayer())) {
            closeAllWindows();
            quickPopUpWindow(gameCon.getCurrentPlayer().getName() + " paid $" + ((Tax) tile).getTaxAmount() + " worth of tax!", 100, 400, 2);
        }
        else if(tile instanceof FreeParking) {
            closeAllWindows();
            if (tile.getPlayers().contains(gameCon.getCurrentPlayer())) {
                quickPopUpWindow(gameCon.getCurrentPlayer().getName() + " picked up $" + ((FreeParking) tile).getCurrentValue() + "!", 100, 400, 2);
                ((FreeParking) gameCon.getBoard().getTile(20)).reset();
            } else {
                quickPopUpWindow("Free parking value stands at $" + ((FreeParking) tile).getCurrentValue(), 100, 400, 2);
            }
        }
        else if(tile instanceof GoToJail) {
            closeAllWindows();
        }
    }

    /**
     * closeAllWindows closes all window objects to clear the screen.
     */
    private void closeAllWindows() {
        propertyPopUpWindow.setVisible(false);
        servicePopUpWindow.setVisible(false);
        auctionPopUpWindow.setVisible(false);
        jailPopUpWindow.setVisible(false);
    }

    /**
     * initialises the propertyPopUpWindow and inserts all tables and buttons
     */
    private void propertyPopUpWindowSetUp() {
        buyPropertyButton = new TextButton("Buy", gameScreenSkin);
        sellPropertyButton = new TextButton("Sell", gameScreenSkin);

        mortgagePropertyButton = new TextButton("Mortgage", gameScreenSkin);
        auctionPropertyButton = new TextButton("Auction", gameScreenSkin);

        developPropertyButton = new TextButton("Develop", gameScreenSkin);
        closePropertyButton = new TextButton("Close", gameScreenSkin);

        propInfoBox = new Table();

        propNameLabel = new Label("", gameScreenSkin, "big");
        propNameLabel.setAlignment(Align.center);
        propOwnerLabel = new Label("", gameScreenSkin);
        propCostLabel = new Label("",gameScreenSkin);

        propInfoBox.add(propNameLabel).colspan(2).width(350);
        propInfoBox.row().pad(10, 0, 0, 0);
        propInfoBox.add(new Label("Owner:", gameScreenSkin)).left();
        propInfoBox.add(propOwnerLabel).right();
        propInfoBox.row().pad(5, 0, 0, 0);
        propInfoBox.add(new Label("Cost:", gameScreenSkin)).left();
        propInfoBox.add(propCostLabel).right();
        propInfoBox.setBackground(getColouredBackground(Color.WHITE));

        Table propInfoBox2 = new Table();

        propHouseCostLabel = new Label("", gameScreenSkin);
        propHotelCostLabel = new Label("", gameScreenSkin);

        developmentPrices = new ArrayList<>();
        for(int i=0; i<6; i++) {
            developmentPrices.add(new Label("", gameScreenSkin));
        }

        propInfoBox2.add(new Label("Rent:", gameScreenSkin)).left().width(350);
        propInfoBox2.add(developmentPrices.get(0)).right();
        propInfoBox2.row().pad(20, 0, 0, 0);
        propInfoBox2.add(new Label("Rent with 1 house:", gameScreenSkin)).left();
        propInfoBox2.add(developmentPrices.get(1)).right();
        propInfoBox2.row().pad(20, 0, 0, 0);
        propInfoBox2.add(new Label("Rent with 2 houses:", gameScreenSkin)).left();
        propInfoBox2.add(developmentPrices.get(2)).right();
        propInfoBox2.row().pad(20, 0, 0, 0);
        propInfoBox2.add(new Label("Rent with 3 houses:", gameScreenSkin)).left();
        propInfoBox2.add(developmentPrices.get(3)).right();
        propInfoBox2.row().pad(20, 0, 0, 0);
        propInfoBox2.add(new Label("Rent with 4 houses:", gameScreenSkin)).left();
        propInfoBox2.add(developmentPrices.get(4)).right();
        propInfoBox2.row().pad(20, 0, 0, 0);
        propInfoBox2.add(new Label("Rent with a hotel", gameScreenSkin)).left();
        propInfoBox2.add(developmentPrices.get(5)).right();
        propInfoBox2.row().pad(40, 0, 0, 0);
        propInfoBox2.add(new Label("House cost:", gameScreenSkin)).left();
        propInfoBox2.add(propHouseCostLabel).right();
        propInfoBox2.row().pad(20, 0, 0, 0);
        propInfoBox2.add(new Label("Hotel cost:", gameScreenSkin)).left();
        propInfoBox2.add(propHotelCostLabel).right();

        propertyPopUpWindow = new Window("", gameScreenSkin);

        propertyPopUpWindow.add(propInfoBox).colspan(2).expand().fill();
        propertyPopUpWindow.row().pad(25, 0, 0, 0);
        propertyPopUpWindow.add(propInfoBox2).colspan(2);
        propertyPopUpWindow.row().pad(25, 0, 0, 0);
        propertyPopUpWindow.add(buyPropertyButton).left();
        propertyPopUpWindow.add(auctionPropertyButton).right();
        propertyPopUpWindow.row().pad(10, 0, 0, 0);
        propertyPopUpWindow.add(sellPropertyButton).left().width(120);
        propertyPopUpWindow.add(mortgagePropertyButton).right().width(230);
        propertyPopUpWindow.row().pad(10, 0, 0, 0);
        propertyPopUpWindow.add(developPropertyButton).left();
        propertyPopUpWindow.add(closePropertyButton).right();
        propertyPopUpWindow.pack();

        float width = 425, height = 600;
        propertyPopUpWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        propertyPopUpWindow.setVisible(false);

        stage.addActor(propertyPopUpWindow);

        // buyProperty logic
        buyPropertyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameCon.getCurrentPlayer().completedFirstLap()) {
                    try {
                        if (clickedProperty.getBuyable() && clickedProperty.getPlayers().contains(gameCon.getCurrentPlayer())) {
                            if (gameCon.getCurrentPlayer().getMoney() >= clickedProperty.getCost()) {
                                clickedProperty.buyOwnable(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                                updatePropertyOwnerIcons();
                                closeAllWindows();
                                openPopUpWindow(clickedProperty);
                                rollDice.setVisible(true);
                            } else {
                                quickPopUpWindow("Not enough money", 100, 400, 0.5f);
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                else{
                    quickPopUpWindow("You have not gone around the board yet!", 100, 400, 2);
                }
            }
        });

        // initialises auction
        auctionPropertyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameCon.getCurrentPlayer().completedFirstLap()) {
                    auctionPopUpWindowSetUp(); //called to add the title and colour of the property to the auction window
                    auctionBid.setVisible(true);
                    currBidder = gameCon.getCurrentPlayer();
                    bidderList = new ArrayList<>(game.players);

                    for (int i = 0; i < bidderList.indexOf(currBidder) - 1; i++) {
                        bidderList.add(bidderList.get(i));
                        bidderList.remove(i);
                    }

                    currBidderNameLabel.setText(gameCon.getCurrentPlayer().getName());
                    closeAllWindows();
                    auctionPopUpWindow.setVisible(true);
                }
                else{quickPopUpWindow("You have not gone around the board yet!", 100, 400, 2);
                }
            }
        });

        // sellProperty logic
        sellPropertyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if((clickedProperty).getMortgaged()){
                    (clickedProperty).unmortgage(gameCon.getCurrentPlayer(), 0);
                    clickedProperty.sellOwnable(gameCon.getCurrentPlayer(), clickedProperty.getCost()/2);
                    updatePropertyOwnerIcons();
                    updatePropertyDevelopmentSprites();
                    closeAllWindows();
                }
                else {
                    clickedProperty.sellOwnable(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                    updatePropertyOwnerIcons();
                    updatePropertyDevelopmentSprites();
                    closeAllWindows();
                }
            }
        });

        //mortgage logic
        mortgagePropertyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(mortgagePropertyButton.getText().toString().equals("Mortgage")) {
                    (clickedProperty).setMortgaged(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                    mortgagePropertyButton.setText("Unmortgage");
                }
                else{
                    (clickedProperty).unmortgage(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                    mortgagePropertyButton.setText("Mortgage");
                }
            }
        });

        // checks if all properties of that colour are owned, and then allows houses and hotels to be built
        developPropertyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameCon.developProperty(((Property)clickedProperty), gameCon.getCurrentPlayer())) {
                    updatePropertyDevelopmentSprites();
                }
                else {
                    quickPopUpWindow("Not able to develop", 100, 400, 0.5f);
                }
            }
        });

        closePropertyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                propertyPopUpWindow.setVisible(false);
            }
        });
    }

    /**
     * sets up buttons and tables for serviceWindow
     */
    private void servicePopUpWindowSetUp() {
        buyServiceButton = new TextButton("Buy", gameScreenSkin);
        sellServiceButton = new TextButton("Sell", gameScreenSkin);

        mortgageServiceButton = new TextButton("Mortgage", gameScreenSkin);
        auctionServiceButton = new TextButton("Auction", gameScreenSkin);

        closeServiceButton = new TextButton("Close", gameScreenSkin);

        Table serviceInfoBox = new Table();

        serviceNameLabel = new Label("", gameScreenSkin, "big");
        serviceNameLabel.setAlignment(Align.center);
        serviceOwnerLabel = new Label("", gameScreenSkin);
        serviceCostLabel = new Label("",gameScreenSkin);

        serviceInfoBox.add(serviceNameLabel).colspan(2).width(350);
        serviceInfoBox.row().pad(10, 0, 0, 0);
        serviceInfoBox.add(new Label("Owner:", gameScreenSkin)).left();
        serviceInfoBox.add(serviceOwnerLabel).right();
        serviceInfoBox.row().pad(5, 0, 0, 0);
        serviceInfoBox.add(new Label("Cost:", gameScreenSkin)).left();
        serviceInfoBox.add(serviceCostLabel).right();

        servicePopUpWindow = new Window("", gameScreenSkin);

        servicePopUpWindow.add(serviceInfoBox).colspan(2).expand().fill();
        servicePopUpWindow.row().pad(20, 0, 0, 0);
        servicePopUpWindow.add(serviceImg).colspan(2).width(300).height(100);
        servicePopUpWindow.row().pad(60, 0, 0, 0);
        servicePopUpWindow.add(serviceInfoBox2).colspan(2);
        servicePopUpWindow.row().pad(20, 0, 0, 0);
        servicePopUpWindow.add(buyServiceButton).left();
        servicePopUpWindow.add(auctionServiceButton).right();
        servicePopUpWindow.row().pad(10, 0, 0, 0);
        servicePopUpWindow.add(sellServiceButton).left().width(120);
        servicePopUpWindow.add(mortgageServiceButton).right().width(230);
        servicePopUpWindow.row().pad(10, 0, 0, 0);
        servicePopUpWindow.add(closeServiceButton).colspan(2);
        servicePopUpWindow.pack();

        float width = 425, height = 600;
        servicePopUpWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        servicePopUpWindow.setVisible(false);

        stage.addActor(servicePopUpWindow);

        buyServiceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameCon.getCurrentPlayer().completedFirstLap()) {
                    try {
                        if (clickedProperty.getBuyable() && clickedProperty.getPlayers().contains(gameCon.getCurrentPlayer())) {
                            if (gameCon.getCurrentPlayer().getMoney() >= clickedProperty.getCost()) {
                                clickedProperty.buyOwnable(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                                updatePropertyOwnerIcons();
                                closeAllWindows();
                                openPopUpWindow(clickedProperty);
                                rollDice.setVisible(true);
                            } else {
                                quickPopUpWindow("Not enough money", 100, 400, 0.5f);
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                else{
                    quickPopUpWindow("You have not gone around the board yet!", 100, 400, 2);
                }
            }
        });

        auctionServiceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(gameCon.getCurrentPlayer().completedFirstLap()) {
                    auctionPopUpWindowSetUp(); //called to add the title and colour of the property to the auction window

                    currBidder = gameCon.getCurrentPlayer();
                    bidderList = new ArrayList<>(game.players);

                    for (int i = 0; i < bidderList.indexOf(currBidder) - 1; i++) {
                        bidderList.add(bidderList.get(i));
                        bidderList.remove(i);
                    }

                    currBidderNameLabel.setText(gameCon.getCurrentPlayer().getName());
                    closeAllWindows();
                    auctionPopUpWindow.setVisible(true);
                }
                else{
                    quickPopUpWindow("You have not gone around the board yet!", 100, 400, 2);
                }
            }
        });

        sellServiceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if((clickedProperty).getMortgaged()){
                    (clickedProperty).unmortgage(gameCon.getCurrentPlayer(), 0);
                    clickedProperty.sellOwnable(gameCon.getCurrentPlayer(), clickedProperty.getCost()/2);
                    updatePropertyOwnerIcons();
                    closeAllWindows();
                }
                else {
                    clickedProperty.sellOwnable(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                    updatePropertyOwnerIcons();
                    closeAllWindows();
                }
            }
        });

        mortgageServiceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(mortgageServiceButton.getText().toString().equals("Mortgage")) {
                    (clickedProperty).setMortgaged(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                    mortgageServiceButton.setText("Unmortgage");
                }
                else{
                    (clickedProperty).unmortgage(gameCon.getCurrentPlayer(), clickedProperty.getCost());
                    mortgageServiceButton.setText("Mortgage");
                }
            }
        });

        closeServiceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                servicePopUpWindow.setVisible(false);
            }
        });
    }

    /**
     * sets up auction property window and the property being auctioned
     */
    private void auctionPopUpWindowSetUp() {
        final Label highestBidderLabel = new Label("Highest bidder: ", gameScreenSkin, "big");
        highestBidderNameLabel = new Label("", gameScreenSkin, "big");
        Label atLabel = new Label(" at ", gameScreenSkin, "big");
        highestBid = new Label("", gameScreenSkin, "big");

        final Table highestBidderTable = new Table();
        highestBidderTable.add(highestBidderLabel);
        highestBidderTable.add(highestBidderNameLabel);
        highestBidderTable.add(atLabel);
        highestBidderTable.add(highestBid);

        highestBidderTable.setVisible(false);

        final Label currBidderLabel = new Label("Current bidder: ", gameScreenSkin, "big");
        currBidderNameLabel = new Label("", gameScreenSkin, "big");

        Table currBidderTable = new Table();
        currBidderTable.add(currBidderLabel);
        currBidderTable.add(currBidderNameLabel);

        auctionBid = new TextField("", gameScreenSkin);

        auctionBid.setMessageText("Enter Bid");
        auctionBid.setAlignment(Align.center);
        final TextButton bidButton = new TextButton("Bid", gameScreenSkin);
        final TextButton leaveButton = new TextButton("Leave", gameScreenSkin);

        Table auctionPopUpTable = new Table();

        if(clickedProperty != null) {
            if (clickedProperty instanceof Property) {
                auctionPopUpTable.setBackground(getColouredBackground(((Property) clickedProperty).getColor()));
            }
            auctionPopUpTable.add(new Label(clickedProperty.getTileName(), gameScreenSkin, "big"));
            auctionPopUpTable.row().pad(40, 0, 0, 0);
        }
        auctionPopUpTable.add(highestBidderTable);
        auctionPopUpTable.row().pad(40, 0, 0, 0);
        auctionPopUpTable.add(currBidderTable);
        auctionPopUpTable.row().pad(20, 0, 0, 0);
        auctionPopUpTable.add(auctionBid).colspan(2);
        auctionPopUpTable.row().pad(10, 0, 0, 0);
        auctionPopUpTable.add(bidButton).colspan(2);
        auctionPopUpTable.row().pad(10, 0, 0, 0);
        auctionPopUpTable.add(leaveButton).colspan(2);

        auctionPopUpWindow = new Window("", gameScreenSkin);
        auctionPopUpWindow.add(auctionPopUpTable).expand().fill();

        float width = 610, height = 500;
        auctionPopUpWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        auctionPopUpWindow.setVisible(false);

        stage.addActor(auctionPopUpWindow);

        auctionBid.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.toString(c).matches("^[0-9]");
            }
        });

        bidButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!auctionBid.getText().equals("")){
                    if (Integer.parseInt(auctionBid.getText()) > gameCon.getAuctionValue() && currBidder.getMoney() >= Integer.parseInt(auctionBid.getText())) {
                        gameCon.setAuctionValue(Integer.parseInt(auctionBid.getText()));
                        highestBidder = currBidder;
                        highestBid.setText("$" + gameCon.getAuctionValue());
                        highestBidderTable.setVisible(true);

                        auctionBid.setText("");

                        if(bidderList.indexOf(currBidder) < bidderList.size() - 1 ){
                            currBidder = bidderList.get(bidderList.indexOf(currBidder) + 1);
                        }
                        else
                        {
                            currBidder = bidderList.get(0);
                        }
                        highestBidderNameLabel.setText(highestBidder.getName());
                        currBidderNameLabel.setText(currBidder.getName());
                        if(currBidder instanceof Bot){
                            botBid();
                        }
                    }
                    else if (Integer.parseInt(auctionBid.getText()) <= gameCon.getAuctionValue()) {
                        quickPopUpWindow("Bid not high enough", 100, 400, 0.5f);
                    }
                    else if (currBidder.getMoney() < Integer.parseInt(auctionBid.getText())) {
                        quickPopUpWindow("Not enough money", 100, 400,0.5f);
                    }
                    if(bidderList.size() == 1 && gameCon.getAuctionValue() != 0) {
                        bidButton.setVisible(false);
                        currBidderNameLabel.setText(currBidder.getName());
                        leaveButton.setText("Buy");
                    }
                }
                currBidderNameLabel.setText(currBidder.getName());
            }
        });

        leaveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = bidderList.indexOf(currBidder);
                bidderList.remove(currBidder);
                if (bidderList.size() == 0 && highestBidder == null) {
                    auctionPopUpWindow.setVisible(false);
                    gameCon.setAuctionValue(0);
                    rollDice.setVisible(true);
                }

                if (bidderList.size() == 0 && highestBidder != null) {
                    auctionPopUpWindow.setVisible(false);
                    clickedProperty.buyOwnable(highestBidder, gameCon.getAuctionValue());
                    highestBidder = null;
                    updatePropertyOwnerIcons();
                    gameCon.setAuctionValue(0);
                    rollDice.setVisible(true);
                }

                if (bidderList.size() != 0) {
                    if (index < bidderList.size()) {
                        currBidder = bidderList.get(index);
                    } else {
                        currBidder = bidderList.get(0);
                    }
                    currBidderNameLabel.setText(currBidder.getName());
                    if(currBidder instanceof Bot){
                        botBid();
                    }
                }

                if(bidderList.size() == 1 && gameCon.getAuctionValue() != 0) {
                    bidButton.setVisible(false);
                    auctionBid.setVisible(false);
                    leaveButton.setText("Buy");
                }
            }
        });
    }


    /**
     * sets up info table on left of gamescreen showing current player and all player balances
     */
    private void gameInfoTableSetUp() {
        Table currPlayerTable = new Table();

        Label currPlayerTitle = new Label("Current player: ", gameScreenSkin, "title");
        currPlayerLabel = new Label(gameCon.getCurrentPlayer().getName(), gameScreenSkin, "title");

        currPlayerTable.add(currPlayerTitle).left().width(320);
        currPlayerTable.add(currPlayerLabel).right().width(170);

        Table diceTable = new Table();

        diceTable.add(die1).height(100).width(100).padRight(10);
        diceTable.add(die2).height(100).width(100);

        Table playerInfoTable = new Table();
        playerBalanceLabels = new ArrayList<>();
        for (Player player: game.players) {
            Label playerNameLabel = new Label(player.getName() + ": ", gameScreenSkin, "title");
            Label playerBalanceLabel = new Label("$"+ player.getMoney(), gameScreenSkin, "title");
            playerBalanceLabels.add(playerBalanceLabel);
            playerInfoTable.row().pad(10, 0, 0, 0);
            playerInfoTable.add(playerNameLabel).left().width(180);
            playerInfoTable.add(playerBalanceLabel).right().width(130);
        }

        Table gameInfoTable = new Table();

        rollDice = new TextButton("Roll dice", gameScreenSkin);
        TextButton centerButton = new TextButton("Recenter view", gameScreenSkin);
        TextButton pauseButton = new TextButton("Pause", gameScreenSkin);

        gameInfoTable.row().pad(10, 0, 0, 0);
        gameInfoTable.add(currPlayerTable).left();
        gameInfoTable.row().pad(10, 0, 0, 0);
        gameInfoTable.add(diceTable).left();
        gameInfoTable.row();
        gameInfoTable.add(playerInfoTable).left();
        gameInfoTable.row().pad(10, 0, 0, 0);
        gameInfoTable.add(rollDice).left();
        gameInfoTable.row().pad(40, 0, 0, 0);
        gameInfoTable.add(centerButton).left();
        gameInfoTable.row().pad(10, 0, 0, 0);
        gameInfoTable.add(pauseButton).left();

        gameInfoTable.setFillParent(true);
        gameInfoTable.left();
        gameInfoTable.padLeft(10);

        stage.addActor(gameInfoTable);

        Table timerTable = new Table();
        timerLabel = new Label("", gameScreenSkin, "title");
        timerTable.add(timerLabel).width(380);
        timerTable.setFillParent(true);
        timerTable.right().padRight(10);
        stage.addActor(timerTable);

        rollDice.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (rollDice.getText().toString().equals("Roll dice")) {
                    performRollDice();
                    if (!gameCon.getPlayAgain()) {
                        rollDice.setText("End turn");
                    }
                }
                else if (rollDice.getText().toString().equals("End turn")) {
                    endTurn();
                }
            }
        });

        // recenters the camera on the gamescreen
        centerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                camera.position.set(new Vector3(layer.getWidth() * layer.getTileWidth() / 2, layer.getHeight() * layer.getTileHeight() / 2, 0));
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game));
            }
        });
    }

    /**
     * handles rolling the dice and moving the current player
     */
    private void performRollDice() {
        if (game.getPreferences().isFxEnabled()) {
            rollDiceFX.play(game.getPreferences().getFxVolume());
            try {
                TimeUnit.MILLISECONDS.sleep(1750);
            } catch(Exception e) {
                e.getMessage();
            }
        }

        Tile tile = gameCon.playerTurn();

        Player p = gameCon.getCurrentPlayer();
        p.getPlayerToken().setPosition(p.getCurrentCoordinates().getX(), p.getCurrentCoordinates().getY());

        die1.setDrawable(getDiceImage(gameCon.getLastD1()));
        die2.setDrawable(getDiceImage(gameCon.getLastD2()));

        if (tile instanceof Ownable && ((Ownable) tile).getOwned() && ((Ownable) tile).getOwner() != gameCon.getCurrentPlayer()) {
            if (tile instanceof Property) {
                Property prop = (Property) tile;
                quickPopUpWindow(gameCon.getCurrentPlayer().getName() + " paid " + prop.getOwner().getName() + " $" + prop.getCurrentRent() + " for landing on " + prop.getTileName(), 100, 450, 3);
            } else if (tile instanceof Station) {
                Station stat = (Station) tile;
                quickPopUpWindow(gameCon.getCurrentPlayer().getName() + " paid " + stat.getOwner().getName() + " $" + stat.getRent() + " for landing on " + stat.getTileName(), 100, 450, 3);
            } else if (tile instanceof Utility) {
                Utility util = (Utility) tile;
                quickPopUpWindow(gameCon.getCurrentPlayer().getName() + " paid " + util.getOwner().getName() + " $" + util.getRent(gameCon.getLastD1() + gameCon.getLastD2()) + " for landing on " + util.getTileName(), 100, 450, 3);
            }
        }
        else if (!gameCon.getCurrentPlayer().isBot()){
            openPopUpWindow(tile);
        }

        if (tile instanceof Ownable) {
            if (!((Ownable) tile).getOwned()) {
                if(gameCon.getCurrentPlayer().completedFirstLap()) {
                    rollDice.setVisible(false);
                }
            }
        }
    }

    /**
     * handles bankruptcy, winning the game, bot turns and ending the turn
     */
    private void endTurn() {
        closeAllWindows();
        ArrayList<String> soldOwnables = new ArrayList<>();
        while (gameCon.getCurrentPlayer().getMoney() < 0 && gameCon.getCurrentPlayer().getOwnables().size() > 0){
            Ownable o = gameCon.getCurrentPlayer().getOwnables().get(0);
            o.sellOwnable(gameCon.getCurrentPlayer(), o.getCost());
            if(!soldOwnables.contains(o.getTileName())) {
                soldOwnables.add(o.getTileName());
            }
        }
        updatePropertyOwnerIcons();
        updatePropertyDevelopmentSprites();
        if(soldOwnables.size() > 0) {
            quickPopUpWindow("Had to sell " + Arrays.toString(soldOwnables.toArray()).replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\,", "and"), 150, 500, 2);
        }
        if(gameCon.getCurrentPlayer().getMoney() < 0) {
            game.players.remove(gameCon.getCurrentPlayer());
            gameCon.getPlayerOrder().remove(0);
            if (game.players.size() == 1) {
                game.getPreferences().setAbridged(false);
                congratsPopUpWindow(gameCon.getFinalStandings(game.players, 0, game.players.size()-1));
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenu(game));
                    }
                }, 7.5f);
            }
        }

        if(gameCon.getCurrentPlayer().getMoney() + gameCon.getCurrentPlayer().getTotalOwnableValue() <= 0) { //need to add a check to see if their cumulative property worth also results in < $0
            game.players.remove(gameCon.getCurrentPlayer());
            gameCon.getPlayerOrder().remove(0);
            if (game.players.size() == 1) {
                game.getPreferences().setAbridged(false);
                congratsPopUpWindow(gameCon.getFinalStandings(game.players, 0, game.players.size()-1));
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenu(game));
                    }
                }, 7.5f);
            }
        }
        gameCon.endTurn();
        die1.setDrawable(null);
        die2.setDrawable(null);
        currPlayerLabel.setText(gameCon.getCurrentPlayer().getName());
        rollDice.setText("Roll dice");
        if(gameCon.getCurrentPlayer().isBot()) {
            rollDice.setVisible(false);
            botTurn();
            updatePropertyDevelopmentSprites();
            updatePropertyOwnerIcons();
        }
    }

    /**
     * sets up buttons and tables within the jail window
     */
    private void jailPopUpWindowSetUp() {
        Label jailInfoLabel = new Label("You're in jail! Post your bail or roll a double on your next go!", gameScreenSkin, "title");
        jailInfoLabel.setWrap(true);
        jailInfoLabel.setWidth(875);
        jailInfoLabel.setAlignment(Align.center);
        TextButton buyOutOfJailButton = new TextButton("Buy way out of Jail for $50", gameScreenSkin);
        TextButton useJailFreeButton = new TextButton("Use your get out of jail free card", gameScreenSkin);

        Table jailPopUpTable = new Table();

        float width = 800, height = 350;

        jailPopUpTable.add(jailInfoLabel).width(width-50);
        jailPopUpTable.row().pad(10, 0, 0, 0);
        jailPopUpTable.add(buyOutOfJailButton);
        if(gameCon.getCurrentPlayer().hasGetOutOfJailFree()) {
            jailPopUpTable.row().pad(10, 0, 0, 0);
            jailPopUpTable.add(useJailFreeButton);
        }
        jailPopUpTable.pack();

        jailPopUpWindow = new Window("", gameScreenSkin);

        jailPopUpWindow.add(jailPopUpTable).expand().fill();

        jailPopUpWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        jailPopUpWindow.setVisible(false);

        stage.addActor(jailPopUpWindow);

        buyOutOfJailButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeAllWindows();
                Player p = gameCon.getCurrentPlayer();
                p.makePurchase(50);
                gameCon.freePlayerFromJail(p);
            }
        });

        useJailFreeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Player p = gameCon.getCurrentPlayer();
                closeAllWindows();
                p.removeGetOutOfJailFreeCard();
                gameCon.freePlayerFromJail(p);
            }
        });
    }

    /**
     * sets up the window to congratulate the winning player(s)
     * @param players the winning players
     */
    private void congratsPopUpWindow(ArrayList<Player> players) {
        Collections.reverse(players);
        Table congratsTable = new Table();
        Label congratsLabel = new Label("Congratulations!", gameScreenSkin, "title");
        congratsTable.add(congratsLabel);
        congratsTable.row().pad(10, 0, 0, 0);
        for(int i=0; i<players.size(); i++) {
            Player p = players.get(i);
            int value = 0;
            for(Ownable o : p.getOwnables()) {
                value += o.getCost();
            }
            value += p.getMoney();
            if(i == 0) {
                Table winnerTable = new Table();
                winnerTable.add(new Label("", gameScreenSkin)).width(625);
                winnerTable.row();
                winnerTable.add(new Label("First place goes to " + p.getName(), gameScreenSkin, "title"));
                winnerTable.row();
                winnerTable.add(new Label("Total wealth: $" + value, gameScreenSkin, "title"));
                winnerTable.setBackground(getColouredBackground(Color.GOLD));
                congratsTable.add(winnerTable);
                congratsTable.row().pad(20, 0, 0, 0);
            }
            else {
                congratsTable.add(new Label(i + 1 + ". " + p.getName() + " finished with $" + value, gameScreenSkin, "title"));
                congratsTable.row().pad(10, 0, 0, 0);
            }
        }
        timerLabel.setAlignment(Align.right);
        timerLabel.setText("Game over");
        Window congratsWindow = new Window("", gameScreenSkin);
        congratsWindow.add(congratsTable);
        float width = 650, height = 700;
        congratsWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        stage.addActor(congratsWindow);
        congratsWindow.setVisible(true);
    }

    /**
     * Updates the balance values shown in the gameInfoTable. Call this in render for frequent updates.
     */
    private void updateBalances(){
        for (int i = 0; i < game.players.size(); i++) {
            Player player = game.players.get(i);
            playerBalanceLabels.get(i).setText("$" + player.getMoney());
        }
    }

    /**
     * handles showing the correct image depending on the number rolled on the die
     * @param num the number to show on the D6
     * @return the image to show
     */
    private Drawable getDiceImage(int num) {
        Texture oneDie = new Texture(Gdx.files.internal("dice/oneDie.png"));
        Texture twoDie = new Texture(Gdx.files.internal("dice/twoDie.png"));
        Texture threeDie = new Texture(Gdx.files.internal("dice/threeDie.png"));
        Texture fourDie = new Texture(Gdx.files.internal("dice/fourDie.png"));
        Texture fiveDie = new Texture(Gdx.files.internal("dice/fiveDie.png"));
        Texture sixDie = new Texture(Gdx.files.internal("dice/sixDie.png"));
        switch (num) {
            case 1:
                return new TextureRegionDrawable(oneDie);
            case 2:
                return new TextureRegionDrawable(twoDie);
            case 3:
                return new TextureRegionDrawable(threeDie);
            case 4:
                return new TextureRegionDrawable(fourDie);
            case 5:
                return new TextureRegionDrawable(fiveDie);
            case 6:
                return new TextureRegionDrawable(sixDie);
        }
        return null;
    }

    /**
     * opens a window with a custom message and custom size for a variable amount of time in seconds
     * @param msg String of what the window should say
     * @param height window height in px
     * @param width window width in px
     * @param time number of seconds to show the window
     */
    private void quickPopUpWindow(String msg, float height, float width, float time) {
        Window quickPopUpWindow = new Window("", gameScreenSkin);
        final Label label = new Label(msg, gameScreenSkin, "big");
        label.setWrap(true);
        label.setAlignment(Align.center);
        quickPopUpWindow.add(label).width(width - 50);
        quickPopUpWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        stage.addActor(quickPopUpWindow);
        quickPopUpWindow.setVisible(true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                quickPopUpWindow.setVisible(false);
            }
        }, time);
    }

    /**
     * used in potluck and opportunity knocks for cards that require a choice
     * @param card the card to use for the window
     */
    private void choiceWindow(Card card) {
        Window choiceWindow = new Window("", gameScreenSkin);
        TextButton payFineButton = new TextButton("Pay Fine",gameScreenSkin);
        TextButton takeOpportunityKnocksButton = new TextButton("Take Card",gameScreenSkin);
        Table table = new Table();
        table.add(new Label("Take opportunity knocks card or pay a fine of $" + card.getValue(), gameScreenSkin, "big"));
        table.row().pad(10, 0, 0, 0);
        table.add(payFineButton);
        table.row().pad(10, 0, 0, 0);
        table.add(takeOpportunityKnocksButton);
        choiceWindow.add(table);
        float width = 800, height = 200;
        choiceWindow.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
        stage.addActor(choiceWindow);
        choiceWindow.setVisible(true);
        payFineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Card card = gameCon.getBoard().getLastCardPulled();
                if (card != null) {
                    try {
                        gameCon.getCurrentPlayer().makePurchase(card.getValue());
                        ((FreeParking) gameCon.getBoard().getTile(20)).addToPot(card.getValue());
                        choiceWindow.setVisible(false);

                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });

        takeOpportunityKnocksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Card card = gameCon.getBoard().getLastCardPulled();
                if (card != null) {
                    try {
                        gameCon.getBoard().drawOpportunityKnocksCard();
                        choiceWindow.setVisible(false);
                        quickPopUpWindow(gameCon.getBoard().getLastCardPulled().getCardMessage(),200,300,2);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });
    }

    /**
     * update the house sprites on the board's properties
     */
    public void updatePropertyDevelopmentSprites(){
        ArrayList<Property> developedProperties = gameCon.getDevelopedProperties();
        propertyHouseAndHotelSprites.clear();
        for (Property prop: developedProperties) {
            Sprite sprite;

            switch (prop.getHousesOwned()) {
                default:
                    sprite = null;
                    break;
                case 1:
                    sprite = new Sprite(oneHouseTexture);
                    break;
                case 2:
                    sprite = new Sprite(twoHouseTexture);
                    break;
                case 3:
                    sprite = new Sprite(threeHouseTexture);
                    break;
                case 4:
                    sprite = new Sprite(fourHouseTexture);
                    break;
                case 5:
                    sprite = new Sprite(hotelTexture);
                    break;
            }

            if (sprite != null) {
                sprite.setSize(192, 64);
                sprite.setOriginCenter();
                if (prop.getTilePos() < 11) {
                    sprite.rotate(-90);
                }
                else if (prop.getTilePos() < 21) {
                    sprite.rotate(-180);
                }
                else if (prop.getTilePos() < 31) {
                    sprite.rotate(-270);
                }
                sprite.setPosition(prop.getOwnableSpriteCoordinate().getX() - 192 / 2, prop.getOwnableSpriteCoordinate().getY() - 64 / 2);
                propertyHouseAndHotelSprites.add(sprite);
            }
        }
    }

    /**
     * updates the sprites above the properties that show who owns them
     */
    private void updatePropertyOwnerIcons(){
        ownedProperties.clear();

        for(Player p: game.players){

            ArrayList<Ownable> props = p.getOwnables(); // gets all the players owned properties

            for (Ownable property: props) {

                Sprite s = new Sprite(p.getPlayerToken().getTexture());
                s.setAlpha(0.5f);
                s.setOriginCenter();
                if (property.getTilePos() < 11){
                    s.setPosition(property.getOwnableSpriteCoordinate().getX()+32,property.getOwnableSpriteCoordinate().getY()-32);
                    s.rotate(-90);
                }   else   if (property.getTilePos() < 21){
                    s.setPosition(property.getOwnableSpriteCoordinate().getX()-32,property.getOwnableSpriteCoordinate().getY()-96);
                    s.rotate(-180);
                }   else   if (property.getTilePos() <31){
                    s.setPosition(property.getOwnableSpriteCoordinate().getX()-96,property.getOwnableSpriteCoordinate().getY()-32);
                    s.rotate(-270);
                }else{
                    s.setPosition(property.getOwnableSpriteCoordinate().getX()-32,property.getOwnableSpriteCoordinate().getY()+32);
                }
                ownedProperties.add(s);
            }
        }
    }

    public OrthographicCamera getCam() {
        return camera;
    }

    /**
     * handles bot turn logic
     */
    private void botTurn() {
        performRollDice();
        Player bot = gameCon.getCurrentPlayer();
        Tile tile = gameCon.getBoard().getTile(bot.getTilePosition());
        if(tile instanceof Ownable) {
            if(bot.getMoney() > 200 + ((Ownable) tile).getCost() && bot.completedFirstLap()) {
                ((Ownable) tile).buyOwnable(bot, ((Ownable) tile).getCost());
                if(((Ownable) tile).getOwner() == bot && tile instanceof Property){
                    gameCon.developProperty((Property) tile, bot);
                }
            }
        }
        if(tile instanceof Jail) {
            if (bot.getIsInJail() && bot.hasGetOutOfJailFree()){
                gameCon.freePlayerFromJail(bot);
                bot.removeGetOutOfJailFreeCard();
            } else if(bot.getIsInJail() && bot.getMoney() >= 50) {
                bot.makePurchase(50);
                gameCon.freePlayerFromJail(bot);
            }
        }
            endTurn();
            rollDice.setVisible(true);
    }

    /**
     * handles bot auction logic
     */
    private void botBid() {
        if (Math.round(gameCon.getAuctionValue() * 1.1f) < currBidder.getMoney() && bidderList.size() > 1) {
            double randDouble = Math.random();

            if (randDouble < 0.4) {
                gameCon.setAuctionValue(Math.round(gameCon.getAuctionValue() * 1.1f + 1));
                highestBidder = currBidder;
                highestBidderNameLabel.setText(currBidder.getName());
                highestBid.setText("$"+gameCon.getAuctionValue());
                quickPopUpWindow(currBidder.getName() + " says: I will raise you to $" + Integer.toString(gameCon.getAuctionValue()), 100, 300, 1);
                if (bidderList.indexOf(currBidder) < bidderList.size() - 1) {
                    currBidder = bidderList.get(bidderList.indexOf(currBidder) + 1);
                } else {
                    currBidder = bidderList.get(0);
                }
            }
            else {
                quickPopUpWindow(currBidder.getName() + " says: That's too much!", 100, 300, 1);
                int index = bidderList.indexOf(currBidder);
                bidderList.remove(currBidder);
                if (bidderList.size() != 0) {
                    if (index < bidderList.size()) {
                        currBidder = bidderList.get(index);
                    } else {
                        currBidder = bidderList.get(0);
                    }
                }
            }

            if (currBidder instanceof Bot) {
                botBid();
            }
            currBidderNameLabel.setText(currBidder.getName());
        }
        else if(highestBidder != null){
            auctionPopUpWindow.setVisible(false);
            clickedProperty.buyOwnable(highestBidder, gameCon.getAuctionValue());
            highestBidder = null;
            updatePropertyOwnerIcons();
            gameCon.setAuctionValue(0);
            rollDice.setVisible(true);
        }
        else{
            auctionPopUpWindow.setVisible(false);
            gameCon.setAuctionValue(0);
            rollDice.setVisible(true);
        }
    }

    /**
     * render() is called when the Screen should render itself
     * @param delta the time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        if(propertyPopUpWindow.isVisible() || servicePopUpWindow.isVisible() || jailPopUpWindow.isVisible() || auctionPopUpWindow.isVisible()) {
            stage.removeListener(clickListener);
        }
        else {
            stage.addListener(clickListener);
        }

        if(game.getPreferences().getAbridged() && gameLength >= game.getPreferences().getAbridgedLength() * 60) {
            if(gameCon.getCurrentPlayer() == game.players.get(0)) {
                game.getPreferences().setAbridged(false);
                congratsPopUpWindow(gameCon.getFinalStandings(game.players, 0, game.players.size() - 1));
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenu(game));
                    }
                }, 7.5f);
            }
            else {
                timerLabel.setAlignment(Align.right);
                timerLabel.setText("Final round");
            }
        }
        else if(game.getPreferences().getAbridged()) {
            gameLength += Gdx.graphics.getRawDeltaTime();
            reverseTime = (game.getPreferences().getAbridgedLength()*60) - gameLength;
            timerLabel.setText("Time left: " + LocalTime.MIN.plusSeconds(Math.round(reverseTime)).toString());
        }

        updateBalances();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();

        for (Sprite sprite: propertyHouseAndHotelSprites) {
            sprite.draw(spriteBatch);
        }

        for (Sprite sprite: ownedProperties) {
            sprite.draw(spriteBatch);
        }

        for (Sprite sprite: propertyIcons){
            sprite.draw(spriteBatch);
        }
        for (Player p : game.players) {
            p.getPlayerToken().draw(spriteBatch);
        }

        spriteBatch.end();

        labelStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        labelStage.draw();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when GameScreen() should release all resources
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
     * Called when this GameScreen() is no longer the current screen for PropertyTycoon()
     */
    @Override
    public void hide() {}
}