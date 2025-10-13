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

    @Override
    public void create() {
        batch = new SpriteBatch();

//        alonso = new Texture("alonso.jpg");
        bob = new Texture("bob.jpg");

        sprite = new Sprite(bob);
        sprite.setBounds(0, 0, 320, 200);
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


        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && (sprite.getX() < (Gdx.graphics.getWidth() - 320))){
            sprite.translateX(speed * delta);
            System.out.println(Gdx.graphics.getWidth());
            System.out.println(sprite.getX());
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) && (sprite.getX() > 0)) {
            sprite.translateX(-speed * delta);
        }

    }
}
