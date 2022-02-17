import acm.graphics.GRect;
import java.awt.Color;
public class Brick extends GRect{

    public static final int WIDTH = 44;
    public static final int HEIGHT = 20;

    public int health;
    public Color color;

    public Brick(double x, double y, Color color, int health) {
        super(x, y, WIDTH, HEIGHT);
        this.color = color;
        this.setFillColor(color);
        this.setFilled(true);
        this.health = health;
    }

}
