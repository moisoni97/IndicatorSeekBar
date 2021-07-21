package com.warkiz.indicatorseekbar.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.warkiz.indicatorseekbar.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorStayLayout;
import com.warkiz.widget.IndicatorType;
import com.warkiz.widget.TickMarkType;

import java.util.Objects;

public class JavaBuildFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.java_build;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView(View root) {
        super.initView(root);
        final LinearLayout content = root.findViewById(R.id.java_build);

        TextView textView1 = getTextView();
        textView1.setText("1. continuous");
        content.addView(textView1);

        IndicatorSeekBar continuous = IndicatorSeekBar
                .with(requireContext())
                .max(200)
                .min(10)
                .progress(33)
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .showIndicatorType(IndicatorType.CIRCULAR_BUBBLE)
                .thumbColorStateList(getResources().getColorStateList(R.color.selector_thumb_color, null))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .showThumbText(true)
                .thumbTextColor(getResources().getColor(R.color.color_gray, null))
                .build();

        content.addView(continuous);

        TextView textView2 = getTextView();
        textView2.setText("2. continuous_text_ends");
        content.addView(textView2);

        IndicatorSeekBar continuous2TickText = IndicatorSeekBar
                .with(requireContext())
                .max(100)
                .min(10)
                .progress(33)
                .tickCount(2)
                .showTickMarkType(TickMarkType.NONE)
                .showTickText(true)
                .indicatorColor(getResources().getColor(R.color.color_gray, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .showIndicatorType(IndicatorType.RECTANGLE)
                .thumbDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_thumb_drawable, null)))
                .thumbSize(18)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .build();

        IndicatorStayLayout continuousStayLayout = new IndicatorStayLayout(getContext());
        continuousStayLayout.attachTo(continuous2TickText);
        content.addView(continuousStayLayout);

        TextView textView22 = getTextView();
        textView22.setText("3. continuous_text_ends_custom_ripple_thumb");
        content.addView(textView22);

        IndicatorSeekBar continuous_text_ends_custom_thumb = IndicatorSeekBar
                .with(requireContext())
                .max(100)
                .min(30)
                .progress(33)
                .tickCount(2)
                .showTickMarkType(TickMarkType.NONE)
                .showTickText(true)
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .showIndicatorType(IndicatorType.CIRCULAR_BUBBLE)
                .thumbDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_thumb_ripple_drawable, null)))
                .thumbSize(26)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .build();
        content.addView(continuous_text_ends_custom_thumb);

        TextView textView3 = getTextView();
        textView3.setText("4. continuous_text_ends_custom");
        content.addView(textView3);
        IndicatorSeekBar continuous2TickText1 = IndicatorSeekBar
                .with(requireContext())
                .max(90)
                .min(10)
                .progress(33)
                .tickCount(2)
                .showTickMarkType(TickMarkType.NONE)
                .showTickText(true)
                .tickTextArray(R.array.last_next_length_2)
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .showIndicatorType(IndicatorType.CIRCULAR_BUBBLE)
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .build();
        content.addView(continuous2TickText1);

        TextView textView4 = getTextView();
        textView4.setText("5. discrete_tick");
        content.addView(textView4);

        IndicatorSeekBar discrete_tick = IndicatorSeekBar
                .with(requireContext())
                .max(50)
                .min(10)
                .progress(33)
                .tickCount(7)
                .tickMarkSize(15)
                .tickMarkDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_tick_mark_drawable, null)))
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .showIndicatorType(IndicatorType.ROUNDED_RECTANGLE)
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .build();
        IndicatorStayLayout indicatorStayLayout = new IndicatorStayLayout(getContext());
        indicatorStayLayout.attachTo(discrete_tick);
        content.addView(indicatorStayLayout);

        TextView textView5 = getTextView();
        textView5.setText("6. discrete_tick_text");
        content.addView(textView5);

        IndicatorSeekBar discrete_tick_text = IndicatorSeekBar
                .with(requireContext())
                .max(110)
                .min(10)
                .progress(53)
                .tickCount(7)
                .showTickMarkType(TickMarkType.OVAL)
                .tickMarkColor(getResources().getColor(R.color.color_blue, null))
                .tickMarkSize(13)
                .showTickText(true)
                .tickTextColor(getResources().getColor(R.color.color_pink, null))
                .tickTextSize(13)
                .tickTextTypeFace(Typeface.MONOSPACE)
                .showIndicatorType(IndicatorType.ROUNDED_RECTANGLE)
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .indicatorTextSize(13)
                .thumbColor(getResources().getColor(R.color.colorAccent, null))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_tick_text);

        TextView textView6 = getTextView();
        textView6.setText("7. discrete_tick_text_custom");
        content.addView(textView6);
        String[] array = {"A", "B", "C", "D", "E", "F", "G"};
        IndicatorSeekBar discrete_tick_text1 = IndicatorSeekBar
                .with(requireContext())
                .max(200)
                .min(10)
                .progress(83)
                .tickCount(7)
                .showTickMarkType(TickMarkType.SQUARE)
                .tickTextArray(array)
                .showTickText(true)
                .tickTextColorStateList(getResources().getColorStateList(R.color.selector_tick_text_3_color, null))
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .showIndicatorType(IndicatorType.RECTANGLE)
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray, null))
                .trackBackgroundSize(2)
                .build();
        content.addView(discrete_tick_text1);

        TextView textView7 = getTextView();
        textView7.setText("8. discrete_tick_text_ends");
        content.addView(textView7);

        String[] array_ends = {"A", "", "", "", "", "", "G"};
        IndicatorSeekBar discrete_tick_text_ends = IndicatorSeekBar
                .with(requireContext())
                .max(100)
                .min(10)
                .progress(83)
                .tickCount(7)
                .showTickMarkType(TickMarkType.OVAL)
                .tickMarkColor(getResources().getColorStateList(R.color.selector_tick_mark_color, null))
                .tickTextArray(array_ends)
                .showTickText(true)
                .showIndicatorType(IndicatorType.CIRCULAR_BUBBLE)
                .tickTextColorStateList(getResources().getColorStateList(R.color.selector_tick_text_3_color, null))
                .indicatorColor(getResources().getColor(R.color.color_blue, null))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .thumbColor(Color.parseColor("#ff0000"))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.color_blue, null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_pink, null))
                .trackBackgroundSize(2)
                .build();

        IndicatorStayLayout stayLayout = new IndicatorStayLayout(getContext());
        stayLayout.attachTo(discrete_tick_text_ends);
        content.addView(stayLayout);

        TextView textView8 = getTextView();
        content.addView(textView8);
    }

    @NonNull
    private TextView getTextView() {
        TextView textView = new TextView(getContext());
        int padding = dp2px(requireContext(), 16);
        textView.setPadding(padding, padding, padding, 0);
        return textView;
    }

    public int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}
