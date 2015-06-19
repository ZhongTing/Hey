package slm2015.hey.view.tabs.watch;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

import slm2015.hey.R;
import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.Selector;
import slm2015.hey.view.component.MyListView;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.util.UiUtility;

public class WatchHistoryFragment extends TabPagerFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, Observer {
    private final int ALPHA_DURATION = 300;
    private final int HIDE = 1;
    private final int SHOW = 2;

    private View changeViewButton;
    private View optionButton;
    private FragmentManager fragmentManager;
    private MyListView issueListView;
    private HistoryIssueAdapter adapter;
    private SwipeRefreshLayout laySwipe;
    private ViewPager pager;
    private IssueLoader issueLoader;
    private ArrayList<Selector> selectors;
    private float y = 0;

    static public WatchHistoryFragment newInstance(FragmentManager fragmentManager, ViewPager pager, IssueLoader issueLoader, ArrayList<Selector> selectors) {
        WatchHistoryFragment fragment = new WatchHistoryFragment();
        fragment.setFragmentManager(fragmentManager);
        fragment.setPager(pager);
        fragment.setIssueLoader(issueLoader);
        fragment.setSelectors(selectors);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watch_history, container, false);
        this.init(view);
        return view;
    }

    private void init(View view) {
        this.adapter = new HistoryIssueAdapter(this.issueLoader.getHistoryIssues());
        onFilterChange();
        initialChangeViewButton(view);
        initialLaySwipe(view);
        initialListView(view);
        initialOptionButton(view);
    }

    private void initialLaySwipe(View view) {
        this.laySwipe = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
        laySwipe.setOnRefreshListener(this);
    }

    private void initialListView(View view) {
        this.issueListView = (MyListView) view.findViewById(R.id.issue_listview);
        this.issueListView.setAdapter(this.adapter);

//        this.issueListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                pager.requestDisallowInterceptTouchEvent(true);
//                return true;
//            }
//        })
        View indicatorView = view.findViewById(R.id.scroll_bar_indicator);
        this.issueListView.setIndicatorView(indicatorView);
        this.issueListView.setOnScrollListener(this);
    }


    private void initialChangeViewButton(View view) {
        this.changeViewButton = view.findViewById(R.id.changeViewButton);
        this.changeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToListView();
            }
        });
        this.y = changeViewButton.getY();
    }

    private void initialOptionButton(View view) {
        this.optionButton = view.findViewById(R.id.optionButton);
        View filterBar = view.findViewById(R.id.filter_bar);
        filterBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), optionButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                // Force icons to show
                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popup);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);

                } catch (Exception e) {
                    Log.w("pop", "error forcing menu icons to show", e);
                    popup.show();
                    return;
                }

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(
                                getActivity().getApplicationContext(),
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    public void setIssueLoader(IssueLoader issueLoader) {
        this.issueLoader = issueLoader;
        this.issueLoader.addObserver(this);
    }

    public void setSelectors(ArrayList<Selector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public int getPageIconRedId() {
        return R.drawable.funny_watch;
    }

    @Override
    public void FragmentSelected() {

    }

    private void changeToListView() {
        this.fragmentManager.popBackStack(WatchFragment.SELF_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onRefresh() {
        //todo implement reload like WatchFragment
        this.laySwipe.setRefreshing(true);
        this.issueLoader.loadNewIssues();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_TOUCH_SCROLL:
                hideChangeViewButton(HIDE);
                break;
            case 0:
                hideChangeViewButton(SHOW);
                break;
        }
    }

    private void hideChangeViewButton(int state) {
        final float BUTTON_SCROLL_ALPHA = 0.2f;
        final float BUTTON_NOT_SCROLL_ALPHA = 1f;
        final int ini_y = UiUtility.dpiToPixel(0, Resources.getSystem());
        final int translationY = UiUtility.dpiToPixel(70, Resources.getSystem());

        AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator anim = null;
        Animator alphaChange = null;
        switch (state) {
            case SHOW:
                alphaChange = ObjectAnimator.ofFloat(this.changeViewButton, "alpha", BUTTON_SCROLL_ALPHA, BUTTON_NOT_SCROLL_ALPHA);
                anim = ObjectAnimator.ofFloat(this.changeViewButton, "translationY", translationY, ini_y);
                break;
            case HIDE:
                alphaChange = ObjectAnimator.ofFloat(this.changeViewButton, "alpha", BUTTON_NOT_SCROLL_ALPHA, BUTTON_SCROLL_ALPHA);
                anim = ObjectAnimator.ofFloat(this.changeViewButton, "translationY", ini_y, translationY);
                break;
        }
        animationSet.playTogether(alphaChange, anim);
        animationSet.setDuration(ALPHA_DURATION);
        animationSet.start();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.pager.requestDisallowInterceptTouchEvent(true);
        boolean enable = false;
        if (issueListView != null && issueListView.getChildCount() > 0) {
            boolean firstItemVisible = issueListView.getFirstVisiblePosition() == 0;
            boolean topOfFirstItemVisible = issueListView.getChildAt(0).getTop() == 0;
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        this.laySwipe.setEnabled(enable);
    }

    @Override
    public void onLoaderChanged() {
        this.laySwipe.setRefreshing(false);

        this.adapter.setList();
        this.adapter.setFilter(this.selectors);
//        this.adapter.setIssueList(this.issueLoader.getHistoryIssues());
    }

    public void onFilterChange() {
        this.adapter.setFilter(this.selectors);
    }

    //fix bug
    //java.IllegalStateException error, No Activity, only when navigating to Fragment for the SECOND time
    //http://stackoverflow.com/questions/14929907/causing-a-java-illegalstateexception-error-no-activity-only-when-navigating-to
    private static final Field sChildFragmentManagerField;
    private static final String LOG_TAG = "WatchHistoryFragment";

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(LOG_TAG, "Error getting mChildFragmentManager field", e);
        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error setting mChildFragmentManager field", e);
            }
        }
    }
}
