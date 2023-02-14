package checkers.test;

import checkers.io.BoardReader;
import checkers.io.BoardWriter;
import checkers.model.Board;
import checkers.model.Cell;
import org.junit.Test;
import org.junit.Assert;

public class BoardTest {
    BoardWriter boardWriter = new BoardWriter("checkers/savingBatches");

    @Test
    public void init_NO_NULL() {
        Board board1 = new Board("против игрока", boardWriter);
        Board board2 = new Board("против компьютера", boardWriter);

        Assert.assertNotNull(board1);
        Assert.assertNotNull(board2);
    }

    @Test(expected = RuntimeException.class)
    public void init_EXCEPTION() {
        new Board("wrong gameMode", boardWriter);
    }

    @Test
    public void comeBackButtonPressed() {
        BoardReader boardReader = new BoardReader(
                "checkers/test/batchesForTests/boardTest/comeBackButtonPressedTest");

        Board expected1 = new Board("против игрока", boardWriter);
        Board actual1 = new Board("против игрока", boardWriter);

        Board expected2 = new Board("против игрока", boardWriter);
        Board actual2 = new Board("против игрока", boardWriter);

        Board expected3 = new Board("против игрока", boardWriter);
        Board actual3 = new Board("против игрока", boardWriter);

        Board expected4 = new Board("против компьютера", boardWriter);
        Board actual4 = new Board("против компьютера", boardWriter);


        boardReader.initBoardFromFile(expected1, "afterComeBackButtonExpected1.txt");
        boardReader.initBoardFromFile(actual1, "afterComeBackButtonActual1.txt");

        boardReader.initBoardFromFile(expected2, "afterComeBackButtonExpected2.txt");
        boardReader.initBoardFromFile(actual2, "afterComeBackButtonActual2.txt");

        boardReader.initBoardFromFile(expected3, "afterComeBackButtonExpected3.txt");
        boardReader.initBoardFromFile(actual3, "afterComeBackButtonActual3.txt");

        boardReader.initBoardFromFile(expected4, "afterComeBackButtonExpected4.txt");
        boardReader.initBoardFromFile(actual4, "afterComeBackButtonActual4.txt");

        Assert.assertTrue(expected1.equalsForComeBackButtonPressedTest(actual1));
        Assert.assertTrue(expected2.equalsForComeBackButtonPressedTest(actual2));
        Assert.assertTrue(expected3.equalsForComeBackButtonPressedTest(actual3));
        Assert.assertTrue(expected4.equalsForComeBackButtonPressedTest(actual4));
    }

