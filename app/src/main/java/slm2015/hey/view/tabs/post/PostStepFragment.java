package slm2015.hey.view.tabs.post;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import junit.framework.Assert;

import slm2015.hey.R;
import slm2015.hey.core.Observer;
import slm2015.hey.core.term.TermLoader;
import slm2015.hey.core.term.TermType;
import slm2015.hey.ui.component.Wizard;

public class PostStepFragment extends Fragment implements Observer {
    private Wizard wizard;
    private ListView listView;
    private TermAdapter adapter;
    private TermLoader loader;
    private TermType termType;
    private EditText searchTextView;
    private OnStepFinishListener onStepFinishListener;

    public static PostStepFragment newInstance(Wizard wizard, TermLoader loader, TermType type) {
        PostStepFragment fragment = new PostStepFragment();
        fragment.setWizard(wizard);
        fragment.setTermLoader(loader, type);
        fragment.init();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_step_fragment, container, false);
        this.initOnCreateView(view);
        return view;
    }

    //init once when create object
    private void init() {
        this.adapter = new TermAdapter(this.loader.getTerms(this.termType));
        this.adapter.setOnTermSelectedListener(new TermAdapter.OnTermSelectedListener() {
            @Override
            public void OnTermSelected(String selectedTerm) {
                //todo
                if (onStepFinishListener !=null) {
                    onStepFinishListener.OnStepFinish(selectedTerm);
                }
                wizard.next();
            }
        });
        this.loader.addObserver(this);
    }

    //init with onCreateView calls
    private void initOnCreateView(View view) {
        Assert.assertNotNull(this.adapter);
        this.listView = (ListView) view.findViewById(R.id.term_list_view);
        this.listView.setAdapter(this.adapter);
        this.searchTextView = (EditText) view.findViewById(R.id.search_text_view);
        switch (this.termType){
            case SUBJECT:
                this.searchTextView.setHint("Who?");
                break;
            case DESCRIPTION:
                this.searchTextView.setHint("What?");
                break;
            case PLACE:
                this.searchTextView.setHint("Where?");
                break;
        }

        //todo implement text changed listener to fire adapter filter event
        //this.adapter.filter("test");

    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public void setTermLoader(TermLoader loader, TermType type) {
        this.loader = loader;
        this.termType = type;
    }

    public void setOnStepFinishListener(OnStepFinishListener listener) {
        this.onStepFinishListener = listener;
    }

    @Override
    public void onSubjectChanged() {
        if (this.adapter != null) {
            this.adapter.setTermList(this.loader.getTerms(this.termType));
        }
    }

    public interface OnStepFinishListener {
        public void OnStepFinish(String selectedTerm);
    }
}
