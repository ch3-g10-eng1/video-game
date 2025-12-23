package uk.ac.york.cs.eng1.team10.headless;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.team3._8.game.PuzzleEvent;
import com.team3._8.game.TextBubble;

public class PuzzleEventTests extends AbstractHeadlessGdxTest {

  PuzzleEvent puzzleEvent;
  Sprite mockSprite;
  BitmapFont mockFont;
  SpriteBatch mockBatch;
  Input mockInput;
  TextBubble mockTextBubble;
  Input originalInput;

  @BeforeEach
  public void createPuzzleEvent() {
    originalInput = Gdx.input;
    mockSprite = mock(Sprite.class);
    mockFont = mock(BitmapFont.class);
    mockBatch = mock(SpriteBatch.class);
    mockInput = mock(Input.class);
    mockTextBubble = mock(TextBubble.class);
    Gdx.input = mockInput;

    puzzleEvent = new PuzzleEvent(mockSprite, 0f, mockTextBubble, mockFont);
  }

  @AfterEach
  public void tearDown() {
    Gdx.input = originalInput;
  }

  @Test
  void testStartInteractionReturnsSuspendInitially() {
    Map<String, Boolean> result = puzzleEvent.startInteraction();
    assertEquals(true, result.containsKey("Suspend"));
    assertEquals(true, result.get("Suspend"));
  }

  @Test
  void testStartInteractionYKeyTriggersPuzzleDisplay() {
    // Simulate conversation finished
    puzzleEvent.setConversationReset(true);
    puzzleEvent.setDrawNext(false);
    when(mockInput.isKeyJustPressed(Input.Keys.Y)).thenReturn(true);

    puzzleEvent.startInteraction();

    verify(mockTextBubble).setText(contains(puzzleEvent.getEquationString()));
  }

  @Test
  void testStartInteractionNKeyMarksSolved() {
    puzzleEvent.setTextBubbleVisible(true);
    puzzleEvent.setConversationReset(true);
    when(mockInput.isKeyJustPressed(Input.Keys.N)).thenReturn(true);

    Map<String, Boolean> result = puzzleEvent.startInteraction();

    assertEquals(true, puzzleEvent.isSolved());
    assertEquals(false, result.get("Suspend"));
    verify(mockTextBubble).hideShow();
  }

  @Test
  void testStartInteractionWithCorrectAnswerEnablesRocketBob() {
    puzzleEvent.setConversationReset(true);
    // Find the index of the correct answer
    int correctIndex = -1;
    for (int i = 0; i < puzzleEvent.getOptions().length; i++) {
        if (puzzleEvent.getOptions()[i] == puzzleEvent.getAnswer()) correctIndex = i;
    }
    // Simulate pressing the correct number key
    switch (correctIndex) {
      case 0 -> when(mockInput.isKeyJustPressed(Input.Keys.NUM_1)).thenReturn(true);
      case 1 -> when(mockInput.isKeyJustPressed(Input.Keys.NUM_2)).thenReturn(true);
      case 2 -> when(mockInput.isKeyJustPressed(Input.Keys.NUM_3)).thenReturn(true);
      case 3 -> when(mockInput.isKeyJustPressed(Input.Keys.NUM_4)).thenReturn(true);
    }

    Map<String, Boolean> result = puzzleEvent.startInteraction();

    assertEquals(true, result.containsKey("Enable Rocket Bob"));
    assertEquals(true, result.get("Enable Rocket Bob"));
    assertEquals(true, puzzleEvent.isSolved());
  }

  @Test
  void testStartInteractionWithIncorrectAnswerAddsTimePenalty() {
    puzzleEvent.setConversationReset(true);

    // Pick a number that is NOT the answer
    int wrongIndex = 0;
    if (puzzleEvent.getOptions()[0] == puzzleEvent.getAnswer()) wrongIndex = 1;
    switch (wrongIndex) {
      case 0 -> when(mockInput.isKeyJustPressed(Input.Keys.NUM_1)).thenReturn(true);
      case 1 -> when(mockInput.isKeyJustPressed(Input.Keys.NUM_2)).thenReturn(true);
    }

    Map<String, Boolean> result = puzzleEvent.startInteraction();

    assertEquals(true, result.containsKey("Time penalty"));
    assertEquals(true, result.get("Time penalty"));
    assertEquals(true, puzzleEvent.isSolved());
    }

  @Test
  void stopInteraction_resetsConversationIfNotSolved() {
    puzzleEvent.setConversationReset(false);
    puzzleEvent.setTextBubbleVisible(true);

    puzzleEvent.stopInteraction();

    verify(mockTextBubble).setText(puzzleEvent.getScript()[0]);
    verify(mockTextBubble).hideShow();
    assertTrue(puzzleEvent.getDrawNext());
  }

  @Test
    void testDrawUpdatesSpriteAndTextBubble() {
        puzzleEvent.setX(0);
        puzzleEvent.setY(0);
        puzzleEvent.setDrawNext(true);

        puzzleEvent.draw(mockBatch, 10, 20);

        verify(mockSprite).setPosition(10, 20);
        verify(mockSprite).setColor(0f, 0f, 0f, 0f);
        verify(mockTextBubble).draw(mockBatch, 330, 605);
        verify(mockFont).setColor(Color.WHITE);
    }

}
