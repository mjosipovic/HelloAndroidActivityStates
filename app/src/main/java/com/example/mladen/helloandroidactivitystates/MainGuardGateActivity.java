package com.example.mladen.helloandroidactivitystates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainGuardGateActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "MainGuardGateActivity";
    private static final String GATES_LANES_DELIMITER = ";";
    private static final String CURRENT_GATE_LANE = "currentGateLane";
    private static final String EMPTY_STRING = "";

    private TextView mGateNumberTextView;
    private String mCurrentGateLane;
    private SeekBar mOpenGateSeekBar;

    private Runnable mMoveToStartGateOpenSliderTask;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guard_gate);
        //
        mGateNumberTextView = (TextView)findViewById(R.id.gateNumberTextView);
        mOpenGateSeekBar = (SeekBar) findViewById(R.id.openGateSeekBar);

        mOpenGateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;
            int rampValue  = mOpenGateSeekBar.getMax()/2;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
//                Log.d(DEBUG_TAG, "Changing seekbar's progress, current value = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(DEBUG_TAG, "Started tracking seekbar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String msg = "Stopped tracking seekbar." +"Covered: " + progressVal + "/" + seekBar.getMax();
                Log.d(DEBUG_TAG, msg);

                if(progressVal >= rampValue){
                    mOpenGateSeekBar.setProgress(seekBar.getMax());
                    Toast.makeText(getApplicationContext(), "Open gate", Toast.LENGTH_SHORT).show();
                    mScheduledThreadPoolExecutor.schedule(mMoveToStartGateOpenSliderTask, 2, TimeUnit.SECONDS);
                }
                else{
                    mOpenGateSeekBar.setProgress(0);
                }

            }
        });

        mMoveToStartGateOpenSliderTask = new Runnable(){
            @Override
            public void run() {
                Log.d(DEBUG_TAG, "Move gate open slider to start");
                mOpenGateSeekBar.setProgress(0);
            }
        };

        mScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

        ImageView iddyLogoImageView = (ImageView) findViewById(R.id.iddyLogoImageView);
        iddyLogoImageView.setImageResource(R.drawable.iddy_logo_img);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mCurrentGateLane = savedInstanceState.getString(CURRENT_GATE_LANE);
            mGateNumberTextView.setText(mCurrentGateLane);
            mOpenGateSeekBar.setEnabled(true);
        } else {
            // Probably initialize members with default values for a new instance
            mCurrentGateLane= EMPTY_STRING;
            mGateNumberTextView.setText(mCurrentGateLane);
            mOpenGateSeekBar.setEnabled(false);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_GATE_LANE, mCurrentGateLane);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_guard_gate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_new_patron:
                addNewPatron();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        Log.d(DEBUG_TAG, "---> onStart");
        super.onStart();
        //
        getInformationAboutGatesAndLanes();
    }

    public void onGateLineTextViewClick(View view) {
        Log.d(DEBUG_TAG, "---> onGateLineTextViewClick");
//        Toast.makeText(getApplicationContext(), "Hello tata", Toast.LENGTH_LONG).show();
        showGateLineMenu(view);
    }

    public void showGateLineMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

//        popup.inflate(R.menu.gate_line_menu);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String gatesLanesStr = sharedPref.getString(getResources().getString(R.string.gates_lanes_key), EMPTY_STRING);

        final String[] gatesLanesArr = gatesLanesStr.split(GATES_LANES_DELIMITER);

        for (int i = 0; i <  gatesLanesArr.length; ++i) {
            popup.getMenu().add(1, i, i, gatesLanesArr[i]);
        }


        // This activity implements OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(this);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId >= 0 && itemId < gatesLanesArr.length) {
//                    Toast.makeText(getApplicationContext(), gatesLanesArr[itemId], Toast.LENGTH_LONG).show();
                    mCurrentGateLane = gatesLanesArr[itemId];
                    mGateNumberTextView.setText(mCurrentGateLane);
                    mOpenGateSeekBar.setEnabled(true);
                    return true;
                }

                return false;
            }
        });


        popup.show();
    }

    /**
     * Get information about gates/lanes from database and store it to shared preferences
     */
    private void getInformationAboutGatesAndLanes() {
        //todo - replace this hardcoded array by getting information from database
        String[] gatesLanesArr = {"Gate 1", "Gate 2", "Gate 3", "Gate 4", "Lane 1", "Lane 2", "Lane 3"};

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


        StringBuilder strBuilder = new StringBuilder();

        for (String item : gatesLanesArr) {
            strBuilder.append(item).append(GATES_LANES_DELIMITER);
        }

        editor.putString(getString(R.string.gates_lanes_key), strBuilder.toString());
        editor.commit();
    }

    private void addNewPatron(){
        Log.d(DEBUG_TAG, "---> addNewPatron");
        Intent intent = new Intent(this, AddNewPatronActivity.class);
        startActivity(intent);
    }
}
