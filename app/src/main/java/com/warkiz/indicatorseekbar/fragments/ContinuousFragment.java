package com.warkiz.indicatorseekbar.fragments;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.warkiz.indicatorseekbar.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class ContinuousFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.continuous;
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(View root) {
        IndicatorSeekBar percent_indicator = root.findViewById(R.id.percent_indicator);
        percent_indicator.setIndicatorTextFormat("${PROGRESS} %");

        IndicatorSeekBar scale = root.findViewById(R.id.scale);
        scale.setDecimalScale(4);

        IndicatorSeekBar thumb_drawable = root.findViewById(R.id.thumb_drawable);
        thumb_drawable.setThumbDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null));

        IndicatorSeekBar listenerSeekBar = root.findViewById(R.id.listener);
        final TextView states = root.findViewById(R.id.states);
        states.setText("states: ");

        final TextView progress = root.findViewById(R.id.progress);
        progress.setText("progress: " + listenerSeekBar.getProgress());

        final TextView progress_float = root.findViewById(R.id.progress_float);
        progress_float.setText("progress_float: " + listenerSeekBar.getProgressFloat());

        final TextView from_user = root.findViewById(R.id.from_user);
        from_user.setText("from_user: ");

        listenerSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                states.setText("states: onSeeking");
                progress.setText("progress: " + seekParams.progress);
                progress_float.setText("progress_float: " + seekParams.progressFloat);
                from_user.setText("from_user: " + seekParams.fromUser);
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
