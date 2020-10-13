package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class HistoryManager {
    static List<String> history = new ArrayList<>();
    static Figure.FigureTeam[] figureTeams = new Figure.FigureTeam[3];
    static Figure.FigureType[] figureTypes = new Figure.FigureType[7];

    static {
        figureTeams[0] = Figure.FigureTeam.EMPTY;
        figureTeams[1] = Figure.FigureTeam.WHITE;
        figureTeams[2] = Figure.FigureTeam.BLACK;

        figureTypes[0] = Figure.FigureType.EMPTY;
        figureTypes[1] = Figure.FigureType.PAWN;
        figureTypes[2] = Figure.FigureType.KING;
        figureTypes[3] = Figure.FigureType.QUEEN;
        figureTypes[4] = Figure.FigureType.BISHOP;
        figureTypes[5] = Figure.FigureType.KNIGHT;
        figureTypes[6] = Figure.FigureType.ROOK;
    }

    static void writeToHistory(Cell focusedCell, Cell cell, Cell[][] cells) {
        String move = String.valueOf(focusedCell.xPosition);
        move += String.valueOf(focusedCell.yPosition);
        move += String.valueOf(cell.xPosition);
        move += String.valueOf(cell.yPosition);
        move = writeTeamAndType(focusedCell, move);
        move = writeTeamAndType(cell, move);
        if(MoveHandler.isEnPassant) {
            move += String.valueOf(1);
        }
        else {
            move += String.valueOf(0);
        }
        if(MoveHandler.isPassant) {
            if(focusedCell.figure.figureTeam == Figure.FigureTeam.WHITE) {
                move += String.valueOf(cell.yPosition+1);
                move = writeTeamAndType(cells[cell.xPosition][cell.yPosition+1], move);
            }
            else {
                move += String.valueOf(cell.yPosition-1);
                move = writeTeamAndType(cells[cell.xPosition][cell.yPosition-1], move);
            }
        }
        else {
            move += "aaa";
        }
        if(MoveHandler.isMoved) {
            move += String.valueOf(1);
            MoveHandler.isMoved = false;
        }
        else {
            move += "a";
        }
        if(MoveHandler.didCastling) {
            if(cell.xPosition > focusedCell.xPosition) {
                move += String.valueOf(cell.xPosition+1);
                move = writeTeamAndType(cells[cell.xPosition+1][cell.yPosition], move);
                move += String.valueOf(focusedCell.xPosition+1);
            }
            else {
                move += String.valueOf(cell.xPosition-2);
                move = writeTeamAndType(cells[cell.xPosition-2][cell.yPosition], move);
                move += String.valueOf(focusedCell.xPosition-1);
            }

            MoveHandler.didCastling = false;
        }
        else {
            move += "aaaa";
        }

        history.add(move);
    }

    static void deleteFromHistory() {
        history.remove(history.size() - 1);
    }

    static String readFromHistory() {
        return String.valueOf(history.get(history.size()-1));
    }

    private static String writeTeamAndType(Cell cell, String move) {
        move += String.valueOf(Arrays.asList(figureTeams).indexOf(cell.figure.figureTeam));
        move += String.valueOf(Arrays.asList(figureTypes).indexOf(cell.figure.figureType));
        return move;
    }

}