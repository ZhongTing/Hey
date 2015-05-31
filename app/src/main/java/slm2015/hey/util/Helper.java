package slm2015.hey.util;

import android.graphics.Bitmap;
import android.util.Log;

public class Helper {
    public static Bitmap limitBitmapSize(Bitmap bitmap) {
        float scale = 1;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final int maxSize = 1000;
        Bitmap resizeBitmap = bitmap;

        Log.d("LimitBitmapSize", "Old Size: " + bitmap.getWidth() + ", " + bitmap.getHeight());

        if (width > maxSize || height > maxSize) {
            if (width / maxSize > height / maxSize) {
                scale = (float) width / maxSize;
            } else
                scale = (float) height / maxSize;

            Log.d("LimitBitmapSize", "Scale " + scale);

            width /= scale;
            height /= scale;
            resizeBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        Log.d("LimitBitmapSize", "New Size: " + resizeBitmap.getWidth() + ", " + resizeBitmap.getHeight());
        return resizeBitmap;
    }
}
