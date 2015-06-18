package slm2015.hey.view.tabs.watch;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import slm2015.hey.entity.Issue;
import slm2015.hey.view.component.IssueCard;

public class CardIssueAdapter extends ArrayAdapter<Issue> {
    static final float threshold = 0.5f;

    private Context context;
    private List<Issue> list;

    private View topCard = null;
    private ViewPager viewPager = null;

    private int newLoadCardCount = 0;

    public CardIssueAdapter(Context context, int resource, ViewPager viewPager, List<Issue> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.viewPager = viewPager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Issue issue = this.list.get(this.getCount() - position - 1);

        if (convertView == null)
            convertView = (new IssueCard(this.context, parent, viewPager, issue)).getView();

        if (position < newLoadCardCount)
            convertView.setVisibility(View.INVISIBLE);
        else
            convertView.setVisibility(View.VISIBLE);

        return convertView;
    }

    public void setNewLoadCardCount(int newLoadCardCount) {
        this.newLoadCardCount = newLoadCardCount;
        this.notifyDataSetChanged();
    }

    public void setFirstCardState(float f) {
        String test;
        if (topCard != null) {
            if (f > threshold)
                test = "Good";
            else if (f < -threshold)
                test = "SoSo";
            else
                test = "!!!!";
        }
    }

    public void removeFirst() {
        if (this.list.size() > 0) {
            this.list.remove(this.getCount() - 1);
            this.notifyDataSetChanged();
        }
    }
}
