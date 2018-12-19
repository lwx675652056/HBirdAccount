package sing.common.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import sing.common.R;

/**
 * 加载框
 */
public class LoadingDialog {

    private Dialog dialog;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context, R.style.ProgressDialogTheme);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        dialog.setContentView(R.layout.progress_dialog);
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
        }
    }
}