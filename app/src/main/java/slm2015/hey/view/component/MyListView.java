package slm2015.hey.view.component;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class MyListView extends ListView implements AbsListView.OnScrollListener {

    private View indicatorView;
    private TextView timeTextView;
    private OnScrollListener scrollListener;

    public MyListView(Context context) {
        super(context);
        this.init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public void setIndicatorView(View view) {
        this.indicatorView = view;
        this.indicatorView.setAlpha(0);
        this.timeTextView = ((TextView) view.findViewById(R.id.time_text_view));
    }

    private void init() {
        super.setOnScrollListener(this);
    }

    public float getScrollRatio() {
        return ((float) computeVerticalScrollOffset()) / computeVerticalScrollRange();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.scrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.indicatorView == null) {
            return;
        }
        if (scrollState == SCROLL_STATE_IDLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this.indicatorView, "alpha", getAlpha(), 0);
            animator.setDuration(300);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this.indicatorView, "alpha", getAlpha(), 1);
            animator.setDuration(300);
            animator.start();
        }
        if (scrollListener != null) {
            scrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        double ratio = this.getScrollRatio();
        int y = (int) (view.getMeasuredHeight() * ratio);
        if (this.indicatorView != null) {
            this.indicatorView.setTranslationY(y);
        }
        if (this.timeTextView != null && totalItemCount > 0) {
            Issue issue = ((Issue) this.getItemAtPosition(firstVisibleItem));
            this.timeTextView.setText(issue.getTimestamp());
        }
        if (scrollListener != null) {
            scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
