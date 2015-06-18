package slm2015.hey.view.tabs.watch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import slm2015.hey.R;
import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.component.IssueCard;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.util.UiUtility;

public class WatchFragment extends TabPagerFragment implements Observer {
    public static final String SELF_TAG = "watch_fragment";
    private static final int REFRESH_ANIMATION_DURATION = 200;
    private static final int MAX_CARD_ANIMATION = 5;

    private WatchManager watchManager;

    CardIssueAdapter cardIssueAdapter;
    private IssueLoader issueLoader;
    private ViewPager pager;
    private SwipeFlingAdapterView flingAdapterContainer;
    private FrameLayout animationCardFrame;
    private FrameLayout bottomCardFrame;
    private ImageButton likeButton, dislikeButton, refreshButton, changeViewButton;
    private float initCardX, initCardY;
    private int lastIssueCount;

    private WatchHistoryFragment watchListViewFragment;

    static public WatchFragment newInstance(ViewPager pager) {
        WatchFragment fragment = new WatchFragment();
        fragment.setViewPager(pager);
        return fragment;
    }

    private void setViewPager(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public int getPageIconRedId() {
        return R.drawable.funny_watch;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watch_deck_layout, container, false);

        this.bottomCardFrame = (FrameLayout) view.findViewById(R.id.bottom_card_frame);

        FrameLayout initCard = (FrameLayout) view.findViewById(R.id.init_card);
        this.initCardX = initCard.getX();
        this.initCardY = initCard.getY();

        this.animationCardFrame = (FrameLayout) view.findViewById(R.id.animation_card_frame);
        this.animationCardFrame.removeAllViews();

        this.initialize(view);
        this.loadNewIssue();

        return view;
    }

    private void loadNewIssue() {
        this.setAllButtonEnable(false);
        this.lastIssueCount = this.issueLoader.getIssues().size();
        this.issueLoader.loadNewIssues();
    }

    private void initialize(View view) {
        initialDislikeButton(view);
        initialLikeButton(view);
        initialRefreshButton(view);
        initialChangeViewButton(view);

        initFlingAdapterContainer(view);
    }

    private void initFlingAdapterContainer(View view) {
        this.issueLoader = new IssueLoader(getActivity());
        this.issueLoader.addObserver(this);
        cardIssueAdapter = new CardIssueAdapter(getActivity(), R.layout.card, this.pager, this.issueLoader.getIssues());

        this.flingAdapterContainer = (SwipeFlingAdapterView) view.findViewById(R.id.card_frame);
        this.flingAdapterContainer.setAdapter(cardIssueAdapter);
        this.flingAdapterContainer.setFlingListener(onFlingListener);
    }
    
    @Override
    public void onLoaderChanged() {
        this.cardIssueAdapter.notifyDataSetChanged();
        this.flingAdapterContainer.clearTopView();

        int newLoadedIssueCount = this.issueLoader.getIssues().size() - this.lastIssueCount;
        this.lastIssueCount = this.issueLoader.getIssues().size();
        playCardLoadAnimation((newLoadedIssueCount > MAX_CARD_ANIMATION ? MAX_CARD_ANIMATION : newLoadedIssueCount) - 1);
    }

    public void playCardLoadAnimation(final int count) {
        if (count >= 0 && this.cardIssueAdapter.getCount() - 1 >= count) {
            View card = this.cardIssueAdapter.getView(count, null, this.animationCardFrame);
            card.setX(this.initCardX);
            card.setY(this.initCardY);
            this.animationCardFrame.addView(card);

            Animation animation = new TranslateAnimation(0, 0, -1000, 0);
            animation.setDuration(REFRESH_ANIMATION_DURATION);
            animation.setRepeatCount(0);
            card.bringToFront();
            card.setAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    WatchFragment.this.playCardLoadAnimation(count - 1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            card.startAnimation(animation);

        } else if (count >= 0) {
            this.playCardLoadAnimation(count - 1);

        } else {
            this.animationCardFrame.removeAllViews();
            setAllButtonEnable(true);

        }
    }

    SwipeFlingAdapterView.onFlingListener onFlingListener = new SwipeFlingAdapterView.onFlingListener() {
        @Override
        public void removeFirstObjectInAdapter() {
            Log.d("LIST", "removed object!");
            cardIssueAdapter.removeFirst();
        }

        @Override
        public void onLeftCardExit(Object dataObject) {
            // Toast.makeText(getActivity(), "Left!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRightCardExit(Object dataObject) {
            // Toast.makeText(getActivity(), "Right!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdapterAboutToEmpty(int itemsInAdapter) {
            cardIssueAdapter.notifyDataSetChanged();
            Log.d("LIST", "notified");
        }

        @Override
        public void onScroll(float v) {
            pager.requestDisallowInterceptTouchEvent(true);
            cardIssueAdapter.setFirstCardState(v);
        }
    };

    private void initialBaseCard() {
        Issue issue = new Issue();
        issue.setDescription("檔案讀取中...");

        for (int i = 0; i < 5; i++) {
            IssueCard card = new IssueCard(this.getActivity(), this.bottomCardFrame, this.pager, issue);
            View view = card.getView();
            view.setRotation(7);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pager.requestDisallowInterceptTouchEvent(true);
                    return true;
                }
            });
            this.bottomCardFrame.addView(view);
        }
    }

    private void initialRefreshButton(View view) {
        this.refreshButton = (ImageButton) view.findViewById(R.id.refreshButton);
        this.refreshButton.bringToFront();
        this.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WatchFragment.this.loadNewIssue();
            }
        });
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

    private void initialLikeButton(View view) {
        this.likeButton = (ImageButton) view.findViewById(R.id.likeButton);
        this.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WatchFragment.this.flingAdapterContainer.getTopCardListener().selectRight();
            }
        });
    }

    private void initialDislikeButton(View view) {
        this.dislikeButton = (ImageButton) view.findViewById(R.id.dislikeButton);
        this.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WatchFragment.this.flingAdapterContainer.getTopCardListener().selectLeft();
            }
        });
    }

    private void setAllButtonEnable(boolean b) {
        this.likeButton.setEnabled(b);
        this.dislikeButton.setEnabled(b);
        this.refreshButton.setEnabled(b);
        this.changeViewButton.setEnabled(b);
    }

    @Override
    public void FragmentSelected() {
        UiUtility.closeKeyBoard(this.getActivity());
    }

    private void changeToListView() {
        FragmentManager fragmentManager = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (this.watchListViewFragment == null)
            this.watchListViewFragment = WatchHistoryFragment.newInstance(fragmentManager, this.pager, this.issueLoader);
        else
            this.watchListViewFragment.onRefresh();
        transaction.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        transaction.replace(R.id.test, this.watchListViewFragment);
        transaction.addToBackStack(WatchFragment.SELF_TAG);
        transaction.commit();
    }

    public WatchManager getWatchManager() {
        return watchManager;
    }
}
