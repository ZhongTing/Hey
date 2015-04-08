package slm2015.hey;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class FunnyPoFragment extends Fragment {


    private ListView _nounListView;
    private EditText _informTxtField;
    private ImageButton _heyButton;
    private String [] _nounArray = {"Police", "Sausage", "NTUT", "Mr.Brown", "Seven-Eleven", "Family-Mart", "Cat", "MRT", "Garbage noodle"};

    private final int HEY_BUTTON_HEIGHT = 120;
    private final int BOTTOM_MARGIN = 5;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity)activity;
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

        _nounListView = (ListView) view.findViewById(R.id.noun_listView);
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
        _heyButton = (ImageButton) view.findViewById(R.id.hey_button);
        if(_nounListView.getAdapter() == null)
            _nounListView.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(),
                    android.R.layout.simple_list_item_activated_1 , _nounArray));

//        initializeListViewWidth(_nounListView);
//        initializeTxtFieldPosition();
//        initializeHeyButton();
        return view;
    }

    private void heyButtonObserver(){
        boolean enable = _informTxtField.getText().toString().length() > 0;
        _heyButton.setClickable(enable);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

//    private void initializeListViewWidth(ListView listView) {
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.width = getScreenSize().x/2;
//        int tabHeight = ((MainActivity)getActivity()).getTabHeight();
//        params.height = getScreenSize().y - tabHeight - HEY_BUTTON_HEIGHT;
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }
//
//    private void initializeTxtFieldPosition(){
//        final RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(getScreenSize().x / 5 * 4, HEY_BUTTON_HEIGHT);
//        _informTxtField.setLayoutParams(lparams);
//        _informTxtField.requestLayout();
//    }
//
//    private void initializeHeyButton(){
//        final RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(getScreenSize().x / 5 , HEY_BUTTON_HEIGHT);
//        lparams.leftMargin = getScreenSize().x - lparams.width;
//        _heyButton.setLayoutParams(lparams);
//        _heyButton.requestLayout();
//    }

    private Point getScreenSize(){
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        return size;
    }
}
