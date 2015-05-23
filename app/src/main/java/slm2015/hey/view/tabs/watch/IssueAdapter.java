package slm2015.hey.view.tabs.watch;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.view.util.UiUtility;

public class IssueAdapter extends BaseAdapter {
    private List<Issue> issueList;
    private IssueHolder issueHolder;

    public IssueAdapter(List<Issue> issueList) {
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
        IssueHolder holder = null;
//        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.issue_adapter_layout, null);
            holder = new IssueHolder();
            holder.front = (LinearLayout) convertView.findViewById(R.id.front);
            holder.subject = (TextView) convertView.findViewById(R.id.subject);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.position = (TextView) convertView.findViewById(R.id.position);
            convertView.setTag(holder);
//        } else {
//            holder = (IssueHolder) convertView.getTag();
//        }

        if (position < this.issueList.size()) {
            Issue issue = this.issueList.get(position);
            if(issue.isLike())
                holder.getFront().setX(UiUtility.dpiToPixel(50, Resources.getSystem()));
            holder.subject.setText(issue.getSubject());
            holder.description.setText(issue.getDescription());
            holder.position.setText(issue.getPlace());
        } else {
            holder.subject.setText("");
            holder.description.setText("");
            holder.position.setText("");
        }
        final IssueHolder issueHolder = holder;
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                IssueHolder i = issueHolder;
                if (!issueList.get(position).isLike()) {
                    final boolean LIKE = true;
                    gestureListItem(i.getFront(), LIKE);
                    issueList.get(position).setLike(LIKE);
                } else {
                    final boolean CANCEL = false;
                    gestureListItem(i.getFront(), CANCEL);
                    issueList.get(position).setLike(CANCEL);
                }
                return true;
            }
        });
        return convertView;
    }

    private class IssueHolder {
        LinearLayout front;
        TextView subject;
        TextView description;
        TextView position;

        public LinearLayout getFront() {
            return this.front;
        }
    }

    private void gestureListItem(final View rowView, boolean like) {
        final int deltaX = UiUtility.dpiToPixel(50, Resources.getSystem());
        final float fromXDelta = like ? 0 : deltaX;
        final float toXDelta = like ? deltaX : 0;
        final float fromYDelta = 0;
        Animation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, fromYDelta);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setRepeatCount(0);
        rowView.startAnimation(animation);
    }
}
