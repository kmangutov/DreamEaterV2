package com.kirillmangutov.dreameater.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Constructor;

/**
 * Created by kmangutov on 8/18/14.
 */
public class GrowFrameLayout extends RelativeLayout {

    public AnimationGoal start;
    public AnimationGoal end;

    private float mGrowFraction = 0;

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


        /*FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                getRootView().getWidth(),
                (int)newH
        );
        //params.height = (int)newH;
        //params.width = getRootView().getWidth();
        setLayoutParams(params);
        this.setMinimumHeight((int)newH);*/

        //setScaleY( (end.y/start.y) * fraction );

        //this.setPadding(0, 0, 0, -1 * (int)newH);
        resizeView(this, this.getRootView().getWidth(), (int)newH);
        //invalidate();

        Log.d("ANIMATION", "x:" + getX() + "\t\ty:" + getY());
        Log.d("ANIMATION", "w:" + getWidth() + "\t\th:" + getHeight() + "\t\tnewH:" + newH);
    }

    public float getGrowFraction() {
        return mGrowFraction;
    }
}

