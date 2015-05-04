package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Intent;
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

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.component.Card;
import slm2015.hey.ui.util.UiUtility;

public class NewFunnyWatchFragment extends Fragment implements View.OnTouchListener {

    private final int SWIPE_WIDTH_DP = 100;

    private Activity activity;
    private RelativeLayout card_frame;
    private int windowwidth;
    private int screenCenter;
    private int x_cord, y_cord, moved_x_cord, moved_y_cord, pressX, pressY, card_iniX, card_iniY;
    private int Likes = 0;
    private float alphaValue = 0;
    private ViewPager pager;
    private ArrayList<Card> cardDeck;

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

        windowwidth = this.activity.getWindowManager().getDefaultDisplay().getWidth();
        screenCenter = windowwidth / 2;

        Issue[] issues = new Issue[]{new Issue("北科紅樓", "玻璃破了", ""), new Issue("垃圾麵", "賣完囉", ""), new Issue("香腸伯", "今天找打手", "在建國南路"), new Issue("starbucks", "is on sale", "")};
        for (int i = 0; i < issues.length; i++) {
            final Card card = new Card(this.activity);
//            myRelView.setTag(i);
            card.assignIssue(issues[i]);
            int marginTop = UiUtility.dpiToPixel(80, getResources());
            int others = UiUtility.dpiToPixel(0, getResources());
            card.setMargin(others, marginTop, others, others);

            card.setY(card.getY() - 10 * i);
            Log.d("card_y", String.valueOf(card.getY()));

            final Button imageLike = new Button(this.activity);
            imageLike.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
            imageLike.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.funny_po));
            imageLike.setX(20);
            imageLike.setY(80);
            imageLike.setAlpha(alphaValue);
            imageLike.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //your like action code write here
                }
            });
            card.addView(imageLike);

            final Button imagePass = new Button(this.activity);
            imagePass.setLayoutParams(new LinearLayout.LayoutParams(100, 50));
            imagePass.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.funny_watch));

            imagePass.setX((windowwidth - 200));
            imagePass.setY(100);
            imagePass.setRotation(45);
            imagePass.setAlpha(alphaValue);
            imagePass.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //your pass action code write here
                }
            });
            card.addView(imagePass);

            card_frame.addView(card);

            cardDeck.add(card);
        }
        this.cardDeck.get(this.cardDeck.size() - 1).setOnTouchListener(this);
        return view;
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
        animation.setDuration(1000);
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
                moved_x_cord = x_cord + tempX;
                moved_y_cord = y_cord + tempY;
                card.setX(moved_x_cord);
                card.setY(moved_y_cord);

                card.setRotation((float) ((card.getX() + card.getWidth() / 2) - screenCenter) / 10);
//                if (moved_x_cord >= 0) {
//                    if (touchX > (screenCenter + (screenCenter / 2))) {
//                        imageLike.setAlpha(1);
//                        if (touchX > (windowwidth - (screenCenter / 4))) {
//                            Likes = 2;
//                        } else {
//                            Likes = 0;
//                        }
//                    } else {
//                        Likes = 0;
//                        imageLike.setAlpha(0);
//                    }
//                    imagePass.setAlpha(0);
//                } else {
//                    // rotate
//                    if (touchX < (screenCenter / 2)) {
//                        imagePass.setAlpha(1);
//                        if (touchX < screenCenter / 4) {
//                            Likes = 1;
//                        } else {
//                            Likes = 0;
//                        }
//                    } else {
//                        Likes = 0;
//                        imagePass.setAlpha(0);
//                    }
//
//                    imageLike.setAlpha(0);
//                }
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
}
