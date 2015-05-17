package slm2015.hey.ui.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.util.UiUtility;

public class Card extends FrameLayout {
    private final int CARD_MARGIN_TOP = 70;

    private TextView subjectTextView;
    private TextView descriptionTextView;
    private TextView positionTextView;
    private ImageView image;
    private Issue issue;
    private View view;
    private Button imagePass;
    private Button imageLike;

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

    public Button getImageLike() {
        return imageLike;
    }

    public void setImageLike(Button imageLike) {
        this.imageLike = imageLike;
    }

    public Button getImagePass() {

        return imagePass;
    }

    public void setImagePass(Button imagePass) {
        this.imagePass = imagePass;
    }

    private void init() {
        //preview in edit mode;
        if (isInEditMode()) {
            inflate(this.getContext(), R.layout.card, this);
        }
    }

    public void initDefaultMargin() {
        int marginTop = UiUtility.dpiToPixel(CARD_MARGIN_TOP, getResources());
        int others = UiUtility.dpiToPixel(0, getResources());
        this.setMargin(others, marginTop, others, others);
    }

    public void assignIssue(Issue issue) {
        this.issue = issue;
        int layoutId = this.issue.getImage() == null ? R.layout.card_no_pic : R.layout.card;
        this.removeAllViews();
        setRotation(0);
        this.view = inflate(this.getContext(), layoutId, this);
        this.subjectTextView = (TextView) this.view.findViewById(R.id.title);
        this.descriptionTextView = (TextView) this.view.findViewById(R.id.description);
        this.positionTextView = (TextView) this.view.findViewById(R.id.location);
        if (issue.getImage() != null) {
            this.image = (ImageView) this.view.findViewById(R.id.image);
            this.image.setImageBitmap(issue.getImage());
        }
        this.findView(this.view, issue);
        this.bindEvent();
    }

    public void initialImageLike() {
        final Button imageLike = new Button(this.getContext());
        imageLike.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
        imageLike.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.like));
        imageLike.setX(this.getX());
        imageLike.setY(this.getY() + UiUtility.dpiToPixel(110, getResources()));
        imageLike.setAlpha(0);
        imageLike.setRotation(-45);
        imageLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //your like action code write here
            }
        });
        this.addView(imageLike);
        this.setImageLike(imageLike);
    }

    public void initialImagePass() {
        final Button imagePass = new Button(this.getContext());
        imagePass.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
        imagePass.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.soso));

        imagePass.setX(this.getX() + UiUtility.dpiToPixel(240, getResources()));
        imagePass.setY(this.getY() + UiUtility.dpiToPixel(110, getResources()));
        imagePass.setRotation(45);
        imagePass.setAlpha(0);
        imagePass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //your pass action code write here
            }
        });
        this.addView(imagePass);
        this.setImagePass(imagePass);
    }

    public Bitmap getImage() {
        return this.issue.getImage();
    }

    public void setImage(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
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

    public Issue getIssue() {
        return issue;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        LinearLayout frame = (LinearLayout) this.view.findViewById(R.id.frame);
        FrameLayout.LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        l.setMargins(left, top, right, bottom);
        frame.setLayoutParams(l);
    }
}
