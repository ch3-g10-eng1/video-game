package uk.ac.york.cs.eng1.team10.headless;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.button.MenuButton;
import com.team3._8.game.menu.impl.WinMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class WinMenuTests {

  private WinMenu winMenu;
  private OrthographicCamera mockCamera;

  private MenuManager mockMenuManager;
  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private Viewport mockViewport;
  private Input mockInput;
  private Graphics mockGraphics;
  private Application mockApplication;

  @BeforeEach
  public void createWinMenu() {
    mockCamera = mock(OrthographicCamera.class);

    mockApplication = mock(Application.class);
    mockMenuManager = mock(MenuManager.class);
    mockBatch = mock(SpriteBatch.class);
    mockFont = mock(BitmapFont.class);
    mockViewport = mock(Viewport.class);
    mockInput = mock(Input.class);
    mockGraphics = mock(Graphics.class);

    when(mockGraphics.getWidth()).thenReturn(800);
    when(mockGraphics.getHeight()).thenReturn(600);
    when(mockViewport.getWorldWidth()).thenReturn(800f);
    when(mockViewport.getWorldHeight()).thenReturn(600f);

    Gdx.app = mockApplication;
    Gdx.input = mockInput;
    Gdx.graphics = mockGraphics;

    winMenu = new WinMenu(mockMenuManager, mockBatch, mockFont, mockCamera, mockViewport,
      mockFont,
      mockFont,
      mockFont,
      mock(TextureRegion.class),
      mock(TextureRegion.class),
      mock(TextureRegion.class));
  }

  @AfterEach
  public void tearDown() {
    Gdx.app = null;
    Gdx.input = null;
    Gdx.graphics = null;
  }

  @Test
  void testHandleInputReturnsFalseWhenNoTouchOccurs() {
    when(Gdx.input.justTouched()).thenReturn(false);

    boolean result = winMenu.handleInput();

    assertEquals(false, result);
    verifyNoInteractions(mockMenuManager);
  }

  @Test
  void testHandleInputReturnsFalseWhenTouchIsOutsideAllButtons() {
    when(Gdx.input.justTouched()).thenReturn(true);
    when(Gdx.input.getX()).thenReturn(10);
    when(Gdx.input.getY()).thenReturn(10);

    boolean result = winMenu.handleInput();

    assertEquals(false, result);
    verifyNoInteractions(mockMenuManager);
  }

  @Test
  void testUpdateSetsButtonHoverAndClickCorrectly() {
    MenuButton button = winMenu.getButtons().get(0);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());
    // Hovered but mouse not pressed
    when(Gdx.input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(false);
    winMenu.update(0.016f);

    assertEquals(true, button.hovered);
    assertEquals(false, button.clicked);

    // Mouse pressed
    when(Gdx.input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);
    winMenu.update(0.016f);

    assertEquals(true, button.clicked);

    // Mouse released
    when(Gdx.input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(false);
    winMenu.update(0.016f);

    assertEquals(false, button.clicked);
  }


  @Test
  void testHandleInputClickPlayAgainButtonSetsGameMenu() {
    MenuButton button = winMenu.getButtons().get(0);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());
    when(mockInput.justTouched()).thenReturn(true);

    boolean result = winMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.GAME);
  }

  @Test
  void testHandleInputClickTitleScreenButtonSetsMainMenu() {
    MenuButton button = winMenu.getButtons().get(1);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());
    when(mockInput.justTouched()).thenReturn(true);

    boolean result = winMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.MAIN_MENU);
  }

  @Test
  void testHandleInputClickQuitButtonRunsExit() {
    MenuButton button = winMenu.getButtons().get(2);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());
    when(mockInput.justTouched()).thenReturn(true);

    boolean result = winMenu.handleInput();

    assertEquals(true, result);
    verify(mockApplication).exit();
  }

  @Test
  void testShowDoesNotThrow() {
    assertDoesNotThrow(() -> winMenu.show());
  }

  @Test
  void testSetCompletionTimeDoesNotThrow() {
    assertDoesNotThrow(() -> winMenu.setCompletionTime(42.5f));
  }

  @Test
  void testSetAchievementsDoesNotThrow() {
    Map<String, Boolean> achievements = new HashMap<>();
    achievements.put("Speed Run", true);

    assertDoesNotThrow(() -> winMenu.setAchievements(achievements));
  }

  @Test
  void testShowUpdatesViewportWithCurrentScreenSize() {
    winMenu.show();

    verify(mockViewport).update(800, 600, true);
  }

  @Test
  void testResizeUpdatesViewportWithNewDimensions() {
    winMenu.resize(1024, 768);

    verify(mockViewport).update(1024, 768, true);
  }

  @Test
  void testUpdateSetsHoverAndClickStateWithoutThrowing() {
    when(mockInput.getX()).thenReturn(400);
    when(mockInput.getY()).thenReturn(200);
    when(mockInput.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);

    assertDoesNotThrow(() -> winMenu.update(0.016f));
}

}
