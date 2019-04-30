package com.example.flappysun;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Game.class which extends View.class would be where we start our game by drawing everything on a canvas with onDraw() method.
 */
public class Game extends View {
    /**
     * Initialise the screen as a Display.
     */
    Display screen;

    /**
     * Initialise a runnable..
     */
    Runnable runnable;

    /**
     * Initialise a handler to hold up and put a runnable into message queue.
     */
    Handler handler;

    /**
     * Initialise the background by using BitmapFactory.decodeResource(getResources(), imageLocation) method.
     */
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.chengdu);

    /**
     * Initialise the point of the screen, which helps us to adjust the size of our background image.
     */
    Point screenPoint;

    /**
     * Initialise a rectangular. This rectangular should be exactly as big as our screen.
     */
    Rect screenSize;

    /**
     * Initialise the image of our "bird".
     */
    Bitmap sun = BitmapFactory.decodeResource(getResources(), R.drawable.sun);

    /**
     * Initialise another image of our "bird". Our bird should smile when it is going up, so we name it as smilesun.
     */
    Bitmap smilesun = BitmapFactory.decodeResource(getResources(), R.drawable.smilesun);

    /**
     * Initialise the image that appears when the game is over.
     */
    Bitmap gameover = BitmapFactory.decodeResource(getResources(), R.drawable.wasted);
    Bitmap restart = BitmapFactory.decodeResource(getResources(), R.drawable.restart);

    /**
     * Initialise the image of the obstacles.
     */
    Bitmap upper = BitmapFactory.decodeResource(getResources(), R.drawable.hightube);
    Bitmap lower = BitmapFactory.decodeResource(getResources(), R.drawable.lowtube);


    /**
     * Initialise the speed and gravity.
     */
    private int speed;
    public int gravity = 1;

    /**
     * Initialise the position of the bird.
     */
    private int xPosition;
    private int yPosition;

    /**
     * Initialise the position of the obstacles, which should be stored in Int arrays.
     */
    private int gap = 2 * sun.getHeight();
    private int[] obsX = new int[4];
    private int[] upperY = new int[4];
    private int[] lowerY = new int[4];
    private int distance = 600;

    /**
     * The game speed controls the speed of the obstacles.
     */
    public int gameSpeed = 5;

    /**
     * The random is used to generate random position indexes for the obstacles.
     */
    Random random = new Random();

    /**
     * This boolean judges the state of our game. If the game is over, we are okay to restart.
     */
    public boolean okToRestart = false;

    /**
     * This function could get the sizes of our screen.
     */
    public void getScreen() {

        //Get the screen here.
        screen = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        screenPoint = new Point();
        screen.getSize(screenPoint);

        //Get the size of our screen. The size of our background depends on these indexes.
        screenSize = new Rect(0, 0, screenPoint.x, screenPoint.y);
    }

    /**
     * This function initialises the position of the "bird" when game starts.
     */
    public void positionInitializer() {
        xPosition = (screenPoint.x - sun.getWidth()) / 2;
        yPosition = (screenPoint.y - sun.getHeight()) / 2;
    }

    public void generateObstacles() {
        for (int i = 0; i <= 3; i++) {
            obsX[i] = screenPoint.x + i * distance;
            upperY[i] = random.nextInt(screenPoint.y - gap);
            lowerY[i] = upperY[i] + gap;
        }
    }

    /**
     * The constructor.
     * @param context The context.
     */
    Game(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        getScreen();
        positionInitializer();
        generateObstacles();
    }

    /**
     * The Hit-Box function. We store all the hit-box of the pipes as Rect(s) in a ArrayList with type parameter Rect.
     * @param x The x-Position of the "bird".
     * @param y The y-Position of the "bird".
     * @return A boolean named hit. If the hit-box of the bird intersects with any of the hit-boxes stored ArrayList, it jumps out from the loop and return true, or return false otherwise.
     */
    public boolean hit(int x, int y) {
        boolean hit = false;
        Rect sun = new Rect(x - (smilesun.getWidth() / 2), y, x + (smilesun.getWidth() / 2), y + smilesun.getHeight());
        List<Rect> obs = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            obs.add(new Rect(obsX[i] - upper.getWidth() / 2, 0, obsX[i] + upper.getWidth() / 2, upperY[i]));
            obs.add(new Rect(obsX[i] - upper.getWidth() / 2, lowerY[i], obsX[i] + upper.getWidth() / 2, screenPoint.y));
        }
        for (Rect pipe : obs) {
            if (sun.intersect(pipe)) {
                hit = true;
                break;
            }
        }
        return hit;
    }

    /**
     * We override the default onDraw method to draw our background, bird, obstacles, and other images.
     *
     * @param canvas The canvas. (our screen)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw the background here.
        canvas.drawBitmap(background, null, screenSize, null);

        //If the speed is negative, the bird should smile. Or it should post a simalian.
        if (speed < 0) {
            canvas.drawBitmap(smilesun, xPosition, yPosition, null);
        } else {
            canvas.drawBitmap(sun, xPosition, yPosition, null);
        }

        //Check if the game should continue or is already over.
        if (yPosition <= screenPoint.y - sun.getHeight() && yPosition >= 0 && !hit(xPosition, yPosition)) {

            //The game continues. We need to move all the obstacles to the left. If the obstacle is outside the left bound of the screen, we need to put it back to the far right.
            for (int i = 0; i <= 3; i++) {
                obsX[i] -= gameSpeed;
                if (obsX[i] <= - upper.getWidth()) {
                    obsX[i] += 4 * distance;
                    upperY[i] = random.nextInt(screenPoint.y - gap);
                    lowerY[i] = upperY[i] + gap;
                }

                canvas.drawBitmap(upper, obsX[i], upperY[i] - upper.getHeight(), null);
                canvas.drawBitmap(lower, obsX[i], lowerY[i], null);
            }

            //The bird goes down.
            int speed0 = speed;
            speed += gravity;
            yPosition += (speed + speed0) / 2;
        } else {

            //The game can be restarted now.
            okToRestart = true;

            //But we need to keep everything on the screen...
            for (int i = 0; i <= 3; i++) {
                canvas.drawBitmap(upper, obsX[i], upperY[i] - upper.getHeight(), null);
                canvas.drawBitmap(lower, obsX[i], lowerY[i], null);
            }

            //Show the gameOver image and restart "button".
            canvas.drawBitmap(gameover, (screenPoint.x - gameover.getWidth()) / 2, (screenPoint.y - gameover.getHeight()) / 2 - gameover.getHeight(), null);
            canvas.drawBitmap(restart, (screenPoint.x - restart.getWidth()) / 2, (screenPoint.y - gameover.getHeight()) / 2 + gameover.getHeight(), null);
        }

        //Set the timer. 16 ms can guarantee 60 fps.
        handler.postDelayed(runnable, 16);
    }

    /**
     * Set a monitor on our screen to judge if the user touches it. If so, the bird should slow down, or fly upwards.
     * @param event The event happened on the screen.
     * @return Returning true means that we have already used this event.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //When the game is on, the bird should fly up when we touch the screen.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            speed -= 9;
        }

        //But when the game is over, we can restart the game if we touch the restart "button".
        if (event.getAction() == MotionEvent.ACTION_DOWN && okToRestart) {
            int x = (int)event.getX();
            int y = (int)event.getY();
            Rect ha = new Rect((screenPoint.x - restart.getWidth()) / 2, (screenPoint.y - gameover.getHeight()) / 2 + gameover.getHeight(), (screenPoint.x - restart.getWidth()) / 2 + restart.getWidth(), (screenPoint.y - gameover.getHeight()) / 2 + gameover.getHeight() + restart.getHeight());
            if (ha.contains(x, y)) {
                ((MainActivity)this.getContext()).game(this);
            }
        }
        return true;
    }
}
