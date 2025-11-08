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
     * @param Collision__size_change float: the change in collision box size 
     *      (must be negative for smaller)
     */
    public CollidableEntity(Sprite sprite, float speed, float Collision__size_change) {
        super(sprite, speed);
        this.createBox(Collision__size_change);
    }

    /**
     * creates a collision box for the entity with the sprite information
     */
    protected void createBox(){
        this.collisionBox = new Rectangle(this.sprite.getX(), this.sprite.getY(),
            this.sprite.getWidth(), this.sprite.getHeight());
        System.out.println(this.sprite.getWidth());
    }

    /**
     * This is the alternative of the createBox method, 
     * allowing for the box to be altered when it is creates
     * @param widthChange float: the change of the width
     * @param heightChange float: the change of the height
     */
    protected void createBox(float Collision__size_change){
        this.collisionBox = new Rectangle(this.getX() - Collision__size_change, 
            this.getY() - Collision__size_change, 
            this.sprite.getWidth() + Collision__size_change*2, 
            this.sprite.getHeight() + Collision__size_change*2);
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    @Override
    public void dispose(){
        this.sprite.getTexture().dispose();

        // This is to minimise the collision box, 
        // to avoid any potential collision box issues as much as possible
        this.collisionBox.width = 0;
        this.collisionBox.height = 0;
    }

    /**
     * updates the collision box of the entity, should be called whenether move finishes
     */
    protected void updateCollisionBox(){
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }
    /**
     * updates the collision box of the entity, should be called whenether move finishes
     * @param Collision__size_change float: the change in collision box size 
     *      (must be negative for smaller)
     */
    protected void updateCollisionBox(float collision__size_change){
        this.collisionBox.setX(this.sprite.getX() - collision__size_change);
        this.collisionBox.setY(this.sprite.getY() - collision__size_change);
    }

    /**
     * This method controls the movement of the entity, updateCollisionBox should be called at end
     * @param movement_halter the directions that bob cannot move, false allowing movement
     *      0-left, 1-top, 2-right, 3-bottom
     */
    @Override
    public abstract void move(boolean[] movement_halter);
}
