package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Set;
/**
 * This class creates the HUD of the game. The idea is that you pass textures that will be rendered
 * through to the class and then you can have all the decision-making in here, to clean up the code
 * and to avoid lots of passing textures through
 *
 * @author Lenny
 */
public class HUD {

    private Batch HUDbatch;
    private Texture keycard;
    private Texture pause;
    private float StartingY;

    public HUD(Batch HUDbatch) {
        this.HUDbatch = HUDbatch;
        this.keycard = new Texture("Keycard.png");
        this.pause = new Texture("libgdx.png");
        this.StartingY = 325f;
    }

    /**
     * The method to draw the HUD
     * @param font BitmapFont: The font used to render the timer String
     * @param timer String: The formatted string of the timer
     * @param bob Bob: The Bob character, to extract the contents of his inventory from
     */
    public void draw(BitmapFont font, String timer, Bob bob){
        this.HUDbatch.begin();
        font.draw(this.HUDbatch, timer,550, 370);
        this.drawTextures(bob);
        this.HUDbatch.end();
    }

    /**
     * This method will draw the items collected on the screen
     * @param bob Bob: The bob object so we can have a look at his inventory
     */
    private void drawTextures(Bob bob){
        Set<String> bobInventory;
        bobInventory = bob.getInventory();

        for (String item : bobInventory){
            switch (item){
                case "Keycard":
                    this.HUDbatch.draw(this.keycard, 10, 325, 50, 50);
                    break;
                default:
//                    System.out.println("Nothing");
            }
            this.StartingY -= 50;
        }
    }

    public void pauseScreen(BitmapFont font, Viewport viewport){
        this.HUDbatch.begin();
        font.draw(this.HUDbatch, "PAUSED",350 , 375);
        this.HUDbatch.draw(this.pause, (float) viewport.getScreenX() / 2, (float) viewport.getScreenY() / 2, 100, 100);
        this.HUDbatch.end();
    }

}
