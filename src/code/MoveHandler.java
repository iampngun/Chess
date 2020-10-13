package code;

import javax.swing.*;
import java.awt.*;

class MoveHandler {
    Cell focusedCell;
    private Cell passantCell;

    static boolean isMoved = false;

    static boolean didCastling = false;

    static boolean isEnPassant = false;
    static boolean isPassant = false;

    static int changingX;
    static int changingY;

    static String WhoseMove = "WHITE";
    static boolean isFigureChanging = false;

    static JLabel changeToBishop = new JLabel();
    static JLabel changeToKnight = new JLabel();
    static JLabel changeToRook = new JLabel();
    static JLabel changeToQueen = new JLabel();

    private static String teamOfChangingFigure = "";

    MoveHandler() {

    }

    void handleMove(Cell cell, Cell[][] cells) {
        if(cell.figure.figureTeam == Figure.FigureTeam.EMPTY) {
            if(focusedCell != null) {
                if (focusedCell.figure.figureTeam != Figure.FigureTeam.EMPTY) {
                    if(!isCastling(focusedCell, cell, cells)) {
                        if(isMoveAllowed(focusedCell, cell, cells)) {
                            HistoryManager.writeToHistory(focusedCell, cell, cells);
                            ButtonManager.changeTeam(focusedCell.figure.figureTeam);
                            doMove(focusedCell, cell, cells);
                            defocusFromFigure();
                        }
                    }
                    else {
                        HistoryManager.writeToHistory(focusedCell, cell, cells);
                        ButtonManager.changeTeam(focusedCell.figure.figureTeam);
                        doCastling(focusedCell, cell, cells);
                        defocusFromFigure();
                    }
                }
            }
        }
        else {
            if(WhoseMove.equals(cell.figure.figureTeam.toString())) {
                defocusFromFigure();
                focusOnFigure(cell);
            }
            else {
                if(focusedCell != null) {
                    if(focusedCell.figure.figureTeam != Figure.FigureTeam.EMPTY) {
                        if (isMoveAllowed(focusedCell, cell, cells)) {
                            HistoryManager.writeToHistory(focusedCell, cell, cells);
                            ButtonManager.changeTeam(focusedCell.figure.figureTeam);
                            doMove(focusedCell, cell, cells);
                            defocusFromFigure();
                        }
                    }
                }
            }
        }
    }

    private boolean isCastling(Cell focusedCell, Cell cell, Cell[][] cells) {
        boolean allowed = false;

        int y;
        if(focusedCell.figure.figureTeam == Figure.FigureTeam.BLACK) {
            y = 0;
        }
        else {
            y = 7;
        }

        if(focusedCell.figure.figureType == Figure.FigureType.KING && Math.abs(focusedCell.xPosition - cell.xPosition) == 2) {
            if(cell.xPosition > focusedCell.xPosition) {
                if(cells[cell.xPosition+1][y].figure.figureType == Figure.FigureType.ROOK) {
                    if(!focusedCell.moved && !cells[cell.xPosition+1][y].moved) {
                        if(isPathClear(focusedCell, cells[cell.xPosition+1][y], cells)) {
                            allowed = true;
                            didCastling = true;
                        }
                    }
                }
            }
            else {
                if(cells[cell.xPosition-2][y].figure.figureType == Figure.FigureType.ROOK) {
                    if(!focusedCell.moved && !cells[cell.xPosition-2][y].moved) {
                        if(isPathClear(focusedCell, cells[cell.xPosition-2][y], cells)) {
                            allowed = true;
                            didCastling = true;
                        }
                    }
                }
            }
        }

        return allowed;
    }

    private void doCastling(Cell focusedCell, Cell cell, Cell[][] cells) {
        if(WhoseMove.equals("WHITE")) {
            WhoseMove = "BLACK";
        }
        else {
            WhoseMove = "WHITE";
        }

        cell.figure.figureTeam = focusedCell.figure.figureTeam;
        cell.figure.figureType = focusedCell.figure.figureType;
        focusedCell.figure.figureTeam = Figure.FigureTeam.EMPTY;
        cell.setImage();
        focusedCell.setImage();

        if(cell.xPosition > focusedCell.xPosition) {
            cells[focusedCell.xPosition+1][focusedCell.yPosition].
                    figure.figureTeam = cells[cell.xPosition+1][cell.yPosition].figure.figureTeam;
            cells[focusedCell.xPosition+1][focusedCell.yPosition].
                    figure.figureType = cells[cell.xPosition+1][cell.yPosition].figure.figureType;
            cells[cell.xPosition+1][cell.yPosition].figure.figureTeam = Figure.FigureTeam.EMPTY;
            cells[focusedCell.xPosition+1][focusedCell.yPosition].setImage();
            cells[cell.xPosition+1][cell.yPosition].setImage();
        }
        else {
            cells[focusedCell.xPosition-1][focusedCell.yPosition].
                    figure.figureTeam = cells[cell.xPosition-2][cell.yPosition].figure.figureTeam;
            cells[focusedCell.xPosition-1][focusedCell.yPosition].
                    figure.figureType = cells[cell.xPosition-2][cell.yPosition].figure.figureType;
            cells[cell.xPosition-2][cell.yPosition].figure.figureTeam = Figure.FigureTeam.EMPTY;
            cells[focusedCell.xPosition-1][focusedCell.yPosition].setImage();
            cells[cell.xPosition-2][cell.yPosition].setImage();
        }
    }

