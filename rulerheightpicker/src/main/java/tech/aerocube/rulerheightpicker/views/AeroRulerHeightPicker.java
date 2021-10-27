package tech.aerocube.rulerheightpicker.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import tech.aerocube.rulerheightpicker.R;


public class AeroRulerHeightPicker extends FrameLayout {

    private View mTopSpacer;
    private View mBottomSpacer;
    private AeroRulerView aeroRulerView;
    private ObservableScrollView mScrollView;
    private float viewMultipleSize = 3f;

    private float maxValue = 100;
    private float minValue = 0;

    private float initValue = 0f;

    private int valueMultiple = 1;

    private int valueTypeMultiple = 5;

    public AeroRulerHeightPicker(Context context) {
        super(context);
        init(context,null);
    }

    public AeroRulerHeightPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public AeroRulerHeightPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AeroRulerHeightPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    public void setOnScrollChangedListener(final ObservableScrollView.OnScrollChangedListenerVertical onScrollChangedListenerVertical) {
        mScrollView.setOnScrollChangedListener(onScrollChangedListenerVertical);
    }

    public void setMaxValue(float minValue, float maxValue) {
        setMaxValue(minValue, maxValue, 1);
    }

    public void setMaxValue(float minValue, float maxValue, int valueMultiple) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueMultiple = valueMultiple;
        aeroRulerView.setMaxValue(this.maxValue);
        aeroRulerView.setMinValue(this.minValue);
        aeroRulerView.setValueMultiple(this.valueMultiple);
    }

    public void setValueTypeMultiple(int valueTypeMultiple) {
        this.valueMultiple = valueTypeMultiple;
        aeroRulerView.setMultipleTypeValue(valueTypeMultiple);
    }

    public void setViewMultipleSize(float size) {
        this.viewMultipleSize = size;
    }

    public void setInitValue(float initValue) {
        this.initValue = initValue;
    }

    public float getViewMultipleSize() {
        return this.viewMultipleSize;
    }


    private void init(Context context,@Nullable AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                    R.styleable.AeroRulerHeightPicker,
                    0,
                    0);

            try { //Parse params
                if (a.hasValue(R.styleable.AeroRulerHeightPicker_min_value)) {
                    minValue = a.getInteger(R.styleable.AeroRulerHeightPicker_min_value, 0);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_max_value)) {
                    maxValue = a.getInteger(R.styleable.AeroRulerHeightPicker_max_value, 100);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_ruler_multiple_size)) {
                    viewMultipleSize= a.getFloat(R.styleable.AeroRulerHeightPicker_ruler_multiple_size, 3f);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_value_multiple)) {
                    maxValue = a.getInteger(R.styleable.AeroRulerHeightPicker_value_multiple, 1);
                }
            }finally {
                a.recycle();
            }
        }



        mScrollView = new ObservableScrollView(context);
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setSmoothScrollingEnabled(true);
        mScrollView.setVerticalFadingEdgeEnabled(false);

        addView(mScrollView);

        final LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        mScrollView.addView(container);

        mTopSpacer = new View(context);
        mBottomSpacer = new View(context);

        aeroRulerView = new AeroRulerView(context);
        container.addView(aeroRulerView);
        container.addView(mTopSpacer, 0);
        container.addView(mBottomSpacer);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getWidth() != 0) {
                    AeroUtils.scrollToValueVertical(getScrollView(), initValue, maxValue, minValue, viewMultipleSize);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    public ObservableScrollView getScrollView() {
        return mScrollView;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ff0000"));
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        Path path = new Path();
        path.moveTo(getWidth() / 2 - 30 , 0);
        path.lineTo(getWidth() / 2, 40);
        path.lineTo(getWidth() / 2 + 30, 0);
        //  canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {

            final int height = getHeight();

            final ViewGroup.LayoutParams leftParams = mTopSpacer.getLayoutParams();
            leftParams.height = height / 2 + AeroUtils.dip2px(getContext(),100);
            mTopSpacer.setLayoutParams(leftParams);

            final ViewGroup.LayoutParams rulerViewParams = aeroRulerView.getLayoutParams();
            rulerViewParams.height = (int) (height * viewMultipleSize);  // set RulerView Width
            aeroRulerView.setLayoutParams(rulerViewParams);
            aeroRulerView.invalidate();


            final ViewGroup.LayoutParams rightParams = mBottomSpacer.getLayoutParams();
            rightParams.height = height / 2  + AeroUtils.dip2px(getContext(),100);
            mBottomSpacer.setLayoutParams(rightParams);

            invalidate();

        }
    }
}
