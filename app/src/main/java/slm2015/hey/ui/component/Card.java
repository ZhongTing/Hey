package slm2015.hey.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class Card extends FrameLayout {

    private TextView subjectTextView;
    private TextView descriptionTextView;
    private TextView positionTextView;
    private ImageView image;
    private Issue issue;
    private View view;

    public Card(Context context) {
        super(context);
        this.init();
    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public Card(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        //preview in edit mode;
        if (isInEditMode()) {
            View view = inflate(this.getContext(), R.layout.card, this);
        }
        //note that the actual view will generate when issue is assigned
    }

    public void assignIssue(Issue issue) {
        this.issue = issue;
        int layoutId = this.issue.getImage() == null ? R.layout.card_no_pic : R.layout.card;
        this.view = inflate(this.getContext(), layoutId, this);
        this.subjectTextView = (TextView) this.view.findViewById(R.id.title);
        this.descriptionTextView = (TextView) this.view.findViewById(R.id.description);
        this.positionTextView = (TextView) this.view.findViewById(R.id.location);
        this.findView(this.view, issue);
        this.bindEvent();
    }

    private void findView(View view, Issue issue) {
        this.subjectTextView.setText(issue.getSubject());
        this.descriptionTextView.setText(issue.getDescription());
        this.positionTextView.setText(issue.getPlace());
        this.subjectTextView.postInvalidate();
        this.descriptionTextView.postInvalidate();
        this.positionTextView.postInvalidate();
    }

    private void bindEvent() {
        //todo bind event here
    }

    public void setMargin(int left, int top, int right, int bottom) {
        LinearLayout frame = (LinearLayout) this.view.findViewById(R.id.frame);
        FrameLayout.LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        l.setMargins(left, top, right, bottom);
        frame.setLayoutParams(l);
    }
}
