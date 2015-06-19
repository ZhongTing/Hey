package slm2015.hey.view.selector;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import slm2015.hey.R;
import slm2015.hey.entity.Selector;
import slm2015.hey.view.util.UiUtility;

public class SelectorAdapter extends BaseAdapter {
    private List<Selector> selectorList = new ArrayList<Selector>();
    private OnSelectorChangeListener onSelectorChangeListener;

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
            holder.rowview = (RelativeLayout) convertView.findViewById(R.id.rowview);
            holder.foreground = (LinearLayout) convertView.findViewById(R.id.foreground);
            holder.text = (TextView) convertView.findViewById(R.id.selector_text);
            holder.notify = (ImageButton) convertView.findViewById(R.id.notify);
            holder.selector = (ImageButton) convertView.findViewById(R.id.selector);
            holder.delete = (ImageButton) convertView.findViewById(R.id.delete);
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
                if(SelectorAdapter.this.onSelectorChangeListener != null)
                    SelectorAdapter.this.onSelectorChangeListener.OnFilterChange();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder, selector);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteButton(holder.foreground);
                return true;
            }
        });

        String text = selector.getContent();
        holder.text.setText(text);
        holder.selector.setSelected(selector.isFilter());
        holder.notify.setSelected(selector.isNotify());
        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rowview;
        LinearLayout foreground;
        ImageButton notify;
        TextView text;
        ImageButton selector;
        ImageButton delete;
    }

    public void addSelector(Selector selector) {
        this.selectorList.add(selector);
        notifyDataSetChanged();
    }

    public void setOnSelectorChangeListener(OnSelectorChangeListener onSelectorChangeListener) {
        this.onSelectorChangeListener = onSelectorChangeListener;
    }

    public interface OnSelectorChangeListener {
        void OnFilterChange();
    }

    final int SWIPE_TO_LEFT = -50;

    private void showDeleteButton(View rowView){
        final int fromX = rowView.getX() == 0 ? 0 : SWIPE_TO_LEFT;
        final int toX = rowView.getX() == 0 ? SWIPE_TO_LEFT : 0;
        final int fromXDelta = UiUtility.dpiToPixel(fromX, Resources.getSystem());
        final int toXDelta = UiUtility.dpiToPixel(toX, Resources.getSystem());
        ObjectAnimator anim = ObjectAnimator.ofFloat(rowView, "translationX", fromXDelta,toXDelta);
        anim.start();
    }

    public void removeItem(final ViewHolder holder, final Selector selector){
        final int fromXDelta = UiUtility.dpiToPixel(0, Resources.getSystem());
        final int toXDelta = UiUtility.dpiToPixel(-200, Resources.getSystem());
        final float fromYDelta = 0;
        Animation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, fromYDelta);
        animation.setDuration(500);
        animation.setRepeatCount(0);
//        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                showDeleteButton(holder.foreground);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SelectorAdapter.this.selectorList.remove(selector);
                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.rowview.startAnimation(animation);
    }
}
