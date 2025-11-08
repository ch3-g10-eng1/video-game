package com.team3._8.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * class for a text bubble that can be spawed by events 
 * @author Henry 
 */
public class TextBubble {
    private final Sprite bubbleSprite;
    private final BitmapFont font;
    private String text = "";
    private boolean visible = false;
    private float width;
    private float height;

    public TextBubble(Texture bubbleTexture, BitmapFont font, float width, float height){
        this.bubbleSprite = new Sprite(bubbleTexture);
        this.font = font;
        this.width = width;
        this.height = height;
    }

    public boolean setText(String text){
        this.text = text;
        return true;
    }

    public boolean hideShow(){
        visible = !visible;
        return visible;
    }

    public void draw(SpriteBatch batch, float x, float y){
        if (visible) {
            bubbleSprite.setPosition(x, y);
            bubbleSprite.setSize(width, height);
            bubbleSprite.draw(batch);
            font.setColor(Color.BLACK);
            font.draw(batch, text, x+(width * (30f/170f)), y+(height * (95f/120f)));
            font.setColor(Color.WHITE);
        }
    }
}
