package slm2015.hey.view.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UiUtility {

    public static int dpiToPixel(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static void closeKeyBoard(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
