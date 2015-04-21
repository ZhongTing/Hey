package slm2015.hey.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;
import slm2015.hey.ui.component.Card;


public class IssuePopupWindow extends PopupWindow {
    private ImageButton cameraButton;
    private ImageButton cancelButton;
    private ImageButton raiseButton;
    private Card card;

    public IssuePopupWindow(View view, Issue issue, int width, int height) {
        super(view, width, height);
        initializeCameraButton(view);
        initializeCancelButton(view);
        this.raiseButton = (ImageButton) view.findViewById(R.id.raiseButton);
        this.card = (Card) view.findViewById(R.id.preview);
        this.card.assignIssue(issue);
        initializeWindow(view, width, height);
    }

    private void initializeCameraButton(View view) {
        this.cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
    }

    public ImageButton getCameraButton() {
        return this.cameraButton;
    }

    private void initializeWindow(View view, int width, int height) {
        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(Color.argb(1, 0, 0, 0)));
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(false);
        showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void initializeCancelButton(View view) {
        this.cancelButton = (ImageButton) view.findViewById(R.id.cancelButton);
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void reasignIssue(Issue issue) {
        this.card.assignIssue(issue);
    }
}
