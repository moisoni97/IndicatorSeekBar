# Android Indicator SeekBar Library [![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=17) [![JitCI](https://jitci.com/gh/moisoni97/IndicatorSeekBar/svg)](https://jitci.com/gh/moisoni97/IndicatorSeekBar) [![JitPack](https://jitpack.io/v/moisoni97/IndicatorSeekBar.svg)](https://jitpack.io/#moisoni97/IndicatorSeekBar)



A highly customizable SeekBar library for Android.

<img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/overview.png?raw=true" width = "392" height = "115"/>

<img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/continuous.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/discrete_1.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/discrete_2.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/custom.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/java_build.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/moisoni97/IndicatorSeekBar/blob/master/art/indicator.gif?raw=true" width = "264" height = "464"/>

# Getting Started

* You project should build against Android 4.2.x (minSdkVersion 17).

* Add the JitPack repository to your project's build.gradle file:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* Add the dependency in your app's build.gradle file:

```gradle
dependencies {
    implementation 'com.github.moisoni97:IndicatorSeekBar:3.0.0'
}
```

# Usage
To create an `IndicatorSeekBar` you can either do it via `XML` or `Java`:

* `XML implementation:`

```xml
<com.warkiz.widget.IndicatorSeekBar
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:isb_max="100"
    app:isb_min="-1.0"
    app:isb_progress="25"
    app:isb_seek_smoothly="true"
    app:isb_tick_count="5"
    app:isb_show_tick_mark_type="oval"
    app:isb_tick_mark_size="13dp"
    app:isb_tick_mark_drawable="@mipmap/ic_launcher"
    app:isb_show_tick_text="true"
    app:isb_tick_text_size="15sp"
    app:isb_tick_text_color="@color/color_blue"
    app:isb_thumb_color="@color/color_green"
    app:isb_thumb_size="20dp"
    app:isb_show_indicator="rounded_rectangle"
    app:isb_indicator_color="@color/color_gray"
    app:isb_indicator_text_color="@color/colorAccent"
    app:isb_indicator_text_size="18sp"
    app:isb_track_background_color="@color/color_gray"
    app:isb_track_background_size="2dp"
    app:isb_track_progress_color="@color/color_blue"
    app:isb_track_progress_size="4dp"
    app:isb_only_thumb_draggable="false"/>
```

* `Java code:`

```java
 IndicatorSeekBar indicatorSeekBar = IndicatorSeekBar
                .with(getContext())
                .max(110)
                .min(10)
                .progress(53)
                .tickCount(7)
                .showTickMarkType(TickMarkType.OVAL)
                .tickMarkColor(getResources().getColor(R.color.color_blue, null))
                .tickMarkSize(13)
                .showTickText(true)
                .tickTextColor(getResources().getColor(R.color.color_pink))
                .tickTextSize(13)
                .tickTextTypeFace(Typeface.MONOSPACE)
                .showIndicatorType(IndicatorType.ROUNDED_RECTANGLE)
                .indicatorColor(getResources().getColor(R.color.color_blue))
                .indicatorTextColor(Color.parseColor("#ffffff"))
                .indicatorTextSize(13)
                .thumbColor(getResources().getColor(R.color.colorAccent, null))
                .thumbSize(14)
                .trackProgressColor(getResources().getColor(R.color.colorAccent,null))
                .trackProgressSize(4)
                .trackBackgroundColor(getResources().getColor(R.color.color_gray))
                .trackBackgroundSize(2)
		.onlyThumbDraggable(false)
                .build();

```

# Indicator stay always
The `indicator` of the SeekBar can be set to stay always visible. To achieve this, `IndicatorSeekBar` must be placed inside of `IndicatorStayLayout`

* `XML implementation:`

```xml
<com.warkiz.widget.IndicatorStayLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
   
    <com.warkiz.widget.IndicatorSeekBar
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:isb_show_indicator="rectangle" <!--indicator can not be type: NONE-->
        ....../>
  
</com.warkiz.widget.IndicatorStayLayout>
```
* `Java code:`

```java
IndicatorSeekBar indicatorSeekBar = IndicatorSeekBar
                .with(getContext())
                .max(50)
                .min(10)
                .showIndicatorType(IndicatorType.RECTANGLE) //indicator can not be type: NONE
                ...
                .build();

new IndicatorStayLayout(getContext()).attachTo(indicatorSeekBar);
```

