package ija.ija2022.homework2.tool.view;

import ija.ija2022.homework2.tool.common.CommonField;
import ija.ija2022.homework2.tool.common.CommonMaze;
import ija.ija2022.homework2.tool.common.CommonMazeObject;
import ija.ija2022.homework2.tool.common.Observable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FieldView extends JPanel implements Observable.Observer {
  private final CommonField model;
  private final List<ComponentView> objects;
  private int changedModel = 0;
  private final CommonMaze maze;

  public FieldView(CommonField model, CommonMaze maze) {
    this.model = model;
    this.maze = maze;
    this.objects = new ArrayList();
    this.privUpdate();
    model.addObserver(this);
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    PacmanView pacman = null;
    GhostView ghost = null;
    for (ComponentView o : this.objects) {
      o.paintComponent(g);
      if(o instanceof PacmanView){
        pacman = (PacmanView) o;
      } else if (o instanceof GhostView) {
        ghost = (GhostView) o;
      }
    }
    // draw ghost and pacman last to be on top of everything
    if (ghost != null) {
      ghost.paintComponent(g);
    }
    if(pacman != null){
      pacman.paintComponent(g);
    }
  }

  private void privUpdate() {
    if (this.model.canMove()) {
      this.setBackground(Color.white);
      if (!this.model.isEmpty()) {
        CommonMazeObject o = this.model.get();
        ComponentView v;
        switch (o.getType()) {
          case TARGET -> v = new TargetView(this, o);
          case KEY -> v = new KeyView(this, o);
          case PACMAN -> v = new PacmanView(this, o);
          case GHOST -> v = new GhostView(this, o);
          default -> v = null;
        }
        this.objects.clear(); // clear field from all object first to stop overlapping
        this.objects.add(v);
      } else {
        this.objects.clear();
      }
      this.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          maze.getPacman().setGoToField(model);
        }
      });
    } else {
      Color wallBackground = new Color(155, 93, 229);
      this.setBackground(wallBackground);
    }
    Color borderColor = new Color(35, 25, 66);
    this.setBorder(BorderFactory.createLineBorder(borderColor));
  }

  public final void update(Observable field) {
    ++this.changedModel;
    this.privUpdate();
  }

  public int numberUpdates() {
    return this.changedModel;
  }

  public void clearChanged() {
    this.changedModel = 0;
  }

  public CommonField getField() {
    return this.model;
  }
}

