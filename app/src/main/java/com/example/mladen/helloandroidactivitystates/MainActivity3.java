package com.example.mladen.helloandroidactivitystates;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;


public class MainActivity3 extends ActionBarActivity {
    private static final String DEBUG_TAG = "MainActivity3";

    private ImageView mImageView;
    private SeekBar mSeekBar;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3);

        mImageView = (ImageView)findViewById(R.id.imageView5);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressVal = 0;
            int rampValue  = mSeekBar.getMax()/2;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressVal = progress;
                Log.d(DEBUG_TAG, "Changing seekbar's progress, current value = " + progress);
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
                    mSeekBar.setProgress(seekBar.getMax());
                }
                else{
                    mSeekBar.setProgress(0);
                }

            }
        });

        mSwitch = (Switch)findViewById(R.id.switch1);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(DEBUG_TAG, "onCheckedChanged " + isChecked);
            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.d(DEBUG_TAG, "metrics=***" + metrics + "***");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void myButtonAction(View view){
        Log.d(DEBUG_TAG, "Someone clicked on me...");
        mImageView.setImageResource(R.drawable.shape_oval_green_gradient);
    }
}
