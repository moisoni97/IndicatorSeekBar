package com.warkiz.widget;

import androidx.annotation.ColorInt;

public interface ColorCollector {
    /**
     * To collect each section track color
     *
     * @param colorIntArr is the container for each section track color.
     *                    This array length will auto equals section track count
     */
    boolean collectSectionTrackColor(@ColorInt int[] colorIntArr);
}