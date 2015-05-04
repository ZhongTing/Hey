package slm2015.hey.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private final List<MainPagerFragment> fragments;

    public MainPagerAdapter(FragmentManager supportFragmentManager, List<MainPagerFragment> fragments) {
        super(supportFragmentManager);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getPageIconResId(int position) {
        return this.fragments.get(position).getPageIconRedId();
    }
}
