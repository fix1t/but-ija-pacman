package src.game;

import src.tool.common.CommonMaze;
import src.tool.common.CommonField;
import src.tool.common.CommonMazeObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the configuration for creating a Pacman maze.
 * @author Gabriel Biel
 */
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
  List<CommonMazeObject> listOfBoosts;
  PacmanObject pacman;
  TargetObject target;
  boolean disableGhosts;
  Map<CommonMazeObject, PathField> initialObjectsLayout;

  private void initiateClass(){
    this.rows = 0;
    this.cols = 0;
    this.currentRow = 0;
    this.started = false;
    this.errorFlag = false;
    this.listOfGhosts = new ArrayList<>();
    this.listOfKeys = new ArrayList<>();
    this.listOfBoosts = new ArrayList<>();
    this.pacman = null;
    this.target = null;
    this.initialObjectsLayout = new HashMap<>();
    this.disableGhosts = false;
  }


  /**
   * Constructs a new MazeConfigure object.
   */
  public MazeConfigure() {
    this.initiateClass();
  }

  /**
   * Constructs a new MazeConfigure object with the option to disable ghosts.
   *
   * @param disableGhosts true to disable ghosts, false otherwise
   */
  public MazeConfigure(boolean disableGhosts) {
    this.initiateClass();
    this.disableGhosts = disableGhosts;
  }

  /**
   * Starts reading the maze by specifying the number of rows and columns.
   *
   * @param rows the number of rows in the maze
   * @param cols the number of columns in the maze
   */
  public void startReading(int rows, int cols) {
    this.rows = rows + BORDER;
    this.cols = cols + BORDER;
    this.started = true;
    this.fields = new CommonField[rows + BORDER][cols + BORDER];
    this.maze = new Maze(this.cols, this.rows);
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
      this.pacman = new PacmanObject(pathField, this.listOfKeys, this.listOfBoosts);
      pathField.put(this.pacman);
      // put pacman into initialObjectsLayout
      this.initialObjectsLayout.put(this.pacman,pathField);
      return true;
    }
  }

  private void handleGhostCase(int i) {
    PathField pathField = createPathField(this.currentRow, i + 1);
    GhostObject ghost = new GhostObject(pathField);
    pathField.put(ghost);
    listOfGhosts.add(ghost);
    // put ghost into initialObjectsLayout
    this.initialObjectsLayout.put(ghost,pathField);
  }

  private void handleKeyCase(int i) {
    PathField pathField = createPathField(this.currentRow, i + 1);
    KeyObject key = new KeyObject(pathField);
    pathField.put(key);
    this.listOfKeys.add(key);
    // put key into initialObjectsLayout
    this.initialObjectsLayout.put(key,pathField);
  }

  private void handleBoostCase(int i) {
    PathField pathField = createPathField(this.currentRow, i + 1);
    BoostObject boost = new BoostObject(pathField);
    pathField.put(boost);
    this.listOfBoosts.add(boost);
    // put boost into initialObjectsLayout
    this.initialObjectsLayout.put(boost,pathField);
  }

  private boolean handleTargetCase(int i) {
    if (this.target != null) {
      return false;
    } else {
      PathField pathField = createPathField(this.currentRow, i + 1);
      this.target = new TargetObject(pathField);
      pathField.put(this.target);
      // put target into initialObjectsLayout
      this.initialObjectsLayout.put(this.target,pathField);
      return true;
    }
  }

  /**
   * Processes a line of the maze.
   *
   * @param line the line to be processed
   * @return true if the line is processed successfully, false otherwise
   */
  public boolean processLine(String line) {
    if (!this.started || this.cols - BORDER != line.length()) {
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
          if (this.disableGhosts) {
            handleDotCase(i);
            break;
          }
          handleGhostCase(i);
          break;
        case 'K':
          handleKeyCase(i);
          break;
        case 'B':
          handleBoostCase(i);
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

  /**
   * Stops reading the maze.
   *
   * @return true if the maze was successfully read, false otherwise
   */
  public boolean stopReading() {
    // if maze is not started or errorFlag is true, return false
    // if stopped reading before all rows/more rows were read, return false
    return currentRow == this.rows  - BORDER && this.started && !this.errorFlag;
  }

  /**
   * Loads the maze from an input stream.
   *
   * @param inputStream the input stream containing the maze data
   * @return the loaded maze, or null if an error occurred
   */
  public CommonMaze loadMaze(InputStream inputStream) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String[] dimensions = br.readLine().split(" ");
      try {
        this.rows = Integer.parseInt(dimensions[0]);
        this.cols = Integer.parseInt(dimensions[1]);
      } catch (NumberFormatException e) {
        System.out.println("Invalid format of maze dimensions");
        return null;
      }
      this.startReading(rows, cols);
      String line;
      while ((line = br.readLine()) != null) {
        if (!this.processLine(line)) {
          System.out.println("Invalid format of this line: " + line);
          // if maze is complete but there are other data return maze - replay use-case
          if (this.stopReading()) {
            return this.createMaze();
          } else {
            return null;
          }
        }
      }
      // check if maze is finished correctly
      if (this.stopReading()) {
        return this.createMaze();
      } else {
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Adds border wall to the maze.
   */
  public void addBorder(){
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        if (i == 0 || i == this.rows - 1) {
          this.fields[i][j] = new WallField(i, j);
        } else if (j == 0 || j == this.cols - 1) {
          this.fields[i][j] = new WallField(i, j);
        }
      }
    }
  }

  /**
   * Creates the maze object based on the configuration.
   *
   * @return the created maze object, or null if an error occurred
   */
  public CommonMaze createMaze() {
    if (this.errorFlag)
      return null;

    //create border wall
    this.addBorder();
    //set fields
    this.maze.setFields(this.fields);
    this.maze.setGhostList(this.listOfGhosts);
    this.maze.setListOfKeys(this.listOfKeys);
    this.maze.setPacman(this.pacman);
    this.maze.setTarget(this.target);
    this.maze.setInitialObjectsLayout(this.initialObjectsLayout);
    this.maze.setListOfBoosts(this.listOfBoosts);
    return this.maze;
  }
}
