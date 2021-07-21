package com.warkiz.widget;

public interface IndicatorType {
    /**
     * Indicator is GONE
     */
    int NONE = 0;
    /**
     * Indicator shape like water-drop
     */
    int CIRCULAR_BUBBLE = 1;

    /**
     * Indicator corners are rounded shape
     */
    int ROUNDED_RECTANGLE = 2;

    /**
     * Indicator corners are square shape
     */
    int RECTANGLE = 3;

    /**
     * Indicator with custom shape
     */
    int CUSTOM = 4;

}