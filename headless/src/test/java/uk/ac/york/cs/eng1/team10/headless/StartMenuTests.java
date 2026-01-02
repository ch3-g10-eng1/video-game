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
import com.team3._8.game.menu.impl.GameMenu;
import com.team3._8.game.menu.impl.StartMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class StartMenuTests {

  private MenuManager mockMenuManager;
  private GameMenu mockGameMenu;
  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private OrthographicCamera mockCamera;
  private Viewport mockViewport;
  private Input mockInput;
  private Graphics mockGraphics;
  private Application mockApplication;

  private StartMenu startMenu;

  @BeforeEach
  public void createStartMenu() {
    mockInput = mock(Input.class);
    mockGraphics = mock(Graphics.class);
    mockApplication = mock(Application.class);
    Gdx.input = mockInput;
    Gdx.graphics = mockGraphics;
    Gdx.app = mockApplication;

    mockMenuManager = mock(MenuManager.class);
    mockBatch = mock(SpriteBatch.class);
    mockFont = mock(BitmapFont.class);
    mockCamera = mock(OrthographicCamera.class);
    mockViewport = mock(Viewport.class);
    mockGameMenu = mock(GameMenu.class);

    when(mockMenuManager.getMenu(MenuType.GAME)).thenReturn(mockGameMenu);

    when(mockGraphics.getWidth()).thenReturn(800);
    when(mockGraphics.getHeight()).thenReturn(600);
    when(mockViewport.getWorldWidth()).thenReturn(800f);
    when(mockViewport.getWorldHeight()).thenReturn(600f);

    startMenu = new StartMenu(
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
    Gdx.input = null;
    Gdx.graphics = null;
  }

  @Test
  void testHandleInputClickPlayButtonSetsGameMenu() {
    MenuButton button = startMenu.getButtons().get(0);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = startMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.GAME);
  }

  @Test
  void testHandleInputClickTutorialButtonSetsTutorialMenu() {
    MenuButton button = startMenu.getButtons().get(1);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = startMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.TUTORIAL);
  }

  @Test
  void testHandleInputClickQuitButtonCallsExit() {
    MenuButton button = startMenu.getButtons().get(2);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = startMenu.handleInput();

    assertEquals(true, result);
    verify(mockApplication).exit();
  }

}
