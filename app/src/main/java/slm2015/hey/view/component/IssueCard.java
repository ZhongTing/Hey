package slm2015.hey.view.component;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class IssueCard {
    private final TextView subjectTextView;
    private final TextView descriptionTextView;
    private final TextView positionTextView;
    private final ImageView incognitoImageView;
    private final TextView timestampTextView;
    private final ImageView imageView;

    private Issue issue;
    private FrameLayout cardView;

    public IssueCard(Context context, ViewGroup parent, final ViewPager pager, Issue issue) {
        this.issue = issue;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutId = this.issue.hasImage() ? R.layout.card : R.layout.card_no_pic;
        this.cardView = (FrameLayout) inflater.inflate(layoutId, parent, false);
        this.cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pager.requestDisallowInterceptTouchEvent(true);
                return true;
            }
        });

        this.subjectTextView = (TextView) this.cardView.findViewById(R.id.title);
        this.descriptionTextView = (TextView) this.cardView.findViewById(R.id.description);
        this.positionTextView = (TextView) this.cardView.findViewById(R.id.location);
        this.incognitoImageView = (ImageView) this.cardView.findViewById(R.id.incognito);
        this.timestampTextView = (TextView) this.cardView.findViewById(R.id.timestampTextView);
        this.imageView = (ImageView) this.cardView.findViewById(R.id.image);

        this.init();
    }

    private void init() {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (this.issue.getImage() != null) {
            this.imageView.setImageBitmap(this.issue.getImage());
        }
        if (this.issue.getPhotoURL() != null) {
            ImageLoader.getInstance().displayImage(this.issue.getPhotoURL(), this.imageView);
        }

        this.subjectTextView.setText(this.issue.getSubject());
        this.subjectTextView.postInvalidate();

        this.descriptionTextView.setText(this.issue.getDescription());
        this.descriptionTextView.postInvalidate();

        this.positionTextView.setText(this.issue.getPlace());
        this.positionTextView.postInvalidate();

        this.timestampTextView.setText(this.issue.getTimestamp());
        this.timestampTextView.postInvalidate();
    }

    public View getView() {
        return this.cardView;
    }
}
