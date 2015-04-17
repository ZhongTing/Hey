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
    private TextView s_textView, v_textView, p_textView;
    private ImageButton cancelButton;
    private ImageButton raiseButton;

    public IssuePopupWindow(View view, Issue issue, int width, int height) {
        super(view, width, height);
        initializeCameraButton(view);
        initializeTextView(view, issue);
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

    private void initializeTextView(View view, Issue issue) {
        this.s_textView = (TextView) view.findViewById(R.id.s_text);
        this.s_textView.setText(issue.getSubject());
        this.v_textView = (TextView) view.findViewById(R.id.v_text);
        this.v_textView.setText(issue.getDescription());
        this.p_textView = (TextView) view.findViewById(R.id.p_text);
        this.p_textView.setText(issue.getLocation());
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
