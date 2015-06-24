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
    private static final float[] rotationList = new float[]{4f, 1f, -3f, 1f};

    private Context context;
    private List<Issue> list;
    private List<Issue> filterList = new ArrayList<>();
    private List<Issue> popularList;

    private ViewPager viewPager = null;

    private int newLoadCardCount = 0;
    private CardState firstCardState = CardState.NONE;
    private boolean showPopular = false;
    private ArrayList<Selector> selectors;

    public CardIssueAdapter(Context context, int resource, ViewPager viewPager, List<Issue> list, List<Issue> popularList) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.filterList.addAll(list);
        this.viewPager = viewPager;
        this.popularList = popularList;
    }

    @Override
    public int getCount() {
        return this.filterList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IssueCard card = (IssueCard) convertView;

        if (card == null) {
            if (this.getCount() > 0) {
                Issue issue = this.filterList.get(this.getCount() - position - 1);
                card = new IssueCard(this.context);
                card.assignIssueCard(parent, viewPager, issue);
                card.setRotation(rotationList[(this.getCount() - position - 1) % rotationList.length]);
            }
        }

        if (position == 0) {
            switch (firstCardState) {
                case LIKE:
                    card.showLike();
                    break;
                case SOSO:
                    card.showSoSo();
                    break;
                case NONE:
                    card.hideLikeAndSoSo();
                    break;
            }
        }

        card.setVisibility(position < newLoadCardCount ? View.INVISIBLE : View.VISIBLE);

        return card;
    }

    public void setNewLoadCardCount(int newLoadCardCount) {
        this.newLoadCardCount = newLoadCardCount;
        this.notifyDataSetChanged();
    }

    public void setFirstCardState(CardState firstCardState) {
        this.firstCardState = firstCardState;
    }

    public Issue getFilterListFirstIssue() {
        return this.filterList.get(this.getCount() - 1);
    }

    public void removeFirst() {
        if (getCount() > 0) {
            Issue issue = this.getFilterListFirstIssue();
            this.filterList.remove(issue);
            this.list.remove(issue);
            this.notifyDataSetChanged();
        }
    }

    public void setFilter(ArrayList<Selector> selectors) {
        this.selectors = selectors;
        this.filterList.clear();
        if (!noFilter(selectors)) {
            for (Issue issue : this.list) {
                boolean addToList = false;
                for (Selector selector : selectors) {
                    String content = selector.getContent();
                    boolean contains = (issue.getSubject().toLowerCase().contains(content.toLowerCase()) || issue.getDescription().toLowerCase().contains(content.toLowerCase()));
                    addToList = selector.isFilter() && contains;
                    if (addToList) {
                        this.filterList.add(issue);
                        break;
                    }
                }
            }
        } else
            this.filterList.addAll(this.list);
        if (this.showPopular)
            filterByPopular();
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
        this.filterList.clear();
        this.filterList.addAll(this.list);
        notifyDataSetChanged();
    }

    public void setShowPopular(boolean showPopular) {
        this.showPopular = showPopular;
        if (showPopular) {
            filterByPopular();
            notifyDataSetChanged();
        } else {
            setFilter(this.selectors);
        }
    }

    public void filterByPopular() {
        ArrayList<Issue> needToSortIssues = new ArrayList<>();
        for (Issue issueInPopList : this.popularList) {
            for (Issue issue : this.filterList) {
                if (issueInPopList.getId().equals(issue.getId())) {
                    needToSortIssues.add(issue);
                }
            }
        }
        this.filterList.removeAll(needToSortIssues);
        for (Issue issue : needToSortIssues) {
            this.filterList.add(issue);
        }
    }

    public enum CardState {
        LIKE,
        SOSO,
        NONE
    }
}
