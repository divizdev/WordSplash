package com.worksplash.akscorp.wordsplash;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Game1Activity extends AppCompatActivity {
    String word;
    FrameLayout[] flCharHolder;
    TextView[] tvHolder;
    GameAnimationView mAnimationView;
    TextView tvTimer;
    TextView tvWrong;
    int time;
    int lvl;
    Timer tmr;
    MediaPlayer player;

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //write UI related code in here
                    if(mAnimationView.gameState == 2){
                        tvWrong.setVisibility(View.VISIBLE);
                    }else if(mAnimationView.gameState == 1){
                        DialogScoreFragment fr;
                        GameResult res;
                        if(time < 10){
                            res = new GameResult(3, word, lvl == 0 ? GameResult.Levels.Easy : (lvl == 1 ? GameResult.Levels.Medium : GameResult.Levels.Hard));
                            fr = DialogScoreFragment.newInstance(new GameResult(3, word, lvl == 0 ? GameResult.Levels.Easy : (lvl == 1 ? GameResult.Levels.Medium : GameResult.Levels.Hard)));
                        }else if(time < 20){
                            res = new GameResult(2, word, lvl == 0 ? GameResult.Levels.Easy : (lvl == 1 ? GameResult.Levels.Medium : GameResult.Levels.Hard));
                            fr = DialogScoreFragment.newInstance(new GameResult(2, word, lvl == 0 ? GameResult.Levels.Easy : (lvl == 1 ? GameResult.Levels.Medium : GameResult.Levels.Hard)));
                        }else{
                            res = new GameResult(1, word, lvl == 0 ? GameResult.Levels.Easy : (lvl == 1 ? GameResult.Levels.Medium : GameResult.Levels.Hard));
                            fr = DialogScoreFragment.newInstance(new GameResult(1, word, lvl == 0 ? GameResult.Levels.Easy : (lvl == 1 ? GameResult.Levels.Medium : GameResult.Levels.Hard)));
                        }
                        ScoreUtils.addScores(Game1Activity.this, res);
                        fr.setCancelable(false);
                        fr.show(getSupportFragmentManager(), "STR");
                        tmr.cancel();
                    }
                    tvTimer.setText("00:" + (time < 10 ? "0" : "") + time);
                    time++;
                }
            });
        }
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvl = getIntent().getIntExtra("level", 1);

        switch (lvl){
            case 0:
                word = MainActivity.easy[(int)(Math.random() * MainActivity.easy.length)];
                break;
            case 1:
                word = MainActivity.medium[(int)(Math.random() * MainActivity.medium.length)];
                break;
            case 2:
                word = MainActivity.hard[(int)(Math.random() * MainActivity.hard.length)];
                break;
        }



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game1);

        FrameLayout flContent = findViewById(R.id.fl_content);

        player = MediaPlayer.create(this, R.raw.skyrim_2);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();

        LinearLayout linearLayout = findViewById(R.id.ll_word);
        flCharHolder = new FrameLayout[word.length()];
        tvHolder = new TextView[word.length()];

        tvTimer = findViewById(R.id.tv_timer);

        tvWrong = findViewById(R.id.tv_wrong);


        for(int i = 0; i < word.length(); ++i){
            FrameLayout.LayoutParams llPar = new FrameLayout.LayoutParams(spToPx(30, this), ViewGroup.LayoutParams.MATCH_PARENT);
            llPar.setMargins(spToPx(5, this), 0, spToPx(5, this), 0);
            llPar.gravity = Gravity.CENTER;
            flCharHolder[i] = new FrameLayout(this);
            flCharHolder[i].setBackgroundColor(Color.argb(255, 255, 255, 255));

            flCharHolder[i].setLayoutParams(llPar);

            tvHolder[i] = new TextView(this);
            tvHolder[i].setTextSize(32);
            tvHolder[i].setTextColor(Color.BLACK);
            tvHolder[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //tvHolder[i].setText(String.valueOf(word.toCharArray()[i]));

            flCharHolder[i].addView(tvHolder[i]);

            linearLayout.addView(flCharHolder[i]);
        }

        mAnimationView = new GameAnimationView(this, word, word, tvHolder);
        mAnimationView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        flContent.addView(mAnimationView);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimationView.gameState = 0;
                tvWrong.setVisibility(View.GONE);
                if(mAnimationView.currentPos != 0){
                    mAnimationView.currentPos--;
                    for(GameAnimationView.Star st : mAnimationView.mStars){
                        if(st.letter == mAnimationView.tvHolder[mAnimationView.currentPos].getText().toString().charAt(0)){
                            st.enabled = true;
                            mAnimationView.tvHolder[mAnimationView.currentPos].setText("");
                            break;
                        }
                    }
                }
            }
        });


        mAnimationView.resume();
        StarAnimationView animView = findViewById(R.id.animated_view);
        animView.resume();

        time = 0;
        tmr = new Timer();
        tmr.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}
