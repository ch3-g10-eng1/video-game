package com.team3._8.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame implements ApplicationListener {
    private SpriteBatch batch;
    private FitViewport viewport;

    private Texture bob;

    private Texture background;

    private Sprite sprite;

    private Rectangle bobRectangle;
    private Rectangle collisionRectangle;


    public int bobHeight = 200;
    public int bobWidth = 320;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(1440,1080);
        bob = new Texture("bob.jpg");
        background = new Texture("background2.jpg");

        sprite = new Sprite(bob);
        sprite.setBounds(0, 0, bobWidth, bobHeight);

        bobRectangle = new Rectangle();
        collisionRectangle = new Rectangle();
    }

    @Override
    public void render() {

        input();

        draw();

        logic();

    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        batch.draw(background, 0, 0, worldWidth, worldHeight);
        sprite.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();

        bob.dispose();
        background.dispose();

    }

    @Override
    public void resize(int width, int height) {
        /*
        This method acts when the screen gets resized.
         */
        viewport.update(width, height, true);
    }

    private void input(){
        /*
        This method is the controller for the player character.
        Right now it only controls the movement on the x and y-axis.
        It gets the input key for the movement, and checks if the character is inside bounds before moving the character.
         */
        float speed = 120f;
        float delta = Gdx.graphics.getDeltaTime(); // Delta time is to ensure that it is the same speed on every machine.

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

    private void logic(){
        /*
        This method controls the logic regarding the collision boxes (Rectangles).
        Right now, there are two collision boxes, for the bob character and one in the corner,
        just to see how they work:
            - When the game is minimised, the collision boxes overlap, causing the if statement to activate repeatedly
         */
        bobRectangle.set(sprite.getX(), sprite.getY(), bobWidth, bobHeight);

        collisionRectangle.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), bobWidth, bobHeight);

        if (bobRectangle.overlaps(collisionRectangle)) {
            System.out.println("bob collision");
        }

    }

    @Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

}
