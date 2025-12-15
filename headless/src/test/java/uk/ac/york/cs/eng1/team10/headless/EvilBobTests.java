package uk.ac.york.cs.eng1.team10.headless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

}
