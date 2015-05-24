package slm2015.hey.view.tabs.post;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Term;

public class TermAdapter extends BaseAdapter implements Filterable {
    private List<Term> termList;
    private List<Term> filterList;
    private int selectPosition = -1;
    private OnTermSelectedListener termSelectedListener;
    private Filter filter;

    public TermAdapter(List<Term> termList) {
        this.setTermList(termList);
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
        if (termList != null)
            this.filterList = new ArrayList<Term>(this.termList);
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
        if (position < this.filterList.size()) {
            final Term term = this.filterList.get(position);
            this.selectPosition = position;
            holder.text.setSelected(term.isSelected());
            holder.text.setText(term.getShowText());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < TermAdapter.this.filterList.size()) {
                        for (Term t : termList)
                            t.setIsSelected(false);
                        term.setIsSelected(true);
                        if (term.needToAdd())
                            termList.add(term);
                        if (termSelectedListener != null) {
                            termSelectedListener.OnTermSelected(term.getText());
                        }
                        filterList = termList;
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            holder.text.setText("");
            holder.text.setSelected(false);
        }
        return convertView;
    }

    public void setOnTermSelectedListener(OnTermSelectedListener listener) {
        this.termSelectedListener = listener;
    }

    public void filter(CharSequence filterString) {
        //todo implement filter function
        this.filterList.clear();
        if (filterString != null) {
            for (Term t : this.termList) {
                if (t.getText().toLowerCase().contains(filterString.toString().toLowerCase()))
                    this.filterList.add(t);
            }
        }
//        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (this.filter == null)
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();
                    if (charSequence == null || charSequence.toString().isEmpty()) {
                        synchronized (results) {
                            results.values = termList;
                            results.count = termList.size();
                        }
                    } else {
                        ArrayList<Term> filterResultsData = new ArrayList<Term>();
                        boolean sameWord = false;
                        for (Term term : termList) {
                            if (term.getText().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                filterResultsData.add(term);
                                if (term.getText().toLowerCase().equals(charSequence.toString().toLowerCase()))
                                    sameWord = true;
                            }
                        }
                        if (!sameWord)
                            filterResultsData.add(new Term(charSequence.toString(), true));
                        synchronized (results) {
                            results.values = filterResultsData;
                            results.count = filterResultsData.size();
                        }
                    }

                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filterList = (ArrayList<Term>) filterResults.values;
                    if (filterResults.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        return filter;
    }

    public interface OnTermSelectedListener {
        void OnTermSelected(String selectedTerm);
    }

    private class ViewHolder {
        TextView text;
    }
}
