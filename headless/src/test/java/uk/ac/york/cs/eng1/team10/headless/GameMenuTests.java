package uk.ac.york.cs.eng1.team10.headless;

import java.util.HashMap;
import java.util.Map;

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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.impl.GameMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class GameMenuTests {

  private GameMenu gameMenu;

  private OrthographicCamera mockCamera;
  private MenuManager mockMenuManager;
  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private Viewport mockViewport;
  private Input mockInput;
  private Graphics mockGraphics;

  @BeforeEach
  public void createGameMenu(){

    mockCamera = mock(OrthographicCamera.class);
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

    Gdx.input = mockInput;
    Gdx.graphics = mockGraphics;

    gameMenu = new GameMenu(mockMenuManager, mockBatch, mockFont, mockCamera, mockViewport, true);
  }

  @Test
  void testHandleInputPressEscapeKeySetsPauseMenu() {
    when(mockInput.isKeyJustPressed(Input.Keys.ESCAPE)).thenReturn(true);
    when(mockInput.isKeyPressed(Input.Keys.ESCAPE)).thenReturn(true);

    boolean result = gameMenu.handleInput();

    assertEquals(true, result);
    verify(mockMenuManager).setMenu(MenuType.PAUSE);
  }

  @Test
  void testCheckAchievementsSetsCorrectAchievements(){
    HashMap<String, Integer> eventTracker = new HashMap<>();
    eventTracker.put("Positive",6);
    eventTracker.put("Negative",2);
    eventTracker.put("Hidden",1);
    gameMenu.setEventTracker(eventTracker);

    gameMenu.doCheckAchievements();
    Map<String, Boolean> achievements = gameMenu.getAchievements();
    assertEquals(true, achievements.get("Positive Collector"));
    assertEquals(true, achievements.get("Negative Collector"));
    assertEquals(true, achievements.get("All Events"));
  }

  @Test
  void testCheckTimeoutSetsLoseMenuWhenTimeIs300() {
    gameMenu.setTimer(300f);

    gameMenu.checkTimeout();

    verify(mockMenuManager).setMenu(MenuType.LOSE);
  }

}
