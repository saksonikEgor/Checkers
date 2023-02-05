package checkers.model;

import java.util.ArrayList;
import java.util.Arrays;

public class HistoricalBoard {
    private final Cell[][] board;
    private final ArrayList<int[]> mustKill;
    private final boolean gameStateIsFirstClick; //true - ожидается первое нажатие  false - ожидается второе нажатие
    private int iOfFirstCell;
    private int jOfFirstCell;

    public HistoricalBoard(Cell[][] board, boolean gameStateIsFirstClick, ArrayList<int[]> mustKill) {
        this.board = board;
        this.gameStateIsFirstClick = gameStateIsFirstClick;
        this.mustKill = mustKill;
    }

    public ArrayList<int[]> getMustKill() {
        return mustKill;
    }

    public boolean isGameStateIsFirstClick() {
        return gameStateIsFirstClick;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setFirstCell(int i, int j) {
        iOfFirstCell = i;
        jOfFirstCell = j;
    }

    public int[] getIndicesOfFirstCell() {
        int[] result = new int[2];

        result[0] = iOfFirstCell;
        result[1] = jOfFirstCell;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof HistoricalBoard historicalBoard))
            return false;

        for (int i = 0; i < this.board.length; i++)
            if (!Arrays.deepEquals(this.board[i], historicalBoard.board[i]))
                return false;


        if (this.mustKill.size() != historicalBoard.mustKill.size())
            return false;

        for (int i = 0; i < this.mustKill.size(); i++) {
            if (this.mustKill.get(i)[0] != historicalBoard.mustKill.get(i)[0] ||
                    this.mustKill.get(i)[1] != historicalBoard.mustKill.get(i)[1])
                return false;
        }

        if (this.gameStateIsFirstClick != historicalBoard.gameStateIsFirstClick)
            return false;

        return this.iOfFirstCell == historicalBoard.iOfFirstCell && this.jOfFirstCell == historicalBoard.jOfFirstCell;
    }
}