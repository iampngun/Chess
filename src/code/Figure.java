package code;

class Figure {
    enum FigureTeam {EMPTY, WHITE, BLACK}
    enum FigureType {EMPTY, PAWN, KING, QUEEN, BISHOP, KNIGHT, ROOK}

    FigureTeam figureTeam = FigureTeam.EMPTY;
    FigureType figureType = FigureType.EMPTY;
}