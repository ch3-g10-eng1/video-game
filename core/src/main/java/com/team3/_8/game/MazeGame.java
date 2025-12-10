package com.team3._8.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.impl.*;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MazeGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private MenuManager menuManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        camera = new OrthographicCamera();
        viewport = new FillViewport(200, 200, camera);

        menuManager = MenuManager.initialise(batch, font, camera, viewport);

        menuManager.registerMenu(MenuType.MAIN_MENU, new StartMenu(menuManager, batch, font, camera, viewport));
        menuManager.registerMenu(MenuType.GAME, new GameMenu(menuManager, batch, font, camera, viewport));
        menuManager.registerMenu(MenuType.WIN, new WinMenu(menuManager, batch, font, camera, viewport));
        menuManager.registerMenu(MenuType.LOSE, new LoseMenu(menuManager, batch, font, camera, viewport));
        menuManager.registerMenu(MenuType.PAUSE, new PauseMenu(menuManager, batch, font, camera, viewport));
        menuManager.registerMenu(MenuType.TUTORIAL, new TutorialMenu(menuManager, batch, font, camera, viewport));

        menuManager.setMenu(MenuType.MAIN_MENU);

    }

    /**
     * Renders different screens based on activeScreen configuration
     */
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        menuManager.update(delta);
        menuManager.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        menuManager.resize(width, height);
    }

    @Override
    public void dispose() {
        menuManager.dispose();
        batch.dispose();
        font.dispose();
    }
}
