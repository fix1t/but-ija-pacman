package src.tool;

import src.game.resources.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MazeMenu class represents a menu interface for a maze game.
 * @author Jakub Mikyšek
 */
public class MazeMenu {
  private JFrame frame;
  private Sound sound;
  JButton soundButton;
  Font customFont;
  Font headerFont;
  GameState gameStatus;

  /**
   * Hash Table to store option Menu Flags to invoke GUI structure later
   */
  public Map<String, Boolean> flags = new HashMap<>();

  /**
   * List to store all menu buttons to remove MouseListeners, when you click on menu option
   */
  List<JButton> menuElements = new ArrayList<>();

  /**
   * Constructs a MazeMenu object.
   *
   * @param frame      the JFrame to display the menu
   * @param sound      the Sound object for audio playback
   * @param gameStatus the current game state
   */
  public MazeMenu(JFrame frame, Sound sound, GameState gameStatus) {
    this.frame = frame;
    this.sound = sound;
    this.gameStatus = gameStatus;
    initFlags();
    addFont();
  }

  /**
   * Opens the menu and initializes the menu interface.
   */
  public void open() {
    try {
      SwingUtilities.invokeAndWait(this::initializeInterface);
    } catch (InvocationTargetException | InterruptedException var2) {
      Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, (String) null, var2);
    }

  }

  /**
   * Visualize Menu
   */
  public void initializeInterface() {
    JPanel content = new JPanel();
    content.setBackground(new Color(67, 91, 251));
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS)); // set vertical BoxLayout

    // Add padding from top
    content.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0));

    JLabel heading = new JLabel(this.gameStatus.message());
    heading.setForeground(new Color(251, 227, 67)); // change color to red when mouse enters
    heading.setAlignmentX(Component.CENTER_ALIGNMENT); // center horizontally
    heading.setFont(headerFont);
    content.add(heading);

    if(this.gameStatus == GameState.TBD){
      content.add(Box.createRigidArea(new Dimension(0, 20))); // add some spacing between labels
      content.add(elementBody("Start game!", "gameFlag"));
      content.add(Box.createRigidArea(new Dimension(0, 10))); // add some spacing between labels
      content.add(elementBody("Choose a map!", "mapFlag"));
    }
    else {
      content.add(Box.createRigidArea(new Dimension(0, 20))); // add some spacing between labels
      content.add(elementBody("Play again!", "gameFlag"));
      content.add(Box.createRigidArea(new Dimension(0, 10))); // add some spacing between labels
      content.add(elementBody("Choose a map!", "mapFlag"));
      content.add(Box.createRigidArea(new Dimension(0, 10))); // add some spacing between labels
      content.add(elementBody("Replay", "replayFlag"));
    }
    content.add(Box.createRigidArea(new Dimension(0, 10))); // add some spacing between labels
    content.add(elementBody("Exit", "exitFlag"));

    final boolean[] soundOn = {sound.isPlaying()};
    ImageIcon soundOnIcon = new ImageIcon(getClass().getClassLoader().getResource("lib/iconSound.png"));
    ImageIcon soundOffIcon = new ImageIcon(getClass().getClassLoader().getResource("lib/iconNoSound.png"));
    soundButton = new JButton(soundOn[0] ? soundOnIcon : soundOffIcon);  // put icon depending on playback status (on/off)
    soundButton.setFocusable(false); // fix: ghost not moving by WASD

    soundButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        soundOn[0] = !soundOn[0];
        if (!soundOn[0]) {
          stopMusic();
          soundButton.setIcon(soundOffIcon);
        } else {
          playMusic();
          soundButton.setIcon(soundOnIcon);
        }
      }
    });

    frame.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
          case 'M', 'm' -> { soundOn[0] = !soundOn[0];
            // Update the music state and the icon of the sound button based on the sound state
            if (!soundOn[0]) {
              stopMusic();
              soundButton.setIcon(soundOffIcon);
            }
            else {
              playMusic();
              soundButton.setIcon(soundOnIcon);
            }}
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });

    content.add(Box.createVerticalGlue()); // Add vertical glue to push soundButton to the bottom
    // center sound button
    JPanel soundButtonPanel = new JPanel();
    soundButtonPanel.setOpaque(false);
    soundButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    soundButtonPanel.add(soundButton);
    content.add(soundButtonPanel);

    frame.getContentPane().add(content, BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Creates new menu option (button).
   *
   * @param text     text in button
   * @param flagName flag to switch
   * @return filled JButton Element for adding to frame
   */
  private JButton elementBody(String text, String flagName) {
    JButton menuElement = new JButton(text);
    menuElement.setAlignmentX(Component.CENTER_ALIGNMENT); // center horizontally
    menuElement.setFont(this.customFont);
    menuElement.setForeground(Color.BLACK);
    menuElement.setMaximumSize(new Dimension(250, 45)); // Set maximum width for all buttons
    menuElement.setFocusable(false); // fix: ghost not moving by WASD

    MouseListener mouseListener = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Store the flag value using the flag name
        flags.put(flagName, true);
        removeListeners();
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        menuElement.setForeground(Color.RED); // change color to red when mouse enters
      }

      @Override
      public void mouseExited(MouseEvent e) {
        menuElement.setForeground(Color.BLACK); // change color back to black when mouse exits
      }
    };
    menuElement.addMouseListener(mouseListener);
    menuElements.add(menuElement); // add the JLabel to the list of menu elements
    return menuElement;
  }

  /**
   * Remove MouseListeners from all the buttons.
   */
  private void removeListeners() {
    for (JButton menuElement : menuElements) {
      for (MouseListener listener : menuElement.getMouseListeners()) {
        menuElement.removeMouseListener(listener);
      }
    }
    for (MouseListener listener : soundButton.getMouseListeners()) {
      soundButton.removeMouseListener(listener);
    }
  }

  /**
   * Initialize flags with <code>false</code> value.
   */
  private void initFlags() {
    this.flags.put("gameFlag", false);
    this.flags.put("replayFlag", false);
    this.flags.put("exitFlag", false);
    this.flags.put("mapFlag", false);
  }

  /**
   * Shows if flag was switch.
   *
   * @return all flags bool values
   */
  public boolean menuElementPressed() {
    return this.flags.get("mapFlag") || this.flags.get("gameFlag") || this.flags.get("replayFlag") || this.flags.get("exitFlag");
  }

  /**
   * Loop through all flags and return the one with value <code>True</code>.
   *
   * @return menu option (flag) that was pressed
   */
  public String flagEnabled() {
    for (String flag : flags.keySet()) {
      if (flags.get(flag)) {
        return flag;
      }
    }
    return null;
  }

  /**
   * Starts playing music.
   */
  public void playMusic() {
    sound.play();
    sound.loop();
  }

  /**
   * Stops playing music.
   */
  public void stopMusic() {
    sound.stop();
  }

  /**
   * Add custom font
   */
  public void addFont() {
    try {
      customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("lib/RetroGaming.ttf"));
      customFont = customFont.deriveFont(24f);
      headerFont = customFont.deriveFont(54f);
    } catch (FontFormatException | IOException e) {
      // Handle font loading exception
      e.printStackTrace();
      // Fallback to a default font
      customFont = new Font("Arial", Font.BOLD, 24);
      headerFont = new Font("Arial", Font.BOLD, 54);
    }
  }
}
