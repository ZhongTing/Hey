package slm2015.hey.view.tabs.watch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;

public class CardIssueAdapter extends ArrayAdapter<Issue> {
    private Context context;
    private List<Issue> list;

    public CardIssueAdapter(Context context, int resource, List<Issue> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Issue issue = new Issue();
        if (this.hasImage(issue)) {
            convertView = inflater.inflate(R.layout.card, parent, false);

        } else {
            convertView = inflater.inflate(R.layout.card_no_pic, parent, false);

        }
        return convertView;
    }

    public void removeFirst() {
        if (this.list.size() > 0) {
            this.list.remove(0);
            this.notifyDataSetChanged();
        }
    }

    private boolean hasImage(Issue issue) {
        return issue.getImage() == null && issue.getPhotoURL() == null;
    }
}
