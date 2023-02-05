package checkers.model;

public class Move {
    private final int iOfCurrentCell;
    private final int jOfCurrentCell;
    private final int iOfAvailableCellToMove;
    private final int jOfAvailableCellToMove;
    private final int color;
    private final boolean isKiller;

    public Move(int iOfCurrentCell, int jOfCurrentCell, int iOfAvailableCellToMove, int jOfAvailableCellToMove, int color,
                boolean isKiller) {
        this.iOfCurrentCell = iOfCurrentCell;
        this.jOfCurrentCell = jOfCurrentCell;
        this.iOfAvailableCellToMove = iOfAvailableCellToMove;
        this.jOfAvailableCellToMove = jOfAvailableCellToMove;
        this.color = color;
        this.isKiller = isKiller;
    }

    public int getIOfAvailableCellToMove() {
        return iOfAvailableCellToMove;
    }

    public int getIOfCurrentCell() {
        return iOfCurrentCell;
    }

    public int getJOfAvailableCellToMove() {
        return jOfAvailableCellToMove;
    }

    public int getJOfCurrentCell() {
        return jOfCurrentCell;
    }

    public int getColor() {
        return color;
    }

    public boolean isKiller() {
        return isKiller;
    }
}