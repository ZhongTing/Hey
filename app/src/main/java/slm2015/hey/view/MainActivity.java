package slm2015.hey.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.ui.tabsswipe.NewFunnyWatchFragment;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        List<MainPagerFragment> fragments = new ArrayList<>();
        fragments.add(new FunnyPostFragment());
        fragments.add(new NewFunnyWatchFragment(pager));

        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        tabs.setViewPager(pager);
    }
}