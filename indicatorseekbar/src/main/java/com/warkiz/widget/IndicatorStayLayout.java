package com.warkiz.widget;

import android.content.Context;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class IndicatorStayLayout extends LinearLayout {

    public IndicatorStayLayout(Context context) {
        this(context, null);
    }

    public IndicatorStayLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorStayLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            layoutIndicator(getChildAt(i), i);
        }
        super.onFinishInflate();
    }

    /**
     * To make indicator stay always by Java code
     *
     * @param seekBar the direct child in IndicatorStayLayout
     */
    public void attachTo(IndicatorSeekBar seekBar) {
        attachTo(seekBar, -2);
    }

    /**
     * To make indicator stay always by Java code
     *
     * @param seekBar the direct child in IndicatorStayLayout
     * @param index   the child index of IndicatorSeekBar attached to IndicatorStayLayout
     */
    public void attachTo(IndicatorSeekBar seekBar, int index) {
        if (seekBar == null) {
            throw new NullPointerException("The IndicatorSeekBar attached to IndicatorStayLayout can not be null");
        }

        layoutIndicator(seekBar, index);
        addView(seekBar, index + 1);
    }

    private void layoutIndicator(View child, int index) {
        if (child instanceof IndicatorSeekBar) {
            IndicatorSeekBar seekBar = (IndicatorSeekBar) child;
            seekBar.setIndicatorStayAlways();
            View contentView = seekBar.getIndicatorContentView();
            if (contentView == null) {
                throw new IllegalStateException("Can not find any indicator in IndicatorSeekBar. " +
                        "Please make sure you have called the attr: SHOW_INDICATOR_TYPE for IndicatorSeekBar and the value is not IndicatorType.NONE");
            }

            if (contentView instanceof IndicatorSeekBar) {
                throw new IllegalStateException("IndicatorContentView can not be an instance of IndicatorSeekBar");
            }

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            MarginLayoutParams layoutParams = new MarginLayoutParams(params);
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                    layoutParams.rightMargin, SizeUtils.dp2px(seekBar.getContext(), 2) - seekBar.getPaddingTop());
            addView(contentView, index, layoutParams);
            seekBar.showStayIndicator();
        }
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation != VERTICAL) {
            throw new IllegalArgumentException("IndicatorStayLayout is always vertical and doesn't support horizontal orientation");
        }
        super.setOrientation(orientation);
    }
}
