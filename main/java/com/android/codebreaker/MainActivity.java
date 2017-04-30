package com.android.codebreaker;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MainActivity extends Activity implements NumberPicker.OnValueChangeListener
{
    InputResponseAdapter mInputResponseAdapter;
    ArrayList<InputResponseColoredBalls> mData;
    Map<Integer, Integer> mNumberPickerValues = new TreeMap<Integer, Integer>();
    int[] mSecretNumber = new int[4];
    int mCounter = 0;
    ListView mListView;
    long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Logo
        final ImageView iv = (ImageView) findViewById(R.id.imgLogo);
        iv.setImageResource(R.drawable.logo);

        // Set properties on the 4 Number Pickers
        setNumberPickerProperties((NumberPicker) findViewById(R.id.numberPicker1), savedInstanceState);
        setNumberPickerProperties((NumberPicker) findViewById(R.id.numberPicker2), savedInstanceState);
        setNumberPickerProperties((NumberPicker) findViewById(R.id.numberPicker3), savedInstanceState);
        setNumberPickerProperties((NumberPicker) findViewById(R.id.numberPicker4), savedInstanceState);

        mListView = (ListView) findViewById(R.id.list);

        if(savedInstanceState == null)
        {
            // Set the secret number
            Random randomGenerator = new Random();
            mSecretNumber[0] = randomGenerator.nextInt(10);
            mSecretNumber[1] = randomGenerator.nextInt(10);
            mSecretNumber[2] = randomGenerator.nextInt(10);
            mSecretNumber[3] = randomGenerator.nextInt(10);

            // Set the start time
            mStartTime = System.currentTimeMillis();

            // Init mData
            mData = new ArrayList<>();
        }
        else
        {
            mCounter = savedInstanceState.getInt("mCounter");
            mSecretNumber = savedInstanceState.getIntArray("mSecretNumber");
            mListView.onRestoreInstanceState(savedInstanceState.getParcelable("mListView"));
            mData = savedInstanceState.getParcelableArrayList("mData");
            mStartTime = savedInstanceState.getLong("mStartTime");
        }

        mInputResponseAdapter = new InputResponseAdapter(this, R.layout.listview_item_row, mData);
        mListView.setAdapter(mInputResponseAdapter);
        mInputResponseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        // Save start time
        outState.putLong("mStartTime", mStartTime);

        // Save Number Picker states
        NumberPicker np1 = (NumberPicker) findViewById(R.id.numberPicker1);
        outState.putInt(Integer.toString(np1.getId()), mNumberPickerValues.get(np1.getId()));

        NumberPicker np2 = (NumberPicker) findViewById(R.id.numberPicker2);
        outState.putInt(Integer.toString(np2.getId()), mNumberPickerValues.get(np2.getId()));

        NumberPicker np3 =(NumberPicker) findViewById(R.id.numberPicker3);
        outState.putInt(Integer.toString(np3.getId()), mNumberPickerValues.get(np3.getId()));

        NumberPicker np4 =(NumberPicker) findViewById(R.id.numberPicker4);
        outState.putInt(Integer.toString(np4.getId()), mNumberPickerValues.get(np4.getId()));

        // Save mCounter (turns)
        outState.putInt("mCounter", mCounter);

        // Save secret number
        outState.putIntArray("mSecretNumber", mSecretNumber);

        // Save list view state
        outState.putParcelable("mListView", mListView.onSaveInstanceState());

        // Save the list view's data
        outState.putParcelableArrayList("mData", mData);
    }

    private void setNumberPickerProperties(NumberPicker np, Bundle savedInstanceState)
    {
        np.setWrapSelectorWheel(false);
        np.setMinValue(0);
        np.setMaxValue(9);
        np.setOnValueChangedListener(this);

        int value = 0;

        if (savedInstanceState != null)
        {
            value = savedInstanceState.getInt(Integer.toString(np.getId()));
            np.setValue(value);
        }

        mNumberPickerValues.put(np.getId(), value);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
    {
        mNumberPickerValues.put(picker.getId(), newVal);
    }

    public void tryItButtonClick(View v)
    {
        mCounter++;

        int counter = 0;
        List<Integer> coloredBallsEnumValues = new ArrayList<Integer>();
        int[] tempSecretNumber = mSecretNumber.clone();
        int totalGreenBalls = 0;

        // How many numbers entered are in the right spots?
        for (Integer value : mNumberPickerValues.values())
        {
            if (value == mSecretNumber[counter])
            {
                coloredBallsEnumValues.add(R.drawable.green);
                tempSecretNumber[counter] = -1;
                totalGreenBalls++;
            }

            counter++;
        }

        // How many numbers entered are in the secret number?
        for (Integer value : mNumberPickerValues.values())
        {
            for (int iter = 0; iter < tempSecretNumber.length; iter++)
            {
                if (value == tempSecretNumber[iter])
                {
                    coloredBallsEnumValues.add(R.drawable.grey);
                    tempSecretNumber[iter] = -1;
                    break;
                }
            }

            counter++;
        }

        // Fill the remaining slots in coloredBallsEnumValues with black
        for (int iter = coloredBallsEnumValues.size(); iter < 4; iter++)
        {
            coloredBallsEnumValues.add(R.drawable.black);
        }

        mData.add(0, new InputResponseColoredBalls(Integer.toString(mCounter),
                mNumberPickerValues.values().toString(),
                coloredBallsEnumValues.get(0),
                coloredBallsEnumValues.get(1),
                coloredBallsEnumValues.get(2),
                coloredBallsEnumValues.get(3)));
        mInputResponseAdapter.notifyDataSetChanged();

        if (totalGreenBalls == 4)
        {
            gotoEndGame();
        }
    }

    private void gotoEndGame()
    {
        // Convert the Secret Number Array to a String
        String secretNumber = Integer.toString(mSecretNumber[0]);
        secretNumber += Integer.toString(mSecretNumber[1]);
        secretNumber += Integer.toString(mSecretNumber[2]);
        secretNumber += Integer.toString(mSecretNumber[3]);

        // Set a cap on the number of turns
        if (mCounter > 100)
        {
            mCounter = 100;
        }

        // Get the duration of time since starting
        Integer duration = (int) (long) ((System.currentTimeMillis() - mStartTime) / 1000);

        // Set a cap on the duration
        if (duration > 9999)
        {
            duration = 9999;
        }

        // Create the intent and pass variables
        Intent intent = new Intent(this, EndGameIgnInputActivity.class);
        intent.putExtra("Turns", Integer.toString(mCounter));
        intent.putExtra("Duration", Integer.toString(duration));
        intent.putExtra("SecretNumber", secretNumber);

        // Start the new activity
        startActivity(intent);
    }
}
