package slm2015.hey.view.tabs.watch;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.core.issue.IssueHandler;
import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Selector;
import slm2015.hey.view.component.IssueCard;

public class CardIssueAdapter extends ArrayAdapter<Issue> {
    private static final float[] rotationList = new float[]{4f, 1f, -3f, 1f};

    private Context context;
    private List<Issue> list;
    private List<Issue> filteList = new ArrayList<>();
    private IssueHandler issueHandler;

    private ViewPager viewPager = null;

    private int newLoadCardCount = 0;
    private CardState firstCardState = CardState.NONE;

    public CardIssueAdapter(Context context, int resource, ViewPager viewPager, List<Issue> list) {
        super(context, resource, list);
        this.issueHandler = new IssueHandler(context);
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
        IssueCard card = (IssueCard) convertView;

        if (card == null) {
            card = new IssueCard(this.context);
            card.assignIssueCard(parent, viewPager, issue);
            card.setRotation(rotationList[(this.getCount() - position - 1) % rotationList.length]);
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
        if (getCount() > 0) {
            this.firstCardState = firstCardState;
            Issue issue = this.filteList.get(this.getCount() - 1);
            issue.setLike(firstCardState == CardState.LIKE);
            if (firstCardState == CardState.LIKE)
                this.issueHandler.like(issue.getId());
        }
    }

    public void removeFirst() {
        if (getCount() > 0) {
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
                boolean addToList = false;
                for (Selector selector : selectors) {
                    String content = selector.getContent();
                    boolean contains = (issue.getSubject().toLowerCase().contains(content.toLowerCase()) || issue.getDescription().toLowerCase().contains(content.toLowerCase()));
                    addToList = selector.isFilter() && contains;
                    if (addToList) {
                        this.filteList.add(issue);
                        break;
                    }
                }
            }
        } else
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

    public enum CardState {
        LIKE,
        SOSO,
        NONE
    }
}
