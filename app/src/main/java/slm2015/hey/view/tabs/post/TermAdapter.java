package slm2015.hey.view.tabs.post;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Term;

public class TermAdapter extends BaseAdapter {
    private List<Term> termList;
    private int selectPosition = -1;
    private OnTermSelectedListener termSelectedListener;

    public TermAdapter(List<Term> termList) {
        this.setTermList(termList);
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.termList != null ? this.termList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.termList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_adapter_layout, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Term term = termList.get(position);
        holder.text.setText(term.getText());
        holder.text.setSelected(term.isSelected());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = position;
                for (Term t : termList)
                    t.setIsSelected(false);
                term.setIsSelected(true);
                notifyDataSetChanged();
                if (termSelectedListener != null) {
                    termSelectedListener.OnTermSelected(term.getText());
                }
            }
        });
        return convertView;
    }

    public void setOnTermSelectedListener(OnTermSelectedListener listener) {
        this.termSelectedListener = listener;
    }

    public void filter(String filterString) {
        //todo implement filter function
        this.notifyDataSetChanged();
    }

    public interface OnTermSelectedListener {
        public void OnTermSelected(String selectedTerm);
    }

    private class ViewHolder {
        TextView text;
    }
}
