package slm2015.hey.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import slm2015.hey.R;


public class IssuePopupWindow extends PopupWindow {
    private ImageButton cameraButton;
    private TextView textView;
    private ImageButton cancelButton;
    private ImageButton raiseButton;
    private Activity activity;

    public IssuePopupWindow(View view, String text, int width, int height, Activity activity) {
        super(view, width, height);
        initializeCameraButton(view, activity);
        initializeTextView(view, text);
        initializeCancelButton(view);
        this.raiseButton = (ImageButton) view.findViewById(R.id.raiseButton);
        initializeWindow(view, width, height);
        this.activity = activity;
    }

    private void initializeCameraButton(View view, final Activity activity) {
        this.cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
//        this.cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                activity.startActivityForResult(intent, 0);
//            }
//        });
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super(requestCode, resultCode,data);
//        if (requestCode == 0) {
//            if (resultCode == Activity.RESULT_OK) {
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                this.cameraButton.setBackground(new BitmapDrawable(this.activity.getResources(), image));
//            }
//        }
//    }
}
