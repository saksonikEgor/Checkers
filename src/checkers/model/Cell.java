package checkers.model;

public class Cell {
    private final String name;
    private int color;// 1 white, -1 black, 0 void
    private boolean queen = false;
    private boolean light = false;
    private boolean dead = false;

    private final int firstDiagonal;
    private final int secondDiagonal;

    public Cell(int firstDiagonal, int secondDiagonal, String name, int color) {
        this.name = name;
        this.color = color;

        this.firstDiagonal = firstDiagonal;
        this.secondDiagonal = secondDiagonal;
    }

    public void changeCell(int color, boolean queen) {
        this.color = color;
        this.queen = queen;
    }

    public void lightOn() {
        light = true;
    }

    public void lightOff() {
        light = false;
    }

    public boolean isLight() {
        return light;
    }

    public int getColor() {
        return color;
    }

    public boolean isQueen() {
        return queen;
    }

    public void makeQueen() {
        queen = true;
    }

    public void makeDead() {
        dead = true;
    }

    public void revive() {
        dead = false;
    }

    public boolean isDead() {
        return dead;
    }

    public String getName() {
        return name;
    }

    public int[] getDiagonals() {
        int[] result = new int[2];

        result[0] = firstDiagonal;
        result[1] = secondDiagonal;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Cell cell))
            return false;

        return cell.name.equals(this.name) && cell.color == this.color && cell.queen == this.queen
                && cell.light == this.light && cell.dead == this.dead && cell.firstDiagonal == this.firstDiagonal
                && cell.secondDiagonal == this.secondDiagonal;
    }
}