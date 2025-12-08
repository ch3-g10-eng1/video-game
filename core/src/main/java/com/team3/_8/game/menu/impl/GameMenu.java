package com.team3._8.game.menu.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.*;
import com.team3._8.game.menu.BaseMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

import java.util.HashMap;
import java.util.Map;

public class GameMenu extends BaseMenu {

    private static final int WORLD_WIDTH = 200;
    private static final int WORLD_HEIGHT = 200;
    private static final int BOB_WIDTH = 15;
    private static final int BOB_HEIGHT = 15;

    private OrthographicCamera gameCamera;
    private Viewport gameViewport;

    private Bob bob;
    private Sprite bobSprite;
    private EvilBob evilBob;
    private CollectableEntity keycard;
    private Maze maze;
    private HUD hud;

    private final CampusSecurity[] allCampusSecuritySprites = new  CampusSecurity[5];
    private boolean campusSecurityCreated;

    private boolean paused = false, intialised = false;
    private float timer = 0f;
    private int events =0;
    private boolean[] movement_halter;

    private Map<String, Boolean> evilBobReturnData = new HashMap<>();
    private Map<String, Boolean> campusSecurityReturnData = new HashMap<>();
    private Map<String, Integer> eventTracker;

    public GameMenu(MenuManager menuManager, SpriteBatch batch, BitmapFont font,
                    OrthographicCamera camera, Viewport viewport) {
        super(menuManager, batch, font, camera, viewport);

        gameCamera = new OrthographicCamera(30, 30 * ((float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        gameViewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, gameCamera);

        initialiseGame();

    }

    private void initialiseGame() {

        timer = 0f;
        events = 0;
        paused = false;
        campusSecurityCreated = false;

        createLayers();

        createBob();

        createEvilBob();

        createKeycard();

        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        gameCamera.position.set(
            bob.getEntity().getX() - ((float) BOB_WIDTH / 2),
            bob.getEntity().getY() - ((float) BOB_HEIGHT / 2),
            0
        );
        gameCamera.zoom = 2f;
        gameCamera.update();

        gameViewport.apply(true);

        SpriteBatch hudBatch = new SpriteBatch();
        hud = new HUD(hudBatch, events);
        eventTracker = GameController.setEventMap();

    }

    private void createLayers() {
        String[] collidable_layers = {"Collision", "Doors"};
        maze = new Maze("Map/CSE_map.tmx", collidable_layers, "WinDoors", "EventTrigger");

    }

    private void createBob() {
        TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(100, 500);
        bobSprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        bob = new Bob(bobSprite, 60, -3);
    }

    private void createEvilBob() {
        evilBob = createSprite(
            "atlas/bob.atlas",
            "evil-bob",
            500, 500,
            2 * BOB_WIDTH,
            2 * BOB_HEIGHT,
            0,
            EvilBob::new
        );
    }

    private void createKeycard() {
        Texture keycardTexture = new Texture("keycard.png");
        Sprite keycardSprite = new Sprite(keycardTexture);
        keycardSprite.setPosition(20, 20);
        keycardSprite.setSize(BOB_WIDTH * 2, BOB_HEIGHT * 2);
        keycard = new CollectableEntity(keycardSprite, 0, "Keycard");
    }

    @Override
    public void show() {
        super.show();

        MenuType prevMenuType = menuManager.getPrevType();
        if (prevMenuType == MenuType.WIN || prevMenuType == MenuType.LOSE) {
            resetGame();
        }else if (!intialised) {
            initialiseGame();
            intialised = true;
        }

        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        gameCamera.update();

        paused = false;
    }

    @Override
    public void update(float delta) {
        if (!paused) {
            if (keycard.collected(bob)) {
                eventTriggered("Positive");
                evilBob.setPlayerHasKeycard(true);
                maze.removeCollisionLayer("Doors");
                maze.removeVisibleLayer("ClosedDoors");

            }

            movement_halter = maze.hitsWall(bob, delta);
            evilBobReturnData = evilBob.collision(bob);

            handleInteraction();

            bob.move(movement_halter);

            timer += delta;

            if (maze.HitsWinLayer(bob)) {
                WinMenu winMenu = (WinMenu) menuManager.getMenu(MenuType.WIN);

                if (winMenu != null) {
                    winMenu.setCompletionTime(timer);
                }

                menuManager.setMenu(MenuType.WIN);
            }

            if (maze.HitsEventLayer(bob)) {
                eventTriggered("Negative");
            }

            if (timer >= 300) {
                menuManager.setMenu(MenuType.LOSE);
            }
        }

        gameCamera.position.set(
            bobSprite.getX() + bobSprite.getWidth() / 2,
            bobSprite.getY() + bobSprite.getHeight() / 2,
            0
        );
        gameCamera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(gameCamera.combined);
        maze.renderMap(gameCamera);

        spriteBatch.begin();

        evilBob.draw(spriteBatch, 1000, 1050);
        bob.draw(spriteBatch);
        keycard.draw(spriteBatch);

        if (campusSecurityCreated) {
            int mod = 0;
            for (int i = 0; i < 5; i++) {
                allCampusSecuritySprites[i].draw(spriteBatch, 870 + mod, 1150, maze.hitsWall(allCampusSecuritySprites[i], delta));
                mod += 35;
            }
        }

        spriteBatch.end();

        if (gameViewport.getScreenWidth() > 0 && gameViewport.getScreenHeight() > 0) {
            hud.draw(font, GameController.formatTime(timer), eventTracker, bob, paused, gameViewport);
        }

    }

    @Override
    public boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = !paused;
            if (paused && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
                menuManager.setMenu(MenuType.PAUSE);
            }
            return true;
        }

        boolean dev_zoom = false;
        if (!paused) {
            paused = GameController.handleInput(gameCamera, paused, dev_zoom);

        }
        return false;


    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        if (gameViewport != null) {
            gameViewport.update(width, height, false);

            if (gameCamera != null) {
                gameCamera.zoom = 2f;
                gameCamera.update();
            }
        }
    }

