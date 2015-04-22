package slm2015.hey.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.Stack;

import slm2015.hey.R;

public class Wizard extends FrameLayout {
    private ViewGroup indicatorGroup;
    private Stack<StepIndicator> stepIndicatorStack = new Stack<>();
    private static int stepIndicatorBaseId = 1000;

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
        this.findViews();
        this.addStep("主角");
        this.addStep("描述");
        this.addStep("地點");
        this.addStep("預覽");
        this.setCurrentStep(2);
    }

    private void findViews() {
        this.indicatorGroup = (ViewGroup) this.findViewById(R.id.step_indicator_group);
        this.indicatorGroup.removeAllViews();
    }

    public void addStep(String indicateText) {
        //crate step
        StepIndicator stepIndicator = new StepIndicator(getContext());
        stepIndicator.setId(stepIndicatorStack.size() + Wizard.stepIndicatorBaseId);
        stepIndicator.setIndicateText(indicateText);
        stepIndicator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentStep(stepIndicatorStack.indexOf(v) + 1);
            }
        });

        //set layout params
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(-stepIndicator.getIndicatorHeadWidthInPixel(), 0, 0, 0);
        if (!this.stepIndicatorStack.isEmpty()) {
            params.addRule(RelativeLayout.RIGHT_OF, this.stepIndicatorStack.lastElement().getId());
        }

        this.stepIndicatorStack.push(stepIndicator);
        this.indicatorGroup.addView(stepIndicator, params);

        //bringToFront implement z order
        for (int i = this.stepIndicatorStack.size() - 1; i >= 0; i--) {
            this.stepIndicatorStack.get(i).bringToFront();
        }
    }

    private void setCurrentStep(final int step) {
        int totalSteps = this.stepIndicatorStack.size();
        int totalStepWidth = 0;
        if (step > totalSteps) {
            return;
        }

        //set active
        for (int i = 0; i < totalSteps; i++) {
            boolean active = i < step;
            StepIndicator stepIndicator = this.stepIndicatorStack.get(i);
            stepIndicator.setActive(active);
            stepIndicator.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (i != step - 1) {
                totalStepWidth += stepIndicator.getMeasuredWidth();
            }
        }

        //compute width
        final StepIndicator currentStep = stepIndicatorStack.get(step - 1);
        final int calculateWidth = totalStepWidth - currentStep.getIndicatorHeadWidthInPixel() * totalSteps;
        this.indicatorGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicatorGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int fullWidth = indicatorGroup.getMeasuredWidth();
                for (StepIndicator stepIndicator : stepIndicatorStack) {
                    stepIndicator.resetWidth();
                }
                currentStep.setActiveWidth(fullWidth - calculateWidth);
                indicatorGroup.requestLayout();
            }
        });
        this.indicatorGroup.requestLayout();
    }
}
