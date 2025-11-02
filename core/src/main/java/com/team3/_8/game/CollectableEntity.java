package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * This is a class inheriting from CollidableEntity which is for
 * entities that can be collected. It makes entities that have been overlapped with
 * collected and stops rendering them.
 * @author Lenny
 */
public class CollectableEntity extends CollidableEntity{

    private boolean collected;
    private String type;

    public CollectableEntity(Sprite sprite, float speed, String type) {
        super(sprite, speed);
        this.type = type;
        this.collected = false;
    }

    /**
     * Method that checks if the collision boxes of the collectable and of Bob
     * have overlapped - if so and Bob's inventory is not full, it adds itself
     * and flags itself as collected.
     * @param bob Bob
     */
    public boolean collides(Bob bob) {
        if (bob.getCollisionBox().overlaps(this.collisionBox) && !this.collected) {
            this.collected = bob.addInventory(this.type); // Calls the method which adds itself to Bob's inventory if it isn't full. Else it isn't collected
            return true;
        }
        return false;
    }

    /**
     * Overrides the draw class, to not draw the entity if it has been collected
     * @param batch sprite batch to be drawn
     */
    @Override
    public void draw(Batch batch){
        if (!this.collected){
            this.sprite.draw(batch);
        } else{
            this.dispose();
        }
    }



    @Override
    public void move(boolean[] movement_halter){}

}
