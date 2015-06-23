package slm2015.hey.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.navdrawer.SimpleSideDrawer;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.core.selector.SelectorHandler;
import slm2015.hey.entity.Selector;
import slm2015.hey.view.selector.AddSelectorActivity;
import slm2015.hey.view.selector.SelectorAdapter;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.tabs.post.PostFragment;
import slm2015.hey.view.tabs.watch.WatchFragment;
import slm2015.hey.view.util.UiUtility;

public class MainActivity extends FragmentActivity implements SelectorAdapter.OnSelectorChangeListener {
    private static final String TAG = "MainActivity";
    private final int WATCH_FRAGMENT = 0;
    private final int ADD_SELECTOR = 1;
    private List<TabPagerFragment> fragments;
    private ImageButton slidingMenuButton;
    private ImageButton addSelectorButton;
    private SimpleSideDrawer mSlidingMenu;
    private SelectorAdapter selectorAdapter;
    private SelectorHandler selectorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.selectorHandler = new SelectorHandler(this);
        this.initialTabs();
        this.initialSlideMenu();
    }

    private void initialTabs() {
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

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

    private void initialSlideMenu() {
        this.mSlidingMenu = new SimpleSideDrawer(this);
        this.mSlidingMenu.setLeftBehindContentView(R.layout.sliding_menu);
        this.mSlidingMenu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                }
            }
        });
        initialAddSelectorButton();
        initialSlidingListView();
        initialSlidingMenuButton();
    }

    private void initialAddSelectorButton() {
        this.addSelectorButton = (ImageButton) this.mSlidingMenu.findViewById(R.id.add_selector_button);
        this.addSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddSelectorActivity.class);
                startActivityForResult(intent, ADD_SELECTOR);
            }
        });
        RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) this.addSelectorButton.getLayoutParams());
        params.setMargins(0, 0, 0, UiUtility.getNavBarHeight(getBaseContext()));
        this.addSelectorButton.setLayoutParams(params);
    }

    private void initialSlidingMenuButton() {
        slidingMenuButton = (ImageButton) findViewById(R.id.sliding_button);
        slidingMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingMenu.toggleLeftDrawer();
            }
        });
    }

    private void initialSlidingListView() {
        ListView selectorListView = (ListView) findViewById(R.id.selectorListView);
        TextView hint = (TextView) findViewById(R.id.no_selector_hint);
        this.selectorAdapter = new SelectorAdapter(this, hint);
        this.selectorAdapter.setOnSelectorChangeListener(this);
        selectorListView.setAdapter(this.selectorAdapter);
        this.selectorHandler.listSelector(new SelectorHandler.ListSelectorCallBack() {
            @Override
            public void onReceiveFilterList(List<Selector> selectorList) {
                for (Selector selector : selectorList)
                    MainActivity.this.selectorAdapter.addSelector(selector);
            }
        });

        WatchFragment fragment = (WatchFragment) this.fragments.get(WATCH_FRAGMENT);
        fragment.setSelectors(this.selectorAdapter.getSelectorList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SELECTOR) {
            if (resultCode == Activity.RESULT_OK) {
                Selector selector = new Selector(data.getStringExtra("selector"));
                selector.setId(data.getIntExtra("id", -1));
                Assert.assertFalse(selector.getId() == -1);
                AddSelector(selector);
            }
        }
    }

    private void AddSelector(Selector selector) {
        this.selectorAdapter.addSelector(selector);
    }

    @Override
    public void OnSelectorChange() {
        WatchFragment fragment = (WatchFragment) this.fragments.get(WATCH_FRAGMENT);
        fragment.onSelectorChange();
    }


}