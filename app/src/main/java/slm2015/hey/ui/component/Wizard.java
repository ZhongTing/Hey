package slm2015.hey.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import slm2015.hey.R;

public class Wizard extends FrameLayout {
    public Wizard(Context context) {
        super(context);
        this.init();
    }

    public Wizard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public Wizard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        inflate(this.getContext(), R.layout.wizard, this);
    }
}
