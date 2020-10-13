package code;

import javax.swing.*;
import java.awt.*;

class Panel extends JPanel {
    private ButtonManager buttonManager = new ButtonManager();
    static JLayeredPane layeredPane = new JLayeredPane();
    private PlayingField playingField;

    Panel() {
        super();

        setFocusable(true);
        requestFocus();

        goToMenu();

        buttonManager.Play.addActionListener(e -> goToGame());
        buttonManager.goMenu.addActionListener(e -> goToMenu());
        buttonManager.Menu.addActionListener(e -> goToMenu());
        buttonManager.About.addActionListener(e -> goToAbout());
        buttonManager.Quit.addActionListener(e -> System.exit(0));
        buttonManager.Restart.addActionListener(e -> playingField.doRestart());
        buttonManager.Cancel.addActionListener(e -> playingField.cancelMove());
    }

    private void goToMenu() {
        layeredPane.removeMouseListener(playingField);
        setLayeredPane("src/image/mainMenu.png", ButtonManager.ButtonPanelType.MENU, 640);
    }

    private void goToAbout() {
        setLayeredPane("src/image/mainMenu.png", ButtonManager.ButtonPanelType.ABOUT, 640);
    }

    private void goToGame() {
        setLayeredPane("src/image/stol1.png", ButtonManager.ButtonPanelType.GAME, 480);
        playingField = new PlayingField();
        playingField.doBegin();
    }

    private void setLayeredPane(String backImageSrc, ButtonManager.ButtonPanelType Type, int imageWidth) {
        repaint();
        layeredPane.removeAll();
        removeAll();
        JLabel imageContainer = new JLabel();
        ImageIcon image = new ImageIcon(new ImageIcon(backImageSrc).getImage().getScaledInstance(imageWidth, 480, Image.SCALE_DEFAULT));
        imageContainer.setIcon(image);
        layeredPane.setPreferredSize(new Dimension(640, 480));
        layeredPane.add(imageContainer, new Integer(50));
        buttonManager.addButtons(layeredPane, Type);
        this.add(layeredPane);
        imageContainer.setBounds( 0, 0, imageWidth, 480);
    }
}