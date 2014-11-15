package com.example.mladen.helloandroidactivitystates;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity5 extends ActionBarActivity implements MyCustomView.MyCallbackClass {
    private static final String DEBUG_TAG = "MainActivity5";
    private MyCustomView mMyCustomView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5);

        mMyCustomView = (MyCustomView)findViewById(R.id.myViewId);

        mMyCustomView.registerCallback(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity5, menu);
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
        Log.d(DEBUG_TAG, "myButtonAction...");
        mMyCustomView.drawSomething();
    }

    public void myButton3Action(View view){
        Log.d(DEBUG_TAG, "myButton3Action...");
        mMyCustomView.exploreBitmap();
    }

    public void myButton4Action(View view){
        Log.d(DEBUG_TAG, "myButton4Action...");
        mMyCustomView.lineartBitmap();
    }

    public void myButton5Action(View view){
        Log.d(DEBUG_TAG, "myButton5Action...");
        mMyCustomView.linHorOpArtBitmap();
    }

    public void myButton6Action(View view){
        Log.d(DEBUG_TAG, "myButton5Action...");
        mMyCustomView.linHorOpArtRandBitmap();
    }

    public void myButton7Action(View view) {
        Log.d(DEBUG_TAG, "myButton7Action...");
        mMyCustomView.linVertOpArtRandBitmap();
    }

    public void myButton8Action(View view){
        Log.d(DEBUG_TAG, "myButton7Action...");
        mMyCustomView.linHorOpArtRandBitmapAddToPath();
    }

    @Override
    public void callbackReturn(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction) {
        //Starting the task
        new DrawLineart(t1,t2,direction).execute();
    }

    private class DrawLineart extends AsyncTask<Void, Void, Void> {
        Point mT1;
        Point mT2;
        MyUtils.MOVE_DIRECTION mDirection;

        public DrawLineart(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction){
            mT1 = t1;
            mT2 = t2;
            mDirection = direction;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity5.this);
            pDialog.setMessage("Creating art ...");
            pDialog.show();
        }

        protected Void doInBackground(Void... args) {
            mMyCustomView.drawLinOpArtBetweenPoints(mT1,mT2,mDirection);
            return null;
        }

        protected void onPostExecute(Void param) {
            mMyCustomView.invalidate();
            pDialog.dismiss();
        }
    }

}
