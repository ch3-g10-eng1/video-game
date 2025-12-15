package uk.ac.york.cs.eng1.team10.headless;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.team3._8.game.Bob;
import com.team3._8.game.EvilBob;


public class EvilBobTests extends AbstractHeadlessGdxTest{
  private EvilBob evilBob;
  private Sprite evilBobSprite;
  private Input mockInput;

  @BeforeEach
  public void createEvilBob() {
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    evilBobSprite = new Sprite(atlas.findRegion("front-bob"));
    evilBobSprite.setPosition(100, 100);
    evilBobSprite.setSize(15, 15);
    evilBob = new EvilBob(evilBobSprite, 60);
    mockInput = mock(Input.class);
    Gdx.input = mockInput;
  }

  // Initialization
  @Test
  public void testEvilBobCreation() {
    assertEquals(15, evilBobSprite.getHeight());
    assertEquals(15, evilBobSprite.getWidth());
    assertEquals(100, evilBob.getX());
    assertEquals(100, evilBob.getY());
    assertEquals(60, evilBob.getSpeed());
  }

  @Test
  public void testTextBoxCreation() {
    assertEquals(170,evilBob.getTextBubble().getWidth());
    assertEquals(120,evilBob.getTextBubble().getHeight());
    assertEquals("Interact: E", evilBob.getTextBubble().getText());
    assertEquals(false, evilBob.getTextBubble().isVisible());
  }

  // Interaction

  @Test
  public void testEvilBobInteractionNoKeycard() {
    when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
    evilBob.startInteraction();
    assertEquals(true, evilBob.getTextBubble().isVisible());
    assertEquals("Go Away!\nYou're missing\nmy keycard", evilBob.getTextBubble().getText());
    evilBob.stopInteraction();
    assertEquals("Interact: E", evilBob.getTextBubble().getText());
    assertEquals(false, evilBob.getTextBubble().isVisible());
  }

  @Test
  public void testEvilBobInteractionWithKeycard() {
    evilBob.setPlayerHasKeycard(true);
    when(mockInput.isKeyJustPressed(Input.Keys.E)).thenReturn(true);
    assertEquals(true, evilBob.getPlayerHasKeycard());
    evilBob.startInteraction();
    assertEquals(true, evilBob.getTextBubble().isVisible());
    assertEquals("You dare\nenter my realm", evilBob.getTextBubble().getText());
  }

  @Test
  public void testCollision() {
    // Create a Bob instance to collide with EvilBob
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    Sprite bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(87, 100);
    bobSprite.setSize(15, 15);
    Bob bob = new Bob(bobSprite, 60, 0);
    when(mockInput.isKeyJustPressed(Input.Keys.N)).thenReturn(true);

    // Used to force to not be returned blank
    evilBob.setConversationReset(true);
    evilBob.setSkipChoice(false);

    // Collision should occur, and return command to create campus security
    Map<String, Boolean> returnData = evilBob.collision(bob);
    assertEquals(true, returnData.containsKey("Create Campus Security"));
    assertEquals(true, returnData.get("Create Campus Security"));

    // Creates a new bob at a different position to avoid collision
    bobSprite.setPosition(0, 0);
    Bob newBob = new Bob(bobSprite, 60, 0);
    // Resets ReturnData to avoid use of previous collision data
    evilBob.resetReturnData();
    assertEquals(false, evilBob.collision(newBob).containsKey("Create Campus Security"));
  }

}
