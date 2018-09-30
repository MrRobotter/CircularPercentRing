package com.joinyon.circularpercenring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CircularPercentRing ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = findViewById(R.id.ring);
        initRing();
    }

    private void initRing() {
        ring.setRoundWidth(30);
        int[] colors = {0xFF00AAFF, 0xFF00AAFF, 0xFF00AAFF};
        ring.setColors(colors);
        ring.update(50.0f, 0);
    }

    public void small(View view) {
        ring.setCircleWidth(200);
    }

    public void big(View view) {
        ring.setCircleWidth(320);
    }

    public void larg(View view) {
        ring.setRoundWidth(30);
    }

    public void li(View view) {
        ring.setRoundWidth(20);
    }

    public void half(View view) {
        ring.update(50.0f, 0);
    }

    public void full(View view) {
        ring.update(100.0f, 0);
    }

    public void quik(View view) {
        ring.update(100.0f, 1500);
    }

    public void slow(View view) {
        ring.update(100.0f, 3000);
    }

    public void single(View view) {
        int[] colors = {0xFFED0957, 0xFFED0957, 0xFFED0957};
        ring.setColors(colors);
    }

    public void colorful(View view) {
        int[] colors = {0xFF11F020,0xFFFFDC40, 0xFFE9151F};
        ring.setColors(colors);
    }
}
