package ija.ija2022.homework2.tool.common;

import ija.ija2022.homework2.game.PacmanObject;

import java.util.List;

public interface CommonMaze {
  CommonField getField(int var1, int var2);

  int numRows();

  int numCols();

  List<CommonMazeObject> getGhosts();

  List<CommonMazeObject> getKeys();

  CommonMazeObject getTarget();

  PacmanObject getPacman();

  void restore();

  void restoreGame();
}
