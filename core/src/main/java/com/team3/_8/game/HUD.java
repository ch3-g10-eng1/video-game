package com.team3._8.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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

    public HUD(Batch HUDbatch, Texture keycard) {
        this.HUDbatch = HUDbatch;
        this.keycard = keycard;
    }

    /**
     * The method to draw the HUD
     * @param font BitmapFont: The font used to render the timer String
     * @param timer String: The formatted string of the timer
     * @param bob Bob: The Bob character, to extract the contents of his inventory from
     */
    public void draw(BitmapFont font, String timer, Bob bob){
        HUDbatch.begin();
        font.draw(HUDbatch, timer,550, 370);
        this.drawTextures(bob);
        HUDbatch.end();
    }

    /**
     * This method will draw the items collected on the screen
     * @param bob
     */
    private void drawTextures(Bob bob){
        String[] bobInventory;
        bobInventory = bob.getInventory();

        for (String item : bobInventory){
            switch (item){
                case "Keycard":
//                    System.out.println("Keycarding 'it'");
                    break;
                default:
//                    System.out.println("Nothing");
            }
        }
    }

}
