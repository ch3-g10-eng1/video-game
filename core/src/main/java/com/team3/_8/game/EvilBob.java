package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

import java.util.Random;

public class EvilBob extends InteractableEntity {
    private TextureAtlas atlas;
    private Animation<TextureRegion> evilBob;

    private float x = 0;
    private float y = 0;

    private TextBubble textBubble;
    private boolean textBubbleVisible = false;
    private Texture bubble;
    private BitmapFont font;

    private int conversationPointer = 0;
    private boolean reset = false;
    private String[] script = {"You dare\nenter my realm\nNext: E", "With a stolen\nkeycard\n" + //
                "Next: E", "Give it to me\nor face the\nconsequence\n" + //
                                        "Next: E", "Y: Give Keycard\nN: Keep Keycard"};
    
    // Used to control animation time
    private float stateTime = 0f;

    private CampusSecurity test[] = new CampusSecurity[5];
    boolean created;

    private int tempX;
    private int tempY;
    private Boolean hasKeycard = false;

    HashMap<String, Boolean> returnData = new HashMap<>();


    public EvilBob(Sprite sprite, float speed) {
        super(sprite, speed);
        loadTextures();
        create();
    }

    @Override
    public HashMap<String, Boolean> startInteraction() {
        boolean skip = false;
        if (!textBubbleVisible){
            textBubbleVisible = textBubble.hideShow();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && conversationPointer > 0) && !reset){
            if (hasKeycard){
                textBubble.setText(script[conversationPointer]);
                conversationPointer += 1;
                
                if (conversationPointer > 3) {
                    conversationPointer = 0;
                    reset = true;
                    skip = true;
                }
            }
            else{
                textBubble.setText("Go Away!\nYou're missing\nmy keycard");
            }
        }

        if (reset && !skip){ 
            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                textBubble.setText("Get out of here!");
                returnData.put("Enable Rocket Bob", true);
                returnData.put("Remove Keycard", true);
                setHasKeycard(false);
                reset = false;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
                textBubble.setText("Release Security!");

                returnData.put("Create Campus Security", true);
                reset = false;
            }
        }

        return returnData;

    }

    @Override
    public HashMap<String, Boolean> stopInteraction() {
        if (textBubbleVisible){
            conversationPointer = 0;
            textBubble.setText("Interact: E");
            textBubbleVisible = textBubble.hideShow();
        }

        return returnData;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        // Timer for animation
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion current_animation = evilBob.getKeyFrame(stateTime, true);
        sprite.setRegion(current_animation);
        sprite.setPosition(x, y);

        if (x != this.x || y != this.y){
            updateCollisionBox();
            this.x = x;
            this.y = y;
        }

        textBubble.draw(batch, 1000+10, 1050+10);
        sprite.draw(batch);
    }

    private void updateCollisionBox(){
        // Sets the collision box of bob
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }

    private void create(){
        // Things for the text for the timer and other strings
        font =  new BitmapFont();

        bubble = new Texture("speech_bubble.png");
        textBubble = new TextBubble(bubble, font, 150, 190);
        textBubble.setText("Interact: E");
    }

    
    /**
     * loads the animation files from atlas into the animation variables
     */
    private void loadTextures(){
        // Loads sprites from Texture atlas
        atlas = new TextureAtlas(Gdx.files.internal("assets\\atlas\\bob.atlas"));

        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("evil-bob");

        // Creates animation object
        this.evilBob = new Animation<TextureRegion>(0.5f, frames);
    }

    public void setHasKeycard(Boolean hasKeycard){
        this.hasKeycard = hasKeycard;
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }
    
}
