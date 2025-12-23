package uk.ac.york.cs.eng1.team10.headless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.team3._8.game.TextBubble;

public class TextBubbleTests extends AbstractHeadlessGdxTest {

  private Texture mockTexture;
  private BitmapFont mockFont;
  private SpriteBatch mockBatch;

  private TextBubble textBubble;


  @BeforeEach
  public void createTextBubble() {
    mockTexture = mock(Texture.class);
    mockFont = mock(BitmapFont.class);
    mockBatch = mock(SpriteBatch.class);

    textBubble = new TextBubble(mockTexture, mockFont, 170f, 120f);
  }

  @Test
  void testHideShowTogglesVisibilityOnAndOff() {
    assertEquals(false, textBubble.isVisible());

    boolean visible = textBubble.hideShow();

    assertEquals(true, visible);
    assertEquals(true, textBubble.isVisible());

    visible = textBubble.hideShow();

    assertEquals(false, visible);
    assertEquals(false, textBubble.isVisible());
  }

  @Test
  void testSetTextUpdatesTextUsedForDrawing() {
    textBubble.setText("Hello world");
    // Make visible so draw() actually runs
    textBubble.hideShow();

    textBubble.draw(mockBatch, 100, 100);

    verify(mockFont).draw(eq(mockBatch), eq("Hello world"), anyFloat(), anyFloat());
  }

  @Test
  void testDrawDoesNothingWhenNotVisible() {
    textBubble.setText("Should not draw");

    textBubble.draw(mockBatch, 100, 100);

    verifyNoInteractions(mockFont);
    verifyNoInteractions(mockBatch);
  }

  @Test
  void testDrawCorrectlySetsFontColorBlackThenWhiteWhenVisible() {
    textBubble.setText("Color test");
    textBubble.hideShow();

    textBubble.draw(mockBatch, 100, 100);

    verify(mockFont).setColor(Color.BLACK);
    verify(mockFont).setColor(Color.WHITE);
  }

  @Test
  void testDrawDrawsTextExactlyOnceWhenVisible() {
    textBubble.setText("Exactly once");
    textBubble.hideShow();

    textBubble.draw(mockBatch, 100, 200);

    verify(mockFont, times(1)).draw(eq(mockBatch), eq("Exactly once"), anyFloat(), anyFloat());
  }
}
