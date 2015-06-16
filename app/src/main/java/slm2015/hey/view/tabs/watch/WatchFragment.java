package slm2015.hey.view.tabs.watch;

import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import junit.framework.Assert;

import slm2015.hey.R;
import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.CardDeck;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.component.Card;
import slm2015.hey.view.tabs.TabPagerFragment;
import slm2015.hey.view.util.UiUtility;

public class WatchFragment extends TabPagerFragment implements Animation.AnimationListener, Observer, View.OnClickListener, WatchManager.OnReloaded {
    public static final String SELF_TAG = "watch_fragment";
    private final int REFRESH_ANIMATION_DURATION = 200;
    private final int MAX_CARD_ANIMATION = 5;

    private ViewPager pager;
    private ImageButton likeButton, dislikeButton, refreshButton, changeViewButton;
    private RelativeLayout cardFrame;

    private SwipeFlingAdapterView flingAdapterContainer;

    private CardDeck deck;

    private float initCardX, initCardY;
    private int windowWidth;
    private int screenCenter;
    private boolean allEvent = true, isRefresh = false, onMove = false;

    private WatchManager watchManager;
    private WatchHistoryFragment watchListViewFragment;


    CardIssueAdapter cardIssueAdapter;
    private IssueLoader issueLoader;

    static public WatchFragment newInstance(ViewPager pager) {
        WatchFragment fragment = new WatchFragment();
        fragment.setViewPager(pager);
        return fragment;
    }

    @Override
    public int getPageIconRedId() {
        return R.drawable.funny_watch;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watch_deck_layout, container, false);
        this.initialize(view);

        this.issueLoader.loadNewIssues();

