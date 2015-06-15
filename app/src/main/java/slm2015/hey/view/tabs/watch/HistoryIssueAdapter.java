package slm2015.hey.view.tabs.watch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class HistoryIssueAdapter extends BaseAdapter {
    private List<Issue> issueList;

    public HistoryIssueAdapter(List<Issue> issueList) {
        setIssueList(issueList);
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    @Override
    public int getCount() {
        return this.issueList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.issueList.get(position);
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
            convertView = inflater.inflate(R.layout.issue_adapter_layout, null);
            holder = new IssueHolder();
            convertView.setTag(holder);
        } else {
            holder = (IssueHolder) convertView.getTag();
        }
        holder.front = (ViewGroup) convertView.findViewById(R.id.front);
        holder.subject = (TextView) convertView.findViewById(R.id.subject);
        holder.description = (TextView) convertView.findViewById(R.id.description);
        holder.place = (TextView) convertView.findViewById(R.id.position);
        holder.like = (ImageView) convertView.findViewById(R.id.like);
        holder.locationImg = (ImageView) convertView.findViewById(R.id.location);
        holder.time = (TextView) convertView.findViewById(R.id.timestamptextview);

        if (upsideDownPosition < this.issueList.size()) {
            Issue issue = this.issueList.get(upsideDownPosition);

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
        }
        final IssueHolder issueHolder = holder;
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                IssueHolder i = issueHolder;
                boolean isLike = !issueList.get(upsideDownPosition).isLike();
                gestureListItem(i.getFront(), isLike);
                issueList.get(upsideDownPosition).setLike(isLike);
                float alpha = isLike ? 1f : 0.2f;
                i.like.setAlpha(alpha);
                return true;
            }
        });
        return convertView;
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
        ImageView like;
        ImageView locationImg;
        TextView time;

        public ViewGroup getFront() {
            return this.front;
        }
    }
}
