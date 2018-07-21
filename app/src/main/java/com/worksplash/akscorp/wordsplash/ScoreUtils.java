package com.worksplash.akscorp.wordsplash;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class ScoreUtils {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_SCORE = "score";


    public static List<GameResult> loadScores(Context context){
        List<GameResult> gameResults = new ArrayList<>();
        SharedPreferences mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        String settingsString = mSettings.getString(APP_PREFERENCES_SCORE, "");
        String[] split = settingsString.split("#");
        for (int i = 0; i < split.length; i++) {
            String[] stringArg = split[i].split("=");
            gameResults.add(new GameResult(Integer.valueOf(stringArg[1]), stringArg[0], GameResult.Levels.valueOf(stringArg[2])));
        }
        return gameResults;
    }

    public static void saveScores(Context context, List<GameResult> list){
        SharedPreferences mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).word);
            builder.append("=");
            builder.append(list.get(i).res.toString());
            builder.append("=");
            builder.append(list.get(i).level.toString());
            builder.append("#");
        }
        mSettings.edit().putString(APP_PREFERENCES_SCORE, builder.toString()).apply();
    }

    public static void addScores(Context context, GameResult gameResult){
        List<GameResult> gameResults = loadScores(context);
        gameResults.add(gameResult);

        saveScores(context, gameResults);
    }


}
