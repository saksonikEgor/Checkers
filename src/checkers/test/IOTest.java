package checkers.test;

import checkers.io.BoardReader;
import checkers.io.BoardWriter;
import checkers.model.Board;
import checkers.model.Cell;
import checkers.model.HistoricalBoard;
import org.junit.Test;
import org.junit.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOTest {
    @Test
    public void boardReaderInit_NO_NULL() {
        BoardReader boardReader = new BoardReader("/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest");
        Assert.assertNotNull(boardReader);
    }

    @Test
    public void boardWriterInit_NO_NULL() {
        BoardWriter boardWriter = new BoardWriter("/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest");
        Assert.assertNotNull(boardWriter);
    }

    @Test(expected = RuntimeException.class)
    public void boardReaderInit_EXCEPTION() {
        new BoardReader("wrong path");
    }

    @Test(expected = RuntimeException.class)
    public void boardWriterInit_EXCEPTION() {
        new BoardWriter("wrong path");
    }

    @Test
    public void boardReaderGetNamesOfFiles() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/getNameOfFilesTest");

        String[] files = boardReader.getNamesOfFiles();
        List<String> expected = List.of("1stFile.txt", "2ndFile.txt", "3rdFile.txt", "4thFile.txt");

        Assert.assertEquals(files.length, 4);

        Assert.assertTrue(expected.contains(files[0]));
        Assert.assertTrue(expected.contains(files[1]));
        Assert.assertTrue(expected.contains(files[2]));
        Assert.assertTrue(expected.contains(files[3]));
    }

    @Test(expected = RuntimeException.class)
    public void boardReaderDeleteFile_EXCEPTION() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/deleteFileTest");

        boardReader.deleteFile("wrong path");
    }

    @Test
    public void boardReaderDeleteFail() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/deleteFileTest");

        String fileName = "deleteMe.txt";
        File file = new File("/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/deleteFileTest" + "/" + fileName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boardReader.deleteFile(fileName);
    }

    @Test
    public void boardReaderGetGameModeFromFile() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/getGameModeFromFileTest");

        String actual1 = boardReader.getGameModeFromFile("getGameModeFromFile1.txt");
        String actual2 = boardReader.getGameModeFromFile("getGameModeFromFile2.txt");
        String actual3 = boardReader.getGameModeFromFile("getGameModeFromFile3.txt");


        Assert.assertEquals(actual1, "против игрока");
        Assert.assertEquals(actual2, "против игрока");
        Assert.assertEquals(actual3, "против компьютера");
    }

    @Test(expected = RuntimeException.class)
    public void boardReaderGetGameModeFromFile_EXCEPTION() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/getGameModeFromFileTest");

        boardReader.getGameModeFromFile("wrong path");
    }

    @Test
    public void boardReaderInitBoardFromFile() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/initBoardFromFileTest");

        Board expected = new Board("против игрока", new BoardWriter(
                "/Users/egor/Desktop/ШАШКИ/сохраненные партии"));
        expected.defaultInit();
        Cell[][] firstBoard = expected.getBoard();
        expected.addInBoardHistory(new HistoricalBoard(firstBoard, true, new ArrayList<>()));

        Cell[][] secondBoard = expected.makeCopyOfBoard(firstBoard);
        secondBoard[1][2].lightOff();
        secondBoard[2][2].lightOff();
        secondBoard[3][2].lightOff();
        secondBoard[4][1].lightOff();
        secondBoard[4][1].changeCell(0, false);
        secondBoard[4][2].changeCell(1, false);

        secondBoard[3][5].lightOn();
        secondBoard[4][4].lightOn();
        secondBoard[5][2].lightOn();
        secondBoard[6][0].lightOn();

        expected.setBoard(expected.makeCopyOfBoard(secondBoard));

        expected.setTern(-1);
        expected.setNumberOfMove(1);
        expected.addInMoveHistory("1 1 g3-f4");


        Board actual = new Board("против игрока", new BoardWriter(
                "/Users/egor/Desktop/ШАШКИ/сохраненные партии"));

        boardReader.initBoardFromFile(actual, "initBoardFromFile.txt");

        Assert.assertTrue(actual.equalsForInitBoardFromFileTest(expected));
    }

    @Test(expected = RuntimeException.class)
    public void boardReaderInitBoardFromFile_EXCEPTION() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/initBoardFromFileTest");
        boardReader.initBoardFromFile(new Board("против игрока", new BoardWriter(
                "/Users/egor/Desktop/ШАШКИ/сохраненные партии")), "wrong path");
    }

    @Test
    public void boardWriterSaveBatch() {
        BoardReader boardReader = new BoardReader(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/saveBatchTest");
        Board board = new Board("против игрока", new BoardWriter(
                "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/saveBatchTest"));

        boardReader.initBoardFromFile(board, "savedBatch.txt");
        board.saveButtonPressed("saveBatchActual");

        try {
            BufferedReader expected = new BufferedReader(new FileReader(
                    "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/saveBatchTest/saveBatchExpected.txt"));

            BufferedReader actual = new BufferedReader(new FileReader(
                    "/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/saveBatchTest/saveBatchActual.txt"));

            String line;
            while ((line = expected.readLine()) != null)
                Assert.assertEquals(line, actual.readLine());


            Assert.assertNull("Actual had more lines then the expected.", actual.readLine());
            Assert.assertNull("Expected had more lines then the actual.", expected.readLine());

            new File("/Users/egor/Desktop/ШАШКИ/партии для тестов/IOTest/saveBatchTest/saveBatchActual").delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
