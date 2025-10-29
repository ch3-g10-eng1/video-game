package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * This is the class for the character (Bob), inheriting from Entity
 * @author Lenny, Henry
 */
public class Bob extends CollidableEntity {

    private String[] inventory = new String[5];// Creates an inventory of size 5, which will be filled with String IDs
    private int inventoryEnd = 0;

    // Vars to hold each set of animation images
    private Animation<TextureRegion> bobFront;
    private Animation<TextureRegion> bobLeft;
    private Animation<TextureRegion> bobRight;
    private Animation<TextureRegion> bobUp;
    private Animation<TextureRegion> bobDown;
    private Animation<TextureRegion> bobRocket;
    private Animation<TextureRegion> bobSquash;
    private TextureAtlas atlas;

    // Used to control animation time
    float stateTime = 0f;

    public Bob(Sprite sprite, float speed){
        super(sprite, speed);
        loadTextures();
    }

    public Bob(Sprite sprite, float speed, float widthChange, float heightChange){
        super(sprite, speed, widthChange, heightChange);
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
        TextureRegion bobAnimation = bobFront.getKeyFrame(stateTime, true);

        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
             (Gdx.input.isKeyPressed(Input.Keys.D)))
              && !movement_halter[0]) {
            this.sprite.translateX(this.speed * delta);
            bobAnimation = bobRight.getKeyFrame(stateTime, true);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) ||
                  (Gdx.input.isKeyPressed(Input.Keys.A)))
                   && !movement_halter[2]) {
            this.sprite.translateX(-speed * delta);
            bobAnimation = bobLeft.getKeyFrame(stateTime, true);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) ||
             (Gdx.input.isKeyPressed(Input.Keys.W)))
              && !movement_halter[3]) {
            this.sprite.translateY(speed * delta);
            bobAnimation = bobUp.getKeyFrame(stateTime, true);
        }
        else if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) ||
                (Gdx.input.isKeyPressed(Input.Keys.S)))
                && !movement_halter[1]) {
            this.sprite.translateY(-speed * delta);
            bobAnimation = bobDown.getKeyFrame(stateTime, true);
        }
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());


        // Sets bob sprite to run the animation configured above
        sprite.setRegion(bobAnimation);
    }

    public void loadTextures(){
        atlas = new TextureAtlas(Gdx.files.internal("assets\\atlas\\bob.atlas"));

        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frontFrames = atlas.findRegions("front-bob");

        // Creates animation object
        bobFront = new Animation<TextureRegion>(0.5f, frontFrames);
        Array<TextureAtlas.AtlasRegion> rightFrames = atlas.findRegions("side-bob");
        bobRight = new Animation<TextureRegion>(0.5f, rightFrames);

        // Flips right into left frames
        Array<TextureRegion> leftFrames = new Array<>();
        for (TextureRegion frame: rightFrames){
            TextureRegion temp_frame = new TextureRegion(frame);
            temp_frame.flip(true, false);
            leftFrames.add(temp_frame);
        }
        bobLeft = new Animation<TextureRegion>(0.5f, leftFrames);

        Array<TextureAtlas.AtlasRegion> upFrames = atlas.findRegions("up-bob");
        bobUp = new Animation<TextureRegion>(0.5f, upFrames);
        bobDown = bobFront;
        Array<TextureAtlas.AtlasRegion> squashFrames = atlas.findRegions("squash-bob");
        bobSquash = new Animation<TextureRegion>(0.5f, squashFrames);
        Array<TextureAtlas.AtlasRegion> rocketFrames = atlas.findRegions("rocket-bob");
        bobRocket = new Animation<TextureRegion>(0.1f, rocketFrames);
    }

    public boolean addInventory(String inventory){
        if (this.inventoryEnd < 5){
            this.inventory[this.inventoryEnd] = inventory;
            this.inventoryEnd++;
            return true;
        }
        return false;
    }
}
