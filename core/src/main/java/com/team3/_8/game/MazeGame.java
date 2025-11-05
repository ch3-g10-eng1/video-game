package com.team3._8.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// import java.io.Console;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame extends ApplicationAdapter {
    //PLACEHOLDERS!!!!!!!!!!!!!!!!!!!
    private Texture image;

    // constants in arbitrary units for the camera
    static final int WORLD_WIDTH = 200;
    static final int WORLD_HEIGHT = 200;

    // constants in arbitrary units for Bob's size
    static final int BOB_WIDTH = 15;
    static final int BOB_HEIGHT = 15;

    // Screen manager
    private int active_screen = 0;

    // Booleans for the game
    private boolean isWon = false; //Variable to see if the game has been won
    private boolean isEnded = false; // Variable to see if the game has ended
    private boolean paused = false; //Variable to see if the game is paused

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

    // Map
    private Map maze;

    // Boolean array to see if Bob has hit a wall, and what wall he has hit
    private boolean[] movement_halter;

    @Override
    public void create() {

        //Placeholder Textures!!!
        image = new Texture("libgdx.png");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        timer = 0f; // The timer variable

        String[] collidable_layers = {"Collision", "Doors"};
        maze = new Map("Map/CSE_map.tmx", collidable_layers);

        // Start of Bob's creation - the birth of Bob
        atlas = new TextureAtlas(Gdx.files.internal("atlas/bob.atlas"));
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(100,500);
        bobSprite.setSize(BOB_WIDTH, BOB_HEIGHT);


        bob = new Bob(bobSprite, 60, -3); //The actual creation of Bob, he has arrived

        // Creation of the keycard
        keycardTexture = new Texture("keycard.png");
        keycardSprite = new Sprite(keycardTexture);
        keycardSprite.setPosition(20,20);
        keycardSprite.setSize(BOB_WIDTH * 2, BOB_HEIGHT * 2);
        keycard = new CollectableEntity(keycardSprite, 0, "Keycard");
        System.out.println(keycard.collisionBox.width+ ","+ keycard.collisionBox.height);

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

        //Creation of the HUD
        HUDBatch = new SpriteBatch();
        hud = new HUD(HUDBatch);
    }

    @Override
    public void render() {
        if (active_screen == 0) {
            titleScreenRender();
        }
        else if (active_screen == 1) {
            gameScreenRender();
        }
    }

    /**
     * runs the code for the game screen every frame
     */
    private void gameScreenRender() {
        paused = gameController.handleInput(camera, paused);// The input for the zoom in and out

        if (keycard.collected(bob)) {
            maze.removeCollisionLayer("Doors");
            maze.removeVisibleLayer("ClosedDoors");
        }

        if (!paused){
            boolean[] movement_halter = maze.hitsWall(bob, Gdx.graphics.getDeltaTime());
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
        batch.begin();



        bob.draw(batch);
        maze.renderMap(camera);
        keycard.draw(batch);


        batch.end();

        // The drawing of the HUD of the game
        hud.draw(font, gameController.formatTime(timer), bob, paused);

        if (paused){
            hud.pauseScreen(font, viewport);
        }


        // The code to check if the game has ended
        if (timer >= 300){
            paused = true;
            isEnded = true;
        }
    }

    private void titleScreenRender() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        camera.position.set(0,0,0);
        camera.update();

        batch.begin();
        font.draw(batch, "This is a game.", 0, 30);;
        font.draw(batch, "Press space to start", 0, 0);;
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            active_screen = 1;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Changes the viewport's size to the sizes passed
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        bob.dispose();
    }
}
