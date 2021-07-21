package com.warkiz.widget;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

public class Indicator {

    private final Context mContext;

    private final int mWindowWidth;
    private final int[] mLocation = new int[2];

    private ArrowView mArrowView;
    private TextView mProgressTextView;
    private PopupWindow mIndicatorPopW;
    private LinearLayout mTopContentView;

    private final int mGap;
    private final int mIndicatorColor;
    private int mIndicatorType;

    private final IndicatorSeekBar mSeekBar;
    private final View mIndicatorCustomTopContentView;
    private View mIndicatorView;
    private View mIndicatorCustomView;

    private final float mIndicatorTextSize;
    private final int mIndicatorTextColor;

    public Indicator(Context context,
                     IndicatorSeekBar seekBar,
                     int indicatorColor,
                     int indicatorType,
                     int indicatorTextSize,
                     int indicatorTextColor,
                     View indicatorCustomView,
                     View indicatorCustomTopContentView) {
        this.mContext = context;
        this.mSeekBar = seekBar;
        this.mIndicatorColor = indicatorColor;
        this.mIndicatorType = indicatorType;
        this.mIndicatorCustomView = indicatorCustomView;
        this.mIndicatorCustomTopContentView = indicatorCustomTopContentView;
        this.mIndicatorTextSize = indicatorTextSize;
        this.mIndicatorTextColor = indicatorTextColor;

        mWindowWidth = getWindowWidth();
        mGap = SizeUtils.dp2px(mContext, 2);

        initIndicator();
    }

    private void initIndicator() {
        if (mIndicatorType == IndicatorType.CUSTOM) {
            if (mIndicatorCustomView != null) {
                mIndicatorView = mIndicatorCustomView;

                int progressTextViewId = mContext.getResources().getIdentifier("isb_progress", "id", mContext.getApplicationContext().getPackageName());

                if (progressTextViewId > 0) {
                    View view = mIndicatorView.findViewById(progressTextViewId);

                    if (view != null) {
                        if (view instanceof TextView) {
                            mProgressTextView = (TextView) view;
                            mProgressTextView.setText(mSeekBar.getIndicatorTextString());
                            mProgressTextView.setTextSize(SizeUtils.px2sp(mContext, mIndicatorTextSize));
                            mProgressTextView.setTextColor(mIndicatorTextColor);
                        } else {
                            throw new ClassCastException("The view identified by isb_progress in indicator custom layout can not be cast to TextView");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("The attr：indicator_custom_layout must be set while you set the indicator type to CUSTOM");
            }
        } else {
            if (mIndicatorType == IndicatorType.CIRCULAR_BUBBLE) {

                mIndicatorView = new CircleBubbleView(mContext, mIndicatorTextSize, mIndicatorTextColor, mIndicatorColor, "1000");
                ((CircleBubbleView) mIndicatorView).setProgress(mSeekBar.getIndicatorTextString());
            } else {
                mIndicatorView = View.inflate(mContext, R.layout.isb_indicator, null);

                mTopContentView = mIndicatorView.findViewById(R.id.indicator_container);

                mArrowView = mIndicatorView.findViewById(R.id.indicator_arrow);
                mArrowView.setColor(mIndicatorColor);

                mProgressTextView = mIndicatorView.findViewById(R.id.isb_progress);
                mProgressTextView.setText(mSeekBar.getIndicatorTextString());
                mProgressTextView.setTextSize(SizeUtils.px2sp(mContext, mIndicatorTextSize));
                mProgressTextView.setTextColor(mIndicatorTextColor);

                mTopContentView.setBackground(getGradientDrawable());

                if (mIndicatorCustomTopContentView != null) {

                    int progressTextViewId = mContext.getResources().getIdentifier("isb_progress", "id", mContext.getApplicationContext().getPackageName());

                    View topContentView = mIndicatorCustomTopContentView;

                    if (progressTextViewId > 0) {
                        View tv = topContentView.findViewById(progressTextViewId);
                        if (tv instanceof TextView) {
                            setTopContentView(topContentView, (TextView) tv);
                        } else {
                            setTopContentView(topContentView);
                        }
                    } else {
                        setTopContentView(topContentView);
                    }
                }
            }
        }
    }

    @NonNull
    private GradientDrawable getGradientDrawable() {
        GradientDrawable tvDrawable;

        if (mIndicatorType == IndicatorType.ROUNDED_RECTANGLE) {
            tvDrawable = (GradientDrawable) AppCompatResources.getDrawable(mContext, R.drawable.isb_indicator_round);
        } else {
            tvDrawable = (GradientDrawable) AppCompatResources.getDrawable(mContext, R.drawable.isb_indicator_square);
        }

        if (tvDrawable == null) {
            tvDrawable = new GradientDrawable();

            if (mIndicatorType == IndicatorType.ROUNDED_RECTANGLE) {
                tvDrawable.setSize(SizeUtils.dp2px(mContext, 28), SizeUtils.dp2px(mContext, 16));
                tvDrawable.setCornerRadius(SizeUtils.dp2px(mContext, 8));
            } else {
                tvDrawable.setSize(SizeUtils.dp2px(mContext, 24), SizeUtils.dp2px(mContext, 16));
            }
        }

        tvDrawable.mutate();
        tvDrawable.setColor(mIndicatorColor);

        return tvDrawable;
    }

    private int getWindowWidth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

            if (windowManager != null) {
                WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();

                Insets insets = windowMetrics.getWindowInsets()
                        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());

                return windowMetrics.getBounds().width() - insets.left - insets.right;
            }
        } else {
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            if (metrics != null) {
                return metrics.widthPixels;
            }
        }

        return 0;
    }

