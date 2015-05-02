package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.component.Card;
import slm2015.hey.ui.util.UiUtility;

public class NewFunnyWatchFragment extends Fragment {

    private Activity activity;
    private RelativeLayout card_frame;
    private int windowwidth;
    private int screenCenter;
    private int x_cord, y_cord, moved_x_cord, moved_y_cord, pressX, pressY;
    private int Likes = 0;
    private float alphaValue = 0;
    private ViewPager pager;

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

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_funnywatch_layout, container, false);
        this.card_frame = (RelativeLayout) view.findViewById(R.id.card_frame);
        windowwidth = this.activity.getWindowManager().getDefaultDisplay().getWidth();
        screenCenter = windowwidth / 2;
        int[] myImageList = new int[]{R.drawable.arrow_head, R.drawable.arrow_head_inactive,
                R.drawable.arrow_right, R.drawable.btn_camera, R.drawable.button_cancel,
                R.drawable.button_ok};
        Issue[] issues = new Issue[]{new Issue("北科紅樓", "玻璃破了", ""), new Issue("垃圾麵", "賣完囉", ""), new Issue("香腸伯", "今天找打手", "在建國南路"), new Issue("starbucks", "is on sale", "")};
        for (int i = 0; i < issues.length; i++) {
            final Card card = new Card(this.activity);
//            myRelView.setTag(i);
            card.assignIssue(issues[i]);
            int marginTop = UiUtility.dpiToPixel(80, getResources());
            int others = UiUtility.dpiToPixel(0, getResources());
            card.setMargin(others, marginTop, others, others);

            if (i == 0) {
                card.setRotation(-1);
            } else if (i == 1) {
                card.setRotation(-5);

            } else if (i == 2) {
                card.setRotation(3);

            } else if (i == 3) {
                card.setRotation(7);

            } else if (i == 4) {
                card.setRotation(-2);

            } else if (i == 5) {
                card.setRotation(5);

            }

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

            card.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pager.requestDisallowInterceptTouchEvent(true);

                    x_cord = (int) card.getX();
                    y_cord = (int) card.getY();

//                    card.setX(x_cord - screenCenter + 40);
//                    card.setY(y_cord - 150);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            pressX = (int) event.getX();
                            pressY = (int) event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int touchX = (int) event.getX();
                            moved_x_cord = (int) event.getX() - pressX;
                            moved_y_cord = (int) event.getY() - pressY;
                            int xt = x_cord + moved_x_cord;
                            int yt = y_cord + moved_y_cord;
                            card.setX(xt);
                            card.setY(yt);

                            card.setRotation((float) ((card.getX()+card.getWidth()/2) - screenCenter)/10);
                            if (moved_x_cord >= 0) {
//                                card
//                                        .setRotation((float) moved_x_cord);
                                if (touchX > (screenCenter + (screenCenter / 2))) {
                                    imageLike.setAlpha(1);
                                    if (touchX > (windowwidth - (screenCenter / 4))) {
                                        Likes = 2;
                                    } else {
                                        Likes = 0;
                                    }
                                } else {
                                    Likes = 0;
                                    imageLike.setAlpha(0);
                                }
                                imagePass.setAlpha(0);
                            } else {
                                // rotate
//                                card
//                                        .setRotation((float) moved_x_cord);

                                if (touchX < (screenCenter / 2)) {
                                    imagePass.setAlpha(1);
                                    if (touchX < screenCenter / 4) {
                                        Likes = 1;
                                    } else {
                                        Likes = 0;
                                    }
                                } else {
                                    Likes = 0;
                                    imagePass.setAlpha(0);
                                }

                                imageLike.setAlpha(0);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
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
            });

            card_frame.addView(card);

        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPager(ViewPager pager){
        this.pager = pager;
    }
}
