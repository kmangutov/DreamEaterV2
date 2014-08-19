package com.kirillmangutov.dreameater;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kirillmangutov.dreameater.custom.AnimationGoal;
import com.kirillmangutov.dreameater.custom.GrowFrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WriteFragment extends Fragment {

    @InjectView(R.id.editTextContents) EditText mContents;
    @InjectView(R.id.textViewTitle) TextView mTitle;
    @InjectView(R.id.layoutWriteBase) GrowFrameLayout mBase;

    public static final String EXTRA_DATE_STRING = "EXTRA_DATE_STRING";
    public static final String EXTRA_ITEM_POSITION = "EXTRA_ITEM_POSITION";
    public static final String EXTRA_ANIM_START = "EXTRA_ANIM_START";
    public static final String EXTRA_ANIM_END= "EXTRA_ANIM_END";

    public Dream mDream;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);
        ButterKnife.inject(this, view);

        Bundle args = getArguments();
        AnimationGoal start = (AnimationGoal) args.getSerializable(EXTRA_ANIM_START);
        AnimationGoal end = (AnimationGoal) args.getSerializable(EXTRA_ANIM_END);

        mBase.start = start;
        mBase.end = end;

        return view;
    }

    public GrowFrameLayout getLayout() {
        return mBase;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String date_string = args.getString(EXTRA_DATE_STRING, Dream.getToday().readableDate());

        mDream = getDream(date_string);
        mContents.setText(mDream.contents);
        mTitle.setText(mDream.shortDate());

        int id = args.getInt(EXTRA_ITEM_POSITION, 0);

        int primary = DreamAdapter.primaryColor(id);
        int secondary = DreamAdapter.secondaryColor(id);

        mTitle.getRootView().setBackgroundColor(primary);
        mTitle.setTextColor(secondary);
        mBase.setBackgroundColor(primary);

        mContents.requestFocus();
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

    }

    @Override
    public void onPause() {
        super.onPause();

        mDream.contents = mContents.getText().toString();
        mDream.save();
    }
}
