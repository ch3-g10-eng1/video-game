package uk.ac.york.cs.eng1.team10.headless;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.team3._8.game.Bob;
import com.team3._8.game.Maze;

public class MazeTests extends AbstractHeadlessGdxTest {
  Maze maze;
  TiledMap mockMap;
  TiledMapRenderer mockRenderer;
  MapObjects mockObjects;
  MapLayers mockLayers;
  RectangleMapObject mockWinLayer;
  RectangleMapObject mockEventLayer;

  @BeforeEach
  public void createMaze() {
    // Mock the map and layers
    mockMap = mock(TiledMap.class);
    mockRenderer = mock(TiledMapRenderer.class);
    mockObjects = mock(MapObjects.class);
    mockLayers = mock(MapLayers.class);
    mockWinLayer = mock(RectangleMapObject.class);
    mockEventLayer = mock(RectangleMapObject.class);

    // Used to force Maze constructor to initialize final fields properly
    MapLayer mockCollisionLayer = mock(MapLayer.class);
    when(mockLayers.get("collisionLayer")).thenReturn(mockCollisionLayer);
    when(mockCollisionLayer.getObjects()).thenReturn(mockObjects);
    when(mockMap.getLayers()).thenReturn(mockLayers);
    when(mockLayers.getCount()).thenReturn(10);

    // Used to mock win layer hit box
    Rectangle winRect = new Rectangle(0, 0, 10, 10);
    when(mockWinLayer.getRectangle()).thenReturn(winRect);

    // Used to mock event layer hit box
    Rectangle eventRect = new Rectangle(0, 0, 10, 10);
    when(mockEventLayer.getRectangle()).thenReturn(eventRect);

    maze = new Maze(
      mockMap,
      mockRenderer,
      new String[]{"layer1", "layer2"},
      new String[]{"collisionLayer"},
      mockWinLayer,
      mockEventLayer
    );
  }

  @Test
    void testAddVisibleLayerReturnsFalseForNonexistentLayer() {
        when(mockLayers.getIndex("fakeLayer")).thenReturn(-1);

        boolean result = maze.addVisibleLayer("fakeLayer");

        assertEquals(false, result);
    }

  @Test
    void testAddVisibleLayerCorrectlyAddsRealLayer() {
        when(mockLayers.getIndex("layer3")).thenReturn(3);

        boolean result = maze.addVisibleLayer("layer3");

        assertEquals(true, result);
    }

    @Test
    void testRemoveVisibleLayerReturnsFalseForNonexistentLayer() {
        when(mockLayers.getIndex("fakeLayer")).thenReturn(-1);

        boolean result = maze.removeVisibleLayer("fakeLayer");

        assertEquals(false, result);
    }

    @Test
    void testRemoveVisibleLayerCorrectlyRemovesRealLayer() {
        when(mockLayers.getIndex("layer3")).thenReturn(3);

        boolean result = maze.removeVisibleLayer("layer3");

        assertEquals(true, result);
    }

    @Test
    void testAddCollisionLayerDoesNotThrow() {
        MapLayer mockNewLayer = mock(MapLayer.class);
        when(mockLayers.get("newLayer")).thenReturn(mockNewLayer);
        when(mockNewLayer.getObjects()).thenReturn(mockObjects);

        assertDoesNotThrow(() -> maze.addCollisionLayer("newLayer"));
    }

    @Test
    void testRemoveCollisionLayerReturnsFalseIfLayerExistsButIsNotInCollidableObjects() {
        MapLayer missingLayer = mock(MapLayer.class);
        when(mockLayers.get("missing")).thenReturn(missingLayer);

        assertEquals(false, maze.removeCollisionLayer("missing"));
    }

  @Test
  void testRemoveCollisionLayerReturnsTrueIfLayerIsInCollidableObjects() throws Exception {
    // Access the private collidable_objects field using reflection
    Field collidableField = Maze.class.getDeclaredField("collidable_objects");
    collidableField.setAccessible(true);
    Set<MapObjects> collidableObjects = (Set<MapObjects>) collidableField.get(maze);
    // Add the mockObjects to simulate that the layer is already a collision layer
    collidableObjects.add(mockLayers.get("collisionLayer").getObjects());

    boolean result = maze.removeCollisionLayer("collisionLayer");

    assertEquals(true, result);
  }

  @Test
  void testHitsWinLayerReturnsTrueWhenBobOverlaps() {
    // Creates a Bob instance to collide with win layer
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    Sprite bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(5, 5);
    bobSprite.setSize(15, 15);
    Bob bob = new Bob(bobSprite, 60, 0);

    assertEquals(true, maze.HitsWinLayer(bob));
  }

  @Test
  void testHitsEventLayerTriggersOnlyOnce() {
    // Creates a Bob instance to collide with event layer
    TextureAtlas atlas = new TextureAtlas("atlas/bob.atlas");
    Sprite bobSprite = new Sprite(atlas.findRegion("front-bob"));
    bobSprite.setPosition(5, 5);
    bobSprite.setSize(15, 15);
    Bob bob = new Bob(bobSprite, 60, 0);

    assertEquals(true, maze.HitsEventLayer(bob));
    assertEquals(false, maze.HitsEventLayer(bob)); // second call should be false
  }
}