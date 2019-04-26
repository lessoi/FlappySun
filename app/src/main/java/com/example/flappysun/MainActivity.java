package com.example.flappysun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import static android.view.View.INVISIBLE;
import static android.view.View.resolveSize;

public class MainActivity extends Activity {
    Game gameView;
    ImageButton restartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restartButton = (ImageButton) findViewById(R.id.imageButton3);
        restartButton.setVisibility(INVISIBLE);
    }

    /** The "game" function. When the function is called, it initialises a Game.class instance and starts a new game.
     * @param view The game view.
     */
    public void game(View view) {
        gameView = new Game(this);
        setContentView(gameView);
    }

    /** The function to check if the current game is over. If so, try to start a new game.
     */
    public void static okToRestart() {
        ImageButton
    }
}