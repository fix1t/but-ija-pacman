package src.tool.tests;

import src.game.Game;
import src.game.PacmanObject;
import src.tool.common.CommonField;
import src.tool.common.CommonMaze;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;


public class AStarTestBasic {

  private CommonMaze maze;

// test maze:
//4 3
//..S
//.T.
//.K.
//...
  @Before
  public void setUp() {
    Game game = new Game(300, true);
    this.maze = game.createMazeFromFile(Path.of("src/tool/tests/maps/valid/valid-ng"));
    //check if loaded
    if (maze == null) {
      System.out.println("Error while loading maze");
    }
  }

  @Test
  public void moveOneLeft() {
    Assert.assertNotNull(this.maze);
    CommonField destinationField = this.maze.getField(1, 2);
    PacmanObject pacman = this.maze.getPacman();
    pacman.setGoToField(destinationField);
    pacman.move();
    //check if pacman is on destination field
    Assert.assertEquals(this.maze.getPacman().getField(), destinationField);
  }
  @Test
  public void moveTwoLeft() {
    Assert.assertNotNull(this.maze);
    CommonField destinationField = this.maze.getField(1, 1);
    PacmanObject pacman = this.maze.getPacman();
    pacman.setGoToField(destinationField);
    pacman.move();
    pacman.move();
    //check if pacman is on destination field
    Assert.assertEquals(this.maze.getPacman().getField(), destinationField);
  }

  @Test
  public void MoveAcrossMap() {
    Assert.assertNotNull(this.maze);
    CommonField destinationField = this.maze.getField(4, 1);
    PacmanObject pacman = this.maze.getPacman();
    pacman.setGoToField(destinationField);
    pacman.move();
    pacman.move();
    pacman.move();
    pacman.move();
    pacman.move();
    //check if pacman is on destination field
    Assert.assertEquals(this.maze.getPacman().getField(), destinationField);
  }
}

