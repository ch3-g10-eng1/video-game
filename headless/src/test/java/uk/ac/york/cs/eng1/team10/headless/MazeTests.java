package uk.ac.york.cs.eng1.team10.headless;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.team3._8.game.Bob;
import com.team3._8.game.CollectableEntity;
import com.team3._8.game.Maze;

public class MazeTests {
  private Maze maze;
  private TiledMap mockMap;
  private TiledMapRenderer mockRenderer;
  private MapObjects mockObjects;
  private MapLayers mockLayers;
  private RectangleMapObject mockWinLayer;
  private RectangleMapObject mockEventLayer;
  private Bob mockBob;

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

    mockBob = mock(Bob.class);
    when(mockBob.getSpeed()).thenReturn(10f);
    Rectangle entityBox = new Rectangle(50, 50, 10, 10);
    mockBob.setCollisionBox(entityBox);

    maze = new Maze(
      mockMap,
      mockRenderer,
      new String[]{"layer1", "layer2"},
      createWallForMaze(new Rectangle(50, 50, 20, 20)),
      mockWinLayer,
      mockEventLayer
    );
  }

  private Set<MapObjects> createWallForMaze(Rectangle wallRect) {
    RectangleMapObject wall = new RectangleMapObject(
            wallRect.x,
            wallRect.y,
            wallRect.width,
            wallRect.height
    );

    MapObjects objects = new MapObjects();
    objects.add(wall);

    Set<MapObjects> collidable = new HashSet<>();
    collidable.add(objects);

    return collidable;
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
    // Creates an entity instance (CollectableEntity for easy field control) to collide with Win Layer
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(0f);
    when(mockSprite.getY()).thenReturn(0f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 10, null);

    assertEquals(true, maze.HitsWinLayer(entity));
  }

  @Test
  void testHitsEventLayerTriggersOnlyOnce() {
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(0f);
    when(mockSprite.getY()).thenReturn(0f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 10, null);

    assertEquals(true, maze.HitsEventLayer(entity));
    assertEquals(false, maze.HitsEventLayer(entity)); // second call should be false
  }

  @Test
  void testHitsWallWithNoCollisionReturnsAllFalse() {
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(0f);
    when(mockSprite.getY()).thenReturn(0f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 5, null);

    boolean[] result = maze.hitsWall(entity, 0.1f);

    assertArrayEquals(new boolean[]{false, false, false, false}, result);
  }

  @Test
  void testHitsWallFromLeftBlocksLeft() {
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(39.9f);
    when(mockSprite.getY()).thenReturn(55f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 5, null);

    boolean[] result = maze.hitsWall(entity, 0.1f);

    assertArrayEquals(new boolean[]{true, false, false, false}, result);
  }

  @Test
  void testHitsWallFromRightBlocksRight() {
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(70f);
    when(mockSprite.getY()).thenReturn(55f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 5, null);

    boolean[] result = maze.hitsWall(entity, 0.1f);

    assertArrayEquals(new boolean[]{false, false, true, false}, result);
  }

  @Test
  void testHitsWallFromAboveBlocksDown() {
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(50f);
    when(mockSprite.getY()).thenReturn(39.9f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 5, null);

    boolean[] result = maze.hitsWall(entity, 0.1f);

    assertArrayEquals(new boolean[]{false, false, false, true}, result);
  }

  @Test
  void testHitsWallFromBelowBlocksUp() {
    Sprite mockSprite = mock(Sprite.class);
    when(mockSprite.getX()).thenReturn(50f);
    when(mockSprite.getY()).thenReturn(70f);
    when(mockSprite.getWidth()).thenReturn(10f);
    when(mockSprite.getHeight()).thenReturn(10f);
    CollectableEntity entity = new CollectableEntity(mockSprite, 5, null);

    boolean[] result = maze.hitsWall(entity, 0.1f);

    assertArrayEquals(new boolean[]{false, true, false, false}, result);
  }

}