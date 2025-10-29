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

import java.io.Console;

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

        maze = new Map("test_map.tmx", "walls");

        // Start of Bob's creation - the birth of Bob
        atlas = new TextureAtlas(Gdx.files.internal("atlas/bob.atlas"));
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(50,50);
        bobSprite.setSize(BOB_WIDTH, BOB_HEIGHT);


        bob = new Bob(bobSprite, 30, -4, -3); //The actual creation of Bob, he has arrived
        System.out.println(bob.collisionBox.width+ ","+bob.collisionBox.height);


        // Creation of the keycard
        keycardTexture = new Texture("keycard.png");
        keycardSprite = new Sprite(keycardTexture);
        keycardSprite.setPosition(30,30);
        keycardSprite.setSize((float) BOB_WIDTH / 2, (float) BOB_HEIGHT / 2);
        keycard = new CollectableEntity(keycardSprite, 0, "Keycard");
        System.out.println(keycard.collisionBox.width+ ","+ keycard.collisionBox.height);

        // Making the camera and the viewport
        camera = new OrthographicCamera(30, 30 * (w/h));
        camera.position.set(bob.getEntity().getX() - ((float) BOB_WIDTH / 2),
        bob.getEntity().getY() - ((float) BOB_HEIGHT / 2), 0);
        camera.zoom = 1f; // Starting a bit zoomed in
        camera.update();

        // Things for the text for the timer and other strings
        font =  new BitmapFont();
        font.setColor(Color.WHITE);

        // Creation of the viewport
        // We are using a Fill Viewport, since the entire screen is covered,
        // whilst the aspect ratio is kept intact
        viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
        HUDBatch = new SpriteBatch();
    }

    @Override
    public void render() {
        paused = gameController.handleInput(camera, paused);// The input for the zoom in and out

        keycard.collides(bob);

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

        if (paused){
        batch.draw(image, 0, 0); //Temporary image to test Bob movement, and pause now
        }

        bob.draw(batch);
        maze.renderMap(camera);
        keycard.draw(batch);

        batch.end();

        // The drawing of the HUD of the game
        // Right now it's just a timer but other things can be added
        // If we get enough items, we could make this into a class?
        HUDBatch.begin();

        font.draw(HUDBatch, gameController.formatTime(timer), 550, 370); // Draws the timer to the screen

        HUDBatch.end();

        // The code to check if the game has ended -> This might work better as a function, but i cba rn
        if (timer >= 300){
            paused = true;
            isEnded = true;
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
