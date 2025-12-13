package com.team3._8.game.menu.impl;

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

import java.util.ArrayList;
import java.util.List;

public class TutorialMenu extends BaseMenu {

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;

    private final List<MenuButton> buttons = new ArrayList<>();
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final Vector3 touchPosition = new Vector3();

    private Texture buttonSheet;
    private BitmapFont titleFont, itemFont, descFont;
    private TextureRegion idle, hover, play;

    private Texture keycardTex, speedBoostTex, timeOrbTex;
    private Texture shieldTex, sizePotionTex, confusedTex, tomatoTex;
    private Texture bobTex, evilBobTex, gooseTex, speechBubbleTex;

    public TutorialMenu(final MenuManager menuManager,
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

        loadTextures();
        loadFonts();
        createButtons();
    }

    private void loadTextures() {
        keycardTex = new Texture("keycard.png");
        speedBoostTex = new Texture("speed_boost.png");
        timeOrbTex = new Texture("time_orb.png");
        shieldTex = new Texture("shield.png");
        sizePotionTex = new Texture("size_potion.png");
        confusedTex = new Texture("confused.png");
        tomatoTex = new Texture("tomato.png");
        bobTex = new Texture("atlas/bob.png");
        evilBobTex = new Texture("sprites/sprite_images/evil-bob_1.png");
        gooseTex = new Texture("atlas/security_geese.png");
        speechBubbleTex = new Texture("speech_bubble.png");
    }

    private void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 36;
        params.color = Color.GOLD;
        params.shadowOffsetX = 2;
        params.shadowOffsetY = 2;
        params.shadowColor = new Color(0, 0, 0, 0.8f);
        titleFont = generator.generateFont(params);

        params.size = 20;
        params.color = Color.WHITE;
        params.shadowOffsetX = 1;
        params.shadowOffsetY = 1;
        itemFont = generator.generateFont(params);

        params.size = 16;
        params.color = Color.LIGHT_GRAY;
        descFont = generator.generateFont(params);

