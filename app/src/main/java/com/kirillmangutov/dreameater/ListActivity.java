package com.kirillmangutov.dreameater;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirillmangutov.dreameater.custom.AnimationGoal;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class ListActivity extends FragmentActivity {

    private static final String TAG = "DreamEater";

    @InjectView(R.id.listViewDreams)
    ListView listDreams;

    //@InjectView(R.id.fragment_container)


    DreamAdapter mAdapter;
    AlarmAdmin mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.inject(this);

        mAdapter = new DreamAdapter(this);
        listDreams.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mAlarm = new AlarmAdmin(this);
        mAlarm.setAlarm();


    }

    public void launchWriteActivity(View view, String date_string, int position) {
        /*Intent intent = new Intent(WelcomeActivity.this,
                WriteActivity.class);
        intent.putExtra(WriteActivity.EXTRA_DATE_STRING, date_string);
        intent.putExtra(WriteActivity.EXTRA_ITEM_POSITION, position);
        startActivity(intent);*/

        Log.d("ANIMATION", "Enter launchWriteActivity()");

        WriteFragment fragment = new WriteFragment();

        AnimationGoal start = new AnimationGoal();
        start.y = (int)view.getY();
        start.h = view.getHeight();

        AnimationGoal end = new AnimationGoal();
        end.y = 0;
        end.h = listDreams.getHeight();

        int[] coords = new int[2];
        int[] parentCoords = new int[2];

        view.findViewById(R.id.textViewDate).getLocationInWindow(coords);
        listDreams.getLocationInWindow(parentCoords);

        end.absTextX = coords[0] - parentCoords[0];
        end.absTextY = coords[1] - parentCoords[1];

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

        launchWriteActivity(v, date_string, position);
    }

    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
