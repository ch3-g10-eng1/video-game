package com.team3._8.game.menu.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
    private PuzzleEvent puzzleEvent;
    private Maze maze;
    private HUD hud;

    private CollectableEntity keycard, speedBoost, speedBoost2, timeOrb, shield, sizePotion, confusedDebuff, hiddenLightsOut, tomato;

    private boolean speedBoostActive = false;
    private float speedBoostTimer = 0f;

    private boolean speedBoost2Active = false;
    private float speedBoost2Timer = 0f;

    private boolean invincibilityActive = false;
    private float invincibilityTimer = 0f;

    private boolean sizeChangeActive = false;
    private float sizeChangeTimer = 0f;

    private boolean confusedActive = false;
    private float confusedTimer = 0f;

    private boolean areLightsOut = false;
    private float lightsOutTimer = 30f;

    private boolean tomatoActive = false;
    private float tomatoTimer = 0f;
    private float tomatoAlpha = 1f;
    private float tomatoFade = 0.1f;

    private int originalSpeed = 60;

    private Sprite lightsOutSprite, tomatoSplatSprite;

    private final CampusSecurity[] allCampusSecuritySprites = new  CampusSecurity[5];
    private boolean campusSecurityCreated;

    private boolean paused = false, intialised = false;
    private float timer = 0f;
    private int events = 0;
    private boolean[] movement_halter;

    private Map<String, Boolean> evilBobReturnData = new HashMap<>();
    private Map<String, Boolean> puzzleEventReturnData = new HashMap<>();
    private Map<String, Boolean> campusSecurityReturnData = new HashMap<>();
    private Map<String, Integer> eventTracker;
    private Map<String, Boolean> achievements;
    private int gooseHits = 0;

    public GameMenu(MenuManager menuManager, SpriteBatch batch, BitmapFont font,
                    OrthographicCamera camera, Viewport viewport) {
        super(menuManager, batch, font, camera, viewport);

        gameCamera = new OrthographicCamera(30, 30 * ((float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight()));
        gameViewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, gameCamera);

        initialiseGame();
        intialised = true;
    }

    private void initialiseGame() {

        timer = 0f;
        events = 0;
        paused = false;
        campusSecurityCreated = false;

        speedBoostActive = false;
        speedBoostTimer = 0f;
        speedBoost2Active = false;
        speedBoost2Timer = 0f;
        invincibilityActive = false;
        invincibilityTimer = 0f;
        sizeChangeActive = false;
        sizeChangeTimer = 0f;
        confusedActive = false;
        confusedTimer = 0f;
        areLightsOut = false;
        lightsOutTimer = 30f;
        tomatoActive = false;
        tomatoTimer = 0f;
        tomatoAlpha = 1f;
        tomatoFade = 0.1f;
        originalSpeed = 60;

        evilBobReturnData.clear();
        puzzleEventReturnData.clear();
        campusSecurityReturnData.clear();

        createLayers();

        createBob();

        createEvilBob();

        createPuzzleEvent();


        createKeycard();
        createLightsOutEvent();
        createSpeedBoost();
        createSpeedBoost2();
        createTomato();
        createTimeOrb();
        createShield();
        createSizePotion();
        createConfusedDebuff();
        createLightsOutOverlay();
        createTomatoSplat();

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
        achievements = GameController.setAchievementMap();
        gooseHits = 0;

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

    private void createLightsOutOverlay(){
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.98f);
        pixmap.fill();
        Texture lightsOutOverlay = new Texture(pixmap);
        pixmap.dispose();

        lightsOutSprite = new Sprite(lightsOutOverlay);
        lightsOutSprite.setSize(WORLD_WIDTH*2, WORLD_WIDTH*2);
    }

    private void createTomatoSplat(){
        Texture texture = new Texture ("tomatoSplat.png");
        tomatoSplatSprite = new Sprite(texture);

        tomatoSplatSprite.setSize(WORLD_WIDTH * 1.5f, WORLD_HEIGHT);
        tomatoAlpha = 1f;
    }

    private void createKeycard() {
        Texture keycardTexture = new Texture("keycard.png");
        Sprite keycardSprite = new Sprite(keycardTexture);
        keycardSprite.setPosition(20, 20);
        keycardSprite.setSize(BOB_WIDTH * 2, BOB_HEIGHT * 2);
        keycard = new CollectableEntity(keycardSprite, 0, "Keycard");
    }

    private void createLightsOutEvent(){
        Texture lightsOutTexture = new Texture("keycard.png");
        Sprite lightsOutEventSprite = new Sprite(lightsOutTexture);
        // use keycard for placeholder, event is invisible anyways
        lightsOutEventSprite.setPosition(1000, 950);
        lightsOutEventSprite.setSize(BOB_WIDTH * 2, BOB_HEIGHT * 2);
        hiddenLightsOut = new CollectableEntity(lightsOutEventSprite, 0, "Lights Out");
        // i actually have no idea why this is invisible, but it is and its meant to be so i wont question it
    }

    private void createSpeedBoost() {
        Texture texture = new Texture("speed_boost.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(176, 656);
        sprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        speedBoost = new CollectableEntity(sprite, 0, "SpeedBoost");
    }

    private void createTomato(){
        Texture texture = new Texture("tomato.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(561, 532);
        sprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        tomato = new CollectableEntity(sprite, 0, "Tomato");
    }

    private void createSpeedBoost2() {
        Texture texture = new Texture("speed_boost2.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(912, 160);
        sprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        speedBoost2 = new CollectableEntity(sprite, 0, "SpeedBoost2");
    }

    private void createTimeOrb() {
        Texture texture = new Texture("time_orb.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(1408, 480);
        sprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        timeOrb = new CollectableEntity(sprite, 0, "TimeOrb");
    }

    private void createShield() {
        Texture texture = new Texture("shield.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(1008, 1008);
        sprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        shield = new CollectableEntity(sprite, 0, "Shield");
    }

    private void createSizePotion() {
        Texture texture = new Texture("size_potion.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(448, 944);
        sprite.setSize(BOB_WIDTH, BOB_HEIGHT);
        sizePotion = new CollectableEntity(sprite, 0, "SizePotion");
    }

    private void createConfusedDebuff(){
        Texture texture = new Texture("confused.png");
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(350, 400);
        sprite.setSize(BOB_WIDTH*1.5f, BOB_HEIGHT*1.5f);
        confusedDebuff = new CollectableEntity(sprite, 0, "ConfusedDebuff");
    }

    private void createPuzzleEvent(){
        puzzleEvent =
            createSprite(
                "atlas/bob.atlas",
                "evil-bob",
                500,
                470,
                2 * BOB_WIDTH,
                2 * BOB_HEIGHT,
                0,
                PuzzleEvent::new);
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

        if (keycard.collected(bob)) {
            eventTriggered("Positive");
            evilBob.setPlayerHasKeycard(true);
            maze.removeCollisionLayer("Doors");
            maze.removeVisibleLayer("ClosedDoors");
        }

        if (hiddenLightsOut.collected(bob)) {
            eventTriggered("Hidden");
            areLightsOut = true;
            lightsOutTimer = 30f; // reset timer when triggered
        }

        if (tomato.collected(bob)) {
            eventTriggered("Negative");
            tomatoActive = true;
            tomatoTimer = 0f;
            tomatoAlpha = 1f;
        }

        if (speedBoost.collected(bob)) {
            eventTriggered("Positive");
            speedBoostActive = true;
            speedBoostTimer = 0f;
            originalSpeed = (int) bob.getSpeed();
            bob.setSpeed(originalSpeed + 60);
        }

        if (speedBoost2.collected(bob)) {
            eventTriggered("Positive");
            speedBoost2Active = true;
            speedBoost2Timer = 0f;
            originalSpeed = (int) bob.getSpeed();
            bob.setSpeed(originalSpeed + 60);
        }

        if (timeOrb.collected(bob)) {
            eventTriggered("Positive");
            timer -= 60f;
        }

        if (shield.collected(bob)) {
            eventTriggered("Positive");
            invincibilityActive = true;
            invincibilityTimer = 0f;
        }

        if (sizePotion.collected(bob)) {
            eventTriggered("Positive");
            sizeChangeActive = true;
            sizeChangeTimer = 0f;
            bobSprite.setScale(0.5f);
        }

        if (confusedDebuff.collected(bob)) {
            eventTriggered("Negative");
            confusedActive = true;
            confusedTimer = 0f;
            bob.setConfused(true);
        }


        if (!paused) {

            if (speedBoostActive) {
                speedBoostTimer += delta;
                if (speedBoostTimer > 10f) {
                    speedBoostActive = false;
                    bob.setSpeed(originalSpeed);
                }
            }

            if (speedBoost2Active) {
                speedBoost2Timer += delta;
                if (speedBoost2Timer > 20f) {
                    speedBoost2Active = false;
                    bob.setSpeed(originalSpeed);
                }
            }

            if (invincibilityActive) {
                invincibilityTimer += delta;
                if (invincibilityTimer > 8f) {
                    invincibilityActive = false;
                }
            }

            if (sizeChangeActive) {
                sizeChangeTimer += delta;
                if (sizeChangeTimer > 12f) {
                    sizeChangeActive = false;
                    bobSprite.setScale(1f);
                }
            }

            if (tomatoActive) {
                tomatoSplatSprite.setColor(1f, 1f, 1f, tomatoAlpha);
                tomatoSplatSprite.setPosition(
                    gameCamera.position.x - tomatoSplatSprite.getWidth() / 2f,
                    gameCamera.position.y - tomatoSplatSprite.getHeight() / 2f
                );
            }

            movement_halter = maze.hitsWall(bob, delta);
            evilBobReturnData.clear();
            evilBobReturnData.putAll(evilBob.collision(bob));

            puzzleEventReturnData.clear();
            puzzleEventReturnData.putAll(puzzleEvent.collision(bob));

            handleInteraction();

            bob.move(movement_halter);

            timer += delta;

            if (areLightsOut) {
                lightsOutTimer -= delta;
                if (lightsOutTimer <= 0f) {
                    areLightsOut = false;
                }
            }


            if (confusedActive) {
                confusedTimer += delta;
                if (confusedTimer >= 20f) {
                    confusedActive = false;
                    bob.setConfused(false);
                }
            }

            if (tomatoActive) {
                tomatoAlpha -= (tomatoFade * delta);
                tomatoTimer += delta;
                if (tomatoTimer >= 15f) {
                    tomatoActive = false;
                    tomatoAlpha = 0f;
                }
            }
        }

        gameCamera.position.set(
            bobSprite.getX() + bobSprite.getWidth() / 2,
            bobSprite.getY() + bobSprite.getHeight() / 2,
            0
        );
        gameCamera.update();

        if (maze.HitsWinLayer(bob)) {
            checkAchievements();
            WinMenu winMenu = (WinMenu) menuManager.getMenu(MenuType.WIN);

            if (winMenu != null) {
                winMenu.setCompletionTime(timer);
                winMenu.setAchievements(achievements);
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(gameCamera.combined);
        maze.renderMap(gameCamera);

        spriteBatch.begin();

        evilBob.draw(spriteBatch, 1000, 1050);
        puzzleEvent.draw(spriteBatch, 345, 600);
        bob.draw(spriteBatch);
        keycard.draw(spriteBatch);
        tomato.draw(spriteBatch);
        speedBoost.draw(spriteBatch);
        speedBoost2.draw(spriteBatch);
        timeOrb.draw(spriteBatch);
        shield.draw(spriteBatch);
        sizePotion.draw(spriteBatch);
        confusedDebuff.draw(spriteBatch);

        if (campusSecurityCreated) {
            int mod = 0;
            for (int i = 0; i < 5; i++) {
                allCampusSecuritySprites[i].draw(spriteBatch, 870 + mod, 1150, maze.hitsWall(allCampusSecuritySprites[i], delta));
                mod += 35;
            }
        }

        if (tomatoActive) {
            tomatoSplatSprite.draw(spriteBatch);
        }

        spriteBatch.end();

        if (areLightsOut) {
            spriteBatch.begin();
            lightsOutSprite.setPosition(
                gameCamera.position.x - WORLD_WIDTH,
                gameCamera.position.y - WORLD_HEIGHT
            );
            lightsOutSprite.draw(spriteBatch);
            spriteBatch.end();
        }

        if (gameViewport.getScreenWidth() > 0 && gameViewport.getScreenHeight() > 0) {
            hud.draw(font, GameController.formatTime(timer), eventTracker, bob, paused, gameViewport, areLightsOut, GameController.formatTime(lightsOutTimer));
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
        if (puzzleEvent != null) puzzleEvent.dispose();
        if (maze != null) maze.dispose();

        for (CampusSecurity security : allCampusSecuritySprites) {
            if (security != null) security.dispose();
        }
    }

    private void eventTriggered(String eventName) {
        eventTracker.merge(eventName, 1, Integer::sum);
    }

    private void checkAchievements() {
        int positiveCount = eventTracker.get("Positive");
        int negativeCount = eventTracker.get("Negative");
        int hiddenCount = eventTracker.get("Hidden");

        if (positiveCount >= 6) {
            achievements.put("Positive Collector", true);
        }

        if (negativeCount >= 2) {
            achievements.put("Negative Collector", true);
        }

        if (positiveCount >= 6 && negativeCount >= 2 && hiddenCount >= 1) {
            achievements.put("All Events", true);
        }

        if (timer < 120) {
            achievements.put("Speed Run", true);
        }

        if (gooseHits >= 2) {
            achievements.put("Goose Chaser", true);
        }

        boolean hasAllEvents = achievements.getOrDefault("All Events", false);
        boolean hasSpeedRun = achievements.getOrDefault("Speed Run", false);
        boolean hasPositive = achievements.getOrDefault("Positive Collector", false);
        boolean hasNegative = achievements.getOrDefault("Negative Collector", false);
        boolean hasGoose = achievements.getOrDefault("Goose Chaser", false);

        if (hasAllEvents && hasSpeedRun && hasPositive && hasNegative && hasGoose) {
            achievements.put("Completionist", true);
        }
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

        if (puzzleEventReturnData.containsKey("Suspend")) {
            bob.setSuspension(puzzleEventReturnData.get("Suspend"));
        }

        if (puzzleEventReturnData.containsKey("Enable Rocket Bob")) {
            if (puzzleEventReturnData.get("Enable Rocket Bob")) {
                bob.setAnimation("Rocket");
                bob.setSpeed(150);
                puzzleEventReturnData.remove("Enable Rocket Bob");
                eventTriggered("Hidden");
            }
        }

        if (puzzleEventReturnData.containsKey("Time penalty")) {
            if (puzzleEventReturnData.get("Time penalty")) {
                timer += 30f;
                puzzleEventReturnData.remove("Time penalty");
                eventTriggered("Hidden");
            }
        }

        // Check campus security collisions
        if (campusSecurityCreated) {
            for (CampusSecurity sec : allCampusSecuritySprites) {
                campusSecurityReturnData.clear();
                campusSecurityReturnData.putAll(sec.collision(bob));
                if (campusSecurityReturnData.containsKey("Reset Player Position")) {
                    if (campusSecurityReturnData.get("Reset Player Position")&& !invincibilityActive) {
                        bobSprite.setPosition(100, 500);
                        gooseHits++;
                    }
                }
            }
        }
    }

    public void resetGame() {
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
