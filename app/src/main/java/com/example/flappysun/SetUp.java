package com.example.flappysun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SetUp extends Activity {
    public View gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up);
    }

    public void chengdu(View view) {
        gameView = new Chengdu(this);
        setContentView(gameView);
    }

    public void newyork(View view) {
        gameView = new NewYork(this);
        setContentView(gameView);
    }

    public void beijing(View view) {
        gameView = new Beijing(this);
        setContentView(gameView);
    }
}
