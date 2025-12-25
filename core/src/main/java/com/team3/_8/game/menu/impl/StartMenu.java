package com.team3._8.game.menu.impl;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.BaseMenu;
import com.team3._8.game.menu.button.MenuButton;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class StartMenu extends BaseMenu {

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;

    private final List<MenuButton> buttons = new ArrayList<>();
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final Vector3 touchPosition =  new Vector3();

    private Texture backgroundTexture, buttonSheet, tittleBanner;

    private BitmapFont tittleFont, buttonFont;

    private TextureRegion idle, hover, play, bannerRegion;

    public StartMenu(final MenuManager menuManager,
                     final SpriteBatch batch,
                     final BitmapFont font,
                     final OrthographicCamera camera,
                     final Viewport viewport) {

        super(menuManager, batch, font, camera, viewport);

        uiCamera = new  OrthographicCamera();
        uiViewport = new ScreenViewport(uiCamera);


        buttonSheet = new Texture("ui/buttons.png");
        buttonSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion[][] split = TextureRegion.split(buttonSheet, 104, 35);

        idle = split[0][0];
        hover = split[0][1];
        play = split[0][2];

        tittleBanner = new Texture("ui/title_banner.png");
        tittleBanner.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bannerRegion = new  TextureRegion(tittleBanner);

        loadFonts();
        createButtons();

    }

    private void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =  new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 40;
        params.color = Color.WHITE;
        params.shadowOffsetX = 2;
        params.shadowOffsetY = 2;
        params.shadowColor = new Color(0, 0, 0, 0.5f);
        tittleFont = generator.generateFont(params);

        params.size = 22;
        params.shadowOffsetX = 1;
        params.shadowOffsetY = 1;
        params.color = Color.WHITE;
        buttonFont = generator.generateFont(params);

        generator.dispose();
    }

    private void createButtons() {
        buttons.add(new MenuButton("Play", 0, 0, 220, 70, idle, hover, play,
            () -> {
                GameMenu gameMenu = (GameMenu) menuManager.getMenu(MenuType.GAME);
                gameMenu.setIntialised(false);
                menuManager.setMenu(MenuType.GAME);
            }
            ));

        buttons.add(new MenuButton("Tutorial", 0, 0, 220, 70, idle, hover, play,
            () -> menuManager.setMenu(MenuType.TUTORIAL)));

        buttons.add(new MenuButton("Quit", 0, 0, 220, 70, idle, hover, play,
            Gdx.app::exit));
    }

    @Override
    public void show(){
        super.show();
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() /2f, 0);
        camera.update();
    }

    @Override
    public void update(float delta) {
        touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        uiCamera.unproject(touchPosition);

        for (MenuButton b : buttons) {
            b.hovered = b.bounds.contains(touchPosition.x, touchPosition.y);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            for (MenuButton b : buttons) {
                b.clicked = b.hovered;
            }
        } else {
            for (MenuButton b : buttons) {
                b.clicked = false;
            }
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        spriteBatch.setProjectionMatrix(uiCamera.combined);
        spriteBatch.begin();

        if (backgroundTexture != null) {
            spriteBatch.draw(
                backgroundTexture,
                0,0,
                uiViewport.getWorldWidth(),
                uiViewport.getWorldHeight()
            );
        }

        glyphLayout.setText(tittleFont, "The Life of Bob");

        float screenW = uiViewport.getWorldWidth();
        float screenH = uiViewport.getWorldHeight();

        float bannerW = glyphLayout.width + 200;
        float bannerH = 120f;

        float bannerX = (screenW - bannerW) / 2f;
        float bannerY = screenH - bannerH -60f;

        spriteBatch.draw(bannerRegion, bannerX, bannerY, bannerW, bannerH);

        float titleX = (screenW - glyphLayout.width) / 2f;
        float titleY = bannerY + bannerH / 2f + glyphLayout.height / 2f;

        tittleFont.draw(
            spriteBatch,
            glyphLayout,
            titleX, titleY
        );

        for (MenuButton button : buttons) {

            TextureRegion frame = button.getFrame();

            spriteBatch.draw(frame, button.bounds.x, button.bounds.y,  button.bounds.width, button.bounds.height);

            glyphLayout.setText(buttonFont, button.text);

            buttonFont.draw(
                spriteBatch,
                glyphLayout,
                button.bounds.x + (button.bounds.width - glyphLayout.width) / 2f,
                button.bounds.y + (button.bounds.height + glyphLayout.height) / 2f);

        }

        spriteBatch.end();
    }

    @Override
    public boolean handleInput() {

        if (Gdx.input.justTouched()) {
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            uiCamera.unproject(touchPosition);

            for (MenuButton button : buttons) {
                if (button.bounds.contains(touchPosition.x, touchPosition.y)) {
                    button.onClick.run();
                    return true;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            buttons.get(0).onClick.run();
            return true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return true;
        }


        return false;
    }

    @Override
    public void dispose() {
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }

        if (buttonSheet != null) {
            buttonSheet.dispose();
        }

        tittleFont.dispose();
        buttonFont.dispose();
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height, true);
        uiCamera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() /2f, 0);
        uiCamera.update();

        this.repositionButtons();
    }

    public void repositionButtons() {
        float screenW = viewport.getWorldWidth();
        float screenH = viewport.getWorldHeight();

        float btnW = 220;
        float btnH = 70;

        float centerX = screenW / 2f - btnW / 2f;
        float centerY = screenH / 2f;

        buttons.get(0).bounds.set(centerX, centerY + 60, btnW, btnH);
        buttons.get(1).bounds.set(centerX, centerY - 30, btnW, btnH);
        buttons.get(2).bounds.set(centerX, centerY - 120, btnW, btnH);
    }

  // Used for testing purposes only

  public List<MenuButton> getButtons() {
    return buttons;
  }


}
