package slm2015.hey.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class Card extends FrameLayout{

    private TextView subjectTextView;
    private TextView descriptionTextView;
    private TextView positionTextView;

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
            inflate(this.getContext(), R.layout.card, this);
        }

        //note that the actual view will generate when issue is assigned
    }

    public void assignIssue(Issue issue) {
        int layoutId = issue.getImage() == null ? R.layout.card_no_pic : R.layout.card;
        inflate(this.getContext(), layoutId, this);
        this.findView();
        this.bindEvent();
    }

    private void findView() {
        //todo find private ui members and assign them
    }

    private void bindEvent() {
        //todo bind event here
    }
}
