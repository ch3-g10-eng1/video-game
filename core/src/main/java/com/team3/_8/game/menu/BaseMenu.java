package com.team3._8.game.menu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.manager.MenuManager;

public abstract class BaseMenu {

    protected final SpriteBatch spriteBatch;
    protected final BitmapFont font;
    protected final OrthographicCamera camera;
    protected final Viewport viewport;
    protected final MenuManager menuManager;

    protected float stateTime = 0f;

    public BaseMenu(final MenuManager menuManager, final SpriteBatch spriteBatch, final BitmapFont font, final OrthographicCamera camera, final Viewport viewport) {
        this.menuManager = menuManager;
        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = camera;
        this.viewport = viewport;
    }

    /**
     * Called when this screen becomes active
     */
    public void show() {
        stateTime = 0f;
    }

    /**
     * Called when switching away from this screen
     */
    public void hide() {

    }

    /**
     * Logic to handle screen updates - this is called before rendering
     *
     * @param delta Time since last frame
     */
    public abstract void update(float delta);

    /**
     * Render the screen
     *
     * @param delta Time since last frame
     */
    public abstract void render(float delta);

    /**
     * Logic to handle input for this screen
     *
     * @return true if input was handled
     */
    public abstract boolean handleInput();

    /**
     * Called when the screen is being disposed
     */
    public void dispose() {}

    /**
     * Handles resizing
     *
     * @param width new window width
     * @param height new window height
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
