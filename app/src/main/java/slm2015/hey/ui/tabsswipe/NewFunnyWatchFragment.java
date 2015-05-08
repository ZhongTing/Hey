package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.component.Card;
import slm2015.hey.ui.util.UiUtility;
import slm2015.hey.view.tabs.TabPagerFragment;

public class NewFunnyWatchFragment extends TabPagerFragment implements View.OnTouchListener, Animation.AnimationListener {

    private final int SWIPE_WIDTH_DP = 100;
    private final int CARD_MARGIN_TOP = 70;
    private final int MOVE_ANIMATION_DURATION = 700;
    private final int RETURN_ANIMATION_DURATION = 300;
    private final int REFRESH_ANIMATION_DURATION = 200;

    private ImageButton dislikeButton, likeButton, refreshButton;
    private Activity activity;
    private RelativeLayout card_frame;
    private int windowwidth;
    private int screenCenter;
    private int x_cord, y_cord, moved_x_cord, moved_y_cord, pressX, pressY, card_iniX, card_iniY;
    private float alphaValue = 0;
    private ViewPager pager;
    private ArrayList<Card> cardDeck;
    private float ini_cardX, ini_cardY;
    private Issue[] issues = new Issue[]{new Issue("北科紅樓", "玻璃破了", ""), new Issue("垃圾麵", "賣完囉", ""), new Issue("香腸伯", "今天找打手", "在建國南路"), new Issue("starbucks", "is on sale", "")};
    private Queue loadedIssues = new LinkedList();
    private boolean allEvent = true, isRefresh =false;

    public NewFunnyWatchFragment(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardDeck = new ArrayList<Card>();
        for (Issue issue : this.issues)
            loadedIssues.offer(issue);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_funnywatch_layout, container, false);
        this.card_frame = (RelativeLayout) view.findViewById(R.id.card_frame);
        initialDislikeButton(view);
        initialLikeButton(view);
        initialRefreshButton(view);

        windowwidth = this.activity.getWindowManager().getDefaultDisplay().getWidth();
        screenCenter = windowwidth / 2;


