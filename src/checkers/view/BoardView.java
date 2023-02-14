package checkers.view;

import checkers.model.Board;
import checkers.model.Cell;
import checkers.model.HistoricalBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

public class BoardView extends JPanel {
    private final Board b;
    private Cell[][] board;

    private boolean wasFirstClick = false;

    private final JTextArea history;

    private final int size = 85;
    private final MyRectangle2D[][] squares;
    private final ArrayList<Ellipse2D> whiteFigures;
    private final ArrayList<Ellipse2D> blackFigures;
    private final ArrayList<Ellipse2D> yellowFigures;

    private Cell firstCell;
    private Cell secondCell;

    private final Color blackCellColor = Color.GRAY;
    private final Color yellowCellColor = Color.YELLOW;

    public BoardView(Board b, JTextArea text, JButton comeBackButton, JButton saveButton) {
        this.b = b;
        board = b.getBoard();

        squares = new MyRectangle2D[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {//массив для представления квадратов шахматной доски.
                squares[i][j] = new MyRectangle2D(i * size, j * size, size, size);

                Color whiteCellColor = Color.LIGHT_GRAY;
                if ((i + j) % 2 == 0)
                    squares[i][j].setColor(whiteCellColor); //Четные квадраты в светлый
                else
                    squares[i][j].setColor(blackCellColor); //цвет, нечетные в темный
            }
        }
        history = text;

        comeBackButton.addActionListener(event -> {
            HistoricalBoard historicalBoard = b.comeBackButtonPressed();
            board = b.getBoard();

            if (!historicalBoard.isGameStateIsFirstClick()) {
                int[] indices = historicalBoard.getIndicesOfFirstCell();

                firstCell = board[indices[0]][indices[1]];

                wasFirstClick = true;
            } else
                wasFirstClick = false;

            reScan();
        });

        saveButton.addActionListener(event -> {
            String getMessage = JOptionPane.showInputDialog(new JFrame(), "Введите название партии");

            while (!b.saveButtonPressed(getMessage)) {
                getMessage = JOptionPane.showInputDialog(new JFrame(), "Партия с таким названием уже существует");
            }
        });


        whiteFigures = new ArrayList<>();
        blackFigures = new ArrayList<>();
        yellowFigures = new ArrayList<>();

        setTable(0, 0, 0, 7);
        setTable(1, 0, 2, 7);
        setTable(1, 1, 1, 6);
        setTable(1, 2, 0, 5);
        setTable(2, 0, 4, 7);
        setTable(2, 1, 3, 6);
        setTable(2, 2, 2, 5);
        setTable(2, 3, 1, 4);
        setTable(2, 4, 0, 3);
        setTable(3, 0, 6, 7);
        setTable(3, 1, 5, 6);
        setTable(3, 2, 4, 5);
        setTable(3, 3, 3, 4);
        setTable(3, 4, 2, 3);
        setTable(3, 5, 1, 2);
        setTable(3, 6, 0, 1);

        setTable(4, 0, 7, 6);
        setTable(4, 1, 6, 5);
        setTable(4, 2, 5, 4);
        setTable(4, 3, 4, 3);
        setTable(4, 4, 3, 2);
        setTable(4, 5, 2, 1);
        setTable(4, 6, 1, 0);
        setTable(5, 0, 7, 4);
        setTable(5, 1, 6, 3);
        setTable(5, 2, 5, 2);
        setTable(5, 3, 4, 1);
        setTable(5, 4, 3, 0);
        setTable(6, 0, 7, 2);
        setTable(6, 1, 6, 1);
        setTable(6, 2, 5, 0);
        setTable(7, 0, 7, 0);


        b.lightAvailableCheckers();
        reScan();

        MyMouseListener ml = new MyMouseListener();
        this.addMouseListener(ml);
    }

    private void setTable(int iBoard, int jBoard, int iTable, int jTable) {
        if (board[iBoard][jBoard].isLight())
            squares[iTable][jTable].setColor(yellowCellColor);
        else
            squares[iTable][jTable].setColor(blackCellColor);


        switch (board[iBoard][jBoard].getColor()) {
            case (1) -> whiteFigures.add(new Ellipse2D.Double(iTable * size + (double) size / 8,
                    jTable * size + (double) size / 8, size - (double) size / 3, size - (double) size / 3));

            case (-1) -> blackFigures.add(new Ellipse2D.Double(iTable * size + (double) size / 8,
                    jTable * size + (double) size / 8, size - (double) size / 3, size - (double) size / 3));
        }

        if (board[iBoard][jBoard].isQueen()) {
            yellowFigures.add(new Ellipse2D.Double(iTable * size + (double) size / 5, jTable * size + (double) size / 5,
                    (double) size / 2, (double) size / 2));
        }
    }

