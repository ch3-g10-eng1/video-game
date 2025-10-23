package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;


/**
 * This is the class for all the entities with collision boxes, to inherit from
 * @author Lenny, Isaac
 */
abstract class CollidableEntity extends Entity {

    protected Rectangle collisionBox;

    public CollidableEntity(Sprite sprite, float speed) {
        super(sprite, speed);
        this.createBox();
    }

    protected void createBox(){
        this.collisionBox = new Rectangle(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
    }
}
