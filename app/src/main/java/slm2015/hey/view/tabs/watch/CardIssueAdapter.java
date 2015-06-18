package slm2015.hey.view.tabs.watch;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.component.IssueCard;

public class CardIssueAdapter extends ArrayAdapter<Issue> {
    static final float threshold = 0.5f;

    private Context context;
    private List<Issue> list;

    private View topCard = null;
    private ViewPager viewPager = null;

    private int newLoadCardCount = 0;
    private CardState firstCardState = CardState.NONE;

    public CardIssueAdapter(Context context, int resource, ViewPager viewPager, List<Issue> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.viewPager = viewPager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Issue issue = this.list.get(this.getCount() - position - 1);

        if (convertView == null) {
            IssueCard card = new IssueCard(this.context, parent, viewPager, issue);
            convertView = card.getView();
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
        if (this.list.size() > 0) {
            this.list.remove(this.getCount() - 1);
            this.notifyDataSetChanged();
        }
    }

    public enum CardState {
        LIKE,
        SOSO,
        NONE
    }
}
