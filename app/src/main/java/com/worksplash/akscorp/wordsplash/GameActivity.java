package com.worksplash.akscorp.wordsplash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DialogScoreFragment dialogScoreFragment = DialogScoreFragment.newInstance(new GameResult(2, "test", GameResult.Levels.Easy));
//        DialogScoreFragment dialogScoreFragment = new DialogScoreFragment();
        dialogScoreFragment.show(getSupportFragmentManager(), "");
    }
}
