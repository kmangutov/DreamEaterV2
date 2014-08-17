package com.kirillmangutov.dreameater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WriteActivity extends Activity {

    @InjectView(R.id.editTextContents)
    EditText mContents;

    @InjectView(R.id.textViewTitle)
    TextView mTitle;

    public static final String EXTRA_DATE_STRING = "EXTRA_DATE_STRING";
    public static final String EXTRA_ITEM_POSITION = "EXTRA_ITEM_POSITION";
    public Dream mDream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String date_string = intent.getStringExtra(EXTRA_DATE_STRING);
        if(date_string == null) {
            date_string = Dream.getToday().readableDate();
        }

        mDream = getDream(date_string);
        mContents.setText(mDream.contents);
        mTitle.setText(mDream.shortDate());

        int id = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);

        int primary = DreamAdapter.primaryColor(id);
        int secondary = DreamAdapter.secondaryColor(id);

        mTitle.getRootView().setBackgroundColor(primary);
        mTitle.setTextColor(secondary);
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mContents, InputMethodManager.SHOW_IMPLICIT);
    }

    public Dream getDream(String date_string) {
        Dream dream = Dream.get(date_string);

        if(dream == null) {
            dream = new Dream(date_string);
        }

        return dream;
    }

    @Override
    public void onResume() {
        super.onResume();

        showKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();

        mDream.contents = mContents.getText().toString();
        mDream.save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.write, menu);
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
