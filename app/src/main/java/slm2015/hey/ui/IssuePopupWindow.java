package slm2015.hey.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import slm2015.hey.R;
import slm2015.hey.entity.Issue;


public class IssuePopupWindow extends PopupWindow {
    private ImageButton cameraButton;
    private TextView textView;
    private ImageButton cancelButton;
    private ImageButton raiseButton;

    public IssuePopupWindow(View view, Issue issue, int width, int height) {
        super(view, width, height);
        String text = issue.getIssue();
        initializeCameraButton(view);
        initializeTextView(view, text);
        initializeCancelButton(view);
        this.raiseButton = (ImageButton) view.findViewById(R.id.raiseButton);
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

    private void initializeTextView(View view, String text) {
        this.textView = (TextView) view.findViewById(R.id.text);
        this.textView.setText(text);
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
}