# Custom indicator view
The `indicator view` can be customized is different ways:

* To replace the `indicator view` top part:
```java
indicatorSeekBar.getIndicator().setTopContentView(yourTopView);
```

* To set a custom `indicator view`:
```java
indicatorSeekBar.getIndicator().setContentView(yourView);
```

# Custom indicator text
Set a format string with placeholder `${PROGRESS}`, `${TICK_TEXT}` or `${EMPTY_TEXT}` to IndicatorSeekBar.

* To show the progress with suffix `%`:

`Java`:
```java
indicatorSeekBar.setIndicatorTextFormat("${PROGRESS} %")
```
`Kotlin`:

```kotlin
indicatorSeekBar.setIndicatorTextFormat("\${PROGRESS} %")
```

* To show the tick text with prefix `I am`:

`Java:`
```java
indicatorSeekBar.setIndicatorTextFormat("I am ${TICK_TEXT}")
```

`Kotlin`:
```kotlin
indicatorSeekBar.setIndicatorTextFormat("I am \${TICK_TEXT}")
```

* To show custom text, excluding `progress` or `tick text`:

`Java:`
```java
indicatorSeekBar.setIndicatorTextFormat("Speed x0.25 ${EMPTY_TEXT}")
```

`Kotlin:`
```kotlin
indicatorSeekBar.setIndicatorTextFormat("Speed x0.25 \${EMPTY_TEXT}")
```

# Custom section track color
The color of every block in `IndicatorSeekBar` can be customized:

```java
seekBar.customSectionTrackColor(new ColorCollector() {
    @Override
    public boolean collectSectionTrackColor(int[] colorIntArr) {
        //the length of colorIntArray equals section count
        colorIntArr[0] = getResources().getColor(R.color.color_blue, null);
        colorIntArr[1] = getResources().getColor(R.color.color_gray, null);
        colorIntArr[2] = Color.parseColor("#FF4081");
        ...
        return true; //true to apply color, otherwise no change
    }
});
```

# Selector drawable & color are supported
You can set the `StateListDrawable` or `ColorStateList` for the thumb and tick mark. Also, `ColorStateList` for tick text is supported:

* `thumb` selector drawable:

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--drawable for thumb in pressed state-->
    <item android:drawable="@mipmap/ic_launcher_round" android:state_pressed="true" />
    <!--drawable for thumb in normal state-->
    <item android:drawable="@mipmap/ic_launcher" />
</selector>
```

* `thumb` selector color:

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--color for thumb in pressed state-->
    <item android:color="@color/colorAccent" android:state_pressed="true" />
    <!--color for thumb in normal state-->
    <item android:color="@color/color_blue" />
</selector>
```

* `tick mark` selector drawable：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--drawable for tick mark at the left side of the thumb-->
    <item android:drawable="@mipmap/ic_launcher_round" android:state_selected="true" />
    <!--drawable for tick mark at the right side of the thumb-->
    <item android:drawable="@mipmap/ic_launcher" />
</selector>
```

* `tick mark` selector color：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--color for tick mark at the left side of the thumb-->
    <item android:color="@color/colorAccent" android:state_selected="true" />
    <!--color for tick mark at the right side of the thumb-->
    <item android:color="@color/color_gray" />
</selector>
```

* `tick text` selector color：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
     <!--color for tick text at the left side of the thumb-->
    <item android:color="@color/colorAccent" android:state_selected="true" />
     <!--color for tick text under the thumb-->
    <item android:color="@color/color_blue" android:state_hovered="true" />
     <!--color for tick text at the right side of the thumb-->
    <item android:color="@color/color_gray" />
</selector>
```

# Listener
* Implement the listener to handle `SeekParams` changes:

```java
seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                Log.i(TAG, seekParams.seekBar);
                Log.i(TAG, seekParams.progress);
                Log.i(TAG, seekParams.progressFloat);
                Log.i(TAG, seekParams.fromUser);
                Log.i(TAG, seekParams.thumbPosition);
                Log.i(TAG, seekParams.tickText);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
	    
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
	    
            }
        });
```

# Attributes
* Check all `IndicatorSeekBar` attributes here:
[ attr.xml ](https://github.com/moisoni97/IndicatorSeekBar/blob/master/indicatorseekbar/src/main/res/values/attr.xml)
