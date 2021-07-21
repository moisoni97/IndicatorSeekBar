package com.warkiz.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class Builder {

    final Context context;

    float max = 100;
    float min = 0;
    float progress = 0;

    boolean progressValueFloat = false;
    boolean seekSmoothly = false;
    boolean r2l = false;
    boolean userSeekable = true;
    boolean onlyThumbDraggable = false;
    boolean clearPadding = false;

    int showIndicatorType = IndicatorType.ROUNDED_RECTANGLE;
    int indicatorColor = Color.parseColor("#FF4081");
    int indicatorTextColor = Color.parseColor("#FFFFFF");
    int indicatorTextSize;

    View indicatorContentView = null;
    View indicatorTopContentView = null;

    int trackBackgroundSize;
    int trackBackgroundColor = Color.parseColor("#D7D7D7");
    int trackProgressSize;
    int trackProgressColor = Color.parseColor("#FF4081");

    boolean trackRoundedCorners = false;

    int thumbTextColor = Color.parseColor("#FF4081");

    boolean showThumbText = false;

    int thumbSize;
    int thumbColor = Color.parseColor("#FF4081");

    ColorStateList thumbColorStateList = null;

    Drawable thumbDrawable = null;

    boolean showTickText;

    int tickTextColor = Color.parseColor("#FF4081");
    int tickTextSize;

    String[] tickTextCustomArray = null;

    Typeface tickTextTypeFace = Typeface.DEFAULT;

    ColorStateList tickTextColorStateList = null;

    int tickCount = 0;
    int showTickMarkType = TickMarkType.NONE;
    int tickMarkColor = Color.parseColor("#FF4081");
    int tickMarkSize;

    Drawable tickMarkDrawable = null;

    boolean tickMarkEndsHide = false;
    boolean tickMarkSweptHide = false;

    ColorStateList tickMarkColorStateList = null;

    Builder(Context context) {
        this.context = context;
        this.indicatorTextSize = SizeUtils.sp2px(context, 14);
        this.trackBackgroundSize = SizeUtils.dp2px(context, 2);
        this.trackProgressSize = SizeUtils.dp2px(context, 2);
        this.tickMarkSize = SizeUtils.dp2px(context, 10);
        this.tickTextSize = SizeUtils.sp2px(context, 13);
        this.thumbSize = SizeUtils.dp2px(context, 14);
    }

    public IndicatorSeekBar build() {
        return new IndicatorSeekBar(this);
    }

    /**
     * Set the upper limit of the SeekBar range
     *
     * @param max the max range
     * @return Builder
     */
    public Builder max(float max) {
        this.max = max;
        return this;
    }

    /**
     * Set the lower limit of the SeekBar range
     *
     * @param min the min range
     * @return Builder
     */
    public Builder min(float min) {
        this.min = min;
        return this;
    }

    /**
     * Set the current progress to the specified value
     *
     * @param progress the current level of SeekBar
     * @return Builder
     */
    public Builder progress(float progress) {
        this.progress = progress;
        return this;
    }

    /**
     * Set the progress in float type
     *
     * @param isFloatProgress true for float progress. Default int type
     * @return Builder
     */
    public Builder progressValueFloat(boolean isFloatProgress) {
        this.progressValueFloat = isFloatProgress;
        return this;
    }

    /**
     * Seek continuously or discrete
     *
     * @param seekSmoothly true for seek continuously ignoring tick marks
     * @return Builder
     */
    public Builder seekSmoothly(boolean seekSmoothly) {
        this.seekSmoothly = seekSmoothly;
        return this;
    }

    /**
     * Right to left support
     *
     * @param r2l true to enable right to left support
     * @return Builder
     */
    public Builder r2l(boolean r2l) {
        this.r2l = r2l;
        return this;
    }

    /**
     * SeekBar has a default padding on left and right (16dp size)
     *
     * @param clearPadding true to clear the default padding
     * @return Builder
     */
    public Builder clearPadding(boolean clearPadding) {
        this.clearPadding = clearPadding;
        return this;
    }

    /**
     * Prevent user to seek
     *
     * @param userSeekable true user can seek
     * @return Builder
     */
    public Builder userSeekable(boolean userSeekable) {
        this.userSeekable = userSeekable;
        return this;
    }

    /**
     * User can change the thumb location by touching the thumb. Touching the track won't work
     *
     * @param onlyThumbDraggable true for seeking only by dragging the thumb. Default false
     * @return Builder
     */
    public Builder onlyThumbDraggable(boolean onlyThumbDraggable) {
        this.onlyThumbDraggable = onlyThumbDraggable;
        return this;
    }

    /**
     * To show different shape of indicator
     *
     * @param showIndicatorType see{@link IndicatorType}
     *                          IndicatorType.NONE;
     *                          IndicatorType.CIRCULAR_BUBBLE;
     *                          IndicatorType.ROUNDED_RECTANGLE;
     *                          IndicatorType.RECTANGLE;
     *                          IndicatorType.CUSTOM;
     * @return Builder
     */
    public Builder showIndicatorType(int showIndicatorType) {
        this.showIndicatorType = showIndicatorType;
        return this;
    }

    /**
     * Set the SeekBar indicator color. Have no influence on custom indicator
     *
     * @param indicatorColor colorInt
     * @return Builder
     */
    public Builder indicatorColor(@ColorInt int indicatorColor) {
        this.indicatorColor = indicatorColor;
        return this;
    }

    /**
     * Set the color for indicator text. Have no influence on custom tick drawable
     *
     * @param indicatorTextColor colorInt
     * @return Builder
     */
    public Builder indicatorTextColor(@ColorInt int indicatorTextColor) {
        this.indicatorTextColor = indicatorTextColor;
        return this;
    }

    /**
     * Change the size for indicator text. Have no influence on custom indicator
     *
     * @param indicatorTextSize the scaled pixel size.
     * @return Builder
     */
    public Builder indicatorTextSize(int indicatorTextSize) {
        this.indicatorTextSize = SizeUtils.sp2px(context, indicatorTextSize);
        return this;
    }

    /**
     * Set the SeekBar custom indicator view. Have effect only on custom indicator type
     *
     * @param indicatorContentView the custom indicator view
     * @return Builder
     */
    public Builder indicatorContentView(@NonNull View indicatorContentView) {
        this.indicatorContentView = indicatorContentView;
        return this;
    }

    /**
     * Set the SeekBar custom indicator layout identifier. Have effect only on custom indicator type
     *
     * @param layoutId the custom indicator layout identifier
     * @return Builder
     */
    public Builder indicatorContentViewLayoutId(@LayoutRes int layoutId) {
        this.indicatorContentView = View.inflate(context, layoutId, null);
        return this;
    }

    /**
     * Set the SeekBar custom top content view. Have no effect on custom and circular_bubble indicator type
     *
     * @param topContentView the custom indicator top content view
     * @return Builder
     */
    public Builder indicatorTopContentView(View topContentView) {
        this.indicatorTopContentView = topContentView;
        return this;
    }

    /**
     * Set the SeekBar custom top content view layout identifier. Have no effect on custom and circular_bubble indicator type
     *
     * @param layoutId the custom view for indicator top content layout identifier
     * @return Builder
     */
    public Builder indicatorTopContentViewLayoutId(@LayoutRes int layoutId) {
        this.indicatorTopContentView = View.inflate(context, layoutId, null);
        return this;
    }


    /**
     * Set the SeekBar background track stroke width
     *
     * @param trackBackgroundSize the dp size
     * @return Builder
     */
    public Builder trackBackgroundSize(int trackBackgroundSize) {
        this.trackBackgroundSize = SizeUtils.dp2px(context, trackBackgroundSize);
        return this;
    }

    /**
     * Set the SeekBar background track color
     *
     * @param trackBackgroundColor colorInt
     * @return Builder
     */
    public Builder trackBackgroundColor(@ColorInt int trackBackgroundColor) {
        this.trackBackgroundColor = trackBackgroundColor;
        return this;
    }

    /**
     * Set the SeekBar progress track stroke width
     *
     * @param trackProgressSize the dp size
     * @return Builder
     */
    public Builder trackProgressSize(int trackProgressSize) {
        this.trackProgressSize = SizeUtils.dp2px(context, trackProgressSize);
        return this;
    }

    /**
     * Set the SeekBar progress track color
     *
     * @param trackProgressColor colorInt
     * @return Builder
     */
    public Builder trackProgressColor(@ColorInt int trackProgressColor) {
        this.trackProgressColor = trackProgressColor;
        return this;
    }

    /**
     * To show the SeekBar ends to square corners. Default rounded corners
     *
     * @param trackRoundedCorners false to show square corners
     * @return Builder
     */
    public Builder trackRoundedCorners(boolean trackRoundedCorners) {
        this.trackRoundedCorners = trackRoundedCorners;
        return this;
    }

    /**
     * Set the SeekBar thumb text color
     *
     * @param thumbTextColor colorInt
     * @return Builder
     */
    public Builder thumbTextColor(@ColorInt int thumbTextColor) {
        this.thumbTextColor = thumbTextColor;
        return this;
    }

    /**
     * To show the text below thumb
     *
     * @param showThumbText show the text below thumb
     * @return Builder
     */
    public Builder showThumbText(boolean showThumbText) {
        this.showThumbText = showThumbText;
        return this;
    }

    /**
     * Set the SeekBar thumb color
     *
     * @param thumbColor colorInt
     * @return Builder
     */
    public Builder thumbColor(@ColorInt int thumbColor) {
        this.thumbColor = thumbColor;
        return this;
    }

    /**
     * Set the SeekBar thumb selector color
     *
     * @param thumbColorStateList color selector
     * @return Builder
     */
    public Builder thumbColorStateList(@NonNull ColorStateList thumbColorStateList) {
        this.thumbColorStateList = thumbColorStateList;
        return this;
    }

    /**
     * Set the SeekBar thumb width. Will be limited in 30dp
     *
     * @param thumbSize the dp size
     * @return Builder
     */
    public Builder thumbSize(int thumbSize) {
        this.thumbSize = SizeUtils.dp2px(context, thumbSize);
        return this;
    }

    /**
     * Set a new thumb drawable
     *
     * @param thumbDrawable the drawable for thumb
     * @return Builder
     */
    public Builder thumbDrawable(@NonNull Drawable thumbDrawable) {
        this.thumbDrawable = thumbDrawable;
        return this;
    }

    /**
     * To custom the thumb showing drawable by selector drawable
     *
     * @param thumbStateListDrawable the drawable show as thumb
     * @return Builder
     */
    public Builder thumbDrawable(@NonNull StateListDrawable thumbStateListDrawable) {
        this.thumbDrawable = thumbStateListDrawable;
        return this;
    }


    /**
     * Show the tick text
     *
     * @param showTickText show the text below track
     * @return Builder
     */
    public Builder showTickText(boolean showTickText) {
        this.showTickText = showTickText;
        return this;
    }

    /**
     * Set the color for text below/above SeekBar tick text
     *
     * @param tickTextColor colorInt
     * @return Builder
     */
    public Builder tickTextColor(@ColorInt int tickTextColor) {
        this.tickTextColor = tickTextColor;
        return this;
    }

    /**
     * Set the selector color for text below/above SeekBar tick text
     *
     * @param tickTextColorStateList colorInt
     * @return Builder
     */
    public Builder tickTextColorStateList(@NonNull ColorStateList tickTextColorStateList) {
        this.tickTextColorStateList = tickTextColorStateList;
        return this;
    }

    /**
     * Set the size for tick text below/above SeekBar
     *
     * @param tickTextSize the scaled pixel size
     * @return Builder
     */
    public Builder tickTextSize(int tickTextSize) {
        this.tickTextSize = SizeUtils.sp2px(context, tickTextSize);
        return this;
    }

    /**
     * To replace the SeekBar tick mark below/above tick text
     *
     * @param tickTextArray the length should same as tick count
     * @return Builder
     */
    public Builder tickTextArray(String[] tickTextArray) {
        this.tickTextCustomArray = tickTextArray;
        return this;
    }


    /**
     * To replace the SeekBar tick mark below/above tick text
     *
     * @param tickTextArray the length should same as tick count
     * @return Builder
     */
    public Builder tickTextArray(@ArrayRes int tickTextArray) {
        this.tickTextCustomArray = context.getResources().getStringArray(tickTextArray);
        return this;
    }

    /**
     * Set the tick/thumb text typeface
     *
     * @param tickTextTypeFace the text typeface
     * @return Builder
     */
    public Builder tickTextTypeFace(Typeface tickTextTypeFace) {
        this.tickTextTypeFace = tickTextTypeFace;
        return this;
    }

    /**
     * Set the tick mark number
     *
     * @param tickCount the tick mark count show on SeekBar
     * @return Builder
     */
    public Builder tickCount(int tickCount) {
        this.tickCount = tickCount;
        return this;
    }

    /**
     * To show different tick mark shape
     *
     * @param tickMarkType see{@link TickMarkType}
     *                     TickMarkType.NONE;
     *                     TickMarkType.OVAL;
     *                     TickMarkType.SQUARE;
     *                     TickMarkType.DIVIDER;
     * @return Builder
     */
    public Builder showTickMarkType(int tickMarkType) {
        this.showTickMarkType = tickMarkType;
        return this;
    }

    /**
     * Set the SeekBar tick color
     *
     * @param tickMarkColor colorInt
     * @return Builder
     */
    public Builder tickMarkColor(@ColorInt int tickMarkColor) {
        this.tickMarkColor = tickMarkColor;
        return this;
    }

    /**
     * Set the SeekBar tick color
     *
     * @param tickMarkColorStateList colorInt
     * @return Builder
     */
    public Builder tickMarkColor(@NonNull ColorStateList tickMarkColorStateList) {
        this.tickMarkColorStateList = tickMarkColorStateList;
        return this;
    }

    /**
     * Set the SeekBar tick width. If tick type is divider, this method won't work (divider has a regular value 2dp)
     *
     * @param tickMarkSize the dp size
     * @return Builder
     */
    public Builder tickMarkSize(int tickMarkSize) {
        this.tickMarkSize = SizeUtils.dp2px(context, tickMarkSize);
        return this;
    }

    /**
     * To custom the tick showing drawable
     *
     * @param tickMarkDrawable the drawable show as tick mark
     * @return Builder
     */
    public Builder tickMarkDrawable(@NonNull Drawable tickMarkDrawable) {
        this.tickMarkDrawable = tickMarkDrawable;
        return this;
    }

    /**
     * To custom the tick showing drawable by selector
     *
     * @param tickMarkStateListDrawable the StateListDrawable show as tick mark
     * @return Builder
     */
    public Builder tickMarkDrawable(@NonNull StateListDrawable tickMarkStateListDrawable) {
        this.tickMarkDrawable = tickMarkStateListDrawable;
        return this;
    }

    /**
     * To hide the tick mark which show in the both end sides of SeekBar
     *
     * @param tickMarkEndsHide true for hide
     * @return Builder
     */
    public Builder tickMarkEndsHide(boolean tickMarkEndsHide) {
        this.tickMarkEndsHide = tickMarkEndsHide;
        return this;
    }

    /**
     * To hide the tick mark on SeekBar thumb left
     *
     * @param tickMarkSweptHide true for hide
     * @return Builder
     */
    public Builder tickMarkSweptHide(boolean tickMarkSweptHide) {
        this.tickMarkSweptHide = tickMarkSweptHide;
        return this;
    }
}