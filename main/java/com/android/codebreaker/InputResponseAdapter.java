package com.android.codebreaker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class InputResponseAdapter extends ArrayAdapter<InputResponseColoredBalls>
{
    Context context;
    int layoutResourceId;
    List<InputResponseColoredBalls> data = null;

    public InputResponseAdapter(Context context, int layoutResourceId, List<InputResponseColoredBalls> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        InputResponseHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new InputResponseHolder();

            holder.txtTurn = (TextView)row.findViewById(R.id.txtTurn);
            holder.txtResponse = (TextView)row.findViewById(R.id.txtResponse);
            holder.imgIcon1 = (ImageView)row.findViewById(R.id.imgIcon1);
            holder.imgIcon2 = (ImageView)row.findViewById(R.id.imgIcon2);
            holder.imgIcon3 = (ImageView)row.findViewById(R.id.imgIcon3);
            holder.imgIcon4 = (ImageView)row.findViewById(R.id.imgIcon4);

            row.setTag(holder);
        }
        else
        {
            holder = (InputResponseHolder)row.getTag();
        }

        InputResponseColoredBalls inputResponseColoredBalls = data.get(position);
        holder.txtTurn.setText(inputResponseColoredBalls.turn);
        holder.txtResponse.setText(inputResponseColoredBalls.response);
        holder.imgIcon1.setImageResource(inputResponseColoredBalls.icon1);
        holder.imgIcon2.setImageResource(inputResponseColoredBalls.icon2);
        holder.imgIcon3.setImageResource(inputResponseColoredBalls.icon3);
        holder.imgIcon4.setImageResource(inputResponseColoredBalls.icon4);

        return row;
    }

    static class InputResponseHolder
    {
        TextView txtTurn;
        TextView txtResponse;
        ImageView imgIcon1;
        ImageView imgIcon2;
        ImageView imgIcon3;
        ImageView imgIcon4;
    }
}