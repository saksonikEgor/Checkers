package checkers.view;

import checkers.io.BoardReader;
import checkers.io.BoardWriter;
import checkers.model.Board;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(JFrame frame, BoardReader boardReader, BoardWriter boardWriter) {
        frame.setVisible(false);

        String title = "ШАШКИ";
        setTitle(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 850);

        Container container = getContentPane();

        FlowLayout flow = new FlowLayout();
        flow.setVgap(50);
        JPanel buttonPanel = new JPanel(flow);

        FlowLayout flow2 = new FlowLayout();
        flow2.setVgap(100);
        JPanel labelPanel = new JPanel(flow2);


        JButton vsPlayerButton = new JButton("Играть против пользователя");
        vsPlayerButton.addActionListener(event -> new GameFrame(title, "против игрока", this,
                boardReader, boardWriter, null));
        vsPlayerButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
        vsPlayerButton.setPreferredSize(new Dimension(300, 100));
        buttonPanel.add(vsPlayerButton);

        JButton vsComputerButton = new JButton("Играть против компьютера");
        vsComputerButton.addActionListener(event -> new GameFrame(title, "против компьютера", this,
                boardReader, boardWriter, null));
        vsComputerButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
        vsComputerButton.setPreferredSize(new Dimension(300, 100));
        buttonPanel.add(vsComputerButton);

        JButton loadBatchButton = new JButton("Загрузить сохраненную партию");
        loadBatchButton.addActionListener(event -> new SavedBatchesFrame(title, this,
                boardReader, boardWriter));
        loadBatchButton.setFont(new Font("TimesRoman", Font.BOLD, 18));
        loadBatchButton.setPreferredSize(new Dimension(300, 100));
        buttonPanel.add(loadBatchButton);

        JLabel label = new JLabel(title);
        label.setFont(new Font("TimesRoman", Font.BOLD, 22));
        label.setPreferredSize(new Dimension(100, 100));
        labelPanel.add(label);


        JPanel content = new JPanel(new BorderLayout());
        content.add(labelPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.CENTER);

        container.add(content);

        setVisible(true);
    }

    private static class SavedBatchesFrame extends JFrame {
        public SavedBatchesFrame(String title, Frame previousFrame,
                                 BoardReader boardReader, BoardWriter boardWriter) {
            super(title);
            previousFrame.setVisible(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(900, 850);

            Container c = getContentPane();

            JPanel menuPanel = new JPanel(new GridLayout(1, 1));

            JButton menuButton = new JButton("Вернуться в главное меню");
            menuButton.addActionListener(event -> new MainWindow(this, boardReader, boardWriter));
            menuButton.setFont(new Font("TimesRoman", Font.BOLD, 14));
            menuButton.setPreferredSize(new Dimension(200, 50));
            menuPanel.add(menuButton);


            JPanel scrollPanePanel = new JPanel();
            JPanel savedBatchesButtonsPanel = new JPanel(new GridLayout(0, 1));
            String[] files = boardReader.getNamesOfFiles();


            for (String file : files) {
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JPanel labelPanel = new JPanel(new FlowLayout());
                JPanel resPanel = new JPanel(new FlowLayout());

                JButton button1 = new JButton("Удалить");
                button1.addActionListener(event -> {
                    boardReader.deleteFile(file);
                    new SavedBatchesFrame(title, this, boardReader, boardWriter);
                });

                JButton button2 = new JButton("Загрузить");
                button2.addActionListener(event -> {
                    String gameMode = boardReader.getGameModeFromFile(file);
                    new GameFrame(title, gameMode, this,
                            boardReader, boardWriter, file);
                });

                JLabel label = new JLabel(file.replaceAll(".txt", ""));

                buttonPanel.add(button1);
                buttonPanel.add(button2);
                labelPanel.add(label);

                resPanel.add(labelPanel);
                resPanel.add(buttonPanel);

                savedBatchesButtonsPanel.add(resPanel);
            }


            JScrollPane savedBatches = new JScrollPane(savedBatchesButtonsPanel);
            savedBatches.setPreferredSize(new Dimension(400, 700));
            scrollPanePanel.add(savedBatches);
            c.add(scrollPanePanel, BorderLayout.CENTER);


            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel1.add(menuPanel);
            c.add(panel1, BorderLayout.NORTH);

            setVisible(true);
        }
    }


    private static class GameFrame extends JFrame {
        public GameFrame(String title, String gameMode, Frame previousFrame,
                         BoardReader boardReader, BoardWriter boardWriter, String nameOfLoadedFile) {
            super(title);
            previousFrame.setVisible(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(900, 850);

            Container c = getContentPane();

            JPanel menuPanel = new JPanel(new GridLayout(1, 1));
            JPanel historyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel comeBackButtonPanel = new JPanel(new GridLayout(1, 1));
            JPanel saveButtonPanel = new JPanel(new GridLayout(1, 1));

            Board board = new Board(gameMode, boardWriter);
            if (nameOfLoadedFile == null)
                board.defaultInit();
            else
                boardReader.initBoardFromFile(board, nameOfLoadedFile);


            JTextArea text = new JTextArea();
            text.setLineWrap(true);
            text.setEditable(false);
            JScrollPane history = new JScrollPane(text);
            history.setPreferredSize(new Dimension(150, 600));
            historyPanel.add(history);

            JButton menuButton = new JButton("Вернуться в главное меню");
            menuButton.addActionListener(event -> new MainWindow(this, boardReader, boardWriter));
            menuButton.setFont(new Font("TimesRoman", Font.BOLD, 14));
            menuButton.setPreferredSize(new Dimension(200, 50));
            menuPanel.add(menuButton);

            JButton comeBackButton = new JButton("Вернуться на предыдущий ход");
            comeBackButton.setFont(new Font("TimesRoman", Font.BOLD, 14));
            comeBackButton.setPreferredSize(new Dimension(280, 70));
            comeBackButtonPanel.add(comeBackButton);

            JButton saveButton = new JButton("Сохранить партию");
            saveButton.setFont(new Font("TimesRoman", Font.BOLD, 14));
            saveButton.setPreferredSize(new Dimension(280, 70));
            saveButtonPanel.add(saveButton);


            BoardView boardPanel = new BoardView(board, text, comeBackButton, saveButton);
            c.add(boardPanel, BorderLayout.CENTER);
            c.add(historyPanel, BorderLayout.EAST);

            JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel1.add(menuPanel);
            panel1.add(comeBackButtonPanel);
            panel1.add(saveButton);
            c.add(panel1, BorderLayout.NORTH);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        String directoryForSavingBatch = "src/checkers/savingBatches";

        new MainWindow(new JFrame(), new BoardReader(directoryForSavingBatch),
                new BoardWriter(directoryForSavingBatch));
    }
}