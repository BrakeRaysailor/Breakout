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
    private int row;
    private Brick brick;

    private int numBricksInRow;

    private GLabel livesLabel;
    private GLabel scoreLabel;

    private Color[] rowColors = {Color.RED,  Color.ORANGE, Color.YELLOW,  Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.DARK_GRAY, Color.BLACK};

    @Override
    public void init(){

        numBricksInRow = (int) (getWidth() / (Brick.WIDTH + 5.0));
        for (int row = 0; row < 10; row++) {
            this.row = row;

            for (int col = 0; col < numBricksInRow; col++) {

                double brickX = 10 + col * (Brick.WIDTH + 5);
                double brickY = Brick.HEIGHT + row * (Brick.HEIGHT + 5);

                brick = new Brick(brickX, brickY, rowColors[9-row], 10-row);
                add(brick);
            }
        }

        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50 ,10);
        add(paddle);

        livesLabel = new GLabel("Lives: " + (lives),getWidth()/3 - 22,10);
        add(livesLabel);

        scoreLabel = new GLabel("Score: " + (score), getWidth()*2/3 - 22, 10);
        add(scoreLabel);
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
                    ((Brick) obj).health -= 1;
                    score += 10;
                    scoreLabel.setLabel("Score: " + score);
                    if(((Brick) obj).health>0){((Brick) obj).setFillColor(rowColors[((Brick) obj).health - 1]);}
                    if(((Brick) obj).health <= 0){
                        this.remove(obj);
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