package com.tylersuehr.bubblesexample;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tylersuehr.bubbles.BubbleLayout;

import java.util.Random;
/**
 * Copyright 2017 Tyler Suehr
 * Created by tyler on 6/9/2017.
 */
public class MainActivity extends AppCompatActivity {
    @DrawableRes
    private int[] drs = new int[] {
            R.drawable.img_profile_0, R.drawable.img_profile_1, R.drawable.img_profile_2,
            R.drawable.img_profile_3, R.drawable.img_profile_4, R.drawable.img_profile_5,
            R.drawable.img_profile_6, R.drawable.img_profile_7, R.drawable.img_profile_8 };

    private BubbleLayout bubbleLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Let's dynamically test our BubbleLayout!
        this.bubbleLayout = (BubbleLayout) findViewById(R.id.bubble_example_4);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_bubble:
                this.bubbleLayout.addBubble(drs[new Random().nextInt(drs.length)]);
                break;
            case R.id.button_clear_bubbles:
                this.bubbleLayout.clearBubbles();
                break;
        }
    }
}