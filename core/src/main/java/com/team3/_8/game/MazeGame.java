package com.team3._8.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// import java.io.Console;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame extends ApplicationAdapter {
    // development modes
    private boolean dev_zoom = false;

    // constants in arbitrary units for the camera
    static final int WORLD_WIDTH = 200;
    static final int WORLD_HEIGHT = 200;

    // constants in arbitrary units for Bob's size
    static final int BOB_WIDTH = 15;
    static final int BOB_HEIGHT = 15;

    // Screen manager
    private int activeScreen = 0;

    private boolean paused = false; //Variable to see if the game is paused

    // Events counter
    private int events;

    // Timer
    private float timer;
    private BitmapFont font;

    // Camera and viewport
    private OrthographicCamera camera;
    private Viewport viewport;

    // The two sprite batches -> ones for the main game, and one for the HUD
    private SpriteBatch batch;
    private SpriteBatch HUDBatch;
    private HUD hud;

    // The texture atlas containing Bob, and the sprite of Bob
    private TextureAtlas atlas;
    private Sprite bobSprite;

    // This is a glorious piece of code, Dr Mike J Freeman would be proud
    private Bob bob;

    // The sprite for the keycard
    private Texture keycardTexture;
    private Sprite keycardSprite;

    // The creation of the keycard entity
    private CollectableEntity keycard;

    //Evil bob collidable entity
    private EvilBob evilBob;

    //tutorial image texture and sprite
    private Texture tutorial_texture;
    private Sprite tutorial_sprite;

    // Map
    private Maze maze;

    // Map to store return data for interactable entities
    // The data can be used to control other objects in this program
    Map<String, Boolean> evilBobReturnData = new HashMap<>();
    Map<String, Boolean> campusSecurityReturnData = new HashMap<>();

    // Holds created campusSecuirty sprites and tracks if they exist
    private CampusSecurity allCampusSecuritySprites[] = new CampusSecurity[5];
    boolean campusSecurityCreated;

    // Boolean array to see if Bob has hit a wall, and what wall he has hit
    private boolean[] movement_halter;

    // Map that keeps track of event completion
    private Map<String, Integer> eventTracker;

    @Override
    public void create() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        timer = 0f; // The timer variable

        events = 0;

        // Collidable layers of map and creates maze
        String[] collidable_layers = {"Collision", "Doors"};
        maze = new Maze("Map/CSE_map.tmx", collidable_layers,
            "WinDoors", "EventTrigger");

        // Start of Bob's creation - the birth of Bob
        // bob = createSprite("atlas/bob.atlas", "front-bob", 100, 500,
        //                     BOB_WIDTH, BOB_HEIGHT, Bob::new);
        atlas = new TextureAtlas("atlas/bob.atlas");
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(100,500);
        bobSprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        bob = new Bob(bobSprite, 60, -3); //The actual creation of Bob, he has arrived

        // Creation of evil bob character
        evilBob = createSprite("atlas/bob.atlas", "evil-bob", 500, 500, 
            2*BOB_WIDTH, 2*BOB_HEIGHT, 0, EvilBob::new);

        // Creation of the keycard
        keycardTexture = new Texture("keycard.png");
        keycardSprite = new Sprite(keycardTexture);
        keycardSprite.setPosition(20,20);
        keycardSprite.setSize(BOB_WIDTH * 2, BOB_HEIGHT * 2);
        keycard = new CollectableEntity(keycardSprite, 0, "Keycard");
        tutorial_texture = new Texture("tutorial_img.png");
        tutorial_sprite  = new Sprite(tutorial_texture);
        tutorial_sprite.setSize(310, 180);
        tutorial_sprite.setPosition(-150, -100);

        // Making the camera and the viewport
        camera = new OrthographicCamera(30, 30 * (w/h));
        camera.position.set(bob.getEntity().getX() - ((float) BOB_WIDTH / 2),
            bob.getEntity().getY() - ((float) BOB_HEIGHT / 2), 0);
        camera.zoom = 2f;
        camera.update();

        // Things for the text for the timer and other strings
        font =  new BitmapFont();
        font.setColor(Color.WHITE);

        // Creation of the viewport
        // We are using a Fill Viewport, since the entire screen is covered,
        // whilst the aspect ratio is kept intact
        viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();

        // Used to track game events
        eventTracker = gameController.setEventMap();

        //Creation of the HUD
        HUDBatch = new SpriteBatch();
        hud = new HUD(HUDBatch, events);
    }

    /**
     * Renders different screens based on activeScreen configuration
     */
    @Override
    public void render() {
        if (activeScreen == 0) {
            titleScreenRender();
        }
        else if (activeScreen == 1) {
            gameScreenRender();
        }
        else if (activeScreen == 2) {
            tutorialScreenRender();
        }
        else if (activeScreen == 3) {
            winScreenRender();
        }
        else if (activeScreen == 4) {
            loseScreenRender();
        }
    }

    /**
     * Runs the code for the game screen every frame
     */
    private void gameScreenRender() {
        // The input for the zoom in and out
        paused = gameController.handleInput(camera, paused, dev_zoom);

        // Configures game if player collects keycard
        if (keycard.collected(bob)) {
            eventTriggered("Positive");
            evilBob.setPlayerHasKeycard(true);
            maze.removeCollisionLayer("Doors");
            maze.removeVisibleLayer("ClosedDoors");
        }

        if (!paused){
            movement_halter = maze.hitsWall(bob, Gdx.graphics.getDeltaTime());

            // Checks for collision with evilBob
            evilBobReturnData = evilBob.collision(bob);
            // Handles interaction with characters
            handleInteraction();

            // Moves bob in player direction (if not hitting a wall)
            bob.move(movement_halter);
            timer += Gdx.graphics.getDeltaTime();
        }

        // Centres the camera on Bob and then updates it
        camera.position.set(bobSprite.getX() + bobSprite.getWidth()/2,
                            bobSprite.getY() + bobSprite.getHeight()/2, 0);
        camera.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        // Sprite batch drawing
        maze.renderMap(camera);

        batch.begin();
        evilBob.draw(batch, 1000, 1050);
        bob.draw(batch);
        keycard.draw(batch);

        if (campusSecurityCreated){
            int mod = 0;
            for (int i = 0; i < 5; i++){
                allCampusSecuritySprites[i].draw(batch, 870+ mod, 1150, 
                    maze.hitsWall(allCampusSecuritySprites[i], Gdx.graphics.getDeltaTime()));
                mod += 35;
            }
        }

        batch.end();

        // The drawing of the HUD of the game
        hud.draw(font, gameController.formatTime(timer), eventTracker, bob, paused);

        // Sets rendered screens based on game state
        if (paused){
            hud.pauseScreen(font, viewport);
        }
        if (maze.HitsWinLayer(bob)) {
            activeScreen = 3;
        }
        if (maze.HitsEventLayer(bob)) {
            eventTriggered("Negative");
        }
        // The code to check if the game has ended
        if (timer >= 300){
            activeScreen = 4;
            paused = true;
        }


    }

    /**
     * Renders the title screen
     */
    private void titleScreenRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        camera.position.set(0,0,0);
        camera.update();

        GlyphLayout topLayout = new GlyphLayout(font, "Welcome to ... The Life of Bob");
        GlyphLayout centreLayout = new GlyphLayout(font, "Press space to start");
        GlyphLayout bottomLayout = new GlyphLayout(font, "Press T to see tutorial");
        GlyphLayout[] textLayout = {topLayout, centreLayout, bottomLayout};
        Map<String, Float> layoutValues = positionText(textLayout, false, false);

        batch.begin();

        // draw centered text
        font.draw(batch, topLayout, layoutValues.get("x1"), layoutValues.get("y1"));
        font.draw(batch, centreLayout, layoutValues.get("x2"), layoutValues.get("y2"));
        font.draw(batch, bottomLayout, layoutValues.get("x3"), layoutValues.get("y3"));

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            activeScreen = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {
            activeScreen = 2;
        }
    }

    /**
     * Renders the tutorial screen
     */
    private void tutorialScreenRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        camera.position.set(0,0,0);
        camera.update();

        GlyphLayout topLayout = new GlyphLayout(font, "Press ESC to go back");
        GlyphLayout[] textLayout = {topLayout};
        Map<String, Float> layoutValues = positionText(textLayout, true, false);

        batch.begin();
        tutorial_sprite.draw(batch);
        font.draw(batch, topLayout, layoutValues.get("x1"), layoutValues.get("y1"));

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            activeScreen = 0;
        }
    }

    /**
     * Renders the win screen
     */
    private void winScreenRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        camera.position.set(0,0,0);
        camera.update();

        // Prepare text layouts for measurement
        GlyphLayout topLayout = new GlyphLayout(font, "Well done!!");
        GlyphLayout bottomLayout = new GlyphLayout(font, "You won the game");
        GlyphLayout[] textLayout = {topLayout, bottomLayout};

        Map<String, Float> layoutValues = positionText(textLayout, false, false);

        batch.begin();

        // Draws centered text
        font.draw(batch, topLayout, layoutValues.get("x1"), layoutValues.get("y1"));
        font.draw(batch, bottomLayout, layoutValues.get("x2"), layoutValues.get("y2"));
        batch.end();
    }

    /**
     * Renders the loose screen
     */
    private void loseScreenRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        camera.position.set(0,0,0);
        camera.update();

        // Prepare text layouts for measurement
        GlyphLayout topLayout = new GlyphLayout(font, "Time's Up!");
        GlyphLayout bottomLayout = new GlyphLayout(font, "You lost the game :(");
        GlyphLayout[] textLayout = {topLayout, bottomLayout};
        Map<String, Float> layoutValues = positionText(textLayout, false, false);

        // Centres the bob sprite
        bobSprite.setPosition(layoutValues.get("centreX") - bobSprite.getWidth() / 2f, 
            (layoutValues.get("centreY") + 20f) - bobSprite.getHeight() / 2f);
        bobSprite.setSize(2*BOB_WIDTH, 2*BOB_HEIGHT);
        bob.setAnimation("Squash");

        batch.begin();

        // Runs each of the 23 animation frames
        for (int i = 0; i < 23; i++){
            movement_halter = new boolean[]{true, true, true, true};
            bob.move(movement_halter);
            bob.draw(batch);
        }

        // Draws centered text
        font.draw(batch, topLayout, layoutValues.get("x1"), layoutValues.get("y1"));
        font.draw(batch, bottomLayout, layoutValues.get("x2"), layoutValues.get("y2"));
        
        batch.end();
    }

    /**
     * Positions text passed in to format nicely on the screen
     * @param textLayout Text objects to position
     * @param top Position top
     * @param bottom Position bottom
     * If top and bottom are both false, defaults to centre
     * @return map x,y values for positioning. Accessed via x1, x2 etc.
     */
    private Map<String, Float> positionText(GlyphLayout textLayout[], boolean top, boolean bottom){
        // Holds position values to return
        Map<String, Float> returnValues = new HashMap<>();
        // Used to evenly space lines
        int numLines = textLayout.length;
        // Spacing between lines
        float spacing = 20f;
        float offset;
        
        // Offsets text based on positioning on screen
        if (top){
            offset = 120f;
        }
        else if (bottom){
            offset = -120f;
        }
        else{
            offset = (numLines-1)*spacing;
        }

        // Centre of the screen in world coordinates
        float centerX = camera.position.x;
        float centerY = camera.position.y;
        returnValues.put("centreX", centerX);
        returnValues.put("centreY", centerY);

        // Positions each line and store position values
        for (int i = 1; i <= numLines; i++){
            float x = centerX - textLayout[i-1].width / 2f;
            float y = centerY - i*spacing + offset;
            returnValues.put(("x" + String.valueOf(i)), x);
            returnValues.put(("y" + String.valueOf(i)), y);
        }

        return returnValues;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Changes the viewport's size to the sizes passed
    }

    @Override
    public void dispose() {
        batch.dispose();
        bob.dispose();
        evilBob.dispose();
        font.dispose();
        maze.dispose();
        for (int i = 0; i<allCampusSecuritySprites.length; ++i){
            if (campusSecurityCreated){allCampusSecuritySprites[i].dispose();}
        }
    }

    /**
     * Allows for a sprite with a texture to be created
     * @param <T> type of entity
     * @param atlas atlas to get textures from
     * @param regionName name of region in atlas to get textures from
     * @param xPos x-position to create sprite
     * @param yPos y-position to create sprite
     * @param xSize x-size of sprite
     * @param ySize y-size of sprite
     * @param speed speed of sprite
     * @param constructorType type of entity
     * 
     * @return constructed entity of specified type
     */
    private <T> T createSprite(String atlas, String regionName, Integer xPos, Integer yPos, 
            Integer xSize, Integer ySize, Integer speed, 
            java.util.function.BiFunction<Sprite,Integer,T> constructorType) {
        // Creation atlas
        TextureAtlas tempAtlas = new TextureAtlas(atlas);
        // Loaded to get size
        Sprite tempSprite = new Sprite(tempAtlas.findRegion(regionName));
        tempSprite.setPosition(xPos,yPos);
        tempSprite.setSize(xSize, ySize);

        // Returns campusSecurityCreated sprite of type passed
        return constructorType.apply(tempSprite, speed);
    }

    /**
     * Handles the interactions for interactable entities
     */
    private void handleInteraction() {
        // Creates campus security if the command has been set to true
        if (evilBobReturnData.containsKey("Create Campus Security")){
            if (evilBobReturnData.get("Create Campus Security") && !campusSecurityCreated){            
                eventTriggered("Hidden");
                for (int i = 0; i < allCampusSecuritySprites.length; i++){
                allCampusSecuritySprites[i] = createSprite("atlas/security_geese.atlas", 
                "walking", 500, 500, 2*BOB_WIDTH, 2*BOB_HEIGHT, 10, CampusSecurity::new);
            }
            campusSecurityCreated = true;
            }
        }

        // Sets rocketBob if the command has been set to true
        if (evilBobReturnData.containsKey("Enable Rocket Bob")){
            if (evilBobReturnData.get("Enable Rocket Bob")){
                bob.setAnimation("Rocket");
                bob.setSpeed(150);
            }
        }

        // Removes the keycard if the command has been set to true
        if (evilBobReturnData.containsKey("Remove Keycard")){
            if (evilBobReturnData.get("Remove Keycard")){
                if (bob.removeInventory("Keycard")) {
                    eventTriggered("Hidden");
                }
            }
        }

        // Checks for collision with CampusSecurity & resets player to start if so
        if (campusSecurityCreated){
            for (CampusSecurity sec : allCampusSecuritySprites){
                campusSecurityReturnData = sec.collision(bob);
                if (campusSecurityReturnData.containsKey("Reset Player Position")){
                    if (campusSecurityReturnData.get("Reset Player Position")){
                        bobSprite.setPosition(100, 500);
                    }
                };
            }
        }
    }

    /**
     * Allows event to be added to eventTracker
     * @param eventName - name of event
     */
    private void eventTriggered(String eventName) {
        eventTracker.put(eventName, eventTracker.get(eventName) + 1);
    }
}
