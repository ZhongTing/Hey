package slm2015.hey.view.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;

import slm2015.hey.R;

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

    public static int getNavBarHeight(Context context) {
        int result = 0;
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey || !hasBackKey) {
            //The device has a navigation bar
            Resources resources = context.getResources();

            int orientation = context.getResources().getConfiguration().orientation;
            int resourceId;
            if (isTablet(context)) {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
            } else {
                resourceId = resources.getIdentifier(orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width", "dimen", "android");
            }

            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;

    }

    public static boolean isTablet(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void showLoading(Context context) {
        Activity activity = (context instanceof Activity ? (Activity) context : null);
        if (activity != null) {
            View progressBar = activity.findViewById(R.id.google_progress);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofFloat(progressBar, "alpha", 0, 1);
                animator.setDuration(1000);
                animator.start();
            }
        }
    }

    public static void stopLoading(Context context) {
        Activity activity = (context instanceof Activity ? (Activity) context : null);
        if (activity != null) {
            View progressBar = activity.findViewById(R.id.google_progress);
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
