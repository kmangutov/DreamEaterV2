package com.kirillmangutov.dreameater.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Constructor;

/**
 * Created by kmangutov on 8/18/14.
 */
public class GrowFrameLayout extends LinearLayout {

    public AnimationGoal start;
    public AnimationGoal end;

    private float mGrowFraction = 0;
    public AnimationFinish listener;

    public GrowFrameLayout(Context ctx) {
        super(ctx);
    }

    public GrowFrameLayout(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public GrowFrameLayout(Context ctx, AttributeSet attrs, int s) {
        super(ctx, attrs, s);
    }

    private void resizeView(View view, int newWidth, int newHeight) { try { Constructor<? extends ViewGroup.LayoutParams> ctor = view.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class); view.setLayoutParams(ctor.newInstance(newWidth, newHeight)); } catch (Exception e) { e.printStackTrace(); } }

    public void setGrowFraction(final float fraction) {
        Log.d("ANIMATION", "Enter setGrowFraction(" + fraction + ")");

        mGrowFraction = fraction;

        float newY = start.y + (end.y - start.y) * fraction;
        float newH = start.h + (end.h - start.h) * fraction;

        setTranslationY(newY);
        resizeView(this, this.getRootView().getWidth(), (int)newH);

        Log.d("ANIMATION", "x:" + getX() + "\t\ty:" + getY());
        Log.d("ANIMATION", "w:" + getWidth() + "\t\th:" + getHeight() + "\t\tnewH:" + newH);

        if(fraction == 1 && listener != null) {
            listener.animationFinish();
        }
    }

    public float getGrowFraction() {
        return mGrowFraction;
    }
}

