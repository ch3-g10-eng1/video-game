package com.team3._8.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TextBubble {
    private final Sprite bubbleSprite;
    private final BitmapFont font;

    private boolean visible = false;
    private String text = "";
    private float width;
    private float height;

    /**
     * Creates text bubble object
     * @param bubbleTexture - bubble image
     * @param font - font for text
     * @param width - width of bubble
     * @param height - height of bubble
     */
    public TextBubble(Texture bubbleTexture, BitmapFont font, float width, float height){
        this.bubbleSprite = new Sprite(bubbleTexture);
        this.font = font;
        this.width = width;
        this.height = height;
    }

    /**
     * Allows text bubble text to be set
     * @param text, text to set
     * @return true if successful
     */
    public boolean setText(String text){
        this.text = text;
        return true;
    }

    /**
     * Configures visibility of text-bubble & text
     * @return visibility state, true if visible, otherwise false
     */
    public boolean hideShow(){
        visible = !visible;
        return visible;
    }

    /**
     * Draws text bubble
     * @param batch - batch to draw using
     * @param x - x-coordinate to draw at
     * @param y - y-coordinate to draw at
     */
    public void draw(SpriteBatch batch, float x, float y){
        // Draws the text and bubble if visible
        if (visible) {
            // Sets bubble position & draws
            bubbleSprite.setPosition(x, y);
            bubbleSprite.setSize(width, height);
            bubbleSprite.draw(batch);

            // Sets font colour & draws in text bubble
            font.setColor(Color.BLACK);
            // Calculates where to place text so within bubble
            font.draw(batch, text, x+(width * (30f/170f)), y+(height * (95f/120f)));
            font.setColor(Color.WHITE);
        }
    }
}
