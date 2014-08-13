package com.kirillmangutov.dreameater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.kirillmangutov.dreameater.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends Activity {

    @InjectView(R.id.editTextContents)
    EditText contents;

    @InjectView(R.id.textViewTitle)
    TextView title;

    public static final String EXTRA_DATE_STRING = "EXTRA_DATE_STRING";
    public Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String date_string = intent.getStringExtra(EXTRA_DATE_STRING);

        dream = getDream(date_string);
        contents.setText(dream.contents);
        title.setText(dream.readableDate());
    }


    public Dream getDream(String date_string) {

        Dream dream = Dream.get(date_string);

        if(dream == null) {
            dream = new Dream();
        }

        return dream;
    }

    @Override
    public void onPause() {

        super.onPause();

        dream.contents = contents.getText().toString();
        dream.save();
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
