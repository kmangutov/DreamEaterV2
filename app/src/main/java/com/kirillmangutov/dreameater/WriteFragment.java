package com.kirillmangutov.dreameater;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kirillmangutov.dreameater.custom.AnimationFinishListener;
import com.kirillmangutov.dreameater.custom.AnimationGoal;
import com.kirillmangutov.dreameater.custom.DreamChangedListener;
import com.kirillmangutov.dreameater.custom.GrowFrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WriteFragment extends Fragment implements AnimationFinishListener {

    @InjectView(R.id.editTextContents) EditText mContents;
    @InjectView(R.id.layoutWriteBase) GrowFrameLayout mBase;
    DateItem mDateItem;

    public static final String EXTRA_DATE_STRING = "EXTRA_DATE_STRING";
    public static final String EXTRA_ITEM_POSITION = "EXTRA_ITEM_POSITION";
    public static final String EXTRA_ANIM_START = "EXTRA_ANIM_START";
    public static final String EXTRA_ANIM_END= "EXTRA_ANIM_END";

    public Dream mDream;
    private boolean mOpening = true;

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
        mBase.listener = this;

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

        mDateItem = new DateItem(view);
        mDateItem.fromDate(mDream.date);

        int id = args.getInt(EXTRA_ITEM_POSITION, 0);

        int primary = DreamAdapter.primaryColor(id);
        int secondary = DreamAdapter.secondaryColor(id);

        mBase.setBackgroundColor(primary);
        mDateItem.setFgColor(secondary);
    }

    public void openKeyboard() {
        mContents.requestFocus();
        mContents.setSelection(mContents.getText().length());
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(mContents, InputMethodManager.SHOW_IMPLICIT);
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

        ((DreamChangedListener)getActivity()).dreamChanged();
    }

    @Override
    public void animationFinish() {
        if(mOpening) {
            openKeyboard();
            mOpening = false;
        }
    }
}
