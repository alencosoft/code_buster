package com.android.codebreaker;

import android.os.Parcel;
import android.os.Parcelable;

public class InputResponseColoredBalls implements Parcelable
{
    public String turn;
    public String response;
    public int icon1;
    public int icon2;
    public int icon3;
    public int icon4;

    public InputResponseColoredBalls(String turn,
                                     String response,
                                     int icon1,
                                     int icon2,
                                     int icon3,
                                     int icon4)
    {
        super();

        this.turn = turn;
        this.response = response;
        this.icon1 = icon1;
        this.icon2 = icon2;
        this.icon3 = icon3;
        this.icon4 = icon4;
    }

    private InputResponseColoredBalls(Parcel in)
    {
        turn = in.readString();
        response = in.readString();
        icon1 = in.readInt();
        icon2 = in.readInt();
        icon3 = in.readInt();
        icon4 = in.readInt();
    }

    public int describeContents()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return turn + ": " + turn;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(turn);
        out.writeString(response);
        out.writeInt(icon1);
        out.writeInt(icon2);
        out.writeInt(icon3);
        out.writeInt(icon4);
    }

    public static final Parcelable.Creator<InputResponseColoredBalls>
            CREATOR = new Parcelable.Creator<InputResponseColoredBalls>()
    {
        public InputResponseColoredBalls createFromParcel(Parcel in)
        {
            return new InputResponseColoredBalls(in);
        }

        public InputResponseColoredBalls[] newArray(int size)
        {
            return new InputResponseColoredBalls[size];
        }
    };
}