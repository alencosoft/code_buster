package com.android.codebreaker;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

class DataRequests extends AsyncTask<String, Integer, String>
{
    private String[] mParams;
    private IAsyncCallback mListener;
    ProgressBar mProgressBar;

    public DataRequests(IAsyncCallback listener)
    {
        this.mListener = listener;
    }

    public void setParams(String link, String data, ProgressBar progressBar)
    {
        mParams = new String[] {link, data};
        mProgressBar = progressBar;
    }

    protected void onPreExecute()
    {
        //Setup is done here
    }

    @Override
    protected String doInBackground(String... params)
    {
        HttpURLConnection connection = null;
        String postData = mParams[1];

        try
        {
            URL url = new URL(mParams[0]);

            // Connection properties
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(postData.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            connection.getOutputStream().write(postData.getBytes("UTF-8"));
            connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try
            {
                // Read Server Response
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
            }
            finally
            {
                reader.close();
            }

            return sb.toString();
        }
        catch (Exception e)
        {
            Log.e("Error", "" + e.getMessage());
            e.printStackTrace();
            mListener.onTaskError();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }

        return null;
    }

    protected void onProgressUpdate(Integer... params)
    {
        if (mProgressBar != null)
        {
            mProgressBar.setProgress(params[0]);
        }
    }

    protected void onPostExecute(String result)
    {
        if (result == null || result.isEmpty())
        {
            Log.i("bad result", "");
            return;
        }
        if (mProgressBar != null)
        {
            mProgressBar.setVisibility(View.GONE);
        }

        mListener.onTaskCompleted(result);
    }
}
