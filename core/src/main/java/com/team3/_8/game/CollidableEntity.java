package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;


/**
 * This is the class for all the entities with collision boxes, to inherit from
 * @author Lenny, Isaac
 */
abstract class CollidableEntity extends Entity {

    protected Rectangle collisionBox;

    /**
     * creates a collision box around the entity
     * @param sprite Sprite: the sprite of the entity
     * @param speed float: the speed of the entity
     */
    public CollidableEntity(Sprite sprite, float speed) {
        super(sprite, speed);
        this.createBox();
    }

    /**
     * Creates a collision box around the entity
     * @param sprite Sprite: the sprite of the entity
     * @param speed float: the speed of the entity
     * @param Collision__size_change float: the change in collision box size (must be negative for smaller)
     */
    public CollidableEntity(Sprite sprite, float speed, float Collision__size_change) {
        super(sprite, speed);
        this.createBox(Collision__size_change);
    }

    protected void createBox(){
        this.collisionBox = new Rectangle(this.sprite.getX(), this.sprite.getY(),
            this.sprite.getWidth(), this.sprite.getHeight());
    }

    /**
     * This is the override of the createBox method, allowing for the box to be altered when it is creates
     * @param widthChange float: the change of the width
     * @param heightChange float: the change of the height
     */
    protected void createBox(float Collision__size_change){
        this.collisionBox = new Rectangle(this.getX() - Collision__size_change, this.getY() - Collision__size_change, // No matter what I do here, the coordinates seem to be stuck in place, anyone know why??
            this.sprite.getWidth() + Collision__size_change*2, this.sprite.getHeight() + Collision__size_change*2);
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    @Override
    public void dispose(){
        this.sprite.getTexture().dispose();

        // This is to minimise the collision box, to avoid any potential collision box issues as much as possible
        this.collisionBox.width = 0;
        this.collisionBox.height = 0;
    }
}