        this.issues[0].setImage(BitmapFactory.decodeResource(getResources(),
                R.drawable.odd_bird));
        initialBaseCard();
        showLoadedCard();
        return view;
    }

    private void initialBaseCard() {
        Issue issue = new Issue("", "檔案讀取中...", "");
        int marginTop = UiUtility.dpiToPixel(CARD_MARGIN_TOP, getResources());
        int others = UiUtility.dpiToPixel(0, getResources());
//        initialAnimationCard();
        for (int i = 0; i < 5; i++) {
            Card card = new Card(this.activity);
            card.assignIssue(issue);
            card.setMargin(others, marginTop, others, others);
            if (i == 0)
                card.setRotation(-1);
            else if (i == 1)
                card.setRotation(-5);
            else if (i == 2)
                card.setRotation(3);
            else if (i == 3)
                card.setRotation(7);
            else {
                ini_cardY = card.getY();
                ini_cardX = card.getX();
                card.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        pager.requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                });
            }
            this.card_frame.addView(card);
        }
    }

    private void initialImagePass(Card card) {
        final Button imagePass = new Button(this.activity);
        imagePass.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
        imagePass.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.funny_watch));

        imagePass.setX(card.getX() + UiUtility.dpiToPixel(240, getResources()));
        imagePass.setY(card.getY() + UiUtility.dpiToPixel(110, getResources()));
        imagePass.setRotation(45);
        imagePass.setAlpha(alphaValue);
        imagePass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //your pass action code write here
            }
        });
        card.addView(imagePass);
        card.setImagePass(imagePass);
    }

    private void initialImageLike(Card card) {
        final Button imageLike = new Button(this.activity);
        imageLike.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
        imageLike.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.funny_po));
        imageLike.setX(card.getX());
        imageLike.setY(card.getY() + UiUtility.dpiToPixel(110, getResources()));
        imageLike.setAlpha(alphaValue);
        imageLike.setRotation(-45);
        imageLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //your like action code write here
            }
        });
        card.addView(imageLike);
        card.setImageLike(imageLike);
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

    private void initialDislikeButton(View view) {
        this.dislikeButton = (ImageButton) view.findViewById(R.id.dislikeButton);
        this.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeck.size() > 0){
                    dislike(cardDeck.get(cardDeck.size() - 1));
                }
            }
        });
    }

    private void initialLikeButton(View view) {
        this.likeButton = (ImageButton) view.findViewById(R.id.likeButton);
        this.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeck.size() > 0){
                    like(cardDeck.get(cardDeck.size() - 1));
                }
            }
        });
    }

    private void showLoadedCard() {
//        for (int i = 0; i < this.loadedIssues.size(); i++) {
        if (this.loadedIssues.size() > 0) {
            final Card card = new Card(this.activity);
            card.assignIssue((Issue) this.loadedIssues.poll());
            int marginTop = UiUtility.dpiToPixel(CARD_MARGIN_TOP, getResources());
            int others = UiUtility.dpiToPixel(0, getResources());
            card.setMargin(others, marginTop, others, others);

            initialImageLike(card);
            initialImagePass(card);

            this.card_frame.addView(card);
            card.bringToFront();
            this.card_frame.requestLayout();
            refreshAmination(card);
            this.cardDeck.add(card);
        }
    }

    private void refresh() {
        Issue[] loadedIssues = new Issue[]{new Issue("北科紅樓", "玻璃破了", ""), new Issue("垃圾麵", "賣完囉", ""), new Issue("香腸伯", "今天找打手", "在建國南路"), new Issue("starbucks", "is on sale", "")};
        for (int i = 0; i < loadedIssues.length; i++) {
            this.loadedIssues.offer(loadedIssues[i]);
        }
        showLoadedCard();
    }

    private void refreshAmination(Card card) {
        final int iniY = -1000;
        Animation animation = new TranslateAnimation(ini_cardX, ini_cardX, iniY, ini_cardY);
        animation.setDuration(REFRESH_ANIMATION_DURATION);
        animation.setRepeatCount(0);
        card.bringToFront();
        card.setAnimation(animation);
        animation.setAnimationListener(this);
        card.startAnimation(animation);
    }

    private void like(Card card) {
        final boolean LIKE = true;
        playMoveAnimation(card, LIKE);
        removeCard(card, LIKE);
    }

    private void dislike(Card card) {
        final boolean DISLIKE = false;
        playMoveAnimation(card, DISLIKE);
        removeCard(card, DISLIKE);
    }

    private void removeCard(Card card, boolean like) {
//        this.card_frame.removeView(card);
        this.cardDeck.remove(card);
        if (this.cardDeck.size() > 0)
            this.cardDeck.get(this.cardDeck.size() - 1).setOnTouchListener(this);
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

    private void playMoveAnimation(Card card, final boolean like) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pager.requestDisallowInterceptTouchEvent(true);
        if (this.cardDeck.size() <= 0 || this.isRefresh)
            return false;
        int index = this.cardDeck.size() - 1;
        Card card = this.cardDeck.get(index);
        x_cord = (int) card.getX();
        y_cord = (int) card.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAllButtonEnable(false);
                if (this.allEvent) {
                    card_iniX = (int) card.getX();
                    card_iniY = (int) card.getY();
                }
                pressX = (int) event.getX();
                pressY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int tempX = (int) event.getX() - pressX;
                int tempY = (int) event.getY() - pressY;
                int touchX = (int) event.getX();
                moved_x_cord = x_cord + tempX;
                moved_y_cord = y_cord + tempY;
                card.setX(moved_x_cord);
                card.setY(moved_y_cord);

                Button imageLike = card.getImageLike();
                Button imagePass = card.getImagePass();

                card.setRotation((float) ((card.getX() + card.getWidth() / 2) - screenCenter) / 10);

                if (moved_x_cord >= screenCenter) {
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
                if (Math.abs(moved_x_cord - card_iniX) > UiUtility.dpiToPixel(SWIPE_WIDTH_DP, getResources())) {
                    if (moved_x_cord - card_iniX >= 0)
                        like(card);
                    else
                        dislike(card);
                } else {
                    playReturnAnimation(card, card_iniX, card_iniY);
                }
                setAllButtonEnable(true);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        setAllEvent(false);
        isRefresh = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (this.loadedIssues.size() > 0) {
            showLoadedCard();
        } else {
            setAllEvent(true);
            isRefresh = false;
            if (this.cardDeck.size() > 0)
                this.cardDeck.get(this.cardDeck.size() - 1).setOnTouchListener(this);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public int getPageIconRedId() {
        return R.drawable.funny_watch;
    }
}
