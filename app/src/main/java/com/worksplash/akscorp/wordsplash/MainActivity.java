package com.worksplash.akscorp.wordsplash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        StarAnimationView  mAnimationView = (StarAnimationView) findViewById(R.id.animated_view);
        mAnimationView.resume();

        findViewById(R.id.easy_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Game1Activity.class);
                startActivity(intent);
            }
        });
    }
}
