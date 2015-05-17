package slm2015.hey.view.component;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class WizardAdaptor extends FragmentPagerAdapter {
    private int count;

    public WizardAdaptor(FragmentManager fm) {
        super(fm);
        count = 1;
    }

    public abstract String getStepIndicateText(int stepIndex);

    public abstract int getActualCount();

    @Override
    public int getCount() {
        return count;
    }

    public void notifyNewCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    };
}
