package slm2015.hey.view.tabs.watch;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Selector;
import slm2015.hey.view.component.IssueCard;

public class CardIssueAdapter extends ArrayAdapter<Issue> {
    static final float threshold = 0.5f;

    private Context context;
    private List<Issue> list;
    private List<Issue> filteList = new ArrayList<>();

    private View topCard = null;
    private ViewPager viewPager = null;

    private int newLoadCardCount = 0;

    public CardIssueAdapter(Context context, int resource, ViewPager viewPager, List<Issue> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.filteList.addAll(list);
        this.viewPager = viewPager;
    }

    @Override
    public int getCount() {
        return this.filteList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Issue issue = this.filteList.get(this.getCount() - position - 1);

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
        if (this.filteList.size() > 0) {
            Issue issue = this.filteList.get(this.getCount() - 1);
            this.filteList.remove(issue);
            this.list.remove(issue);
            this.notifyDataSetChanged();
        }
    }

    public void setFilter(ArrayList<Selector> selectors) {
        this.filteList.clear();
        if (!noFilter(selectors)) {
            for (Issue issue : this.list) {
                boolean contains = false;
                for (Selector selector : selectors) {
                    String content = selector.getContent();
                    contains = selector.isFilter() && (issue.getSubject().contains(content) || issue.getDescription().contains(content));
                    if (contains) {
                        this.filteList.add(issue);
                        break;
                    }
                }
            }
        }else
            this.filteList.addAll(this.list);
        notifyDataSetChanged();
    }

    private boolean noFilter(ArrayList<Selector> selectors) {
        for (Selector selector : selectors) {
            if (selector.isFilter())
                return false;
        }
        return true;
    }

    public void setList() {
//        this.list = list;
        this.filteList.clear();
        this.filteList.addAll(this.list);
        notifyDataSetChanged();
    }
}
