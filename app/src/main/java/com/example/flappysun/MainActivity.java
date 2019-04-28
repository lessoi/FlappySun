package com.example.flappysun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    Game gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * The "game" function. When the function is called, it initialises a Game.class instance and starts a new game.
     * @param view The game view.
     */
    public void game(View view) {
        gameView = new Game(this);
        setContentView(gameView);
    }
}