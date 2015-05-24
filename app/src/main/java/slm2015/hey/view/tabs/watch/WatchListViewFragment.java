package slm2015.hey.view.tabs.watch;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import slm2015.hey.R;
import slm2015.hey.view.tabs.TabPagerFragment;

public class WatchListViewFragment extends TabPagerFragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, WatchManager.OnReloaded {

    private ImageButton changeViewButton;
    private FragmentManager fragmentManager;
    private ListView issueListView;
    private IssueAdapter adapter;
    private SwipeRefreshLayout laySwipe;
    private ViewPager pager;
    private WatchManager watchManager;

    static public WatchListViewFragment newInstance(FragmentManager fragmentManager, ViewPager pager, WatchManager watchManager) {
        WatchListViewFragment fragment = new WatchListViewFragment();
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
        this.adapter = new IssueAdapter(this.watchManager.getIssues());
        initialChangeViewButton(view);
        initialLaySwipe(view);
        initailListView(view);
    }

    private void initialLaySwipe(View view) {
        this.laySwipe = (SwipeRefreshLayout) view.findViewById(R.id.layswipe);
        laySwipe.setOnRefreshListener(this);
    }

    private void initailListView(View view) {
        this.issueListView = (ListView) view.findViewById(R.id.issue_listview);
        this.issueListView.setAdapter(this.adapter);

//        this.issueListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                pager.requestDisallowInterceptTouchEvent(true);
//                return true;
//            }
//        });
        this.issueListView.setOnScrollListener(this);
    }


    private void initialChangeViewButton(View view) {
        this.changeViewButton = (ImageButton) view.findViewById(R.id.changeViewButton);
        this.changeViewButton.bringToFront();
        this.changeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToListView();
            }
        });
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    public void setWatchManager(WatchManager watchManager) {
        this.watchManager = watchManager;
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
        if (firstVisibleItem == 0)
            this.laySwipe.setEnabled(true);
        else
            this.laySwipe.setEnabled(false);
    }

    @Override
    public void notifyReloaded() {
        this.laySwipe.setRefreshing(false);
        this.adapter.setIssueList(this.watchManager.getIssues());
        this.adapter.notifyDataSetChanged();
    }
}
