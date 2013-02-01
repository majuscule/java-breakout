/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Random;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

    private static double vx, vy;

    private static final int BALL_SPEED = 1;

    private static GRect paddle;

    private static final int PADDLE_SPEED = 15;

    private static int paddleControl = 0;

    private static final double GRAVITY = .4;

    private static boolean ROUND_STARTED = false;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
        addKeyListeners();
        createBoard();
        int bricksLeft = NBRICKS_PER_ROW * NBRICK_ROWS;
        paddle = createPaddle();
        GOval ball = createBall();
        vx = (int)(Math.random() * ((20 - 2) + 1));
        vy = BALL_SPEED;
        GLabel startMsg = new GLabel("PRESS SPACE TO PLAY");
        startMsg.setColor(Color.white);
        startMsg.setLocation(getWidth()/2 - 63,
                            NBRICK_ROWS*(BRICK_WIDTH + BRICK_SEP));
        add(startMsg);
        while (true) {
            // WTF. Somehow this System.out call is flushing ROUND_STARTED from keyPressed
            System.out.println();
            if (ROUND_STARTED) {
                remove(startMsg);
                tick(ball, paddle, bricksLeft);
                pause(30);
            }
        }
    }

    private void tick(GOval ball, GRect paddle, int bricksLeft) {
        ball.move(vx, vy);
        vy += GRAVITY;
        if (paddleControl == 1 && paddle.getX() + PADDLE_WIDTH < getWidth())
            paddle.move(PADDLE_SPEED, 0);
        if (paddleControl == -1 && paddle.getX() > 0)
            paddle.move(-PADDLE_SPEED, 0);
        GRectangle bounds = ball.getBounds();
        double x = bounds.getX();
        double y = bounds.getY();
        GObject collision = getElementAt(x, y);
        if (collision == null) collision = getElementAt(x+BRICK_WIDTH, y);
        if (collision == null) collision = getElementAt(x, y+BRICK_HEIGHT);
        if (collision == null) collision = getElementAt(x+BRICK_WIDTH, y+BRICK_HEIGHT);
        if (collision == paddle) {
            vy = -vy;
            // be a little random
            if (paddleControl != 0) vx += (int)(Math.random() * ((5 - -5) + 1));
            // don't be dull
            if (vy < BALL_SPEED*2) vy -= (int)(Math.random() * ((5 - 0) + 1));
        } else if (collision != null) {
            remove(collision);
            if (--bricksLeft == 0) {
                GLabel winMsg = new GLabel("YOU WIN!!");
                winMsg.setColor(Color.green);
                winMsg.setLocation(getWidth()/2 - 25,
                                    NBRICK_ROWS*(BRICK_WIDTH + BRICK_SEP));
                add(winMsg);
                remove(ball);
            }
            vy = -vy-GRAVITY;
        }
        if (x < 0 || x+BALL_RADIUS > getWidth())
            vx = -vx;
        else if (y < 0)
            vy = -vy;
        else if (y > getHeight()) {
            ball.setLocation(getWidth()/2 - BALL_RADIUS, 
                    BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP)*NBRICK_ROWS + BALL_RADIUS*5);
            vy = BALL_SPEED + (int)(Math.random() * ((5 - 0) + 1));
            vx = (int)(Math.random() * ((20 - 2) + 1));
            ROUND_STARTED = false;
        }
    }

    private void createBoard() {
        setBackground(Color.black);

        int leftOverSpace = getWidth() - (NBRICKS_PER_ROW * (BRICK_WIDTH + BRICK_SEP)) + 2;

        for (int i = NBRICKS_PER_ROW; i > 0; i--) {
            for (int ii = 0; ii < NBRICK_ROWS; ii++) {
                GRect rectangle = new GRect(leftOverSpace/2 + BRICK_WIDTH*ii + BRICK_SEP*ii,
                                            BRICK_Y_OFFSET + BRICK_HEIGHT*i + BRICK_SEP*i - 1,
                                            BRICK_WIDTH,
                                            BRICK_HEIGHT);
                rectangle.setFilled(true);
                switch (i) {
                    case 1:
                    case 2:
                        rectangle.setColor(Color.red); break;
                    case 3:
                    case 4:
                        rectangle.setColor(Color.orange); break;
                    case 5:
                    case 6:
                        rectangle.setColor(Color.yellow); break;
                    case 7:
                    case 8:
                        rectangle.setColor(Color.green); break;
                    case 9:
                    case 10:
                        rectangle.setColor(Color.blue); break;
                }

                add(rectangle);
            }
        }
    }

    private GRect createPaddle() {
        GRect paddle = new GRect(getWidth()/2 - PADDLE_WIDTH/2,
                                    getHeight() - PADDLE_Y_OFFSET,
                                    PADDLE_WIDTH,
                                    PADDLE_HEIGHT);
        paddle.setFilled(true);
        paddle.setColor(Color.white);
        add(paddle);
        return paddle;
    }

    private GOval createBall() {
        GOval ball = new GOval(getWidth()/2 - BALL_RADIUS/2,
                                BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * NBRICK_ROWS + BALL_RADIUS*2,
                                BALL_RADIUS, BALL_RADIUS);
        ball.setFilled(true);
        ball.setColor(Color.white);
        add(ball);
        return ball;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            paddleControl = 1;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            paddleControl = -1;
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            ROUND_STARTED = true;
    }

    public void keyReleased(KeyEvent e) {
        paddleControl = 0;
    }

}
