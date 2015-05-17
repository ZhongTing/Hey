package slm2015.hey.view.tabs.watch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import junit.framework.Assert;

import slm2015.hey.R;
import slm2015.hey.core.Observer;
import slm2015.hey.core.issue.IssueLoader;
import slm2015.hey.entity.CardDeck;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.component.Card;
import slm2015.hey.view.util.UiUtility;
import slm2015.hey.view.tabs.TabPagerFragment;

public class WatchFragment extends TabPagerFragment implements Animation.AnimationListener, View.OnTouchListener, Observer {
    private final int SWIPE_WIDTH_DP = 100;
    private final int MOVE_ANIMATION_DURATION = 700;
    private final int RETURN_ANIMATION_DURATION = 300;
    private final int REFRESH_ANIMATION_DURATION = 200;

    private ViewPager pager;
    private ImageButton likeButton, dislikeButton, refreshButton;
    private RelativeLayout cardFrame;
    //    private List<Card> cardDeck = new ArrayList<>();
    private IssueLoader issueLoader;
    private CardDeck deck;

    private int xCord, yCord, movedXCord, movedYCord, pressX, pressY, card_iniX, card_iniY;
    private float initCardX, initCardY;
    private int windowWidth;
    private int screenCenter;
    private boolean allEvent = true, isRefresh = false;

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
        View view = inflater.inflate(R.layout.new_funnywatch_layout, container, false);
        this.init(view);
        return view;
    }

    private void setViewPager(ViewPager pager) {
        this.pager = pager;
    }

    private void init(View view) {
        this.issueLoader = new IssueLoader(this.getActivity());
        this.issueLoader.addObserver(this);

        this.cardFrame = (RelativeLayout) view.findViewById(R.id.card_frame);

        initialDislikeButton(view);
        initialLikeButton(view);
        initialRefreshButton(view);

        this.windowWidth = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
        this.screenCenter = this.windowWidth / 2;

        initialBaseCard();

        this.deck = new CardDeck(this.issueLoader, this.getActivity());
        this.deck.setOnDataSetChanged(new CardDeck.OnDataSetChanged() {
            @Override
            public void notifyTest() {
                for (int i = 5; i < 10; i++) {
                    if (deck.getCardQueue().size() > i - 5) {
//                        WatchFragment.this.cardFrame.removeViewAt(i);
                        Card addCard = deck.getCardQueue().get(i - 5);
                        if (addCard.getParent() != null)
                            ((ViewGroup) addCard.getParent()).removeView(addCard);
                        WatchFragment.this.cardFrame.addView(addCard, i);
                        float x = addCard.getX();
                        float y = addCard.getY();
                        float width = addCard.getWidth();
                        float height = addCard.getHeight();
                    }
                }
            }
        });
        refresh();
    }

    private void showLoadedCard(boolean resetI) {
        while (this.issueLoader.getNewIssues().size() > 5)
            this.issueLoader.pushToIssues();
        if (this.issueLoader.getNewIssues().size() > 0) {
            Issue issue = this.issueLoader.getNewIssues().poll();
            this.issueLoader.getIssues().add(issue);
            final Card card = findCard(issue);
            if (card.getParent() != null)
                ((ViewGroup) card.getParent()).removeView(card);
            this.cardFrame.addView(card);
            card.bringToFront();
            this.cardFrame.requestLayout();
            refreshAnimation(card);
        } else {
            this.setAllButtonEnable(true);
        }
    }

    //in addition to load the correct card in deck, need to use issue to find the correct card
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

    private void initialLikeButton(View view) {
        this.likeButton = (ImageButton) view.findViewById(R.id.likeButton);
        this.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WatchFragment.this.deck.getCardQueue().size() > 0) {
                    like(deck.getCardQueue().get(deck.getCardQueue().size() - 1));
                }
            }
        });
    }

    private void like(Card card) {
        final boolean LIKE = true;
        playMoveAnimation(card, LIKE);
    }

    private void initialDislikeButton(View view) {
        this.dislikeButton = (ImageButton) view.findViewById(R.id.dislikeButton);
        this.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WatchFragment.this.deck.getCardQueue().size() > 0) {
                    dislike(deck.getCardQueue().get(deck.getCardQueue().size() - 1));
                }
            }
        });
    }

    private void dislike(Card card) {
        final boolean DISLIKE = false;
        playMoveAnimation(card, DISLIKE);
    }

    private void playMoveAnimation(final Card card, final boolean like) {
        final int posX = like ? 500 : -500;
        float card_x = card.getRotationX();
        float card_y = card.getRotationY();
        Animation animation = new TranslateAnimation(card_x, posX, card_y, card_y);
        animation.setDuration(MOVE_ANIMATION_DURATION);
        animation.setRepeatCount(0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                setAllEvent(false);
                allEvent = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeCard(card, like);
                setAllEvent(true);
                if (like) {
                    //todo save like card
                } else {
                    //todo save dislike card
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card.setAnimation(animation);
        card.setVisibility(View.GONE);
        animation.start();
    }

    private void setAllEvent(boolean b) {
        this.allEvent = b;
        setAllButtonEnable(b);
    }

    private void setAllButtonEnable(boolean b) {
        this.likeButton.setEnabled(b);
        this.dislikeButton.setEnabled(b);
        this.refreshButton.setEnabled(b);
    }

    private void refresh() {
        this.issueLoader.loadNewIssues();
    }

    @Override
    public void FragmentSelected() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        setAllEvent(false);
        isRefresh = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (this.issueLoader.getNewIssues().size() > 0) {
            showLoadedCard(false);
        } else {
            setAllEvent(true);
            isRefresh = false;
            if (this.deck.getCardQueue().size() > 0)
                this.deck.getCardQueue().get(this.deck.getCardQueue().size() - 1).setOnTouchListener(this);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void removeCard(Card card, boolean like) {
//         this.cardFrame.removeView(card);
//        this.cardDeck.remove(card);
//        if (this.cardDeck.size() > 0)
//            this.cardDeck.get(this.cardDeck.size() - 1).setOnTouchListener(this);
        this.deck.operation();
        if (this.deck.getCardQueue().size() > 0)
            this.deck.getCardQueue().get(this.deck.getCardQueue().size() - 1).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pager.requestDisallowInterceptTouchEvent(true);
        if (this.deck.getCardQueue().size() <= 0 || this.isRefresh)
            return false;
        int index = this.deck.getCardQueue().size() - 1;
        Card card = this.deck.getCardQueue().get(index);
        xCord = (int) card.getX();
        yCord = (int) card.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAllButtonEnable(false);
                if (this.allEvent) {
                    card_iniX = (int) card.getX();
                    card_iniY = (int) card.getY();
                    deck.setIniCardXY(card_iniX, card_iniY);
                }
                pressX = (int) event.getX();
                pressY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int tempX = (int) event.getX() - pressX;
                int tempY = (int) event.getY() - pressY;
                int touchX = (int) event.getX();
                movedXCord = xCord + tempX;
                movedYCord = yCord + tempY;
                card.setX(movedXCord);
                card.setY(movedYCord);

                Button imageLike = card.getImageLike();
                Button imagePass = card.getImagePass();

                card.setRotation((float) ((card.getX() + card.getWidth() / 2) - screenCenter) / 10);

                if (movedXCord >= screenCenter) {
                    Log.d("touchX", String.valueOf(touchX));
                    if ((card.getX() + card.getWidth() / 2) > (screenCenter + (screenCenter / 2))) {
                        imageLike.setAlpha(1);
                    } else {
                        imageLike.setAlpha(0);
                    }
                    imagePass.setAlpha(0);
                } else {
                    // rotate
                    if ((card.getX() + card.getWidth() / 2) < (screenCenter / 2)) {
                        imagePass.setAlpha(1);
                    } else {
                        imagePass.setAlpha(0);
                    }

                    imageLike.setAlpha(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(movedXCord - card_iniX) > UiUtility.dpiToPixel(SWIPE_WIDTH_DP, getResources())) {
                    if (movedXCord - card_iniX >= 0)
                        like(card);
                    else
                        dislike(card);
                    setAllButtonEnable(true);
                } else {
                    playReturnAnimation(card, card_iniX, card_iniY);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void playReturnAnimation(final Card card, final int iniX, final int iniY) {
        float card_x = card.getRotationX();
        float card_y = card.getRotationY();
        float deltaX = card.getX() - iniX;
        float deltaY = card.getY() - iniY;
        Animation animation = new TranslateAnimation(card_x, -deltaX, card_y, -deltaY);
        animation.setDuration(RETURN_ANIMATION_DURATION);
        animation.setRepeatCount(0);
        animation.setFillEnabled(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setAllEvent(false);
                card.setRotation(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setX(iniX);
                card.setY(iniY);
                setAllEvent(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card.setAnimation(animation);
        card.startAnimation(animation);
    }

    @Override
    public void onSubjectChanged() {
        if (this.issueLoader.getNewIssues().size() > 0) {
            this.deck.reloadDeck();
            showLoadedCard(true);
        } else {
            setAllEvent(true);
            isRefresh = false;
            if (this.deck.getCardQueue().size() > 0)
                this.deck.getCardQueue().get(this.deck.getCardQueue().size() - 1).setOnTouchListener(this);
        }
    }
}
