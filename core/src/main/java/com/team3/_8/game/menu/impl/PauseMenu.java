package com.team3._8.game.menu.impl;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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

public class PauseMenu extends BaseMenu {

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;

    private final List<MenuButton> buttons = new ArrayList<>();
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final Vector3 touchPosition =  new Vector3();

    private Texture backgroundTexture, buttonSheet, tittleBanner, pixelTexture;

    private BitmapFont tittleFont, buttonFont;

    private TextureRegion idle, hover, play, bannerRegion;

    private boolean pixelTextureCreated = false;

    private final Color overlayColor = new Color(0f, 0f, 0f, 0.7f);


    public PauseMenu(final MenuManager menuManager,
                     final SpriteBatch batch,
                     final BitmapFont font,
                     final OrthographicCamera camera,
                     final Viewport viewport) {

        super(menuManager, batch, font, camera, viewport);

        this.uiCamera = new OrthographicCamera();
        this.uiViewport = new ScreenViewport(uiCamera);

        buttonSheet = new Texture("ui/buttons.png");
        buttonSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion[][] split = TextureRegion.split(buttonSheet, 104, 35);

        idle = split[0][0];
        hover = split[0][1];
        play = split[0][2];

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixelTexture = new Texture(pixmap);
        pixmap.dispose();
        pixelTextureCreated = true;


        //TODO: create paused title region

        loadFonts();
        createButtons();

    }

  public PauseMenu(MenuManager menuManager,
                  SpriteBatch batch,
                  BitmapFont defaultFont,
                  OrthographicCamera camera,
                  Viewport viewport,
                  BitmapFont titleFont,
                  BitmapFont buttonFont,
                  TextureRegion idle,
                  TextureRegion hover,
                  TextureRegion play) {

      super(menuManager, batch, defaultFont, camera, viewport);

      this.uiCamera = camera;
      this.uiViewport = viewport;

      this.tittleFont = titleFont;
      this.buttonFont = buttonFont;

      this.idle = idle;
      this.hover = hover;
      this.play = play;

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
        buttons.add(new MenuButton("Resume", 0, 0, 220, 70, idle, hover, play, () -> menuManager.setMenu(MenuType.GAME)));
        buttons.add(new MenuButton("Restart",0, 0, 220, 70, idle, hover, play, () -> menuManager.setMenu(MenuType.MAIN_MENU)));
        buttons.add(new MenuButton("Quit", 0, 0, 220, 70, idle, hover, play, () -> Gdx.app.exit()));

        repositionButtons();
    }

    @Override
    public void show() {
        super.show();
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        repositionButtons();
    }

    @Override
    public void render(float delta) {

        menuManager.getMenu(MenuType.GAME).render(delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatch.setProjectionMatrix(uiCamera.combined);
        spriteBatch.begin();

        spriteBatch.setColor(overlayColor);
        spriteBatch.draw(pixelTexture, 0, 0, uiViewport.getWorldWidth(), uiViewport.getWorldHeight());

        spriteBatch.setColor(Color.WHITE);

        glyphLayout.setText(tittleFont, "PAUSED");

        float titleX = (uiViewport.getWorldWidth() - glyphLayout.width) / 2f;
        float titleY = uiViewport.getWorldHeight() * 0.85f;
        tittleFont.draw(spriteBatch, glyphLayout, titleX, titleY);

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            menuManager.setMenu(MenuType.GAME);
            return true;
        }

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

        return false;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        uiViewport.update(width, height, true);
        repositionButtons();
    }

    private void repositionButtons() {
        float screenW = uiViewport.getWorldWidth();
        float screenH = uiViewport.getWorldHeight();

        float btnW = 220;
        float btnH = 70;
        float spacing = 20;

        float centerX = screenW / 2f - btnW / 2f;
        float centerY = screenH / 2f;

        float totalHeight = (btnH * 3) + (spacing * 2);
        float startY = centerY + totalHeight / 2f - btnH;

        buttons.get(0).bounds.set(centerX, startY, btnW, btnH);
        buttons.get(1).bounds.set(centerX, startY - btnH - spacing, btnW, btnH);
        buttons.get(2).bounds.set(centerX, startY - 2 * (btnH + spacing), btnW, btnH);
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
    public void dispose() {
        if (pixelTextureCreated && pixelTexture != null) {
            pixelTexture.dispose();
        }
        if (buttonSheet != null) buttonSheet.dispose();
        if (tittleFont != null) tittleFont.dispose();
        if (buttonFont != null) buttonFont.dispose();
    }

  // Used for testing purposes only

  public List<MenuButton> getButtons() {
    return buttons;
  }
}
