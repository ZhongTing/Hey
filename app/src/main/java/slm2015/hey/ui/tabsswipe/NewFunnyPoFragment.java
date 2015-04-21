package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import slm2015.hey.R;
import slm2015.hey.adapter.ListViewAdapter;
import slm2015.hey.entity.Term;
import slm2015.hey.manager.RaiseIssueManager;
import slm2015.hey.ui.IssuePopupWindow;

public class NewFunnyPoFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private Activity activity;
    private MyPagerAdapter mPagerAdapter;
    private LayoutInflater inflater;
    private ListViewAdapter adapter;
    private RaiseIssueManager raiseIssueManager;
    private Button nounButton, adjButton, locationButton;
    private EditText informTxtField;
    private ArrayList<Button> buttonMap;
    private View popupView;
    private IssuePopupWindow window;
    private ArrayList<ListView> lv;
    private ArrayList<ListViewAdapter> adapters;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.raiseIssueManager = new RaiseIssueManager();
        this.buttonMap = new ArrayList<Button>();
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = this.inflater.inflate(R.layout.new_funnypo_layout, container, false);
        this.popupView = inflater.inflate(R.layout.raise_issue, null);
        initializeInformTxtField(view);
        initializeNounButton(view);
        initializeAdjButton(view);
        initializeLocationButton(view);
        refreshState();
        initializeListViewViewPager(view);
        return view;
    }

    private void initializeListViewViewPager(View view){
        this.mViewPager = (ViewPager) view.findViewById(R.id.viewpagerforfunnypo);
        this.mViewPager.setOnPageChangeListener(this);
        ArrayList<Term>[] termListArray = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            termListArray[i] = this.raiseIssueManager.getList(i);
        }
        this.mPagerAdapter = new MyPagerAdapter(this.activity, termListArray);
        try {
            this.mViewPager.setAdapter(this.mPagerAdapter);
        } catch (Exception e) {
            System.out.print(e);
        }
        this.mViewPager.setCurrentItem(0);
    }

    private void initializeInformTxtField(View view) {
        this.informTxtField = (EditText) view.findViewById(R.id.informtextfield);
        this.informTxtField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAdapter(raiseIssueManager.getIssuePosNum()).getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                heyButtonObserver();
            }
        });
    }

    private void initializeNounButton(View view) {
        this.nounButton = (Button) view.findViewById(R.id.noun_button);
        this.buttonMap.add(this.nounButton);
        this.nounButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseIssueManager.setIssuePosNum(0);
                refreshListView();
            }
        });
    }

    private void initializeAdjButton(View view) {
        this.adjButton = (Button) view.findViewById(R.id.adj_button);
        this.buttonMap.add(this.adjButton);
        this.adjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseIssueManager.setIssuePosNum(1);
                refreshListView();
            }
        });
    }

    private void initializeLocationButton(View view) {
        this.locationButton = (Button) view.findViewById(R.id.location_button);
        this.buttonMap.add(this.locationButton);
        this.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseIssueManager.setIssuePosNum(2);
                refreshListView();
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        scaleButton(i);
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    private synchronized ListViewAdapter getAdapter(int position) {
        return this.adapters.get(position);
    }

    private void setItemSelectedFalse(int listNum) {
        ArrayList<Term> termList = this.raiseIssueManager.getList(listNum);
        for (Term term : termList)
            term.setIsSelected(false);
    }

    private void setItemSelected(int listNum, int position) {
        if (position != -1) {
            setItemSelectedFalse(listNum);
            ArrayList<Term> termList = this.raiseIssueManager.getList(listNum);
            termList.get(position).setIsSelected(true);
        }
    }

    private void setText(String text, int listNum) {
        setItemSelectedFalse(listNum);
        if(!text.isEmpty()){
            checkNewTerm(text);
            this.buttonMap.get(listNum).setText(text);
        }else if(listNum == 2)
            this.buttonMap.get(listNum).setText("P");
        setIssue(text);
    }

    private void refreshAll() {
        refreshListView();
        refreshState();
    }

    private void refreshListView() {
        int listNum = this.raiseIssueManager.getIssuePosNum();
        ArrayList<Term> showList = this.raiseIssueManager.getList(listNum);
        getAdapter(listNum).SetData(showList);
        mViewPager.setCurrentItem(listNum);
        scaleButton(listNum);
    }

    private void scaleButton(int listNum) {
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(width, height);
        p1.weight = 1;
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(width, height);
        p.weight = 100;

        p1.setMargins(margin, margin, margin, margin);
        p.setMargins(margin, margin, margin, margin);
        for (Button b : this.buttonMap) {
            b.setLayoutParams(p1);
            b.requestLayout();
        }
        this.buttonMap.get(listNum).setLayoutParams(p);
        this.buttonMap.get(listNum).requestLayout();
    }

    private void refreshState() {
        this.adjButton.setEnabled(raiseIssueManager.adjButtonEnable());
        this.locationButton.setEnabled(raiseIssueManager.locationButtonEnable());
        this.informTxtField.getText().clear();
        if (this.raiseIssueManager.isPreview()) {
            closeKeyboard();
            this.raiseIssueManager.setIsPreview(false);
            int length = RelativeLayout.LayoutParams.FILL_PARENT;
            String text = this.raiseIssueManager.getIssueInString();
            this.window = new IssuePopupWindow(this.popupView, this.raiseIssueManager.getIssue(), length, length);
            this.window.getCameraButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

    public void changeKeyboardVisible(boolean open) {
        if (open) {
            this.informTxtField.requestFocus();
            openKeyboard();
        } else
            closeKeyboard();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.informTxtField.getWindowToken(), 0);
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.informTxtField, InputMethodManager.SHOW_IMPLICIT);
    }

    private void checkNewTerm(String content) {
        boolean isNewTerm = getAdapter(raiseIssueManager.getIssuePosNum()).checkNewTerm(content);
        if (isNewTerm)
            this.raiseIssueManager.addTerm(content);
    }

    private void setIssue(String text) {
        int issuePosNum = this.raiseIssueManager.getIssuePosNum();
        this.raiseIssueManager.setIssue(text);
        this.raiseIssueManager.setIssuePosNum(issuePosNum + 1);
    }

    private class MyPagerAdapter extends PagerAdapter {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private final int MUN_OF_PAGES = 3;
        private ListViewAdapter adapter;
        private ArrayList<Term>[] termListArray;

        public MyPagerAdapter(Context context, ArrayList<Term>[] termListArray) {
            this.mContext = context;
            this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            adapters = new ArrayList<ListViewAdapter>();
            this.termListArray = termListArray;
            for (int i = 0; i < MUN_OF_PAGES; i++) {
                final ArrayList<Term> list = this.termListArray[i];
                final int listNum = i;
                adapter = new ListViewAdapter(this.mContext, list);
                adapters.add(adapter);
            }
        }

        @Override
        public int getCount() {
            return MUN_OF_PAGES;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_adapter_layout, container, false);
            final int listNum = position;
            final int nextPage = listNum < 2 ? listNum + 1 : listNum;
            ListView l = (ListView) itemView.findViewById(R.id.listView);
            l.setAdapter(adapters.get(listNum));
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position < termListArray[listNum].size()) {
                        Term term = (Term) parent.getItemAtPosition(position);
                        if(listNum == 2)
                            raiseIssueManager.setIsPreview(true);
                        if (listNum != 2 || id != 0)
                            setText(term.getTerm(), listNum);
                        else
                            setText("", listNum);
                        setItemSelected(listNum, (int) id);
                        refreshAll();
                    }
                    mViewPager.setCurrentItem(nextPage);
                }
            });

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

}