    private int getIndicatorScreenX() {
        mSeekBar.getLocationOnScreen(mLocation);
        return mLocation[0];
    }

    private void adjustArrow(float touchX) {
        if (mIndicatorType == IndicatorType.CUSTOM || mIndicatorType == IndicatorType.CIRCULAR_BUBBLE) {
            return;
        }

        int indicatorScreenX = getIndicatorScreenX();

        if (indicatorScreenX + touchX < mIndicatorPopW.getContentView().getMeasuredWidth() / 2.0f) {
            setMargin(mArrowView, -(int) (mIndicatorPopW.getContentView().getMeasuredWidth() / 2 - indicatorScreenX - touchX), -1, -1, -1);
        } else if (mWindowWidth - indicatorScreenX - touchX < mIndicatorPopW.getContentView().getMeasuredWidth() / 2.0f) {
            setMargin(mArrowView, (int) (mIndicatorPopW.getContentView().getMeasuredWidth() / 2 - (mWindowWidth - indicatorScreenX - touchX)), -1, -1, -1);
        } else {
            setMargin(mArrowView, 0, 0, 0, 0);
        }
    }

    private void setMargin(View view, int left, int top, int right, int bottom) {
        if (view == null) {
            return;
        }

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(left == -1 ? layoutParams.leftMargin : left, top == -1 ? layoutParams.topMargin : top, right == -1 ? layoutParams.rightMargin : right, bottom == -1 ? layoutParams.bottomMargin : bottom);
            view.requestLayout();
        }
    }

    void iniPop() {
        if (mIndicatorPopW != null) {
            return;
        }

        if (mIndicatorType != IndicatorType.NONE && mIndicatorView != null) {
            mIndicatorView.measure(0, 0);
            mIndicatorPopW = new PopupWindow(mIndicatorView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        }
    }

    View getInsideContentView() {
        return mIndicatorView;
    }

    void setProgressTextView(String text) {
        if (mIndicatorView instanceof CircleBubbleView) {
            ((CircleBubbleView) mIndicatorView).setProgress(text);
        } else if (mProgressTextView != null) {
            mProgressTextView.setText(text);
        }
    }

    void updateIndicatorLocation(int offset) {
        setMargin(mIndicatorView, offset, -1, -1, -1);
    }

    void updateArrowViewLocation(int offset) {
        setMargin(mArrowView, offset, -1, -1, -1);
    }


    /**
     * Update the indicator position
     *
     * @param touchX the x location without padding left
     */
    void update(float touchX) {
        if (!mSeekBar.isEnabled() || !(mSeekBar.getVisibility() == View.VISIBLE)) {
            return;
        }

        refreshProgressText();

        if (mIndicatorPopW != null) {
            mIndicatorPopW.getContentView().measure(0, 0);
            mIndicatorPopW.update(mSeekBar, (int) (touchX - mIndicatorPopW.getContentView().getMeasuredWidth() / 2),
                    -(mSeekBar.getMeasuredHeight() + mIndicatorPopW.getContentView().getMeasuredHeight() - mSeekBar.getPaddingTop() /*- mSeekBar.getTextHeight() */ + mGap), -1, -1);
            adjustArrow(touchX);
        }
    }

    /**
     * To show the indicator
     *
     * @param touchX the x location, padding left excluded
     */
    void show(float touchX) {
        if (!mSeekBar.isEnabled() || !(mSeekBar.getVisibility() == View.VISIBLE)) {
            return;
        }

        refreshProgressText();

        if (mIndicatorPopW != null) {
            mIndicatorPopW.getContentView().measure(0, 0);
            mIndicatorPopW.showAsDropDown(mSeekBar, (int) (touchX - mIndicatorPopW.getContentView().getMeasuredWidth() / 2f),
                    -(mSeekBar.getMeasuredHeight() + mIndicatorPopW.getContentView().getMeasuredHeight() - mSeekBar.getPaddingTop() /*- mSeekBar.getTextHeight()*/ + mGap));
            adjustArrow(touchX);
        }
    }

    void refreshProgressText() {
        String tickTextString = mSeekBar.getIndicatorTextString();

        if (mIndicatorView instanceof CircleBubbleView) {
            ((CircleBubbleView) mIndicatorView).setProgress(tickTextString);
        } else if (mProgressTextView != null) {
            mProgressTextView.setText(tickTextString);
        }
    }

    /**
     * To hide the indicator
     */
    void hide() {
        if (mIndicatorPopW == null) {
            return;
        }

        mIndicatorPopW.dismiss();
    }

    /**
     * Returns the state of the indicator
     */
    boolean isShowing() {
        return mIndicatorPopW != null && mIndicatorPopW.isShowing();
    }

    /**
     * Get the indicator content view
     *
     * @return the view which is inside indicator
     */
    public View getContentView() {
        return mIndicatorView;
    }

    /**
     * To replace the current indicator with a new indicator view, indicator arrow will be replaced too
     *
     * @param customIndicatorView a new content view for indicator
     */
    public void setContentView(@NonNull View customIndicatorView) {
        this.mIndicatorType = IndicatorType.CUSTOM;
        this.mIndicatorCustomView = customIndicatorView;

        initIndicator();
    }

    /**
     * To replace the current indicator with a new indicator view, indicator arrow will be replaced too
     *
     * @param customIndicatorView a new content view for indicator
     * @param progressTextView    this TextView will show the progress or tick text, must be found in @param customIndicatorView
     */
    public void setContentView(@NonNull View customIndicatorView, TextView progressTextView) {
        this.mProgressTextView = progressTextView;
        this.mIndicatorType = IndicatorType.CUSTOM;
        this.mIndicatorCustomView = customIndicatorView;

        initIndicator();
    }

    /**
     * Get the indicator top content view
     * If indicator type {@link IndicatorType} is CUSTOM or CIRCULAR_BUBBLE, this method will get a null value
     *
     * @return the view which is inside indicator top part, without including the arrow
     */
    public View getTopContentView() {
        return mTopContentView;
    }

    /**
     * Set the view to the indicator top container
     * If indicator type {@link IndicatorType} is CUSTOM or CIRCULAR_BUBBLE, this method won't work
     *
     * @param topContentView the view is inside the indicator top part
     */
    public void setTopContentView(@NonNull View topContentView) {
        setTopContentView(topContentView, null);
    }

    /**
     * Set the view to the indicator top container and show the changing progress in indicator when seek
     * If indicator type is CUSTOM, this method won't work
     *
     * @param topContentView   the view is inside the indicator top part
     * @param progressTextView this TextView will show the progress or tick text, must be found in @param topContentView
     */
    public void setTopContentView(@NonNull View topContentView, @Nullable TextView progressTextView) {
        this.mProgressTextView = progressTextView;
        this.mTopContentView.removeAllViews();

        topContentView.setBackground(getGradientDrawable());

        this.mTopContentView.addView(topContentView);
    }
}