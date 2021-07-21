package com.warkiz.widget;

public class SeekParams {

    SeekParams(IndicatorSeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public IndicatorSeekBar seekBar;

    public int progress;

    public float progressFloat;

    public boolean fromUser;

    public int thumbPosition;

    public String tickText;
}
