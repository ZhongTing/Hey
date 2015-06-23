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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class IssueCard extends FrameLayout {
    private TextView subjectTextView;
    private TextView descriptionTextView;
    private TextView positionTextView;
    private ImageView incognitoImageView;
    private TextView timestampTextView;
    private ImageView imageView;
    private ImageView likeImageView, sosoImageView;
    private LinearLayout likeLayout;
    private TextView likeCountTextView;

    private Context context;
    private Issue issue;

    public IssueCard(Context context) {
        super(context);
        this.context = context;
    }

    public void assignIssueCard(ViewGroup parent, final ViewPager pager, Issue issue) {
        this.issue = issue;

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutId = this.issue.hasImage() ? R.layout.card : R.layout.card_no_pic;
        FrameLayout cardView = (FrameLayout) inflater.inflate(layoutId, parent, false);
        FrameLayout.LayoutParams rootViewLayoutParams = (LayoutParams) cardView.getLayoutParams();

        cardView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.addView(cardView);
        this.setLayoutParams(rootViewLayoutParams);

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pager != null)
                    pager.requestDisallowInterceptTouchEvent(true);
                return true;
            }
        });

        this.subjectTextView = (TextView) this.findViewById(R.id.title);
        this.descriptionTextView = (TextView) this.findViewById(R.id.description);
        this.positionTextView = (TextView) this.findViewById(R.id.location);
        this.incognitoImageView = (ImageView) this.findViewById(R.id.incognito);
        this.timestampTextView = (TextView) this.findViewById(R.id.timestampTextView);
        this.imageView = (ImageView) this.findViewById(R.id.image);
        this.incognitoImageView = (ImageView) this.findViewById(R.id.incognito);
        this.likeLayout = (LinearLayout) this.findViewById(R.id.like_layout);
        this.likeCountTextView = (TextView) this.findViewById(R.id.like_count);
        this.likeImageView = (ImageView) this.findViewById(R.id.like_image_view);
        this.sosoImageView = (ImageView) this.findViewById(R.id.soso_image_view);

        this.init();
    }

    private void init() {
        if (this.issue.getImage() != null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            this.imageView.setImageBitmap(this.issue.getImage());
        }
        if (this.issue.getPhotoURL() != null && this.imageView != null) {
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

        this.likeCountTextView.setText(String.valueOf(this.issue.getLikeCount()));
        this.likeCountTextView.postInvalidate();

        this.hideLikeAndSoSo();
        this.hideIncognito();
    }

    public void hideLikeCount() {
        this.likeLayout.setVisibility(INVISIBLE);
    }

    public void showIncognito() {
        this.setAlpha(0.7f);
        this.incognitoImageView.setVisibility(VISIBLE);
    }

    public void hideIncognito() {
        this.setAlpha(1.0f);
        this.incognitoImageView.setVisibility(INVISIBLE);
    }

    public void hideLikeAndSoSo() {
        this.likeImageView.setVisibility(INVISIBLE);
        this.sosoImageView.setVisibility(INVISIBLE);
    }

    public void showLike() {
        this.sosoImageView.setVisibility(INVISIBLE);
        this.likeImageView.setVisibility(VISIBLE);
    }

    public void showSoSo() {
        this.sosoImageView.setVisibility(VISIBLE);
        this.likeImageView.setVisibility(INVISIBLE);
    }
}
