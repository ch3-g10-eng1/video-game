package com.team3._8.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame extends ApplicationAdapter {
    private SpriteBatch batch;

//    private Texture alonso;

    private Texture bob;

    private Sprite sprite;

    public int bobHeight = 200;
    public int bobWidth = 320;

    @Override
    public void create() {
        batch = new SpriteBatch();

//        alonso = new Texture("alonso.jpg");
        bob = new Texture("bob.jpg");

        sprite = new Sprite(bob);
        sprite.setBounds(0, 0, bobWidth, bobHeight);
    }

    @Override
    public void render() {

        input();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();

        sprite.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

        bob.dispose();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println(Math.abs(width - 1024)/2);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    private void input(){
        float speed = 40f;
        float delta = Gdx.graphics.getDeltaTime();

        // Movement controller, can probably make it more efficient later (switch??) but for now it works well, learning stage frfr

        //X-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) || (Gdx.input.isKeyPressed(Input.Keys.D))) && (sprite.getX() < (Gdx.graphics.getWidth() - bobWidth))) {
            sprite.translateX(speed * delta);
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.A))) && (sprite.getX() > 0)) {
            sprite.translateX(-speed * delta);
        }

        //Y-axis
        if (((Gdx.input.isKeyPressed(Input.Keys.UP)) || (Gdx.input.isKeyPressed(Input.Keys.W))) && (sprite.getY() < (Gdx.graphics.getHeight() - bobHeight))) {
            sprite.translateY(speed * delta);
        }
        if (((Gdx.input.isKeyPressed(Input.Keys.DOWN)) || (Gdx.input.isKeyPressed(Input.Keys.S))) && (sprite.getY() > 0)) {
            sprite.translateY(-speed * delta);
        }

    }
}
