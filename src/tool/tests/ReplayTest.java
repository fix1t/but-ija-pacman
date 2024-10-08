package src.tool.tests;

import src.game.Game;
import src.game.GameReplay;
import src.game.GhostObject;
import src.game.PacmanObject;
import src.tool.common.CommonMaze;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

public class ReplayTest {

  private CommonMaze maze;
  private Game game;
  private GameReplay replay;

  @Before
  public void setUp() {
    this.replay = new GameReplay();
    Path pathToMaze = Path.of("src/tool/tests/replays/expectedLog");
    boolean result = replay.loadGameFromFile(pathToMaze);
    this.maze = replay.getMaze();
    Assert.assertNotNull(this.maze);
    Assert.assertTrue(result);
  }

  @Test
  public void LoadSuccess() {
    Assert.assertNotNull(this.replay.getMaze());
    PacmanObject pacman = this.maze.getPacman();
    GhostObject ghost = (GhostObject) this.maze.getGhosts().get(0);
    Assert.assertNotNull(pacman);
    Assert.assertNotNull(ghost);

    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());

    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));
  }

  @Test
  public void GetRightPositionFromStart() {
    this.replay.replayGameFromStart();
    this.maze = this.replay.getMaze();

    PacmanObject pacman = this.maze.getPacman();
    GhostObject ghost = (GhostObject) this.maze.getGhosts().get(0);
    Assert.assertNotNull(pacman);
    Assert.assertNotNull(ghost);

    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());

    // First state - show the same position
    this.replay.presentPreviousState();
    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentNextState();
    Assert.assertEquals(this.maze.getField(4,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(4,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentNextState();
    Assert.assertEquals(this.maze.getField(3,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(3,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentNextState();
    Assert.assertEquals(this.maze.getField(2,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(2,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentNextState();
    Assert.assertEquals(this.maze.getField(1,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(1,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    // Last state - show the same position
    this.replay.presentNextState();
    Assert.assertEquals(this.maze.getField(1,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(1,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));
  }

  @Test
  public void GetRightPositionFromEnd() {
    this.replay.replayGameFromEnd();
    this.maze = this.replay.getMaze();

    PacmanObject pacman = this.maze.getPacman();
    GhostObject ghost = (GhostObject) this.maze.getGhosts().get(0);
    Assert.assertNotNull(pacman);
    Assert.assertNotNull(ghost);
    Assert.assertNotNull(this.maze);



    Assert.assertEquals(this.maze.getField(1,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(1,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    // Last state - show the same position
    this.replay.presentNextState();
    Assert.assertEquals(this.maze.getField(1,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(1,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentPreviousState();
    Assert.assertEquals(this.maze.getField(2,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(2,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentPreviousState();
    Assert.assertEquals(this.maze.getField(3,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(3,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentPreviousState();
    Assert.assertEquals(this.maze.getField(4,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(4,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    this.replay.presentPreviousState();
    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));

    // First state - show the same position
    this.replay.presentPreviousState();
    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));
  }

  @Test
  public void GetRightPositionFromStart_Continuous() {
    this.replay.replayGameFromStart();
    this.maze = this.replay.getMaze();

    PacmanObject pacman = this.maze.getPacman();
    GhostObject ghost = (GhostObject) this.maze.getGhosts().get(0);
    Assert.assertNotNull(pacman);
    Assert.assertNotNull(ghost);

    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());

    this.replay.resume();
    this.replay.continueForward(0);
    Assert.assertEquals(this.maze.getField(1,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(1,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));
  }

  @Test
  public void GetRightPositionFromEnd_Continuous() {
    this.replay.replayGameFromEnd();
    this.maze = this.replay.getMaze();

    PacmanObject pacman = this.maze.getPacman();
    GhostObject ghost = (GhostObject) this.maze.getGhosts().get(0);
    Assert.assertNotNull(pacman);
    Assert.assertNotNull(ghost);

    Assert.assertEquals(this.maze.getField(1,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(1,1),ghost.getField());

    this.replay.resume();
    this.replay.continueBackward(0);
    Assert.assertEquals(this.maze.getField(5,3),pacman.getField());
    Assert.assertEquals(this.maze.getField(5,1),ghost.getField());
    Assert.assertTrue(pacman.getField().contains(pacman));
    Assert.assertTrue(ghost.getField().contains(ghost));
  }
}

