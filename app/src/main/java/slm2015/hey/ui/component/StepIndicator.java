package slm2015.hey.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import slm2015.hey.R;

public class StepIndicator extends FrameLayout{
    private TextView indicateTextView;
    private ImageView indicateHeadImageView;
    private Boolean active;
    private String indicateText;

    public StepIndicator(Context context) {
        super(context);
        this.init(null, 0);
    }

    public StepIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0);
    }

    public StepIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(this.getContext(), R.layout.step_indicator, this);
        this.findview();
        this.loadAttribute(attrs, defStyle);
    }

    private void loadAttribute(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StepIndicator, defStyle, 0);
        this.setActive(a.getBoolean(R.styleable.StepIndicator_active, false));
        this.setIndicateText(a.getString(R.styleable.StepIndicator_text));
        a.recycle();
    }

    private void findview() {
        this.indicateTextView = (TextView) findViewById(R.id.step_indicator_text);
        this.indicateHeadImageView = (ImageView) findViewById(R.id.step_indicator_head);
    }

    public void setActive(Boolean active) {
        this.active = active;
        int color = active ? R.color.blue_green : R.color.peach_red;
        int source = active ? R.drawable.arrow_head : R.drawable.arrow_head_unactive;
        this.indicateTextView.setBackgroundResource(color);
        this.indicateHeadImageView.setImageResource(source);
    }

    public void setIndicateText(String indicateText) {
        this.indicateText = indicateText;
        this.indicateTextView.setText(indicateText);
    }
}
