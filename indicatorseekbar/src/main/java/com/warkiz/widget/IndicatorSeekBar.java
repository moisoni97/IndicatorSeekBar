package com.warkiz.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;

public class IndicatorSeekBar extends View {

    private static final int THUMB_MAX_WIDTH = 30;
    private static final String FORMAT_PROGRESS = "${PROGRESS}";
    private static final String FORMAT_TICK_TEXT = "${TICK_TEXT}";
    private static final String FORMAT_EMPTY_TEXT = "${EMPTY_TEXT}";

    private final Context mContext;

    private Paint mStockPaint;
    private TextPaint mTextPaint;
    private OnSeekChangeListener mSeekChangeListener;
    private Rect mRect;
    private float mCustomDrawableMaxHeight;
    private float lastProgress;
    private float mFaultTolerance = -1;
    private float mScreenWidth = -1;
    private boolean mClearPadding;
    private SeekParams mSeekParams;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mMeasuredWidth;
    private int mPaddingTop;
    private float mSeekLength;
    private float mSeekBlockLength;
    private boolean mIsTouching;
    private float mMax;
    private float mMin;
    private float mProgress;
    private boolean mIsFloatProgress;
    private int mScale = 1;
    private boolean mUserSeekable;
    private boolean mOnlyThumbDraggable;
    private boolean mSeekSmoothly;
    private float[] mProgressArr;
    private boolean mR2L;

    private boolean mShowTickText;
    private boolean mShowBothTickTextOnly;
    private int mTickTextHeight;
    private String[] mTickTextArr;
    private float[] mTickTextWidth;
    private float[] mTextCenterX;
    private float mTickTextY;
    private int mTickTextSize;
    private Typeface mTextTypeface;
    private int mSelectedTextColor;
    private int mUnselectedTextColor;
    private int mHoveredTextColor;
    private CharSequence[] mTickTextCustomArray;

    private Indicator mIndicator;
    private int mIndicatorColor;
    private int mIndicatorTextColor;
    private boolean mIndicatorStayAlways;
    private int mIndicatorTextSize;
    private View mIndicatorContentView;
    private View mIndicatorTopContentView;
    private int mShowIndicatorType;
    private String mIndicatorTextFormat;

    private float[] mTickMarkX;
    private int mTickCount;
    private int mUnSelectedTickMarkColor;
    private int mSelectedTickMarkColor;
    private float mTickRadius;
    private Bitmap mUnselectTickMarkBitmap;
    private Bitmap mSelectTickMarkBitmap;
    private Drawable mTickMarkDrawable;
    private int mShowTickMarkType;
    private boolean mTickMarkEndsHide;
    private boolean mTickMarkSweptHide;
    private int mTickMarkSize;

    private boolean mTrackRoundedCorners;
    private RectF mProgressTrack;
    private RectF mBackgroundTrack;
    private int mBackgroundTrackSize;
    private int mProgressTrackSize;
    private int mBackgroundTrackColor;
    private int mProgressTrackColor;
    private int[] mSectionTrackColorArray;
    private boolean mCustomTrackSectionColorResult;

    private float mThumbRadius;
    private float mThumbTouchRadius;
    private Bitmap mThumbBitmap;
    private int mThumbColor;
    private int mThumbSize;
    private Drawable mThumbDrawable;
    private Bitmap mPressedThumbBitmap;
    private int mPressedThumbColor;

    private boolean mShowThumbText;
    private float mThumbTextY;
    private int mThumbTextColor;
    private boolean mHideThumb;
    private boolean mAdjustAuto;

    public IndicatorSeekBar(Context context) {
        this(context, null);
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initAttrs(mContext, attrs);
        initParams();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IndicatorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initAttrs(mContext, attrs);
        initParams();
    }

