package com.warkiz.indicatorseekbar.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.warkiz.indicatorseekbar.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class DiscreteFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.discrete;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(View root) {

        IndicatorSeekBar seekBar = root.findViewById(R.id.custom_text);
        String[] arr = {"A", "a", "B", "b", "C", "c", "D"};
        seekBar.customTickText(arr);

        IndicatorSeekBar tick_drawable = root.findViewById(R.id.tick_drawable);
        tick_drawable.setTickMarkDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));

        IndicatorSeekBar sectionSeekBar = root.findViewById(R.id.custom_section_color);

        sectionSeekBar.customSectionTrackColor(colorIntArr -> {
            colorIntArr[0] = getResources().getColor(R.color.color_blue, null);
            colorIntArr[1] = getResources().getColor(R.color.color_gray, null);
            colorIntArr[2] = Color.parseColor("#FF4081");
            colorIntArr[3] = Color.parseColor("#303F9F");
            return true;
        });


        IndicatorSeekBar listenerSeekBar = root.findViewById(R.id.listener);

        final TextView states = root.findViewById(R.id.states);
        states.setText("states: ");

        final TextView progress = root.findViewById(R.id.progress);
        progress.setText("progress: " + listenerSeekBar.getProgress());

        final TextView progress_float = root.findViewById(R.id.progress_float);
        progress_float.setText("progress_float: " + listenerSeekBar.getProgressFloat());

        final TextView from_user = root.findViewById(R.id.from_user);
        from_user.setText("from_user: ");

        final TextView thumb_position = root.findViewById(R.id.thumb_position);
        thumb_position.setText("thumb_position: ");

        final TextView tick_text = root.findViewById(R.id.tick_text);
        tick_text.setText("tick_text: ");

        listenerSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                states.setText("states: onSeeking");
                progress.setText("progress: " + seekParams.progress);
                progress_float.setText("progress_float: " + seekParams.progressFloat);
                from_user.setText("from_user: " + seekParams.fromUser);
                thumb_position.setText("thumb_position: " + seekParams.thumbPosition);
                tick_text.setText("tick_text: " + seekParams.tickText);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                states.setText("states: onStart");
                progress.setText("progress: " + seekBar.getProgress());
                progress_float.setText("progress_float: " + seekBar.getProgressFloat());
                from_user.setText("from_user: true");
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                states.setText("states: onStop");
                progress.setText("progress: " + seekBar.getProgress());
                progress_float.setText("progress_float: " + seekBar.getProgressFloat());
                from_user.setText("from_user: false");
            }
        });
    }
}
