package slm2015.hey.ui.tabsswipe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton heyButton;
    private Button nounButton, adjButton, locationButton;
    private ArrayList<Button> buttonMap;
    private RaiseIssueManager raiseIssueManager;
    private ListViewAdapter adapter;
    private View popupView;
    private Activity activity;

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
        initializeHeyButton(view);
        initializeNounButton(view);
        initializeAdjButton(view);
        initializeLocationButton(view);
        refreshState();
        return view;
    }

    private void initializeHeyButton(View view) {
        this.heyButton = (ImageButton) view.findViewById(R.id.hey_button);
        this.heyButton.setEnabled(false);
        this.heyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int listNum = raiseIssueManager.getIssuePosNum();
                String text = informTxtField.getText().toString();
                if (listNum == 2)
                    raiseIssueManager.setIsPreview(true);
                if (!text.isEmpty()) {
                    setText(text, listNum);
                }
                refreshListView();
//                final PopupWindow popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            }
        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.informTxtField.getWindowToken(), 0);
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

    private void refreshState() {
        this.adjButton.setEnabled(raiseIssueManager.adjButtonEnable());
        this.locationButton.setEnabled(raiseIssueManager.locationButtonEnable());
        heyButtonObserver();
        if (this.raiseIssueManager.isPreview()) {
            closeKeyBoard();
            this.raiseIssueManager.setIsPreview(false);
            int length = RelativeLayout.LayoutParams.WRAP_CONTENT;
            String text = this.raiseIssueManager.getIssueInString();
            new IssuePopupWindow(this.popupView, text, length, length, this.activity);
        }
    }

    private void initializeListView(View view) {
        this.listView = (ListView) view.findViewById(R.id.noun_listView);
        if (this.listView.getAdapter() == null) {
            final int NOUNLIST = 0;
            this.adapter = new ListViewAdapter(getActivity().getApplicationContext(), raiseIssueManager.getList(NOUNLIST));
            this.listView.setAdapter(this.adapter);
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int listNum = raiseIssueManager.getIssuePosNum();
                    ListView listView = (ListView) parent;
                    if (position < raiseIssueManager.getList(listNum).size()) {
                        setItemSelected(listNum, position);
                        Term term = (Term) listView.getItemAtPosition(position);
                        setText(term.getTerm(), listNum);
                        refreshListView();
                    }
                }
            });
        }
    }

    private void setText(String text, int listNum) {
        setIssue(text);
        this.informTxtField.getText().clear();
        this.buttonMap.get(listNum).setText(text);
    }

    private void setItemSelected(int listNum, int position) {
        ArrayList<Term> termList = this.raiseIssueManager.getList(listNum);
        for (Term term : termList)
            term.setIsSelected(false);
        termList.get(position).setIsSelected(true);
    }

    private void setIssue(String text) {
        int issuePosNum = this.raiseIssueManager.getIssuePosNum();
        this.raiseIssueManager.setIssue(text);
        this.raiseIssueManager.setIssuePosNum(issuePosNum + 1);
    }

    private void refreshListView() {
        int listNum = this.raiseIssueManager.getIssuePosNum();
        ArrayList<Term> showList = this.raiseIssueManager.getList(listNum);
        this.adapter.SetData(showList);
        listView.setAdapter(this.adapter);
        refreshState();
    }

    private void initializeInformTxtField(View view) {
        this.informTxtField = (EditText) view.findViewById(R.id.informtextfield);
        this.informTxtField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                heyButtonObserver();
            }
        });
    }

    private void heyButtonObserver() {
        boolean enable = this.informTxtField.getText().toString().length() > 0 || this.raiseIssueManager.hetButtonEnable();
        this.heyButton.setEnabled(enable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
