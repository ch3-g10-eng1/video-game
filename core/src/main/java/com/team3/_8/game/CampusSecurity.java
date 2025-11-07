package com.team3._8.game;

import java.util.Map;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class CampusSecurity extends InteractableEntity {
    private TextureAtlas atlas;
    private Animation<TextureRegion> campusSecurity;
    private float stateTime = 0f;

    private float x = 0;
    private float y = 0;
    private boolean intialisePos = true;
    private boolean up = false;

    Map<String, Boolean> returnData = new HashMap<>();

    public CampusSecurity(Sprite sprite, float speed) {
        super(sprite, speed);
        loadTextures();
    }

    @Override
    public Map<String, Boolean> startInteraction() {
        returnData.put("Reset Player Position", true);
        return returnData;
    }

    @Override
    public Map<String, Boolean> stopInteraction() {
        returnData.put("Reset Player Position", false);
        return returnData;
    }
    
    /**
     * loads the animation files from atlas into the animation variables
     */
    private void loadTextures(){
        // Loads sprites from Texture atlas
        atlas = new TextureAtlas(Gdx.files.internal("atlas/security_geese.atlas"));

        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("walking");

        // Creates animation object
        this.campusSecurity = new Animation<TextureRegion>(0.1f, frames);
    }

    public void draw(SpriteBatch batch, float x, float y, boolean[] movement_halter) {
        // Timer for animation
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion current_animation = campusSecurity.getKeyFrame(stateTime, true);
        sprite.setRegion(current_animation);

        if (this.sprite.getX() != this.x || this.sprite.getY() != this.y ){
            if (intialisePos){
                sprite.setPosition(x, y);
                intialisePos = false;
            }
            updateCollisionBox();
            this.x = x;
            this.y = y;
        }

        if (movement_halter[3]){
            up = false;
        }
        else if (movement_halter[1]){
            up = true;
        }
        move(up);
        sprite.draw(batch);
    }

    private void updateCollisionBox(){
        // Sets the collision box of bob
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }

    private void move(boolean up){
        float delta = Gdx.graphics.getDeltaTime();

        if (up){
            sprite.translateY(this.speed * delta);
        }
        else{
            sprite.translateY(-this.speed * delta);
        }

        // Corrects collision box
        float inset = 8f;
        float newWidth = Math.max(0f, this.sprite.getWidth() - inset * 2f);
        this.collisionBox.setX(this.sprite.getX() + inset);
        this.collisionBox.setY(this.sprite.getY());
        this.collisionBox.setWidth(newWidth);
        this.collisionBox.setHeight(this.sprite.getHeight());
    }
}
