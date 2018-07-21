package com.worksplash.akscorp.wordsplash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static String[] easy = { "LUCK",  "CALM", "CARE", "CODE", "BOMB", "ROAD",
            "RUSH", "LAKE", "FLOW" };
    static String[] medium = { "MEDIUM",  "YANDEX", "CAPACITY", "KOTLIN" };
    static String[] hard = { "ABSTRACT",  "ABSINTHE",  "ALLIANCE",  "ACTUALLY",
            "BIRTHDAY", "BALLOON", "BANALITY", "BEAUTIFY", "CALENDAR" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu);

        /*ArrayList<GameResult> gr = new ArrayList<GameResult>(2);
        gr.add(new GameResult(3, "YANDEX", GameResult.Levels.Medium));
        gr.add(new GameResult(2, "BIRTHDAY", GameResult.Levels.Hard));
        ScoreUtils.saveScores(this, gr);*/

        StarAnimationView  mAnimationView = (StarAnimationView) findViewById(R.id.animated_view);
        mAnimationView.resume();

        findViewById(R.id.easy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game1Activity.class);
                intent.putExtra("level", 0);
                startActivity(intent);
            }
        });
        findViewById(R.id.medium_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game1Activity.class);
                intent.putExtra("level", 1);
                startActivity(intent);
            }
        });
        findViewById(R.id.hard_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game1Activity.class);
                intent.putExtra("level", 2);
                startActivity(intent);
            }
        });
        findViewById(R.id.history_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);

                startActivity(intent);
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);

                startActivity(intent);
            }
        });

    }
}
