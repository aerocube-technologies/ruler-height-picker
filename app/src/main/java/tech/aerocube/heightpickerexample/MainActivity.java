package tech.aerocube.heightpickerexample;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.constraintlayout.widget.ConstraintSet;

import android.widget.TextView;

import java.util.Locale;

import tech.aerocube.rulerheightpicker.views.AeroRulerHeightPicker;
import tech.aerocube.rulerheightpicker.views.AeroUtils;
import tech.aerocube.rulerheightpicker.views.ObservableScrollView;


public class MainActivity extends AppCompatActivity {
    private AeroRulerHeightPicker myScrollingValuePicker;

    private static final float MIN_VALUE = 40;
    private static final float MAX_VALUE = 128;
    private static final float LINE_RULER_MULTIPLE_SIZE = 2f;
    private TextView rulerText;
    private View indicator;
    private int scrollHeight =0;
    private float lastValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rulerText = (TextView) findViewById(R.id.rulerview_value);
        indicator=findViewById(R.id.indicator);

        initRulerView();
    }

    /**
     * initRulerView
     */
    private void initRulerView() {
        myScrollingValuePicker = findViewById(R.id.myScrollingValuePicker);
        myScrollingValuePicker.setViewMultipleSize(LINE_RULER_MULTIPLE_SIZE);
        myScrollingValuePicker.setMaxValue(MIN_VALUE, MAX_VALUE);
        myScrollingValuePicker.setValueTypeMultiple(12);
        myScrollingValuePicker.getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    myScrollingValuePicker.getScrollView().startScrollerTask();
                }
                return false;
            }
        });


        myScrollingValuePicker.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListenerVertical() {

            @Override
            public void onScrollChanged(ObservableScrollView view, int l, int t) {
                LinearLayout v= (LinearLayout) view.getChildAt(0);
                scrollHeight =v.getChildAt(1).getMeasuredHeight()+ AeroUtils.dip2px(MainActivity.this,200);

                ConstraintSet set=new ConstraintSet();
                set.clone((ConstraintLayout) indicator.getParent());
                set.setMargin(R.id.indicator,ConstraintSet.TOP,AeroUtils.dip2px(MainActivity.this,200)-(t* AeroUtils.dip2px(MainActivity.this,200)/ scrollHeight));
                set.applyTo((ConstraintLayout) indicator.getParent());

                int value=AeroUtils.getRulerViewValueVertical(myScrollingValuePicker.getScrollView()
                        , l
                        , t-(t* AeroUtils.dip2px(MainActivity.this,200)/ scrollHeight)
                        , MAX_VALUE
                        , MIN_VALUE,myScrollingValuePicker.getViewMultipleSize(),1);
                int feet=value/12;
                int inch=value-(feet*12);
                rulerText.setText(String.format(Locale.US,"%d'%d\"",feet,inch));


            }

            @Override
            public void onScrollStopped(int l, int t) {
                int value=AeroUtils.getValueAndScrollItemToAnchorVertical(myScrollingValuePicker.getScrollView()
                        , l
                        , t-(t* AeroUtils.dip2px(MainActivity.this,200)/ scrollHeight)
                        , MAX_VALUE
                        , MIN_VALUE
                        , myScrollingValuePicker.getViewMultipleSize());
                int feet=value/12;
                int inch=value-(feet*12);
                rulerText.setText(String.format(Locale.US,"%d'%d\"",feet,inch));

            }
        });
    }


}