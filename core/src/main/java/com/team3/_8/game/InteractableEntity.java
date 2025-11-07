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

    /**
     * measures if this is colliding with another entity 
     * @param entity the other entity to be considered in the collision
     * @return a map of any event IDs and whether or not they are triggered
     */
    public Map<String, Boolean> collision(CollidableEntity entity){
        /**
         * Used to check for collision & runs interaction based on collision
         */
        if (entity.getCollisionBox().overlaps(this.collisionBox)) {
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
     * @return Map of eventIDs and whether or not it is triggered 
     */
    public abstract Map<String, Boolean> startInteraction();

    /**
     * stops the interaction with entity if the interaction can be stopped early 
     * @return map of eventIDs and whether or not they have been stopped
     */
    public abstract Map<String, Boolean> stopInteraction();
    
    @Override
    public void move(boolean[] movement_halter) {
        // Not currently implemented, but useful if you want the entity to move
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
    
}
