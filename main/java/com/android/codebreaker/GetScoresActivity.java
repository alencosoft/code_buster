package com.android.codebreaker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GetScoresActivity extends Activity implements IAsyncCallback
{
    Boolean mLastQuery = false;
    String mIgn;
    String mSecretNumber;
    int mTurns;
    int mTimeInSeconds;
    int mScore;
    ListView mListView;
    String mResult;
    int mUserIndexInScoresList = 0;
    GetScoresListAdapter mGetScoresListAdapter;
    ArrayList<GetScoresListRowData> mData;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_scores);

        final ImageView iv = (ImageView) findViewById(R.id.imgLogo);
        iv.setImageResource(R.drawable.logo);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(10);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setItemsCanFocus(true);

        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();

            if(extras != null)
            {
                mIgn = extras.getString("Ign");
                mSecretNumber = extras.getString("SecretNumber");
                mTurns = Integer.parseInt(extras.getString("Turns"));
                mTimeInSeconds = Integer.parseInt(extras.getString("TimeInSeconds"));
                mScore = Integer.parseInt(extras.getString("Score"));
            }
        }
        else
        {
            mIgn = savedInstanceState.getString("mIgn");
            mSecretNumber = savedInstanceState.getString("mSecretNumber");
            mTurns = savedInstanceState.getInt("mTurns");
            mTimeInSeconds = savedInstanceState.getInt("mTimeInSeconds");
            mScore = savedInstanceState.getInt("mScore");
        }

        saveTheDataAndMaybeQueryForScores();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // Save Ign, SecretNumber, Turns, TimeInSeconds and Score
        outState.putString("mIgn", mIgn);
        outState.putString("mSecretNumber", mSecretNumber);
        outState.putInt("mTurns", mTurns);
        outState.putInt("mTimeInSeconds", mTimeInSeconds);
        outState.putInt("mScore", mScore);
    }

    private void saveTheDataAndMaybeQueryForScores()
    {
        try
        {
            String link = "http://api.greggscoolapps.com/codebuster_submitscores.php";

            String data = URLEncoder.encode("ign", "UTF-8") + "=" +
                    URLEncoder.encode(mIgn, "UTF-8");
            data += "&" + URLEncoder.encode("secret_number", "UTF-8") + "=" +
                    URLEncoder.encode(mSecretNumber, "UTF-8");
            data += "&" + URLEncoder.encode("turns", "UTF-8") + "=" +
                    URLEncoder.encode(Integer.toString(mTurns), "UTF-8");
            data += "&" + URLEncoder.encode("time_in_seconds", "UTF-8") + "=" +
                    URLEncoder.encode(Integer.toString(mTimeInSeconds), "UTF-8");
            data += "&" + URLEncoder.encode("score", "UTF-8") + "=" +
                    URLEncoder.encode(Integer.toString(mScore), "UTF-8");

            DataRequests dataRequests = new DataRequests(this);
            dataRequests.setParams(link, data, mProgressBar);
            dataRequests.execute("");
        }
        catch(Exception e)
        {
            Log.e("Exception", "" + e.getMessage());
        }
    }

    public void onTaskError()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mListView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                TextView txtNotConnected = (TextView) findViewById(R.id.txtNoDataConnection);
                txtNotConnected.setText(getString(R.string.get_scores_no_internet_connection));
            }
        });
    }

    public void onTaskCompleted(String result)
    {
        if (mLastQuery)
        {
            if (result.isEmpty())
            {
                Log.e("Scores result empty", "");
                return;
            }

            mResult = "{\"scores\":" + result + "}";
            mData = new ArrayList<>();
            initList();

            mGetScoresListAdapter =
                    new GetScoresListAdapter(
                            this,
                            R.layout.listview_item_row_get_scores,
                            mData,
                            mUserIndexInScoresList);
            mListView.setAdapter(mGetScoresListAdapter);
            mListView.setSelection(mUserIndexInScoresList);
            mListView.setItemChecked(mUserIndexInScoresList, true);
            mGetScoresListAdapter.notifyDataSetChanged();
        }
        else
        {
            try
            {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(0);

                String link = "http://api.greggscoolapps.com/codebuster_getscores.php";

                DataRequests dataRequests = new DataRequests(this);
                dataRequests.setParams(link, "", mProgressBar);
                dataRequests.execute("");
            }
            catch (Exception e)
            {
                Log.e("Exception", "" + e.getMessage());
            }

            mLastQuery = true;
        }
    }

    private void initList()
    {
        try
        {
            JSONObject jsonResponse = new JSONObject(mResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("scores");

            for(int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                String outputIgn = jsonChildNode.optString("ign");
                String score = jsonChildNode.optString("score");
                String outputScore = NumberFormat.getNumberInstance(Locale.US).format(
                        Integer.parseInt(score));

                mData.add(0, new GetScoresListRowData((jsonMainNode.length() - i), outputIgn, outputScore));

                // Find this user in the list
                if (outputIgn.equals(mIgn) && score.equals(Integer.toString(mScore)))
                {
                    mUserIndexInScoresList = jsonMainNode.length() - i - 1;
                }
            }
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void playAgainButtonClick(View v)
    {
        // Create the intent and pass variables
        Intent intent = new Intent(this, MainActivity.class);

        // Start the new activity
        startActivity(intent);
    }
}