    @Test
    public void lightAfterFirstClick() {
        BoardReader boardReader = new BoardReader(
                "checkers/test/batchesForTests/boardTest/lightAfterFirstClickTest");

        Board expected1 = new Board("против игрока", boardWriter);
        Board actual1 = new Board("против игрока", boardWriter);

        Board expected2 = new Board("против игрока", boardWriter);
        Board actual2 = new Board("против игрока", boardWriter);

        Board expected3 = new Board("против игрока", boardWriter);
        Board actual3 = new Board("против игрока", boardWriter);

        Board expected4 = new Board("против компьютера", boardWriter);
        Board actual4 = new Board("против компьютера", boardWriter);

        Board expected5 = new Board("против компьютера", boardWriter);
        Board actual5 = new Board("против компьютера", boardWriter);

        Board expected6 = new Board("против игрока", boardWriter);
        Board actual6 = new Board("против игрока", boardWriter);


        boardReader.initBoardFromFile(expected1, "lightAfterFirstClick1.txt");
        boardReader.initBoardFromFile(actual1, "lightAfterFirstClick1.txt");
        Cell[][] actual1Board = actual1.getBoard();
        actual1.setFirstCell(actual1Board[3][1]);
        actual1.lightAfterFirstClick();

        Cell[][] expected1Board = expected1.getBoard();
        expected1.setFirstCell(expected1Board[3][1]);
        expected1Board[1][2].lightOff();
        expected1Board[2][2].lightOff();
        expected1Board[3][2].lightOff();
        expected1Board[4][0].lightOff();
        expected1Board[4][1].lightOn();
        Assert.assertTrue(expected1.equalsForLightAfterFirstClickTest(actual1));


        boardReader.initBoardFromFile(expected2, "lightAfterFirstClick1.txt");
        boardReader.initBoardFromFile(actual2, "lightAfterFirstClick1.txt");
        Cell[][] actual2Board = actual2.getBoard();
        actual2.setFirstCell(actual2Board[1][2]);
        actual2.lightAfterFirstClick();

        Cell[][] expected2Board = expected2.getBoard();
        expected2.setFirstCell(expected2Board[1][2]);
        expected2Board[2][2].lightOff();
        expected2Board[3][1].lightOff();
        expected2Board[3][2].lightOff();
        expected2Board[4][0].lightOff();
        expected2Board[2][3].lightOn();
        Assert.assertTrue(expected2.equalsForLightAfterFirstClickTest(actual2));


        boardReader.initBoardFromFile(expected3, "lightAfterFirstClick2.txt");
        boardReader.initBoardFromFile(actual3, "lightAfterFirstClick2.txt");
        Cell[][] actual3Board = actual3.getBoard();
        actual3.setFirstCell(actual3Board[6][1]);
        actual3.lightAfterFirstClick();

        Cell[][] expected3Board = expected3.getBoard();
        expected3.setFirstCell(expected3Board[6][1]);
        expected3Board[3][4].lightOff();
        expected3Board[3][5].lightOff();
        expected3Board[4][4].lightOff();
        expected3Board[5][4].lightOff();
        expected3Board[5][2].lightOn();
        expected3Board[6][0].lightOn();
        Assert.assertTrue(expected3.equalsForLightAfterFirstClickTest(actual3));


        boardReader.initBoardFromFile(expected4, "lightAfterFirstClick3.txt");
        boardReader.initBoardFromFile(actual4, "lightAfterFirstClick3.txt");
        Cell[][] actual4Board = actual4.getBoard();
        actual4.setFirstCell(actual4Board[4][2]);
        actual4.lightAfterFirstClick();

        Cell[][] expected4Board = expected4.getBoard();
        expected4.setFirstCell(expected4Board[4][2]);
        expected4Board[1][1].lightOff();
        expected4Board[1][2].lightOff();
        expected4Board[2][1].lightOff();
        expected4Board[3][1].lightOff();
        expected4Board[4][0].lightOff();
        expected4Board[5][0].lightOff();
        expected4Board[4][3].lightOn();
        expected4Board[5][1].lightOn();
        Assert.assertTrue(expected4.equalsForLightAfterFirstClickTest(actual4));


        boardReader.initBoardFromFile(expected5, "lightAfterFirstClick3.txt");
        boardReader.initBoardFromFile(actual5, "lightAfterFirstClick3.txt");
        Cell[][] actual5Board = actual5.getBoard();
        actual5.setFirstCell(actual5Board[1][2]);
        actual5.lightAfterFirstClick();

        Cell[][] expected5Board = expected5.getBoard();
        expected5.setFirstCell(expected5Board[1][2]);
        expected5Board[1][1].lightOff();
        expected5Board[4][2].lightOff();
        expected5Board[2][1].lightOff();
        expected5Board[3][1].lightOff();
        expected5Board[4][0].lightOff();
        expected5Board[5][0].lightOff();
        expected5Board[2][3].lightOn();
        Assert.assertTrue(expected5.equalsForLightAfterFirstClickTest(actual5));


        boardReader.initBoardFromFile(expected6, "lightAfterFirstClick4.txt");
        boardReader.initBoardFromFile(actual6, "lightAfterFirstClick4.txt");
        Cell[][] actual6Board = actual6.getBoard();
        actual6.setFirstCell(actual6Board[5][1]);
        actual6.lightAfterFirstClick();

        Cell[][] expected6Board = expected6.getBoard();
        expected6.setFirstCell(expected6Board[5][1]);
        expected6Board[1][2].lightOff();
        expected6Board[2][0].lightOff();
        expected6Board[2][2].lightOff();
        expected6Board[3][0].lightOff();
        expected6Board[3][2].lightOff();
        expected6Board[4][0].lightOff();
        expected6Board[4][2].lightOn();
        expected6Board[5][0].lightOn();
        expected6Board[5][2].lightOn();
        expected6Board[5][3].lightOn();
        expected6Board[6][0].lightOn();
        Assert.assertTrue(expected6.equalsForLightAfterFirstClickTest(actual6));
    }

