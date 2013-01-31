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

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
        createBoard();
        createPaddle();
        GOval ball = createBall();
        Random randomNumberGenerator = new Random(System.currentTimeMillis() / 1000L);
        vx = randomNumberGenerator.nextInt() % 10;
        vy = 3;
        while (true) {
            ball.move(vx, vy);
            GRectangle bounds = ball.getBounds();
            double x = bounds.getX();
            double y = bounds.getY();
            System.out.println(x);
            if (getElementAt(x, y) != null
                || getElementAt(x+BRICK_WIDTH, y) != null
                || getElementAt(x, y+BRICK_HEIGHT) != null
                || getElementAt(x+BRICK_WIDTH, y+BRICK_HEIGHT) != null)
                System.out.println("hit object");
            if (x == 0 || y == 0)
                System.out.println("hit wall");

            pause(50);
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

    private void createPaddle() {
        GRect rectangle = new GRect(getWidth()/2 - PADDLE_WIDTH/2,
                                    getHeight() - PADDLE_Y_OFFSET,
                                    PADDLE_WIDTH,
                                    PADDLE_HEIGHT);
        rectangle.setFilled(true);
        rectangle.setColor(Color.white);
        add(rectangle);
    }

    private GOval createBall() {
        GOval ball = new GOval(getWidth()/2 - BALL_RADIUS/2,
                                BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP)*NBRICK_ROWS + BALL_RADIUS*5,
                                BALL_RADIUS, BALL_RADIUS);
        ball.setFilled(true);
        ball.setColor(Color.white);
        add(ball);
        return ball;
    }
}
