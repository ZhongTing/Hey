package slm2015.hey.view.tabs.watch;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

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
        final int upsideDownPosition = getCount() - position - 1;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
        convertView = inflater.inflate(R.layout.issue_adapter_layout, null);
        holder = new IssueHolder();
        holder.front = (LinearLayout) convertView.findViewById(R.id.front);
        holder.subject = (TextView) convertView.findViewById(R.id.subject);
        holder.description = (TextView) convertView.findViewById(R.id.description);
        holder.place = (TextView) convertView.findViewById(R.id.position);
        holder.like = (ImageView) convertView.findViewById(R.id.like);
        holder.time = (TextView) convertView.findViewById(R.id.timestamptextview);
        Log.e("****", "cccc");
        convertView.setTag(holder);
//        } else {
//            holder = (IssueHolder) convertView.getTag();
//        }

        if (upsideDownPosition < this.issueList.size()) {
            Issue issue = this.issueList.get(upsideDownPosition);
            if (issue.isLike()) {
                gestureListItem(holder.getFront(), true);
                holder.like.setVisibility(View.VISIBLE);
//                holder.front.setX(UiUtility.dpiToPixel(50, Resources.getSystem()));
                Log.e("****", "AAAA");
            }
            holder.subject.setText(issue.getSubject());
            holder.description.setText(issue.getDescription());
            holder.place.setText(issue.getPlace());
            holder.time.setText(issue.getTimestamp());


        } else {
            holder.subject.setText("");
            holder.description.setText("");
            holder.place.setText("");
            holder.time.setText("");
            Log.e("****", "BBBB");
        }
        final IssueHolder issueHolder = holder;
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                IssueHolder i = issueHolder;
                boolean isLike = !issueList.get(upsideDownPosition).isLike();
                gestureListItem(i.getFront(), isLike);
                issueList.get(upsideDownPosition).setLike(isLike);
                int visible = isLike ? View.VISIBLE : View.INVISIBLE;
                i.like.setVisibility(visible);
                return true;
            }
        });
        return convertView;
    }

    private class IssueHolder {
        LinearLayout front;
        TextView subject;
        TextView description;
        TextView place;
        ImageView like;
        TextView time;

        public LinearLayout getFront() {
            return this.front;
        }
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
}
