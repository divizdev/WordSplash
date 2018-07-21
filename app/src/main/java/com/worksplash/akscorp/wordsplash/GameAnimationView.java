package com.worksplash.akscorp.wordsplash;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

class GameAnimationView extends View {

    private Random random = new Random();
    /**
     * Class representing the state of a star
     */

    private class MyDrawable extends Drawable{
        public void draw(@NonNull Canvas canvas, char chr) {
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(android.R.color.white));
            paint.setTextSize(130);
            canvas.drawText(String.valueOf(chr), 0, 0, paint);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {

        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }
    }

    public class Star {
        private float x;
        private float y;
        private int v;
        private int h;

        public char letter;

        private float speedX;
        private float speedY;
        public boolean enabled = true;
    }

    private static final int BASE_SPEED_DP_PER_S = 40;
    private static final int SEED = 1337;

    public Star[] mStars;
    private final Random mRnd = new Random(SEED);

    private TimeAnimator mTimeAnimator;
    private MyDrawable mDrawable;

    private float mBaseSpeed;
    private float mBaseSize;
    private long mCurrentPlayTime;

    TextView[] tvHolder;

    public String word;

    public Integer currentPos = 0;

    /** @see View#View(Context) */
    public GameAnimationView(Context context, String word, TextView[] tvHolder) {
        super(context);
        this.tvHolder = tvHolder;
        init(word);
    }