    @Override
    public void dispose() {
        if (bob != null) bob.dispose();
        if (evilBob != null) evilBob.dispose();
        if (maze != null) maze.dispose();

        for (CampusSecurity security : allCampusSecuritySprites) {
            if (security != null) security.dispose();
        }
    }

    private void eventTriggered(String eventName) {
        eventTracker.merge(eventName, 1, Integer::sum);
    }

    private void handleInteraction() {
        // Create campus security if triggered
        if (evilBobReturnData.containsKey("Create Campus Security")) {
            if (evilBobReturnData.get("Create Campus Security") && !campusSecurityCreated) {
                eventTriggered("Hidden");
                for (int i = 0; i < allCampusSecuritySprites.length; i++) {
                    allCampusSecuritySprites[i] = createSprite(
                        "atlas/security_geese.atlas",
                        "walking",
                        500, 500,
                        2 * BOB_WIDTH,
                        2 * BOB_HEIGHT,
                        10,
                        CampusSecurity::new
                    );
                }
                campusSecurityCreated = true;
            }
        }

        // Enable rocket Bob
        if (evilBobReturnData.containsKey("Enable Rocket Bob")) {
            if (evilBobReturnData.get("Enable Rocket Bob")) {
                bob.setAnimation("Rocket");
                bob.setSpeed(150);
            }
        }

        // Remove keycard
        if (evilBobReturnData.containsKey("Remove Keycard")) {
            if (evilBobReturnData.get("Remove Keycard")) {
                if (bob.removeInventory("Keycard")) {
                    eventTriggered("Hidden");
                }
            }
        }

        // Check campus security collisions
        if (campusSecurityCreated) {
            for (CampusSecurity sec : allCampusSecuritySprites) {
                campusSecurityReturnData = sec.collision(bob);
                if (campusSecurityReturnData.containsKey("Reset Player Position")) {
                    if (campusSecurityReturnData.get("Reset Player Position")) {
                        bobSprite.setPosition(100, 500);
                    }
                }
            }
        }
    }

    public void resetGame() {
        timer = 0f;
        events = 0;
        paused = false;
        campusSecurityCreated = false;


        evilBobReturnData.clear();
        campusSecurityReturnData.clear();

        if (bobSprite != null) {
            bobSprite.setPosition(100, 500);
        }

        createKeycard();

        eventTracker = GameController.setEventMap();

        initialiseGame();
        intialised = true;

    }


    private <T> T createSprite(String atlas, String regionName,
                               Integer xPos, Integer yPos,
                               Integer xSize, Integer ySize,
                               Integer speed,
                               java.util.function.BiFunction<Sprite, Integer, T> constructorType) {
        TextureAtlas tempAtlas = new TextureAtlas(atlas);
        Sprite tempSprite = new Sprite(tempAtlas.findRegion(regionName));
        tempSprite.setPosition(xPos, yPos);
        tempSprite.setSize(xSize, ySize);
        return constructorType.apply(tempSprite, speed);
    }

    public void setIntialised(boolean intialised) {
        this.intialised = intialised;
    }

}
