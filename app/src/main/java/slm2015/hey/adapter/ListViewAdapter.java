package slm2015.hey.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import slm2015.hey.R;
import slm2015.hey.entity.Term;


public class ListViewAdapter extends BaseAdapter {
    private final int INI_ROW_COUNT = 12;
    private ArrayList<Term> data = new ArrayList<Term>();
    private LayoutInflater inflater;
    private Context context;

    public ListViewAdapter(Context context, ArrayList<Term> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        if(this.data.size() < INI_ROW_COUNT)
            return INI_ROW_COUNT;
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
        if(position >= data.size())
            return new Term("");
        return this.data.get(position);
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
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_yellow));
        if(position < data.size()){
            if(this.data.get(position).isSelected()){
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_blue));
            }
            holder.text.setText(this.data.get(position).getTerm());
        }else{
            holder.text.setText("");
        }
        return convertView;
    }

    public void SetData(ArrayList<Term> data){
        this.data = data;
    }

    private class ViewHolder{
        TextView text;
    }
}
