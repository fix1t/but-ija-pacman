package src.tool.common;

import src.game.resources.ObjectType;

public interface CommonMazeObject {
  boolean canMove(CommonField.Direction var1);

  boolean move();

  boolean move(CommonField.Direction var1);

  default boolean isPacman() {
    return false;
  }

  CommonField getField();

  void setField(CommonField field);

  int getLives();

  ObjectType getType();

  void setDirection(CommonField.Direction l);
}
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
