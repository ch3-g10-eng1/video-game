package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import com.team3._8.game.menu.impl.PauseMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class PauseMenuTests {

  private MenuManager mockMenuManager;
  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private OrthographicCamera mockCamera;
  private Viewport mockViewport;
  private Input mockInput;
  private Graphics mockGraphics;
  private Application mockApplication;

  private PauseMenu pauseMenu;

  @BeforeEach
  public void createPauseMenu() {
    mockApplication = mock(Application.class);
    mockInput = mock(Input.class);
    mockGraphics = mock(Graphics.class);
    Gdx.app = mockApplication;
    Gdx.input = mockInput;
    Gdx.graphics = mockGraphics;

    mockMenuManager = mock(MenuManager.class);
    mockBatch = mock(SpriteBatch.class);
    mockFont = mock(BitmapFont.class);
    mockCamera = mock(OrthographicCamera.class);
    mockViewport = mock(Viewport.class);

    when(mockGraphics.getWidth()).thenReturn(800);
    when(mockGraphics.getHeight()).thenReturn(600);
    when(mockViewport.getWorldWidth()).thenReturn(800f);
    when(mockViewport.getWorldHeight()).thenReturn(600f);

    pauseMenu = new PauseMenu(
            mockMenuManager,
            mockBatch,
            mockFont,
            mockCamera,
            mockViewport,
            mockFont,
            mockFont,
            mock(TextureRegion.class),
            mock(TextureRegion.class),
            mock(TextureRegion.class)
    );
  }

  @AfterEach
  public void tearDown() {
    Gdx.app = null;
    Gdx.input = null;
    Gdx.graphics = null;
  }

  @Test
  void testHandleInputClickResumeButtonSetsGameMenu() {
    MenuButton button = pauseMenu.getButtons().get(0);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = pauseMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.GAME);
  }

  @Test
  void testHandleInputClickRestartButtonSetsMainMenu() {
    MenuButton button = pauseMenu.getButtons().get(1);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = pauseMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.MAIN_MENU);
  }

  @Test
  void testHandleInputClickQuitButtonRunsExit() {
    MenuButton button = pauseMenu.getButtons().get(2);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = pauseMenu.handleInput();

    assertEquals(true, result);
    verify(mockApplication).exit();
  }

  @Test
  void testHandleInputPressEscapeKeySetsGameMenu() {
    when(mockInput.isKeyJustPressed(Input.Keys.ESCAPE)).thenReturn(true);

    boolean result = pauseMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.GAME);
  }

}
