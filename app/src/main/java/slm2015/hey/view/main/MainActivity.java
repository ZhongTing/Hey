package slm2015.hey.view.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.navdrawer.SimpleSideDrawer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.core.gcm.RegistrationIntentService;
import slm2015.hey.entity.Selector;
import slm2015.hey.util.LocalPreference;
import slm2015.hey.view.selector.AddSelectorActivity;
import slm2015.hey.view.selector.SelectorAdapter;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.tabs.post.PostFragment;
import slm2015.hey.view.tabs.watch.WatchFragment;
import slm2015.hey.view.util.UiUtility;

public class MainActivity extends FragmentActivity implements SelectorAdapter.OnSelectorChangeListener{
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private final int WATCH_FRAGMENT = 0;
    private final int ADD_SELECTOR = 1;
    private List<TabPagerFragment> fragments;
    private ImageButton slidingMenuButton;
    private ImageButton addSelectorButton;
    private SimpleSideDrawer mSlidingMenu;
    private SelectorAdapter selectorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        initialSlidingMenuButton();
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getBaseContext()).build());
        LocalPreference.init(this);

        this.fragments = new ArrayList<>();
        this.fragments.add(WatchFragment.newInstance(pager));
        this.fragments.add(PostFragment.newInstance(pager));

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

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
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
        this.selectorAdapter = new SelectorAdapter();
        this.selectorAdapter.setOnSelectorChangeListener(this);
        selectorListView.setAdapter(this.selectorAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SELECTOR) {
            if (resultCode == Activity.RESULT_OK)
                AddSelector(new Selector(data.getStringExtra("selector")));
        }
    }

    private void AddSelector(Selector selector) {
        WatchFragment fragment = (WatchFragment) this.fragments.get(WATCH_FRAGMENT);
        fragment.addSelector(selector);
        this.selectorAdapter.addSelector(selector);
    }

    @Override
     public void OnFilterChange() {
        WatchFragment fragment = (WatchFragment) this.fragments.get(WATCH_FRAGMENT);
        fragment.onFilterChange();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}