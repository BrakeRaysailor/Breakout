import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Breakout extends GraphicsProgram {

    private Ball ball;
    private Paddle paddle;
    private int lives = 3;
    private int score = 0;
    private int brickHealth;

    private int numBricksInRow;

    private GLabel livesLabel;

    private Color[] rowColors = {Color.RED,  Color.ORANGE, Color.YELLOW,  Color.GREEN, Color.CYAN, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};

    @Override
    public void init(){
// this is a comment

        numBricksInRow = (int) (getWidth() / (Brick.WIDTH + 5.0));

        for (int row = 0; row < 10; row++) {

            for (int col = 0; col < numBricksInRow; col++) {
                brickHealth = row - 1;

                double brickX = 10 + col * (Brick.WIDTH + 5);
                double brickY = Brick.HEIGHT + row * (Brick.HEIGHT + 5);

                Brick brick = new Brick(brickX, brickY, rowColors[row], row);
                add(brick);
            }
        }

        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50 ,10);
        add(paddle);

        livesLabel = new GLabel("Lives: " + (lives),getWidth()/2 - 22,10);
        add(livesLabel);
    }

    @Override
    public void run(){
        addMouseListeners();
        waitForClick();
        gameLoop();
    }


    @Override
    public void mouseMoved(MouseEvent me){
        // make sure that the paddle doesn't go offscreen
        if((me.getX() < getWidth() - paddle.getWidth()/2)&&(me.getX() > paddle.getWidth() / 2)){
            paddle.setLocation(me.getX() - paddle.getWidth()/2, paddle.getY());
        }
    }

    private void gameLoop(){
        while(true){
            ball.handleMove();

            handleCollisions();

            if(ball.lost){
                handleLoss();

            }

            pause(5);
        }
    }

    private void handleCollisions(){
        // obj can store what was hit
        GObject obj = null;

        if(obj == null){
            // check top right
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY());
        }

        if(obj == null){
            // check bottom right
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY()+ball.getHeight());
        }

        if(obj == null){
            // check bottom left
            obj = this.getElementAt(ball.getX(), ball.getY()+ball.getHeight());
        }

        if(obj == null){
            // check top left
            obj = this.getElementAt(ball.getX(), ball.getY());
        }

        if(obj != null){
            if(obj instanceof Paddle){
                if(ball.getX() < (paddle.getX() + (paddle.getWidth() * .2))){
                    //Left side?
                    ball.bounceLeft();
                } else if(ball.getX() > (paddle.getX() + (paddle.getWidth() * .8))){
                    //Right side?
                    ball.bounceRight();
                } else {
                    //Middle?
                    ball.bounce();
                }
            }

            if(obj instanceof Brick){
                    ball.bounce();
                    if(this.brickHealth <= 0){
                        this.remove(obj);
                    }else{
                        this.brickHealth -= 1;
                        ((Brick) obj).setFillColor(rowColors[8 - this.brickHealth]);
                    }

            }

        }
    }

    private void handleLoss(){
        ball.lost = false;
        lives -= 1;
        livesLabel.setLabel("Lives: " + lives);

        reset();
    }

    private void reset(){
        ball.setLocation(getWidth()/2 - ball.getWidth()/2, 350);
        paddle.setLocation(230, 430);
        if(lives<=0){
            removeAll();
            add(new GLabel("Score: " + score),getWidth()/2-10, getHeight()/2-5);

            return;
        }

        waitForClick();
    }

    public static void main(String[] args) {
        new Breakout().start();
    }

}