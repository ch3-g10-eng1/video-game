package com.team3._8.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Orthographic implements ApplicationListener {

    static final int WORLD_WIDTH = 60;
    static final int WORLD_HEIGHT = 120;

    static final int BOB_HEIGHT = 15;
    static final int BOB_WIDTH = 15;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    // Vars to hold each set of animation images
    private Animation<TextureRegion> bobFront;
    private Animation<TextureRegion> bobLeft;
    private Animation<TextureRegion> bobRight;
    private Animation<TextureRegion> bobUp;
    private Animation<TextureRegion> bobDown;
    private Animation<TextureRegion> bobRocket;
    private Animation<TextureRegion> bobSquash;

    private Sprite bob;

    // Used to control animation time
    float stateTime;

    private TextureAtlas atlas;
    private Map maze;

    private boolean[] overlapping_walls;


    @Override
    public void create() {
        atlas = new TextureAtlas(Gdx.files.internal("assets\\atlas\\bob.atlas"));

        // Loads animation frames
        Array<TextureAtlas.AtlasRegion> frontFrames = atlas.findRegions("front-bob");
        // Creates animation object
        bobFront = new Animation<TextureRegion>(0.5f, frontFrames);

        Array<TextureAtlas.AtlasRegion> rightFrames = atlas.findRegions("side-bob");
        bobRight = new Animation<TextureRegion>(0.5f, rightFrames);

        // Flips right into left frames
        Array<TextureRegion> leftFrames = new Array<>();
        for (TextureRegion frame: rightFrames){
            TextureRegion temp_frame = new TextureRegion(frame);
            temp_frame.flip(true, false);
            leftFrames.add(temp_frame);
        }
        bobLeft = new Animation<TextureRegion>(0.5f, leftFrames);

        Array<TextureAtlas.AtlasRegion> upFrames = atlas.findRegions("up-bob");
        bobUp = new Animation<TextureRegion>(0.5f, upFrames);

        bobDown = bobFront;
        
        Array<TextureAtlas.AtlasRegion> squashFrames = atlas.findRegions("squash-bob");
        bobSquash = new Animation<TextureRegion>(0.5f, squashFrames);

        Array<TextureAtlas.AtlasRegion> rocketFrames = atlas.findRegions("rocket-bob");
        bobRocket = new Animation<TextureRegion>(0.1f, rocketFrames);

        bob = new Sprite(atlas.findRegion("front-bob"));
        bob.setSize(BOB_WIDTH,BOB_HEIGHT);
        bob.setPosition(30,30);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(30, 30 * (w / h));
        camera.position.set(bob.getX() + ((float) BOB_WIDTH / 2), bob.getY() + ((float) BOB_HEIGHT / 2), 0);
        camera.zoom = 2f;
        camera.update();
    
        maze = new Map("test_map.tmx", "walls");
        maze.renderMap(camera);


        batch = new SpriteBatch();
        stateTime = 0f;
        viewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        handleInput();
        movement();

        // Clears past animation & ticks to next frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();

        camera.position.set(bob.getX() + ((float) BOB_WIDTH / 2), bob.getY() + ((float) BOB_HEIGHT / 2), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        maze.renderMap(camera);
        batch.begin();
        bob.draw(batch); // Renders bob sprite animation
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }
    }

    private void movement(){
        // Max 500 to prevent issues with collision boxes
        float speed = 30f;
        float delta = Gdx.graphics.getDeltaTime(); // Delta time is to ensure that it is the same speed on every machine.

        // Movement controller, can probably make it more efficient later (switch??) but for now it works well, learning stage frfr

        //X-axis
        overlapping_walls = maze.hits_wall(bob, speed, delta);

        // Default animation if no movement
        TextureRegion bobAnimation = bobFront.getKeyFrame(stateTime, true);

        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || (Gdx.input.isKeyPressed(Input.Keys.D))) && !overlapping_walls[0] ) {
            // Sets animation for right movement
            bobAnimation = bobRight.getKeyFrame(stateTime, true);
            bob.translateX(speed * delta);
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.A))) && !overlapping_walls[2]) {
            // bob.setRegion(bobLeft);
            bobAnimation = bobLeft.getKeyFrame(stateTime, true);
            bob.translateX(-speed * delta);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) || (Gdx.input.isKeyPressed(Input.Keys.W))) && !overlapping_walls[3]) {
           bobAnimation = bobUp.getKeyFrame(stateTime, true);
            bob.translateY(speed * delta);
        }

        if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || (Gdx.input.isKeyPressed(Input.Keys.S))) && !overlapping_walls[1]) {
            bobAnimation = bobDown.getKeyFrame(stateTime, true);
            bob.translateY(-speed * delta);
        }

        // Sets bob sprite to run the animation configured above
        bob.setRegion(bobAnimation);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
