package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team3._8.game.menu.BaseMenu;
import com.team3._8.game.menu.manager.MenuManager;
import com.team3._8.game.menu.type.MenuType;

public class MenuManagerTests extends AbstractHeadlessGdxTest {

  private MenuManager menuManager;

  private SpriteBatch mockBatch;
  private BitmapFont mockFont;
  private OrthographicCamera mockCamera;
  private Viewport mockViewport;

  private BaseMenu mockMainMenu;
  private BaseMenu mockGameMenu;

  @BeforeEach
  public void createMenuManager() {
    mockBatch = mock(SpriteBatch.class);
    mockFont = mock(BitmapFont.class);
    mockCamera = mock(OrthographicCamera.class);
    mockViewport = mock(Viewport.class);
    mockMainMenu = mock(BaseMenu.class);
    mockGameMenu = mock(BaseMenu.class);

    menuManager = MenuManager.initialise(mockBatch, mockFont, mockCamera, mockViewport);

    menuManager.registerMenu(MenuType.MAIN_MENU, mockMainMenu);
    menuManager.registerMenu(MenuType.GAME, mockGameMenu);
  }

  @AfterEach
  public void tearDown() {
    menuManager.dispose();
  }

  @Test
  void testInitialiseReturnsSameInstance() {
    // MenuManager is a singleton, so calling initialise again should return the same instance
    MenuManager second = MenuManager.initialise(mockBatch, mockFont, mockCamera, mockViewport);

    assertSame(menuManager, second);
  }

  @Test
  void testGetMenuReturnsRegisteredMenu() {
    assertSame(mockMainMenu, menuManager.getMenu(MenuType.MAIN_MENU));
  }

  @Test
  void testGetMenuThrowsForUnknownMenu() {
    assertThrows(IllegalStateException.class, () -> menuManager.getMenu(MenuType.WIN));
  }

  @Test
  void testSetMenuThrowsIfMenuNotRegistered() {
    assertThrows(IllegalStateException.class, () -> menuManager.setMenu(MenuType.WIN));
  }

  @Test
  void testSetMenuCallsShowOnNewMenu() {
    menuManager.setMenu(MenuType.MAIN_MENU);

    verify(mockMainMenu).show();
  }

  @Test
  void testSetMenuHidesPreviousMenu() {
    menuManager.setMenu(MenuType.MAIN_MENU);
    menuManager.setMenu(MenuType.GAME);

    verify(mockMainMenu).hide();
    verify(mockGameMenu).show();
  }

  @Test
  void testSetMenuSetsCurrentAndPreviousTypes() {
    menuManager.setMenu(MenuType.MAIN_MENU);
    menuManager.setMenu(MenuType.GAME);

    assertEquals(MenuType.GAME, menuManager.getCurrentType());
    assertEquals(MenuType.MAIN_MENU, menuManager.getPrevType());
  }

  @Test
  void testGoBackSwitchesToPreviousMenu() {
    menuManager.setMenu(MenuType.MAIN_MENU);
    menuManager.setMenu(MenuType.GAME);

    menuManager.goBack();

    verify(mockGameMenu).hide();
    verify(mockMainMenu, times(2)).show();
  }

  @Test
  void testUpdateCallsHandleInputAndUpdateOnCurrentMenu() {
    menuManager.setMenu(MenuType.MAIN_MENU);

    menuManager.update(0.16f);

    verify(mockMainMenu).handleInput();
    verify(mockMainMenu).update(0.16f);
  }

  @Test
  void testRenderCallsRenderOnCurrentMenu() {
    menuManager.setMenu(MenuType.MAIN_MENU);

    menuManager.render(0.16f);

    verify(mockMainMenu).render(0.16f);
  }

  @Test
  void testResizeDelegatesToCurrentMenu() {
    menuManager.setMenu(MenuType.MAIN_MENU);

    menuManager.resize(800, 600);

    verify(mockMainMenu).resize(800, 600);
  }

  @Test
  void testDisposeDisposesAllMenusAndClearsSingleton() {
    menuManager.dispose();

    verify(mockMainMenu).dispose();
    verify(mockGameMenu).dispose();
    assertThrows(IllegalStateException.class, MenuManager::getInstance);
  }

}

