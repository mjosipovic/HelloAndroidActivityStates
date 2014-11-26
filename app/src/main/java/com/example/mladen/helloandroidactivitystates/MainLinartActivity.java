package com.example.mladen.helloandroidactivitystates;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainLinartActivity extends ActionBarActivity implements MyLinartView.MyCallbackClass {
    private static final String DEBUG_TAG = "MainLinartActivity";
    private static final int LINART_VIEW_DEFAULT_BACK_COLOR = Color.GRAY;

    private MyLinartView mMyLinartView;
    private ProgressDialog pDialog;

    private boolean mLinartMode;

    private View popupPositionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linart);

        mMyLinartView = (MyLinartView) findViewById(R.id.myLinartViewId);
        mMyLinartView.registerCallback(this);
        mMyLinartView.setBackgroundColor(LINART_VIEW_DEFAULT_BACK_COLOR);

        mLinartMode = true;
        mMyLinartView.setmLinartMode(mLinartMode);

        popupPositionView = findViewById(R.id.popuPositionTextView);
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

            case R.id.action_switch_mode:
//                Toast.makeText(getApplicationContext(), "action_add_to_background", Toast.LENGTH_SHORT).show();
                if (mLinartMode) {
                    mMyLinartView.setBackgroundColor(Color.DKGRAY);
                    mLinartMode = false;
                    mMyLinartView.setmLinartMode(mLinartMode);
                } else {
                    mMyLinartView.setBackgroundColor(LINART_VIEW_DEFAULT_BACK_COLOR);
                    mLinartMode = true;
                    mMyLinartView.setmLinartMode(mLinartMode);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void callbackReturn(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction) {
        //Starting the task
        new DrawLineart(t1, t2, direction).execute();
    }

    @Override
    public void callbackToShowPopup() {
        showPopup(popupPositionView);
    }

    private class DrawLineart extends AsyncTask<Void, Void, Void> {
        Point mT1;
        Point mT2;
        MyUtils.MOVE_DIRECTION mDirection;

        public DrawLineart(Point t1, Point t2, MyUtils.MOVE_DIRECTION direction) {
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
            mMyLinartView.drawLinOpArtBetweenPoints(mT1, mT2, mDirection);
            return null;
        }

        protected void onPostExecute(Void param) {
            mMyLinartView.invalidate();
            pDialog.dismiss();
        }
    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu__basic_shapes_for_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainLinartActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }
}
