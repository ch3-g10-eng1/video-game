package com.team3._8.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture alonso;

    private Sprite sprite;

    @Override
    public void create() {
        batch = new SpriteBatch();

        alonso = new Texture("alonso.jpg");

        sprite = new Sprite(alonso);
        sprite.setBounds(256, 320, 512, 384);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();

        sprite.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

        alonso.dispose();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println(Math.abs(width - 1024)/2);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
//        sprite.setBounds(Math.abs(width - 1024)/2, Math.abs(height - 384)/2, width, height);
    }
}
