package tech.aerocube.rulerheightpicker.views;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import tech.aerocube.rulerheightpicker.R;


public class AeroRulerHeightPicker extends FrameLayout {

    private View mTopSpacer;
    private View mBottomSpacer;
    private AeroRulerView aeroRulerView;
    private ObservableScrollView mScrollView;
    private View indicator;
    private int indicatorId;
    private float viewMultipleSize = 3f;
    private float spacingMultiplier=1f;

    private float maxValue = 100;
    private float minValue = 0;

    private float initValue = 0f;
    private float currentValue=0f;

    private int movement=0;

    private int valueMultiple = 1;

    private int valueTypeMultiple = 5;

    private boolean matric=true;
    private boolean reinit=true;

    private Context mContext;

    private int scrollHeight=0;
    private OnAeroPickerScrollListener onAeroPickerScrollListener;

    public interface OnAeroPickerScrollListener{
        void onPickerScrolling(float value);

        void onPickerStopped(float value);

    }

    public AeroRulerHeightPicker(Context context) {
        super(context);
        mContext=context;
        init(context,null);
    }

    public AeroRulerHeightPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init(context,attrs);
    }

    public AeroRulerHeightPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AeroRulerHeightPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
        init(context,attrs);
    }

    public void setOnAeroPickerScrollListener(final OnAeroPickerScrollListener l) {
        onAeroPickerScrollListener=l;

    }




    private void init(Context context,@Nullable AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                    R.styleable.AeroRulerHeightPicker,
                    0,
                    0);

            try { //Parse params
                if (a.hasValue(R.styleable.AeroRulerHeightPicker_init_value)) {
                    initValue = a.getInteger(R.styleable.AeroRulerHeightPicker_init_value, 0);
                }
                if (a.hasValue(R.styleable.AeroRulerHeightPicker_min_value)) {
                    minValue = a.getInteger(R.styleable.AeroRulerHeightPicker_min_value, 0);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_max_value)) {
                    maxValue = a.getInteger(R.styleable.AeroRulerHeightPicker_max_value, 100);
                }

                //space between line
                if (a.hasValue(R.styleable.AeroRulerHeightPicker_spacing_multiplier)) {
                    spacingMultiplier= a.getFloat(R.styleable.AeroRulerHeightPicker_spacing_multiplier, 1f);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_value_multiple)) {
                    valueMultiple = a.getInteger(R.styleable.AeroRulerHeightPicker_value_multiple, 1);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_value_type_multiple)) {
                    valueTypeMultiple = a.getInteger(R.styleable.AeroRulerHeightPicker_value_type_multiple, 5);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_matric)) {
                    matric = a.getBoolean(R.styleable.AeroRulerHeightPicker_matric, true);
                }

                if (a.hasValue(R.styleable.AeroRulerHeightPicker_indicator)) {
                    indicatorId = a.getResourceId(R.styleable.AeroRulerHeightPicker_indicator, -1);
                }
                if (a.hasValue(R.styleable.AeroRulerHeightPicker_indicator_movement)) {
                    movement = a.getInteger(R.styleable.AeroRulerHeightPicker_indicator_movement, 0);
                }

            }finally {
                a.recycle();
            }
        }

        if(initValue<minValue)
            initValue=minValue;
        if(initValue>maxValue)
            initValue=maxValue;

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


        initializeView(context,false);
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

    public void initializeView(Context context, boolean relayout) {
        if(initValue<minValue)
            initValue=minValue;
        if(initValue>maxValue)
            initValue=maxValue;
        aeroRulerView.setMaxValue(this.maxValue);
        aeroRulerView.setMinValue(this.minValue);
        aeroRulerView.setValueMultiple(this.valueMultiple);
        aeroRulerView.setMultipleTypeValue(valueTypeMultiple);
        aeroRulerView.setMatric(matric);
        viewMultipleSize=spacingMultiplier*(maxValue-minValue)/100;
        reinit=true;
        if(relayout)
            onRelayout();

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

    private void onRelayout() {
        final int height = getHeight();

        final ViewGroup.LayoutParams leftParams = mTopSpacer.getLayoutParams();
        leftParams.height = height / 2 + AeroUtils.dip2px(getContext(),(float) movement/2);
        mTopSpacer.setLayoutParams(leftParams);

        final ViewGroup.LayoutParams rulerViewParams = aeroRulerView.getLayoutParams();
        rulerViewParams.height = (int) ((int) (height * viewMultipleSize));// set RulerView Width
        aeroRulerView.setLayoutParams(rulerViewParams);
        aeroRulerView.invalidate();


        final ViewGroup.LayoutParams rightParams = mBottomSpacer.getLayoutParams();
        rightParams.height = height / 2  + AeroUtils.dip2px(getContext(),(float) movement/2);
        mBottomSpacer.setLayoutParams(rightParams);

        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        indicator=((View)this.getParent()).findViewById(indicatorId);

        getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    getScrollView().startScrollerTask();
                }
                return false;
            }
        });

        mScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListenerVertical() {
            @Override
            public void onScrollChanged(ObservableScrollView view, int l, int t) {
                LinearLayout v= (LinearLayout) view.getChildAt(0);
                scrollHeight =v.getChildAt(1).getMeasuredHeight()+ AeroUtils.dip2px(mContext,movement);

                ConstraintSet set=new ConstraintSet();
                set.clone((ConstraintLayout) indicator.getParent());
                set.setMargin(indicatorId,ConstraintSet.TOP,AeroUtils.dip2px(mContext,movement)-(t* AeroUtils.dip2px(mContext,movement)/ scrollHeight));
                set.applyTo((ConstraintLayout) indicator.getParent());

                currentValue=AeroUtils.getRulerViewValueVertical(getScrollView()
                        , l
                        , t-(t* AeroUtils.dip2px(mContext,movement)/ scrollHeight)
                        , getMaxValue()
                        , getMinValue(),getViewMultipleSize(),1);


                onAeroPickerScrollListener.onPickerScrolling(currentValue);

            }

            @Override
            public void onScrollStopped(int l, int t) {

                currentValue=AeroUtils.getValueAndScrollItemToAnchorVertical(getScrollView()
                        , l
                        , t-(t* AeroUtils.dip2px(mContext,movement)/ scrollHeight)
                        ,getMaxValue()
                        ,getMinValue()
                        ,getViewMultipleSize());

                onAeroPickerScrollListener.onPickerStopped(currentValue);

            }
        });

    }

    public ObservableScrollView getScrollView() {
        return mScrollView;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (reinit) {

            final int height = getHeight();

            final ViewGroup.LayoutParams leftParams = mTopSpacer.getLayoutParams();
            leftParams.height = height / 2 + AeroUtils.dip2px(getContext(),(float) movement/2);
            mTopSpacer.setLayoutParams(leftParams);

            final ViewGroup.LayoutParams rulerViewParams = aeroRulerView.getLayoutParams();
            rulerViewParams.height = (int) ((int) (height * viewMultipleSize));// set RulerView Width
            aeroRulerView.setLayoutParams(rulerViewParams);
            aeroRulerView.invalidate();


            final ViewGroup.LayoutParams rightParams = mBottomSpacer.getLayoutParams();
            rightParams.height = height / 2  + AeroUtils.dip2px(getContext(),(float) movement/2);
            mBottomSpacer.setLayoutParams(rightParams);

            invalidate();
            aeroRulerView.invalidate();
            reinit=false;
        }
    }





    public void setInitValue(float initValue) {
        this.initValue = initValue;
    }



    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        aeroRulerView.setMaxValue(this.maxValue);
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
        aeroRulerView.setMinValue(this.minValue);
    }

    public float getViewMultipleSize() {
        return this.viewMultipleSize;
    }

    public void setViewMultipleSize(float size) {
        this.viewMultipleSize = size;
    }

    public int getValueMultiple() {
        return valueMultiple;
    }

    public void setValueMultiple(int valueMultiple) {
        this.valueMultiple = valueMultiple;
        aeroRulerView.setValueMultiple(this.valueMultiple);
    }

    public int getValueTypeMultiple() {
        return valueTypeMultiple;
    }

    public void setValueTypeMultiple(int valueTypeMultiple) {
        this.valueTypeMultiple = valueTypeMultiple;
        aeroRulerView.setMultipleTypeValue(valueTypeMultiple);
    }

    public boolean isMatric() {
        return matric;
    }

    public void setMatric(boolean matric) {
        this.matric = matric;
        aeroRulerView.setMatric(this.matric);

    }

    public float getSpacingMultiplier() {
        return spacingMultiplier;
    }

    public void setSpacingMultiplier(float spacingMultiplier) {
        this.spacingMultiplier = spacingMultiplier;
    }
}
