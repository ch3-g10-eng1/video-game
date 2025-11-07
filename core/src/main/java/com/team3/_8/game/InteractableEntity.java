package com.team3._8.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.HashMap;
import java.util.Map;


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

    Map<String, Boolean> returnData = new HashMap<>();

    public InteractableEntity(Sprite sprite, float speed) {
        super(sprite, speed);
    }

    public Map<String, Boolean> collision(Bob bob){
        /**
         * Used to check for collision & runs interaction based on collision
         */
        if (bob.getCollisionBox().overlaps(this.collisionBox)) {
            // Returns any data from the interaction back to the call instance
            returnData = startInteraction();
            // return true;
        }
        else {
            returnData = stopInteraction();
            // return false;
        }
        return returnData;
    }

    /**
     * starts the interaction for when the player intereacts with the entity 
     * @return Map
     */
    public abstract Map<String, Boolean> startInteraction();
    public abstract Map<String, Boolean> stopInteraction();
    
    public abstract void draw();

    @Override
    public void move(boolean[] movement_halter) {
        // Not currently implemented, but useful if you want the entity to move
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
    
}
