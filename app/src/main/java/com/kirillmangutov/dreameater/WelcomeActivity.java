package com.kirillmangutov.dreameater;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class WelcomeActivity extends Activity {

    private static final String TAG = "DreamEater";

    @InjectView(R.id.listViewDreams)
    ListView listDreams;

    DreamAdapter mAdapter;
    AlarmAdmin mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);

        mAdapter = new DreamAdapter(this);
        listDreams.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAlarm = new AlarmAdmin(this);
        mAlarm.setAlarm();


    }

    @OnItemClick(R.id.listViewDreams)
    void onItemClick(
            AdapterView<?> parent,
            View v,
            int position,
            long id) {

        Dream dream = (Dream) parent.getItemAtPosition(position);
        String date_string = dream.readableDate();

        launchWriteActivity(date_string, position);
    }

    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    public void launchWriteActivity(String date_string, int position) {
        Intent intent = new Intent(WelcomeActivity.this,
                WriteActivity.class);
        intent.putExtra(WriteActivity.EXTRA_DATE_STRING, date_string);
        intent.putExtra(WriteActivity.EXTRA_ITEM_POSITION, position);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
