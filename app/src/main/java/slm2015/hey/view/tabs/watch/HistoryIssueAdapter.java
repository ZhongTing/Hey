package slm2015.hey.view.tabs.watch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.core.issue.IssueHandler;
import slm2015.hey.entity.Issue;
import slm2015.hey.entity.Selector;

public class HistoryIssueAdapter extends BaseAdapter {
    private IssueHandler issueHandler;
    private List<Issue> issueList;
    private List<Issue> filterList = new ArrayList<>();
    private ArrayList<Selector> selectors;
    private CardIssueAdapter.CardState cardState = CardIssueAdapter.CardState.NONE;

    public HistoryIssueAdapter(Context context, List<Issue> issueList) {
        this.issueHandler = new IssueHandler(context);
        setIssueList(issueList);
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
        this.filterList.addAll(issueList);
    }

    @Override
    public int getCount() {
        return this.filterList.size();
    }

    @Override
    public Object getItem(int position) {
        final int upsideDownPosition = getCount() - position - 1;
        return this.filterList.get(upsideDownPosition);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        IssueHolder holder;
        final int upsideDownPosition = getCount() - position - 1;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_issue_adapter_layout, null);
            holder = new IssueHolder();
            convertView.setTag(holder);
        } else {
            holder = (IssueHolder) convertView.getTag();
        }
        holder.front = (ViewGroup) convertView.findViewById(R.id.front);
        holder.subject = (TextView) convertView.findViewById(R.id.subject);
        holder.description = (TextView) convertView.findViewById(R.id.description);
        holder.place = (TextView) convertView.findViewById(R.id.position);
        holder.like = (ImageButton) convertView.findViewById(R.id.like);
        holder.locationImg = (ImageView) convertView.findViewById(R.id.location);
        holder.time = (TextView) convertView.findViewById(R.id.timestamptextview);
        holder.likeCount = (TextView) convertView.findViewById(R.id.like_count);

        if (upsideDownPosition < this.filterList.size()) {
            Issue issue = this.filterList.get(upsideDownPosition);

            if (issue.getPlace().equals("")) {
                holder.locationImg.setVisibility(View.GONE);
                holder.place.setVisibility(View.GONE);
            } else {
                holder.locationImg.setVisibility(View.VISIBLE);
                holder.place.setVisibility(View.VISIBLE);
                holder.place.setText(issue.getPlace());
            }

            float alpha = issue.isLike() ? 1f : 0.2f;
            holder.like.setAlpha(alpha);
            holder.subject.setText(issue.getSubject());
            holder.description.setText(issue.getDescription());
            holder.time.setText(issue.getTimestamp());
            holder.likeCount.setText(String.valueOf(issue.getLikeCount()));
        }
        final IssueHolder issueHolder = holder;
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IssueHolder i = issueHolder;
                Issue issue = filterList.get(upsideDownPosition);
                boolean isLike = !issue.isLike();
                gestureListItem(i.getFront(), isLike);
                issue.setLike(isLike);
                syncIssueLikeToServer(issue);
                float alpha = isLike ? 1f : 0.2f;
                i.like.setAlpha(alpha);
            }
        });
//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
        return convertView;
    }

    private void syncIssueLikeToServer(Issue issue) {
        if (issue.isLike()) {
            this.issueHandler.like(issue.getId());
        } else {
            this.issueHandler.regretLike(issue.getId());
        }
    }

    public void setFilter(ArrayList<Selector> selectors) {
        this.selectors = selectors;
        this.filterList.clear();
        if (!noFilter(selectors)) {
            for (Issue issue : this.issueList) {
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
        } else {
            this.filterList.addAll(this.issueList);
        }
        ArrayList<Issue> removeIssues = new ArrayList<>();
        for (Issue issue : filterList) {
            switch (this.cardState) {
                case LIKE:
                    if (!issue.isLike())
                        removeIssues.add(issue);
                    break;
                case SOSO:
                    if (issue.isLike())
                        removeIssues.add(issue);
                    break;
            }
        }
        this.filterList.removeAll(removeIssues);
        notifyDataSetChanged();
    }

    public void setCardState(CardIssueAdapter.CardState cardState) {
        this.cardState = cardState;
        setFilter(this.selectors);
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
        this.filterList.addAll(this.issueList);
        notifyDataSetChanged();
    }

    private void gestureListItem(final View rowView, boolean like) {
//        final int deltaX = UiUtility.dpiToPixel(50, Resources.getSystem());
//        final float fromXDelta = like ? 0 : deltaX;
//        final float toXDelta = like ? deltaX : 0;
//        final float fromYDelta = 0;
//        Animation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, fromYDelta);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        animation.setRepeatCount(0);
//        rowView.startAnimation(animation);
    }

    private class IssueHolder {
        ViewGroup front;
        TextView subject;
        TextView description;
        TextView place;
        ImageButton like;
        ImageView locationImg;
        TextView time;
        TextView likeCount;

        public ViewGroup getFront() {
            return this.front;
        }
    }
}
