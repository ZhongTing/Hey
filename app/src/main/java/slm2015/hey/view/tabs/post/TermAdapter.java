package slm2015.hey.view.tabs.post;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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
        if (convertView == null) {
            convertView = new TextView(parent.getContext());
        }
        ((TextView) convertView).setText(termList.get(position).getText());
        if (selectPosition == position) {
            //todo implement selected effect
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = position;
                if (termSelectedListener!=null ){
                    termSelectedListener.OnTermSelected(termList.get(position).getText());
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

    public interface OnTermSelectedListener{
        public void OnTermSelected(String selectedTerm);
    }
}
