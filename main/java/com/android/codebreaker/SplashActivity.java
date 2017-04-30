package com.android.codebreaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SplashActivity extends Activity implements IAsyncCallback
{
    TextView mTxtGamePlay;
    String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView iv = (ImageView) findViewById(R.id.imgLogo);
        iv.setImageResource(R.drawable.logo_lg);

        mTxtGamePlay = (TextView) findViewById(R.id.txtGamePlay);
        mTxtGamePlay.setMovementMethod(new ScrollingMovementMethod());

        queryForMiscData();
    }

    // Get instructions/link from server...
    private void queryForMiscData()
    {
        try
        {
            String link = "http://api.greggscoolapps.com/codebuster_miscdata.php";

            DataRequests dataRequests = new DataRequests(this);
            dataRequests.setParams(link, "", null);
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
                mTxtGamePlay.setText(Html.fromHtml(getString(R.string.splash_activity_game_play_body)));
            }
        });
    }

    public void onTaskCompleted(String result)
    {
        // Oops, no data! This is bad.
        if (result != null && result.isEmpty())
        {
            Log.e("Misc result empty", "");
            return;
        }

        mResult = "{\"misc\":" + result + "}";
        String websiteLinkText = "";
        String instructions = "";

        try
        {
            JSONObject jsonResponse = new JSONObject(mResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("misc");

            for(int i = 0; i < jsonMainNode.length(); i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                String outputName = jsonChildNode.optString("name");
                String outputValue = jsonChildNode.optString("value");

                switch (outputName)
                {
                    case "website_link_text":
                        websiteLinkText = outputValue;
                        break;

                    case "instructions":
                        instructions = outputValue;
                        break;
                }
            }
        }
        catch(JSONException e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        // Yay! Data is valid.
        if ( ! websiteLinkText.isEmpty())
        {
            TextView txtLink = (TextView) findViewById(R.id.txtLink);
            txtLink.setBackgroundColor(Color.parseColor("#990000"));
            txtLink.setText(Html.fromHtml(websiteLinkText));
        }

        // Appears we had a connection problem so get baked instructions below. BORING!!!
        if (instructions.isEmpty())
        {
            instructions = getString(R.string.splash_activity_game_play_body);
        }

        mTxtGamePlay.setText(Html.fromHtml(instructions));
    }

    public void beginGame(View v)
    {
        // Create the intent and pass variables
        Intent intent = new Intent(this, MainActivity.class);

        // Start the new activity
        startActivity(intent);
    }
}
