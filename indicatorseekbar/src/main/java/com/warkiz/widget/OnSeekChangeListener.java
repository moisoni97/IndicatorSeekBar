package com.warkiz.widget;

public interface OnSeekChangeListener {

    /**
     * Callback when progress level has changed
     *
     * @param seekParams info about the SeekBar
     */
    void onSeeking(SeekParams seekParams);

    /**
     * Callback when user has started a touch gesture
     *
     * @param seekBar the SeekBar in which the touch gesture begin
     */
    void onStartTrackingTouch(IndicatorSeekBar seekBar);

    /**
     * Callback when the user has finished a touch gesture
     *
     * @param seekBar the SeekBar in which the touch gesture cease
     */
    void onStopTrackingTouch(IndicatorSeekBar seekBar);
}