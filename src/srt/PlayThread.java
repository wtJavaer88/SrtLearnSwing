package srt;

import java.util.Queue;

import com.wnc.srtlearn.modules.srt.SrtVoiceHelper;
import com.wnc.srtlearn.monitor.StudyMonitor;
import com.wnc.srtlearn.monitor.work.ActiveWork;
import com.wnc.srtlearn.monitor.work.WORKTYPE;
import com.wnc.srtlearn.setting.SrtSetting;
import common.utils.Mp3Utils;

public class PlayThread extends Thread
{// 两个音频间的播放延迟
    final int VOICE_PLAY_DELAY = 200;
    public volatile boolean threadRunning = true;
    SrtPlayService srtPlayService;

    public PlayThread(SrtPlayService srtPlayService)
    {
        this.srtPlayService = srtPlayService;
    }

    @Override
    public void run()
    {
        try
        {
            long voiceDuration = palyVoice();

            long beginTime = System.currentTimeMillis();
            final int SLEEP_TIME = 100;
            ActiveWork work = StudyMonitor.peekWork(WORKTYPE.SRT);
            while (threadRunning)
            {
                if(System.currentTimeMillis() - beginTime >= voiceDuration)
                {
                    // 默认听完声音才算一个
                    StudyMonitor.addActiveWork(work);
                    // 正常结束
                    threadRunning = false;
                    normalOver();
                }
                else
                {

                    Thread.sleep(SLEEP_TIME);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("PlayThread err:" + e.getMessage());
            e.printStackTrace();
            threadRunning = false;
            // 出现异常, 自动播放停止
            srtPlayService.autoPlayNextCtrl = false;
            // 通知UI,停止播放
            exOver();
        }
    }

    /**
     * 异常结束时
     */
    private void exOver()
    {
        srtPlayService.getIBaseLearn().stopSrtPlay();
    }

    /**
     * 正常结束时
     */
    private void normalOver()
    {
        if(srtPlayService.isReplayInvalid())
        {
            srtPlayService.stopReplayModel();
        }
        if(srtPlayService.isReplayCtrl())
        {
            // 复读结束时,回到复读开始的地方继续复读
            if(srtPlayService.getCurIndex() == srtPlayService
                    .getEndReplayIndex())
            {
                DataHolder.setCurrentSrtIndex(srtPlayService
                        .getBeginReplayIndex());
                srtPlayService.getIBaseLearn().playCurrent();
            }
            else
            {
                // 复读模式下,也会自动播放下一条,但是临时性的
                srtPlayService.getIBaseLearn().playNext();
            }
        }
        else if(srtPlayService.isAutoPlayModel())
        {
            // 在自动播放模式下,播放下一条
            srtPlayService.getIBaseLearn().playNext();
        }
        else
        {
            exOver();
        }
    }

    private long palyVoice()
    {
        final SrtInfo currentSrtInfo = DataHolder.getCurrent();
        long curSrtduration = TimeHelper.getTime(currentSrtInfo.getToTime())
                - TimeHelper.getTime(currentSrtInfo.getFromTime());
        long voiceDuration = VOICE_PLAY_DELAY + curSrtduration;
        Queue<String> srtVoicesWithBg = SrtTextHelper.getSrtVoicesInRange(
                DataHolder.getFileKey(), currentSrtInfo.getFromTime()
                        .toString(), currentSrtInfo.getToTime().toString());

        try
        {
            if(!SrtSetting.isPlayVoice() || srtVoicesWithBg.isEmpty())
            {
                return voiceDuration;
            }
            else if(!SrtSetting.isPlayBgVoice() || srtVoicesWithBg.size() == 1)
            {
                SrtVoiceHelper.play(srtVoicesWithBg.peek());
                return voiceDuration;
            }

            if(srtVoicesWithBg.size() >= 2)
            {
                SrtInfo nextSrtInfo;
                String secondPath = "";
                for (String q : srtVoicesWithBg)
                {
                    secondPath = q;
                }
                try
                {
                    nextSrtInfo = DataHolder.getSrtInfoByIndex(DataHolder
                            .getCurrentSrtIndex() + 1);

                    // 如果第二段是背景声音,则播放并累加时间
                    if(!secondPath.contains(nextSrtInfo.getFromTime()
                            .toString().replace(":", "")))
                    {
                        final long l = TimeHelper.getTime(nextSrtInfo
                                .getFromTime())
                                - TimeHelper
                                        .getTime(currentSrtInfo.getToTime());
                        voiceDuration += l;
                    }
                    else
                    {
                        // 否则剔除,不要把下一段字幕的声音加进来了
                        srtVoicesWithBg.remove(1);
                    }
                }
                catch (RuntimeException ex)
                {
                    voiceDuration += 1000 * Mp3Utils.getTime(secondPath);
                }

            }

            if(SrtSetting.isPlayVoice())
            {
                SrtVoiceHelper.playInList(srtVoicesWithBg);
            }
        }
        catch (Exception e)
        {
        }

        return voiceDuration;
    }
}