        generator.dispose();
    }

    private void createButtons() {
        buttons.add(new MenuButton("Back", 0, 0, 220, 70, idle, hover, play,
                () -> menuManager.setMenu(MenuType.MAIN_MENU)));
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
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(uiCamera.combined);
        spriteBatch.begin();

        float screenW = uiViewport.getWorldWidth();
        float screenH = uiViewport.getWorldHeight();

        glyphLayout.setText(titleFont, "TUTORIAL");
        titleFont.draw(spriteBatch, glyphLayout, (screenW - glyphLayout.width) / 2f, screenH - 40);

        float startY = screenH - 100;
        float iconSize = 35;
        float leftX = 50;
        float midX = screenW / 2 - 150;
        float rightX = screenW / 2 + 220;
        float spacing = 70;

        glyphLayout.setText(itemFont, "COLLECTIBLES");
        itemFont.setColor(Color.CYAN);
        itemFont.draw(spriteBatch, glyphLayout, leftX, startY + 35);
        itemFont.setColor(Color.WHITE);

        drawItem(leftX, startY - 10, iconSize, keycardTex, "Keycard", "Unlocks the doors");
        drawItem(leftX, startY - 10 - spacing, iconSize, speedBoostTex, "Speed Boost", "Run faster temporarily");
        drawItem(leftX, startY - 10 - spacing * 2, iconSize, timeOrbTex, "Time Orb", "Subtracts from timer");
        drawItem(leftX, startY - 10 - spacing * 3, iconSize, shieldTex, "Shield", "Become invincible");
        drawItem(leftX, startY - 10 - spacing * 4, iconSize, sizePotionTex, "Shrink Potion", "Makes Bob smaller");
        drawItem(leftX, startY - 10 - spacing * 5, iconSize, confusedTex, "Confusion", "Reverses your controls");
        drawItem(leftX, startY - 10 - spacing * 6, iconSize, tomatoTex, "Tomato", "Splat covers screen");

        glyphLayout.setText(itemFont, "INTERACTABLES");
        itemFont.setColor(Color.CYAN);
        itemFont.draw(spriteBatch, glyphLayout, midX, startY + 35);
        itemFont.setColor(Color.WHITE);

        drawItem(midX, startY - 10, iconSize, bobTex, "Bob", "You play as Bob");
        drawItem(midX, startY - 10 - spacing, iconSize, evilBobTex, "Evil Bob", "Give him keycard or fight");
        drawItem(midX, startY - 10 - spacing * 2, iconSize, gooseTex, "Campus Security", "Don't let them catch you");
        drawItem(midX, startY - 10 - spacing * 3, iconSize, speechBubbleTex, "Puzzle Event", "Solve for speed boost");

        glyphLayout.setText(itemFont, "ACHIEVEMENTS");
        itemFont.setColor(Color.GOLD);
        itemFont.draw(spriteBatch, glyphLayout, rightX, startY + 35);
        itemFont.setColor(Color.WHITE);

        drawAchievement(rightX, startY - 10, "Speed Run", "Beat game in under 2 mins");
        drawAchievement(rightX, startY - 10 - spacing, "Positive Collector", "Collect all good items");
        drawAchievement(rightX, startY - 10 - spacing * 2, "Negative Collector", "Collect all bad items");
        drawAchievement(rightX, startY - 10 - spacing * 3, "Goose Chaser", "Get caught twice");
        drawAchievement(rightX, startY - 10 - spacing * 4, "All Events", "Trigger every event");
        drawAchievement(rightX, startY - 10 - spacing * 5, "Completionist", "Unlock everything else");

        for (MenuButton button : buttons) {
            TextureRegion frame = button.getFrame();
            spriteBatch.draw(frame, button.bounds.x, button.bounds.y, button.bounds.width, button.bounds.height);

            glyphLayout.setText(itemFont, button.text);
            itemFont.draw(spriteBatch, glyphLayout,
                    button.bounds.x + (button.bounds.width - glyphLayout.width) / 2f,
                    button.bounds.y + (button.bounds.height + glyphLayout.height) / 2f);
        }

        spriteBatch.end();
    }

    private void drawItem(float x, float y, float size, Texture tex, String name, String desc) {
        spriteBatch.draw(tex, x, y, size, size);

        glyphLayout.setText(itemFont, name);
        itemFont.draw(spriteBatch, glyphLayout, x + size + 10, y + size - 8);

        glyphLayout.setText(descFont, desc);
        descFont.draw(spriteBatch, glyphLayout, x + size + 10, y + size - 28);
    }

    private void drawAchievement(float x, float y, String name, String desc) {
        glyphLayout.setText(itemFont, name);
        itemFont.draw(spriteBatch, glyphLayout, x, y + 20);

        glyphLayout.setText(descFont, desc);
        descFont.draw(spriteBatch, glyphLayout, x, y);
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            menuManager.setMenu(MenuType.MAIN_MENU);
            return true;
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
        float centerX = screenW / 2f - btnW / 2f;

        buttons.get(0).bounds.set(centerX, 40, btnW, btnH);
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
        if (buttonSheet != null) buttonSheet.dispose();
        if (titleFont != null) titleFont.dispose();
        if (itemFont != null) itemFont.dispose();
        if (descFont != null) descFont.dispose();
        if (keycardTex != null) keycardTex.dispose();
        if (speedBoostTex != null) speedBoostTex.dispose();
        if (timeOrbTex != null) timeOrbTex.dispose();
        if (shieldTex != null) shieldTex.dispose();
        if (sizePotionTex != null) sizePotionTex.dispose();
        if (confusedTex != null) confusedTex.dispose();
        if (tomatoTex != null) tomatoTex.dispose();
        if (bobTex != null) bobTex.dispose();
        if (evilBobTex != null) evilBobTex.dispose();
        if (gooseTex != null) gooseTex.dispose();
        if (speechBubbleTex != null) speechBubbleTex.dispose();
    }
}
