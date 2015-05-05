package slm2015.hey.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.ui.component.Wizard;

public class PostStepFragment extends Fragment {

    private static String step = "step_key";
    private Wizard wizard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_step_fragment, container, false);
        this.init(view);
        return view;
    }

    private void init(View view) {
        Bundle arguments = this.getArguments();
        int stepIndex = arguments.getInt(PostStepFragment.step, 0);
        ((TextView) view.findViewById(R.id.this_step_text_view)).setText(stepIndex + "");
        view.findViewById(R.id.next_step_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizard.next();
            }
        });
        view.findViewById(R.id.previous_step_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizard.back();
            }
        });
    }

    public static PostStepFragment newInstance(Wizard wizard, int position) {
        PostStepFragment fragment = new PostStepFragment();
        fragment.setWizard(wizard);
        Bundle bundle = new Bundle();
        bundle.putInt(step, position + 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
