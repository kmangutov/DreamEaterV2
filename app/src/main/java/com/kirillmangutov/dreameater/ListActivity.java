package com.kirillmangutov.dreameater;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import android.preference.PreferenceFragment;

import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kirillmangutov.dreameater.custom.AnimationGoal;
import com.kirillmangutov.dreameater.custom.DreamChangedListener;
import com.kirillmangutov.dreameater.custom.PreferencesUpdatedListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class ListActivity extends FragmentActivity implements DreamChangedListener,
        PreferencesUpdatedListener {

    private static final String TAG = "DreamEater";

    @InjectView(R.id.listViewDreams)
    ListView listDreams;

    //@InjectView(R.id.fragment_container)


    DreamAdapter mAdapter;
    AlarmAdmin mAlarm;

    boolean mPreferencesOpen = false;
    boolean mItemOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        ButterKnife.inject(this);

        mAdapter = new DreamAdapter(this);
        listDreams.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAlarm = AlarmAdmin.getInstance(this);

        preferencesUpdated();
    }

    public void launchWriteActivity(String date_string, int position) {

        if(mItemOpen)
            return;
        mItemOpen = false;
        /*Intent intent = new Intent(WelcomeActivity.this,
                WriteActivity.class);
        intent.putExtra(WriteActivity.EXTRA_DATE_STRING, date_string);
        intent.putExtra(WriteActivity.EXTRA_ITEM_POSITION, position);
        startActivity(intent);*/

        Log.d("ANIMATION", "Enter launchWriteActivity()");

        WriteFragment fragment = new WriteFragment();

        AnimationGoal start = new AnimationGoal();
        start.y = listDreams.getHeight();
        start.h = listDreams.getHeight();

        AnimationGoal end = new AnimationGoal();
        end.y = 0;
        end.h = listDreams.getHeight();

        Bundle bundle = new Bundle();
        bundle.putString(WriteFragment.EXTRA_DATE_STRING, date_string);
        bundle.putInt(WriteFragment.EXTRA_ITEM_POSITION, position);
        bundle.putSerializable(WriteFragment.EXTRA_ANIM_START, start);
        bundle.putSerializable(WriteFragment.EXTRA_ANIM_END, end);

        fragment.setArguments(bundle);
        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();*/

        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.setCustomAnimations(R.anim.grow,
                R.anim.shrink,
                R.anim.grow,
                R.anim.shrink);
        trans.add(R.id.fragment_container, fragment);
        trans.addToBackStack(null).commit();
    }

    @OnItemClick(R.id.listViewDreams)
    void onItemClick(
            AdapterView<?> parent,
            View v,
            int position,
            long id) {

        Log.d("ANIMATION", "Enter onItemClick");
        Dream dream = (Dream) parent.getItemAtPosition(position);
        String date_string = dream.readableDate();

        launchWriteActivity(date_string, position);
    }

    public void openPreferences() {

        if(mPreferencesOpen)
            return;
        mPreferencesOpen = true;
        Log.d("PREF", "Enter openPreferences()");

        PrefFragment fragment = new PrefFragment();

        AnimationGoal start = new AnimationGoal();
        start.y = -listDreams.getHeight();
        start.h = listDreams.getHeight();

        AnimationGoal end = new AnimationGoal();
        end.y = 0;
        end.h = listDreams.getHeight();

        Bundle bundle = new Bundle();

        bundle.putSerializable(WriteFragment.EXTRA_ANIM_START, start);
        bundle.putSerializable(WriteFragment.EXTRA_ANIM_END, end);

        fragment.setArguments(bundle);
        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();*/

        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.setCustomAnimations(R.anim.grow,
                R.anim.shrink,
                R.anim.grow,
                R.anim.shrink);
        trans.add(R.id.fragment_container, fragment);
        trans.addToBackStack(null).commit();


    }

    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();

        Log.d("ALARM", getIntent() + ",TODAY:" + getIntent().getBooleanExtra("TODAY", false));
        if(getIntent().getBooleanExtra("TODAY", false)) {
            Log.d("ALARM", "onResume() inside if statement");

            final ViewTreeObserver observer = listDreams.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    launchWriteActivity(Dream.getToday().readableDate(), mAdapter.getCount());
                    listDreams.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();

        if(manager.getBackStackEntryCount() > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
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

            if(!mPreferencesOpen)
                openPreferences();
            else
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dreamChanged() {
        mAdapter.notifyDataSetChanged();
        mItemOpen = false;
    }


    @Override
    public void preferencesUpdated() {
        mPreferencesOpen = false;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean alarm = prefs.getBoolean("checkbox_notif_enabled", true);

        if(alarm) {
            //Toast.makeText(this, "alarm set up", Toast.LENGTH_SHORT).show();
            mAlarm.setAlarm();
        } else {
            //Toast.makeText(this, "alarm cancelled", Toast.LENGTH_SHORT).show();
            mAlarm.cancelAlarm();
        }
    }
}
