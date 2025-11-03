package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the class for the character (Bob), inheriting from Entity
 * @author Lenny, Henry
 */
public class Bob extends CollidableEntity {

    private String[] inventory = {"","","","",""};
    // Creates an inventory of size 5, which will be filled with String IDs
    private int inventoryEnd = 0;
    private float collision__size_change; // offset for collision size, required for move()

    private TextureAtlas atlas;
    private Map<String, Animation<TextureRegion>> bob_animations;
    // holds the name of the animation linked to the animation

    // Used to control animation time
    float stateTime = 0f;

    public Bob(Sprite sprite, float speed){
        super(sprite, speed);
        loadTextures();
    }

    public Bob(Sprite sprite, float speed, float collision__size_change){
        super(sprite, speed, collision__size_change);        
        this.collision__size_change = collision__size_change;
        loadTextures();
    }

    /**
     * This method controls the movement of Bob, by moving him around the axis,
     * depending on the input
     * @param movement_halter the directions that bob cannot move, false allowing movement
     *      0-left, 1-top, 2-right, 3-bottom
     */
    @Override
    public void move(boolean[] movement_halter){
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += Gdx.graphics.getDeltaTime();
        // Default animation if no movement
        TextureRegion current_animation = bob_animations.get("Front").getKeyFrame(stateTime, true);

        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
             (Gdx.input.isKeyPressed(Input.Keys.D)))
              && !movement_halter[0]) {
            this.sprite.translateX(this.speed * delta);
            current_animation = bob_animations.get("Right").getKeyFrame(stateTime, true);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) ||
                  (Gdx.input.isKeyPressed(Input.Keys.A)))
                   && !movement_halter[2]) {
            this.sprite.translateX(-speed * delta);
            current_animation = bob_animations.get("Left").getKeyFrame(stateTime, true);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) ||
             (Gdx.input.isKeyPressed(Input.Keys.W)))
              && !movement_halter[3]) {
            this.sprite.translateY(speed * delta);
            current_animation = bob_animations.get("Up").getKeyFrame(stateTime, true);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) ||
                (Gdx.input.isKeyPressed(Input.Keys.S)))
                && !movement_halter[1]) {
            this.sprite.translateY(-speed * delta);
            current_animation = bob_animations.get("Front").getKeyFrame(stateTime, true);
        }
        
        // Sets the collision box of bob after he moves 
        this.collisionBox.setX(this.sprite.getX() - collision__size_change);
        this.collisionBox.setY(this.sprite.getY() - collision__size_change);

        // Sets bob sprite to run the animation configured above
        sprite.setRegion(current_animation);
    }

    /**
     * loads the animation files from atlas into the animation variables
     */
    public void loadTextures(){
        atlas = new TextureAtlas(Gdx.files.internal("assets\\atlas\\bob.atlas"));

        bob_animations = new HashMap<String, Animation<TextureRegion>>();
        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frontFrames = atlas.findRegions("front-bob");
        Array<TextureAtlas.AtlasRegion> rightFrames = atlas.findRegions("side-bob");
        Array<TextureAtlas.AtlasRegion> upFrames = atlas.findRegions("up-bob");
        Array<TextureAtlas.AtlasRegion> squashFrames = atlas.findRegions("squash-bob");
        Array<TextureAtlas.AtlasRegion> rocketFrames = atlas.findRegions("rocket-bob");

        // Creates animation object
        bob_animations.put("Front", new Animation<TextureRegion>(0.5f, frontFrames));
        bob_animations.put("Right", new Animation<TextureRegion>(0.5f, rightFrames));
        bob_animations.put("Up", new Animation<TextureRegion>(0.5f, upFrames));
        bob_animations.put("Squash", new Animation<TextureRegion>(0.5f, squashFrames));
        bob_animations.put("Rocket", new Animation<TextureRegion>(0.5f, rocketFrames));

        // Flips right into left frames
        Array<TextureRegion> leftFrames = new Array<>();
        for (TextureRegion frame: rightFrames){
            TextureRegion temp_frame = new TextureRegion(frame);
            temp_frame.flip(true, false);
            leftFrames.add(temp_frame);
        }
        bob_animations.put("Left", new Animation<TextureRegion>(0.5f, leftFrames));
    }

    /**
     * function to add item to bobs inventory if it is not full and item is not already present
     * @param inventory ID for the item being added to inventory
     * @return boolean to indicate success of function, true if item was added successfully
     */
    public boolean addInventory(String inventory){
        if (this.inventoryEnd < 5){
            this.inventory[this.inventoryEnd] = inventory;
            this.inventoryEnd++;
            return true;
        }
        return false;
    }

    /**
     * function to remove item from bobs inventory if it contains that item 
     * @param item to be removed from the inventory
     * @return boolean to indicate success of function 
     *  (true if item was removed, false if it could not be found)
     */
    public boolean removeInventory(String item) {
        for (String inv_item : inventory) {
            if (inv_item == item) {
                inv_item = "";
                return true;
            }
        }
        return false;
    }

    public String[] getInventory(){
        return this.inventory;
    }
}
