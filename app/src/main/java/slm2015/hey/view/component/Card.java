package slm2015.hey.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.util.UiUtility;

public class Card extends FrameLayout {
    private final int CARD_MARGIN_TOP = 70;

    private TextView subjectTextView;
    private TextView descriptionTextView;
    private TextView positionTextView;
    private ImageView imageView;
    private Issue issue;
    private View view;
    private Button imagePass;
    private Button imageLike;
    private ImageView incognitoImageView;
    private TextView timestampTextView;
    private int tagWidth, tagHeight;

    private boolean incognito = false;

    public Card(Context context) {
        super(context);
        this.init();
        this.tagWidth = UiUtility.dpiToPixel(100, getResources());
        this.tagHeight = UiUtility.dpiToPixel(50, getResources());
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
        int layoutId = this.hasImage() ? R.layout.card_no_pic : R.layout.card;
        this.removeAllViews();
        setRotation(0);
        this.view = inflate(this.getContext(), layoutId, this);
        this.subjectTextView = (TextView) this.view.findViewById(R.id.title);
        this.descriptionTextView = (TextView) this.view.findViewById(R.id.description);
        this.positionTextView = (TextView) this.view.findViewById(R.id.location);
        this.incognitoImageView = (ImageView) this.view.findViewById(R.id.incognito);
        this.timestampTextView = (TextView) this.view.findViewById(R.id.timestampTextView);
        this.imageView = (ImageView) this.view.findViewById(R.id.image);

        if (issue.getImage() != null) {
            this.imageView.setImageBitmap(issue.getImage());
        }
        if (issue.getPhotoURL() != null) {
            ImageLoader.getInstance().loadImage(issue.getPhotoURL(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            });
        }
        this.findView(this.view, issue);
        this.bindEvent();
    }

    private boolean hasImage() {
        return this.issue.getImage() == null && this.issue.getPhotoURL() == null;
    }

    public void initialImageLike() {
        final Button imageLike = new Button(this.getContext());
        imageLike.setLayoutParams(new LinearLayout.LayoutParams(this.tagWidth, this.tagHeight));
        imageLike.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.like));
        imageLike.setX(0);
        imageLike.setY(UiUtility.dpiToPixel(110, getResources()));
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
        imagePass.setLayoutParams(new LinearLayout.LayoutParams(this.tagWidth, this.tagHeight));
        imagePass.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.soso));

        imagePass.setX(UiUtility.dpiToPixel(210, getResources()));
        imagePass.setY(UiUtility.dpiToPixel(110, getResources()));
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
        this.imageView.setImageBitmap(bitmap);
    }

    private void findView(View view, Issue issue) {
        this.subjectTextView.setText(issue.getSubject());
        this.descriptionTextView.setText(issue.getDescription());
        this.positionTextView.setText(issue.getPlace());
        this.subjectTextView.postInvalidate();
        this.descriptionTextView.postInvalidate();
        this.positionTextView.postInvalidate();
        this.timestampTextView.setText(issue.getTimestamp());
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

    public void setIncognito(boolean incognito) {
        this.incognito = incognito;
        float alpha = incognito ? 0.7f : 1f;
        setAlpha(alpha);
        int visible = incognito ? View.VISIBLE : View.INVISIBLE;
        this.incognitoImageView.setVisibility(visible);
    }
}
