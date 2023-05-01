package ija.ija2022.homework2.game;

import ija.ija2022.homework2.tool.common.CommonMaze;
import ija.ija2022.homework2.tool.common.CommonField;
import ija.ija2022.homework2.tool.common.CommonMazeObject;

import java.util.List;

public class MazeConfigure {
  private static final int BORDER = 2;
  boolean started;
  int rows;
  int cols;
  boolean errorFlag;
  int currentRow;
  CommonField[][] fields;
  Maze maze;
  List<CommonMazeObject> listOfGhosts;
  List<CommonMazeObject> listOfKeys;
  PacmanObject pacman;
  TargetObject target;

  //constructor
  public MazeConfigure() {
    this.rows = 0;
    this.cols = 0;
    this.currentRow = 0;
    this.started = false;
    this.errorFlag = false;
  }

  public void startReading(int rows, int cols) {
    this.rows = rows + BORDER;
    this.cols = cols + BORDER;
    this.started = true;
    this.fields = new CommonField[rows + BORDER][cols + BORDER];
    this.maze = new Maze(this.cols, this.rows);
    this.listOfGhosts = maze.ghosts();
    this.listOfKeys = maze.keys();
    this.pacman = null;
    this.target = null;
  }

  private PathField createPathField(int row, int col) {
    PathField pathField = new PathField(row, col);
    pathField.setMaze(this.maze);
    fields[row][col] = pathField;
    return pathField;
  }

  private void handleDotCase(int i) {
    createPathField(this.currentRow, i + 1);
  }

  private void handleWallCase(int i) {
    fields[this.currentRow][i + 1] = new WallField(this.currentRow, i + 1);
  }

  private boolean handlePacmanCase(int i) {
    // if pacman is already placed, return false
    if (this.pacman != null) {
      return false;
    } else {
      PathField pathField = createPathField(this.currentRow, i + 1);
      this.pacman = new PacmanObject(pathField);
      pathField.put(this.pacman);
      return true;
    }
  }

  private void handleGhostCase(int i) {
    PathField pathField = createPathField(this.currentRow, i + 1);
    GhostObject ghost = new GhostObject(pathField);
    pathField.put(ghost);
    listOfGhosts.add(ghost);
  }

  private void handleKeyCase(int i) {
    PathField pathField = createPathField(this.currentRow, i + 1);
    KeyObject key = new KeyObject(pathField);
    pathField.put(key);
    this.listOfKeys.add(key);
  }

  private boolean handleTargetCase(int i) {
    if (this.target != null) {
      return false;
    } else {
      PathField pathField = createPathField(this.currentRow, i + 1);
      this.target = new TargetObject(pathField);
      pathField.put(this.target);
      return true;
    }
  }

  public boolean processLine(String line) {
    if (!this.started || this.cols - BORDER != line.length()) {
      this.errorFlag = true;
      return false;
    }

    if (this.currentRow >= this.rows - BORDER) {
      this.errorFlag = true;
      return false;
    }
    this.currentRow++;

    for (int i = 0; i < line.length(); i++) {
      switch (line.charAt(i)) {
        case '.':
          handleDotCase(i);
          break;
        case 'X':
          handleWallCase(i);
          break;
        case 'S':
          // if pacman is already placed, return false = error
          if (!handlePacmanCase(i)) {
            return false;
          }
          break;
        case 'G':
          handleGhostCase(i);
          break;
        case 'K':
          handleKeyCase(i);
          break;
        case 'T':
          // if target is already placed, return false = error
          if (!handleTargetCase(i)){
            return false;
          }
          break;
        default:
          this.errorFlag = true;
          return false;
      }
    }
    return true;
  }


  public boolean stopReading() {
    return currentRow + 1 == this.rows && this.started && !this.errorFlag;
  }

  public CommonMaze createMaze() {
    if (this.errorFlag)
      return null;

    //create border wall
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        if (i == 0 || i == this.rows - 1) {
          this.fields[i][j] = new WallField(i, j);
        } else if (j == 0 || j == this.cols - 1) {
          this.fields[i][j] = new WallField(i, j);
        }
      }
    }
    this.maze.setFields(fields);
    this.maze.setGhostList(listOfGhosts);
    this.maze.setKeysList(listOfKeys);
    this.maze.setPacman(this.pacman);
    this.maze.setTarget(this.target);
    return this.maze;
  }
}
