package com.example.mladen.helloandroidactivitystates;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainLinartActivity extends ActionBarActivity implements MyLinartView.MyCallbackClass{
    private static final String DEBUG_TAG = "MainLinartActivity";
    private MyLinartView mMyLinartView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linart);

        mMyLinartView = (MyLinartView)findViewById(R.id.myLinartViewId);

        mMyLinartView.registerCallback(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_linart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_new:
//                Toast.makeText(getApplicationContext(), "action_new", Toast.LENGTH_SHORT).show();
                mMyLinartView.newArt();
                return true;
            case R.id.action_remove:
//                Toast.makeText(getApplicationContext(), "action_remove", Toast.LENGTH_SHORT).show();
                mMyLinartView.removeArt();
                return true;
            case R.id.action_undo:
//                Toast.makeText(getApplicationContext(), "action_undo", Toast.LENGTH_SHORT).show();
                mMyLinartView.undoArt();
                return true;
            case R.id.action_redo:
//                Toast.makeText(getApplicationContext(), "action_redo", Toast.LENGTH_SHORT).show();
                mMyLinartView.redoArt();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
            pDialog = new ProgressDialog(MainLinartActivity.this);
            pDialog.setMessage("Creating art ...");
            pDialog.show();
        }

        protected Void doInBackground(Void... args) {
            mMyLinartView.drawLinOpArtBetweenPoints(mT1,mT2,mDirection);
            return null;
        }

        protected void onPostExecute(Void param) {
            mMyLinartView.invalidate();
            pDialog.dismiss();
        }
    }
}
