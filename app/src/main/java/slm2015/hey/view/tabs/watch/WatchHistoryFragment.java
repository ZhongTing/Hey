package slm2015.hey.view.tabs.watch;

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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.lang.reflect.Field;

import slm2015.hey.R;
import slm2015.hey.view.tabs.TabPagerFragment;

public class WatchHistoryFragment extends TabPagerFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, WatchManager.OnReloaded {

    private View changeViewButton;
    private View optionButton;
    private FragmentManager fragmentManager;
    private ListView issueListView;
    private HistoryIssueAdapter adapter;
    private SwipeRefreshLayout laySwipe;
    private ViewPager pager;
    private WatchManager watchManager;

    static public WatchHistoryFragment newInstance(FragmentManager fragmentManager, ViewPager pager, WatchManager watchManager) {
        WatchHistoryFragment fragment = new WatchHistoryFragment();
        fragment.setFragmentManager(fragmentManager);
        fragment.setPager(pager);
        fragment.setWatchManager(watchManager);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watch_listview_layout, container, false);
        this.init(view);
        return view;
    }

    private void init(View view) {
        this.adapter = new HistoryIssueAdapter(this.watchManager.getHistory());
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
        this.issueListView = (ListView) view.findViewById(R.id.issue_listview);
        this.issueListView.setAdapter(this.adapter);

//        this.issueListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                pager.requestDisallowInterceptTouchEvent(true);
//                return true;
//            }
//        })
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

    public void setWatchManager(WatchManager watchManager) {
        this.watchManager = watchManager;
        this.watchManager.addObserver(this);
        this.watchManager.setOnReloaded(this);
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
        this.watchManager.reload();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

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
    public void notifyReloaded() {
        this.laySwipe.setRefreshing(false);
        this.adapter.setIssueList(this.watchManager.getHistory());
        this.adapter.notifyDataSetChanged();
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
