package slm2015.hey.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import slm2015.hey.R;
import slm2015.hey.entity.Term;


public class ListViewAdapter extends BaseAdapter implements Filterable {
    private final int INI_ROW_COUNT = 12;
    private ArrayList<Term> originalData = new ArrayList<Term>();
    private ArrayList<Term> filterData = new ArrayList<Term>();
    private LayoutInflater inflater;
    private Context context;
    private Filter filter;

    public ListViewAdapter(Context context, ArrayList<Term> data) {
        this.context = context;
        this.originalData = data;
        this.filterData = data;
    }

    @Override
    public int getCount() {
        return this.filterData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.filterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        Term term = this.filterData.get(position);
        return this.originalData.indexOf(term);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            this.inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = this.inflater.inflate(R.layout.listview_adapter_layout, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_yellow));
        if (position < filterData.size()) {
            if (this.filterData.get(position).isSelected()) {
                convertView.setBackground(convertView.getResources().getDrawable(R.drawable.term_button));
//                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_blue));
            }
            holder.text.setText(this.filterData.get(position).getText());
        } else {
            holder.text.setText("");
        }
        return convertView;
    }

    public void SetData(final ArrayList<Term> data) {
        originalData = data;
        filterData = data;
        notifyDataSetChanged();
    }

    public boolean checkNewTerm(String content){
        boolean isNewTerm = true;
        for(Term t : this.originalData)
            if(t.getText().equals(content))
                isNewTerm = false;
        return isNewTerm;
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
                            results.values = originalData;
                            results.count = originalData.size();
                        }
                    } else {
                        ArrayList<Term> filterResultsData = new ArrayList<Term>();
                        boolean hasTerm = false, sameWord = false;
                        for (Term term : originalData) {
                            if (term.getText().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                filterResultsData.add(term);
                                hasTerm = true;
                                if(term.getText().toLowerCase().equals(charSequence.toString().toLowerCase()))
                                    sameWord = true;
                            }
                        }
                        if(!sameWord)
                            filterResultsData.add(new Term(charSequence.toString()));
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
                    filterData = (ArrayList<Term>) filterResults.values;
                    if (filterResults.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        return filter;
    }


    private class ViewHolder {
        TextView text;
    }
}
