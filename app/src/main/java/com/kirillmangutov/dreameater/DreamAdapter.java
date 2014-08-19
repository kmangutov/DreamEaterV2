package com.kirillmangutov.dreameater;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kirillmangutov on 7/5/14.
 */
public class DreamAdapter extends ArrayAdapter<Dream> {

    //public final Context context;
    private List<Dream> dreams = new LinkedList<Dream>();

    public DreamAdapter(Context context) {
        super(context, R.layout.dream_item);

        dreams = Dream.getAll();
        //for(int i = 1; i < 20; i++)
        //    dreams.add(0, Dream.daysAgo(20 - i));

        if(!hasToday()) {
            dreams.add(0, Dream.getToday());
        }

        addAll(dreams);
    }

    private boolean hasToday() {
        Dream today = Dream.getToday();
        return Dream.get(today.date_string) != null;
    }


    public static int clampPosition(int position) {
        return (int) (Math.sin(Math.toRadians(position * 30)) * 4);
    }

    public static int grayDelta(int index) {
        int r = 0;
        int g = 0;
        int b = 0;

        int mod = 8;
        int amt = (8 + index) * mod;
        return Color.rgb(r + amt, g + amt, b + amt);
        //return Color.rgb(204, 204 - amt, 204 - amt);
    }

    public static int primaryColor(int position) {
        return grayDelta(clampPosition(position));
    }

    public static int secondaryColor(int position) {
        return grayDelta(clampPosition(position) + 2);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {

            convertView = inflater.inflate(
                    R.layout.dream_item, parent, false);
        }

        Dream current = dreams.get(position);
        DreamItem item = new DreamItem(convertView);

        int primary = primaryColor(position);
        int secondary = secondaryColor(position);

        convertView.setBackgroundColor(primary);
        item.date.fromDate(current.date);
        item.date.setFgColor(secondary);
        item.summary.setText(current.shortDesc(25).replaceAll("\n", ""));

        return convertView;
    }

    public static class DreamItem {

        public DateItem date;
        @InjectView(R.id.tvSummary) public TextView summary;

        public DreamItem(View v) {
            ButterKnife.inject(this, v);
            date = new DateItem(v);
        }
    }
}
