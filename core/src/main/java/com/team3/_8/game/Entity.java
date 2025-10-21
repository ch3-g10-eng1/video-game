package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Entity {

    private Sprite sprite;
    private float speed;
    private Rectangle collisionBox;

    public Entity(Sprite sprite, float speed) {
        this.sprite = sprite;
        this.speed = speed;
        this.createBox();
    }

    private void createBox(){
        float width = this.sprite.getWidth();
        float height = this.sprite.getHeight();

        this.collisionBox = new Rectangle(this.sprite.getX(), this.sprite.getY(), width, height);
    }
}
