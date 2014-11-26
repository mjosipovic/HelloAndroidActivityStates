package com.example.mladen.helloandroidactivitystates;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AddNewPatronActivity extends ActionBarActivity {
    private static final String DEBUG_TAG = "AddNewPatronActivity";

    private Spinner mVisitingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patron);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView iddyLogoImageView = (ImageView) findViewById(R.id.iddyLogoImageView);
        iddyLogoImageView.setImageResource(R.drawable.iddy_logo_img);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Item 1");
        adapter.add("Item 2");
        adapter.add("Visiting ...");

        mVisitingSpinner = (Spinner) findViewById(R.id.visiting_spinner);

        mVisitingSpinner.setAdapter(adapter);
        mVisitingSpinner.setSelection(adapter.getCount()); //display hint

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_patron, menu);
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

    public void saveBtnAction(View view){
        Log.d(DEBUG_TAG, "saveBtnAction");
        Toast.makeText(getApplicationContext(), "New patron information will be saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelBtnAction(View view){
        Log.d(DEBUG_TAG, "cancelBtnAction");
        Toast.makeText(getApplicationContext(), "New patron action cancelled.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
