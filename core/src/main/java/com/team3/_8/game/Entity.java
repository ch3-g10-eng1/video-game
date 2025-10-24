package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;


/**
 * This is the class for all the entities in the game, to inherit from.
 * @author Lenny
 */
public class Entity {

    protected Sprite sprite;
    protected float speed;
    protected Rectangle collisionBox;

    /**
     * Creates the entity based on the sprite given and creates its collision box
     * @param sprite This is the sprite passed through, containing the position and the texture
     * @param speed The speed of the entity
     */
    public Entity(Sprite sprite, float speed) {
        this.sprite = sprite;
        this.speed = speed;
        this.createBox();
    }

    public void draw (Batch batch) {
        this.sprite.draw(batch); // Draws the sprite on the spritebatch provided
    }

    public void dispose () {
        this.sprite.getTexture().dispose();
    }

    public void move(boolean[] movement_halter){

    }

    public Sprite getEntity() {
        return sprite;
    }

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    protected void createBox(){
        this.collisionBox = new Rectangle(this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
    }
}
