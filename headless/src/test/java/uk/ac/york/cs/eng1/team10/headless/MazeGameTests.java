package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.team3._8.game.MazeGame;
import com.team3._8.game.menu.manager.MenuManager;

public class MazeGameTests extends AbstractHeadlessGdxTest {

  private MazeGame game;
  private MenuManager mockMenuManager;
  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private OrthographicCamera mockCamera;
  private FillViewport mockViewport;
  private Graphics mockGraphics;
  private Graphics originalGraphics;

  @BeforeEach
  public void createMazeGame() {
    originalGraphics = Gdx.graphics;

    mockMenuManager = mock(MenuManager.class);
    mockBatch = mock(SpriteBatch.class);
    mockFont = mock(BitmapFont.class);
    mockCamera = mock(OrthographicCamera.class);
    mockViewport = mock(FillViewport.class);
    mockGraphics = mock(Graphics.class);

    Gdx.graphics = mockGraphics;
    when(Gdx.graphics.getDeltaTime()).thenReturn(0.016f);

    game = new MazeGame(mockBatch, mockFont, mockCamera, mockViewport, mockMenuManager);
  }

  @AfterEach
  public void tearDown() {
    Gdx.graphics = originalGraphics;
  }

  @Test
  void testRenderCallsMenuManagerUpdateAndRender() {
    game.render();

    verify(mockGraphics).getDeltaTime();
    verify(mockMenuManager).update(0.016f);
    verify(mockMenuManager).render(0.016f);
  }

  @Test
  void testResizeCallsViewportAndMenuManager() {
    int width = 800;
    int height = 600;
    game.resize(width, height);

    verify(mockViewport).update(width, height, true);
    verify(mockMenuManager).resize(width, height);
  }


  @Test
  void testDisposeCallsDisposeOnBatchFontAndMenuManager() {
    game.dispose();

    verify(mockMenuManager).dispose();
    verify(mockBatch).dispose();
    verify(mockFont).dispose();
  }
}