    IndicatorSeekBar(Builder builder) {
        super(builder.context);
        this.mContext = builder.context;
        int defaultPadding = SizeUtils.dp2px(mContext, 16);
        setPadding(defaultPadding, getPaddingTop(), defaultPadding, getPaddingBottom());
        this.apply(builder);

        initParams();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Builder builder = new Builder(context);

        if (attrs == null) {
            this.apply(builder);
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorSeekBar);

        mMax = ta.getFloat(R.styleable.IndicatorSeekBar_isb_max, builder.max);
        mMin = ta.getFloat(R.styleable.IndicatorSeekBar_isb_min, builder.min);
        mProgress = ta.getFloat(R.styleable.IndicatorSeekBar_isb_progress, builder.progress);
        mIsFloatProgress = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_progress_value_float, builder.progressValueFloat);
        mUserSeekable = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_user_seekable, builder.userSeekable);
        mClearPadding = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_clear_default_padding, builder.clearPadding);
        mOnlyThumbDraggable = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_only_thumb_draggable, builder.onlyThumbDraggable);
        mSeekSmoothly = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_seek_smoothly, builder.seekSmoothly);
        mR2L = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_r2l, builder.r2l);

        mBackgroundTrackSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_track_background_size, builder.trackBackgroundSize);
        mProgressTrackSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_track_progress_size, builder.trackProgressSize);
        mBackgroundTrackColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_track_background_color, builder.trackBackgroundColor);
        mProgressTrackColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_track_progress_color, builder.trackProgressColor);
        mTrackRoundedCorners = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_track_rounded_corners, builder.trackRoundedCorners);

        mThumbSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_thumb_size, builder.thumbSize);
        mThumbDrawable = ta.getDrawable(R.styleable.IndicatorSeekBar_isb_thumb_drawable);
        mAdjustAuto = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_thumb_adjust_auto, true);
        initThumbColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_isb_thumb_color), builder.thumbColor);

        mShowThumbText = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_show_thumb_text, builder.showThumbText);
        mThumbTextColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_thumb_text_color, builder.thumbTextColor);

        mTickCount = ta.getInt(R.styleable.IndicatorSeekBar_isb_tick_count, builder.tickCount);
        mShowTickMarkType = ta.getInt(R.styleable.IndicatorSeekBar_isb_show_tick_mark_type, builder.showTickMarkType);
        mTickMarkSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_tick_mark_size, builder.tickMarkSize);
        initTickMarkColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_isb_tick_mark_color), builder.tickMarkColor);
        mTickMarkDrawable = ta.getDrawable(R.styleable.IndicatorSeekBar_isb_tick_mark_drawable);
        mTickMarkSweptHide = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_tick_mark_swept_hide, builder.tickMarkSweptHide);
        mTickMarkEndsHide = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_tick_mark_ends_hide, builder.tickMarkEndsHide);

        mShowTickText = ta.getBoolean(R.styleable.IndicatorSeekBar_isb_show_tick_text, builder.showTickText);
        mTickTextSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_tick_text_size, builder.tickTextSize);
        initTickTextColor(ta.getColorStateList(R.styleable.IndicatorSeekBar_isb_tick_text_color), builder.tickTextColor);
        mTickTextCustomArray = ta.getTextArray(R.styleable.IndicatorSeekBar_isb_tick_text_array);
        initTextTypeface(ta.getInt(R.styleable.IndicatorSeekBar_isb_tick_text_typeface, -1), builder.tickTextTypeFace);

        mShowIndicatorType = ta.getInt(R.styleable.IndicatorSeekBar_isb_show_indicator, builder.showIndicatorType);
        mIndicatorColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_indicator_color, builder.indicatorColor);
        mIndicatorTextSize = ta.getDimensionPixelSize(R.styleable.IndicatorSeekBar_isb_indicator_text_size, builder.indicatorTextSize);
        mIndicatorTextColor = ta.getColor(R.styleable.IndicatorSeekBar_isb_indicator_text_color, builder.indicatorTextColor);

        int indicatorContentViewId = ta.getResourceId(R.styleable.IndicatorSeekBar_isb_indicator_content_layout, 0);

        if (indicatorContentViewId > 0) {
            mIndicatorContentView = View.inflate(mContext, indicatorContentViewId, null);
        }

        int indicatorTopContentLayoutId = ta.getResourceId(R.styleable.IndicatorSeekBar_isb_indicator_top_content_layout, 0);

        if (indicatorTopContentLayoutId > 0) {
            mIndicatorTopContentView = View.inflate(mContext, indicatorTopContentLayoutId, null);
        }

        ta.recycle();
    }

    private void initParams() {
        initProgressRangeValue();

        if (mBackgroundTrackSize > mProgressTrackSize) {
            mBackgroundTrackSize = mProgressTrackSize;
        }

        if (mThumbDrawable == null) {
            mThumbRadius = mThumbSize / 2.0f;
            mThumbTouchRadius = mThumbRadius * 1.2f;
        } else {
            mThumbRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mThumbSize) / 2.0f;
            mThumbTouchRadius = mThumbRadius;
        }

        if (mTickMarkDrawable == null) {
            mTickRadius = mTickMarkSize / 2.0f;
        } else {
            mTickRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mTickMarkSize) / 2.0f;
        }

        mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
        initStrokePaint();
        measureTickTextBonds();
        lastProgress = mProgress;

        collectTickInfo();

        mProgressTrack = new RectF();
        mBackgroundTrack = new RectF();

        initDefaultPadding();
        initIndicatorContentView();
    }

    private void collectTickInfo() {
        if (mTickCount < 0 || mTickCount > 50) {
            throw new IllegalArgumentException("The argument: tick count must be limited between (0-50). Currently is: " + mTickCount);
        }

        if (mTickCount != 0) {
            mTickMarkX = new float[mTickCount];
            if (mShowTickText) {
                mTextCenterX = new float[mTickCount];
                mTickTextWidth = new float[mTickCount];
            }

            mProgressArr = new float[mTickCount];
            for (int i = 0; i < mProgressArr.length; i++) {
                mProgressArr[i] = mMin + i * (mMax - mMin) / ((mTickCount - 1) > 0 ? (mTickCount - 1) : 1);
            }

        }
    }

    private void initDefaultPadding() {
        if (!mClearPadding) {
            int normalPadding = SizeUtils.dp2px(mContext, 16);
            if (getPaddingLeft() == 0) {
                setPadding(normalPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
            if (getPaddingRight() == 0) {
                setPadding(getPaddingLeft(), getPaddingTop(), normalPadding, getPaddingBottom());
            }
        }
    }

    private void initProgressRangeValue() {
        if (mMax < mMin) {
            throw new IllegalArgumentException("The argument: MAX value must be larger than MIN value");
        }

        if (mProgress < mMin) {
            mProgress = mMin;
        }

        if (mProgress > mMax) {
            mProgress = mMax;
        }
    }

    private void initStrokePaint() {
        if (mStockPaint == null) {
            mStockPaint = new Paint();
        }

        if (mTrackRoundedCorners) {
            mStockPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        mStockPaint.setAntiAlias(true);

        if (mBackgroundTrackSize > mProgressTrackSize) {
            mProgressTrackSize = mBackgroundTrackSize;
        }
    }

    private void measureTickTextBonds() {
        if (needDrawText()) {
            initTextPaint();
            mTextPaint.setTypeface(mTextTypeface);
            mTextPaint.getTextBounds("j", 0, 1, mRect);
            mTickTextHeight = mRect.height() + SizeUtils.dp2px(mContext, 3);
        }
    }

    private boolean needDrawText() {
        return mShowThumbText || (mTickCount != 0 && mShowTickText);
    }

    private void initTextPaint() {
        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(mTickTextSize);
        }

        if (mRect == null) {
            mRect = new Rect();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = Math.round(mCustomDrawableMaxHeight + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(resolveSize(SizeUtils.dp2px(mContext, 170), widthMeasureSpec), height + mTickTextHeight);

        initSeekBarInfo();
        refreshSeekBarLocation();
    }

    private void initSeekBarInfo() {
        mMeasuredWidth = getMeasuredWidth();
        mPaddingLeft = getPaddingStart();
        mPaddingRight = getPaddingEnd();
        mPaddingTop = getPaddingTop();
        mSeekLength = mMeasuredWidth - mPaddingLeft - mPaddingRight;
        mSeekBlockLength = mSeekLength / (mTickCount - 1 > 0 ? mTickCount - 1 : 1);
    }

    private void refreshSeekBarLocation() {
        initTrackLocation();

        if (needDrawText()) {
            mTextPaint.getTextBounds("j", 0, 1, mRect);
            mTickTextY = mPaddingTop + mCustomDrawableMaxHeight + Math.round(mRect.height() - mTextPaint.descent()) + SizeUtils.dp2px(mContext, 3);
            mThumbTextY = mTickTextY;
        }

        if (mTickMarkX == null) {
            return;
        }

        initTextArray();

        if ((!mSeekSmoothly) && mTickCount > 2) {
            mProgress = mProgressArr[getClosestIndex()];
            lastProgress = mProgress;
        }

        refreshThumbCenterXByProgress(mProgress);
    }

    private void initTextArray() {
        if (mTickCount == 0) {
            return;
        }

        if (mShowTickText) {
            mTickTextArr = new String[mTickCount];
        }

        for (int i = 0; i < mTickMarkX.length; i++) {
            if (mShowTickText) {
                mTickTextArr[i] = getTickTextByPosition(i);
                mTextPaint.getTextBounds(mTickTextArr[i], 0, mTickTextArr[i].length(), mRect);
                mTickTextWidth[i] = mRect.width();
                mTextCenterX[i] = mPaddingLeft + mSeekBlockLength * i;
            }
            mTickMarkX[i] = mPaddingLeft + mSeekBlockLength * i;
        }
    }

    private void initTrackLocation() {
        if (mR2L) {
            mBackgroundTrack.left = mPaddingLeft;
            mBackgroundTrack.top = mPaddingTop + mThumbTouchRadius;

            mBackgroundTrack.right = mPaddingLeft + mSeekLength * (1.0f - (mProgress - mMin) / (getAmplitude()));
            mBackgroundTrack.bottom = mBackgroundTrack.top;

            mProgressTrack.left = mBackgroundTrack.right;
            mProgressTrack.top = mBackgroundTrack.top;
            mProgressTrack.right = mMeasuredWidth - mPaddingRight;
            mProgressTrack.bottom = mBackgroundTrack.bottom;
        } else {
            mProgressTrack.left = mPaddingLeft;
            mProgressTrack.top = mPaddingTop + mThumbTouchRadius;

            mProgressTrack.right = (mProgress - mMin) * mSeekLength / (getAmplitude()) + mPaddingLeft;
            mProgressTrack.bottom = mProgressTrack.top;

            mBackgroundTrack.left = mProgressTrack.right;
            mBackgroundTrack.top = mProgressTrack.bottom;
            mBackgroundTrack.right = mMeasuredWidth - mPaddingRight;
            mBackgroundTrack.bottom = mProgressTrack.bottom;
        }
    }

    private String getTickTextByPosition(int index) {
        if (mTickTextCustomArray == null) {
            return getProgressString(mProgressArr[index]);
        }

        if (index < mTickTextCustomArray.length) {
            return String.valueOf(mTickTextCustomArray[index]);
        }

        return "";
    }

    /**
     * Calculate the thumb centerX by the changing progress
     */
    private void refreshThumbCenterXByProgress(float progress) {
        if (mR2L) {
            mBackgroundTrack.right = mPaddingLeft + mSeekLength * (1.0f - (progress - mMin) / (getAmplitude()));//ThumbCenterX
            mProgressTrack.left = mBackgroundTrack.right;
        } else {
            mProgressTrack.right = (progress - mMin) * mSeekLength / (getAmplitude()) + mPaddingLeft;
            mBackgroundTrack.left = mProgressTrack.right;
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawTrack(canvas);
        drawTickMark(canvas);
        drawTickText(canvas);
        drawThumb(canvas);
        drawThumbText(canvas);
    }

    private void drawTrack(Canvas canvas) {
        if (mCustomTrackSectionColorResult) {
            int sectionSize = mTickCount - 1 > 0 ? mTickCount - 1 : 1;
            for (int i = 0; i < sectionSize; i++) {
                if (mR2L) {
                    mStockPaint.setColor(mSectionTrackColorArray[sectionSize - i - 1]);
                } else {
                    mStockPaint.setColor(mSectionTrackColorArray[i]);
                }

                float thumbPosFloat = getThumbPosOnTickFloat();
                if (i < thumbPosFloat && thumbPosFloat < (i + 1)) {
                    float thumbCenterX = getThumbCenterX();
                    mStockPaint.setStrokeWidth(getLeftSideTrackSize());
                    canvas.drawLine(mTickMarkX[i], mProgressTrack.top, thumbCenterX, mProgressTrack.bottom, mStockPaint);
                    mStockPaint.setStrokeWidth(getRightSideTrackSize());
                    canvas.drawLine(thumbCenterX, mProgressTrack.top, mTickMarkX[i + 1], mProgressTrack.bottom, mStockPaint);
                } else {
                    if (i < thumbPosFloat) {
                        mStockPaint.setStrokeWidth(getLeftSideTrackSize());
                    } else {
                        mStockPaint.setStrokeWidth(getRightSideTrackSize());
                    }
                    canvas.drawLine(mTickMarkX[i], mProgressTrack.top, mTickMarkX[i + 1], mProgressTrack.bottom, mStockPaint);
                }
            }
        } else {
            mStockPaint.setColor(mProgressTrackColor);
            mStockPaint.setStrokeWidth(mProgressTrackSize);
            canvas.drawLine(mProgressTrack.left, mProgressTrack.top, mProgressTrack.right, mProgressTrack.bottom, mStockPaint);

            mStockPaint.setColor(mBackgroundTrackColor);
            mStockPaint.setStrokeWidth(mBackgroundTrackSize);
            canvas.drawLine(mBackgroundTrack.left, mBackgroundTrack.top, mBackgroundTrack.right, mBackgroundTrack.bottom, mStockPaint);
        }
    }

    private void drawTickMark(Canvas canvas) {
        if (mTickCount == 0 || (mShowTickMarkType == TickMarkType.NONE && mTickMarkDrawable == null)) {
            return;
        }

        float thumbCenterX = getThumbCenterX();
        for (int i = 0; i < mTickMarkX.length; i++) {
            float thumbPosFloat = getThumbPosOnTickFloat();
            if (mTickMarkSweptHide) {
                if (thumbCenterX >= mTickMarkX[i]) {
                    continue;
                }
            }

            if (mTickMarkEndsHide) {
                if (i == 0 || i == mTickMarkX.length - 1) {
                    continue;
                }
            }

            if (i == getThumbPosOnTick() && mTickCount > 2 && !mSeekSmoothly) {
                continue;
            }

            if (i <= thumbPosFloat) {
                mStockPaint.setColor(getLeftSideTickColor());
            } else {
                mStockPaint.setColor(getRightSideTickColor());
            }

            if (mTickMarkDrawable != null) {
                if (mSelectTickMarkBitmap == null || mUnselectTickMarkBitmap == null) {
                    initTickMarkBitmap();
                }
                if (mSelectTickMarkBitmap == null || mUnselectTickMarkBitmap == null) {
                    throw new IllegalArgumentException("The format of the selector tick mark drawable is wrong");
                }
                if (i <= thumbPosFloat) {
                    canvas.drawBitmap(mSelectTickMarkBitmap, mTickMarkX[i] - mUnselectTickMarkBitmap.getWidth() / 2.0f, mProgressTrack.top - mUnselectTickMarkBitmap.getHeight() / 2.0f, mStockPaint);
                } else {
                    canvas.drawBitmap(mUnselectTickMarkBitmap, mTickMarkX[i] - mUnselectTickMarkBitmap.getWidth() / 2.0f, mProgressTrack.top - mUnselectTickMarkBitmap.getHeight() / 2.0f, mStockPaint);
                }
                continue;
            }

            if (mShowTickMarkType == TickMarkType.OVAL) {
                canvas.drawCircle(mTickMarkX[i], mProgressTrack.top, mTickRadius, mStockPaint);
            } else if (mShowTickMarkType == TickMarkType.DIVIDER) {
                int rectWidth = SizeUtils.dp2px(mContext, 1);
                float dividerTickHeight;
                if (thumbCenterX >= mTickMarkX[i]) {
                    dividerTickHeight = getLeftSideTrackSize();
                } else {
                    dividerTickHeight = getRightSideTrackSize();
                }
                canvas.drawRect(mTickMarkX[i] - rectWidth, mProgressTrack.top - dividerTickHeight / 2.0f, mTickMarkX[i] + rectWidth, mProgressTrack.top + dividerTickHeight / 2.0f, mStockPaint);
            } else if (mShowTickMarkType == TickMarkType.SQUARE) {
                canvas.drawRect(mTickMarkX[i] - mTickMarkSize / 2.0f, mProgressTrack.top - mTickMarkSize / 2.0f, mTickMarkX[i] + mTickMarkSize / 2.0f, mProgressTrack.top + mTickMarkSize / 2.0f, mStockPaint);
            }
        }
    }

    private void drawTickText(Canvas canvas) {
        if (mTickTextArr == null) {
            return;
        }

        float thumbPosFloat = getThumbPosOnTickFloat();
        for (int i = 0; i < mTickTextArr.length; i++) {
            if (mShowBothTickTextOnly) {
                if (i != 0 && i != mTickTextArr.length - 1) {
                    continue;
                }
            }
            if (i == getThumbPosOnTick() && i == thumbPosFloat) {
                mTextPaint.setColor(mHoveredTextColor);
            } else if (i < thumbPosFloat) {
                mTextPaint.setColor(getLeftSideTickTextColor());
            } else {
                mTextPaint.setColor(getRightSideTickTextColor());
            }

            int index = i;
            if (mR2L) {
                index = mTickTextArr.length - i - 1;
            }
            if (i == 0) {
                canvas.drawText(mTickTextArr[index], mTextCenterX[i] + mTickTextWidth[index] / 2.0f, mTickTextY, mTextPaint);
            } else if (i == mTickTextArr.length - 1) {
                canvas.drawText(mTickTextArr[index], mTextCenterX[i] - mTickTextWidth[index] / 2.0f, mTickTextY, mTextPaint);
            } else {
                canvas.drawText(mTickTextArr[index], mTextCenterX[i], mTickTextY, mTextPaint);
            }
        }
    }

    private void drawThumb(Canvas canvas) {
        if (mHideThumb) {
            return;
        }

        float thumbCenterX = getThumbCenterX();
        if (mThumbDrawable != null) {
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                initThumbBitmap();
            }
            if (mThumbBitmap == null || mPressedThumbBitmap == null) {
                throw new IllegalArgumentException("The format of the selector thumb drawable is wrong");
            }
            mStockPaint.setAlpha(255);
            if (mIsTouching) {
                canvas.drawBitmap(mPressedThumbBitmap, thumbCenterX - mPressedThumbBitmap.getWidth() / 2.0f, mProgressTrack.top - mPressedThumbBitmap.getHeight() / 2.0f, mStockPaint);
            } else {
                canvas.drawBitmap(mThumbBitmap, thumbCenterX - mThumbBitmap.getWidth() / 2.0f, mProgressTrack.top - mThumbBitmap.getHeight() / 2.0f, mStockPaint);
            }
        } else {
            if (mIsTouching) {
                mStockPaint.setColor(mPressedThumbColor);
            } else {
                mStockPaint.setColor(mThumbColor);
            }
            canvas.drawCircle(thumbCenterX, mProgressTrack.top, mIsTouching ? mThumbTouchRadius : mThumbRadius, mStockPaint);
        }
    }

    private void drawThumbText(Canvas canvas) {
        if (!mShowThumbText || (mShowTickText && mTickCount > 2)) {
            return;
        }

        mTextPaint.setColor(mThumbTextColor);
        canvas.drawText(getProgressString(mProgress), getThumbCenterX(), mThumbTextY, mTextPaint);
    }

    private float getThumbCenterX() {
        if (mR2L) {
            return mBackgroundTrack.right;
        }
        return mProgressTrack.right;
    }

    private int getLeftSideTickColor() {
        if (mR2L) {
            return mUnSelectedTickMarkColor;
        }
        return mSelectedTickMarkColor;
    }

    private int getRightSideTickColor() {
        if (mR2L) {
            return mSelectedTickMarkColor;
        }
        return mUnSelectedTickMarkColor;
    }

    private int getLeftSideTickTextColor() {
        if (mR2L) {
            return mUnselectedTextColor;
        }
        return mSelectedTextColor;
    }

    private int getRightSideTickTextColor() {
        if (mR2L) {
            return mSelectedTextColor;
        }
        return mUnselectedTextColor;
    }

    /**
     * Get the track size on the thumb left in R2L/L2R case
     */
    private int getLeftSideTrackSize() {
        if (mR2L) {
            return mBackgroundTrackSize;
        }
        return mProgressTrackSize;
    }

    /**
     * Get the track size on the thumb right in R2L/L2R case
     */
    private int getRightSideTrackSize() {
        if (mR2L) {
            return mProgressTrackSize;
        }
        return mBackgroundTrackSize;
    }

    private int getThumbPosOnTick() {
        if (mTickCount != 0) {
            return Math.round((getThumbCenterX() - mPaddingLeft) / mSeekBlockLength);
        }
        return 0;
    }

    private float getThumbPosOnTickFloat() {
        if (mTickCount != 0) {
            return (getThumbCenterX() - mPaddingLeft) / mSeekBlockLength;
        }
        return 0;
    }

    private int getHeightByRatio(Drawable drawable, int width) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        return Math.round(1.0f * width * intrinsicHeight / intrinsicWidth);
    }

    private Bitmap getDrawBitmap(Drawable drawable, boolean isThumb) {
        if (drawable == null) {
            return null;
        }

        int width;
        int height;
        int maxRange = SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (intrinsicWidth > maxRange) {
            if (isThumb) {
                width = mThumbSize;
            } else {
                width = mTickMarkSize;
            }
            height = getHeightByRatio(drawable, width);

            if (width > maxRange) {
                width = maxRange;
                height = getHeightByRatio(drawable, width);
            }
        } else {
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * To initialize the color for the thumb
     */
    private void initThumbColor(ColorStateList colorStateList, int defaultColor) {
        if (colorStateList == null) {
            mThumbColor = defaultColor;
            mPressedThumbColor = mThumbColor;
            return;
        }

        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parsing thumb selector color");
        }

        if (states.length == 1) {
            mThumbColor = colors[0];
            mPressedThumbColor = mThumbColor;
        } else if (states.length == 2) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {
                    mPressedThumbColor = colors[i];
                    continue;
                }
                if (attr[0] == android.R.attr.state_pressed) {
                    mThumbColor = colors[i];
                } else {
                    throw new IllegalArgumentException("The selector color file you set for the argument: isb_thumb_color is in wrong format");
                }
            }
        } else {
            throw new IllegalArgumentException("The selector color file you set for the argument: isb_thumb_color is in wrong format");
        }

    }

    /**
     * To initialize the color for the tickMasks
     */
    private void initTickMarkColor(ColorStateList colorStateList, int defaultColor) {
        if (colorStateList == null) {
            mSelectedTickMarkColor = defaultColor;
            mUnSelectedTickMarkColor = mSelectedTickMarkColor;
            return;
        }

        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parsing thumb selector color: " + e.getMessage());
        }

        if (states.length == 1) {
            mSelectedTickMarkColor = colors[0];
            mUnSelectedTickMarkColor = mSelectedTickMarkColor;
        } else if (states.length == 2) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {
                    mUnSelectedTickMarkColor = colors[i];
                    continue;
                }
                if (attr[0] == android.R.attr.state_selected) {
                    mSelectedTickMarkColor = colors[i];
                } else {
                    throw new IllegalArgumentException("The selector color file you set for the argument: isb_tick_mark_color is in wrong format");
                }
            }
        } else {
            throw new IllegalArgumentException("The selector color file you set for the argument: isb_tick_mark_color is in wrong format");
        }
    }

    /**
     * To initialize the color for the tick text
     */
    private void initTickTextColor(ColorStateList colorStateList, int defaultColor) {
        if (colorStateList == null) {
            mUnselectedTextColor = defaultColor;
            mSelectedTextColor = mUnselectedTextColor;
            mHoveredTextColor = mUnselectedTextColor;
            return;
        }

        int[][] states = null;
        int[] colors = null;
        Class<? extends ColorStateList> aClass = colorStateList.getClass();
        try {
            Field[] f = aClass.getDeclaredFields();
            for (Field field : f) {
                field.setAccessible(true);
                if ("mStateSpecs".equals(field.getName())) {
                    states = (int[][]) field.get(colorStateList);
                }
                if ("mColors".equals(field.getName())) {
                    colors = (int[]) field.get(colorStateList);
                }
            }
            if (states == null || colors == null) {
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something wrong happened when parsing thumb selector color");
        }

        if (states.length == 1) {
            mUnselectedTextColor = colors[0];
            mSelectedTextColor = mUnselectedTextColor;
            mHoveredTextColor = mUnselectedTextColor;
        } else if (states.length == 3) {
            for (int i = 0; i < states.length; i++) {
                int[] attr = states[i];
                if (attr.length == 0) {
                    mUnselectedTextColor = colors[i];
                    continue;
                }
                switch (attr[0]) {
                    case android.R.attr.state_selected:
                        mSelectedTextColor = colors[i];
                        break;
                    case android.R.attr.state_hovered:
                        mHoveredTextColor = colors[i];
                        break;
                    default:
                        throw new IllegalArgumentException("The selector color file you set for the argument: isb_tick_text_color is in wrong format");
                }
            }
        } else {
            throw new IllegalArgumentException("The selector color file you set for the argument: isb_tick_text_color is in wrong format");
        }
    }

    /**
     * To initialize both the tick text and thumb text typeface, 4 types to choose
     */
    private void initTextTypeface(int typeface, Typeface defaultTypeface) {
        switch (typeface) {
            case 0:
                mTextTypeface = Typeface.DEFAULT;
                break;
            case 1:
                mTextTypeface = Typeface.MONOSPACE;
                break;
            case 2:
                mTextTypeface = Typeface.SANS_SERIF;
                break;
            case 3:
                mTextTypeface = Typeface.SERIF;
                break;
            default:
                if (defaultTypeface == null) {
                    mTextTypeface = Typeface.DEFAULT;
                    break;
                }
                mTextTypeface = defaultTypeface;
                break;
        }
    }

    /**
     * To initialize the bitmap for the thumb
     */
    private void initThumbBitmap() {
        if (mThumbDrawable == null) {
            return;
        }

        if (mThumbDrawable instanceof StateListDrawable) {
            try {
                StateListDrawable listDrawable = (StateListDrawable) mThumbDrawable;

                Class<? extends StateListDrawable> aClass = listDrawable.getClass();
                Method getStateCount = aClass.getMethod("getStateCount");

                String state = String.valueOf(getStateCount.invoke(listDrawable));
                int stateCount = Integer.parseInt(state);

                if (stateCount == 2) {
                    Method getStateSet = aClass.getMethod("getStateSet", int.class);
                    Method getStateDrawable = aClass.getMethod("getStateDrawable", int.class);
                    for (int i = 0; i < stateCount; i++) {
                        int[] stateSet = (int[]) getStateSet.invoke(listDrawable, i);
                        if (stateSet != null) {
                            if (stateSet.length > 0) {
                                if (stateSet[0] == android.R.attr.state_pressed) {
                                    Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                    mPressedThumbBitmap = getDrawBitmap(stateDrawable, true);
                                } else {
                                    throw new IllegalArgumentException("The state of the selector thumb drawable is wrong");
                                }
                            } else {
                                Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                mThumbBitmap = getDrawBitmap(stateDrawable, true);
                            }
                        }
                    }
                } else {
                    throw new IllegalArgumentException("The format of the selector thumb drawable is wrong");
                }
            } catch (Exception e) {
                mThumbBitmap = getDrawBitmap(mThumbDrawable, true);
                mPressedThumbBitmap = mThumbBitmap;
            }
        } else {
            mThumbBitmap = getDrawBitmap(mThumbDrawable, true);
            mPressedThumbBitmap = mThumbBitmap;
        }
    }

    /**
     * To initialize the bitmap for the tick mark
     */
    private void initTickMarkBitmap() {
        if (mTickMarkDrawable instanceof StateListDrawable) {
            StateListDrawable listDrawable = (StateListDrawable) mTickMarkDrawable;
            try {
                Class<? extends StateListDrawable> aClass = listDrawable.getClass();
                Method getStateCount = aClass.getMethod("getStateCount");

                String state = String.valueOf(getStateCount.invoke(listDrawable));
                int stateCount = Integer.parseInt(state);

                if (stateCount == 2) {
                    Method getStateSet = aClass.getMethod("getStateSet", int.class);
                    Method getStateDrawable = aClass.getMethod("getStateDrawable", int.class);
                    for (int i = 0; i < stateCount; i++) {
                        int[] stateSet = (int[]) getStateSet.invoke(listDrawable, i);
                        if (stateSet != null) {
                            if (stateSet.length > 0) {
                                if (stateSet[0] == android.R.attr.state_selected) {
                                    Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                    mSelectTickMarkBitmap = getDrawBitmap(stateDrawable, false);
                                } else {
                                    throw new IllegalArgumentException("The state of the selector tick mark drawable is wrong");
                                }
                            } else {
                                Drawable stateDrawable = (Drawable) getStateDrawable.invoke(listDrawable, i);
                                mUnselectTickMarkBitmap = getDrawBitmap(stateDrawable, false);
                            }
                        }
                    }
                } else {
                    throw new IllegalArgumentException("The format of the selector tick mark drawable is wrong");
                }
            } catch (Exception e) {
                mUnselectTickMarkBitmap = getDrawBitmap(mTickMarkDrawable, false);
                mSelectTickMarkBitmap = mUnselectTickMarkBitmap;
            }
        } else {
            mUnselectTickMarkBitmap = getDrawBitmap(mTickMarkDrawable, false);
            mSelectTickMarkBitmap = mUnselectTickMarkBitmap;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }

        super.setEnabled(enabled);
        if (isEnabled()) {
            setAlpha(1.0f);
            if (mIndicatorStayAlways) {
                mIndicatorContentView.setAlpha(1.0f);
            }
        } else {
            setAlpha(0.3f);
            if (mIndicatorStayAlways) {
                mIndicatorContentView.setAlpha(0.3f);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        post(this::requestLayout);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();
        if (parent == null) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                parent.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("isb_instance_state", super.onSaveInstanceState());
        bundle.putFloat("isb_progress", mProgress);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setProgress(bundle.getFloat("isb_progress"));
            super.onRestoreInstanceState(bundle.getParcelable("isb_instance_state"));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mUserSeekable || !isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performClick();
                float mX = event.getX();
                if (isTouchSeekBar(mX, event.getY())) {
                    if ((mOnlyThumbDraggable && !isTouchThumb(mX))) {
                        return false;
                    }
                    mIsTouching = true;
                    if (mSeekChangeListener != null) {
                        mSeekChangeListener.onStartTrackingTouch(this);
                    }
                    refreshSeekBar(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                refreshSeekBar(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsTouching = false;
                if (mSeekChangeListener != null) {
                    mSeekChangeListener.onStopTrackingTouch(this);
                }
                if (!autoAdjustThumb()) {
                    invalidate();
                }
                if (mIndicator != null) {
                    mIndicator.hide();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void refreshSeekBar(MotionEvent event) {
        refreshThumbCenterXByProgress(calculateProgress(calculateTouchX(adjustTouchX(event))));
        setSeekListener(true);
        invalidate();
        updateIndicator();
    }

    private boolean progressChange() {
        if (mIsFloatProgress) {
            return lastProgress != mProgress;
        } else {
            return Math.round(lastProgress) != Math.round(mProgress);
        }
    }

    private float adjustTouchX(MotionEvent event) {
        float mTouchXCache;

        if (event.getX() < mPaddingLeft) {
            mTouchXCache = mPaddingLeft;
        } else if (event.getX() > mMeasuredWidth - mPaddingRight) {
            mTouchXCache = mMeasuredWidth - mPaddingRight;
        } else {
            mTouchXCache = event.getX();
        }

        return mTouchXCache;
    }

    private float calculateProgress(float touchX) {
        lastProgress = mProgress;
        mProgress = mMin + (getAmplitude()) * (touchX - mPaddingLeft) / mSeekLength;
        return mProgress;
    }

    private float calculateTouchX(float touchX) {
        float touchXTemp = touchX;

        if (mTickCount > 2 && !mSeekSmoothly) {
            int touchBlockSize = Math.round((touchX - mPaddingLeft) / mSeekBlockLength);
            touchXTemp = mSeekBlockLength * touchBlockSize + mPaddingLeft;
        }
        if (mR2L) {
            return mSeekLength - touchXTemp + 2 * mPaddingLeft;
        }

        return touchXTemp;
    }

    private boolean isTouchSeekBar(float mX, float mY) {
        if (mFaultTolerance == -1) {
            mFaultTolerance = SizeUtils.dp2px(mContext, 5);
        }
        boolean inWidthRange = mX >= (mPaddingLeft - 2 * mFaultTolerance) && mX <= (mMeasuredWidth - mPaddingRight + 2 * mFaultTolerance);
        boolean inHeightRange = mY >= mProgressTrack.top - mThumbTouchRadius - mFaultTolerance && mY <= mProgressTrack.top + mThumbTouchRadius + mFaultTolerance;
        return inWidthRange && inHeightRange;
    }

    private boolean isTouchThumb(float mX) {
        float rawTouchX;
        refreshThumbCenterXByProgress(mProgress);

        if (mR2L) {
            rawTouchX = mBackgroundTrack.right;
        } else {
            rawTouchX = mProgressTrack.right;
        }

        return rawTouchX - mThumbSize / 2f <= mX && mX <= rawTouchX + mThumbSize / 2f;
    }

    private void updateIndicator() {
        if (mIndicatorStayAlways) {
            updateStayIndicator();
        } else {
            if (mIndicator == null) {
                return;
            }
            mIndicator.iniPop();
            if (mIndicator.isShowing()) {
                mIndicator.update(getThumbCenterX());
            } else {
                mIndicator.show(getThumbCenterX());
            }
        }
    }

    private void initIndicatorContentView() {
        if (mShowIndicatorType == IndicatorType.NONE) {
            return;
        }

        if (mIndicator == null) {
            mIndicator = new Indicator(mContext,
                    this,
                    mIndicatorColor,
                    mShowIndicatorType,
                    mIndicatorTextSize,
                    mIndicatorTextColor,
                    mIndicatorContentView,
                    mIndicatorTopContentView);
            this.mIndicatorContentView = mIndicator.getInsideContentView();
        }
    }

    private void updateStayIndicator() {
        if (!mIndicatorStayAlways || mIndicator == null) {
            return;
        }

        mIndicator.setProgressTextView(getIndicatorTextString());
        mIndicatorContentView.measure(0, 0);
        int measuredWidth = mIndicatorContentView.getMeasuredWidth();
        float thumbCenterX = getThumbCenterX();

        if (mScreenWidth == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

                if (windowManager != null) {
                    WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
                    Insets insets = windowMetrics.getWindowInsets()
                            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());

                    mScreenWidth = windowMetrics.getBounds().width() - insets.left - insets.right;
                }
            } else {
                DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
                if (metrics != null) {
                    mScreenWidth = metrics.widthPixels;
                }
            }
        }

        int indicatorOffset;
        int arrowOffset;

        if (measuredWidth / 2.0f + thumbCenterX > mMeasuredWidth) {
            indicatorOffset = mMeasuredWidth - measuredWidth;
            arrowOffset = (int) (thumbCenterX - indicatorOffset - measuredWidth / 2);
        } else if (thumbCenterX - measuredWidth / 2.0f < 0) {
            indicatorOffset = 0;
            arrowOffset = -(int) (measuredWidth / 2 - thumbCenterX);
        } else {
            indicatorOffset = (int) (getThumbCenterX() - measuredWidth / 2);
            arrowOffset = 0;
        }

        mIndicator.updateIndicatorLocation(indicatorOffset);
        mIndicator.updateArrowViewLocation(arrowOffset);
    }

    private boolean autoAdjustThumb() {
        if (mTickCount < 3 || !mSeekSmoothly) {
            return false;
        }

        if (!mAdjustAuto) {
            return false;
        }

        final int closestIndex = getClosestIndex();
        final float touchUpProgress = mProgress;

        ValueAnimator animator = ValueAnimator.ofFloat(0, Math.abs(touchUpProgress - mProgressArr[closestIndex]));
        animator.start();
        animator.addUpdateListener(animation -> {
            lastProgress = mProgress;

            if (touchUpProgress - mProgressArr[closestIndex] > 0) {
                mProgress = touchUpProgress - (Float) animation.getAnimatedValue();
            } else {
                mProgress = touchUpProgress + (Float) animation.getAnimatedValue();
            }
            refreshThumbCenterXByProgress(mProgress);

            setSeekListener(false);

            if (mIndicator != null && mIndicatorStayAlways) {
                mIndicator.refreshProgressText();
                updateStayIndicator();
            }

            invalidate();
        });
        return true;
    }

    /**
     * Transfer the progress value to string type
     */
    private String getProgressString(float progress) {
        if (mIsFloatProgress) {
            return FormatUtils.fastFormat(progress, mScale);
        } else {
            return String.valueOf(Math.round(progress));
        }
    }

    private int getClosestIndex() {
        int closestIndex = 0;
        float amplitude = Math.abs(mMax - mMin);
        for (int i = 0; i < mProgressArr.length; i++) {
            float amplitudeTemp = Math.abs(mProgressArr[i] - mProgress);
            if (amplitudeTemp <= amplitude) {
                amplitude = amplitudeTemp;
                closestIndex = i;
            }
        }
        return closestIndex;
    }

    private float getAmplitude() {
        return (mMax - mMin) > 0 ? (mMax - mMin) : 1;
    }

    private void setSeekListener(boolean formUser) {
        if (mSeekChangeListener == null) {
            return;
        }

        if (progressChange()) {
            mSeekChangeListener.onSeeking(collectParams(formUser));
        }
    }

    private SeekParams collectParams(boolean formUser) {
        if (mSeekParams == null) {
            mSeekParams = new SeekParams(this);
        }

        mSeekParams.progress = getProgress();
        mSeekParams.progressFloat = getProgressFloat();
        mSeekParams.fromUser = formUser;

        if (mTickCount > 2) {
            int rawThumbPos = getThumbPosOnTick();

            if (mShowTickText && mTickTextArr != null) {
                mSeekParams.tickText = mTickTextArr[rawThumbPos];
            }

            if (mR2L) {
                mSeekParams.thumbPosition = mTickCount - rawThumbPos - 1;
            } else {
                mSeekParams.thumbPosition = rawThumbPos;
            }
        }
        return mSeekParams;
    }

    private void apply(Builder builder) {

        this.mMax = builder.max;
        this.mMin = builder.min;
        this.mProgress = builder.progress;
        this.mIsFloatProgress = builder.progressValueFloat;
        this.mTickCount = builder.tickCount;
        this.mSeekSmoothly = builder.seekSmoothly;
        this.mR2L = builder.r2l;
        this.mUserSeekable = builder.userSeekable;
        this.mClearPadding = builder.clearPadding;
        this.mOnlyThumbDraggable = builder.onlyThumbDraggable;

        this.mShowIndicatorType = builder.showIndicatorType;
        this.mIndicatorColor = builder.indicatorColor;
        this.mIndicatorTextColor = builder.indicatorTextColor;
        this.mIndicatorTextSize = builder.indicatorTextSize;
        this.mIndicatorContentView = builder.indicatorContentView;
        this.mIndicatorTopContentView = builder.indicatorTopContentView;

        this.mBackgroundTrackSize = builder.trackBackgroundSize;
        this.mBackgroundTrackColor = builder.trackBackgroundColor;
        this.mProgressTrackSize = builder.trackProgressSize;
        this.mProgressTrackColor = builder.trackProgressColor;
        this.mTrackRoundedCorners = builder.trackRoundedCorners;

        this.mThumbSize = builder.thumbSize;
        this.mThumbDrawable = builder.thumbDrawable;
        this.mThumbTextColor = builder.thumbTextColor;
        initThumbColor(builder.thumbColorStateList, builder.thumbColor);
        this.mShowThumbText = builder.showThumbText;

        this.mShowTickMarkType = builder.showTickMarkType;
        this.mTickMarkSize = builder.tickMarkSize;
        this.mTickMarkDrawable = builder.tickMarkDrawable;
        this.mTickMarkEndsHide = builder.tickMarkEndsHide;
        this.mTickMarkSweptHide = builder.tickMarkSweptHide;
        initTickMarkColor(builder.tickMarkColorStateList, builder.tickMarkColor);

        this.mShowTickText = builder.showTickText;
        this.mTickTextSize = builder.tickTextSize;
        this.mTickTextCustomArray = builder.tickTextCustomArray;
        this.mTextTypeface = builder.tickTextTypeFace;
        initTickTextColor(builder.tickTextColorStateList, builder.tickTextColor);
    }

    void showStayIndicator() {
        mIndicatorContentView.setVisibility(INVISIBLE);
        postDelayed(() -> {
            Animation animation = new AlphaAnimation(0.1f, 1.0f);
            animation.setDuration(180);
            mIndicatorContentView.setAnimation(animation);
            updateStayIndicator();
            mIndicatorContentView.setVisibility(VISIBLE);
        }, 300);
    }

    void setIndicatorStayAlways() {
        this.mIndicatorStayAlways = true;
    }

    View getIndicatorContentView() {
        return mIndicatorContentView;
    }

    String getIndicatorTextString() {
        if (mIndicatorTextFormat != null && mIndicatorTextFormat.contains(FORMAT_TICK_TEXT)) {
            if (mTickCount > 2 && mTickTextArr != null) {
                return mIndicatorTextFormat.replace(FORMAT_TICK_TEXT, mTickTextArr[mR2L ? mTickCount - getThumbPosOnTick() - 1 : getThumbPosOnTick()]);
            }
        } else if (mIndicatorTextFormat != null && mIndicatorTextFormat.contains(FORMAT_PROGRESS)) {
            return mIndicatorTextFormat.replace(FORMAT_PROGRESS, getProgressString(mProgress));
        } else if (mIndicatorTextFormat != null && mIndicatorTextFormat.contains(FORMAT_EMPTY_TEXT)) {
            return mIndicatorTextFormat.replace(FORMAT_EMPTY_TEXT, "");
        }

        return getProgressString(mProgress);
    }

    /**
     * To create new a builder with default params
     *
     * @param context context environment
     * @return Builder
     */
    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * Get current indicator
     *
     * @return the indicator
     */
    public Indicator getIndicator() {
        return mIndicator;
    }

    /**
     * Get the tick count
     *
     * @return tick count
     */
    public int getTickCount() {
        return mTickCount;
    }

    /**
     * Get the SeekBar current level of progress in float type
     *
     * @return current progress in float type
     */
    public synchronized float getProgressFloat() {
        BigDecimal bigDecimal = BigDecimal.valueOf(mProgress);
        return bigDecimal.setScale(mScale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Get the SeekBar current level of progress in int type
     *
     * @return progress in int type
     */
    public int getProgress() {
        return Math.round(mProgress);
    }

    /**
     * Get the max limit of Seekbar range
     *
     * @return the SeekBar max value
     */
    public float getMax() {
        return mMax;
    }

    /**
     * Get the min limit of SeekBar range
     *
     * @return the SeekBar min value
     */
    public float getMin() {
        return mMin;
    }

    /**
     * Seeking params changing listener
     *
     * @return seeking listener
     */
    public OnSeekChangeListener getOnSeekChangeListener() {
        return mSeekChangeListener;
    }

    /**
     * Sets the current progress to the specified value
     * If the Seekbar tick count is larger than 2, the progress will adjust to the closest tick progress automatically
     *
     * @param progress a new progress value, if the new progress is less than min it will set to min,
     *                 if is over max, will be max
     */
    public synchronized void setProgress(float progress) {
        lastProgress = mProgress;
        mProgress = progress < mMin ? mMin : (Math.min(progress, mMax));

        if ((!mSeekSmoothly) && mTickCount > 2) {
            mProgress = mProgressArr[getClosestIndex()];
        }

        setSeekListener(false);
        refreshThumbCenterXByProgress(mProgress);
        postInvalidate();
        updateStayIndicator();
    }

    /**
     * Set the max value for SeekBar
     *
     * @param max the upper range of this progress bar
     */
    public synchronized void setMax(float max) {
        this.mMax = Math.max(mMin, max);
        initProgressRangeValue();
        collectTickInfo();
        refreshSeekBarLocation();
        invalidate();
        updateStayIndicator();
    }

    /**
     * Set the min value for SeekBar
     *
     * @param min the lower range of this progress bar
     */
    public synchronized void setMin(float min) {
        this.mMin = Math.min(mMax, min);
        initProgressRangeValue();
        collectTickInfo();
        refreshSeekBarLocation();
        invalidate();
        updateStayIndicator();
    }

    /**
     * Right-to-Left support
     *
     * @param isR2L true to enable right to left on the screen
     */
    public void setR2L(boolean isR2L) {
        this.mR2L = isR2L;
        requestLayout();
        invalidate();
        updateStayIndicator();
    }

    /**
     * Set a new thumb drawable
     *
     * @param drawable the drawable for thumb
     */
    public void setThumbDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mThumbDrawable = null;
            this.mThumbBitmap = null;
            this.mPressedThumbBitmap = null;
        } else {
            this.mThumbDrawable = drawable;
            this.mThumbRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mThumbSize) / 2.0f;
            this.mThumbTouchRadius = mThumbRadius;
            this.mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
            initThumbBitmap();
        }

        requestLayout();
        invalidate();
    }

    /**
     * To stop drawing the thumb
     *
     * @param hide true to hide, false to show
     */
    public void hideThumb(boolean hide) {
        mHideThumb = hide;
        invalidate();
    }

    /**
     * To stop drawing the text below the thumb
     *
     * @param hide true to hide, false to show
     */
    public void hideThumbText(boolean hide) {
        mShowThumbText = !hide;
        invalidate();
    }

    /**
     * Set the SeekBar thumb color
     *
     * @param thumbColor colorInt
     */
    public void thumbColor(@ColorInt int thumbColor) {
        this.mThumbColor = thumbColor;
        this.mPressedThumbColor = thumbColor;
        invalidate();
    }

    /**
     * Set the Seekbar thumb selector color
     *
     * @param thumbColorStateList color selector
     */
    public void thumbColorStateList(@NonNull ColorStateList thumbColorStateList) {
        initThumbColor(thumbColorStateList, mThumbColor);
        invalidate();
    }

    /**
     * Set a new tick mark drawable
     *
     * @param drawable the drawable for tick mark
     */
    public void setTickMarkDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mTickMarkDrawable = null;
            this.mUnselectTickMarkBitmap = null;
            this.mSelectTickMarkBitmap = null;
        } else {
            this.mTickMarkDrawable = drawable;
            this.mTickRadius = Math.min(SizeUtils.dp2px(mContext, THUMB_MAX_WIDTH), mTickMarkSize) / 2.0f;
            this.mCustomDrawableMaxHeight = Math.max(mThumbTouchRadius, mTickRadius) * 2.0f;
            initTickMarkBitmap();
        }
        invalidate();
    }

    /**
     * Set the SeekBar tick color
     *
     * @param tickMarkColor colorInt
     */
    public void tickMarkColor(@ColorInt int tickMarkColor) {
        this.mSelectedTickMarkColor = tickMarkColor;
        this.mUnSelectedTickMarkColor = tickMarkColor;
        invalidate();
    }

    /**
     * Set the SeekBar tick color
     *
     * @param tickMarkColorStateList colorInt
     */
    public void tickMarkColor(@NonNull ColorStateList tickMarkColorStateList) {
        initTickMarkColor(tickMarkColorStateList, mSelectedTickMarkColor);
        invalidate();
    }

    /**
     * Set the color for text below/above SeekBar tick text
     *
     * @param tickTextColor colorInt
     */
    public void tickTextColor(@ColorInt int tickTextColor) {
        mUnselectedTextColor = tickTextColor;
        mSelectedTextColor = tickTextColor;
        mHoveredTextColor = tickTextColor;
        invalidate();
    }

    /**
     * Set the selector color for text below/above SeekBar tick text
     *
     * @param tickTextColorStateList colorInt
     */
    public void tickTextColorStateList(@NonNull ColorStateList tickTextColorStateList) {
        initTickTextColor(tickTextColorStateList, mSelectedTextColor);
        invalidate();
    }

    /**
     * The specified scale for the progress value
     * Make sure you have chosen the float progress type
     * <p>
     * Such as:
     * scale = 3; progress: 1.78627347 to 1.786
     * scale = 4; progress: 1.78627347 to 1.7863
     * <p>
     * Make sure you have call the attr progress_value_float = true before, otherwise no change
     *
     * @param scale scale for the float type progress value
     */
    public void setDecimalScale(int scale) {
        this.mScale = scale;
    }

    /**
     * Set a format string with placeholder ${PROGRESS} or ${TICK_TEXT} to IndicatorSeekBar
     * The indicator's text would change
     * <p>
     * For example:
     * seekBar.setIndicatorTextFormat("${PROGRESS} %");
     * seekBar.setIndicatorTextFormat("${PROGRESS} miles");
     * seekBar.setIndicatorTextFormat("I am ${TICK_TEXT}%");
     * <p>
     * Make sure you show the tick text before you use ${TICK_TEXT}%
     * Otherwise will be show an empty value
     * <p>
     * Also, if the SeekBar type is CUSTOM, this method won't work, see{@link IndicatorType}
     *
     * @param format the format for indicator text
     */
    public void setIndicatorTextFormat(String format) {
        this.mIndicatorTextFormat = format;
        initTextArray();
        updateStayIndicator();
    }

    /**
     * To collect and customize the color for each of section track
     *
     * @param collector the container for section track color
     */
    public void customSectionTrackColor(@NonNull ColorCollector collector) {
        int[] colorArray = new int[mTickCount - 1 > 0 ? mTickCount - 1 : 1];

        Arrays.fill(colorArray, mBackgroundTrackColor);
        this.mCustomTrackSectionColorResult = collector.collectSectionTrackColor(colorArray);
        this.mSectionTrackColorArray = colorArray;
        invalidate();
    }

    /**
     * Replace the number tick text with yours by String[]
     * Usually, the text array length you set should equals SeekBar tick mark count
     *
     * @param tickTextArr the array contains the tick text
     */
    public void customTickText(@NonNull String[] tickTextArr) {
        this.mTickTextCustomArray = tickTextArr;
        if (mTickTextArr != null) {
            for (int i = 0; i < mTickTextArr.length; i++) {
                String tickText;
                if (i < tickTextArr.length) {
                    tickText = String.valueOf(tickTextArr[i]);
                } else {
                    tickText = "";
                }

                int index = i;
                if (mR2L) {
                    index = mTickCount - 1 - i;
                }
                mTickTextArr[index] = tickText;

                if (mTextPaint != null && mRect != null) {
                    mTextPaint.getTextBounds(tickText, 0, tickText.length(), mRect);
                    mTickTextWidth[index] = mRect.width();
                }
            }
            invalidate();
        }
    }

    /**
     * To set a custom tick text typeface
     *
     * @param typeface the typeface for tick text
     */
    public void customTickTextTypeface(@NonNull Typeface typeface) {
        this.mTextTypeface = typeface;
        measureTickTextBonds();
        requestLayout();
        invalidate();
    }

    /**
     * To attach a listener for seeking params changes
     *
     * @param listener OnSeekChangeListener
     */
    public void setOnSeekChangeListener(@NonNull OnSeekChangeListener listener) {
        this.mSeekChangeListener = listener;
    }

    /**
     * To only show the tick text on both ends of the SeekBar, make sure you have called the attr:show tick text before
     *
     * @param onlyShow true to only show the tick text on both ends of the SeekBar
     */
    public void showBothEndsTickTextOnly(boolean onlyShow) {
        this.mShowBothTickTextOnly = onlyShow;
    }

    /**
     * To prevent user from seeking
     *
     * @param seekAble true if user can seek
     */
    public void setUserSeekAble(boolean seekAble) {
        this.mUserSeekable = seekAble;
    }

    /**
     * To set the tick count
     */
    public synchronized void setTickCount(int tickCount) {
        if (mTickCount < 0 || mTickCount > 50) {
            throw new IllegalArgumentException("The argument: tick count must be limited between (0-50). Currently is: " + mTickCount);
        }

        mTickCount = tickCount;
        collectTickInfo();
        initTextArray();
        initSeekBarInfo();
        refreshSeekBarLocation();
        invalidate();
        updateStayIndicator();
    }

    /**
     * To set the thumb to automatically move to the closed tick after touched up, default is true
     *
     * @param adjustAuto true to auto move after touched up
     */
    public void setThumbAdjustAuto(boolean adjustAuto) {
        mAdjustAuto = adjustAuto;
    }
}