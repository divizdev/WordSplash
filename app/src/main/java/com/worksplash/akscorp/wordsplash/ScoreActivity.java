package com.worksplash.akscorp.wordsplash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class ScoreActivity extends AppCompatActivity {

    private RecyclerView _recyclerView;
    private List<GameResult> _list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("");
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        _list = ScoreUtils.loadScores(this);
        _recyclerView = findViewById(R.id.score_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(linearLayoutManager);
        ScoreAdapter scoreAdapter = new ScoreAdapter(_list);
        _recyclerView.setAdapter(scoreAdapter);


        DialogFragment dialogFragment = DialogScoreFragment.newInstance(_list.get(1));
        dialogFragment.show(getSupportFragmentManager(), "TEST");



    }




    public static class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {


        private final List<GameResult> _gameResultList;

        public ScoreAdapter(List<GameResult> gameResultList) {
            _gameResultList = gameResultList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setData(_gameResultList.get(position));
        }

        @Override
        public int getItemCount() {
            return _gameResultList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView _wordText;
            TextView _levelText;
            ImageView image1View;
            ImageView image2View;
            ImageView image3View;
            private GameResult _gameResult;

            public ViewHolder(View itemView) {
                super(itemView);
                _wordText = itemView.findViewById(R.id.word_text_view);
                _levelText = itemView.findViewById(R.id.level_text_view);
                image1View = itemView.findViewById(R.id.scope_1_image);
                image2View = itemView.findViewById(R.id.scope_2_image);
                image3View = itemView.findViewById(R.id.scope_3_image);
            }

            public void setData(GameResult gameResult) {
                _wordText.setText(gameResult.word);
                _levelText.setText(gameResult.level.name());

                if (gameResult.res >= 1) {
                    image1View.setImageResource(R.drawable.circle_full_shape);
                }
                if (gameResult.res >= 2) {
                    image2View.setImageResource(R.drawable.circle_full_shape);
                }
                if (gameResult.res >= 3) {
                    image3View.setImageResource(R.drawable.circle_full_shape);
                }
                _gameResult = gameResult;
            }
        }
    }

}