    @Test
    public void lightAvailableCheckers() {
        BoardReader boardReader = new BoardReader(
                "checkers/test/batchesForTests/boardTest/lightAvailableCheckersTest");

        Board expected1 = new Board("против игрока", boardWriter);
        Board actual1 = new Board("против игрока", boardWriter);

        Board expected2 = new Board("против игрока", boardWriter);
        Board actual2 = new Board("против игрока", boardWriter);

        Board expected3 = new Board("против компьютера", boardWriter);
        Board actual3 = new Board("против компьютера", boardWriter);

        Board expected4 = new Board("против компьютера", boardWriter);
        Board actual4 = new Board("против компьютера", boardWriter);


        boardReader.initBoardFromFile(expected1, "lightAvailableCheckers1.txt");
        boardReader.initBoardFromFile(actual1, "lightAvailableCheckers1.txt");
        Cell[][] actual1Board = actual1.getBoard();
        actual1Board[1][2].lightOff();
        actual1Board[2][2].lightOff();
        actual1Board[3][2].lightOff();
        actual1Board[3][1].lightOff();
        actual1Board[4][0].lightOff();
        actual1.lightAvailableCheckers();

        Assert.assertTrue(expected1.equalsForLightAvailableCheckersTest(actual1));


        boardReader.initBoardFromFile(expected2, "lightAvailableCheckers2.txt");
        boardReader.initBoardFromFile(actual2, "lightAvailableCheckers2.txt");
        Cell[][] actual2Board = actual2.getBoard();
        actual2Board[3][4].lightOff();
        actual2Board[3][5].lightOff();
        actual2Board[4][4].lightOff();
        actual2Board[5][4].lightOff();
        actual2Board[6][1].lightOff();
        actual2.lightAvailableCheckers();

        Assert.assertTrue(expected2.equalsForLightAvailableCheckersTest(actual2));


        boardReader.initBoardFromFile(expected3, "lightAvailableCheckers3.txt");
        boardReader.initBoardFromFile(actual3, "lightAvailableCheckers3.txt");
        Cell[][] actual3Board = actual3.getBoard();
        actual3Board[1][1].lightOff();
        actual3Board[1][2].lightOff();
        actual3Board[2][1].lightOff();
        actual3Board[3][1].lightOff();
        actual3Board[4][0].lightOff();
        actual3Board[4][2].lightOff();
        actual3Board[5][0].lightOff();
        actual3.lightAvailableCheckers();

        Assert.assertTrue(expected3.equalsForLightAvailableCheckersTest(actual3));


        boardReader.initBoardFromFile(expected4, "lightAvailableCheckers4.txt");
        boardReader.initBoardFromFile(actual4, "lightAvailableCheckers4.txt");
        Cell[][] actual4Board = actual4.getBoard();
        actual4Board[1][1].lightOff();
        actual4Board[1][2].lightOff();
        actual4Board[2][1].lightOff();
        actual4Board[3][1].lightOff();
        actual4Board[4][0].lightOff();
        actual4.lightAvailableCheckers();

        Assert.assertTrue(expected4.equalsForLightAvailableCheckersTest(actual4));
    }

