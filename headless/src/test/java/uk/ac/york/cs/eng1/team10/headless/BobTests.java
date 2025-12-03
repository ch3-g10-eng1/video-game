package uk.ac.york.cs.eng1.team10.headless;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.team3._8.game.Bob;

public class BobTests extends AbstractHeadlessGdxTest {
    private Bob bob;
    private Sprite bobSprite;

    @BeforeEach
    public void createBob() {
        TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
        bobSprite = new Sprite(atlas.findRegion("front-bob"));
        bobSprite.setPosition(100, 500);
        bobSprite.setSize(15, 15);
        bob = new Bob(bobSprite, 60, -3);
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
    public void testSetSuspension() {
    bob.setSuspension(true);
    // This wont throw an exception when calling move()
    boolean[] movementHalter = {false, false, false, false};
    assertDoesNotThrow(() -> bob.move(movementHalter));
    }

    @Test
    public void testSetConfused() {
        bob.setConfused(true);
        assertDoesNotThrow(() -> bob.setConfused(false));
    }

    @Test
    public void testSetAnimationOverride() {
        bob.setAnimation("Squash");
        // Just make sure it doesn't crash
        boolean[] movementHalter = {false, false, false, false};
        assertDoesNotThrow(() -> bob.move(movementHalter));
    }
}


