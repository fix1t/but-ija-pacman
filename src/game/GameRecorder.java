package src.game;

import src.tool.common.CommonField;
import src.tool.common.CommonMazeObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the recorder for a Pacman game.
 * Records the states of each maze object at each move.
 * @author Gabriel Biel
 */
public class GameRecorder {
  protected Map<CommonMazeObject, List<CommonField>> stateMap;
  private PrintWriter writer;
  private int moveCount;

  /**
   * Creates a new game recorder.
   */
  public GameRecorder() {
    this.stateMap = new HashMap<>();
    this.moveCount = 0;
    try {
      // Overwrite the file if it already exists
      this.writer = new PrintWriter(new FileWriter("game.log", false));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Stops the recording and closes the writer.
   */
  public void stopRecording() {
    this.writer.close();
  }

  /**
   * Captures the state of each maze object.
   *
   * @param allMazeObjects the list of all maze objects
   */
  public void captureState(List<CommonMazeObject> allMazeObjects) {
    for (CommonMazeObject mazeObject : allMazeObjects) {
      if (!this.stateMap.containsKey(mazeObject)) {
        // If not present, create a new empty list and add it to the map
        this.stateMap.put(mazeObject, new ArrayList<>());
      }
      List<CommonField> fieldsList = this.stateMap.get(mazeObject);
      fieldsList.add(mazeObject.getField());
    }
  }

  /**
   * Captures the state of each maze object and optionally writes it to the file.
   *
   * @param allMazeObjects the list of all maze objects
   * @param writeToFile    flag indicating whether to write to the file
   */
  public void captureState(List<CommonMazeObject> allMazeObjects, boolean writeToFile) {
    if (!writeToFile) {
      this.captureState(allMazeObjects);
      return;
    }
    for (int i = 0; i < allMazeObjects.size(); i++) {
      CommonMazeObject mazeObject = allMazeObjects.get(i);
      CommonField field = mazeObject.getField();
      String mazeObjectType = mazeObject.getType().toString();
      if (field == null) {
        // If the maze object is not on any field, skip it
        continue;
      }
      // [ORD]: [moveCount] [OBJ]: [mazeObjectType] [i] [ON]: ([x],[y])
      String message = "ORD: " + (this.moveCount) + " OBJ: " + mazeObjectType + " " + i +
        " ON: (" + field.getCoordinate().getX() + "," + field.getCoordinate().getY() + ")";
      this.writer.println(message);
    }
    this.moveCount++;
  }

  /**
   * Creates a game log file.
   * For each maze object, print its type and all fields it has been on in order.
   */
  public void createGameLog() {
    for (Map.Entry<CommonMazeObject, List<CommonField>> entry : stateMap.entrySet()) {
      CommonMazeObject mazeObject = entry.getKey();
      List<CommonField> fieldsList = entry.getValue();
      for (int i = 0; i < fieldsList.size(); i++) {
        if (i == 0) {
          // Print the type of the maze object only once at the beginning
          this.writer.println("NEW: " + mazeObject.getType().toString());
        }
        CommonField field = fieldsList.get(i);
        this.writer.println("ORD: " + (i + 1) + " | (" + field.getCoordinate().getX() + "," + field.getCoordinate().getY() + ")");
      }
    }
  }

  /**
   * Records the maze from the specified path and writes it to the file.
   *
   * @param pathToMaze the path to the maze file
   */
  public void recordMaze(Path pathToMaze) {
    try (InputStream inputStream = Files.newInputStream(pathToMaze)) {
      // Read the maze from the input stream and write it to the file
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = br.readLine()) != null) {
        this.writer.println(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
