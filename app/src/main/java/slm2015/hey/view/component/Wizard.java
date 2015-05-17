package slm2015.hey.view.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
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
    private ViewPager viewPager;
    private WizardAdaptor adaptor;
    private int currentStep = 1;
    private int currentStepBound = 1;

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
        this.bindEvents();
        if (isInEditMode()) {
            this.addStep("Step1");
            this.addStep("Step2");
            this.addStep("Step3");
            this.addStep("Step4");
            this.setCurrentStep(2);
        }
    }

    private void findViews() {
        this.indicatorGroup = (ViewGroup) this.findViewById(R.id.step_indicator_group);
        this.indicatorGroup.removeAllViews();
        this.viewPager = (ViewPager) this.findViewById(R.id.wizard_viewpager);
    }


    private void bindEvents() {
        this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setCurrentStep(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setAdaptor(WizardAdaptor wizardAdaptor) {
        this.adaptor = wizardAdaptor;
        this.viewPager.setAdapter(this.adaptor);
        this.stepIndicatorStack.clear();
        for (int i = 0; i < wizardAdaptor.getActualCount(); i++) {
            this.addStep(wizardAdaptor.getStepIndicateText(i));
        }
        setCurrentStep(1);
    }

    private void addStep(String indicateText) {
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

    //step range from 0 to n - 1
    private void setCurrentStep(final int step) {
        int totalSteps = this.stepIndicatorStack.size();
        int totalStepWidth = 0;
        if (step > this.currentStepBound || step > totalSteps || step < 1) {
            return;
        }

        this.currentStep = step;
        ((WizardAdaptor) this.viewPager.getAdapter()).notifyNewCount(step);
        this.viewPager.setCurrentItem(step - 1, true);

        //set active
        for (int i = 0; i < totalSteps; i++) {
            StepIndicator.Status status = i < step ? StepIndicator.Status.DONE : StepIndicator.Status.INACTIVE;
            status = i == step - 1 ? StepIndicator.Status.CURRENT : status;
            StepIndicator stepIndicator = this.stepIndicatorStack.get(i);
            stepIndicator.setStatus(status);
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

    public void next() {
        int nextStep = this.currentStep + 1;
        if (nextStep > this.currentStepBound) {
            this.currentStepBound = nextStep;
        }
        this.setCurrentStep(nextStep);
    }

    public void back() {
        this.setCurrentStep(this.currentStep - 1);
    }
}
