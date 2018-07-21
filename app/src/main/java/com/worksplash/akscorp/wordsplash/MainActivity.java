package com.worksplash.akscorp.wordsplash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        List<GameResult> _list = new ArrayList<>();
        _list.add(new GameResult(1,"word", GameResult.Levels.Easy));
        _list.add(new GameResult(2,"test", GameResult.Levels.Medium));
        _list.add(new GameResult(3,"android", GameResult.Levels.Hard));
        _list.add(new GameResult(2,"list", GameResult.Levels.Easy));
        ScoreUtils.saveScores(this, _list);

    }
}
