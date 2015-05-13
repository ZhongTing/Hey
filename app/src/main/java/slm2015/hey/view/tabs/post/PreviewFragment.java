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
import android.widget.Toast;

import slm2015.hey.R;
import slm2015.hey.core.issue.IssueHandler;
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
    private OnPreviewFinishListener previewFinishListener;

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
                IssueHandler issueHandler = new IssueHandler(PreviewFragment.this.getActivity());
                issueHandler.raise(PreviewFragment.this.issue, new IssueHandler.RaiseIssueHandlerCallback() {
                    @Override
                    public void onRaisedIssue() {
                        PreviewFragment.this.previewFinishListener.OnPreviewFinish();
                        Toast.makeText(PreviewFragment.this.getActivity(), "發送成功！", Toast.LENGTH_SHORT).show();
                    }
                });
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

    public void setOnPreviewFinishListener(OnPreviewFinishListener listener) {
        this.previewFinishListener = listener;
    }

    public interface OnPreviewFinishListener {
        public void OnPreviewFinish();
    }

    public void reassignCard(Issue issue) {
        this.issue = issue;
        this.card.assignIssue(issue);
    }
}
