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
import java.util.logging.Logger;

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
        //    dreams.add(0, getToday(20 - i));


        if(!hasToday()) {
            dreams.add(0, getToday());
        }


        addAll(dreams);
    }

    private boolean hasToday() {
        Dream today = getToday();

        for(Dream dream : dreams) {

            if(dream.date_string.equals(today.date_string))
                return true;
        }
        return false;
    }

    private Dream getToday() {
        return getToday(0);
    }

    private Dream getToday(int deltaDay) {
        Dream today = new Dream();
        today.date = new Date();
        today.date.setDate(today.date.getDate() - deltaDay);
        today.contents = "";
        today.date_string = today.readableDate();
        return today;
    }

    public static int grayDelta(int index) {

        int mod = 16;
        int amt = (4 + index) * mod;
        return Color.rgb(240 - amt, 240 - amt, 240 - amt);
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(convertView == null) {

            convertView = inflater.inflate(
                    R.layout.dream_item, parent, false);
        }

        Dream current = dreams.get(position);

        TextView textView = (TextView) convertView.findViewById(R.id.textViewDate);
        textView.setText(current.readableDate());

        TextView shortDesc = (TextView) convertView.findViewById(R.id.textViewDesc);
        shortDesc.setText(current.shortDesc(30));

        int clampedOffset = (int) (Math.sin(Math.toRadians(position * 30)) * 4);
        int opposite = (int) (Math.sin(Math.toRadians(position * 30)) * 4) + 6;

        Log.d("offset", "pos:" + position + " clampedOffset:" + clampedOffset);

        convertView.setBackgroundColor(grayDelta(clampedOffset));
        textView.setTextColor(grayDelta(opposite));

        return convertView;
    }
}
