package slm2015.hey.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import slm2015.hey.R;
import slm2015.hey.entity.Term;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private final int MUN_OF_PAGES = 3;
    private ListViewAdapter adapter;
    private ArrayList<ListViewAdapter> adapters;
    private ArrayList<Term>[] termListArray;

    public ViewPagerAdapter(Context context, ArrayList<Term>[] termListArray) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.termListArray = termListArray;
        for(int i = 0; i < MUN_OF_PAGES; i++) {
            final int nextPage = i < 2 ? i + 1 : i;
//            ListView listView = new ListView(activity);
//            listView.setBackgroundColor(R.color.light_yellow);
            final ArrayList<Term> list = this.termListArray[i];
            final int listNum = i;
            adapter = new ListViewAdapter(this.mContext, list);
            adapters.add(adapter);
//            listView.setAdapter(getAdapter(i));
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    if (position < list.size()) {
//                        Term term = (Term) parent.getItemAtPosition(position);
//                        if (listNum != 2 || id != 0)
//                            setText(term.getText(), listNum);
//                        else
//                            raiseIssueManager.setIsPreview(true);
//                        setItemSelected(listNum, (int) id);
//                        refreshAll();
//                    }
//                    mViewPager.setCurrentItem(nextPage);
//                }
//            });
        }
    }

    @Override
    public int getCount() {
        return MUN_OF_PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_adapter_layout, container, false);
        ListView l = (ListView) itemView.findViewById(R.id.listView);
        l.setAdapter(this.adapters.get(position));
//        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position < termListArray[position].size()) {
//                    Term term = (Term) parent.getItemAtPosition(position);
//                    if (position != 2 || id != 0)
//                        setText(term.getText(), listNum);
//                    else
//                        raiseIssueManager.setIsPreview(true);
//                    setItemSelected(listNum, (int) id);
//                    refreshAll();
//                }
//                mViewPager.setCurrentItem(nextPage);
//            }
//        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
