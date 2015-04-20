package slm2015.hey.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<View> mListViews;
    private LayoutInflater inflater;

    public ViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
//        return mListViews.size();
        return 5;
    }

//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }

    @Override
    public Fragment getItem(int i) {
        return null;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        RelativeLayout item = (RelativeLayout) this.inflater.inflate(R.layout.raise_issue, container, false);
////        container.addView(mListViews.get(position), 0);
//        container.addView(item);
//        return item;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((LinearLayout)object);
//    }
}