    @Test
    public void afterSecondClick() {
        BoardReader boardReader = new BoardReader(
                "checkers/test/batchesForTests/boardTest/afterSecondClickTest");

        Board expected1 = new Board("против игрока", boardWriter);
        Board actual1 = new Board("против игрока", boardWriter);

        Board expected2 = new Board("против игрока", boardWriter);
        Board actual2 = new Board("против игрока", boardWriter);

        Board expected3 = new Board("против игрока", boardWriter);
        Board actual3 = new Board("против игрока", boardWriter);

        Board expected4 = new Board("против компьютера", boardWriter);
        Board actual4 = new Board("против компьютера", boardWriter);

        Board expected5 = new Board("против компьютера", boardWriter);
        Board actual5 = new Board("против компьютера", boardWriter);


        boardReader.initBoardFromFile(expected1, "afterSecondClick1.txt");
        boardReader.initBoardFromFile(actual1, "afterSecondClick1.txt");
        Cell[][] actual1Board = actual1.getBoard();
        actual1.setFirstCell(actual1Board[3][1]);
        actual1.setSecondCell(actual1Board[4][1]);
        actual1.lightAfterFirstClick();
        actual1.afterSecondClick();

        Cell[][] expected1Board = expected1.getBoard();
        expected1.setFirstCell(expected1Board[3][1]);
        expected1.setSecondCell(expected1Board[4][1]);
        expected1Board[1][2].lightOff();
        expected1Board[2][2].lightOff();
        expected1Board[3][2].lightOff();
        expected1Board[3][1].lightOff();
        expected1Board[4][0].lightOff();
        expected1Board[3][1].changeCell(0, false);
        expected1Board[4][1].changeCell(1, false);
        Assert.assertTrue(expected1.equalsForAfterSecondClickTest(actual1));


        boardReader.initBoardFromFile(expected2, "afterSecondClick1.txt");
        boardReader.initBoardFromFile(actual2, "afterSecondClick1.txt");
        Cell[][] actual2Board = actual2.getBoard();
        actual2.setFirstCell(actual2Board[3][1]);
        actual2.setSecondCell(actual2Board[1][2]);
        actual2.lightAfterFirstClick();
        actual2.afterSecondClick();

        Cell[][] expected2Board = expected2.getBoard();
        expected2.setFirstCell(expected2Board[3][1]);
        expected2.setSecondCell(actual2Board[1][2]);
        expected2Board[2][2].lightOff();
        expected2Board[3][1].lightOff();
        expected2Board[3][2].lightOff();
        expected2Board[4][0].lightOff();
        expected2Board[2][3].lightOn();
        Assert.assertTrue(expected2.equalsForAfterSecondClickTest(actual2));


        boardReader.initBoardFromFile(expected3, "afterSecondClick2.txt");
        boardReader.initBoardFromFile(actual3, "afterSecondClick2.txt");
        Cell[][] actual3Board = actual3.getBoard();
        actual3.setFirstCell(actual3Board[6][1]);
        actual3.setSecondCell(actual3Board[6][0]);
        actual3.lightAfterFirstClick();
        actual3.afterSecondClick();

        Cell[][] expected3Board = expected3.getBoard();
        expected3.setFirstCell(expected3Board[6][1]);
        expected3.setSecondCell(expected3Board[6][0]);
        expected3Board[3][4].lightOff();
        expected3Board[3][5].lightOff();
        expected3Board[4][4].lightOff();
        expected3Board[5][4].lightOff();
        expected3Board[6][1].lightOff();
        expected3Board[6][1].changeCell(0, false);
        expected3Board[6][0].changeCell(-1, false);
        Assert.assertTrue(expected3.equalsForAfterSecondClickTest(actual3));


        boardReader.initBoardFromFile(expected4, "afterSecondClick3.txt");
        boardReader.initBoardFromFile(actual4, "afterSecondClick3.txt");
        Cell[][] actual4Board = actual4.getBoard();
        actual4.setFirstCell(actual4Board[4][2]);
        actual4.setSecondCell(actual4Board[4][3]);
        actual4.lightAfterFirstClick();
        actual4.afterSecondClick();

        Cell[][] expected4Board = expected4.getBoard();
        expected4.setFirstCell(expected4Board[4][2]);
        expected4.setSecondCell(expected4Board[4][3]);
        expected4Board[1][1].lightOff();
        expected4Board[1][2].lightOff();
        expected4Board[2][1].lightOff();
        expected4Board[3][1].lightOff();
        expected4Board[4][0].lightOff();
        expected4Board[4][2].lightOff();
        expected4Board[5][0].lightOff();
        expected4Board[4][2].changeCell(0, false);
        expected4Board[4][3].changeCell(1, false);
        Assert.assertTrue(expected4.equalsForAfterSecondClickTest(actual4));


        boardReader.initBoardFromFile(expected5, "afterSecondClick3.txt");
        boardReader.initBoardFromFile(actual5, "afterSecondClick3.txt");
        Cell[][] actual5Board = actual5.getBoard();
        actual5.setFirstCell(actual5Board[1][2]);
        actual5.setSecondCell(actual5Board[1][0]);
        actual5.lightAfterFirstClick();
        actual5.afterSecondClick();

        Cell[][] expected5Board = expected5.getBoard();
        expected5.setFirstCell(expected5Board[1][2]);
        expected5.setSecondCell(expected5Board[1][0]);
        expected5Board[1][1].lightOff();
        expected5Board[4][2].lightOff();
        expected5Board[2][1].lightOff();
        expected5Board[3][1].lightOff();
        expected5Board[4][0].lightOff();
        expected5Board[5][0].lightOff();
        expected5Board[2][3].lightOn();
        Assert.assertTrue(expected5.equalsForAfterSecondClickTest(actual5));
    }

