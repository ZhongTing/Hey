package slm2015.hey.view.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.ui.tabsswipe.NewFunnyWatchFragment;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.tabs.post.PostFragment;
import slm2015.hey.view.tabs.watch.WatchFragment;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        List<TabPagerFragment> fragments = new ArrayList<>();
        fragments.add(new NewFunnyWatchFragment(pager));
        fragments.add(new PostFragment());
        fragments.add(new WatchFragment());

        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        tabs.setViewPager(pager);
    }
}