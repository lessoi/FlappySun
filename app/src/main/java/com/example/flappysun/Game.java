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
 * The Game.class which extends View.class would be the main class where we start our game by drawing everything on a canvas with onDraw() method.
 */
public class Game extends View {
    /**
     * Initialise the screen as a Display.
     */
    Display screen;

    /**
     * Initialise a runnable to imply multi-threads.
     */
    Runnable runnable;

    /**
     * Initialise a handle to deal with message.
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

    /**
     * Initialise the image of the obstacles.
     */
    Bitmap upper = BitmapFactory.decodeResource(getResources(), R.drawable.hightube);
    Bitmap lower = BitmapFactory.decodeResource(getResources(), R.drawable.lowtube);

    /**
     * Initialise some important indexes, such as the speed of our bird (positive as going down and negative as going up), gravity, and its xPosition and yPosition.
     */
    private int speed;
    private int gravity = 1;
    private int xPosition;
    private int yPosition;
    private int gap = 500;
    private int[] obsX = new int[4];
    private int[] upperY = new int[4];
    private int[] lowerY = new int[4];
    private int minHeight;
    private int maxHeight;
    private int distance = 50;
    private int gameSpeed = 10;

    Random random = new Random();

    //private ImageButton restartButton;

    /**
     * The constructor.
     *
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

        //Get the screen here.
        screen = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        screenPoint = new Point();
        screen.getSize(screenPoint);

        //Get the size of our screen. The size of our background depends on these indexes.
        screenSize = new Rect(0, 0, screenPoint.x, screenPoint.y);

        //The position of our bird should be at exactly the middle of our screen at the beginning.
        xPosition = (screenPoint.x - sun.getWidth()) / 2;
        yPosition = (screenPoint.y - sun.getHeight()) / 2;
        distance = (screenPoint.x * 3) / 4;
        minHeight = gap;
        maxHeight = screenPoint.y - gap;
        for (int i = 0; i <= 3; i++) {
            obsX[i] = screenPoint.x + i * distance;
            upperY[i] = minHeight + random.nextInt(maxHeight - minHeight);
            lowerY[i] = upperY[i] + gap;
        }
    }

    /**
     * The boolean to judge if the bird is dead. If so, the game should be over immediately, or it continues.
     */
    private boolean dead = false;

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
        if (yPosition <= screenPoint.y - sun.getHeight() && yPosition >= 0 && !(hit(xPosition, yPosition))) {
            for (int i = 0; i <= 3; i++) {
                obsX[i] -= gameSpeed;
                if (obsX[i] <= - upper.getWidth()) {
                    obsX[i] += 4 * distance;
                    upperY[i] = minHeight + random.nextInt(maxHeight - minHeight);
                    lowerY[i] = upperY[i] + gap;
                }
                canvas.drawBitmap(upper, obsX[i], upperY[i] - upper.getHeight(), null);
                canvas.drawBitmap(lower, obsX[i], lowerY[i], null);
            }
            speed += gravity;
            yPosition += speed;
        } else {
            for (int i = 0; i <= 3; i++) {
                canvas.drawBitmap(upper, obsX[i], upperY[i] - upper.getHeight(), null);
                canvas.drawBitmap(lower, obsX[i], lowerY[i], null);
            }
            canvas.drawBitmap(gameover, (screenPoint.x - gameover.getWidth()) / 2, (screenPoint.y - gameover.getHeight()) / 2, null);
        }


        //Set a delay. It can also work as a timer.
        handler.postDelayed(runnable, 30);
    }

    /**
     * Set a monitor on our screen to judge if the user touches it. If so, the bird should slow down, or fly upwards.
     *
     * @param event The event happened on the screen.
     * @return Returning true means that we have already used this event.
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            speed -= 10;
        }
        return true;
    }

    public boolean hit(int x, int y) {
        boolean hit = false;
        Rect sun = new Rect(x - smilesun.getWidth() / 2, y - smilesun.getHeight() / 2, x + smilesun.getWidth() / 2, y + smilesun.getHeight() / 2);
        List<Rect> obs = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            obs.add(new Rect(obsX[i] - upper.getWidth() / 2, upperY[i] - upper.getHeight() / 2, obsX[i] + upper.getWidth() / 2, upperY[i] + upper.getHeight() / 2));
            obs.add(new Rect(obsX[i] - lower.getWidth() / 2, lowerY[i] - lower.getHeight() / 2, obsX[i] + lower.getWidth() / 2, lowerY[i] + lower.getHeight() / 2));
        }
        for (Rect pipe : obs) {
            if (sun.intersect(pipe)) {
                hit = true;
                break;
            }
        }
        return hit;
    }
}