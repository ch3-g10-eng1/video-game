package uk.ac.york.cs.eng1.team10.headless;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.team3._8.game.GameController;

public class GameControllerTests extends AbstractHeadlessGdxTest {

  Input mockInput;
  Input originalInput;

  @BeforeEach
  public void createGameController() {
    originalInput = Gdx.input;
    mockInput = mock(Input.class);
    Gdx.input = mockInput;
  }

  @AfterEach
  public void tearDown() {
    Gdx.input = originalInput;
  }

  @Test
  void testFormatTimeFormatsMinutesAndSecondsCorrectly() {
      String result = GameController.formatTime(125.9f);
      assertEquals("Timer: 2:05", result);
  }

  @Test
    void testFormatTimeHandlesZeroTime() {
        assertEquals("Timer: 0:00", GameController.formatTime(0));
    }

  @Test
  void testHandleInputTogglesPauseWhenEscapePressed() {
      OrthographicCamera camera = new OrthographicCamera();

      when(mockInput.isKeyJustPressed(Input.Keys.ESCAPE)).thenReturn(true);

      boolean paused = GameController.handleInput(camera, false, false);

      assertEquals(true, paused);
  }

  @Test
  void testHandleInputZoomIncreasesWhenMinusPressedAndZoomEnabled() {
      OrthographicCamera camera = new OrthographicCamera();
      camera.zoom = 1.0f;

      when(mockInput.isKeyPressed(Input.Keys.MINUS)).thenReturn(true);

      GameController.handleInput(camera, false, true);

      assertEquals(1.02f, camera.zoom);
  }

  @Test
  void testHandleInputZoomDecreasesWhenEqualsPressedAndZoomEnabled() {
      OrthographicCamera camera = new OrthographicCamera();
      camera.zoom = 1.0f;

      when(mockInput.isKeyPressed(Input.Keys.EQUALS)).thenReturn(true);

      GameController.handleInput(camera, false, true);

      assertEquals(0.98f, camera.zoom);
  }

  @Test
  void testHandleInputDoesNotZoomWhenMinusPressedAndZoomDisabled() {
      OrthographicCamera camera = new OrthographicCamera();
      camera.zoom = 1.0f;
      when(mockInput.isKeyPressed(Input.Keys.MINUS)).thenReturn(true);

      GameController.handleInput(camera, false, false);

      assertEquals(1.00f, camera.zoom);
  }

  @Test
  void testHandleInputDoesNotZoomWhenEqualsPressedAndZoomDisabled() {
      OrthographicCamera camera = new OrthographicCamera();
      camera.zoom = 1.0f;
      when(mockInput.isKeyPressed(Input.Keys.EQUALS)).thenReturn(true);

      GameController.handleInput(camera, false, false);

      assertEquals(1.00f, camera.zoom);
  }

  @Test
  void testSetEventMapCreatesExpectedMap() {
      Map<String, Integer> eventMap = GameController.setEventMap();

      assertEquals(3, eventMap.size());
      eventMap.values().forEach(value -> assertEquals(0, value));
  }

  @Test
  void testSetAchievementMapCreatesExpectedMap() {
      Map<String, Boolean> achievements = GameController.setAchievementMap();

      assertEquals(6, achievements.size());
      achievements.values().forEach(value -> assertEquals(false, value));
  }

}


