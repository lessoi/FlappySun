package com.example.flappysun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * The "game" function. When the function is called, it initialises a Game.class instance and starts a new game.
     *
     * @param view The game view.
     */
    public void game(View view) {
        Intent intent = new Intent(this, SetUp.class);
        startActivity(intent);
        finish();
    }
}