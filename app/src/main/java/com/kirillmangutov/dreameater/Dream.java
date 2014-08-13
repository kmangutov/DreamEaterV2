package com.kirillmangutov.dreameater;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kirillmangutov on 7/4/14.
 */

@Table(name = "Dreams")
public class Dream extends Model implements Serializable {


    SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");

    @Column(name = "date_string")
    public String date_string;

    @Column(name = "date")
    public Date date;

    @Column(name = "contents")
    public String contents;

    public Dream() {

        super();

        contents = "";
        date = new Date();
        date_string = readableDate();
    }

    public Dream(String date_string) {

        super();

        try {
            contents = "";
            date = dateFormat.parse(date_string);
            this.date_string = date_string;
        }catch(Exception e){}
    }


    public String readableDate() {

        //MM = month, mm = minute
        return dateFormat.format(date);
    }

    public String shortDesc(int len) {

        if(contents.length() > len)
            return contents.substring(0, len) + "...";
        else
            return contents;
    }

    public static List<Dream> getAll() {
        return new Select()
                .from(Dream.class)
                .orderBy("date DESC")
                .execute();
    }

    public static Dream get(String date_string) {
        List<Dream> dreams = new Select()
                .from(Dream.class)
                .where("date_string = ?", date_string)
                .orderBy("date DESC")
                .execute();

        if(dreams.size() > 0)
            return dreams.get(0);
        return null;
    }
}
