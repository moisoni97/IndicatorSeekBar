package com.warkiz.indicatorseekbar.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warkiz.indicatorseekbar.R;

public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);
        TextView textView = root.findViewById(R.id.source_code);

        if (textView != null) {
            textView.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/moisoni97/IndicatorSeekBar"))));
        }

        initView(root);
        return root;
    }

    protected abstract int getLayoutId();

    protected void initView(View root) {
    }
}
