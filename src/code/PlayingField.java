package code;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class PlayingField implements MouseListener {
    private Cell[][] cells = new Cell[8][8];
    static JLabel imageContainer = new JLabel();
    private MoveHandler moveHandler = new MoveHandler();

    PlayingField() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                cells[x][y] = new Cell(x, y);
            }
        }

        Panel.layeredPane.addMouseListener(this);
    }

    void doRestart() {
        HistoryManager.history.clear();
        ButtonManager.endGame(Figure.FigureTeam.EMPTY);
        ButtonManager.isEnd = false;
        MoveHandler.isEnPassant = false;
        MoveHandler.isFigureChanging = false;
        ButtonManager.changeTeam(Figure.FigureTeam.BLACK);
        MoveHandler.WhoseMove = "WHITE";
        imageContainer.setBounds(-1, -1, 1, 1);
        hideFigureChanging();
        setStartPositions();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                cells[x][y].setImage();
                cells[x][y].moved = false;
            }
        }
    }

    private void hideFigureChanging() {
        MoveHandler.changeToBishop.setBounds(-1, -1, 1, 1);
        MoveHandler.changeToKnight.setBounds(-1, -1, 1, 1);
        MoveHandler.changeToRook.setBounds(-1, -1, 1, 1);
        MoveHandler.changeToQueen.setBounds(-1, -1, 1, 1);
    }

    void doBegin() {
        HistoryManager.history.clear();
        ButtonManager.endGame(Figure.FigureTeam.EMPTY);
        ButtonManager.isEnd = false;
        MoveHandler.isEnPassant = false;
        MoveHandler.isFigureChanging = false;
        ButtonManager.changeTeam(Figure.FigureTeam.BLACK);
        MoveHandler.WhoseMove = "WHITE";

        ImageIcon image = new ImageIcon(new ImageIcon("src/image/focus1.png")
                .getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT));
        imageContainer.setIcon(image);
        Panel.layeredPane.add(imageContainer, new Integer(100));
        imageContainer.setBounds(-1, -1, 1, 1);

        Panel.layeredPane.add(MoveHandler.changeToBishop, new Integer(150));
        Panel.layeredPane.add(MoveHandler.changeToKnight, new Integer(150));
        Panel.layeredPane.add(MoveHandler.changeToRook, new Integer(150));
        Panel.layeredPane.add(MoveHandler.changeToQueen, new Integer(150));

        hideFigureChanging();

        setStartPositions();

        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                cells[x][y].drawFigure(Panel.layeredPane);
                cells[x][y].moved = false;
            }
        }
    }

    private void setStartPositions() {
        for(int y = 2; y < 6; y++) {
            for(int x = 0; x < 8; x++) {
                cells[x][y].figure.figureTeam = Figure.FigureTeam.EMPTY;
            }
        }
        for(int x = 0; x < 8; x++) {
            cells[x][1].figure.figureTeam = Figure.FigureTeam.BLACK;
            cells[x][1].figure.figureType = Figure.FigureType.PAWN;
        }
        for(int x = 0; x < 8; x++) {
            cells[x][6].figure.figureTeam = Figure.FigureTeam.WHITE;
            cells[x][6].figure.figureType = Figure.FigureType.PAWN;
        }
        cells[0][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[0][0].figure.figureType = Figure.FigureType.ROOK;
        cells[7][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[7][0].figure.figureType = Figure.FigureType.ROOK;
        cells[0][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[0][7].figure.figureType = Figure.FigureType.ROOK;
        cells[7][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[7][7].figure.figureType = Figure.FigureType.ROOK;

        cells[1][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[1][0].figure.figureType = Figure.FigureType.KNIGHT;
        cells[6][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[6][0].figure.figureType = Figure.FigureType.KNIGHT;
        cells[1][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[1][7].figure.figureType = Figure.FigureType.KNIGHT;
        cells[6][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[6][7].figure.figureType = Figure.FigureType.KNIGHT;

        cells[2][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[2][0].figure.figureType = Figure.FigureType.BISHOP;
        cells[5][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[5][0].figure.figureType = Figure.FigureType.BISHOP;
        cells[2][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[2][7].figure.figureType = Figure.FigureType.BISHOP;
        cells[5][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[5][7].figure.figureType = Figure.FigureType.BISHOP;

        cells[3][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[3][0].figure.figureType = Figure.FigureType.QUEEN;
        cells[3][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[3][7].figure.figureType = Figure.FigureType.QUEEN;

        cells[4][0].figure.figureTeam = Figure.FigureTeam.BLACK;
        cells[4][0].figure.figureType = Figure.FigureType.KING;
        cells[4][7].figure.figureTeam = Figure.FigureTeam.WHITE;
        cells[4][7].figure.figureType = Figure.FigureType.KING;
    }

    void cancelMove() {
        ButtonManager.endGame(Figure.FigureTeam.EMPTY);
        ButtonManager.isEnd = false;
        if(!MoveHandler.isFigureChanging && HistoryManager.history.size() > 0) {
            String history = HistoryManager.readFromHistory();

            moveHandler.focusedCell = null;

            if(history.charAt(8) == '1') {
                MoveHandler.isEnPassant = true;
            }
            if(history.charAt(9) != 'a') {
                cells[Character.getNumericValue(history.charAt(2))][Character.getNumericValue(history.charAt(9))].
                        figure.figureTeam = HistoryManager.figureTeams[Character.getNumericValue(history.charAt(10))];
                cells[Character.getNumericValue(history.charAt(2))][Character.getNumericValue(history.charAt(9))].
                        figure.figureType = HistoryManager.figureTypes[Character.getNumericValue(history.charAt(11))];
                cells[Character.getNumericValue(history.charAt(2))][Character.getNumericValue(history.charAt(9))].setImage();
                MoveHandler.isEnPassant = true;
            }
            if(history.charAt(12) == '1') {
                cells[Character.getNumericValue(history.charAt(0))][Character.getNumericValue(history.charAt(1))].moved = false;
            }
            if(history.charAt(13) != 'a') {
                cells[Character.getNumericValue(history.charAt(13))][Character.getNumericValue(history.charAt(1))].
                        figure.figureTeam = HistoryManager.figureTeams[Character.getNumericValue(history.charAt(14))];
                cells[Character.getNumericValue(history.charAt(13))][Character.getNumericValue(history.charAt(1))].
                        figure.figureType = HistoryManager.figureTypes[Character.getNumericValue(history.charAt(15))];
                cells[Character.getNumericValue(history.charAt(13))][Character.getNumericValue(history.charAt(1))].setImage();

                cells[Character.getNumericValue(history.charAt(16))][Character.getNumericValue(history.charAt(1))].
                        figure.figureTeam = Figure.FigureTeam.EMPTY;
                cells[Character.getNumericValue(history.charAt(16))][Character.getNumericValue(history.charAt(1))].setImage();
            }

            int x = Character.getNumericValue(history.charAt(0));
            int y = Character.getNumericValue(history.charAt(1));
            int a = Character.getNumericValue(history.charAt(4));
            cells[x][y].figure.figureTeam = HistoryManager.figureTeams[a];
            a = Character.getNumericValue(history.charAt(5));
            cells[x][y].figure.figureType = HistoryManager.figureTypes[a];
            cells[x][y].setImage();

            if(cells[x][y].figure.figureTeam == Figure.FigureTeam.WHITE) {
                ButtonManager.changeTeam(Figure.FigureTeam.BLACK);
            }
            else {
                ButtonManager.changeTeam(Figure.FigureTeam.WHITE);
            }
            MoveHandler.WhoseMove = cells[x][y].figure.figureTeam.toString();

            x = Character.getNumericValue(history.charAt(2));
            y = Character.getNumericValue(history.charAt(3));
            a = Character.getNumericValue(history.charAt(6));
            cells[x][y].figure.figureTeam = HistoryManager.figureTeams[a];
            a = Character.getNumericValue(history.charAt(7));
            cells[x][y].figure.figureType = HistoryManager.figureTypes[a];
            cells[x][y].setImage();

            HistoryManager.deleteFromHistory();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = (e.getX()-16)/56;
        int mouseY = (e.getY()-16)/56;
        if(e.getY() >= 16 && e.getX() >= 16) {
            mouseX = Math.round(mouseX);
            mouseY = Math.round(mouseY);
            if(mouseX <= 7 && mouseX >= 0 && mouseY <= 7 && mouseY >= 0 && !ButtonManager.isEnd) {
                if(!MoveHandler.isFigureChanging) {
                    moveHandler.handleMove(cells[mouseX][mouseY], cells);
                }
                else {
                    if(mouseY == 3) {
                       switch (mouseX) {
                           case 2:
                               cells[MoveHandler.changingX][MoveHandler.changingY].figure.figureType = Figure.FigureType.BISHOP;
                               cells[MoveHandler.changingX][MoveHandler.changingY].setImage();
                               MoveHandler.isFigureChanging = false;
                               hideFigureChanging();
                               break;
                           case 3:
                               cells[MoveHandler.changingX][MoveHandler.changingY].figure.figureType = Figure.FigureType.KNIGHT;
                               cells[MoveHandler.changingX][MoveHandler.changingY].setImage();
                               MoveHandler.isFigureChanging = false;
                               hideFigureChanging();
                               break;
                           case 4:
                               cells[MoveHandler.changingX][MoveHandler.changingY].figure.figureType = Figure.FigureType.ROOK;
                               cells[MoveHandler.changingX][MoveHandler.changingY].setImage();
                               MoveHandler.isFigureChanging = false;
                               hideFigureChanging();
                               break;
                           case 5:
                               cells[MoveHandler.changingX][MoveHandler.changingY].figure.figureType = Figure.FigureType.QUEEN;
                               cells[MoveHandler.changingX][MoveHandler.changingY].setImage();
                               MoveHandler.isFigureChanging = false;
                               hideFigureChanging();
                               break;
                       }
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}