    private void focusOnFigure(Cell cell) {
        focusedCell = cell;
        PlayingField.imageContainer.setBounds(16+56*cell.xPosition, 16+56*cell.yPosition, 56, 56);
    }

    private void defocusFromFigure() {
        focusedCell = null;
        PlayingField.imageContainer.setBounds(-1, -1, 1, 1);
    }

    private boolean isMoveAllowed(Cell focusedCell, Cell cell, Cell[][] cells) {
        boolean allowed = false;
        switch (focusedCell.figure.figureType) {
            case PAWN:
                if(focusedCell.figure.figureTeam == Figure.FigureTeam.WHITE) {
                    if(focusedCell.yPosition == 6 && cell.yPosition == 4) {
                        if(isPathClear(focusedCell, cell, cells)) {
                            allowed = true;
                            isEnPassant = true;
                            passantCell = cell;
                        }
                    }
                }
                else {
                    if(focusedCell.yPosition == 1 && cell.yPosition == 3) {
                        if(isPathClear(focusedCell, cell, cells)) {
                            allowed = true;
                            isEnPassant = true;
                            passantCell = cell;
                        }
                    }
                }
                if(focusedCell.figure.figureTeam == Figure.FigureTeam.WHITE) {
                    if(focusedCell.xPosition == cell.xPosition && focusedCell.yPosition - cell.yPosition == 1
                            && cell.figure.figureTeam == Figure.FigureTeam.EMPTY) {
                        allowed = true;
                        isEnPassant = false;
                    }
                    if(Math.abs(focusedCell.xPosition - cell.xPosition) == 1 && focusedCell.yPosition - cell.yPosition == 1
                            && cell.figure.figureTeam == Figure.FigureTeam.BLACK) {
                        allowed = true;
                        isEnPassant = false;
                    }
                    if(Math.abs(focusedCell.xPosition - cell.xPosition) == 1 && focusedCell.yPosition - cell.yPosition == 1
                            && cell.figure.figureTeam == Figure.FigureTeam.EMPTY
                            && cells[cell.xPosition][cell.yPosition+1].figure.figureTeam == Figure.FigureTeam.BLACK
                            && cells[cell.xPosition][cell.yPosition+1].figure.figureType == Figure.FigureType.PAWN && isEnPassant) {
                        if(cells[cell.xPosition][cell.yPosition+1] == passantCell) {
                            allowed = true;
                            isEnPassant = false;
                            isPassant = true;
                        }
                    }
                }
                else {
                    if(focusedCell.xPosition == cell.xPosition && cell.yPosition - focusedCell.yPosition == 1
                            && cell.figure.figureTeam == Figure.FigureTeam.EMPTY) {
                        allowed = true;
                        isEnPassant = false;
                    }
                    if(Math.abs(focusedCell.xPosition - cell.xPosition) == 1 && cell.yPosition - focusedCell.yPosition == 1
                            && cell.figure.figureTeam == Figure.FigureTeam.WHITE) {
                        allowed = true;
                        isEnPassant = false;
                    }
                    if(Math.abs(focusedCell.xPosition - cell.xPosition) == 1 && cell.yPosition - focusedCell.yPosition == 1
                            && cell.figure.figureTeam == Figure.FigureTeam.EMPTY
                            && cells[cell.xPosition][cell.yPosition-1].figure.figureTeam == Figure.FigureTeam.WHITE
                            && cells[cell.xPosition][cell.yPosition-1].figure.figureType == Figure.FigureType.PAWN && isEnPassant) {
                        if(cells[cell.xPosition][cell.yPosition-1] == passantCell) {
                            allowed = true;
                            isEnPassant = false;
                            isPassant = true;
                        }
                    }
                }
                break;
            case KING:
                if(Math.abs(focusedCell.xPosition - cell.xPosition) <= 1 && Math.abs(focusedCell.yPosition - cell.yPosition) <= 1) {
                    allowed = true;
                    isEnPassant = false;
                    if(!focusedCell.moved) {
                        focusedCell.moved = true;
                        isMoved = true;
                    }
                }
                break;
            case QUEEN:
                if(Math.abs(focusedCell.xPosition - cell.xPosition) == Math.abs(focusedCell.yPosition - cell.yPosition)) {
                    if(isPathClear(focusedCell, cell, cells)) {
                        allowed = true;
                        isEnPassant = false;
                    }
                }
                if(focusedCell.xPosition == cell.xPosition || focusedCell.yPosition == cell.yPosition) {
                    if(isPathClear(focusedCell, cell, cells)) {
                        allowed = true;
                        isEnPassant = false;
                    }
                }
                break;
            case BISHOP:
                if(Math.abs(focusedCell.xPosition - cell.xPosition) == Math.abs(focusedCell.yPosition - cell.yPosition)) {
                    if(isPathClear(focusedCell, cell, cells)) {
                        allowed = true;
                        isEnPassant = false;
                    }
                }
                break;
            case KNIGHT:
                if(Math.abs(focusedCell.xPosition - cell.xPosition) == 1 && Math.abs(focusedCell.yPosition - cell.yPosition) == 2) {
                    allowed = true;
                    isEnPassant = false;
                }
                if(Math.abs(focusedCell.xPosition - cell.xPosition) == 2 && Math.abs(focusedCell.yPosition - cell.yPosition) == 1) {
                    allowed = true;
                    isEnPassant = false;
                }
                break;
            case ROOK:
                if(focusedCell.xPosition == cell.xPosition || focusedCell.yPosition == cell.yPosition) {
                    if(isPathClear(focusedCell, cell, cells)) {
                        allowed = true;
                        isEnPassant = false;
                        if(!focusedCell.moved) {
                            focusedCell.moved = true;
                            isMoved = true;
                        }
                    }
                }
                break;
        }
        return allowed;
    }

