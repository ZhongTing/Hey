package slm2015.hey.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class Card extends FrameLayout {

    private TextView subjectTextView;
    private TextView descriptionTextView;
    private TextView positionTextView;
    private ImageView image;
    private Issue issue;

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
        this.issue = issue;
        int layoutId = this.issue.getImage() == null ? R.layout.card_no_pic : R.layout.card;
        View view = inflate(this.getContext(), layoutId, this);
        this.findView(view, issue);
        this.bindEvent();
    }

    private void findView(View view, Issue issue) {
        if (this.issue.getImage() != null) {
            this.subjectTextView = (TextView) view.findViewById(R.id.title1);
            this.subjectTextView.setText(issue.getSubject());
            this.descriptionTextView = (TextView) view.findViewById(R.id.description1);
            this.descriptionTextView.setText(issue.getDescription());
            this.positionTextView = (TextView) view.findViewById(R.id.location1);
            this.positionTextView.setText(issue.getPosition());
            this.image = (ImageView) view.findViewById(R.id.image1);
            this.image.setImageBitmap(issue.getImage());
        }else{
            this.subjectTextView = (TextView) view.findViewById(R.id.title);
            this.subjectTextView.setText(issue.getSubject());
            this.descriptionTextView = (TextView) view.findViewById(R.id.description);
            this.descriptionTextView.setText(issue.getDescription());
            this.positionTextView = (TextView) view.findViewById(R.id.location);
            this.positionTextView.setText(issue.getPosition());
        }
    }

    private void bindEvent() {
        //todo bind event here
    }
}
