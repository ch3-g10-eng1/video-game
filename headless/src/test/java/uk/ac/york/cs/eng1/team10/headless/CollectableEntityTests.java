package uk.ac.york.cs.eng1.team10.headless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.team3._8.game.Bob;
import com.team3._8.game.CollectableEntity;

public class CollectableEntityTests extends AbstractHeadlessGdxTest {
  private Sprite stubSprite;
  private CollectableEntity collectableEntity;

  @BeforeEach
  public void createCollectableEntity() {
    Texture texture = new Texture(new Pixmap(1, 1, Pixmap.Format.RGBA8888));
    stubSprite = new Sprite(texture);
    stubSprite.setPosition(100, 100);
    collectableEntity = new CollectableEntity(stubSprite, 0, "TestType");
  }

  @Test
  public void testCollectableEntityCreationSetsCorrectValues() {
    assertEquals(0, collectableEntity.getSpeed());
    assertEquals(1, collectableEntity.getCollisionBox().getWidth());
    assertEquals(1, collectableEntity.getCollisionBox().getHeight());
    assertEquals(100, collectableEntity.getCollisionBox().getX());
    assertEquals(100, collectableEntity.getCollisionBox().getY());
  }

  @Test
  public void testCollectableEntityCollectedReturnsTrueWhenCollected() {
    // Create a Bob instance to collect the entity
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    Sprite bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(100, 100);
    bobSprite.setSize(15, 15);
    Bob bob = new Bob(bobSprite, 60, 0);
    assertEquals(true, collectableEntity.collected(bob));
  }

  @Test
  public void testCollectableEntityCollectedReturnsFalseWhenNotCollected() {
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    Sprite bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(100, 100);
    bobSprite.setSize(15, 15);
    Bob bob = new Bob(bobSprite, 60, 0);
    collectableEntity.collected(bob);
    // Collecting again should return false as it is already collected
    assertEquals(false, collectableEntity.collected(bob));
  }

  @Test
  public void testCollectableEntityNotCollectedWhenNotInContactWithBob() {
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    Sprite bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(500, 500);
    bobSprite.setSize(15, 15);
    Bob bob = new Bob(bobSprite, 60, -3);
    // Should not be able to collect as Bob is far away
    assertEquals(false, collectableEntity.collected(bob));
  }
}
