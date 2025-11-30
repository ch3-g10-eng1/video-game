package com.team3._8.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

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
    private boolean drawNext = false;
    private BitmapFont font;

    public PuzzleEvent(Sprite sprite, float speed){
        super(sprite, speed);
        loadTextures();
        createTextBubble();
        PuzzleEventLogic thisPuzzle = new PuzzleEventLogic();
        options = thisPuzzle.getOptions();
        equationString = thisPuzzle.getPrintableExpression();
        answer = thisPuzzle.getAnswer();
    }

    @Override
    public Map<String, Boolean> startInteraction(){
        if (isSolved) { return returnData; }
        drawNext = true;
        boolean skipChoice = false;
        if (!textBubbleVisible) {
            textBubbleVisible = textBubble.hideShow();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && conversationPointer > 0) && !conversationReset){
            textBubble.setText(script[conversationPointer]);
            conversationPointer += 1;

            if (conversationPointer > script.length - 1){
                conversationPointer = 0;
                conversationReset = true;
                skipChoice = true;
            }
        }

        if (conversationReset && !skipChoice){
            if(Gdx.input.isKeyJustPressed(Input.Keys.Y)){
                textBubble.setText(equationString + "\n" + "1:  " + options[0]
                    + "    " + "2:  " + options[1]
                    + "\n" + "3:  " + options[2]
                    + "    " + "4:  " + options[3]);
            }
        }

        return returnData;
    }

    @Override
    public Map<String, Boolean> stopInteraction() {
        if (isSolved) { return returnData; }
        if (textBubbleVisible) {
            textBubble.setText(script[0]);
            conversationPointer = 1;

            textBubbleVisible = textBubble.hideShow();
        }
        drawNext = false;
        return returnData;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        sprite.setPosition(x, y);

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

    protected void updateCollisionBox() {
        this.collisionBox.setX(this.sprite.getX());
        this.collisionBox.setY(this.sprite.getY());
    }

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
