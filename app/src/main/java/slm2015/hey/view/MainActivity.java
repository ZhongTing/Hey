package slm2015.hey.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;
import java.util.Vector;

import slm2015.hey.R;
import slm2015.hey.core.issue.IssueHandler;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.tabsswipe.NewFunnyWatchFragment;

public class MainActivity extends FragmentActivity {
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_hey_main);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);


        this.fragments = new Vector<Fragment>();
        this.fragments.add(NewFunnyWatchFragment.newInstance(pager));
        this.fragments.add(NewFunnyWatchFragment.newInstance(pager));

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        new IssueHandler(this).raise(new Issue());
    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
        private final int[] ICON_RES_IDS = {R.layout.new_funnypo_layout, R.layout.new_funnywatch_layout};


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getPageIconResId(int position) {
            return ICON_RES_IDS[position];
        }
    }
}