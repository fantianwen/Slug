package van.tian.wen.multistaterefreshlayoutlib.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * ToastUtils
 *
 * @author hefeng
 */
public class ToastUtil {
    private static Toast mToast;

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void showCenter(final Context context, final CharSequence text, final int duration) {
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, text, duration);
                toast.setText(text);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    public static void show(final Context context, final CharSequence text, final int duration) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (context == null) {
            return;
        }

        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(context, text, duration);
                } else {
                    mToast.setText(text);
                    mToast.setDuration(duration);
                }
                // view.setBackgroundResource(/*你的背景*/);
                mToast.show();
            }
        });
    }


    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static void show(final Context context, final View layout) {
        if (context == null) {
            return;
        }
        if (layout == null) {
            return;
        }
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = new Toast(context);
                toast.setView(layout);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