    private void init(String word) {
        this.word = word;
        mStars = new Star[word.length()];
        mDrawable = new MyDrawable();
        mBaseSize = Math.max(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight()) / 10f;
        mBaseSpeed = BASE_SPEED_DP_PER_S * getResources().getDisplayMetrics().density;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Log.d("TOUCH_X", "" + event.getX());
            Log.d("TOUCH_Y", "" + event.getY());
            for(int i = 0; i < mStars.length; ++i){
                if(mStars[i].speedX != 0 && mStars[i].speedY != 0){
                    final float starSize = 200;

                    if(mStars[i].enabled && (event.getX() > (mStars[i].x - (starSize / 2))) && (event.getX() < (mStars[i].x + (starSize / 2))) &&
                        (event.getY() > (mStars[i].y - (starSize / 2))) && (event.getY() < (mStars[i].y + (starSize / 2)))){
                        //Log.d("TOUCH", "FOUND LETTER!!!");

                        if(currentPos < word.length()){
                            tvHolder[currentPos++].setText(String.valueOf(mStars[i].letter));
                        }

                        mStars[i].enabled = false;

                        break;
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);

        // The starting position is dependent on the size of the view,
        // which is why the model is initialized here, when the view is measured.
        for (int i = 0; i < mStars.length; i++) {
            final Star star = new Star();
            initializeStar(star, width, height);
            star.letter = word.toCharArray()[i];
            mStars[i] = star;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int viewHeight = getHeight();
        for (final Star star : mStars) {
            if(!star.enabled){
                continue;
            }
            // Ignore the star if it's outside of the view bounds
            final float starSize = 130;

            // Save the current canvas state
            final int save = canvas.save();

            // Move the canvas to the center of the star
            canvas.translate(star.x, star.y);

            // Rotate the canvas based on how far the star has moved
            final float progress = (star.y + starSize) / viewHeight;
            canvas.rotate(360 * progress);

            // Prepare the size and alpha of the drawable
            final int size = Math.round(starSize);
            mDrawable.setBounds(-size, -size, size, size);
            mDrawable.setAlpha(Math.round(255));


            // Draw the star to the canvas
            mDrawable.draw(canvas, star.letter);

            // Restore the canvas to it's previous position and rotation
            canvas.restoreToCount(save);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTimeAnimator = new TimeAnimator();
        mTimeAnimator.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                if (!isLaidOut()) {
                    // Ignore all calls before the view has been measured and laid out.
                    return;
                }
                updateState(deltaTime);
                invalidate();
            }
        });
        mTimeAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTimeAnimator.cancel();
        mTimeAnimator.setTimeListener(null);
        mTimeAnimator.removeAllListeners();
        mTimeAnimator = null;
    }

    /**
     * Pause the animation if it's running
     */
    public void pause() {
        if (mTimeAnimator != null && mTimeAnimator.isRunning()) {
            // Store the current play time for later.
            mCurrentPlayTime = mTimeAnimator.getCurrentPlayTime();
            mTimeAnimator.pause();
        }
    }

    /**
     * Resume the animation if not already running
     */
    public void resume() {
        if (mTimeAnimator != null && mTimeAnimator.isPaused()) {
            mTimeAnimator.start();
            // Why set the current play time?
            // TimeAnimator uses timestamps internally to determine the delta given
            // in the TimeListener. When resumed, the next delta received will the whole
            // pause duration, which might cause a huge jank in the animation.
            // By setting the current play time, it will pick of where it left off.
            mTimeAnimator.setCurrentPlayTime(mCurrentPlayTime);
        }
    }

    /**
     * Progress the animation by moving the stars based on the elapsed time
     * @param deltaMs time delta since the last frame, in millis
     */
    private void updateState(float deltaMs) {
        // Converting to seconds since PX/S constants are easier to understand
        final float deltaSeconds = deltaMs / 1000f;
        final int viewWidth = getWidth();
        final int viewHeight = getHeight();

        for (final Star star : mStars) {
            // Move the star based on the elapsed time and it's speed
            if(star.v == 0)
            {
                star.v = random.nextInt() % 1000 * (random.nextInt()%2 == 0?1:-1);
            }

            if(star.h == 0)
            {
                star.h = random.nextInt() % 1000 * (random.nextInt()%2 == 0?1:-1);
            }

            final float size = 130;
            if(star.y - 40  < 0){

                star.v = 200;
            }

            if(star.x - 40 < 0){

                star.h = 200;
            }

            if(star.y + 40 >= viewHeight){

                star.v = -200;
            }

            if(star.x + 40 >= viewWidth){

                star.h = -200;
            }

            int c = 0;
            if(star.v < 0)
            {
                star.v++;
                c = -1;
            }
            else
            {
                star.v--;
                c=1;
            }

            int ch = 0;
            if(star.h < 0)
            {
                star.h++;
                ch = -1;
            }
            else
            {
                star.h--;
                ch=1;
            }

            star.x += star.speedX * deltaSeconds * ch;
            star.y += star.speedY * deltaSeconds * c;


            // If the star is completely outside of the view bounds after
            // updating it's position, recycle it.


            /*if (star.y + size < 0) {
                initializeStar(star, viewWidth, viewHeight);
            }
            if (star.x + size < 0) {
                initializeStar(star, viewWidth, viewHeight);
            }*/
        }
    }

    /**
     * Initialize the given star by randomizing it's position, scale and alpha
     * @param star the star to initialize
     * @param viewWidth the view width
     * @param viewHeight the view height
     */
    private void initializeStar(Star star, int viewWidth, int viewHeight) {

        // Set X to a random value within the width of the view
        star.x = viewWidth / 2 + (random.nextFloat() * 200) * (random.nextInt() % 2 == 0 ? 1 : -1);
        star.y = viewHeight / 2 + (random.nextFloat() * 200) * (random.nextInt() % 2 == 0 ? 1 : -1);

        star.v = 0;
        star.h = 0;

        // Set the Y position
        // Start at the bottom of the view

        // The Y value is in the center of the star, add the size
        // to make sure it starts outside of the view bound
        // star.y += star.scale * mBaseSize;
        // Add a random offset to create a small delay before the
        // star appears again.
        // star.y += viewHeight * mRnd.nextFloat() / 4f;

        // The bigger and brighter a star is, the faster it moves
        star.speedX = mBaseSpeed + (int) (Math.random() * (mBaseSpeed / 2));
        star.speedY = mBaseSpeed + (int) (Math.random() * (mBaseSpeed / 2));
    }
}