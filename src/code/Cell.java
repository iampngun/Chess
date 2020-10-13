package code;

import javax.swing.*;
import java.awt.*;

class Cell {
    int xPosition;
    int yPosition;

    boolean moved = false;

    Figure figure = new Figure();

    private JLabel imageContainer;

    Cell(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    void drawFigure(JLayeredPane layeredPane) {
        //16+56*x
        imageContainer = new JLabel();
        setImage();
        layeredPane.add(imageContainer, new Integer(100));
        imageContainer.setBounds(16+56*xPosition, 16+56*yPosition, 56, 56);
    }

    void setImage() {
        String imageSrc = "src/image/" + figure.figureTeam.toString() + "_" + figure.figureType.toString() + ".png";
        ImageIcon image = new ImageIcon(new ImageIcon(imageSrc).getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT));
        imageContainer.setIcon(image);
    }
}