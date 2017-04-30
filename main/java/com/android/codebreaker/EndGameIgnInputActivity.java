package com.android.codebreaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class EndGameIgnInputActivity extends Activity
{
    int mDuration = 1;
    int mTurns = 1;
    String mSecretNumber = "";
    AlertDialog mAlert;
    EditText mNameInput;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_ign_input);

        final ImageView iv = (ImageView) findViewById(R.id.imgLogo);
        iv.setImageResource(R.drawable.logo);

        mNameInput = (EditText) findViewById(R.id.txtIgn);

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();

            if(extras != null)
            {
                mTurns =  Integer.parseInt(extras.getString("Turns"));
                mDuration = Integer.parseInt(extras.getString("Duration"));
                mSecretNumber = extras.getString("SecretNumber");
            }
        }
        else
        {
            mTurns = savedInstanceState.getInt("mTurns");
            mDuration = savedInstanceState.getInt("mDuration");
            mSecretNumber = savedInstanceState.getString("mSecretNumber");

            if (savedInstanceState.getString("mNameInput") != null)
            {
                mNameInput.setText(savedInstanceState.getString("mNameInput"));
            }
        }

        // Display the score
        String score = NumberFormat.getNumberInstance(Locale.US).format(getScore());
        final TextView txtIgnScore = (TextView) findViewById(R.id.txtIgnScore);
        txtIgnScore.setText("Score: " + score);

        // Prepare the Alert Dialogue - No "name" entered when "Get Scores" button is mashed.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You must enter your \"Name\".")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // we don't want to do anything
                    }
                });
        mAlert = builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // Save Duration
        outState.putInt("mDuration", mDuration);

        // Save Turns
        outState.putInt("mTurns", mTurns);

        // Save Secret Number
        outState.putString("mSecretNumber", mSecretNumber);

        // Save input name
        String txtName = mNameInput.getText().toString();

        if ( ! txtName.isEmpty())
        {
            outState.putString("mNameInput", txtName);
        }
    }

    public int getScore()
    {
        return ((10000 - mDuration) / mTurns) * 100;
    }

    public void submitScoreButtonClick(View v)
    {
        // Be sure a 'Name' was entered
        String txtName = mNameInput.getText().toString();

        if (txtName.isEmpty())
        {
            mAlert.show();
            return;
        }

        if (txtName.length() > 10)
        {
            txtName = txtName.substring(0, 10);
        }

        txtName = txtName.toLowerCase();

        // Create the intent and pass variables
        Intent intent = new Intent(this, GetScoresActivity.class);
        intent.putExtra("Ign", txtName);
        intent.putExtra("SecretNumber", mSecretNumber);
        intent.putExtra("Turns", Integer.toString(mTurns));
        intent.putExtra("TimeInSeconds", Integer.toString(mDuration));
        intent.putExtra("Score", Integer.toString(getScore()));

        // Start the new activity
        startActivity(intent);
    }
}
