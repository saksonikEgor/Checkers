package checkers.io;

import checkers.model.Board;
import checkers.model.Cell;
import checkers.model.HistoricalBoard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class BoardReader {
    private final String directoryForSavingBatch;

    public BoardReader(String directoryForSavingBatch) {
        File directory = new File(directoryForSavingBatch);
        if (!directory.exists())
            throw new RuntimeException("File not found");

        this.directoryForSavingBatch = directoryForSavingBatch;
    }

    public String[] getNamesOfFiles() {
        return Objects.requireNonNull(new File(directoryForSavingBatch).list());
    }

    public void deleteFile(String fileName) {
        if (!new File(directoryForSavingBatch + "/" + fileName).delete())
            throw new RuntimeException("File not found");
    }

    public String getGameModeFromFile(String fileName) {
        if (!new File(directoryForSavingBatch + "/" + fileName).exists())
            throw new RuntimeException("File not found");

        String gameMode = "";

        try (var buf = new BufferedReader(new FileReader(directoryForSavingBatch + "/" + fileName))) {
            gameMode = buf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameMode;
    }

    public void initBoardFromFile(Board b, String fileName) {
        if (!new File(directoryForSavingBatch + "/" + fileName).exists())
            throw new RuntimeException("File not found");

        try (var buf = new BufferedReader(new FileReader(directoryForSavingBatch + "/" + fileName))) {
            String line;
            buf.readLine();
            b.setTern(Integer.parseInt(buf.readLine()));
            b.setNumberOfMove(Integer.parseInt(buf.readLine()));

            int moveHistorySize = Integer.parseInt(buf.readLine());
            int boardHistorySize = Integer.parseInt(buf.readLine());

            for (int i = 0; i < moveHistorySize; i++)
                b.addInMoveHistory(buf.readLine());


            int i = 0;
            while (i < boardHistorySize) {
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


                for (int j = 0; j < 32; j++) {
                    line = buf.readLine();
                    String[] split = line.split(" ");

                    if (split.length == 6) {
                        resultBoard[Integer.parseInt(split[0])][Integer.parseInt(split[1])] =
                                new Cell(Integer.parseInt(split[2]), Integer.parseInt(split[3]), split[4], 0);

                        if (split[5].equals("true"))
                            resultBoard[Integer.parseInt(split[0])][Integer.parseInt(split[1])].lightOn();
                    } else {
                        resultBoard[Integer.parseInt(split[0])][Integer.parseInt(split[1])] =
                                new Cell(Integer.parseInt(split[2]), Integer.parseInt(split[3]), split[4],
                                        Integer.parseInt(split[5]));

                        if (split[6].equals("true"))
                            resultBoard[Integer.parseInt(split[0])][Integer.parseInt(split[1])].lightOn();

                        if (split[7].equals("true"))
                            resultBoard[Integer.parseInt(split[0])][Integer.parseInt(split[1])].makeQueen();

                        if (split[8].equals("true"))
                            resultBoard[Integer.parseInt(split[0])][Integer.parseInt(split[1])].makeDead();
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


                ArrayList<int[]> currentMustKill = new ArrayList<>();
                while (!((line = buf.readLine()).equals("gameStateIsFirstClick"))) {
                    String[] split = line.split(" ");

                    int[] ints = new int[2];
                    ints[0] = Integer.parseInt(split[0]);
                    ints[1] = Integer.parseInt(split[1]);
                    currentMustKill.add(ints);
                }
                boolean gameStateIsFirstClick = Boolean.parseBoolean(buf.readLine());

                HistoricalBoard historicalBoard;
                if (gameStateIsFirstClick) {
                    historicalBoard = new HistoricalBoard(resultBoard, true, currentMustKill);
                } else {
                    historicalBoard = new HistoricalBoard(resultBoard, false, currentMustKill);

                    line = buf.readLine();
                    String[] split = line.split(" ");

                    historicalBoard.setFirstCell(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }
                b.addInBoardHistory(historicalBoard);
                i++;
            }
            HistoricalBoard lastBoard = b.removeLastHistoricalBoard();
            Cell[][] board = lastBoard.getBoard();

            if (!lastBoard.isGameStateIsFirstClick()) {
                int[] ints = lastBoard.getIndicesOfFirstCell();

                b.setFirstCell(board[ints[0]][ints[1]]);
            }
            b.setBoard(board);
            b.convertArrayListToMustKill(lastBoard.getMustKill());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
