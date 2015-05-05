package slm2015.hey.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import slm2015.hey.R;
import slm2015.hey.ui.component.Wizard;
import slm2015.hey.ui.component.WizardAdaptor;

public class FunnyPostFragment extends MainPagerFragment {
    private Wizard wizard;

    @Override
    protected int getPageIconRedId() {
        return R.drawable.funny_po;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.funnypo_layout, container, false);
        this.init(view);
        return view;
    }

    private void init(View rootView) {
        this.findViews(rootView);
        this.initWizard();
    }

    private void findViews(View rootView) {
        this.wizard = ((Wizard) rootView.findViewById(R.id.post_wizard));
    }

    private void initWizard() {
        this.wizard.setAdaptor(new WizardAdaptor(getFragmentManager()) {
            private String[] indicateTexts = {"主角", "描述", "地點", "預覽"};

            @Override
            public String getStepIndicateText(int stepIndex) {
                return indicateTexts[stepIndex];
            }

            @Override
            public Fragment getItem(int position) {
                return PostStepFragment.newInstance(wizard, position);
            }

            @Override
            public int getCount() {
                return indicateTexts.length;
            }
        });
    }
}