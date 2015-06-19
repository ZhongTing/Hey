package slm2015.hey.view.tabs.watch;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Selector;
import slm2015.hey.view.component.IssueCard;

public class CardIssueAdapter extends ArrayAdapter<Issue> {
    private static final float[] rotationList = new float[]{4f, 1f, -3f, 1f};

    private Context context;
    private List<Issue> list;
    private List<Issue> filteList = new ArrayList<>();

    private ViewPager viewPager = null;

    private int newLoadCardCount = 0;
    private CardState firstCardState = CardState.NONE;

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

        if (convertView == null) {
            IssueCard card = new IssueCard(this.context);
            card.assignIssueCard(parent, viewPager, issue);
            convertView = card;
            convertView.setRotation(rotationList[(this.getCount() - position - 1) % rotationList.length]);
        }

        ImageView likeImageView = (ImageView) convertView.findViewById(R.id.like_image_view);
        ImageView sosoImageView = (ImageView) convertView.findViewById(R.id.soso_image_view);
        likeImageView.setVisibility(View.INVISIBLE);
        sosoImageView.setVisibility(View.INVISIBLE);
        if (position == 0) {
            switch (firstCardState) {
                case LIKE:
                    likeImageView.setVisibility(View.VISIBLE);
                    break;
                case SOSO:
                    sosoImageView.setVisibility(View.VISIBLE);
                    break;
                case NONE:
                    break;
            }
        }

        convertView.setVisibility(position < newLoadCardCount ? View.INVISIBLE : View.VISIBLE);

        return convertView;
    }

    public void setNewLoadCardCount(int newLoadCardCount) {
        this.newLoadCardCount = newLoadCardCount;
        this.notifyDataSetChanged();
    }

    public void setFirstCardState(CardState firstCardState) {
        this.firstCardState = firstCardState;
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
