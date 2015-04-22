package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class FunnyPoFragment extends Fragment {

    private ListView listView;
    private EditText informTxtField;
    //    private ImageButton heyButton;
    private Button nounButton, adjButton, locationButton;
    private ArrayList<Button> buttonMap;
    private RaiseIssueManager raiseIssueManager;
    private ListViewAdapter adapter;
    private View popupView;
    private Activity activity;
    private IssuePopupWindow window;

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
        View view = inflater.inflate(R.layout.funnypo_layout, container, false);
        this.popupView = inflater.inflate(R.layout.raise_issue, null);
        initializeInformTxtField(view);
        initializeListView(view);
//        initializeHeyButton(view);
        initializeNounButton(view);
        initializeAdjButton(view);
        initializeLocationButton(view);
        refreshState();
        return view;
    }

//    private void initializeHeyButton(View view) {
//        this.heyButton = (ImageButton) view.findViewById(R.id.hey_button);
//        this.heyButton.setEnabled(false);
//        this.heyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int listNum = raiseIssueManager.getIssuePosNum();
//                String text = informTxtField.getText().toString();
//                if (listNum == 2)
//                    raiseIssueManager.setIsPreview(true);
//                if (!text.isEmpty()) {
//                    setText(text, listNum);
//                }
//                refreshAll();
//
//            }
//        });
//    }

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

    public void changeKeyboardVisible(boolean open) {
        if (open) {
            this.informTxtField.requestFocus();
            openKeyboard();
        } else
            closeKeyboard();
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

    private void refreshAll() {
        refreshListView();
        refreshState();
    }

    private void refreshListView() {
        int listNum = this.raiseIssueManager.getIssuePosNum();
        ArrayList<Term> showList = this.raiseIssueManager.getList(listNum);
        getAdapter().SetData(showList);
        scaleButton(listNum);
    }

    private void refreshState() {
        this.adjButton.setEnabled(raiseIssueManager.adjButtonEnable());
        this.locationButton.setEnabled(raiseIssueManager.locationButtonEnable());
        this.informTxtField.getText().clear();
//        heyButtonObserver();
        if (this.raiseIssueManager.isPreview()) {
            closeKeyboard();
            this.raiseIssueManager.setIsPreview(false);
            int length = RelativeLayout.LayoutParams.FILL_PARENT;
            String text = this.raiseIssueManager.getIssueInString();
            this.window = new IssuePopupWindow(this.getActivity(), this.popupView, this.raiseIssueManager.getIssue(), length, length);
            this.window.getCameraButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, 0);
                }
            });
        }
    }

    private void initializeListView(View view) {
        this.listView = (ListView) view.findViewById(R.id.noun_listView);
        if (this.listView.getAdapter() == null) {
            final int NOUNLIST = 0;
            final int LOCATIONLIST = 2;
            this.adapter = new ListViewAdapter(getActivity().getApplicationContext(), raiseIssueManager.getList(NOUNLIST));
            this.listView.setAdapter(getAdapter());
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int listNum = raiseIssueManager.getIssuePosNum();
                    ListView listView = (ListView) parent;
                    if (position < raiseIssueManager.getList(listNum).size()) {
                        Term term = (Term) listView.getItemAtPosition(position);
                        if (listNum != LOCATIONLIST || id != 0)
                            setText(term.getTerm(), listNum);
                        else
                            raiseIssueManager.setIsPreview(true);
                        setItemSelected(listNum, (int) id);
                        refreshAll();
                    }
                }
            });
        }
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

    private void setText(String text, int listNum) {
        setIssue(text);
        this.buttonMap.get(listNum).setText(text);
        setItemSelectedFalse(listNum);
        checkNewTerm(text);
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

    private void setIssue(String text) {
        int issuePosNum = this.raiseIssueManager.getIssuePosNum();
        this.raiseIssueManager.setIssue(text);
        this.raiseIssueManager.setIssuePosNum(issuePosNum + 1);
    }

    private void checkNewTerm(String content) {
        boolean isNewTerm = getAdapter().checkNewTerm(content);
        if (isNewTerm)
            this.raiseIssueManager.addTerm(content);
    }

    private synchronized ListViewAdapter getAdapter() {
        return this.adapter;
    }

    private void initializeInformTxtField(View view) {
        this.informTxtField = (EditText) view.findViewById(R.id.informtextfield);
        this.informTxtField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getAdapter().getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                heyButtonObserver();
            }
        });
    }

//    private void heyButtonObserver() {
//        boolean enable = this.informTxtField.getText().toString().length() > 0 || this.raiseIssueManager.hetButtonEnable();
//        this.heyButton.setEnabled(enable);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    this.window.getCameraButton().setBackground(new BitmapDrawable(getResources(), image));
                } else {
                    this.window.getCameraButton().setBackgroundDrawable(new BitmapDrawable(getResources(), image));
                }
                this.raiseIssueManager.getIssue().setImage(image);
            }
        }
    }
}
