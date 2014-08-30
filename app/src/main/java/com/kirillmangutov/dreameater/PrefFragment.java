package com.kirillmangutov.dreameater;

//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kirillmangutov.dreameater.custom.AnimationFinishListener;
import com.kirillmangutov.dreameater.custom.AnimationGoal;
import com.kirillmangutov.dreameater.custom.DreamChangedListener;
import com.kirillmangutov.dreameater.custom.GrowFrameLayout;
import com.kirillmangutov.dreameater.custom.PreferencesUpdatedListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PrefFragment extends Fragment {

    @InjectView(R.id.layoutPrefBase) GrowFrameLayout mBase;

    public static final String EXTRA_ANIM_START = "EXTRA_ANIM_START";
    public static final String EXTRA_ANIM_END= "EXTRA_ANIM_END";

    public Dream mDream;
    private boolean mOpening = true;

    private AlarmAdmin mAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_pref, container, false);
        ButterKnife.inject(this, view);

        mAlarm = AlarmAdmin.getInstance(getActivity());

        Bundle args = getArguments();
        AnimationGoal start = (AnimationGoal) args.getSerializable(EXTRA_ANIM_START);
        AnimationGoal end = (AnimationGoal) args.getSerializable(EXTRA_ANIM_END);
        mBase.start = start;
        mBase.end = end;

        mBase.setBackgroundColor(DreamAdapter.primaryColor(3));

        return view;
    }

    public GrowFrameLayout getLayout() {
        return mBase;
    }

    @Override
    public void onPause() {
        super.onPause();

        ((PreferencesUpdatedListener) getActivity()).preferencesUpdated();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PrefsFragment frag = new PrefsFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.add(R.id.layoutPrefBase, frag);
        trans.commit();
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle saved) {
            super.onCreate(saved);

            addPreferencesFromResource(R.xml.prefs);
        }
    }
}
