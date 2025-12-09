package uk.ac.york.cs.eng1.team10.headless;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.team3._8.game.Bob;

public class BobTests extends AbstractHeadlessGdxTest {
    private Bob bob;
    private Sprite bobSprite;
    private Input mockInput;

    @BeforeEach
    public void createBob() {
        TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(100, 500);
        bobSprite.setSize(15, 15);
        bob = new Bob(bobSprite, 60, -3);
        mockInput = mock(Input.class);
        Gdx.input = mockInput;
    }

    // Inventory
    @Test
    public void testAddAndRemoveInventory() {
        assertTrue(bob.addInventory("Key"));
        assertFalse(bob.addInventory("Key")); // duplicate
        assertTrue(bob.getInventory().contains("Key"));

        assertTrue(bob.removeInventory("Key"));
        assertFalse(bob.removeInventory("Key")); // already removed
    }

    @Test
    public void testGetInventoryReturnsCorrectSet() {
        bob.addInventory("Gem");
        bob.addInventory("Coin");

        Set<String> inv = bob.getInventory();
        assertEquals(2, inv.size());
        assertTrue(inv.contains("Gem"));
        assertTrue(inv.contains("Coin"));
    }

    // Movement

    @Test
    public void testSpeed() {
        int speed = 100;
        bob.setSpeed(speed);
        assertEquals(speed, bob.getSpeed());
    }

    @Test
    public void testMoveLeft() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate LEFT key pressed
        when(mockInput.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX - bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
        assertEquals(initialY, bobSprite.getY());
    }

    @Test
    public void testMoveRight() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate RIGHT key pressed
        when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX + bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
        assertEquals(initialY, bobSprite.getY());
    }

    @Test
    public void testMoveUp() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate UP key pressed
        when(mockInput.isKeyPressed(Input.Keys.UP)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX, bobSprite.getX());
        assertEquals(initialY + bob.getSpeed() * deltaTime, bobSprite.getY(), deltaTime);
    }

    @Test
    public void testMoveDown() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate DOWN key pressed
        when(mockInput.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX, bobSprite.getX());
        assertEquals(initialY - bob.getSpeed() * deltaTime, bobSprite.getY(), deltaTime);
    }

    @Test
    public void testConfusedMoveLeft() {
        bob.setSuspension(false);
        bob.setConfused(true);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate LEFT key pressed
        when(mockInput.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX + bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
        assertEquals(initialY, bobSprite.getY());
    }

    @Test
    public void testConfusedMoveRight() {
        bob.setSuspension(false);
        bob.setConfused(true);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate RIGHT key pressed
        when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX - bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
        assertEquals(initialY, bobSprite.getY());
    }

    @Test
    public void testConfusedMoveUp() {
        bob.setSuspension(false);
        bob.setConfused(true);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate UP key pressed
        when(mockInput.isKeyPressed(Input.Keys.UP)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX, bobSprite.getX());
        assertEquals(initialY - bob.getSpeed() * deltaTime, bobSprite.getY(), deltaTime);
    }

    @Test
    public void testConfusedMoveDown() {
        bob.setSuspension(false);
        bob.setConfused(true);

        boolean[] movementHalter = {false, false, false, false};
        float initialX = bobSprite.getX();
        float initialY = bobSprite.getY();
        float deltaTime = 0.016f;

        // Simulate DOWN key pressed
        when(mockInput.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);

        bob.move(movementHalter);

        assertEquals(initialX, bobSprite.getX());
        assertEquals(initialY + bob.getSpeed() * deltaTime, bobSprite.getY(), deltaTime);
    }

    @Test
    public void testSetSuspension() {
    bob.setSuspension(true);
    boolean[] movementHalter = {false, false, false, false};
    assertDoesNotThrow(() -> bob.move(movementHalter));
    }

    @Test
    public void testSetConfused() {
        bob.setConfused(true);
        assertDoesNotThrow(() -> bob.setConfused(false));
    }

    @Test
    public void testCollisionBox() {
        Rectangle box = bob.getCollisionBox();

        float spriteX = bobSprite.getX();
        float spriteY = bobSprite.getY();
        float spriteW = bobSprite.getWidth();
        float spriteH = bobSprite.getHeight();

        float offset = 3;

        assertEquals(spriteX + offset, box.getX(), 0.001);
        assertEquals(spriteY + offset, box.getY(), 0.001);
        assertEquals(spriteW - offset * 2, box.getWidth(), 0.001);
        assertEquals(spriteH - offset * 2, box.getHeight(), 0.001);
    }

    // Texture tests

    @Test
    public void testSetAnimationOverride() {
        bob.setAnimation("Squash");
        boolean[] movementHalter = {false, false, false, false};
        // Ensure game does not crash when moving (right as example)
        when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        assertDoesNotThrow(() -> bob.move(movementHalter));
    }

    @Test
    public void testRightMoveAnimation() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};

        // Simulate RIGHT key pressed
        when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

        bob.move(movementHalter);
        TextureRegion expected = bob.getAnimation("Right");
        TextureRegion actual = bob.getCurrentRegion();

        assertEquals(expected.getTexture(), actual.getTexture());
        assertEquals(expected.getRegionX(), actual.getRegionX());
        assertEquals(expected.getRegionY(), actual.getRegionY());
        assertEquals(expected.getRegionWidth(), actual.getRegionWidth());
        assertEquals(expected.getRegionHeight(), actual.getRegionHeight());
    }

    @Test
    public void testLeftMoveAnimation() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};

        // Simulate LEFT key pressed
        when(mockInput.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

        bob.move(movementHalter);
        TextureRegion expected = bob.getAnimation("Left");
        TextureRegion actual = bob.getCurrentRegion();

        assertEquals(expected.getTexture(), actual.getTexture());
        assertEquals(expected.getRegionX(), actual.getRegionX());
        assertEquals(expected.getRegionY(), actual.getRegionY());
        assertEquals(expected.getRegionWidth(), actual.getRegionWidth());
        assertEquals(expected.getRegionHeight(), actual.getRegionHeight());
    }

    @Test
    public void testUpMoveAnimation() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};

        // Simulate UP key pressed
        when(mockInput.isKeyPressed(Input.Keys.UP)).thenReturn(true);

        bob.move(movementHalter);
        TextureRegion expected = bob.getAnimation("Up");
        TextureRegion actual = bob.getCurrentRegion();

        assertEquals(expected.getTexture(), actual.getTexture());
        assertEquals(expected.getRegionX(), actual.getRegionX());
        assertEquals(expected.getRegionY(), actual.getRegionY());
        assertEquals(expected.getRegionWidth(), actual.getRegionWidth());
        assertEquals(expected.getRegionHeight(), actual.getRegionHeight());
    }

    @Test
    public void testDownMoveAnimation() {
        bob.setSuspension(false);
        bob.setConfused(false);

        boolean[] movementHalter = {false, false, false, false};

        // Simulate DOWN key pressed
        when(mockInput.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);

        bob.move(movementHalter);
        TextureRegion expected = bob.getAnimation("Front");
        TextureRegion actual = bob.getCurrentRegion();

        assertEquals(expected.getTexture(), actual.getTexture());
        assertEquals(expected.getRegionX(), actual.getRegionX());
        assertEquals(expected.getRegionY(), actual.getRegionY());
        assertEquals(expected.getRegionWidth(), actual.getRegionWidth());
        assertEquals(expected.getRegionHeight(), actual.getRegionHeight());
    }
}



