package checkers.model;

import checkers.io.BoardWriter;

import java.util.*;
import java.util.function.ToIntBiFunction;

public class Board {
    private Cell[][] board;

    private int tern = 1;

    private final String gameMode; // vsPlayer или vsComputer
    private String gameResult;
    private final ArrayList<String> moveHistory = new ArrayList<>();
    private final ArrayList<HistoricalBoard> boardHistory = new ArrayList<>();
    private int numberOfMove = 1;

    private final BoardWriter boardWriter;

    private final int limitOfRecursion;

    private Cell firstCell;
    private Cell secondCell;
    private Cell[] mustKill;

    private boolean gameRestarted = false;

    public Board(String gameMode, BoardWriter boardWriter) {
        if (!(gameMode.equals("против компьютера") || gameMode.equals("против игрока")))
            throw new RuntimeException("Unexpected game mode");

        this.boardWriter = boardWriter;

        board = new Cell[15][];
        limitOfRecursion = 7;

        this.gameMode = gameMode;
    }

    public Cell[][] makeCopyOfBoard(Cell[][] currentBoard) {
        Cell[][] resultBoard = new Cell[15][];

        resultBoard[0] = new Cell[1];
        resultBoard[1] = new Cell[3];
        resultBoard[2] = new Cell[5];
        resultBoard[3] = new Cell[7];
        resultBoard[4] = new Cell[7];
        resultBoard[5] = new Cell[5];
        resultBoard[6] = new Cell[3];
        resultBoard[7] = new Cell[1];
        resultBoard[8] = new Cell[2];
        resultBoard[9] = new Cell[4];
        resultBoard[10] = new Cell[6];
        resultBoard[11] = new Cell[8];
        resultBoard[12] = new Cell[6];
        resultBoard[13] = new Cell[4];
        resultBoard[14] = new Cell[2];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                int[] diagonals = currentBoard[i][j].getDiagonals();

                resultBoard[i][j] = new Cell(diagonals[0], diagonals[1],
                        currentBoard[i][j].getName(), currentBoard[i][j].getColor());

                if (currentBoard[i][j].isQueen())
                    resultBoard[i][j].makeQueen();

                if (currentBoard[i][j].isDead())
                    resultBoard[i][j].makeDead();

                if (currentBoard[i][j].isLight())
                    resultBoard[i][j].lightOn();
            }
        }

        resultBoard[8][0] = resultBoard[3][0];
        resultBoard[8][1] = resultBoard[4][0];
        resultBoard[9][0] = resultBoard[2][0];
        resultBoard[9][1] = resultBoard[3][1];
        resultBoard[9][2] = resultBoard[4][1];
        resultBoard[9][3] = resultBoard[5][0];
        resultBoard[10][0] = resultBoard[1][0];
        resultBoard[10][1] = resultBoard[2][1];
        resultBoard[10][2] = resultBoard[3][2];
        resultBoard[10][3] = resultBoard[4][2];
        resultBoard[10][4] = resultBoard[5][1];
        resultBoard[10][5] = resultBoard[6][0];
        resultBoard[11][0] = resultBoard[0][0];
        resultBoard[11][1] = resultBoard[1][1];
        resultBoard[11][2] = resultBoard[2][2];
        resultBoard[11][3] = resultBoard[3][3];

        resultBoard[11][4] = resultBoard[4][3];
        resultBoard[11][5] = resultBoard[5][2];
        resultBoard[11][6] = resultBoard[6][1];
        resultBoard[11][7] = resultBoard[7][0];
        resultBoard[12][0] = resultBoard[1][2];
        resultBoard[12][1] = resultBoard[2][3];
        resultBoard[12][2] = resultBoard[3][4];
        resultBoard[12][3] = resultBoard[4][4];
        resultBoard[12][4] = resultBoard[5][3];
        resultBoard[12][5] = resultBoard[6][2];
        resultBoard[13][0] = resultBoard[2][4];
        resultBoard[13][1] = resultBoard[3][5];
        resultBoard[13][2] = resultBoard[4][5];
        resultBoard[13][3] = resultBoard[5][4];
        resultBoard[14][0] = resultBoard[3][6];
        resultBoard[14][1] = resultBoard[4][6];

        return resultBoard;
    }

    public void restartInit() {
        gameResult = "";

        moveHistory.clear();
        boardHistory.clear();

        numberOfMove = 1;
        gameRestarted = false;

        mustKill = null;

        defaultInit();
        lightAvailableCheckers();
    }

    public boolean isGameRestarted() {
        return gameRestarted;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void defaultInit() {
        board[0] = new Cell[1];
        board[1] = new Cell[3];
        board[2] = new Cell[5];
        board[3] = new Cell[7];
        board[4] = new Cell[7];
        board[5] = new Cell[5];
        board[6] = new Cell[3];
        board[7] = new Cell[1];
        board[8] = new Cell[2];
        board[9] = new Cell[4];
        board[10] = new Cell[6];
        board[11] = new Cell[8];
        board[12] = new Cell[6];
        board[13] = new Cell[4];
        board[14] = new Cell[2];

        board[0][0] = new Cell(0, 11, "a1", 1);
        board[1][0] = new Cell(1, 10, "c1", 1);
        board[1][1] = new Cell(1, 11, "b2", 1);
        board[1][2] = new Cell(1, 12, "a3", 1);
        board[2][0] = new Cell(2, 9, "e1", 1);
        board[2][1] = new Cell(2, 10, "d2", 1);
        board[2][2] = new Cell(2, 11, "c3", 1);
        board[2][3] = new Cell(2, 12, "b4", 0);
        board[2][4] = new Cell(2, 13, "a5", 0);
        board[3][0] = new Cell(3, 8, "g1", 1);
        board[3][1] = new Cell(3, 9, "f2", 1);
        board[3][2] = new Cell(3, 10, "e3", 1);
        board[3][3] = new Cell(3, 11, "d4", 0);
        board[3][4] = new Cell(3, 12, "c5", 0);
        board[3][5] = new Cell(3, 13, "b6", -1);
        board[3][6] = new Cell(3, 14, "a7", -1);

        board[4][0] = new Cell(4, 8, "h2", 1);
        board[4][1] = new Cell(4, 9, "g3", 1);
        board[4][2] = new Cell(4, 10, "f4", 0);
        board[4][3] = new Cell(4, 11, "e5", 0);
        board[4][4] = new Cell(4, 12, "d6", -1);
        board[4][5] = new Cell(4, 13, "c7", -1);
        board[4][6] = new Cell(4, 14, "b8", -1);
        board[5][0] = new Cell(5, 9, "h4", 0);
        board[5][1] = new Cell(5, 10, "g5", 0);
        board[5][2] = new Cell(5, 11, "f6", -1);
        board[5][3] = new Cell(5, 12, "e7", -1);
        board[5][4] = new Cell(5, 13, "d8", -1);
        board[6][0] = new Cell(6, 10, "h6", -1);
        board[6][1] = new Cell(6, 11, "g7", -1);
        board[6][2] = new Cell(6, 12, "f8", -1);
        board[7][0] = new Cell(7, 11, "h8", -1);

        board[8][0] = board[3][0];
        board[8][1] = board[4][0];
        board[9][0] = board[2][0];
        board[9][1] = board[3][1];
        board[9][2] = board[4][1];
        board[9][3] = board[5][0];
        board[10][0] = board[1][0];
        board[10][1] = board[2][1];
        board[10][2] = board[3][2];
        board[10][3] = board[4][2];
        board[10][4] = board[5][1];
        board[10][5] = board[6][0];
        board[11][0] = board[0][0];
        board[11][1] = board[1][1];
        board[11][2] = board[2][2];
        board[11][3] = board[3][3];

        board[11][4] = board[4][3];
        board[11][5] = board[5][2];
        board[11][6] = board[6][1];
        board[11][7] = board[7][0];
        board[12][0] = board[1][2];
        board[12][1] = board[2][3];
        board[12][2] = board[3][4];
        board[12][3] = board[4][4];
        board[12][4] = board[5][3];
        board[12][5] = board[6][2];
        board[13][0] = board[2][4];
        board[13][1] = board[3][5];
        board[13][2] = board[4][5];
        board[13][3] = board[5][4];
        board[14][0] = board[3][6];
        board[14][1] = board[4][6];

        tern = 1;
    }


    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public void setTern(int tern) {
        this.tern = tern;
    }

    public void setNumberOfMove(int numberOfMove) {
        this.numberOfMove = numberOfMove;
    }

    public void setFirstCell(Cell cell) {
        firstCell = cell;
    }

    public void setSecondCell(Cell cell) {
        secondCell = cell;
    }

    public void addInMoveHistory(String move) {
        moveHistory.add(move);
    }

    public void addInBoardHistory(HistoricalBoard historicalBoard) {
        boardHistory.add(historicalBoard);
    }


    public HistoricalBoard removeLastHistoricalBoard() {
        return boardHistory.remove(boardHistory.size() - 1);
    }

    public Cell[][] getBoard() {
        return board;
    }

    public String getMoveHistoryToString() {
        StringBuilder buf = new StringBuilder();

        buf.append("  WHITE     BLACK\n");

        boolean previousMoveBelongToBlack = false;
        boolean previousMoveBelongToWhite = false;
        for (String current : moveHistory) {
            String[] split = current.split(" ");
            String curString;

            if (split.length == 3) { // если ходят белые
                if (!previousMoveBelongToWhite) { // если белые сходили первый раз за ход
                    curString = "\n" + split[1] + ". " + split[2] + "     ";
                    previousMoveBelongToBlack = false;
                    previousMoveBelongToWhite = true;
                } else  // белыел побили еще раз за ход
                    curString = "\n" + "    " + split[2] + "     ";

            } else { // если ходят черные

                if (!previousMoveBelongToBlack) { // черные сходили в первый раз за ход
                    curString = split[1];
                    previousMoveBelongToBlack = true;
                    previousMoveBelongToWhite = false;

                } else  // черные побили еще раз за ход
                    curString = "\n" + "                    " + split[1];
            }
            buf.append(curString);
        }
        return buf.toString();
    }

    public ArrayList<String> getBoardHistoryToStringList() {
        ArrayList<String> res = new ArrayList<>();

        for (HistoricalBoard currentHistoricalBoard : boardHistory) {
            Cell[][] currentBoard = currentHistoricalBoard.getBoard();

            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j < currentBoard[i].length; j++) {
                    String curString;
                    int[] diagonals = currentBoard[i][j].getDiagonals();

                    if (currentBoard[i][j].getColor() == 0) {
                        curString = i + " " + j + " " + diagonals[0] + " " + diagonals[1]
                                + " " + currentBoard[i][j].getName() + " " + currentBoard[i][j].isLight();
                    } else {
                        curString = i + " " + j + " " + diagonals[0] + " " + diagonals[1]
                                + " " + currentBoard[i][j].getName() + " " + currentBoard[i][j].getColor()
                                + " " + currentBoard[i][j].isLight() + " " + currentBoard[i][j].isQueen()
                                + " " + currentBoard[i][j].isDead();

                    }
                    res.add(curString);
                }
            }
            ArrayList<int[]> currentMustKill = currentHistoricalBoard.getMustKill();

            for (int[] ints : currentMustKill) {
                String curString = ints[0] + " " + ints[1];
                res.add(curString);
            }
            res.add("gameStateIsFirstClick");

            boolean gameStateIsFirstClick = currentHistoricalBoard.isGameStateIsFirstClick();

            if (gameStateIsFirstClick) {
                res.add("true");
            } else {
                res.add("false");

                int[] ints = currentHistoricalBoard.getIndicesOfFirstCell();
                res.add(ints[0] + " " + ints[1]);
            }
        }
        return res;
    }

    public ArrayList<String> getMoveHistory() {
        return moveHistory;
    }

    public ArrayList<HistoricalBoard> getBoardHistory() {
        return boardHistory;
    }

    public String getGameMode() {
        return gameMode;
    }

    public int getTern() {
        return tern;
    }

    public int getNumberOfMove() {
        return numberOfMove;
    }

    private int[] getIndicesOfCurrentCellInBoard(Cell cell) {
        int[] result = new int[2];

        afterCycles:
        {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == cell) {
                        result[0] = i;
                        result[1] = j;

                        break afterCycles;
                    }
                }
            }
        }
        return result;
    }


    public HistoricalBoard comeBackButtonPressed() {
        if (boardHistory.size() == 1)
            return boardHistory.get(0);

        boardHistory.remove(boardHistory.size() - 1);
        HistoricalBoard historicalBoard = boardHistory.get(boardHistory.size() - 1);

        String currentMove = moveHistory.remove(moveHistory.size() - 1);
        board = makeCopyOfBoard(historicalBoard.getBoard());

        convertArrayListToMustKill(historicalBoard.getMustKill());

        if (!historicalBoard.isGameStateIsFirstClick()) {
            int[] indices = historicalBoard.getIndicesOfFirstCell();
            setFirstCell(board[indices[0]][indices[1]]);
        }

        String[] split = currentMove.split(" ");
        if (gameMode.equals("против игрока")) {
            if (split.length == 3)
                tern = 1;
            else
                tern = -1;
        } else {

            while (split.length == 2)  //пока ход черных
                split = moveHistory.remove(moveHistory.size() - 1).split(" ");

            tern = 1;
            numberOfMove = Integer.parseInt(split[1]);
        }
        return historicalBoard;
    }

    public boolean saveButtonPressed(String fileName) {
        boolean result;
        result = boardWriter.saveBatch(this, fileName);
        return result;
    }


    public boolean lightAfterFirstClick() {// подсвечивает все доступные ходы для данной шашки
        if (mustKill.length > 0) { // если есть шашки обязанные бить
            if (!List.of(mustKill).contains(firstCell)) { // если текушая шашка не такова
                System.out.println("существуют шашки бязанные бить и вы нажали не на такую или нажали на пустую клетку");
                return false;
            }

            for (Cell value : mustKill) { // погасить все шашки кроме текущей
                if (value != firstCell)
                    value.lightOff();

                else
                    value.lightOn(); // на случай если пользователь нажал еще раз на свою шашку
            }

            lightAvailableKills();// подсветить все клетки на которые текущая шашка может походить (бьет)
        } else { // если нету шашек обязанных бить
            if (firstCell.getColor() != tern) { // если текущая шашка не ваша или клетка пустая
                System.out.println("вы нажали не на свою шашку или нажали на пустую клетку");
                return false;
            }

            if (!firstCell.isLight()) { // если пользователь нажал на неподсвеченную шашку
                System.out.println("данная шашка не имеет возможности куда-либо сходить");
                return false;
            }

            lightOffAllCells(); // погасить все клетки кроме текущей
            firstCell.lightOn();

            lightAvailableMoves(); // подсветить все клетки на которые данная шашка может сходить
        }
        System.out.println("были подсвечены все доступные ходы для данной шашки");
        return true;
    }

    public void lightAvailableCheckers() {
        // подсвечивает шашки которые обязаны бить или которые могут сходить (если бьющих шашек нету)
        ArrayList<Cell> res = new ArrayList<>();

        boolean afterQueen = false;
        boolean waitingForQueen = false;

        int iCurQueen = 0;
        int jCurQueen = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length - 2; j++) {

                if (board[i][j].getColor() == tern && board[i][j + 1].getColor() == (-1) * tern
                        && board[i][j + 2].getColor() == 0) {// если ситуация: ты-враг-0

                    res.add(board[i][j]);
                    board[i][j].lightOn();

                    continue;
                }

                if (board[i][j].getColor() == 0 && board[i][j + 1].getColor() == (-1) * tern
                        && board[i][j + 2].getColor() == tern) {// если ситуация: 0-враг-ты

                    res.add(board[i][j + 2]);
                    board[i][j + 2].lightOn();

                    continue;
                }

                if (board[i][j].getColor() == tern && board[i][j].isQueen()
                        && board[i][j + 1].getColor() == 0) { // если ситуация: дамка-0

                    afterQueen = true;

                    iCurQueen = i;
                    jCurQueen = j;

                    continue;
                }

                if (board[i][j].getColor() == 0 && board[i][j + 1].getColor() == (-1) * tern
                        && board[i][j + 2].getColor() == 0) { // если ситуация: 0-враг-0

                    if (afterQueen) { // если была дамка
                        res.add(board[iCurQueen][jCurQueen]);
                        board[iCurQueen][jCurQueen].lightOn();
                        afterQueen = false;
                    }
                    waitingForQueen = true;

                    continue;
                }

                if ((board[i][j].getColor() != 0 && board[i][j + 1].getColor() != 0)
                        || (board[i][j].getColor() == tern || board[i][j + 1].getColor() == tern)) {
                    // если была сиутация: шашка-шашка или по диагонали встрелилась своя шашка

                    afterQueen = false;
                    waitingForQueen = false;

                    continue;
                }

                if (waitingForQueen) { // если ожидается дама

                    if (board[i][j].getColor() == (-1) * tern) { // если ожидалась дама и встретили врага

                        if (board[i][j + 1].getColor() == tern && board[i][j + 1].isQueen()) { // и за врагом стоит дама

                            res.add(board[i][j + 1]);
                            board[i][j + 1].lightOn(); // подсвечиваем даму

                            waitingForQueen = false;

                            continue;
                        }

                        if (board[i][j + 1].getColor() == 0 && board[i][j + 2].getColor() == tern
                                && board[i][j + 2].isQueen()) { // и за врагом 0-дама

                            res.add(board[i][j + 2]);
                            board[i][j + 2].lightOn();// подсвечиваем даму

                            waitingForQueen = false;
                        }

                    } else {
                        if (board[i][j].getColor() == tern && board[i][j].isQueen()) {
                            // если ситуация: ожидается дама и   дама

                            res.add(board[i][j]);
                            board[i][j].lightOn();// подсвечиваем даму

                            waitingForQueen = false;

                            continue;
                        }

                        if (board[i][j + 1].getColor() == tern && board[i][j + 1].isQueen()
                                && board[i][j].getColor() == 0) {
                            // если ситуация: ожидается дама и   0-дама

                            res.add(board[i][j + 1]);
                            board[i][j + 1].lightOn();// подсвечиваем даму

                            waitingForQueen = false;

                            continue;
                        }

                        if (board[i][j + 2].getColor() == tern && board[i][j + 2].isQueen()
                                && board[i][j + 1].getColor() == 0 && board[i][j].getColor() == 0) {
                            // если ситуация: ожидается дама и    0-0-дама

                            res.add(board[i][j + 2]);
                            board[i][j + 2].lightOn();// подсвечиваем даму

                            waitingForQueen = false;
                        }
                    }
                }
            }
            waitingForQueen = false;
            afterQueen = false;
        }


        boolean noOneIsLighted = true;

        if (res.isEmpty()) { // если нету шашек которые могуть срубить, тогда подсветить те, которые могут сходить

            for (Cell[] cells : board) {
                for (int j = 0; j < cells.length; j++) {
                    if (cells.length == 1) // если на диагонали стоит одна клетка
                        continue;

                    if (tern == 1 && j <= cells.length - 2 && cells[j].getColor() == tern
                            && cells[j + 1].getColor() == 0) { // если ход белых и шашка не дамка и ситуация: ты-0
                        cells[j].lightOn(); // подсветить ты
                        noOneIsLighted = false;

                        continue;
                    }

                    if (tern == -1 && j > 0 && cells[j].getColor() == tern
                            && cells[j - 1].getColor() == 0) { // если ход черных и шашка не дамка и ситуация: 0-ты
                        cells[j].lightOn(); // подсветить ты
                        noOneIsLighted = false;

                        continue;
                    }

                    if (cells[j].isQueen() && cells[j].getColor() == tern) { // если шашка дамка
                        if (j <= cells.length - 2 && cells[j + 1].getColor() == 0) { // если ситуация: дамка-0
                            cells[j].lightOn();// подсветить дамку
                            noOneIsLighted = false;

                            continue;
                        }

                        if (j > 0 && cells[j - 1].getColor() == 0) { // если ситуация: 0-дамка
                            cells[j].lightOn();// подсветить дамку
                            noOneIsLighted = false;
                        }
                    }
                }
            }
            if (noOneIsLighted) { // если никто не был подсвечен, то есть игрок не имеет возможности куда-либо сходить
                if (tern == 1)  // если ход белых
                    gameResult = "BLACK WON";
                else  // если ход черных
                    gameResult = "WHITES WON";

                gameRestarted = true;
                //restartGame();
            }
            System.out.println("нету шашек которые могут срубить, были подсвечены все шашки которые могут сходить");
        } else
            System.out.println("были подсвечены все шашки которые могут срубить");

        mustKill = res.toArray(new Cell[0]);
        boardHistory.add(new HistoricalBoard(makeCopyOfBoard(board), true, convertMustKillToArrayList()));
    }

    private boolean lightAvailableKills() { // подсвечивает все клетки на которые текущая шашка может сходить чтобы срубить
        boolean res = false;
        int[] diagonals = firstCell.getDiagonals();

        if (!firstCell.isQueen()) { // если текущая шашка не дамка
            for (int diagonal : diagonals) {
                int indexOfCell = List.of(board[diagonal]).indexOf(firstCell);

                if (indexOfCell <= board[diagonal].length - 3) {
                    if (board[diagonal][indexOfCell + 1].getColor() == (-1) * tern
                            && board[diagonal][indexOfCell + 2].getColor() == 0
                            && !board[diagonal][indexOfCell + 1].isDead()) {// если ситуация: cell-враг-0 и враг живой

                        board[diagonal][indexOfCell + 2].lightOn();// то подсветить 0
                        res = true;
                    }
                }

                if (indexOfCell >= 2) {
                    if (board[diagonal][indexOfCell - 2].getColor() == 0
                            && board[diagonal][indexOfCell - 1].getColor() == (-1) * tern
                            && !board[diagonal][indexOfCell - 1].isDead()) {// если ситуация: 0-враг-cell и враг живой

                        board[diagonal][indexOfCell - 2].lightOn();// то подсветить 0
                        res = true;
                    }
                }
            }
        } else { // если текущая шашка дамка
            for (int diagonal : diagonals) {
                int indexOfCell = List.of(board[diagonal]).indexOf(firstCell);

                int j = indexOfCell + 1;
                boolean enemyWasMet = false;

                while (j != board[diagonal].length) {// пробегаем по диагонали (подсвечиваем возможные ходы после cell)
                    if (board[diagonal][j].getColor() == tern) // если встретили свою шашку
                        break;

                    if (board[diagonal][j].getColor() == (-1) * tern) {// если встретили врага
                        if (enemyWasMet || board[diagonal][j].isDead())// если ранее уже встречали врага или враг мертвый
                            break;

                        enemyWasMet = true;

                        j++;
                        continue;
                    }

                    if (enemyWasMet && board[diagonal][j].getColor() == 0) {// ранее встретили врага и встретили 0
                        //подсвечиваем 0
                        board[diagonal][j].lightOn();
                        res = true;
                    }
                    j++;
                }

                if (indexOfCell >= 2) {
                    enemyWasMet = false;
                    j = indexOfCell - 1;
                    while (j >= 0) {// пробегаем по диагонали (подсвечиваем возможные ходы перед cell)
                        if (board[diagonal][j].getColor() == tern) // если встретили свою шашку
                            break;

                        if (board[diagonal][j].getColor() == (-1) * tern) {// если встретили врага
                            if (enemyWasMet || board[diagonal][j].isDead()) {// если ранее уже встретили врага или враг мертвый
                                break;
                            }
                            enemyWasMet = true;

                            j--;
                            continue;
                        }

                        if (enemyWasMet && board[diagonal][j].getColor() == 0) {// ранее встретили врага и встретили 0
                            //подсвечиваем 0
                            board[diagonal][j].lightOn();
                            res = true;
                        }
                        j--;
                    }
                }
            }
        }
        System.out.println("были подсвечены все клетки на которые вы можете сходить чтобы срубить");
        return res;
    }

    private boolean lightAvailableMoves() {
        //подсвечивает все клетки на которые может сходить cell, если нет доступных ходов то вернуть false иначе true
        boolean res = false;

        int[] diagonals = firstCell.getDiagonals();

        if (!firstCell.isQueen()) { // если текущая шашка не дамка
            if (firstCell.getColor() == 1) { // если текущая шашка белая
                for (int diagonal : diagonals) {
                    if (board[diagonal].length == 1)
                        continue;

                    int indexOfCell = List.of(board[diagonal]).indexOf(firstCell);

                    if (indexOfCell >= board[diagonal].length - 1)
                        continue;

                    if (board[diagonal][indexOfCell + 1].getColor() == 0) {// если ситуация: cell-0
                        board[diagonal][indexOfCell + 1].lightOn();// то подсветить 0
                        res = true;
                    }
                }
            } else { // если текущая шашка черная
                for (int diagonal : diagonals) {
                    if (board[diagonal].length == 1)
                        continue;

                    int indexOfCell = List.of(board[diagonal]).indexOf(firstCell);

                    if (indexOfCell == 0)
                        continue;

                    if (board[diagonal][indexOfCell - 1].getColor() == 0) {// если ситуация: 0-cell
                        board[diagonal][indexOfCell - 1].lightOn();// то подсветить 0
                        res = true;
                    }
                }
            }
        } else {// если текущая шашка дамка
            for (int diagonal : diagonals) {
                if (board[diagonal].length == 1)
                    continue;

                int indexOfCell = List.of(board[diagonal]).indexOf(firstCell);

                if (indexOfCell <= board[diagonal].length - 2) { // дамка не должна находиться наверху
                    for (int i = indexOfCell + 1; i < board[diagonal].length; i++) {// помечаем все ходы после cell
                        if (board[diagonal][i].getColor() != 0) // если встретили шашку
                            break;

                        if (board[diagonal][i].getColor() == 0) {// встретили 0
                            //подсвечиваем 0
                            board[diagonal][i].lightOn();
                            res = true;
                        }
                    }
                }

                if (indexOfCell >= 1) { // дамка не должна находиться внизу
                    for (int i = indexOfCell - 1; i >= 0; i--) {// помечаем все ходы перед cell
                        if (board[diagonal][i].getColor() != 0) // если встретили шашку
                            break;

                        if (board[diagonal][i].getColor() == 0) {// встретили 0
                            //подсвечиваем 0
                            board[diagonal][i].lightOn();
                            res = true;
                        }
                    }
                }
            }
        }
        System.out.println("были подсвечены все клетки на которые вы можете сходить");
        return res;
    }

    public boolean afterSecondClick() {
        if (secondCell.isLight()) { // если пользователь кликнул на подсвеченную клетку
            if (firstCell == secondCell) { // если новая клетка равна cell
                System.out.println("вы клекнули на ту же самую шашку");
                return false;
            }
            secondCell.changeCell(tern, firstCell.isQueen()); // ставим cell на новую клетку
            firstCell.changeCell(0, false); // очищаем cell

            if (tern == -1) {
                for (int i = moveHistory.size() - 1; i >= 0; i--) {
                    String[] split = moveHistory.get(i).split(" ");
                    if (split.length == 3) {
                        numberOfMove = Integer.parseInt(split[1]) + 1;
                        break;
                    }
                }
            }

            if (mustKill.length > 0) { // если шашка срубила
                int[] diagonalsOfCurCell = secondCell.getDiagonals();
                int[] diagonalsOfLightCell = firstCell.getDiagonals();

                int indexOfIdenticalInCurCell = List.of(diagonalsOfCurCell[0],
                        diagonalsOfCurCell[1]).indexOf(diagonalsOfLightCell[0]);

                int indexOfDifferentInCurCell;
                int indexOfDifferentInLightCell = 1;

                if (indexOfIdenticalInCurCell == -1) {
                    indexOfIdenticalInCurCell = List.of(diagonalsOfCurCell[0],
                            diagonalsOfCurCell[1]).indexOf(diagonalsOfLightCell[1]);
                    indexOfDifferentInLightCell = 0;
                }

                if (indexOfIdenticalInCurCell == 0)
                    indexOfDifferentInCurCell = 1;
                else
                    indexOfDifferentInCurCell = 0;

                int j;

                for (j = 0; j < board[diagonalsOfCurCell[indexOfIdenticalInCurCell]].length; j++) {
                    int[] curDiagonals = board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getDiagonals();

                    int firstDiagonal = curDiagonals[0];
                    int secondDiagonal = curDiagonals[1];

                    if (board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getColor() != (-1) * tern
                            || (board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getColor() == (-1) * tern
                            && board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].isDead()))
                        continue;

                    if (List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).contains(firstDiagonal)
                            && List.of(diagonalsOfLightCell[0], diagonalsOfLightCell[1]).contains(firstDiagonal)) {
                        // если firstDiagonal общая

                        if ((secondDiagonal > diagonalsOfCurCell[indexOfDifferentInCurCell]
                                && secondDiagonal < diagonalsOfLightCell[indexOfDifferentInLightCell])
                                || (secondDiagonal < diagonalsOfCurCell[indexOfDifferentInCurCell]
                                && secondDiagonal > diagonalsOfLightCell[indexOfDifferentInLightCell])) {
                            // если secondDiagonal находится между curCell и lightCell
                            break;
                        }
                    } else { // если secondDiagonal общая
                        if ((firstDiagonal > diagonalsOfCurCell[indexOfDifferentInCurCell]
                                && firstDiagonal < diagonalsOfLightCell[indexOfDifferentInLightCell])
                                || (firstDiagonal < diagonalsOfCurCell[indexOfDifferentInCurCell]
                                && firstDiagonal > diagonalsOfLightCell[indexOfDifferentInLightCell])) {
                            // если firstDiagonal находится между curCell и lightCell
                            break;
                        }
                    }
                }
                // сделать срубленную шашку врага мертвой, но не убирать ее с доски
                board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].makeDead();

                String curString;
                if (tern == 1)  // добавляем ход в историю ходов
                    curString = tern + " " + numberOfMove + " " + firstCell.getName() + "-" + secondCell.getName();
                else
                    curString = tern + " " + firstCell.getName() + "-" + secondCell.getName();

                moveHistory.add(curString);

                setFirstCell(secondCell);

                lightOffAllCells(); // погасить все клтеки кроме новой
                firstCell.lightOn();

                if (lightAvailableKills()) { // если еще есть клетки куда можно срубить, то
                    Cell[] updatedMustKill = new Cell[1];
                    updatedMustKill[0] = firstCell;
                    mustKill = updatedMustKill;

                    HistoricalBoard historicalBoard = new HistoricalBoard(makeCopyOfBoard(board),
                            false, convertMustKillToArrayList());

                    int[] indices = getIndicesOfCurrentCellInBoard(firstCell);
                    historicalBoard.setFirstCell(indices[0], indices[1]);

                    boardHistory.add(historicalBoard);
                    return false;
                }
                // если для данной шашки больше нету возможности срубить
                // тогда убрать все мертвые шашки и сделать все клетки живыми
                removeDeadAndMakeAllAlive();

                firstCell.lightOff(); // погасить новую cell
            } else { // если шашка сходила
                // то погасить все ранее подсвеченные клетки
                lightOffAllCells();

                String curString;
                if (tern == 1)  // добавляем ход в историю ходов
                    curString = tern + " " + numberOfMove + " " + firstCell.getName() + "-" + secondCell.getName();
                else
                    curString = tern + " " + firstCell.getName() + "-" + secondCell.getName();

                moveHistory.add(curString);
            }
            // если шашка дошла до конца, то сделать ее дамкой
            int[] diagonals = secondCell.getDiagonals();
            List<Integer> curList = List.of(diagonals[0], diagonals[1]);

            if (tern == 1) { // если шашка белая
                if ((curList.contains(4) && curList.contains(14))
                        || (curList.contains(5) && curList.contains(13))
                        || (curList.contains(6) && curList.contains(12))
                        || (curList.contains(7) && curList.contains(11)))
                    secondCell.makeQueen();
            } else { // если шашка черная
                if ((curList.contains(0) && curList.contains(11))
                        || (curList.contains(1) && curList.contains(10))
                        || (curList.contains(2) && curList.contains(9))
                        || (curList.contains(3) && curList.contains(8)))
                    secondCell.makeQueen();
            }
        } else { // если пользователь кликнул на не подсвеченную клетку
            if (secondCell.getColor() == tern) { // если шашка своя
                if (List.of(mustKill).contains(secondCell)) { // если эта другая шашка может кого-нибудь срубить
                    lightOffAllCells(); // то погасить все клетки кроме новой
                    secondCell.lightOn();

                    setFirstCell(secondCell);

                    lightAvailableKills(); // и подсветить все клетки на которые эта шашка может сходить чтобы срубить

                    System.out.println("вы кликнули на другую свою шашку, которая может рубить");
                    return false;
                }

                if (List.of(mustKill).contains(firstCell) && !List.of(mustKill).contains(secondCell)) {
                    // если старая шашка обязана рубить и новая шашка не обязана
                    System.out.println("вы кликнули на свою новую шашку, которая не может рубить, " +
                            "в то время как ваша старая шашка рубить обязана");
                    return false;
                }
                // если нету шашек обязанных рубить, значит предидущая шашка должна была походить
                Cell curCell = firstCell;
                setFirstCell(secondCell);

                if (lightAvailableMoves()) { // если новая шашка может куда-либо сходить
                    lightOffAllCells(); // погасить все клетки кроме новой
                    firstCell.lightOn();

                    lightAvailableMoves(); // и подсветить все возможные ходы для новой шашки

                    System.out.println("вы кликнули на другую свою шашку");
                } else { // если новая шашка не имеет возможности куда-либо сходить
                    setFirstCell(curCell);
                    System.out.println("вы кликнули на дуругю свою шашку, которая не имеет возможности куда-либо сходить");
                }
            } else
                System.out.println("ваша шашка не может походить на указанную клетку");

            return false;
        }
        tern = (-1) * tern;
        return true;
    }

    private void removeDeadAndMakeAllAlive() { // убрать и оживить всех мертвых
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isDead()) {
                    board[i][j].changeCell(0, false); // убрать мертвого
                    board[i][j].revive(); // сделать мертвого живым
                }
            }
        }
    }

    private void lightOffAllCells() { // погасить все клетки
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < board[i].length; j++)
                board[i][j].lightOff();
    }

    public void makeComputerMove() {
        Move[] availableMoves = checkAvailableKills(board, tern); // находим все возможные рубящие ходы

        if (availableMoves.length == 0) // если нету шашек обязанныех бить
            availableMoves = checkAvailableMoves(board, tern);// тогда находим все возможные хожы

        if (availableMoves.length == 0) {// если кто-то победил
            if (tern == 1)  // если ход белых
                gameResult = "BLACK WOM";
            else  // если ход черных
                gameResult = "WHITES WON";

            gameRestarted = true;
            return;
            //restartGame();
        }

        Cell[][] resultBoard = new Cell[15][];
        int maximum = -30000;
        int currentValue;
        int previousValue = -30000;

        String resultMove = "";


        for (Move move : availableMoves) { // находим наилучший ход среди возможных
            ArrayList<Cell[][]> boardsAfterMove = addExtractionInCurrentMoves(move, makeCopyOfBoard(board)); // делаем ход

            for (int i = 0; i < boardsAfterMove.size(); i++) {
                currentValue = calculateCombinations(previousValue, 0, boardsAfterMove.get(i), 1);

                if (currentValue > maximum) {
                    maximum = currentValue;
                    resultBoard = boardsAfterMove.get(i);
                    previousValue = currentValue;

                    resultMove = convertMoveToString(move, makeCopyOfBoard(board), "").get(i);
                }
            }
        }
        board = resultBoard;

        String[] split = resultMove.split("!");

        moveHistory.addAll(Arrays.asList(split));
        numberOfMove++;

        tern = 1;
    }

    private int calculateCombinations(int previousValue, int depthOfRecursion, Cell[][] currentBoard, int tern) {
        depthOfRecursion++;

        if (depthOfRecursion == limitOfRecursion)// если достигли конца рекурсии
            return calculateMovePrice(currentBoard); // то возвращаем ценность конечного хода

        Move[] availableMoves = checkAvailableKills(currentBoard, tern); // находим все возможные рубящие ходы

        if (availableMoves.length == 0) // если нету шашек обязанныех бить
            availableMoves = checkAvailableMoves(currentBoard, tern);// тогда находим все возможные хожы

        if (availableMoves.length == 0) // если кто-то победил
            return calculateMovePrice(currentBoard);


        ToIntBiFunction<Integer, Integer> function;

        int resultIfWrongWay;
        int value;

        if (tern == -1) {
            value = -30000;
            function = Integer::max;
        } else {
            value = 30000;
            function = Integer::min;
        }
        resultIfWrongWay = value * (-1);

        for (Move move : availableMoves) {
            ArrayList<Cell[][]> boardsAfterMove = addExtractionInCurrentMoves(move, makeCopyOfBoard(currentBoard)); // делаем ход

            for (Cell[][] cur : boardsAfterMove) {
                value = function.applyAsInt(
                        calculateCombinations(value, depthOfRecursion, cur, tern * (-1)), value);

                if (function.applyAsInt(previousValue, value) != previousValue)// понимаем что путь уже не может быть выгоднее
                    return resultIfWrongWay;
            }
        }
        return value;
    }

    private int calculateMovePrice(Cell[][] board) {
        int whitePawn = 0;
        int whiteQueen = 0;
        int blackPawn = 0;
        int blackQueen = 0;

        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.getColor() == 0)
                    continue;

                if (cell.getColor() == 1) {
                    if (cell.isQueen())
                        whiteQueen++;
                    else
                        whitePawn++;
                } else {
                    if (cell.isQueen())
                        blackQueen++;
                    else
                        blackPawn++;
                }
            }
        }
        return blackPawn * 100 + blackQueen * 300 - whitePawn * 100 - whiteQueen * 300;
    }

    private Move[] checkAvailableKills(Cell[][] board, int tern) {
        // проверяет доску на наличие бьющих шашек
        // возвращает массив с шашками которые обязаны бить
        ArrayList<Move> res = new ArrayList<>();

        boolean afterQueen = false;
        boolean waitingForQueen = false;
        int jOfEnemyIfWaitingForQueen = 0;

        int iCurQueen = 0;
        int jCurQueen = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length - 2; j++) {

                if (board[i][j].getColor() == tern && board[i][j + 1].getColor() == (-1) * tern
                        && board[i][j + 2].getColor() == 0) {// если ситуация: ты-враг-0

                    res.add(new Move(i, j, i, j + 2, tern, true));
                    int jOfCurrentCell = j;

                    if (board[i][j].isQueen()) { // если ты дамка, то добавить в массив все 0 за врагом
                        j++;
                        while (j + 2 < board[i].length) {
                            if (board[i][j + 2].getColor() == 0)
                                res.add(new Move(i, jOfCurrentCell, i, j + 2, tern, true));
                            else
                                break;
                            j++;
                        }
                    }
                    continue;
                }

                if (board[i][j].getColor() == 0 && board[i][j + 1].getColor() == (-1) * tern
                        && board[i][j + 2].getColor() == tern) {// если ситуация: 0-враг-ты

                    res.add(new Move(i, j + 2, i, j, tern, true));
                    int jOfCurrentCell = j + 2;

                    if (board[i][j].isQueen()) { // если ты дамка, то добавить в массив все 0 перед врагом
                        j--;
                        while (j >= 0) {
                            if (board[i][j].getColor() == 0)
                                res.add(new Move(i, jOfCurrentCell, i, j, tern, true));
                            else
                                break;
                            j--;
                        }
                    }
                    j = jOfCurrentCell;
                    continue;
                }

                if (board[i][j].getColor() == tern && board[i][j].isQueen()
                        && board[i][j + 1].getColor() == 0) { // если ситуация: дамка-0

                    afterQueen = true;

                    iCurQueen = i;
                    jCurQueen = j;

                    continue;
                }

                if (board[i][j].getColor() == 0 && board[i][j + 1].getColor() == (-1) * tern
                        && board[i][j + 2].getColor() == 0) { // если ситуация: 0-враг-0

                    jOfEnemyIfWaitingForQueen = j + 1;

                    if (afterQueen) { // если была дамка
                        res.add(new Move(iCurQueen, jCurQueen, i, j + 2, tern, true));
                        afterQueen = false;

                        j++;
                        while (j + 2 < board[i].length) {
                            if (board[i][j + 2].getColor() == 0)
                                res.add(new Move(iCurQueen, jCurQueen, i, j + 2, tern, true));
                            else
                                break;
                            j++;
                        }

                    }
                    waitingForQueen = true;

                    continue;
                }

                if ((board[i][j].getColor() != 0 && board[i][j + 1].getColor() != 0)
                        || (board[i][j].getColor() == tern || board[i][j + 1].getColor() == tern)) {
                    // если была сиутация: шашка-шашка или по диагонали встрелилась своя шашка

                    afterQueen = false;
                    waitingForQueen = false;

                    continue;
                }

                if (waitingForQueen) { // если ожидается дама
                    if (board[i][j].getColor() == (-1) * tern) { // если ожидалась дама и встретили врага
                        if (board[i][j + 1].getColor() == tern && board[i][j + 1].isQueen()) { // и за врагом стоит дама

                            res.add(new Move(i, j + 1, i, j - 1, tern, true));
                            int jOfCurrentCell = j + 1;

                            j--;
                            while (j >= 0) {
                                if (board[i][j].getColor() == 0)
                                    res.add(new Move(i, jOfCurrentCell, i, j, tern, true));
                                else
                                    break;
                                j--;
                            }
                            j = jOfCurrentCell - 1;

                            waitingForQueen = false;

                            continue;
                        }

                        if (board[i][j + 1].getColor() == 0 && board[i][j + 2].getColor() == tern
                                && board[i][j + 2].isQueen()) { // и за врагом 0-дама

                            res.add(new Move(i, j + 2, i, j - 1, tern, true));
                            int jOfCurrentCell = j + 2;

                            j = j - 2;
                            while (j >= 0) {
                                if (board[i][j].getColor() == 0)
                                    res.add(new Move(i, jOfCurrentCell, i, j, tern, true));
                                else
                                    break;
                                j--;
                            }
                            j = jOfCurrentCell - 1;
                            waitingForQueen = false;
                        }
                    } else {
                        if (board[i][j].getColor() == tern && board[i][j].isQueen()) {
                            // если ситуация: ожидается дама и   дама
                            int jOfCurrentCell = j;

                            j = jOfEnemyIfWaitingForQueen - 1;
                            while (j >= 0) {
                                if (board[i][j].getColor() == 0)
                                    res.add(new Move(i, jOfCurrentCell, i, j, tern, true));
                                else
                                    break;
                                j--;
                            }
                            j = jOfCurrentCell;

                            waitingForQueen = false;

                            continue;
                        }

                        if (board[i][j + 1].getColor() == tern && board[i][j + 1].isQueen()
                                && board[i][j].getColor() == 0) {
                            // если ситуация: ожидается дама и   0-дама
                            int jOfCurrentCell = j + 1;

                            j = jOfEnemyIfWaitingForQueen - 1;
                            while (j >= 0) {
                                if (board[i][j].getColor() == 0)
                                    res.add(new Move(i, jOfCurrentCell, i, j, tern, true));
                                else
                                    break;
                                j--;
                            }
                            j = jOfCurrentCell - 1;
                            waitingForQueen = false;

                            continue;
                        }

                        if (board[i][j + 2].getColor() == tern && board[i][j + 2].isQueen()
                                && board[i][j + 1].getColor() == 0 && board[i][j].getColor() == 0) {
                            // если ситуация: ожидается дама и    0-0-дама
                            int jOfCurrentCell = j + 2;

                            j = jOfEnemyIfWaitingForQueen - 1;
                            while (j >= 0) {
                                if (board[i][j].getColor() == 0)
                                    res.add(new Move(i, jOfCurrentCell, i, j, tern, true));
                                else
                                    break;
                                j--;
                            }
                            j = jOfCurrentCell - 2;

                            waitingForQueen = false;
                        }
                    }
                }
            }
            waitingForQueen = false;
            afterQueen = false;
        }
        return res.toArray(new Move[0]);
    }

    private Move[] checkAvailableMoves(Cell[][] board, int tern) {
        // на случай, если нету шашек которые могуть срубить, тогда найти всех которые могут сходить
        // возвращает массив с шашками которые имеют возможность куда-либо походить
        ArrayList<Move> res = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                if (board[i].length == 1) // если на диагонали стоит одна клетка
                    continue;

                if (tern == 1 && j <= board[i].length - 2 && board[i][j].getColor() == tern
                        && board[i][j + 1].getColor() == 0) { // если ход белых и шашка не дамка и ситуация: ты-0
                    res.add(new Move(i, j, i, j + 1, tern, false));

                    continue;
                }

                if (tern == -1 && j > 0 && board[i][j].getColor() == tern
                        && board[i][j - 1].getColor() == 0) { // если ход черных и шашка не дамка и ситуация: 0-ты
                    res.add(new Move(i, j, i, j - 1, tern, false));

                    continue;
                }

                if (board[i][j].isQueen() && board[i][j].getColor() == tern) { // если шашка дамка

                    if (j <= board[i].length - 2 && board[i][j + 1].getColor() == 0) { // если ситуация: дамка-0
                        res.add(new Move(i, j, i, j + 1, tern, false));

                        int jOfCurrentCell = j;
                        j = j + 2;
                        while (j < board[i].length) {
                            if (board[i][j].getColor() == 0)
                                res.add(new Move(i, jOfCurrentCell, i, j, tern, false));
                            else
                                break;
                            j++;
                        }
                        j = j - 2;

                        continue;
                    }

                    if (j > 0 && board[i][j - 1].getColor() == 0) { // если ситуация: 0-дамка
                        res.add(new Move(i, j, i, j - 1, tern, false));

                        int jOfCurrentCell = j;
                        j = j - 2;
                        while (j >= 0) {
                            if (board[i][j].getColor() == 0)
                                res.add(new Move(i, jOfCurrentCell, i, j, tern, false));
                            else
                                break;
                            j--;
                        }
                        j = jOfCurrentCell;
                    }
                }
            }
        }
        return res.toArray(new Move[0]);
    }

    private ArrayList<Cell[][]> addExtractionInCurrentMoves(Move move, Cell[][] board) {
        // возвращает массив досок после каждого из возможных ходов
        ArrayList<Cell[][]> result = new ArrayList<>();

        Cell firstCell = board[move.getIOfCurrentCell()][move.getJOfCurrentCell()];
        Cell secondCell = board[move.getIOfAvailableCellToMove()][move.getJOfAvailableCellToMove()];
        int tern = move.getColor();

        secondCell.changeCell(tern, firstCell.isQueen()); // ставим cell на новую клетку
        firstCell.changeCell(0, false); // очищаем cell

        if (move.isKiller()) { // если шашка срубила
            int[] diagonalsOfCurCell = secondCell.getDiagonals();
            int[] diagonalsOfLightCell = firstCell.getDiagonals();

            int indexOfIdenticalInCurCell = List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).indexOf(diagonalsOfLightCell[0]);

            int indexOfDifferentInCurCell;
            int indexOfDifferentInLightCell = 1;

            if (indexOfIdenticalInCurCell == -1) {
                indexOfIdenticalInCurCell = List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).indexOf(diagonalsOfLightCell[1]);
                indexOfDifferentInLightCell = 0;
            }

            if (indexOfIdenticalInCurCell == 0)
                indexOfDifferentInCurCell = 1;
            else
                indexOfDifferentInCurCell = 0;

            int j;
            for (j = 0; j < board[diagonalsOfCurCell[indexOfIdenticalInCurCell]].length; j++) {
                int[] curDiagonals = board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getDiagonals();

                int firstDiagonal = curDiagonals[0];
                int secondDiagonal = curDiagonals[1];

                if (board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getColor() != (-1) * tern
                        || (board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getColor() == (-1) * tern
                        && board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].isDead()))
                    continue;

                if (List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).contains(firstDiagonal)
                        && List.of(diagonalsOfLightCell[0], diagonalsOfLightCell[1]).contains(firstDiagonal)) {
                    // если firstDiagonal общая
                    if ((secondDiagonal > diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && secondDiagonal < diagonalsOfLightCell[indexOfDifferentInLightCell])
                            || (secondDiagonal < diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && secondDiagonal > diagonalsOfLightCell[indexOfDifferentInLightCell])) {
                        // если secondDiagonal находится между curCell и lightCell
                        break;
                    }
                } else { // если secondDiagonal общая
                    if ((firstDiagonal > diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && firstDiagonal < diagonalsOfLightCell[indexOfDifferentInLightCell])
                            || (firstDiagonal < diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && firstDiagonal > diagonalsOfLightCell[indexOfDifferentInLightCell])) {
                        // если firstDiagonal находится между curCell и lightCell
                        break;
                    }
                }
            }

            // сделать срубленную шашку врага мертвой, но не убирать ее с доски
            board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].makeDead();

            // если еще есть клетки куда можно срубить
            ArrayList<Move> availableMoves = checkAvailableKillsForCurrentCell(move.getIOfAvailableCellToMove(),
                    move.getJOfAvailableCellToMove(), secondCell, board);

            if (availableMoves.size() > 0) {
                for (Move availableMove : availableMoves)
                    result.addAll(addExtractionInCurrentMoves(availableMove, makeCopyOfBoard(board)));

            } else {
                // если для данной шашки больше нету возможности срубить
                // тогда убрать все мертвые шашки и сделать все клетки живыми
                removeDeadAndMakeAllAliveForCurrentBoard(board);
                result.add(board);
            }
        } else  // если шашка сходила
            result.add(board);

        // если шашка дошла до конца, то сделать ее дамкой
        int[] diagonals = secondCell.getDiagonals();
        List<Integer> curList = List.of(diagonals[0], diagonals[1]);

        if (tern == 1) { // если шашка белая
            if ((curList.contains(4) && curList.contains(14))
                    || (curList.contains(5) && curList.contains(13))
                    || (curList.contains(6) && curList.contains(12))
                    || (curList.contains(7) && curList.contains(11)))
                secondCell.makeQueen();
        } else { // если шашка черная
            if ((curList.contains(0) && curList.contains(11))
                    || (curList.contains(1) && curList.contains(10))
                    || (curList.contains(2) && curList.contains(9))
                    || (curList.contains(3) && curList.contains(8)))
                secondCell.makeQueen();
        }
        return result;
    }

    private ArrayList<Move> checkAvailableKillsForCurrentCell(int iOfCurrentCell, int jOfCurrentCell,
                                                              Cell cell, Cell[][] board) {
        ArrayList<Move> res = new ArrayList<>();
        int[] diagonals = cell.getDiagonals();
        int tern = cell.getColor();

        if (!cell.isQueen()) { // если текущая шашка не дамка
            for (int diagonal : diagonals) {
                int indexOfCell = List.of(board[diagonal]).indexOf(cell);

                if (indexOfCell <= board[diagonal].length - 3) {
                    if (board[diagonal][indexOfCell + 1].getColor() == (-1) * tern
                            && board[diagonal][indexOfCell + 2].getColor() == 0
                            && !board[diagonal][indexOfCell + 1].isDead()) {// если ситуация: cell-враг-0 и враг живой

                        res.add(new Move(iOfCurrentCell, jOfCurrentCell, diagonal,
                                indexOfCell + 2, tern, true));
                    }
                }

                if (indexOfCell >= 2) {
                    if (board[diagonal][indexOfCell - 2].getColor() == 0
                            && board[diagonal][indexOfCell - 1].getColor() == (-1) * tern
                            && !board[diagonal][indexOfCell - 1].isDead()) {// если ситуация: 0-враг-cell и враг живой

                        res.add(new Move(iOfCurrentCell, jOfCurrentCell, diagonal,
                                indexOfCell - 2, tern, true));
                    }
                }
            }
        } else { // если текущая шашка дамка
            for (int diagonal : diagonals) {
                int indexOfCell = List.of(board[diagonal]).indexOf(cell);

                int j = indexOfCell + 1;
                boolean enemyWasMet = false;

                while (j != board[diagonal].length) {// пробегаем по диагонали (подсвечиваем возможные ходы после cell)
                    if (board[diagonal][j].getColor() == tern) // если встретили свою шашку
                        break;

                    if (board[diagonal][j].getColor() == (-1) * tern) {// если встретили врага
                        if (enemyWasMet || board[diagonal][j].isDead())// если ранее уже встречали врага или враг мертвый
                            break;

                        enemyWasMet = true;

                        j++;
                        continue;
                    }

                    if (enemyWasMet && board[diagonal][j].getColor() == 0) {// ранее встретили врага и встретили 0

                        res.add(new Move(iOfCurrentCell, jOfCurrentCell, diagonal,
                                j, tern, true));
                    }
                    j++;
                }
                if (indexOfCell >= 2) {
                    enemyWasMet = false;
                    j = indexOfCell - 1;
                    while (j >= 0) {// пробегаем по диагонали (подсвечиваем возможные ходы перед cell)
                        if (board[diagonal][j].getColor() == tern) // если встретили свою шашку
                            break;

                        if (board[diagonal][j].getColor() == (-1) * tern) {// если встретили врага
                            if (enemyWasMet || board[diagonal][j].isDead()) // если ранее уже встретили врага или враг мертвый
                                break;

                            enemyWasMet = true;

                            j--;
                            continue;
                        }

                        if (enemyWasMet && board[diagonal][j].getColor() == 0) {// ранее встретили врага и встретили 0
                            res.add(new Move(iOfCurrentCell, jOfCurrentCell, diagonal,
                                    j, tern, true));
                        }
                        j--;
                    }
                }
            }
        }
        return res;
    }

    private void removeDeadAndMakeAllAliveForCurrentBoard(Cell[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].isDead()) {
                    board[i][j].changeCell(0, false); // убрать мертвого
                    board[i][j].revive(); // сделать мертвого живым
                }
            }
        }
    }

    private ArrayList<String> convertMoveToString(Move move, Cell[][] board, String previousString) {
        ArrayList<String> res = new ArrayList<>();

        Cell firstCell = board[move.getIOfCurrentCell()][move.getJOfCurrentCell()];
        Cell secondCell = board[move.getIOfAvailableCellToMove()][move.getJOfAvailableCellToMove()];
        int tern = move.getColor();

        secondCell.changeCell(tern, firstCell.isQueen()); // ставим cell на новую клетку
        firstCell.changeCell(0, false); // очищаем cell

        if (move.isKiller()) { // если шашка срубила
            int[] diagonalsOfCurCell = secondCell.getDiagonals();
            int[] diagonalsOfLightCell = firstCell.getDiagonals();

            int indexOfIdenticalInCurCell = List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).indexOf(diagonalsOfLightCell[0]);

            int indexOfDifferentInCurCell;
            int indexOfDifferentInLightCell = 1;

            if (indexOfIdenticalInCurCell == -1) {
                indexOfIdenticalInCurCell = List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).indexOf(diagonalsOfLightCell[1]);
                indexOfDifferentInLightCell = 0;
            }
            if (indexOfIdenticalInCurCell == 0)
                indexOfDifferentInCurCell = 1;
            else
                indexOfDifferentInCurCell = 0;

            int j;
            for (j = 0; j < board[diagonalsOfCurCell[indexOfIdenticalInCurCell]].length; j++) {
                int[] curDiagonals = board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getDiagonals();

                int firstDiagonal = curDiagonals[0];
                int secondDiagonal = curDiagonals[1];

                if (board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getColor() != (-1) * tern
                        || (board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].getColor() == (-1) * tern
                        && board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].isDead()))
                    continue;

                if (List.of(diagonalsOfCurCell[0], diagonalsOfCurCell[1]).contains(firstDiagonal)
                        && List.of(diagonalsOfLightCell[0], diagonalsOfLightCell[1]).contains(firstDiagonal)) {
                    // если firstDiagonal общая

                    if ((secondDiagonal > diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && secondDiagonal < diagonalsOfLightCell[indexOfDifferentInLightCell])
                            || (secondDiagonal < diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && secondDiagonal > diagonalsOfLightCell[indexOfDifferentInLightCell])) {
                        // если secondDiagonal находится между curCell и lightCell
                        break;
                    }
                } else { // если secondDiagonal общая
                    if ((firstDiagonal > diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && firstDiagonal < diagonalsOfLightCell[indexOfDifferentInLightCell])
                            || (firstDiagonal < diagonalsOfCurCell[indexOfDifferentInCurCell]
                            && firstDiagonal > diagonalsOfLightCell[indexOfDifferentInLightCell])) {
                        // если firstDiagonal находится между curCell и lightCell
                        break;
                    }
                }
            }
            // сделать срубленную шашку врага мертвой, но не убирать ее с доски
            board[diagonalsOfCurCell[indexOfIdenticalInCurCell]][j].makeDead();

            // если еще есть клетки куда можно срубить
            ArrayList<Move> availableMoves = checkAvailableKillsForCurrentCell(move.getIOfAvailableCellToMove(),
                    move.getJOfAvailableCellToMove(), secondCell, board);

            previousString += "-1 " + board[move.getIOfCurrentCell()][move.getJOfCurrentCell()].getName() + "-"
                    + board[move.getIOfAvailableCellToMove()][move.getJOfAvailableCellToMove()].getName() + "!";

            if (availableMoves.size() > 0) {
                for (Move availableMove : availableMoves)
                    res.addAll(convertMoveToString(availableMove, makeCopyOfBoard(board), previousString));
            } else {
                // если для данной шашки больше нету возможности срубить
                // тогда убрать все мертвые шашки и сделать все клетки живыми
                removeDeadAndMakeAllAliveForCurrentBoard(board);
                res.add(previousString);
            }
        } else { // если шашка сходила
            res.add("-1 " + board[move.getIOfCurrentCell()][move.getJOfCurrentCell()].getName() + "-"
                    + board[move.getIOfAvailableCellToMove()][move.getJOfAvailableCellToMove()].getName());
        }
        // если шашка дошла до конца, то сделать ее дамкой
        int[] diagonals = secondCell.getDiagonals();
        List<Integer> curList = List.of(diagonals[0], diagonals[1]);

        if (tern == 1) { // если шашка белая
            if ((curList.contains(4) && curList.contains(14))
                    || (curList.contains(5) && curList.contains(13))
                    || (curList.contains(6) && curList.contains(12))
                    || (curList.contains(7) && curList.contains(11)))
                secondCell.makeQueen();
        } else { // если шашка черная
            if ((curList.contains(0) && curList.contains(11))
                    || (curList.contains(1) && curList.contains(10))
                    || (curList.contains(2) && curList.contains(9))
                    || (curList.contains(3) && curList.contains(8)))
                secondCell.makeQueen();
        }
        return res;
    }

    private ArrayList<int[]> convertMustKillToArrayList() {
        ArrayList<int[]> res = new ArrayList<>();

        if (mustKill == null)
            return res;

        ArrayList<Cell> mustKillList = new ArrayList<>();
        Collections.addAll(mustKillList, mustKill);

        afterCycles:
        {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {

                    for (int k = 0; k < mustKillList.size(); k++) {
                        if (board[i][j] == mustKillList.get(k)) {
                            res.add(new int[]{i, j});

                            mustKillList.remove(k);
                            if (mustKillList.size() == 0)
                                break afterCycles;
                        }
                    }
                }
            }
        }
        return res;
    }

    public void convertArrayListToMustKill(ArrayList<int[]> mustKillList) {
        ArrayList<Cell> result = new ArrayList<>();

        for (int[] ints : mustKillList)
            result.add(board[ints[0]][ints[1]]);

        mustKill = result.toArray(new Cell[0]);
    }


    public boolean equalsForComeBackButtonPressedTest(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Board b))
            return false;

        for (int i = 0; i < this.board.length; i++)
            if (!Arrays.deepEquals(this.board[i], b.board[i]))
                return false;

        if (!Arrays.deepEquals(this.mustKill, b.mustKill))
            return false;

        if (!Arrays.deepEquals(this.boardHistory.toArray(new HistoricalBoard[0]), b.boardHistory.toArray(new HistoricalBoard[0])))
            return false;

        if (!Arrays.deepEquals(this.moveHistory.toArray(), b.moveHistory.toArray()))
            return false;


        return this.numberOfMove == b.numberOfMove && this.gameMode.equals(b.gameMode) && this.tern == b.tern;
    }

    public boolean equalsForLightAfterFirstClickTest(Object obj) {
        if (!this.equalsForComeBackButtonPressedTest(obj))
            return false;

        Board b = (Board) obj;
        return this.firstCell.equals(b.firstCell);
    }

    public boolean equalsForLightAvailableCheckersTest(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Board b))
            return false;

        for (int i = 0; i < this.board.length; i++)
            if (!Arrays.deepEquals(this.board[i], b.board[i]))
                return false;

        return this.numberOfMove == b.numberOfMove && this.gameMode.equals(b.gameMode) && this.tern == b.tern;
    }

    public boolean equalsForAfterSecondClickTest(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Board b))
            return false;

        for (int i = 0; i < this.board.length; i++)
            if (!Arrays.deepEquals(this.board[i], b.board[i]))
                return false;

        return this.gameMode.equals(b.gameMode);
    }

    public boolean equalsForInitBoardFromFileTest(Object obj) {
        if (!this.equalsForLightAvailableCheckersTest(obj))
            return false;

        Board b = (Board) obj;
        return Arrays.deepEquals(this.moveHistory.toArray(), b.moveHistory.toArray());
    }
}