        return view;
    }

    private void initialize(View view) {
        this.issueLoader = new IssueLoader(getActivity());
        this.issueLoader.addObserver(this);
        cardIssueAdapter = new CardIssueAdapter(getActivity(), R.layout.card, this.issueLoader.getIssues());

        this.flingAdapterContainer = (SwipeFlingAdapterView) view.findViewById(R.id.card_frame);
        this.flingAdapterContainer.setAdapter(cardIssueAdapter);
        this.flingAdapterContainer.setFlingListener(onFlingListener);
    }

    @Override
    public void onLoaderChanged() {
        cardIssueAdapter.notifyDataSetChanged();
    }

    SwipeFlingAdapterView.onFlingListener onFlingListener = new SwipeFlingAdapterView.onFlingListener() {
        @Override
        public void removeFirstObjectInAdapter() {
            Log.d("LIST", "removed object!");
            cardIssueAdapter.removeFirst();
        }

        @Override
        public void onLeftCardExit(Object dataObject) {
            Toast.makeText(getActivity(), "Left!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRightCardExit(Object dataObject) {
            Toast.makeText(getActivity(), "Right!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdapterAboutToEmpty(int itemsInAdapter) {
            // Ask for more data here
            cardIssueAdapter.notifyDataSetChanged();
            Log.d("LIST", "notified");
        }

        @Override
        public void onScroll(float v) {
            pager.requestDisallowInterceptTouchEvent(true);
            cardIssueAdapter.setFirstCardState(v);
            Log.d("Scroll", Float.toString(v));
        }
    };

    private void setViewPager(ViewPager pager) {
        this.pager = pager;
    }

    private void init(View view) {
        this.watchManager = new WatchManager(this.getActivity());
        this.watchManager.addObserver(this);
        this.watchManager.setOnReloaded(this);

        this.cardFrame = (RelativeLayout) view.findViewById(R.id.card_frame);

        initialDislikeButton(view);
        initialLikeButton(view);
        initialRefreshButton(view);
        initialChangeViewButton(view);

        this.windowWidth = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        this.screenCenter = this.windowWidth / 2;

        initialBaseCard();

        this.deck = new CardDeck(this.watchManager, this.getActivity());
        this.deck.setOnDataSetChanged(new CardDeck.OnDataSetChanged() {
            @Override
            public void notifyCardDeckChanged() {
                if (!WatchFragment.this.onMove)
                    resetCardDeckView();
            }
        });
        refresh();
    }

    private void resetCardDeckView() {
        for (int i = 5; i < 5 + CardDeck.CARD_MAX_AMOUNT; i++) {
            if (deck.getCardQueue().size() > i - 5) {
                Card addCard = deck.getCardQueue().get(i - 5);
                if (addCard.getParent() != null)
                    ((ViewGroup) addCard.getParent()).removeView(addCard);
                WatchFragment.this.cardFrame.addView(addCard, i);
            }
        }
    }

    private void showLoadedCard(boolean resetI) {
        while (this.watchManager.getNewIssues().size() > MAX_CARD_ANIMATION)
            this.watchManager.pushToIssues();
        if (this.watchManager.getNewIssues().size() > 0) {
            Issue issue = this.watchManager.pollNewIssues();
            this.watchManager.getIssues().add(issue);
            final Card card = findCard(issue);
            if (card.getParent() != null)
                ((ViewGroup) card.getParent()).removeView(card);
            this.cardFrame.addView(card);
            refreshAnimation(card);
        } else {
            this.setAllButtonEnable(true);
        }
    }

    private Card findCard(Issue issue) {
        for (int i = 0; i < this.deck.getCardQueue().size(); i++) {
            Card card = this.deck.getCardQueue().get(i);
            if (card.getIssue() == issue)
                return card;
        }
        Assert.assertFalse(true);
        return null;
    }

    public void refreshAnimation(Card card) {
        final int iniY = -1000;
        Animation animation = new TranslateAnimation(initCardX, initCardX, iniY, initCardY);
        animation.setDuration(REFRESH_ANIMATION_DURATION);
        animation.setRepeatCount(0);
        card.bringToFront();
        card.setAnimation(animation);
        animation.setAnimationListener(this);
        card.startAnimation(animation);
    }

    private void initialBaseCard() {
        Issue issue = new Issue();
        issue.setDescription("檔案讀取中...");

        for (int i = 0; i < 5; i++) {
            Card card = new Card(this.getActivity());
            card.assignIssue(issue);
            card.initDefaultMargin();

            if (i == 0)
                card.setRotation(-1);
            else if (i == 1)
                card.setRotation(-5);
            else if (i == 2)
                card.setRotation(3);
            else if (i == 3)
                card.setRotation(7);
            else {
                initCardY = card.getY();
                initCardX = card.getX();
                card.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        pager.requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                });
            }
            this.cardFrame.addView(card);
        }
    }

    private void initialRefreshButton(View view) {
        this.refreshButton = (ImageButton) view.findViewById(R.id.refreshButton);
        this.refreshButton.bringToFront();
        this.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllButtonEnable(false);
                refresh();
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
        this.likeButton.setOnClickListener(this);
    }

    private void initialDislikeButton(View view) {
        this.dislikeButton = (ImageButton) view.findViewById(R.id.dislikeButton);
        this.dislikeButton.setOnClickListener(this);
    }

    private void setAllEvent(boolean b) {
        this.allEvent = b;
        setAllButtonEnable(b);
    }

    private void setAllButtonEnable(boolean b) {
        this.likeButton.setEnabled(b);
        this.dislikeButton.setEnabled(b);
        this.refreshButton.setEnabled(b);
        this.changeViewButton.setEnabled(b);
    }

    private void refresh() {
        this.watchManager.reload();
    }

    @Override
    public void FragmentSelected() {
        UiUtility.closeKeyBoard(this.getActivity());
    }

    @Override
    public void onAnimationStart(Animation animation) {
        setAllEvent(false);
        isRefresh = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (this.watchManager.getNewIssues().size() > 0) {
            showLoadedCard(false);
        } else {
            setAllEvent(true);
            isRefresh = false;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public WatchManager getWatchManager() {
        return watchManager;
    }

    private long mLastClickTime = 0;

    @Override
    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 100)
            return;
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    private void changeToListView() {
        FragmentManager fragmentManager = getChildFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (this.watchListViewFragment == null)
            this.watchListViewFragment = WatchHistoryFragment.newInstance(fragmentManager, this.pager, this.watchManager);
        else
            this.watchListViewFragment.onRefresh();
        transaction.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        transaction.replace(R.id.test, this.watchListViewFragment);
        transaction.addToBackStack(WatchFragment.SELF_TAG);
        transaction.commit();
    }

    @Override
    public void notifyReloaded() {
        this.deck.reloadDeck();
        resetCardDeckView();
        if (this.watchManager.getNewIssues().size() > 0) {
            showLoadedCard(true);
        } else {
            setAllEvent(true);
            isRefresh = false;
        }
    }
}
