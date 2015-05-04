package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class NewFunnyWatchFragment extends Fragment implements View.OnTouchListener, Animation.AnimationListener {

    private final int SWIPE_WIDTH_DP = 100;
    private final int CARD_MARGIN_TOP = 70;

    private Activity activity;
    private RelativeLayout card_frame;
    private int windowwidth;
    private int screenCenter;
    private int x_cord, y_cord, moved_x_cord, moved_y_cord, pressX, pressY, card_iniX, card_iniY;
    private int Likes = 0;
    private float alphaValue = 0;
    private ViewPager pager;
    private ArrayList<Card> cardDeck;
    private float ini_cardX, ini_cardY;
    private Issue[] issues = new Issue[]{new Issue("北科紅樓", "玻璃破了", ""), new Issue("垃圾麵", "賣完囉", ""), new Issue("香腸伯", "今天找打手", "在建國南路"), new Issue("starbucks", "is on sale", "")};
    private Queue loadedIssues = new LinkedList();
    private Queue loadCard = new LinkedList();
    private Card animationCard;

    public static NewFunnyWatchFragment newInstance(ViewPager pager) {
        NewFunnyWatchFragment fragment = new NewFunnyWatchFragment();
        fragment.setPager(pager);
        return fragment;
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
        initialAnimationCard();
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
            }
            this.card_frame.addView(card);
        }
    }

    private void initialAnimationCard() {
        Issue issue = new Issue("", "檔案讀取中...", "");
        int marginTop = UiUtility.dpiToPixel(CARD_MARGIN_TOP, getResources());
        int others = UiUtility.dpiToPixel(0, getResources());
        this.animationCard = new Card(this.activity);
        this.animationCard.assignIssue(issue);
        this.animationCard.setMargin(others, marginTop, others, others);
        this.card_frame.addView(this.animationCard, 0);
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
        ImageButton refreshButton = (ImageButton) view.findViewById(R.id.refreshButton);
        refreshButton.bringToFront();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    private void initialDislikeButton(View view) {
        final boolean CLICK = true;
        ImageButton dislikeButton = (ImageButton) view.findViewById(R.id.dislikeButton);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeck.size() > 0)
                    dislike(cardDeck.get(cardDeck.size() - 1), CLICK);
            }
        });
    }

    private void initialLikeButton(View view) {
        final boolean CLICK = true;
        ImageButton dislikeButton = (ImageButton) view.findViewById(R.id.likeButton);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardDeck.size() > 0)
                    like(cardDeck.get(cardDeck.size() - 1), CLICK);
            }
        });
    }

    private void showLoadedCard() {
//        for (int i = 0; i < this.loadedIssues.size(); i++) {
        while (this.loadedIssues.size() > 0) {
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
//            refreshAmination(card);
            this.cardDeck.add(card);
//            refreshAmination(this.cardDeck.get(this.cardDeck.size() - 1));
        }

        this.cardDeck.get(this.cardDeck.size() - 1).setOnTouchListener(this);
    }

    private void refresh() {
        Issue[] loadedIssues = new Issue[]{new Issue("北科紅樓", "玻璃破了", ""), new Issue("垃圾麵", "賣完囉", ""), new Issue("香腸伯", "今天找打手", "在建國南路"), new Issue("starbucks", "is on sale", "")};
        for (int i = 0; i < loadedIssues.length; i++) {
            this.loadedIssues.offer(loadedIssues[i]);
//            Card card = new Card(this.activity);
//            card.assignIssue((Issue) this.loadedIssues.poll());
//            int marginTop = UiUtility.dpiToPixel(100, getResources());
//            int others = UiUtility.dpiToPixel(0, getResources());
//            card.setMargin(others, marginTop, others, others);
//            card.bringToFront();
//            this.card_frame.addView(card);
//            this.loadCard.offer(card);
        }


        if (loadedIssues.length > 0)
            refreshAmination(this.animationCard);

    }

    private void refreshAmination(Card card) {
        final int iniY = -1000;
        Animation animation = new TranslateAnimation(ini_cardX, ini_cardX, iniY, ini_cardY);
        animation.setDuration(100);
        animation.setRepeatCount(1);
//        card.bringToFront();
//        this.card_frame.requestLayout();
        card.bringToFront();
        card.setAnimation(animation);
        card.setVisibility(View.GONE);
        animation.setAnimationListener(this);
        animation.start();
    }

    private void like(Card card, boolean touchButton) {
        final boolean LIKE = true;
        if (touchButton)
            playAnimation(card, LIKE);
        else
            swipe(card, LIKE);
        removeCard(card, LIKE);
    }

    private void dislike(Card card, boolean touchButton) {
        final boolean DISLIKE = false;
        if (touchButton)
            playAnimation(card, DISLIKE);
        else
            swipe(card, DISLIKE);
        removeCard(card, DISLIKE);
    }

    private void removeCard(Card card, boolean like) {
//        this.card_frame.removeView(card);
        this.cardDeck.remove(card);
        if (this.cardDeck.size() > 0)
            this.cardDeck.get(this.cardDeck.size() - 1).setOnTouchListener(this);
    }

    private void swipe(Card card, boolean like) {
        final int pos = like ? 1000 : -1000;
        final float FRAME = (float) 10;
        if (like) {
            while (card.getX() < pos) {
                card.setX(card.getX() + FRAME);
                card.setY(card.getY() + FRAME);
            }
        } else {
            while (card.getX() > pos) {
                card.setX(card.getX() - FRAME);
                card.setY(card.getY() - FRAME);
            }
        }
    }

    private void playAnimation(Card card, boolean like) {
        final int posX = like ? 1000 : -1000;
        float card_x = card.getX();
        float card_y = card.getY();
        Animation animation = new TranslateAnimation(card_x, posX, card_y, card_y);
        animation.setDuration(300);
        animation.setRepeatCount(0);
        card.setAnimation(animation);
        card.setVisibility(View.GONE);
        animation.start();
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
        int index = this.cardDeck.size() - 1;
        Card card = this.cardDeck.get(index);
        x_cord = (int) card.getX();
        y_cord = (int) card.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                card_iniX = (int) card.getX();
                card_iniY = (int) card.getY();
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
//                        if (touchX < screenCenter / 4) {
//                            Likes = 1;
//                        } else {
//                            Likes = 0;
//                        }
                    } else {
//                        Likes = 0;
                        imagePass.setAlpha(0);
                    }

                    imageLike.setAlpha(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(moved_x_cord - card_iniX) > UiUtility.dpiToPixel(SWIPE_WIDTH_DP, getResources())) {
                    final boolean SWIPE = false;
                    if (moved_x_cord - card_iniX >= 0)
                        like(card, SWIPE);
                    else
                        dislike(card, SWIPE);
                } else {
                    card.setX(card_iniX);
                    card.setY(card_iniY);
                    card.setRotation(0);
                }
//                            x_cord = (int) event.getRawX();
//                            y_cord = (int) event.getRawY();
//
//                            Log.e("X Point", "" + x_cord + " , Y " + y_cord);
//                            imagePass.setAlpha(0);
//                            imageLike.setAlpha(0);
//
//                            if (Likes == 0) {
//                                Log.e("Event Status", "Nothing");
//                                card.setX(40);
//                                card.setY(40);
//                                card.setRotation(0);
//                            } else if (Likes == 1) {
//                                Log.e("Event Status", "Passed");
//                                parentView.removeView(card);
//                            } else if (Likes == 2) {
//
//                                Log.e("Event Status", "Liked");
//                                parentView.removeView(card);
//                            }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        this.cardDeck.get(this.cardDeck.size() - 1).setVisibility(View.VISIBLE);
        this.card_frame.removeView(this.animationCard);
        this.card_frame.invalidate();
        initialAnimationCard();
        showLoadedCard();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
