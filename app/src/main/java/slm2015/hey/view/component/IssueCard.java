package slm2015.hey.view.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
    private Context context;
    private FrameLayout cardView;

    public IssueCard(Context context, ViewGroup parent, Issue issue) {
        this.context = context;
        this.issue = issue;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutId = this.hasImage() ? R.layout.card : R.layout.card_no_pic;
        this.cardView = (FrameLayout) inflater.inflate(layoutId, parent, false);

        this.subjectTextView = (TextView) this.cardView.findViewById(R.id.title);
        this.descriptionTextView = (TextView) this.cardView.findViewById(R.id.description);
        this.positionTextView = (TextView) this.cardView.findViewById(R.id.location);
        this.incognitoImageView = (ImageView) this.cardView.findViewById(R.id.incognito);
        this.timestampTextView = (TextView) this.cardView.findViewById(R.id.timestampTextView);
        this.imageView = (ImageView) this.cardView.findViewById(R.id.image);

        this.init();
    }

    private void init() {
        if (this.issue.getImage() != null) {
            this.imageView.setImageBitmap(this.issue.getImage());
        }
        if (this.issue.getPhotoURL() != null) {
            ImageLoader.getInstance().loadImage(this.issue.getPhotoURL(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            });
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

    private boolean hasImage() {
        return !(this.issue.getImage() == null || this.issue.getPhotoURL() == null);
    }
}
