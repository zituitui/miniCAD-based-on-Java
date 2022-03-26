import java.awt.*;
import java.io.Serializable;

public class Line extends Shape implements Serializable {
    // the another point of the line
    // on x-axis and y-axis
    private int dx, dy;

    // change center and the end point
    public void move(int deltax, int deltay) {
        super.move(deltax, deltay);
        dx += deltax;
        dy += deltay;
    }

    public Line(String name, int cx, int cy, int dx, int dy, Color color) {
        super(name, cx, cy, color);
        this.dx = dx;
        this.dy = dy;
    }

    // junge if this point is near line
    public boolean in(int x, int y) {
        int px = x - cx, py = y - cy;
        int qx = dx - cx, qy = dy - cy;
        int S = (px * qy - qx * py);
        double d = S * S / (qx * qx + qy * qy);
        return d <= 8.0;
    }

    // draw the line
    public void draw(Graphics g) {
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(size));
        g.drawLine(cx, cy, dx, dy);
        // ((Graphics2D)g).setStroke(new BasicStroke(1.0f));
    }
}
