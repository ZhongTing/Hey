package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import slm2015.hey.R;

public class FunnyWatchFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private Activity activity;
    private MyPagerAdapter mPagerAdapter;
    private LayoutInflater inflater;

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
        this.inflater = inflater;
        View view = this.inflater.inflate(R.layout.funnywatch_layout, container, false);
        TextView t = (TextView) view.findViewById(R.id.test);
        this.mViewPager = (ViewPager) view.findViewById(R.id.viewpagerforfunnywatch);
        this.mPagerAdapter = new MyPagerAdapter();
        try {
            this.mViewPager.setAdapter(this.mPagerAdapter);
        }catch (Exception e){
            System.out.print(e);
        }
        this.mViewPager.setCurrentItem(0);

//        intialiseViewPager(view);
        return view;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void intialiseViewPager(View view) {
        this.mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        this.mPagerAdapter = new ViewPagerAdapter(this.inflater);
        this.mViewPager.setAdapter(this.mPagerAdapter);
//        this.mViewPager.setOnPageChangeListener(this);
    }

    private class MyPagerAdapter extends PagerAdapter {

        int NumberOfPages = 5;

        int[] res = {
                android.R.drawable.ic_dialog_alert,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_compass,
                android.R.drawable.ic_menu_directions,
                android.R.drawable.ic_menu_gallery};
        int[] backgroundcolor = {
                0xFF101010,
                0xFF202020,
                0xFF303030,
                0xFF404040,
                0xFF505050};

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            TextView textView = new TextView(activity);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(30);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setText(String.valueOf(position));

            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(res[position]);
            ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageParams);
//
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setBackgroundColor(backgroundcolor[position]);
            layout.setLayoutParams(layoutParams);
            layout.addView(textView);
            layout.addView(imageView);

            final int page = position;
            layout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,
                            "Page " + page + " clicked",
                            Toast.LENGTH_LONG).show();
                }});

            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }

    }
}
