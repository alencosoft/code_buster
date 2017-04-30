package com.android.codebreaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class GetScoresListAdapter extends ArrayAdapter<GetScoresListRowData>
{
    Context mContext;
    int mLayoutResourceId;
    List<GetScoresListRowData> mData = null;
    int mHighlightedIndex = 3;

    public GetScoresListAdapter(Context context,
                                int layoutResourceId,
                                List<GetScoresListRowData> data,
                                int highlightedIndex)
    {
        super(context, layoutResourceId, data);
        mLayoutResourceId = layoutResourceId;
        mContext = context;
        mData = data;
        mHighlightedIndex = highlightedIndex;

        Log.i("Highlighted index", "" + Integer.toString(mHighlightedIndex));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        GetScoresListRowHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new GetScoresListRowHolder();

            holder.txtNumber = (TextView)row.findViewById(R.id.txtNumber);
            holder.txtIgn = (TextView)row.findViewById(R.id.txtIgn);
            holder.txtScores = (TextView)row.findViewById(R.id.txtScores);

            row.setTag(holder);
        }
        else
        {
            holder = (GetScoresListRowHolder)row.getTag();
        }

        if (position == mHighlightedIndex)
        {
            row.setBackgroundColor(Color.parseColor("#000066"));
        }
        else
        {
            row.setBackgroundColor(Color.TRANSPARENT);
        }

        GetScoresListRowData GetScoresListRowData = mData.get(position);
        holder.txtNumber.setText(Integer.toString(GetScoresListRowData.mIndex));
        holder.txtIgn.setText(GetScoresListRowData.mIgn);
        holder.txtScores.setText(GetScoresListRowData.mScore);

        return row;
    }

    static class GetScoresListRowHolder
    {
        TextView txtNumber;
        TextView txtIgn;
        TextView txtScores;
    }
}