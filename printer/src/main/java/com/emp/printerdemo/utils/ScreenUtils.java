package com.emp.printerdemo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;

import static android.view.Surface.ROTATION_0;

public class ScreenUtils {

    private static DisplayUtil displayUtil = new DisplayUtil();

    public ScreenUtils() {
    }

    public static Bitmap screenshot() throws Exception {
        String surfaceClassName;
        Point size = displayUtil.getCurrentDisplaySize();
        if (Build.VERSION.SDK_INT <= 17) {
            surfaceClassName = "android.view.Surface";
        } else {
            surfaceClassName = "android.view.SurfaceControl";
        }
        Bitmap b = (Bitmap) Class.forName(surfaceClassName).getDeclaredMethod("screenshot", new Class[]{Rect.class, int.class, int.class, int.class}).invoke(null, new Object[]{new Rect(0, 0, Integer.valueOf(size.x), Integer.valueOf(size.y)), Integer.valueOf(size.x), Integer.valueOf(size.y), ROTATION_0});
        int rotation = displayUtil.getScreenRotation();
        if (rotation == 0) {
            return b;
        }
        Matrix m = new Matrix();
        if (rotation == 1) {
            m.postRotate(-90.0f);
        } else if (rotation == 2) {
            m.postRotate(-180.0f);
        } else if (rotation == 3) {
            m.postRotate(-270.0f);
        }
        return Bitmap.createBitmap(b, 0, 0, size.x, size.y, m, false);
    }
}
