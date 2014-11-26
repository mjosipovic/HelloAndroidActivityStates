package com.example.mladen.helloandroidactivitystates;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivityWithDrawingView extends ActionBarActivity {
    private static final String DEBUG_TAG = "MainActivityWithDrawingView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_with_drawing_view);

        AssetManager assetManager = getAssets();

//        try {
//            InputStream is = assetManager.open("A.json");
//            BufferedReader bReader = new BufferedReader(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        // Reading text file from assets folder

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open("A.json")));

            Gson gson = new GsonBuilder().create();
            MyPath myPath = gson.fromJson(br, MyPath.class);

            Log.d(DEBUG_TAG, "*****myPath*****");
            Log.d(DEBUG_TAG, myPath.toString());
            Log.d(DEBUG_TAG, "*****myPath*****");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_with_drawing_view, menu);
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
}
