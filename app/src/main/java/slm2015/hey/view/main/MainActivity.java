package slm2015.hey.view.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.astuetz.PagerSlidingTabStrip;
import com.navdrawer.SimpleSideDrawer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.util.LocalPreference;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.tabs.post.PostFragment;
import slm2015.hey.view.tabs.watch.WatchFragment;

public class MainActivity extends FragmentActivity {
    private List<TabPagerFragment> fragments;
    private ImageButton slidingMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SimpleSideDrawer mSlidingMenu = new SimpleSideDrawer( this );
        mSlidingMenu.setLeftBehindContentView( R.layout.sliding_menu );

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        slidingMenuButton = (ImageButton) findViewById(R.id.sliding_button);
        slidingMenuButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSlidingMenu.toggleLeftDrawer();
                return true;
            }
        });
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getBaseContext()).build());
        LocalPreference.init(this);

        this.fragments = new ArrayList<>();
        this.fragments.add(WatchFragment.newInstance(pager));
        this.fragments.add(PostFragment.newInstance(pager));

        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.fragments.get(position).FragmentSelected();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setViewPager(pager);
    }
}