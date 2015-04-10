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


public class ListViewAdapter extends BaseAdapter {
    private ArrayList<String> data = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;
    private int selectId = -1;

    public ListViewAdapter(Context context, ArrayList<String> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Object getItem(int position) {
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
        if(convertView.isSelected()){
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.light_blue));
        }
        holder.text.setText(this.data.get(position));
        return convertView;
    }

    public void SetData(ArrayList<String> data){
        this.data = data;
    }

    private class ViewHolder{
        TextView text;
    }

    public void SetSelectId(int id){
        this.selectId = id;
    }
}
