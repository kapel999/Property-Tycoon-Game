package main;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bot extends Player {
    /**
     * The constructor for Bot
     * @param name  a String that is the name of the player
     * @param token a Sprite that is the game token that represents the player in the GUI
     */
    public Bot(String name, Sprite token) {
        super(name, token);
    }
}