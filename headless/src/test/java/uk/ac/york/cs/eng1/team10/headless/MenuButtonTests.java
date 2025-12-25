package uk.ac.york.cs.eng1.team10.headless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.team3._8.game.menu.button.MenuButton;

public class MenuButtonTests {

  private TextureRegion mockIdle;
  private TextureRegion mockHover;
  private TextureRegion mockPressed;
  private Runnable mockOnClick;

  private MenuButton button;

  @BeforeEach
  public void createMenuButton() {
    mockIdle = mock(TextureRegion.class);
    mockHover = mock(TextureRegion.class);
    mockPressed = mock(TextureRegion.class);
    mockOnClick = mock(Runnable.class);

    button = new MenuButton("Test", 10, 20, 100, 40, mockIdle, mockHover, mockPressed, mockOnClick);
  }

  @Test
  void testGetFrameReturnsIdleByDefault() {
    TextureRegion frame = button.getFrame();

    assertSame(mockIdle, frame);
  }

  @Test
  void testGetFrameReturnsHoverWhenHovered() {
    button.hovered = true;

    TextureRegion frame = button.getFrame();

    assertSame(mockHover, frame);
  }

  @Test
  void testGetFrameReturnsPressedWhenClicked() {
    button.clicked = true;

    TextureRegion frame = button.getFrame();

    assertSame(mockPressed, frame);
  }

  @Test
  void testGetFramePrioritisesPressedOverHover() {
    button.hovered = true;
    button.clicked = true;

    TextureRegion frame = button.getFrame();

    assertSame(mockPressed, frame);
  }

  @Test
  void testGetFrameUsesHoverIfPressedTextureIsNull() {
    MenuButton buttonWithoutPressed = new MenuButton("Options", 0, 0, 50, 20, mockIdle, mockHover, null, mockOnClick);
    buttonWithoutPressed.clicked = true;
    buttonWithoutPressed.hovered = true;

    TextureRegion frame = buttonWithoutPressed.getFrame();

    assertSame(mockHover, frame);
  }

  @Test
  void testConstructorSetsBoundsCorrectly() {
    Rectangle bounds = button.bounds;

    assertEquals(10, bounds.x);
    assertEquals(20, bounds.y);
    assertEquals(100, bounds.width);
    assertEquals(40, bounds.height);
  }

  @Test
    void testConstructorSetsTextAndOnClick() {
      assertEquals("Test", button.text);
      assertSame(mockOnClick, button.onClick);
    }

}
