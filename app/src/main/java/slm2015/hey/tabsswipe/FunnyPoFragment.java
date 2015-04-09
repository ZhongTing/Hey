package slm2015.hey.tabsswipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import slm2015.hey.R;
import slm2015.hey.manager.RaiseIssueManager;

public class FunnyPoFragment extends Fragment {


    private ListView _listView;
    private EditText _informTxtField;
    private ImageButton _heyButton;
    private ImageButton _nounButton, _adjButton, _locationButton;
    private ArrayList<ImageButton> _buttonMap;
    private TextView _hintTextView;
    private RaiseIssueManager _raiseIssueManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _raiseIssueManager = new RaiseIssueManager();
        _buttonMap = new ArrayList<ImageButton>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.funnypo_layout, container, false);

        _hintTextView = (TextView) view.findViewById(R.id.hintTextView);
        initializeInformTxtField(view);
        initializeListView(view);
        _heyButton = (ImageButton) view.findViewById(R.id.hey_button);
        initializeNounButton(view);
        initializeAdjButton(view);
        initializeLocationButton(view);
        refreshBtnState();
        return view;
    }

    private void initializeNounButton(View view){
        _nounButton = (ImageButton) view.findViewById(R.id.noun_button);
        _buttonMap.add(_nounButton);
        _nounButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _raiseIssueManager.setIssuePosNum(0);
                refreshListView();
            }
        });
    }

    private void initializeAdjButton(View view){
        _adjButton = (ImageButton) view.findViewById(R.id.adj_button);
        _buttonMap.add(_adjButton);
        _adjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _raiseIssueManager.setIssuePosNum(1);
                refreshListView();
            }
        });
    }

    private void initializeLocationButton(View view){
        _locationButton = (ImageButton) view.findViewById(R.id.location_button);
        _buttonMap.add(_locationButton);
        _locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _raiseIssueManager.setIssuePosNum(2);
                refreshListView();
            }
        });
    }

    private void refreshBtnState(){
        _adjButton.setEnabled(_raiseIssueManager.adjButtonEnable());
        _locationButton.setEnabled(_raiseIssueManager.locationButtonEnable());
    }

    private void initializeListView(View view){
        _listView = (ListView) view.findViewById(R.id.noun_listView);
        if(_listView.getAdapter() == null) {
            final int NOUNLIST = 0;
            _listView.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(),
                    android.R.layout.simple_list_item_activated_1, _raiseIssueManager.getList(NOUNLIST)));
            _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView) parent;
                    String text = listView.getItemAtPosition(position).toString();
                    setIssue(text);
                    CharSequence issue = _raiseIssueManager.getIssueInString();
                    _informTxtField.setText(issue);
                    _hintTextView.setText(issue);
                    refreshListView();
                }
            });
        }
    }

    private void setIssue(String text){
        int issuePosNum = _raiseIssueManager.getIssuePosNum();
        _raiseIssueManager.setIssue(text);
        _raiseIssueManager.setIssuePosNum(issuePosNum + 1);
    }

    private void refreshListView(){
        int listNum = _raiseIssueManager.getIssuePosNum();
        ArrayList showList = _raiseIssueManager.getList(listNum);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_activated_1, showList);
        _listView.setAdapter(adapter);
        refreshBtnState();
    }

    private void initializeInformTxtField(View view){
        _informTxtField = (EditText) view.findViewById(R.id.informtextfield);
        _informTxtField.addTextChangedListener(new TextWatcher() {
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

    private void heyButtonObserver(){
        boolean enable = _informTxtField.getText().toString().length() > 0;
        _heyButton.setClickable(enable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
