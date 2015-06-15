package slm2015.hey.view.selector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Selector;

public class SelectorAdapter extends BaseAdapter {
    private List<Selector> selectorList = new ArrayList<Selector>();

    @Override
    public int getCount() {
        return this.selectorList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.selectorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.selector_adapter_layout, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.selector_text);
            holder.notify = (ImageButton) convertView.findViewById(R.id.notify);
            holder.selector = (ImageButton) convertView.findViewById(R.id.selector);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Selector selector = this.selectorList.get(position);
        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean notify = !holder.notify.isSelected();
                holder.notify.setSelected(notify);
                selector.setNotify(notify);
            }
        });
        holder.selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean filter = !holder.selector.isSelected();
                holder.selector.setSelected(filter);
                selector.setFilter(filter);
            }
        });

        String text = selector.getContent();
        holder.text.setText(text);
        holder.selector.setSelected(selector.isFilter());
        holder.notify.setSelected(selector.isNotify());
        return convertView;
    }

    private class ViewHolder {
        ImageButton notify;
        TextView text;
        ImageButton selector;
    }

    public void addSelector(Selector selector) {
        this.selectorList.add(selector);
        notifyDataSetChanged();
    }
}