    private void reScan() {
        whiteFigures.clear();
        blackFigures.clear();
        yellowFigures.clear();

        setTable(0, 0, 0, 7);
        setTable(1, 0, 2, 7);
        setTable(1, 1, 1, 6);
        setTable(1, 2, 0, 5);
        setTable(2, 0, 4, 7);
        setTable(2, 1, 3, 6);
        setTable(2, 2, 2, 5);
        setTable(2, 3, 1, 4);
        setTable(2, 4, 0, 3);
        setTable(3, 0, 6, 7);
        setTable(3, 1, 5, 6);
        setTable(3, 2, 4, 5);
        setTable(3, 3, 3, 4);
        setTable(3, 4, 2, 3);
        setTable(3, 5, 1, 2);
        setTable(3, 6, 0, 1);

        setTable(4, 0, 7, 6);
        setTable(4, 1, 6, 5);
        setTable(4, 2, 5, 4);
        setTable(4, 3, 4, 3);
        setTable(4, 4, 3, 2);
        setTable(4, 5, 2, 1);
        setTable(4, 6, 1, 0);
        setTable(5, 0, 7, 4);
        setTable(5, 1, 6, 3);
        setTable(5, 2, 5, 2);
        setTable(5, 3, 4, 1);
        setTable(5, 4, 3, 0);
        setTable(6, 0, 7, 2);
        setTable(6, 1, 6, 1);
        setTable(6, 2, 5, 0);
        setTable(7, 0, 7, 0);

        repaint();
    }

    class MyMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            Point2D p = event.getPoint();

            System.out.println("мышка кликнута");

