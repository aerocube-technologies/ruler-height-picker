package tech.aerocube.rulerheightpicker.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by DWFOX on 2016-09-01.
 */
public class ObservableScrollView extends ScrollView {

    private Runnable scrollerTask;
    private int initialPosition;

    private int newCheck = 100;

    private float initValue = 0f;

    public interface OnScrollChangedListenerVertical {
        void onScrollChanged(ObservableScrollView view, int l, int t);

        void onScrollStopped(int l, int t);
    }


    private OnScrollChangedListenerVertical mOnScrollChangedListenerVertical;

    public ObservableScrollView(Context context) {
        super(context);
        init();
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void setOnScrollChangedListener(OnScrollChangedListenerVertical l) {
        mOnScrollChangedListenerVertical = l;
    }


    private void init() {
        scrollerTask = new Runnable() {
            public void run() {

                int newPosition = getScrollY();
                if (initialPosition - newPosition == 0) {//has stopped

                    if (mOnScrollChangedListenerVertical != null) {
                        mOnScrollChangedListenerVertical.onScrollStopped(getScrollX(), getScrollY());
                    }
                } else {
                    initialPosition = getScrollY();
                    ObservableScrollView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListenerVertical != null) {
            mOnScrollChangedListenerVertical.onScrollChanged(this, l, t);

        }
    }

    public void startScrollerTask() {

        initialPosition = getScrollY();
        ObservableScrollView.this.postDelayed(scrollerTask, newCheck);
    }
}