    private boolean isPathClear(Cell focusedCell, Cell cell, Cell[][] cells) {
        int x = bringToNumber(focusedCell.xPosition, cell.xPosition);
        int y = bringToNumber(focusedCell.yPosition, cell.yPosition);

        boolean isClear = true;

        while(x != bringToNumber(x, cell.xPosition) || y != bringToNumber(y, cell.yPosition)) {
            if(cells[x][y].figure.figureTeam != Figure.FigureTeam.EMPTY) {
                isClear = false;
                break;
            }
            x = bringToNumber(x, cell.xPosition);
            y = bringToNumber(y, cell.yPosition);
        }
        return isClear;
    }

    private int bringToNumber(int a, int b) {
        if(a < b) {
            a = a + 1;
        }
        else if(a > b) {
            a = a - 1;
        }
        return a;
    }

    private void doMove(Cell focusedCell, Cell cell, Cell[][] cells) {
        if(WhoseMove.equals("WHITE")) {
            WhoseMove = "BLACK";
        }
        else {
            WhoseMove = "WHITE";
        }

        if(cell.figure.figureType == Figure.FigureType.KING) {
            endGame(cell);
        }

        cell.figure.figureTeam = focusedCell.figure.figureTeam;
        cell.figure.figureType = focusedCell.figure.figureType;
        focusedCell.figure.figureTeam = Figure.FigureTeam.EMPTY;
        if(isPassant) {
            if(cell.figure.figureTeam == Figure.FigureTeam.WHITE) {
                cells[cell.xPosition][cell.yPosition+1].figure.figureTeam = Figure.FigureTeam.EMPTY;
                cells[cell.xPosition][cell.yPosition+1].setImage();
            }
            else {
                cells[cell.xPosition][cell.yPosition-1].figure.figureTeam = Figure.FigureTeam.EMPTY;
                cells[cell.xPosition][cell.yPosition-1].setImage();
            }
            isPassant = false;
        }

        cell.setImage();
        focusedCell.setImage();

        if((cell.yPosition == 0 || cell.yPosition == 7) && cell.figure.figureType == Figure.FigureType.PAWN) {
            isFigureChanging = true;
            changingX = cell.xPosition;
            changingY = cell.yPosition;
            teamOfChangingFigure = cell.figure.figureTeam.toString();

            changeToBishop.setIcon(new ImageIcon(new
                    ImageIcon("src/image/" + MoveHandler.teamOfChangingFigure + "_BISHOP1.png")
                    .getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT)));
            changeToKnight.setIcon(new ImageIcon(new
                    ImageIcon("src/image/" + MoveHandler.teamOfChangingFigure + "_KNIGHT1.png")
                    .getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT)));
            changeToRook.setIcon(new ImageIcon(new
                    ImageIcon("src/image/" + MoveHandler.teamOfChangingFigure + "_ROOK1.png")
                    .getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT)));
            changeToQueen.setIcon(new ImageIcon(new
                    ImageIcon("src/image/" + MoveHandler.teamOfChangingFigure + "_QUEEN1.png")
                    .getImage().getScaledInstance(56, 56, Image.SCALE_DEFAULT)));

            changeToBishop.setBounds(16+56*2, 16+56*3, 56, 56);
            changeToKnight.setBounds(16+56*3, 16+56*3, 56, 56);
            changeToRook.setBounds(16+56*4, 16+56*3, 56, 56);
            changeToQueen.setBounds(16+56*5, 16+56*3, 56, 56);
        }
    }

    private void endGame(Cell cell) {
        ButtonManager.endGame(cell.figure.figureTeam);
    }
}