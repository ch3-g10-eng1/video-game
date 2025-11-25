package com.team3._8.game.button;


import com.badlogic.gdx.math.Rectangle;

public class MenuButton {
    public Rectangle bounds;
    public String text;
    public Runnable onClick;
    public boolean hovered;

    public MenuButton(String text, float x, float y, float width, float height, Runnable onClick) {
        this.text = text;
        this.bounds = new Rectangle(x, y, width, height);
        this.onClick = onClick;
        this.hovered = false;
    }
}
