package checkers.io;

import checkers.model.Board;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BoardWriter {
    private final String directoryForSavingBatch;

    public BoardWriter(String directoryForSavingBatch) {
        File directory = new File(directoryForSavingBatch);
        if (!directory.exists())
            throw new RuntimeException("File not found");

        this.directoryForSavingBatch = directoryForSavingBatch;
    }

    public boolean saveBatch(Board b, String fileName) {
        File file = new File(directoryForSavingBatch + "/" + fileName + ".txt");

        if (file.exists()) {
            return false;
        } else {
            try (BufferedWriter buf = new BufferedWriter(new FileWriter(file))) {
                buf.write(b.getGameMode() + "\n");
                buf.write(b.getTern() + "\n");
                buf.write(b.getNumberOfMove() + "\n");

                buf.write(b.getMoveHistory().size() + "\n");
                buf.write(b.getBoardHistory().size() + "\n");
                for (String move : b.getMoveHistory()) {
                    buf.write(move + "\n");
                }
                ArrayList<String> boardHistoryList = b.getBoardHistoryToStringList();

                for (String str : boardHistoryList) {
                    buf.write(str + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }
}
