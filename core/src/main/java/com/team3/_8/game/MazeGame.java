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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.Console;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame extends ApplicationAdapter {
    //PLACEHOLDERS!!!
    private Texture image;

    // constants in arbitrary units for the camera
    static final int WORLD_WIDTH = 200;
    static final int WORLD_HEIGHT = 200;

    // constants in arbitrary units for Bob's size
    static final int BOB_WIDTH = 15;
    static final int BOB_HEIGHT = 15;

    // Booleans for the game
    private boolean isWon = false; //Variable to see if the game has been won
    private boolean paused = false; //Variable to see if the game is paused

    // Timer
    private float timer;
    private BitmapFont font;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Sprite bobSprite;

    private PlayerChar bob; // This is a glorious piece of code, Dr Mike J Freeman would be proud

    private Map maze;

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
        bobSprite = new Sprite(atlas.findRegion("front-bob-2"));
        bobSprite.setPosition(50,50);
        bobSprite.setSize(BOB_WIDTH, BOB_HEIGHT);

        bob = new PlayerChar(bobSprite, 15); //The actual creation of Bob, he has arrived

        // Making the camera and the viewport
        camera = new OrthographicCamera(30, 30 * (w/h));
        camera.position.set(bob.getEntity().getX() - ((float) BOB_WIDTH / 2), bob.getEntity().getY() - ((float) BOB_HEIGHT / 2), 0);
        camera.zoom = 1f; // Starting a bit zoomed in
        camera.update();

        // Things for the text for the timer and other strings
        font =  new BitmapFont();
        font.setColor(Color.WHITE);

        // Creation of the viewport
        // We are using a Fill Viewport, since the entire screen is covered, whilst the aspect ratio is kept intact
        viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        handleInput();// The input for the zoom in and out

        if (!paused){
            movement_halter = maze.hitsWall(bob, Gdx.graphics.getDeltaTime());
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
        font.draw(batch, "Timer: " + (int)timer + "s", 50, 100);

        batch.end();
    }


    //This whole method should be cleaned up at some point
    private void handleInput() {
        //When Q is pressed, the camera is zoomed in, and zoomed out when E is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }

        //This is to pause the game
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }
        //System.out.println("Paused state is: " + paused); //Display the current pause state
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
