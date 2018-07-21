package com.worksplash.akscorp.wordsplash;

import android.content.Context;
import android.graphics.Color;
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

import java.util.Timer;
import java.util.TimerTask;

public class Game1Activity extends AppCompatActivity {
    String word;
    FrameLayout[] flCharHolder;
    TextView[] tvHolder;
    GameAnimationView mAnimationView;
    TextView tvTimer;
    int time;

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //write UI related code in here
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

        word = "CODING";

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game1);

        FrameLayout flContent = findViewById(R.id.fl_content);



        LinearLayout linearLayout = findViewById(R.id.ll_word);
        flCharHolder = new FrameLayout[word.length()];
        tvHolder = new TextView[word.length()];

        tvTimer = findViewById(R.id.tv_timer);


        for(int i = 0; i < word.length(); ++i){
            FrameLayout.LayoutParams llPar = new FrameLayout.LayoutParams(spToPx(35, this), ViewGroup.LayoutParams.MATCH_PARENT);
            llPar.setMargins(spToPx(5, this), 0, spToPx(5, this), 0);
            llPar.gravity = Gravity.CENTER;
            flCharHolder[i] = new FrameLayout(this);
            flCharHolder[i].setBackgroundColor(Color.argb(255, 255, 255, 255));

            flCharHolder[i].setLayoutParams(llPar);

            tvHolder[i] = new TextView(this);
            tvHolder[i].setTextSize(35);
            tvHolder[i].setTextColor(Color.BLACK);
            tvHolder[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //tvHolder[i].setText(String.valueOf(word.toCharArray()[i]));

            flCharHolder[i].addView(tvHolder[i]);

            linearLayout.addView(flCharHolder[i]);
        }

        mAnimationView = new GameAnimationView(this, "CODING", tvHolder);
        mAnimationView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        flContent.addView(mAnimationView);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        time = 0;
        Timer tmr = new Timer();
        tmr.scheduleAtFixedRate(new MyTimerTask(), 0, 1000);
    }
}
