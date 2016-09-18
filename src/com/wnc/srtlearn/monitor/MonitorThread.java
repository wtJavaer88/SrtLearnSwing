package com.wnc.srtlearn.monitor;

import com.wnc.srtlearn.monitor.work.WORKTYPE;

public class MonitorThread extends Thread
{
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(5000);
                System.out.println("当前学习的字幕数:"
                        + StudyMonitor.getWorkCount(WORKTYPE.SRT));
                System.out.println("当前学习的朗读数:"
                        + StudyMonitor.getWorkCount(WORKTYPE.TTS_REC));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
