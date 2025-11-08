package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * defines the evil bob character used in the suprise event 
 * @author Henry
 */
public class EvilBob extends InteractableEntity {
    private TextureAtlas atlas;
    private Animation<TextureRegion> evilBob;

    // Co-ordinates to draw at
    private float x = 0;
    private float y = 0;

    // Text bubble settings for character
    private TextBubble textBubble;
    private boolean textBubbleVisible = false;
    private Texture bubble;
    private BitmapFont font;

    // Variables used to control character conversation
    private int conversationPointer = 0;
    private boolean conversationReset = false;
    private Boolean playerHasKeycard = false;
    
    // Conversation script for the character to use
    private String[] script = {"You dare\nenter my realm", "With a stolen\nkeycard", 
        "Give it to me\nor face the\nconsequence", 
        "Y: Give Keycard\nN: Keep Keycard"};

    // Used to control animation time
    private float stateTime = 0f;

    private Boolean hasKeycard = false;

    HashMap<String, Boolean> returnData = new HashMap<>();

    /**
     * Creates character, loads character Texture + Animation
     * Also creates text-bubble for character
     * @param sprite
     * @param speed
     */
    public EvilBob(Sprite sprite, float speed) {
        super(sprite, speed);
        loadTextures();
        createTextBubble();
    }

    /**
     * Handles interaction with character when started
     * @return map of commands & their state for the calling class to use
     *         useful to control effects etc. based on interaction
     */
    @Override
    public Map<String, Boolean> startInteraction() {
        // Controls final option section of interaction
        boolean skipChoice = false;

        // Makes textBubbleVisible if no already
        if (!textBubbleVisible){
            textBubbleVisible = textBubble.hideShow();
        }
        System.out.println(skipChoice + " " + conversationPointer);
        // Handles interaction as player presses E
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && conversationPointer > 0) && !conversationReset){
            if (playerHasKeycard){
                // Loads each line of the interaction text
                textBubble.setText(script[conversationPointer]);
                conversationPointer += 1;
                // Resets conversation if end reached
                if (conversationPointer > script.length -1) {
                    conversationPointer = 0;
                    conversationReset = true;
                    skipChoice = true;
                }
            }
            else{
                // Message to player if they don't have the keycard needed for this
                textBubble.setText("Go Away!\nYou're missing\nmy keycard");
            }
        }

        if (conversationReset && !skipChoice){
            // Final step of interaction where player given a choice
            if (Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                textBubble.setText("Get out of here!");

                // Instructions to MazeGame.java to configure
                // bob sprite and remove inventory item
                returnData.put("Enable Rocket Bob", true);
                returnData.put("Remove Keycard", true);

                setPlayerHasKeycard(false);
                conversationReset = false;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
                textBubble.setText("Release Security!");

                // Instruction to MazeGame.java to createTextBubble campusSecurity entities
                returnData.put("Create Campus Security", true);
                conversationReset = false;
            }
        }

        return returnData;

    }

    /**
     * Handles destruction and reset of interaction
     * if player walks away during / after it
     * @return  map of commands & their state for the calling class to use
     *         useful to control effects etc. based on interaction
     */
    @Override
    public Map<String, Boolean> stopInteraction() {
        if (textBubbleVisible){
            // Resets conversation & text
            conversationPointer = 0;
            textBubble.setText("Interact: E");

            // Hides text-bubble
            textBubbleVisible = textBubble.hideShow();
        }

        return returnData;
    }

    /**
     * Draws evilBob character animation & text-bubble
     * @param batch - batch from calling class
     * @param x - x-position of sprite
     * @param y - y-position of sprite
     */
    public void draw(SpriteBatch batch, float x, float y) {
        // Timer for animation
        stateTime += Gdx.graphics.getDeltaTime();

        // Sets sprite's animation to next frame
        TextureRegion current_animation = evilBob.getKeyFrame(stateTime, true);
        sprite.setRegion(current_animation);
        sprite.setPosition(x, y);

        // If character has moved update collision box
        if (x != this.x || y != this.y){
            updateCollisionBox();
            this.x = x;
            this.y = y;
        }

        // Draws text to instruct advance of conversation
        if (conversationPointer > 0){
            font.setColor(Color.WHITE);
            font.draw(batch, "Next: E", 1105,1085);
        }
        textBubble.draw(batch, 985, 1055);
        sprite.draw(batch);
    }

    /**
     * Updates the collision box to the placement of the sprite
     */
    protected void updateCollisionBox(){
        // Sets the collision box of bob
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }

    /**
     * Creates TextBubble
     */
    private void createTextBubble(){
        // Things for the text for the timer and other strings
        font =  new BitmapFont();

        bubble = new Texture("speech_bubble.png");
        textBubble = new TextBubble(bubble, font, 170, 120);
        textBubble.setText("Interact: E");
    }


    /**
     * Loads the animation files from atlas into the animation variables
     */
    private void loadTextures(){
        // Loads sprites from Texture atlas
        atlas = new TextureAtlas(Gdx.files.internal("atlas/bob.atlas"));

        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("evil-bob");

        // Creates animation object
        this.evilBob = new Animation<TextureRegion>(0.5f, frames);
    }

    /**
     * Sets player keycard status
     * @param playerHasKeycard
     */
    public void setPlayerHasKeycard(boolean playerHasKeycard){
        this.playerHasKeycard = playerHasKeycard;
    }

}
