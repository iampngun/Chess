package code;

import javax.swing.*;
import java.awt.*;

class ButtonManager {
    enum ButtonPanelType {MENU, GAME, ABOUT}
    static boolean isEnd = false;

    JButton Play = new JButton();
    JButton About = new JButton();
    JButton Quit = new JButton();
    JButton goMenu = new JButton();
    JButton Restart = new JButton();
    JButton Cancel = new JButton();
    JButton Menu = new JButton();
    private JLabel TextAboutProgram = new JLabel("<html>ИСП-31<br>Черняк Д.С.<br><br>penguin#5302<html>");
    private static JLabel WhoseMove = new JLabel("Ход белых");
    private static JLabel WhoWin = new JLabel("<html><html>");

    static void changeTeam(Figure.FigureTeam figureTeam) {
        if(figureTeam == Figure.FigureTeam.WHITE) {
            WhoseMove.setText("Ход чёрных");
        }
        else {
            WhoseMove.setText("Ход белых");
        }
    }

    static void endGame(Figure.FigureTeam figureTeam) {
        if(figureTeam == Figure.FigureTeam.WHITE) {
            WhoWin.setText("<html>Команда чёрного цвета одержала победу!<html>");
        }
        else {
            WhoWin.setText("<html>Команда белого цвета одержала победу!<html>");
        }
        if(figureTeam == Figure.FigureTeam.EMPTY) {
            WhoWin.setText("<html><html>");
        }
        isEnd = true;
    }

    void addButtons(JLayeredPane layeredPane, ButtonPanelType Type) {
        switch (Type) {
            case MENU:
                addButton(layeredPane, Play, 220, 100, 200, "play");
                addButton(layeredPane, About, 220, 180, 200, "about");
                addButton(layeredPane, Quit, 220, 260, 200, "quit");
                break;
            case GAME:
                addButton(layeredPane, goMenu, 480, 0, 160, "menu");
                addButton(layeredPane, Restart, 480, 71, 160, "restart");
                addButton(layeredPane, Cancel, 480, 142, 160, "cancel");
                addLabel(layeredPane, WhoseMove, 505, 213, 135, 70, 20, Color.BLACK);
                addLabel(layeredPane, WhoWin, 505, 284, 135, 80, 15, Color.BLACK);
                break;
            case ABOUT:
                addButton(layeredPane, Menu, 415, 30, 200, "menu");
                addLabel(layeredPane, TextAboutProgram, 30, -60, 500, 300, 20, Color.WHITE);
                break;
        }
    }

    private void addButton(JLayeredPane layeredPane, JButton button, int x, int y, int width, String name) {
        layeredPane.add(button, new Integer(100));
        button.setBounds(x, y, width, 70);
        button.setIcon(new ImageIcon("src/image/"+ name + ".jpeg"));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void addLabel(JLayeredPane layeredPane, JLabel label, int x, int y, int width, int height, int size, Color color) {
        layeredPane.add(label, new Integer(100));
        label.setBounds(x, y, width, height);
        label.setForeground(color);
        label.setFont(new Font("Tahoma", Font.BOLD, size));
    }
}