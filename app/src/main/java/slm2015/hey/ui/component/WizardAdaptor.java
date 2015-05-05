package slm2015.hey.ui.component;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class WizardAdaptor extends FragmentPagerAdapter {
    public WizardAdaptor(FragmentManager fm) {
        super(fm);
    }

    public abstract String getStepIndicateText(int stepIndex);
}
