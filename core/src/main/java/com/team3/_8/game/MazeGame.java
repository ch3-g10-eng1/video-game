package com.team3._8.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Sprite bobSprite;

    private Bob bob; // This is a glorious piece of code, Dr Mike J Freeman would be proud

    @Override
    public void create() {
        //Placeholder Textures!!!
        image = new Texture("libgdx.png");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        // Start of Bob's creation - the birth of Bob
        atlas = new TextureAtlas(Gdx.files.internal("atlas/bob.atlas"));
        bobSprite = new Sprite(atlas.findRegion("front-bob-2"));
        bobSprite.setPosition(0, 0);
        bobSprite.setSize(BOB_WIDTH, BOB_HEIGHT);

        bob = new Bob(bobSprite, 4); //The actual creation of Bob, he has arrived

        // Making the camera and the viewport
        camera = new OrthographicCamera(30, 30 * (w/h));
        camera.position.set(bob.getEntity().getX() + ((float) BOB_WIDTH / 2), bob.getEntity().getY() + ((float) BOB_HEIGHT / 2), 0);
        camera.zoom += 2f; // Starting a bit zoomed in
        camera.update();

        // Creation of the viewport
        // We are using a Fill Viewport, since the entire screen is covered, whilst the aspect ratio is kept intact
        viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        handleInput();// The input for the zoom in and out
        bob.move();

        // Centres the camera on Bob and then updates it
        camera.position.set(bobSprite.getX(), bobSprite.getY(), 0);
        camera.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears the screen
        batch.setProjectionMatrix(camera.combined);

        // Sprite batch drawing
        batch.begin();
        batch.draw(image, 0, 0); //Temporary image to test Bob movement
        bob.draw(batch);
        batch.end();
    }

    private void handleInput() {
        //When Q is pressed, the camera is zoomed in, and zoomed out when E is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
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
    }
}
