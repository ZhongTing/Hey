package slm2015.hey.view.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HeyPager extends ViewPager{
    private float mStartDragX;

    public HeyPager(Context context) {
        super(context);
    }

    public HeyPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX < x && getCurrentItem() == 0) {
//                    mListener.onSwipeOutAtStart();
                    return false;
                } else if (mStartDragX > x && getCurrentItem() == getAdapter().getCount() - 1) {
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
