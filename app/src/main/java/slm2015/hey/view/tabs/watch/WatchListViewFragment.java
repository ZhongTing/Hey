package slm2015.hey.view.tabs.watch;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import slm2015.hey.R;
import slm2015.hey.core.Observer;
import slm2015.hey.view.tabs.TabPagerFragment;

public class WatchListViewFragment extends TabPagerFragment implements Observer {

    private ImageButton changeViewButton;
    private FragmentManager fragmentManager;

    static public WatchListViewFragment newInstance(FragmentManager fragmentManager) {
        WatchListViewFragment fragment = new WatchListViewFragment();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watch_listview_layout, container, false);
        this.init(view);
        return view;
    }

    private void init(View view) {
        initialChangeViewButton(view);
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

    @Override
    public int getPageIconRedId() {
        return R.drawable.funny_watch;
    }

    @Override
    public void FragmentSelected() {

    }

    @Override
    public void onSubjectChanged() {

    }

    private void changeToListView() {
        this.fragmentManager.popBackStack(WatchFragment.WATCH_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
