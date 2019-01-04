package z.android.communication.datasharingandcommunication.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {

    private static Point mPoint;
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (manager!=null){
            Display display = manager.getDefaultDisplay();
            mPoint = new Point();
            display.getSize(mPoint);
            return mPoint.x;
        }else {
            return 0;
        }
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (manager!=null){
            Display display = manager.getDefaultDisplay();
            mPoint = new Point();
            display.getSize(mPoint);
            return mPoint.y;
        }else {
            return 0;
        }
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            if (manager!=null){
                manager.getDefaultDisplay().getMetrics(dm);
            }
            return dm.density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1.0f;
    }

}
