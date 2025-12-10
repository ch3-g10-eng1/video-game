package com.team3._8.game.menu.button;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class MenuButton {
    public Rectangle bounds;
    public String text;
    public Runnable onClick;

    public boolean hovered = false;
    public boolean clicked = false;

    private final TextureRegion idle, hover, pressedTexture;

    public MenuButton(String text,
                      float x, float y,
                      float width, float height,
                      TextureRegion idle, TextureRegion hover, TextureRegion pressedTexture,
                      Runnable onClick) {
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);

        this.idle = idle;
        this.hover = hover;
        this.pressedTexture = pressedTexture;


        this.onClick = onClick;
    }

    public TextureRegion getFrame() {
        if (clicked && pressedTexture != null) {
            return pressedTexture;
        }

        if (hovered) {
            return hover;
        }

        return idle;
    }
}
