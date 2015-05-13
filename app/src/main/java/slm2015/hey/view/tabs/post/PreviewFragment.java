package slm2015.hey.view.tabs.post;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.component.Card;
import slm2015.hey.ui.component.Wizard;

public class PreviewFragment extends Fragment {
    private ImageButton cameraButton;
    private ImageButton cancelButton;
    private ImageButton raiseButton;
    private Card card;
    private Issue issue;
    private Wizard wizard;

    public static PreviewFragment newInstance(Issue issue, Wizard wizard) {
        PreviewFragment fragment = new PreviewFragment();
        fragment.wizard = wizard;
        fragment.issue = issue;
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                this.issue.setImage(image);
                this.card.assignIssue(issue);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.raise_issue, container, false);
        this.card = (Card) view.findViewById(R.id.preview);
        this.card.assignIssue(this.issue);
        this.initOnCreateView(view);
        return view;
    }

    private void initOnCreateView(View view) {
        initializeCameraButton(view);
        initializeCancelButton(view);
        initializeRaiseButton(view);
    }

    private void initializeRaiseButton(View view) {
        this.raiseButton = (ImageButton) view.findViewById(R.id.raiseButton);
        this.raiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                APIManager.getInstance().run(getActivity(), new RaiseIssueAPI(PreviewFragment.this.issue, new APIBase.Callback() {
//                    @Override
//                    public void requestSuccess(JSONObject result) throws JSONException {
//                        wizard.back();
//                    }
//
//                    @Override
//                    public void requestFail() {
//                        wizard.back();
//                    }
//                }));
            }
        });
    }

    private void initializeCameraButton(View view) {
        this.cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
        this.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 0);
            }
        });
    }

    private void initializeCancelButton(View view) {
        this.cancelButton = (ImageButton) view.findViewById(R.id.cancelButton);
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizard.back();
            }
        });
    }
}
