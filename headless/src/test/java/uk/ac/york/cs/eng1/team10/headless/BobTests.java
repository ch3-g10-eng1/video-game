package uk.ac.york.cs.eng1.team10.headless;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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
  private Graphics mockGraphics;
  private Input originalInput;
  private Graphics originalGraphics;
  private TextureAtlas atlas;

  @BeforeEach
  public void createBob() {
    originalInput = Gdx.input;
    originalGraphics = Gdx.graphics;

    atlas = new TextureAtlas("atlas/bob.atlas");
    bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(100, 500);
    bobSprite.setSize(15, 15);
    bob = new Bob(bobSprite, 60, -3);
    mockInput = mock(Input.class);
    mockGraphics = mock(Graphics.class);
    Gdx.input = mockInput;
    Gdx.graphics = mockGraphics;
  }

  @AfterEach
  public void tearDown() {
    Gdx.input = originalInput;
    Gdx.graphics = originalGraphics;

    if (atlas != null) {
        atlas.dispose();
    }
}

  // Initialization
  @Test
  public void testBobCreationSetsCorrectValues() {
    assertEquals(bobSprite.getHeight(), 15);
    assertEquals(bobSprite.getWidth(), 15);
    assertEquals(bob.getX(), 100);
    assertEquals(bob.getY(), 500);
    assertEquals(bob.getSpeed(), 60);
    assertEquals(bob.getVerticalSpeedMultiplier(), 1.2f);
    assertDoesNotThrow(() -> bob.getCurrentRegion());
  }

  // Inventory
  @Test
  public void testAddAndRemoveInventoryCorrectlyChangeInventory() {
    assertEquals(true,bob.addInventory("Key"));
    assertEquals(false, bob.addInventory("Key")); // duplicate
    assertEquals(true, bob.getInventory().contains("Key"));
    assertEquals(true, bob.removeInventory("Key"));
    assertEquals(false, bob.removeInventory("Key")); // already removed
  }

  @Test
  public void testGetInventoryReturnsCorrectSet() {
    bob.addInventory("Gem");
    bob.addInventory("Coin");

    Set<String> inv = bob.getInventory();

    assertEquals(2, inv.size());
    assertEquals(true, inv.contains("Gem"));
    assertEquals(true, inv.contains("Coin"));
  }

  // Movement

  @Test
  public void testSetSpeedCorrectlyChangesSpeed() {
    int speed = 100;

    bob.setSpeed(speed);

    assertEquals(speed, bob.getSpeed());
  }

  @Test
  public void testMoveLeftChangesBobPositionLeft() {
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    // Inject delta time into move()
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    // Simulate LEFT key pressed
    when(mockInput.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX - bob.getSpeed() * deltaTime, bobSprite.getX());
    assertEquals(initialY, bobSprite.getY());
  }

  @Test
  public void testMoveRightChangesBobPositionRight() {
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX + bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
    assertEquals(initialY, bobSprite.getY());
  }

  @Test
  public void testMoveUpChangesBobPositionUp() {
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.UP)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX, bobSprite.getX());
    assertEquals(initialY + bob.getSpeed() * deltaTime * bob.getVerticalSpeedMultiplier(), bobSprite.getY(), deltaTime);
  }

  @Test
  public void testMoveDownChangesBobPositionDown() {
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX, bobSprite.getX());
    assertEquals(initialY - bob.getSpeed() * deltaTime * bob.getVerticalSpeedMultiplier(), bobSprite.getY(), deltaTime);
  }

  @Test
  public void testConfusedMoveLeftChangesBobPositionRight() {
    bob.setConfused(true);
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX + bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
    assertEquals(initialY, bobSprite.getY());
  }

  @Test
  public void testConfusedMoveRightChangesBobPositionLeft() {
    bob.setConfused(true);
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX - bob.getSpeed() * deltaTime, bobSprite.getX(), deltaTime);
    assertEquals(initialY, bobSprite.getY());
  }

  @Test
  public void testConfusedMoveUpChangesBobPositionDown() {
    bob.setConfused(true);
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.UP)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX, bobSprite.getX());
    assertEquals(initialY - bob.getSpeed() * deltaTime * bob.getVerticalSpeedMultiplier(), bobSprite.getY(), deltaTime);
  }

  @Test
  public void testConfusedMoveDownChangesBobPositionUp() {
    bob.setConfused(true);
    boolean[] movementHalter = {false, false, false, false};
    float initialX = bobSprite.getX();
    float initialY = bobSprite.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);
    when(mockInput.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);

    bob.move(movementHalter);

    assertEquals(initialX, bobSprite.getX());
    assertEquals(initialY + bob.getSpeed() * deltaTime * bob.getVerticalSpeedMultiplier(), bobSprite.getY(), deltaTime);
  }

  @Test
  public void testSetSuspensionDoesNotThrowWhenBobMoves() {
    bob.setSuspension(true);
    boolean[] movementHalter = {false, false, false, false};

    assertDoesNotThrow(() -> bob.move(movementHalter));
  }

  @Test
  public void testSetConfusedDoesNotThrow() {
    bob.setConfused(true);

    assertDoesNotThrow(() -> bob.setConfused(false));
  }

  @Test
  public void testCollisionBoxIsInCorrectLocation() {
    Rectangle box = bob.getCollisionBox();
    float spriteX = bobSprite.getX();
    float spriteY = bobSprite.getY();
    float spriteW = bobSprite.getWidth();
    float spriteH = bobSprite.getHeight();
    float offset = 3;

    assertEquals(spriteX + offset, box.getX());
    assertEquals(spriteY + offset, box.getY());
    assertEquals(spriteW - offset * 2, box.getWidth());
    assertEquals(spriteH - offset * 2, box.getHeight());
  }

  // Texture tests

  @Test
  public void testSetAnimationDoesNotCauseMoveToThrow() {
    bob.setAnimation("Squash");
    boolean[] movementHalter = {false, false, false, false};
    // Ensure game does not crash when moving (right as example)
    when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

    assertDoesNotThrow(() -> bob.move(movementHalter));
  }

  @Test
  public void testRightMoveAnimationSetCorrectlyWhenBobMovesRight() {
    boolean[] movementHalter = {false, false, false, false};
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
  public void testLeftMoveAnimationSetCorrectlyWhenBobMovesLeft() {
    boolean[] movementHalter = {false, false, false, false};
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
  public void testUpMoveAnimationSetCorrectlyWhenBobMovesUp() {
    boolean[] movementHalter = {false, false, false, false};
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
  public void testDownMoveAnimationSetCorrectlyWhenBobMovesDown() {
    boolean[] movementHalter = {false, false, false, false};
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



