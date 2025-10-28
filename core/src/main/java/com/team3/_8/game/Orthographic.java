package com.team3._8.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
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

    private TextureRegion bobFront;
    private TextureRegion bobLeft;
    private TextureRegion bobRight;
    private TextureRegion bobUp;
    private TextureRegion bobDown;
    private TextureRegion bobRocket;
    private Sprite bob;

    float bobX;
    float bobY;

    private TextureAtlas atlas;
    private Map maze;

    private boolean[] overlapping_walls;


    @Override
    public void create() {
        atlas = new TextureAtlas(Gdx.files.internal("assets\\atlas\\bob.atlas"));

        // Gets bob sprite from texture atlas
        bobFront = new TextureRegion(atlas.findRegion("front-bob-2"));
        bobRight = new TextureRegion(atlas.findRegion("side-bob-2"));

        bobLeft =  new TextureRegion(atlas.findRegion("side-bob-2"));
        bobLeft.flip(true, false);

        bobDown = bobFront;

        bobUp = new TextureRegion(atlas.findRegion("up-bob-2"));
        bobRocket =  new TextureRegion(atlas.findRegion("rocket-bob-2"));

        bob = new Sprite(atlas.findRegion("front-bob-2"));
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
        camera.position.set(bob.getX() + ((float) BOB_WIDTH / 2), bob.getY() + ((float) BOB_HEIGHT / 2), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        maze.renderMap(camera);
        batch.begin();
        bob.draw(batch);
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

        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || (Gdx.input.isKeyPressed(Input.Keys.D))) && !overlapping_walls[0] ) {
            bob.setRegion(bobRight); // Changes bob sprite icon
            bob.translateX(speed * delta);
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.A))) && !overlapping_walls[2]) {
            bob.setRegion(bobLeft);
            bob.translateX(-speed * delta);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) || (Gdx.input.isKeyPressed(Input.Keys.W))) && !overlapping_walls[3]) {
            bob.setRegion(bobUp);
            bob.translateY(speed * delta);
        }

        if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || (Gdx.input.isKeyPressed(Input.Keys.S))) && !overlapping_walls[1]) {
            bob.setRegion(bobDown);
            bob.translateY(-speed * delta);
        }
        
        
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
