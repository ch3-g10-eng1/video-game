package uk.ac.york.cs.eng1.team10.headless;

import org.junit.jupiter.api.BeforeEach;

import com.team3._8.game.Maze;

public class MazeTests extends AbstractHeadlessGdxTest {
  private Maze maze;

  @BeforeEach
  public void createMaze() {
  String[] collidable_layers = {"Collision", "Doors"};
  maze = new Maze("Map/CSE_map.tmx", collidable_layers, "WinDoors", "EventTrigger");
  }
}
