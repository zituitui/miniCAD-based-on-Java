import java.awt.*;
import java.io.Serializable;

public class Oval extends Shape implements Serializable {
    // parameter width and height stand for 2a and 2b
    private int width, height;

    public Oval(String name, int cx, int cy, int width, int height, Color color) {
        super(name, cx, cy, color);
        this.width = width;
        this.height = height;
    }

    public boolean in(int x, int y) {
        // actually judge if in the rectangle
        return x >= cx && x <= cx + width && y >= cy && y <= cy + height;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(size));
        // Draws the outline of an oval. The result is a circle or ellipse that fits
        // within the rectangle specified by the x, y, width, and height arguments.
        g.drawOval(cx, cy, width, height);
    }
}
