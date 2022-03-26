import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {

    protected int cx, cy; // stand for leftmost up point
    protected Color color;
    protected float size;
    protected String name; // "Line" "Rectangle" etc

    // cons func
    public Shape(String name, int cx, int cy, Color color) {
        this.name = name;
        this.cx = cx;
        this.cy = cy;
        this.color = color;
        size = 3.0f;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int deltax, int deltay) {
        cx += deltax;
        cy += deltay;
    }

    public float changeSize(float ds) {
        if (this.size + ds > 10.0f)
            this.size = 10.0f;
        else if (this.size + ds < 1.0f)
            this.size = 1.0f;
        else
            this.size += ds;
        return this.size;
    }

    public abstract void draw(Graphics g);

    public abstract boolean in(int curX, int curY);
}
