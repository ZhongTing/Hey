package slm2015.hey.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import slm2015.hey.R;

public class StepIndicator extends FrameLayout {
    private TextView indicateTextView;
    private ImageView indicateHeadImageView;
    private String indicateText;
    private int originWidth;

    public enum Status {
        DONE,
        CURRENT,
        INACTIVE;
    }

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
        this.originWidth = LayoutParams.WRAP_CONTENT;
    }

    private void loadAttribute(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StepIndicator, defStyle, 0);
        Status status = Status.values()[a.getInt(R.styleable.StepIndicator_status, 0)];
        this.setStatus(status);
        this.setIndicateText(a.getString(R.styleable.StepIndicator_text));
        a.recycle();
    }

    private void findview() {
        this.indicateTextView = (TextView) findViewById(R.id.step_indicator_text);
        this.indicateHeadImageView = (ImageView) findViewById(R.id.step_indicator_head);
    }

    public void setStatus(Status status) {
        int color;
        int source;
        switch (status) {
            case INACTIVE:
                color = R.color.wizard_step_inactive;
                source = R.drawable.arrow_head_inactive;
                break;
            case CURRENT:
                color = R.color.wizard_step_current;
                source = R.drawable.arrow_head_current;
                break;
            case DONE:
            default:
                color = R.color.wizard_step_done;
                source = R.drawable.arrow_head_done;
                break;
        }
        this.indicateTextView.setBackgroundResource(color);
        this.indicateHeadImageView.setImageResource(source);
        this.indicateTextView.requestLayout();
    }

    public void setIndicateText(String indicateText) {
        this.indicateText = indicateText;
        this.indicateTextView.setText(indicateText);
    }

    public int getIndicatorHeadWidthInPixel() {
        return this.indicateHeadImageView.getLayoutParams().width;
    }

    public void setActiveWidth(int width) {
        this.getLayoutParams().width = width;
    }

    public void resetWidth() {
        this.getLayoutParams().width = this.originWidth;
    }
}
