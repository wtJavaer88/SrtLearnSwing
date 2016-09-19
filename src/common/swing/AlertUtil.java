package common.swing;

import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;

public class AlertUtil
{

    public static void showLongToast(String string)
    {
        new PopupDialog("提示", string).show();
    }

    public static void showShortToast(String string, long time)
    {
        JDialog autoDisposePopupDialog = new AutoDisposePopupDialog("提示",
                string, time);
        autoDispose(2000, autoDisposePopupDialog);
        autoDisposePopupDialog.show();
    }

    public static void showShortToast(String string)
    {
        showShortToast(string, 2000);
    }

    public static void autoDispose(final long time, final JDialog dialog)
    {
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    TimeUnit.MILLISECONDS.sleep(time);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                dialog.dispose();
            }

        }).start();

    }
}
