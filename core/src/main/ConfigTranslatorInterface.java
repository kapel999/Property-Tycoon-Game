package main;

import Tiles.Tile;
import misc.Card;

import java.util.ArrayList;

public interface ConfigTranslatorInterface {
    void genTiles();

    void genCards();

    ArrayList<Card> getOpportunityCards();

    ArrayList<Card> getPotluckChestCards();

    Tile[] getTiles();

    ArrayList<String> getTileIdentities();
}
