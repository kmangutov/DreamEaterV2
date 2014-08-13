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

/**
 * Created by kirillmangutov on 7/5/14.
 */
public class DreamAdapter extends ArrayAdapter<Dream> {

    //public final Context context;
    private List<Dream> dreams = new LinkedList<Dream>();

    public DreamAdapter(Context context) {

        super(context, R.layout.dream_item);

        dreams = Dream.getAll();
        if(!hasToday()) {
            dreams.add(0, getToday());
        }

        //dreams.add(0, getToday());

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
        Dream today = new Dream();
        today.date = new Date();
        today.contents = "";
        today.date_string = today.readableDate();
        return today;
    }

    public static int grayDelta(int index) {

        int mod = 16;
        int amt = index * mod;
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
        shortDesc.setText(current.shortDesc(10));

        convertView.setBackgroundColor(grayDelta(5));

        return convertView;
    }
}
