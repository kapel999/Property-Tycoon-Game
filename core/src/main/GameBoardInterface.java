package main;

import Tiles.Ownable;
import Tiles.Property;
import Tiles.Services;
import Tiles.Tile;
import misc.Card;

import java.util.ArrayList;
import java.util.Map;

public interface GameBoardInterface {
    Map<String, ArrayList<Ownable>> getIdentityOwnablesMap();

    Boolean playerTurn(Player player);

    int getPlayerPos(Player player);

    void setPlayerPos(Player player, int pos);

    void movePlayer(Player player, int moves);

    Tile getTile(int i);

    void sendToJail(Player player);

    Boolean checkBoardCircumstances();

    void performCardAction(Card card);

    int getLastD1();

    int getLastD2();

    void checkForDevelopedProperties();

    ArrayList<Property> getDevelopedProperties();

    int howManyStationUtilityDoesPlayerOwn(Player playerOwner, Services tile);

    Integer[] getNumberOfHousesOwned(Player player);

    Card getLastCardPulled();

    void drawPotluckCard();

    void drawOpportunityKnocksCard();
}
