package com.team3._8.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Orthographic implements ApplicationListener {

    static final int WORLD_WIDTH = 200;
    static final int WORLD_HEIGHT = 100;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Sprite sprite;
    private Sprite bob;
    private float rotationSpeed;


    @Override
    public void create() {
        rotationSpeed = 0.5f;

        sprite = new Sprite(new Texture(Gdx.files.internal("background2.jpg")));
        sprite.setPosition(0,0);
        sprite.setSize(WORLD_WIDTH,WORLD_HEIGHT);

        bob = new Sprite(new Texture(Gdx.files.internal("bob.jpg")));
        bob.setPosition(0,0);
        bob.setSize(15,15);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (w / h));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 30f;
        camera.viewportHeight = 30f;
        camera.update();
    }

    @Override
    public void render() {
        handleInput();
        movement();
//        camera.lookAt(bob.getX(), bob.getY(), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.draw(batch);
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
        float speed = 120f;
        float delta = Gdx.graphics.getDeltaTime(); // Delta time is to ensure that it is the same speed on every machine.

        // Movement controller, can probably make it more efficient later (switch??) but for now it works well, learning stage frfr

        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || (Gdx.input.isKeyPressed(Input.Keys.D)))) {
            sprite.translateX(speed * delta);
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.A)))) {
            sprite.translateX(-speed * delta);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) || (Gdx.input.isKeyPressed(Input.Keys.W)))) {
            sprite.translateY(speed * delta);
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || (Gdx.input.isKeyPressed(Input.Keys.S)))) {
            sprite.translateY(-speed * delta);
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
