package com.android.codebreaker;

import android.os.Parcel;
import android.os.Parcelable;

public class GetScoresListRowData implements Parcelable
{
    public int mIndex;
    public String mIgn;
    public String mScore;

    public GetScoresListRowData(int index, String ign, String score)
    {
        super();

        mIndex = index;
        mIgn = ign;
        mScore = score;
    }

    private GetScoresListRowData(Parcel in)
    {
        mIndex = in.readInt();
        mIgn = in.readString();
        mScore = in.readString();
    }

    public int describeContents()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return mIgn + ": " + mScore;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(mIndex);
        out.writeString(mIgn);
        out.writeString(mScore);
    }

    public static final Parcelable.Creator<GetScoresListRowData>
            CREATOR = new Parcelable.Creator<GetScoresListRowData>()
    {
        public GetScoresListRowData createFromParcel(Parcel in)
        {
            return new GetScoresListRowData(in);
        }

        public GetScoresListRowData[] newArray(int size)
        {
            return new GetScoresListRowData[size];
        }
    };
}