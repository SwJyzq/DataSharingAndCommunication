package z.android.communication.datasharingandcommunication.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import z.android.communication.datasharingandcommunication.R;

public class DialogFactory {
    public static Dialog createRequestDialog(final Context context, String tip) {

        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_layout);
        Window window = dialog.getWindow();
        if (window!=null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getScreenWidth(context);
            lp.width = (int) (0.6 * width);
        }

        TextView titleTextView = dialog.findViewById(R.id.tvLoad);
        if (tip == null || tip.length() == 0) {
            titleTextView.setText(R.string.sending_request);
        } else {
            titleTextView.setText(tip);
        }

        return dialog;
    }

    public static void ToastDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
                .setPositiveButton("确定", null).create().show();
    }
}
