package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This is the class for the character (Bob), inheriting from Entity
 * @author Lenny, Henry
 */
public class Bob extends CollidableEntity {

    private Set<String> inventory = new HashSet<String>();
    // Creates an inventory, which will be filled with String IDs
    private float collision__size_change; // offset for collision size, required for move()

    private TextureAtlas atlas;
    private Map<String, Animation<TextureRegion>> bob_animations;
    // holds the name of the animation linked to the animation

    // Used to control animation time
    float stateTime = 0f;
    private String animationOverride = "";
    private boolean reset = false;

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

        // Timer for animation
        stateTime += Gdx.graphics.getDeltaTime();
        // Default animation if no movement
        TextureRegion current_animation = bob_animations.get("Front").getKeyFrame(stateTime, true);

        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
             (Gdx.input.isKeyPressed(Input.Keys.D)))
              && !movement_halter[0]) {
            this.sprite.translateX(this.speed * delta);
            // Sets sprite animation to right
            if (animationOverride.length() == 0){
                current_animation = bob_animations.get("Right").getKeyFrame(stateTime, true);
            }
            else {
                current_animation = bob_animations.get("RocketRight").getKeyFrame(stateTime, true);
            }
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) ||
                  (Gdx.input.isKeyPressed(Input.Keys.A)))
                   && !movement_halter[2]) {
            this.sprite.translateX(-speed * delta);

            if (animationOverride.length() == 0){
                current_animation = bob_animations.get("Left").getKeyFrame(stateTime, true);
            }
            else {
                current_animation = bob_animations.get("RocketLeft").getKeyFrame(stateTime, true);
            }
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) ||
             (Gdx.input.isKeyPressed(Input.Keys.W)))
              && !movement_halter[3]) {
            this.sprite.translateY(speed * delta);

            if (animationOverride.length() == 0){
                current_animation = bob_animations.get("Up").getKeyFrame(stateTime, true);
            }
            else {
                current_animation = bob_animations.get("RocketUp").getKeyFrame(stateTime, true);
            }
        }

        else if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) ||
                (Gdx.input.isKeyPressed(Input.Keys.S)))
                && !movement_halter[1]) {
            this.sprite.translateY(-speed * delta);

            if (animationOverride.length() == 0){
                current_animation = bob_animations.get("Front").getKeyFrame(stateTime, true);
            }
            else {
                current_animation = bob_animations.get("RocketDown").getKeyFrame(stateTime, true);
            }
        }

        updateCollisionBox(collision__size_change);

        // Sets bob sprite to run the animation configured above
        if (animationOverride.equals("Squash")){
            if (!reset){
                reset = true;
                stateTime = 0f;
            }
            current_animation = bob_animations.get("Squash").getKeyFrame(stateTime, true);
        }
        sprite.setRegion(current_animation);
    }

    /**
     * loads the animation files from atlas into the animation variables
     */
    public void loadTextures(){
        // Loads sprites from Texture atlas
        atlas = new TextureAtlas("atlas/bob.atlas");

        bob_animations = new HashMap<String, Animation<TextureRegion>>();
        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frontFrames = atlas.findRegions("front-bob");
        Array<TextureAtlas.AtlasRegion> rightFrames = atlas.findRegions("side-bob");
        Array<TextureAtlas.AtlasRegion> upFrames = atlas.findRegions("up-bob");
        Array<TextureAtlas.AtlasRegion> squashFrames = atlas.findRegions("squash-bob");
        Array<TextureAtlas.AtlasRegion> rocketSideFrames = atlas.findRegions("rocket-side-bob");
        Array<TextureAtlas.AtlasRegion> rocketUpFrames = atlas.findRegions("rocket-up-bob");

        // Creates animation object
        bob_animations.put("Front", new Animation<TextureRegion>(0.5f, frontFrames));
        bob_animations.put("Right", new Animation<TextureRegion>(0.5f, rightFrames));
        bob_animations.put("Up", new Animation<TextureRegion>(0.5f, upFrames));
        bob_animations.put("Squash", new Animation<TextureRegion>(5f, squashFrames));
        bob_animations.put("RocketLeft", new Animation<TextureRegion>(0.2f, rocketSideFrames));
        bob_animations.put("RocketUp", new Animation<TextureRegion>(0.2f, rocketUpFrames));

        // Flips right into left frames
        Array<TextureRegion> leftFrames = flipFrames(rightFrames, true, false);
        bob_animations.put("Left", new Animation<TextureRegion>(0.5f, leftFrames));

        Array<TextureRegion> RocketRight = flipFrames(rocketSideFrames, true, false);
        bob_animations.put("RocketRight", new Animation<TextureRegion>(0.5f, RocketRight));

        Array<TextureRegion> RocketDown = flipFrames(rocketUpFrames, false, true);
        bob_animations.put("RocketDown", new Animation<TextureRegion>(0.5f, RocketDown));
    }

    /**
     * function to add item to bobs inventory if it is not full and item is not already present
     * @param inventory ID for the item being added to inventory
     * @return boolean to indicate success of function, true if item was added successfully
     */
    public boolean addInventory(String item){
        return inventory.add(item);
    }

    /**
     * function to remove item from bobs inventory if it contains that item
     * @param item to be removed from the inventory
     * @return boolean to indicate success of function
     */
    public boolean removeInventory(String item) {
        return inventory.remove(item);
    }

    public Set<String> getInventory(){
        return this.inventory;
    }

    public void setAnimation(String animationName){
        this.animationOverride = animationName;
    }

    public void setSpeed(Integer speed){
        this.speed = speed;
    }

    private Array<TextureRegion> flipFrames(Array<TextureAtlas.AtlasRegion> frameArray, 
            Boolean flipX, Boolean flipY){
        // Flips frames
        Array<TextureRegion> frames = new Array<>();
        for (TextureRegion frame: frameArray){
            TextureRegion temp_frame = new TextureRegion(frame);
            temp_frame.flip(flipX, flipY);
            frames.add(temp_frame);
        }
        return frames;
    }
}
