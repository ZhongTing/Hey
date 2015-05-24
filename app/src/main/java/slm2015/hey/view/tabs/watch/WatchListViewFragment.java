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

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
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

    private List<Issue> createFakeIssueList() {
        List<Issue> issueList = new ArrayList<>();
        Issue issue1 = new Issue();
        issue1.setSubject("test1S");
        issue1.setDescription("test1D");
        issue1.setPlace("test1P");
        Issue issue2 = new Issue();
        issue2.setSubject("test2S");
        issue2.setDescription("test2D");
        issue2.setPlace("test2P");
        Issue issue3 = new Issue();
        issue3.setSubject("test3S");
        issue3.setDescription("test3D");
        issue3.setPlace("test3P");
        Issue issue4 = new Issue();
        issue4.setSubject("test4S");
        issue4.setDescription("test4D");
        issue4.setPlace("test4P");
        Issue issue5 = new Issue();
        issue5.setSubject("test5S");
        issue5.setDescription("test5D");
        issue5.setPlace("test5P");
        Issue issue6 = new Issue();
        issue6.setSubject("test6S");
        issue6.setDescription("test6D");
        issue6.setPlace("test6P");
        Issue issue7 = new Issue();
        issue7.setSubject("test7S");
        issue7.setDescription("test7D");
        issue7.setPlace("test7P");
        Issue issue8 = new Issue();
        issue8.setSubject("test8S");
        issue8.setDescription("test8D");
        issue8.setPlace("test8P");
        Issue issue9 = new Issue();
        issue9.setSubject("test9S");
        issue9.setDescription("test9D");
        issue9.setPlace("test9P");
        Issue issue10 = new Issue();
        issue10.setSubject("test10S");
        issue10.setDescription("test10D");
        issue10.setPlace("test10P");
        issueList.add(issue1);
        issueList.add(issue2);
        issueList.add(issue3);
        issueList.add(issue4);
        issueList.add(issue5);
        issueList.add(issue6);
        issueList.add(issue7);
        issueList.add(issue8);
        issueList.add(issue9);
        issueList.add(issue10);
        return issueList;
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
        this.fragmentManager.popBackStack(WatchFragment.WATCH_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
