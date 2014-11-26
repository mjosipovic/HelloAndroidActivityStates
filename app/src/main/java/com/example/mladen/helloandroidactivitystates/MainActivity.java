package com.example.mladen.helloandroidactivitystates;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;


public class MainActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "MainActivity";

    private TextView mTextView;
    private EditText mEditText;
    private TextView mTextView01;

    final static private DateFormat mDateFormat = DateFormat.getDateTimeInstance();

    final static private String ACTIVITY_START_TIME_KEY = "com.example.mladen.helloandroidactivitystates.ACTIVITY_START_TIME";

    private String mActivityStartTimeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "MyActivity: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.textView);
        mEditText = (EditText)findViewById(R.id.editText);
        mTextView01 = (TextView)findViewById(R.id.textView01);


        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            Log.d(DEBUG_TAG, "MyActivity: onCreate --> Restore value of members from saved state");
            mActivityStartTimeStr = savedInstanceState.getString(ACTIVITY_START_TIME_KEY);
            mTextView.setText(mActivityStartTimeStr);
        } else {
            mActivityStartTimeStr = mDateFormat.format(System.currentTimeMillis());
            mTextView.setText(mActivityStartTimeStr);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "MyActivity: onRestoreInstanceState");
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        Log.d(DEBUG_TAG, "MyActivity: onRestoreInstanceState --> Restore value of members from saved state");
        mActivityStartTimeStr = savedInstanceState.getString(ACTIVITY_START_TIME_KEY);
        mTextView.setText(mActivityStartTimeStr);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the activity start time
        Log.d(DEBUG_TAG, "MyActivity: onSaveInstanceState");
        outState.putString(ACTIVITY_START_TIME_KEY, mActivityStartTimeStr);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(DEBUG_TAG, "MyActivity: onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(DEBUG_TAG, "MyActivity: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(DEBUG_TAG, "MyActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(DEBUG_TAG, "MyActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(DEBUG_TAG, "MyActivity: onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(DEBUG_TAG, "MyActivity: onStart");
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

    public void testPopupAction(View view){
        Log.d(DEBUG_TAG, "MyActivity: testPopupAction");
//        PopupMenu popup = new PopupMenu(this, view);
        PopupMenu popup = new PopupMenu(this, mTextView01);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu__basic_shapes_for_popup, popup.getMenu());
        popup.show();
    }
}
