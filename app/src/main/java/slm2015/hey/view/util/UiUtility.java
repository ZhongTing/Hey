package slm2015.hey.view.util;

import android.content.res.Resources;
import android.util.TypedValue;

public class UiUtility {

    public static int dpiToPixel(int dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
