package com.kirillmangutov.dreameater;

import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kmangutov on 8/19/14.
 */
public class DateItem {

    @InjectView(R.id.tvDay) public TextView day;
    @InjectView(R.id.tvMonth) public TextView month;
    @InjectView(R.id.tvStringDay) public TextView dayString;

    public DateItem(View v) {

        ButterKnife.inject(this, v);
        day = (TextView) v.findViewById(R.id.tvDay);

    }

    public void fromDate(Date d) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd EEE", Locale.US);
        String[] all = dateFormat.format(d).split(" ");

        month.setText(all[0]);
        day.setText(all[1]);
        dayString.setText(all[2]);
    }

    public void setFgColor(int color) {
        day.setTextColor(color);
        month.setTextColor(color);
        dayString.setTextColor(color);
    }
}