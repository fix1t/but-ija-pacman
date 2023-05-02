package ija.ija2022.homework2.tool.view;

import ija.ija2022.homework2.tool.common.CommonMazeObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GhostView implements ComponentView {
  private final CommonMazeObject model;
  private final FieldView parent;
  Image image;

  public GhostView(FieldView parent, CommonMazeObject m) {
    this.model = m;
    this.parent = parent;
    image = new ImageIcon(getClass().getResource("GhostRed.png")).getImage();
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    // Get the dimensions of the parent field
    int fieldWidth = parent.getWidth();
    int fieldHeight = parent.getHeight();

    // Scale the image to fit the field size
    double imageWidth = image.getWidth(null);
    double imageHeight = image.getHeight(null);
    double scale = Math.min(fieldWidth / imageWidth, fieldHeight / imageHeight);
    int scaledWidth = (int) (imageWidth * scale);
    int scaledHeight = (int) (imageHeight * scale);

    // Draw the scaled image at the center of the field
    int x = (fieldWidth - scaledWidth) / 2;
    int y = (fieldHeight - scaledHeight) / 2;
    g2.drawImage(image, x, y, scaledWidth, scaledHeight, null);
  }
}

