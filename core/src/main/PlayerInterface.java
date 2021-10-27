package main;

import Tiles.Ownable;
import com.badlogic.gdx.graphics.g2d.Sprite;
import misc.Coordinate;

import java.util.ArrayList;

public interface PlayerInterface {
    boolean isBot();

    void addGetOutOfJailFreeCard();

    void removeGetOutOfJailFreeCard();

    String getName();

    Sprite getPlayerToken();

    void setPlayerToken(Sprite token);

    void setTilePosition(int i);

    int getTilePosition();

    ArrayList<Ownable> getOwnables();

    int getTotalOwnableValue();

    void addOwnable(Ownable ownable);

    void removeOwnable(Ownable ownable);

    int getMoney();

    void setMoney(int amount);

    void payPlayer(int amount);

    void makePurchase(int cost);

    boolean hasGetOutOfJailFree();

    void setInJail(boolean isInJail);

    boolean getIsInJail();

    void setFirstLap(boolean lap);

    boolean completedFirstLap();

    Coordinate getCurrentCoordinates();

    void setCurrentCoordinates(Coordinate currentCoordinate);

    int getWealth();
}