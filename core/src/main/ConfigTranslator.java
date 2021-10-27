package main;

import Tiles.*;
import misc.Card;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * Translates provided configuration file into game Tile and Card objects for the board.
 */
public class ConfigTranslator implements ConfigTranslatorInterface {

    private Tile[] tileList = new Tile[40];
    private ArrayList<ArrayList<Card>> cardDecks;
    private ArrayList<String> tileIdentities;
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    private Document document;

    /**
     * Initialises instance variables and builds node lists
     */
    public ConfigTranslator() {
        try {
            File file = new File("config/property-config.xml");
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            document = docBuilder.parse(file);
            document.getDocumentElement().normalize();
            tileIdentities = new ArrayList<>();
            genTiles();
            genCards();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a list of set up Tile objects for the game board.
     */
    @Override
    public void genTiles() {
        NodeList tileNodeList = document.getElementsByTagName("tile");
        for (int i = 0; i < 40; i++) {
            Node aNode = tileNodeList.item(i);
            if (aNode.getNodeType() == Node.ELEMENT_NODE) {
                Element tileElement = (Element) aNode;
                Tile tile = new Tile();
                try {
                    switch (tileElement.getElementsByTagName("type").item(0).getTextContent()) {
                        case "property":
                            List<String> houses = Arrays.asList("rent", "one-house", "two-house", "three-house", "four-house", "hotel");
                            tile = new Property();
                            tile.setTileName(tileElement.getElementsByTagName("name").item(0).getTextContent());
                            ((Property) tile).setBuyable(true);
                            ((Property) tile).setCost(Integer.parseInt(tileElement.getElementsByTagName("cost").item(0).getTextContent()));
                            String colour = tileElement.getElementsByTagName("colour").item(0).getTextContent().toUpperCase();
                            ((Property) tile).setColour(colour);
                            if(!tileIdentities.contains(colour)) {
                                tileIdentities.add(colour);
                            }
                            ((Property) tile).setHousePrice(Integer.parseInt(tileElement.getElementsByTagName("house-cost").item(0).getTextContent().toUpperCase()));
                            ((Property) tile).setHotelPrice(Integer.parseInt(tileElement.getElementsByTagName("hotel-cost").item(0).getTextContent().toUpperCase()));
                            for (String house : houses) {
                                ((Property) tile).addDevPrice(Integer.parseInt(tileElement.getElementsByTagName(house).item(0).getTextContent()));
                            }
                            break;
                        case "go":
                            tile = new Go();
                            break;
                        case "jail":
                            tile = new Jail();
                            break;
                        case "tax":
                            tile = new Tax();
                            (tile).setTileName(tileElement.getElementsByTagName("name").item(0).getTextContent());
                            ((Tax) tile).setTaxAmount(Integer.parseInt(tileElement.getElementsByTagName("amount").item(0).getTextContent()));
                            break;
                        case "opportunityknocks":
                            tile = new OpportunityKnocks();
                            break;
                        case "station":
                            tile = new Station();
                            (tile).setTileName(tileElement.getElementsByTagName("name").item(0).getTextContent());
                            ((Station)tile).setBuyable(true);
                            ((Station) tile).setCost(Integer.parseInt(tileElement.getElementsByTagName("cost").item(0).getTextContent()));
                            if(!tileIdentities.contains("STATION")) {
                                tileIdentities.add("STATION");
                            }
                            break;
                        case "parking":
                            tile = new FreeParking();
                            break;
                        case "gotojail":
                            tile = new GoToJail();
                            break;
                        case "potluck":
                            tile = new PotLuck();
                            break;
                        case "utility":
                            tile = new Utility();
                            (tile).setTileName(tileElement.getElementsByTagName("name").item(0).getTextContent());
                            ((Utility) tile).setCost(Integer.parseInt(tileElement.getElementsByTagName("cost").item(0).getTextContent()));
                            ((Utility)tile).setBuyable(true);
                            if(!tileIdentities.contains("UTILITY")) {
                                tileIdentities.add("UTILITY");
                            }
                            break;
                    }
                    tile.setTilePos(i);
                    tileList[i] = tile;
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    /**
     * Generates lists of set up Card objects for the community chest and potluck draws.
     */
    @Override
    public void genCards() {
        ArrayList<NodeList> cardNodeLists = new ArrayList<>();
        cardDecks = new ArrayList<>();
        cardNodeLists.add(document.getElementsByTagName("potluckcard"));
        cardNodeLists.add(document.getElementsByTagName("opportunityknockscard"));
        for (NodeList nodes : cardNodeLists) {
            ArrayList<Card> cardList = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node aNode = nodes.item(i);
                if (aNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element cardElement = (Element) aNode;
                    Card card = new Card();
                    try {
                        card.setAction(cardElement.getElementsByTagName("action").item(0).getTextContent());
                        card.setValue(Integer.parseInt(cardElement.getElementsByTagName("value").item(0).getTextContent()));
                        cardList.add(card);
                    } catch (Exception e) {
                    }
                }
            }
            cardDecks.add(cardList);
        }
    }

    /**
     * Gets the list of set up opportunity knocks cards.
     * @return ArrayList of cards.
     */
    @Override
    public ArrayList<Card> getOpportunityCards() {
        return cardDecks.get(1);
    }

    /**
     * Gets the list of set up potluck cards.
     * @return ArrayList of cards.
     */
    @Override
    public ArrayList<Card> getPotluckChestCards() {
        return cardDecks.get(0);
    }

    /**
     * Gets the list of set up tiles.
     * @return Array of tiles.
     */
    @Override
    public Tile[] getTiles() {
        return tileList;
    }

    /**
     * Returns the array list of tile identities which uniquely identify each tile in the game allowing for develop
     * logic to be implemented
     * @return returns the array list tile identities
     */
    @Override
    public ArrayList<String> getTileIdentities() {
        return tileIdentities;
    }
}