package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * An object to create an entity which can be interacted with on collision
 * @author Henry
 */
abstract class InteractableEntity extends CollidableEntity{
    /**
     * 
     * @param sprite
     * @param speed
     */

    public InteractableEntity(Sprite sprite, float speed) {
        super(sprite, speed);
    }

    public boolean collision(Bob bob){
        /**
         * Used to check for collision & runs interaction based on collision
         */
        if (bob.getCollisionBox().overlaps(this.collisionBox)) {
            // System.out.println("Collision");
            startInteraction();
            return true;
        }
        else {
            stopInteraction();
            return false;
        }
    }

    public abstract void startInteraction();
    public abstract void stopInteraction();
    
    public abstract void draw();

    @Override
    public void move(boolean[] movement_halter) {
        // Not currently implemented, but useful if you want the entity to move
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
    
}
