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
import com.team3._8.game.storage.data.LeaderboardData;
import com.team3._8.game.storage.manager.TimeStorageManager;
import com.team3._8.game.storage.model.LeaderboardEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class WinMenu extends BaseMenu {

    private OrthographicCamera uiCamera;
    private Viewport uiViewport;

    private final List<MenuButton> buttons = new ArrayList<>();
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final Vector3 touchPosition =  new Vector3();

    private Texture buttonSheet;

    private BitmapFont tittleFont, buttonFont, statsFont;

    private TextureRegion idle, hover, play;

    private float completionTime = 0f;
    private List<Float> previousTimes = new ArrayList<>();
    private Map<String, Boolean> achievements = new HashMap<>();

    private boolean askForName = false;
    private String playerName = "";
    private TimeStorageManager storageManager;
    private LeaderboardData leaderboard;


    public WinMenu(final MenuManager menuManager,
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

        loadFonts();
        createButtons();

        initialiseDummyData();
    }

    public WinMenu(
        MenuManager menuManager,
        SpriteBatch batch,
        BitmapFont font,
        OrthographicCamera camera,
        Viewport viewport,
        BitmapFont titleFont,
        BitmapFont buttonFont,
        BitmapFont statsFont,
        TextureRegion idle,
        TextureRegion hover,
        TextureRegion play
        ) {
        super(menuManager, batch, font, camera, viewport);

        this.uiCamera = camera;
        this.uiViewport = viewport;

        this.tittleFont = titleFont;
        this.buttonFont = buttonFont;
        this.statsFont = statsFont;

        this.idle = idle;
        this.hover = hover;
        this.play = play;

        createButtons();
        initialiseDummyData();
    }

    private void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =  new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 40;
        params.color = Color.GOLD;
        params.shadowOffsetX = 3;
        params.shadowOffsetY = 3;
        params.shadowColor = new Color(0, 0, 0, 0.8f);
        tittleFont = generator.generateFont(params);

        params.size = 22;
        params.shadowOffsetX = 1;
        params.shadowOffsetY = 1;
        params.color = Color.WHITE;
        buttonFont = generator.generateFont(params);

        params.size = 18;
        params.color = Color.LIGHT_GRAY;
        statsFont = generator.generateFont(params);

        generator.dispose();
    }

    private void createButtons() {
        buttons.add(new MenuButton("Play Again", 0, 0, 220, 70, idle, hover, play, () -> menuManager.setMenu(MenuType.GAME)));
        buttons.add(new MenuButton("Title Screen",0, 0, 220, 70, idle, hover, play, () -> menuManager.setMenu(MenuType.MAIN_MENU)));
        buttons.add(new MenuButton("Quit", 0, 0, 220, 70, idle, hover, play, () -> Gdx.app.exit()));

        repositionButtons();
    }

    private void initialiseDummyData() {
        previousTimes.clear();
        previousTimes.add(45.2f);
        previousTimes.add(52.8f);
        previousTimes.add(61.3f);
        previousTimes.add(89.7f);
        previousTimes.add(125.4f);
    }

    public void setCompletionTime(float completionTime) {
        this.completionTime = completionTime;
    }

    public void setAchievements(Map<String, Boolean> achievements) {
        this.achievements = achievements;
    }


    @Override
    public void show() {
        super.show();
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        repositionButtons();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.1f, 0.2f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(uiCamera.combined);
        spriteBatch.begin();

        float screenW = uiViewport.getWorldWidth();
        float screenH = uiViewport.getWorldHeight();

        glyphLayout.setText(tittleFont, "You Won!");

        float titleX = (screenW- glyphLayout.width) / 2f;
        float titleY = screenH * 0.85f;
        tittleFont.draw(spriteBatch, glyphLayout, titleX, titleY);

        String timeText = String.format("Your time: %.2f seconds", completionTime);
        glyphLayout.setText(statsFont, timeText);
        statsFont.setColor(Color.YELLOW);
        statsFont.draw(spriteBatch, glyphLayout, (screenW - glyphLayout.width) /2f, titleY - 60);

        if (askForName) {
            glyphLayout.setText(statsFont, "New High Score! Enter your name: ");
            statsFont.draw(spriteBatch, glyphLayout, (screenW - glyphLayout.width) /2f, titleY - 120);

            glyphLayout.setText(statsFont, playerName + "_");
            statsFont.draw(spriteBatch, glyphLayout,
                (screenW - glyphLayout.width) / 2f,
                titleY - 150);


        }

        glyphLayout.setText(statsFont, "Previous Best Times:");
        statsFont.setColor(Color.LIGHT_GRAY);
        float timesY = titleY - 100;
        statsFont.draw(spriteBatch, glyphLayout,
            (screenW - glyphLayout.width) / 2f,
            timesY);

        timesY -= 30;
        if (leaderboard != null) {

            List<LeaderboardEntry> entries = leaderboard.getEntries();

            for (int i = 0; i < Math.min(5, entries.size()); i++) {
                LeaderboardEntry entry = entries.get(i);
                String line = String.format("%d. %s - %.2f seconds",
                    i + 1,
                    entry.getName(),
                    entry.getTime());

                glyphLayout.setText(statsFont, line);
                statsFont.draw(spriteBatch, glyphLayout,
                    (screenW - glyphLayout.width) / 2f,
                    timesY - (i * 25));
            }
        }

        if (!achievements.isEmpty()) {
            float leftX = 50;
            float achievementY = screenH * 0.75f;
            float boxWidth = 280;
            float boxHeight = 35;
            float spacing = 10;

            glyphLayout.setText(statsFont, "ACHIEVEMENTS");
            statsFont.setColor(Color.GOLD);
            statsFont.draw(spriteBatch, glyphLayout, leftX + 10, achievementY + 10);

            achievementY -= 40;

            String[] achOrder = {"Speed Run", "Positive Collector", "Negative Collector",
                                 "Goose Chaser", "All Events", "Completionist"};

            for (String achName : achOrder) {
                if (achievements.containsKey(achName)) {
                    boolean unlocked = achievements.get(achName);

                    Gdx.gl.glEnable(GL20.GL_BLEND);
                    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

                    spriteBatch.end();
                    spriteBatch.begin();

                    if (unlocked) {
                        statsFont.setColor(Color.WHITE);
                        statsFont.draw(spriteBatch, achName, leftX + 10, achievementY + 22);

                        glyphLayout.setText(statsFont, "DONE");
                        statsFont.setColor(Color.GREEN);
                        statsFont.draw(spriteBatch, glyphLayout,
                                     leftX + boxWidth - glyphLayout.width - 10, achievementY + 22);
                    } else {
                        statsFont.setColor(Color.DARK_GRAY);
                        statsFont.draw(spriteBatch, achName, leftX + 10, achievementY + 22);

                        statsFont.setColor(new Color(0.3f, 0.3f, 0.3f, 1));
                        statsFont.draw(spriteBatch, "LOCKED", leftX + boxWidth - 70, achievementY + 22);
                    }

                    achievementY -= (boxHeight + spacing);
                }
            }
        }


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

        float startY = screenH * 0.15f;

        buttons.get(0).bounds.set(centerX, startY + btnH + spacing, btnW, btnH);
        buttons.get(1).bounds.set(centerX, startY, btnW, btnH);
        buttons.get(2).bounds.set(centerX, startY - btnH - spacing, btnW, btnH);
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
        if (tittleFont != null) tittleFont.dispose();
        if (buttonFont != null) buttonFont.dispose();
        if (statsFont != null) statsFont.dispose();
    }

    public void setPreviousTimes(List<Float> times) {
        previousTimes = times;
    }

    public void setLeaderboard(LeaderboardData data) {
        this.leaderboard = data;
    }

    public void enableNameEntry(float completionTime, TimeStorageManager manager, LeaderboardData data) {
        this.askForName = true;
        this.completionTime = completionTime;
        this.storageManager = manager;
        this.leaderboard = data;
    }

    // Used for testing purposes only

    public List<MenuButton> getButtons() {
        return buttons;
    }
}

