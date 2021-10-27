package tech.aerocube.rulerheightpicker.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class AeroRulerView extends View {

    private Paint paint;
    private Paint paintLong;
    private Paint textPaint;
    private DashPathEffect dashPathEffect;
    private Path backGroundPath;

    private float bigUnitLineHeight = 0f;
    private float samllUnitLineHeight = 0f;

    private float MAX_DATA = 100;
    private float MIN_DATA = 0;

    private int viewHeight = 0;
    private int viewWidth = 0;

    private int showRangeValue = 5;

    private int valueMultiple = 1;

    private int displayNumberType = 1;

    public static final int DISPLAY_NUMBER_TYPE_SPACIAL_COUNT = 1;
    public static final int DISPLAY_NUMBER_TYPE_MULTIPLE = 2;
    private int valueTypeMultiple = 5;

    private int longWidthRatio = 10;
    private int shortWidthRatio = 5;
    private int baseWidthRatio = 2;

    private boolean matric=true;


    public AeroRulerView(Context context) {
        super(context);
        init(context);
    }

    public AeroRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AeroRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AeroRulerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setColor(Color.parseColor("#dddddd"));
        paint.setStrokeWidth(4f);
        paint.isAntiAlias();
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        paintLong = new Paint();
        paintLong.setColor(Color.parseColor("#aaaaaa"));
        paintLong.setStrokeWidth(4f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paintLong.isAntiAlias();
        paintLong.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFF000000);
        textPaint.isAntiAlias();
        textPaint.setTextSize(AeroUtils.sp2px(context, 14));
        textPaint.setTextAlign(Paint.Align.RIGHT);

        invalidate();
    }

    public AeroRulerView setMaxValue(float maxValue) {
        this.MAX_DATA = maxValue;
        return this;
    }

    public AeroRulerView setMatric(boolean matric) {
        this.matric = matric;
        return this;
    }

    public AeroRulerView setMinValue(float minValue) {
        this.MIN_DATA = minValue;
        return this;
    }

    public AeroRulerView setMinMaxValue(float minValue, float maxValue) {
        this.MIN_DATA = minValue;
        this.MAX_DATA = maxValue;
        return this;
    }

    public AeroRulerView setValueMultiple(int valueMultiple) {
        this.valueMultiple = valueMultiple;
        return this;
    }

    public void setMultipleTypeValue(int valueTypeMultiple) {
        this.displayNumberType = DISPLAY_NUMBER_TYPE_MULTIPLE;
        this.valueTypeMultiple = valueTypeMultiple;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        float endpoint= (float) (viewWidth-(viewWidth*0.1));
        float viewInterval = (float) viewHeight / (MAX_DATA - MIN_DATA);
        canvas.drawLine(0, 0, viewWidth / longWidthRatio * baseWidthRatio, 0, paint);
        for (int i = 1; i < (MAX_DATA - MIN_DATA); i++) {
            if (displayNumberType == DISPLAY_NUMBER_TYPE_MULTIPLE) {
                if (((int) (i + MIN_DATA) * valueMultiple) % valueTypeMultiple == 0) {
                    canvas.drawLine(endpoint, viewInterval * i,  endpoint- viewWidth / shortWidthRatio * baseWidthRatio,viewInterval * i, paintLong);
                    canvas.drawText("" + ((int) (i + MIN_DATA) * valueMultiple)/(matric?1:12),  endpoint- viewWidth / shortWidthRatio * baseWidthRatio - AeroUtils.sp2px(getContext(), 14),viewInterval * i+textPaint.getTextSize()/3, textPaint);
                } else {
                    canvas.drawLine( endpoint,viewInterval * i,  endpoint-viewWidth / longWidthRatio * baseWidthRatio,viewInterval * i, paint);
                }

            } else {
                if (i % 5 == 0) {
                    canvas.drawLine( endpoint, viewInterval * i, endpoint-viewWidth / shortWidthRatio * baseWidthRatio,viewInterval * i, paintLong);
                    canvas.drawText("" + ((int) (i + MIN_DATA) * valueMultiple)/(matric?1:12),  endpoint-viewWidth / shortWidthRatio * baseWidthRatio - AeroUtils.sp2px(getContext(), 14),viewInterval * i+textPaint.getTextSize()/3, textPaint);
                } else {
                    canvas.drawLine(endpoint, viewInterval * i,  endpoint-viewWidth / longWidthRatio * baseWidthRatio,viewInterval * i, paint);
                }
            }
        }
        canvas.drawLine( endpoint,viewHeight,  endpoint-viewWidth / longWidthRatio * baseWidthRatio,viewHeight, paint);

        super.onDraw(canvas);
    }
}