            if (checkClicking(p)) { // если пользователь нажал на клетку

                if (!wasFirstClick) { // если клик первый

                    b.setFirstCell(firstCell);
                    if (!b.lightAfterFirstClick()) { // если пользователь нажал не на подсвеченную шашку
                        return; // первый клик все еще остается первым
                    }
                    //иначе подсвечиваются все доступные для данной шашки ходы
                    wasFirstClick = true;
                    reScan();

                    return;
                }
                // если клик второй
                b.setSecondCell(secondCell);

                if (!b.afterSecondClick()) {
                    // пользователь кликнул на ту же самую шашку или кликнул туда, куда походить не может
                    // или кликнул на другую свою шашку которая не обязана рубить и старая шашка была обязана рубить
                    // или кликнул на другую свою шашку, которая не имеет возможности куда-либо сходить
                    // или кликнул на дургую своб шашку, которая имеет доступные ходы
                    // или если пользователь срубил и есть еще клетки куда можно срубить данной шашке
                    // или пользователь кликнул на другую свою шашку, которая так же как и предидущая шашка может срубить
                    reScan();

                    return;
                }
                // если шашка сходила или срубила кого-то то меняется tern
                wasFirstClick = false;
                reScan();

                if (Objects.equals(b.getGameMode(), "против компьютера")) {
                    b.makeComputerMove(); // компьютер делает ход

                    if (b.isGameRestarted()) { //// !!!!
                        restartGame();
                    }

                    board = b.getBoard();
                    reScan();
                }
                b.lightAvailableCheckers(); // подсветить клетки которые могут куда-то сходить или кого-то срубить

                if (b.isGameRestarted()) { //// !!!!
                    restartGame();
                }

                board = b.getBoard(); // на случай если началась новая игра
                reScan();

                System.out.println("вы походили");
            }
        }
    }

    private void restartGame() {
        System.out.println(b.getGameResult());
        System.out.println("НАЧАЛАСЬ НОВАЯ ИГРА");

        JOptionPane.showMessageDialog(new JFrame(), b.getGameResult());

        b.restartInit();
    }

    private boolean checkClicking(Point2D p) {
        int i = 0;

        while (i < 8) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) // если клетка белая
                    continue;

                if (squares[i][j].contains(p)) {
                    if (wasFirstClick) {
                        Cell curCell = firstCell;
                        setFirstCell(i, j);
                        secondCell = firstCell;
                        firstCell = curCell;
                        return true;
                    }
                    setFirstCell(i, j);
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    private void setFirstCell(int iTable, int jTable) {
        switch (iTable) {
            case (0) -> {
                switch (jTable) {
                    case (1) -> firstCell = board[3][6];
                    case (3) -> firstCell = board[2][4];
                    case (5) -> firstCell = board[1][2];
                    case (7) -> firstCell = board[0][0];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (1) -> {
                switch (jTable) {
                    case (0) -> firstCell = board[4][6];
                    case (2) -> firstCell = board[3][5];
                    case (4) -> firstCell = board[2][3];
                    case (6) -> firstCell = board[1][1];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (2) -> {
                switch (jTable) {
                    case (1) -> firstCell = board[4][5];
                    case (3) -> firstCell = board[3][4];
                    case (5) -> firstCell = board[2][2];
                    case (7) -> firstCell = board[1][0];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (3) -> {
                switch (jTable) {
                    case (0) -> firstCell = board[5][4];
                    case (2) -> firstCell = board[4][4];
                    case (4) -> firstCell = board[3][3];
                    case (6) -> firstCell = board[2][1];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (4) -> {
                switch (jTable) {
                    case (1) -> firstCell = board[5][3];
                    case (3) -> firstCell = board[4][3];
                    case (5) -> firstCell = board[3][2];
                    case (7) -> firstCell = board[2][0];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (5) -> {
                switch (jTable) {
                    case (0) -> firstCell = board[6][2];
                    case (2) -> firstCell = board[5][2];
                    case (4) -> firstCell = board[4][2];
                    case (6) -> firstCell = board[3][1];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (6) -> {
                switch (jTable) {
                    case (1) -> firstCell = board[6][1];
                    case (3) -> firstCell = board[5][1];
                    case (5) -> firstCell = board[4][1];
                    case (7) -> firstCell = board[3][0];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
            case (7) -> {
                switch (jTable) {
                    case (0) -> firstCell = board[7][0];
                    case (2) -> firstCell = board[6][0];
                    case (4) -> firstCell = board[5][0];
                    case (6) -> firstCell = board[4][0];
                    default -> throw new IllegalStateException("Unexpected value: " + jTable);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < 8; i++) {  //Отрисовка шахматной доски
            for (int j = 0; j < 8; j++) {
                g.setColor(squares[i][j].getColor());
                g2.fill(squares[i][j]);
            }
        }


        for (Ellipse2D els : blackFigures) {
            g.setColor(Color.BLACK);         //отрисовка черных фигур
            g2.fill(els);
        }

        for (Ellipse2D els : whiteFigures) {
            g.setColor(Color.WHITE);          //отрисовка белых фигур
            g2.fill(els);
        }

        for (Ellipse2D els : yellowFigures) {
            g.setColor(Color.YELLOW);          //отрисовка желтых фигур
            g2.fill(els);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, 40));

        g2.drawString("A", (float) size / 3 + size * 0, size * (float) 8.4);
        g2.drawString("B", (float) size / 3 + size * 1, size * (float) 8.4);
        g2.drawString("C", (float) size / 3 + size * 2, size * (float) 8.4);
        g2.drawString("D", (float) size / 3 + size * 3, size * (float) 8.4);
        g2.drawString("E", (float) size / 3 + size * 4, size * (float) 8.4);
        g2.drawString("F", (float) size / 3 + size * 5, size * (float) 8.4);
        g2.drawString("G", (float) size / 3 + size * 6, size * (float) 8.4);
        g2.drawString("H", (float) size / 3 + size * 7, size * (float) 8.4);

        g2.drawString("8", size * (float) 8.2, (float) size / 2 + size * 0);
        g2.drawString("7", size * (float) 8.2, (float) size / 2 + size * 1);
        g2.drawString("6", size * (float) 8.2, (float) size / 2 + size * 2);
        g2.drawString("5", size * (float) 8.2, (float) size / 2 + size * 3);
        g2.drawString("4", size * (float) 8.2, (float) size / 2 + size * 4);
        g2.drawString("3", size * (float) 8.2, (float) size / 2 + size * 5);
        g2.drawString("2", size * (float) 8.2, (float) size / 2 + size * 6);
        g2.drawString("1", size * (float) 8.2, (float) size / 2 + size * 7);


        history.setText(b.getMoveHistoryToString());
    }
}