package com.team3._8.game.menu.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.BaseMenu;
import com.team3._8.game.menu.type.MenuType;

import java.util.HashMap;
import java.util.Map;

/**
 * manages all game menus and the transitions between them.
 *
 * @author Sharjil
 */
public class MenuManager {

    private static MenuManager instance;

    private final Map<MenuType, BaseMenu> menus;

    private BaseMenu currentMenu, prevMenu;
    private MenuType currentType, prevType;

    private final SpriteBatch batch;;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private MenuManager(final SpriteBatch batch, final BitmapFont font, final OrthographicCamera camera, final Viewport viewport) {
        this.batch = batch;
        this.font = font;
        this.camera = camera;
        this.menus = new HashMap<>();
        this.viewport = viewport;
    }

    /**
     * Initialise the menu manager
     *
     */
    public static MenuManager initialise(final SpriteBatch batch, final BitmapFont font, final OrthographicCamera camera, final Viewport viewport) {
        if (instance == null) {
            instance = new MenuManager(batch, font, camera, viewport);
        }
        return instance;
    }

    /**
     * Get the singleton instance
     */
    public static MenuManager getInstance() {
        if  (instance == null) {
            throw new IllegalStateException("MenuManager instance is null");
        }
        return instance;
    }


    /**
     * Register a menu
     */
    public void registerMenu(final MenuType type, final BaseMenu menu) {
        this.menus.put(type, menu);
    }

    /**
     * Switch to a different menu
     */
    public void setMenu(MenuType type) {

        if (!menus.containsKey(type)) {
            throw new IllegalStateException("Menu type not found");
        }

        if (currentMenu != null) {
            currentMenu.hide();
            prevMenu = currentMenu;
            prevType = currentType;
        }

        currentType = type;
        currentMenu = menus.get(type);
        currentMenu.show();
    }

    /**
     * Get a menu class instance for the specified type
     */
    public BaseMenu getMenu(MenuType type) {
        BaseMenu menu = menus.get(type);
        if (menu == null) {
            throw new IllegalStateException("Menu type not found: " + type);
        }
        return menu;
    }

    /**
     * Go back to the previous menu which was open
     */
    public void goBack() {
        if (prevMenu != null) {
            BaseMenu temp = currentMenu;
            currentMenu.hide();
            currentMenu = prevMenu;
            prevMenu = temp;
            currentMenu.show();
        }
    }

    /**
     * Update and render current screen
     */
    public void update(float delta) {
        if (currentMenu != null) {
            currentMenu.handleInput();
            currentMenu.update(delta);
        }
    }

    public void render(float delta) {
        if (currentMenu != null) {
            currentMenu.render(delta);
        }
    }

    /**
     * Handle window resize
     */
    public void resize(int width, int height) {
        if (currentMenu != null) {
            currentMenu.resize(width, height);
        }
    }

    public void dispose() {
        for (BaseMenu menu : menus.values()) {
            menu.dispose();
        }
        menus.clear();
        instance = null;
    }

    public MenuType getCurrentType() {return currentType;}
    public MenuType getPrevType() {return prevType;}
}
