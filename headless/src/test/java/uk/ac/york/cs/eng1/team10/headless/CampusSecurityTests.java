package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.team3._8.game.CampusSecurity;

public class CampusSecurityTests extends AbstractHeadlessGdxTest {
  private CampusSecurity campusSecurity;
  private Sprite campusSecuritySprite;
  private Graphics mockGraphics;
  private Graphics originalGraphics;
  private TextureAtlas atlas;

  @BeforeEach
  public void createCampusSecurity() {
    originalGraphics = Gdx.graphics;

    atlas = new TextureAtlas("atlas/security_geese.atlas");
    campusSecuritySprite = new Sprite(atlas.findRegion("walking"));
    campusSecuritySprite.setPosition(100, 500);
    campusSecuritySprite.setSize(15, 15);
    campusSecurity = new CampusSecurity(campusSecuritySprite, 50);
    mockGraphics = mock(Graphics.class);
    Gdx.graphics = mockGraphics;
  }

  @AfterEach
  public void tearDown() {
    Gdx.graphics = originalGraphics;

    if (atlas != null) {
        atlas.dispose();
    }
  }

  @Test
  public void testCampusSecurityCreationSetsCorrectValues() {
    assertEquals(campusSecuritySprite.getHeight(), 15);
    assertEquals(campusSecuritySprite.getWidth(), 15);
    assertEquals(campusSecurity.getX(), 100);
    assertEquals(campusSecurity.getY(), 500);
    assertEquals(campusSecurity.getSpeed(), 50);
  }

  @Test
  public void testStartAndStopInteractionReturnCorrectData() {
    assertEquals(true, campusSecurity.startInteraction().get("Reset Player Position"));
    assertEquals(false, campusSecurity.stopInteraction().get("Reset Player Position"));
  }

  @Test
  public void testCampusSecurityMoveUpChangesCampusSecurityPositionUp() {
    float initialX = campusSecurity.getX();
    float initialY = campusSecurity.getY();
    float deltaTime = 0.016f;
    // Inject delta time into move()
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);

    // Move campus security up
    campusSecurity.doMove(true);

    assertEquals(initialX, campusSecurity.getX());
    assertEquals(initialY + campusSecurity.getSpeed() * deltaTime, campusSecurity.getY());
  }

  @Test
  public void testCampusSecurityMoveDownChangesCampusSecurityPositionDown() {
    float initialX = campusSecurity.getX();
    float initialY = campusSecurity.getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);

    campusSecurity.doMove(false);

    assertEquals(initialX, campusSecurity.getX());
    assertEquals(initialY - campusSecurity.getSpeed() * deltaTime, campusSecurity.getY());
  }

  @Test
  public void testCollisionBoxMoveUpChangesCollisionBoxPositionUp() {
    float initialX = campusSecurity.getCollisionBox().getX();
    float initialY = campusSecurity.getCollisionBox().getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);

    campusSecurity.doMove(true);

    assertEquals(initialX + 8f, campusSecurity.getCollisionBox().getX()); // 8f added as inset value
    assertEquals(initialY + deltaTime * campusSecurity.getSpeed(), campusSecurity.getCollisionBox().getY());
  }

  @Test
  public void testCollisionBoxMoveDownChangesCollisionBoxPositionDown() {
    float initialX = campusSecurity.getCollisionBox().getX();
    float initialY = campusSecurity.getCollisionBox().getY();
    float deltaTime = 0.016f;
    when(mockGraphics.getDeltaTime()).thenReturn(deltaTime);

    campusSecurity.doMove(false);

    assertEquals(initialX + 8f, campusSecurity.getCollisionBox().getX()); // 8f added as inset value
    assertEquals(initialY - deltaTime * campusSecurity.getSpeed(), campusSecurity.getCollisionBox().getY());
  }
}

