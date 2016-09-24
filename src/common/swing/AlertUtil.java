package common.swing;

import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;

public class AlertUtil {
	static JDialog alertDialog;

	public static void showLongToast(String string) {
		if (alertDialog != null) {
			alertDialog.dispose();
		}
		alertDialog = new PopupDialog("提示", string);
		alertDialog.show();
	}

	public static void showShortToast(String string, long time) {
		if (alertDialog != null) {
			alertDialog.dispose();
		}
		alertDialog = new AutoDisposePopupDialog("提示", string, time);
		autoDispose(2000, alertDialog);
		alertDialog.show();
	}

	public static void showShortToast(String string) {
		showShortToast(string, 2000);
	}

	public static void autoDispose(final long time, final JDialog dialog) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dialog.dispose();
			}

		}).start();

	}
}
