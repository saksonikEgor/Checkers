package checkers.view;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MyRectangle2D extends Rectangle2D.Double {
    private Color color;

    public MyRectangle2D(double x, double y, double w, double h) {
        super(x, y, w, h);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}