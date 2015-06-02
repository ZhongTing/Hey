package slm2015.hey.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fortysevendeg.swipelistview.SwipeListView;

public class HeySwipeListView extends SwipeListView{
    public HeySwipeListView(Context context, int swipeBackView, int swipeFrontView) {
        super(context, swipeBackView, swipeFrontView);
    }

    public HeySwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeySwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
