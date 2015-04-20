package slm2015.hey.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import slm2015.hey.R;

public class StepIndicator extends FrameLayout{
    private TextView indicateTextView;

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

        this.indicateTextView.setText(a.getString(R.styleable.StepIndicator_text));
        a.recycle();
    }

    private void findview() {
        this.indicateTextView = (TextView) findViewById(R.id.step_indicator_text);
    }
}
