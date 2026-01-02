package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.button.MenuButton;
import com.team3._8.game.menu.impl.TutorialMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class TutorialMenuTests {

  private MenuManager mockMenuManager;
  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private OrthographicCamera mockCamera;
  private Viewport mockViewport;
  private Input mockInput;
  private Graphics mockGraphics;

  private TutorialMenu tutorialMenu;

  @BeforeEach
  public void createTutorialMenu() {
    mockInput = mock(Input.class);
    mockGraphics = mock(Graphics.class);
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

    tutorialMenu = new TutorialMenu(
            mockMenuManager,
            mockBatch,
            mockFont,
            mockCamera,
            mockViewport,
            mockFont,
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
  void testHandleInputClickPlayAgainButtonSetsMainMenu() {
    MenuButton button = tutorialMenu.getButtons().get(0);
    when(mockInput.justTouched()).thenReturn(true);
    when(mockInput.getX()).thenReturn((int) button.bounds.getX());
    when(mockInput.getY()).thenReturn((int) button.bounds.getY());

    boolean result = tutorialMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.MAIN_MENU);
  }

  @Test
  void testEscapeKeyTriggersMainMenu() {
    when(mockInput.isKeyJustPressed(Input.Keys.ESCAPE)).thenReturn(true);

    boolean result = tutorialMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.MAIN_MENU);
  }

  @Test
  void testShowDoesNotThrow() {
    assertDoesNotThrow(() -> tutorialMenu.show());
  }

}
