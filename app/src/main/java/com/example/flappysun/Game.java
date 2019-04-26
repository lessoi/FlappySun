package com.example.flappysun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
     * Initialise some important indexes, such as the speed of our bird (positive as going down and negative as going up), gravity, and its xPosition and yPosition.
     */
    private int speed;
    private int gravity = 1;
    private int xPosition;
    private int yPosition;

    //private ImageButton restartButton;

    /** The constructor.
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


    }
    //MainActivity main = new MainActivity();

    /**
     * The boolean to judge if the bird is dead. If so, the game should be over immediately, or it continues.
     */
    private boolean dead = false;

    /** We override the default onDraw method to draw our background, bird, obstacles, and other images.
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

        //Set a delay. It can also work as a timer.
        handler.postDelayed(runnable, 30);

        //Judge if the bird is outside the screen. If so, the game should be over immediately.
        if (yPosition <= screenPoint.y - sun.getHeight() && yPosition >= 0) {
            speed += gravity;
            yPosition += speed;
        } else {
            dead = true;
            canvas.drawBitmap(gameover, (screenPoint.x - gameover.getWidth()) / 2, (screenPoint.y - gameover.getHeight()) / 2, null);

            //Try to set the Restart button here.
            Intent tryToRestart = get
            MainActivity.restartButton.setVisibility(VISIBLE);

            main.restartButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dead = false;
                    //System.out.println("button works");
                    main.game(v);
                }
            });

        }
    }

    /** Set a monitor on our screen to judge if the user touches it. If so, the bird should slow down, or fly upwards.
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
}