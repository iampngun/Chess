package code;

import javax.swing.*;
import java.awt.*;

class Frame extends JFrame {
    Frame(String title) {
        this.setTitle(title);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("src/image/windowIcon.png"));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Panel Panel = new Panel();
        this.setResizable(false);
        this.add(Panel);
        this.pack();
        this.setVisible(true);
    }
}