    @Test
    public void makeComputerMove() {
        BoardReader boardReader = new BoardReader(
                "checkers/test/batchesForTests/boardTest/makeComputerMoveTest");

        Board expected1 = new Board("против компьютера", boardWriter);
        Board actual1 = new Board("против компьютера", boardWriter);

        Board expected2 = new Board("против компьютера", boardWriter);
        Board actual2 = new Board("против компьютера", boardWriter);

        Board expected3 = new Board("против компьютера", boardWriter);
        Board actual3 = new Board("против компьютера", boardWriter);

        Board expected4 = new Board("против компьютера", boardWriter);
        Board actual4 = new Board("против компьютера", boardWriter);


        boardReader.initBoardFromFile(expected1, "afterComputerMove1.txt");
        boardReader.initBoardFromFile(actual1, "beforeComputerMove1.txt");
        Cell[][] actual1Board = actual1.getBoard();
        actual1.setFirstCell(actual1Board[3][2]);
        actual1.setSecondCell(actual1Board[4][2]);
        actual1.lightAfterFirstClick();
        actual1.afterSecondClick();
        actual1.makeComputerMove();

        Cell[][] expected1Board = expected1.getBoard();
        expected1Board[0][0].lightOff();
        expected1Board[1][0].lightOff();
        expected1Board[1][2].lightOff();
        expected1Board[4][0].lightOff();
        expected1Board[4][2].lightOff();
        Assert.assertTrue(expected1.equalsForLightAvailableCheckersTest(actual1));


        boardReader.initBoardFromFile(expected2, "afterComputerMove2.txt");
        boardReader.initBoardFromFile(actual2, "beforeComputerMove2.txt");
        Cell[][] actual2Board = actual2.getBoard();
        actual2.setFirstCell(actual2Board[1][0]);
        actual2.setSecondCell(actual2Board[2][1]);
        actual2.lightAfterFirstClick();
        actual2.afterSecondClick();
        actual2.makeComputerMove();

        Cell[][] expected2Board = expected2.getBoard();
        expected2Board[0][0].lightOff();
        Assert.assertTrue(expected2.equalsForLightAvailableCheckersTest(actual2));


        boardReader.initBoardFromFile(expected3, "afterComputerMove3.txt");
        boardReader.initBoardFromFile(actual3, "beforeComputerMove3.txt");
        Cell[][] actual3Board = actual3.getBoard();
        actual3.setFirstCell(actual3Board[4][2]);
        actual3.setSecondCell(actual3Board[4][3]);
        actual3.lightAfterFirstClick();
        actual3.afterSecondClick();
        actual3.makeComputerMove();

        Cell[][] expected3Board = expected3.getBoard();
        expected3Board[3][2].lightOff();
        Assert.assertTrue(expected3.equalsForLightAvailableCheckersTest(actual3));


        boardReader.initBoardFromFile(expected4, "afterComputerMove4.txt");
        boardReader.initBoardFromFile(actual4, "beforeComputerMove4.txt");
        Cell[][] actual4Board = actual4.getBoard();
        actual4.setFirstCell(actual4Board[4][3]);
        actual4.setSecondCell(actual4Board[5][2]);
        actual4.lightAfterFirstClick();
        actual4.afterSecondClick();
        actual4.makeComputerMove();

        Cell[][] expected4Board = expected4.getBoard();
        expected4Board[3][1].lightOff();
        Assert.assertTrue(expected4.equalsForLightAvailableCheckersTest(actual4));
    }
}