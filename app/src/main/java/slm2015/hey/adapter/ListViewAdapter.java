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

    public ListViewAdapter(Context context, ArrayList<Term> data) {
        this.context = context;
        this.originalData = data;
        this.filterData = data;
    }

    @Override
    public int getCount() {
        if (this.filterData.size() < INI_ROW_COUNT)
            return INI_ROW_COUNT;
        return this.filterData.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= filterData.size())
            return new Term("");
        return this.filterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_blue));
            }
            holder.text.setText(this.filterData.get(position).getTerm());
        } else {
            holder.text.setText("");
        }
        return convertView;
    }

    public void SetData(ArrayList<Term> data) {
        this.originalData = data;
        this.filterData = data;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence == null || charSequence.toString().isEmpty()) {
                    results.values = originalData;
                    results.count = originalData.size();
                } else {
                    ArrayList<Term> filterResultsData = new ArrayList<Term>();

                    for (Term term : originalData) {
                        if (term.getTerm().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterResultsData.add(term);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
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
    }


    private class ViewHolder {
        TextView text;
    }
}
