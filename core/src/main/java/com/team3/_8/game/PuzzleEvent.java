package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements PuzzleEventLogic and creates a hidden event where the player can choose to solve an equation for a reward
 * The puzzle is in the format:
 *    a * b +- c
 * If the puzzle is solved the player gains the rocket boost
 * If the player chooses the incorrect answer a 30 second time penalty is added
 *
 * @author Oliver
 */
public class PuzzleEvent extends InteractableEntity{
    final HashMap<String, Boolean> returnData = new HashMap<>();
    private final String[] script = {
        "You have\nencountered\na hidden puzzle!",
        "Do you wish to\nsolve a puzzle\nfor a reward?",
        "Y: Yes\n N: No"
    };
    int[] options;
    String equationString;
    int answer;
    private float x = 0;
    private float y = 0;
    private TextBubble textBubble;
    private Animation<TextureRegion> puzzleEvent;
    private boolean textBubbleVisible = false;
    private int conversationPointer = 0;
    private boolean conversationReset = false;
    private boolean isSolved = false;
    private boolean drawNext = true;
    private BitmapFont font;

    /**
     * Creates PuzzleEvent, loads evilBob texture as placeholder (is invisible anyways), creates puzzle logic and sets relevant vars
     *
     * @param sprite redundant sprite param, is made invisible anyways
     * @param speed speed param, always 0f as the hidden event does not move
     */
    public PuzzleEvent(Sprite sprite, float speed){
        super(sprite, speed);
        loadTextures();
        createTextBubble();
        PuzzleEventLogic thisPuzzle = new PuzzleEventLogic();
        options = thisPuzzle.getOptions();
        equationString = thisPuzzle.getPrintableExpression();
        answer = thisPuzzle.getAnswer();
    }

    /**
     * Handles player interaction with the event
     *
     * @return map of commands & their state for the calling class to use useful to control effects
     *         etc. based on interaction
     */
    @Override
    public Map<String, Boolean> startInteraction(){
        if (isSolved) { return returnData; }
        returnData.put("Suspend", true);
        boolean skipChoice = false;
        if (!textBubbleVisible) {
            textBubbleVisible = textBubble.hideShow();
            drawNext = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)  && !conversationReset|| (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && conversationPointer > 0) && !conversationReset){
            drawNext = true;
            textBubble.setText(script[conversationPointer]);
            conversationPointer += 1;

            if (conversationPointer > script.length - 1){
                conversationReset = true;
                skipChoice = true;
                drawNext = false;
            }
        }

        if (conversationReset && !skipChoice){
            if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                textBubble.setText(equationString + "\n" + "1:  " + options[0]
                    + "    " + "2:  " + options[1]
                    + "\n" + "3:  " + options[2]
                    + "    " + "4:  " + options[3]);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.N)){
                isSolved = true;
                textBubbleVisible = textBubble.hideShow();
                drawNext = false;
                returnData.put("Suspend", false);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
                if(options[0] == answer){
                    returnData.put("Enable Rocket Bob", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
                else{
                    returnData.put("Time penalty", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
                if(options[1] == answer){
                    returnData.put("Enable Rocket Bob", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
                else{
                    returnData.put("Time penalty", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
                if(options[2] == answer){
                    returnData.put("Enable Rocket Bob", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
                else{
                    returnData.put("Time penalty", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
                if(options[3] == answer){
                    returnData.put("Enable Rocket Bob", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
                else{
                    returnData.put("Time penalty", true);
                    isSolved = true;
                    textBubbleVisible = textBubble.hideShow();
                    drawNext = false;
                    returnData.put("Suspend", false);
                }
            }
        }

        return returnData;
    }

    /**
     * Handles destruction/reset of interaction after finishing
     *
     * @return map of commands & their state for the calling class to use useful to control effects
     *         etc. based on interaction
     */
    @Override
    public Map<String, Boolean> stopInteraction() {
        if (isSolved) { return returnData; }
        drawNext = false;
        if (textBubbleVisible) {
            textBubble.setText(script[0]);
            conversationPointer = 1;

            textBubbleVisible = textBubble.hideShow();
            drawNext = true;
        }
        return returnData;
    }


    /**
     * Draws invisible sprite and text bubble
     *
     * @param batch batch for calling class
     * @param x x-position of sprite
     * @param y y-position of sprite
     */
    public void draw(SpriteBatch batch, float x, float y) {
        sprite.setPosition(x, y);
        sprite.setColor(0f, 0f, 0f, 0f);

        // If character has moved update collision box
        if (x != this.x || y != this.y) {
            updateCollisionBox();
            this.x = x;
            this.y = y;
        }

        // Draws text to instruct advance of conversation
        if (drawNext) {
            font.setColor(Color.WHITE);
            font.draw(batch, "Next: E", 450, 635);
        }
        textBubble.draw(batch, 330, 605);
        sprite.draw(batch);
    }

    /** Updates the collision box to the placement of the sprite */
    protected void updateCollisionBox() {
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }

    /** Creates TextBubble */
    private void createTextBubble() {
        font = new BitmapFont();
        Texture bubble = new Texture("speech_bubble.png");
        textBubble = new TextBubble(bubble, font, 170, 120);
        textBubble.setText(script[0]);
        conversationPointer += 1;
    }

    private void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("atlas/bob.atlas"));

        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("evil-bob");

        // Creates animation object
        this.puzzleEvent = new Animation<TextureRegion>(0.5f, frames);
    }

}
