package com.team3._8.game;

// import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Set;
import java.util.Map;
/**
 * This class creates the HUD of the game. The idea is that you pass textures that will be rendered
 * through to the class and then you can have all the decision-making in here, to clean up the code
 * and to avoid lots of passing textures through
 * 
 * @author Lenny
 */
public class HUD {

    private Batch HUDbatch;
    private Texture keycard, pause;

    public HUD(Batch HUDbatch, int events) {
        this.HUDbatch = HUDbatch;
        this.keycard = new Texture("keycard.png");
        this.pause = new Texture("libgdx.png");
    }

    /**
     * The method to draw the HUD
     * @param font BitmapFont: The font used to render the timer String
     * @param timer String: The formatted string of the timer
     * @param bob Bob: The Bob character, to extract the contents of his inventory from
     */
    public void draw(BitmapFont font, String timer, Map<String, Integer> events, 
                     Bob bob, boolean isPaused, Viewport viewport){

        float X = viewport.getScreenX();
        float Y = viewport.getScreenY();
        float W = viewport.getScreenWidth();
        float H = viewport.getScreenHeight();

        Matrix4 previous = this.HUDbatch.getProjectionMatrix().cpy();
        Matrix4 ortho = new Matrix4().setToOrtho2D(0, 0, W, H);
        this.HUDbatch.setProjectionMatrix(ortho);
        GlyphLayout layout = new GlyphLayout();

        String[] HUDText = {timer, "Positive: " + events.get("Positive"), "Negative: " + events.get("Negative"), "Hidden: " + events.get("Hidden")};
        float y = H - 10f - font.getCapHeight();
        font.getData().setScale((0.0015625f)*W);
        
        this.HUDbatch.begin();
        for (String text : HUDText){
            layout.setText(font, text);
            float x = W - 10f - layout.width;

            font.draw(this.HUDbatch, text, X + x, Y + y);
            y -= font.getLineHeight();
        }

        this.HUDbatch.setProjectionMatrix(previous);
        font.getData().setScale(1);
        this.drawTextures(bob, font, isPaused);
        this.HUDbatch.end();
    }

    /**
     * This method will draw the items collected on the screen
     * @param bob Bob: The bob object so we can have a look at his inventory
     */
    private void drawTextures(Bob bob, BitmapFont font, boolean isPaused){
        Set<String> bobInventory;
        bobInventory = bob.getInventory();

        for (String item : bobInventory){
            switch (item){ // Switch so that more items can efficiently be added, 
                           //although its just keycard rn
                case "Keycard":
                    this.HUDbatch.draw(this.keycard, 10, 325, 50, 50);
                    if (isPaused){font.draw(this.HUDbatch, 
                        "This keycard can be used to unlock something...", 10, 325);}
                    break;
                default:
            }
        }
    }

    /**
     * draws pause screen onto the current active window
     * @param font the font of any text on the pause screen to be used
     * @param viewport the current viewport of the game 
     */
    public void pauseScreen(BitmapFont font, Viewport viewport){
        this.HUDbatch.begin();
        font.draw(this.HUDbatch, "PAUSED",340 , 375);
        this.HUDbatch.draw(this.pause, (float) viewport.getScreenX() / 2, 
            (float) viewport.getScreenY() / 2, 100, 100);
        this.HUDbatch.end();
    }
}
