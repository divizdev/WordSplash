package com.worksplash.akscorp.wordsplash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class DialogScoreFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "gameResult";

    // TODO: Rename and change types of parameters
    private GameResult _gameResult;

    public DialogScoreFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        _gameResult = getArguments().getParcelable(ARG_PARAM);
//    }


    public static DialogScoreFragment newInstance(GameResult result) {
        DialogScoreFragment fragment = new DialogScoreFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, result);
        fragment.setArguments(args);
        return fragment;
    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_dialog_score_fragmnet, container, false);
//
//        if (_gameResult.res > 1) {
//            ImageView imageView = view.findViewById(R.id.scope_1_image);
//            imageView.setImageResource(R.drawable.circle_full_shape);
//        }
//        if (_gameResult.res > 2) {
//            ImageView imageView = view.findViewById(R.id.scope_2_image);
//            imageView.setImageResource(R.drawable.circle_full_shape);
//        }
//        if (_gameResult.res > 3) {
//            ImageView imageView = view.findViewById(R.id.scope_3_image);
//            imageView.setImageResource(R.drawable.circle_full_shape);
//        }
//        return  view;
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        GameResult gameResult = getArguments().getParcelable(ARG_PARAM);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_score_fragmnet, null, false);

        Button buttonNext = view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Button buttonMenu = view.findViewById(R.id.button_main_menu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button buttonShare = view.findViewById(R.id.button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "I have " + _gameResult.res + " points in Word Splash");
                getActivity().startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        if (gameResult.res >= 1) {
            ImageView imageView = view.findViewById(R.id.scope_1_image);
            imageView.setImageResource(R.drawable.circle_full_shape);
        }
        if (gameResult.res >= 2) {
            ImageView imageView = view.findViewById(R.id.scope_2_image);
            imageView.setImageResource(R.drawable.circle_full_shape);
        }
        if (gameResult.res >= 3) {
            ImageView imageView = view.findViewById(R.id.scope_3_image);
            imageView.setImageResource(R.drawable.circle_full_shape);
        }

        return new AlertDialog.Builder(getActivity())
//                .setTitle("Congratulation")
                .setView(view)
                .create();

    }
}
