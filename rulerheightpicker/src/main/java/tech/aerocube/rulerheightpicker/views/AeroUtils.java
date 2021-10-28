package tech.aerocube.rulerheightpicker.views;


import android.content.Context;

public class AeroUtils {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }





    //===========================================================================================================

    public static int getRulerViewValueVertical(ObservableScrollView view, int l, int t, float MAX_VALUE, float MIN_VALUE, float multipleSize, int valueMultiple) {

        float oneValue = (float) view.getHeight() * multipleSize / (MAX_VALUE - MIN_VALUE);
        int value = (int) (t / oneValue) + (int) MIN_VALUE;
        int offset = (int) (t % oneValue);


        if (offset > oneValue / 2) {
            value += 1;

        }

        if (value > MAX_VALUE) {
            value = (int) MAX_VALUE;
        }

        return value * valueMultiple;
    }

    public static void scrollToValueVertical(ObservableScrollView view, float value, float MAX_VALUE, float MIN_VALUE, float multipleSize) {
        float oneValue = (float) view.getHeight() * multipleSize / (MAX_VALUE - MIN_VALUE);
        float valueHeight = oneValue * (value - MIN_VALUE);

        view.scrollBy((int) valueHeight, 0);
    }

    public static int getValueAndScrollItemToAnchorVertical(ObservableScrollView view, int l, int t, float MAX_VALUE, float MIN_VALUE, float multipleSize) {
        return getValueAndScrollItemToCenterVertical(view, l, t, MAX_VALUE, MIN_VALUE, multipleSize, 1);
    }

    public static int getValueAndScrollItemToCenterVertical(ObservableScrollView view, int l, int t, float MAX_VALUE, float MIN_VALUE, float multipleSize, int valueMultiple) {
        float oneValue = (float) view.getHeight() * multipleSize / (MAX_VALUE - MIN_VALUE);
        int value = (int) (t / oneValue) + (int) MIN_VALUE;
        int offset = (int) (t % oneValue);


        if (offset > oneValue / 2) {
            value += 1;
            view.smoothScrollBy(0,(int) oneValue - offset);

        } else {
            view.smoothScrollBy(0,-offset);
        }

        if (value > MAX_VALUE) {
            value = (int) MAX_VALUE;
        }

        return value * valueMultiple;
    }


}

