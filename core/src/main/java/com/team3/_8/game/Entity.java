package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * This is the class for all the entities in the game, to inherit from.
 * @author Lenny
 */
abstract class Entity {

    protected Sprite sprite;
    protected float speed;

    /**
     * Creates the entity based on the sprite given and creates its collision box
     * @param sprite This is the sprite passed through, containing the position and the texture
     * @param speed The speed of the entity
     */
    public Entity(Sprite sprite, float speed) {
        this.sprite = sprite;
        this.speed = speed;
    }

    /**
     * Draws the sprite on the spritebatch provided
     * @param batch sprite batch to be drawn
     */
    public void draw (Batch batch) {
        this.sprite.draw(batch); // Draws the sprite on the spritebatch provided
    }

    /**
     * disposes of the sprite texture
     */
    public void dispose () {
        this.sprite.getTexture().dispose();
    }

    /**
     * This method controls the movement of the entity
     * @param movement_halter the directions that bob cannot move, false allowing movement
     *      0-left, 1-top, 2-right, 3-bottom
     */
    public abstract void move(boolean[] movement_halter);

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
}
