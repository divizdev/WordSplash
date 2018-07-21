package com.worksplash.akscorp.wordsplash;

import android.os.Parcel;
import android.os.Parcelable;

public class GameResult implements Parcelable {
    public enum Levels{
        Easy, Medium, Hard
    }

    public GameResult(Integer res, String word, Levels level) {
        this.res = res;
        this.word = word;
        this.level = level;
    }

    Integer res;
    String word;
    Levels level;

    protected GameResult(Parcel in) {
        if (in.readByte() == 0) {
            res = null;
        } else {
            res = in.readInt();
        }
        word = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (res == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(res);
        }
        dest.writeString(word);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GameResult> CREATOR = new Creator<GameResult>() {
        @Override
        public GameResult createFromParcel(Parcel in) {
            return new GameResult(in);
        }

        @Override
        public GameResult[] newArray(int size) {
            return new GameResult[size];
        }
    };
}
