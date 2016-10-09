package srt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import srt.ex.SrtException;
import srt.ex.SrtNotFoundException;

import com.wnc.basic.BasicDateUtil;
import com.wnc.basic.BasicFileUtil;
import com.wnc.srtlearn.dao.FavDao;
import com.wnc.srtlearn.modules.srt.SrtVoiceHelper;
import com.wnc.srtlearn.pojo.FavoriteMultiSrt;
import com.wnc.srtlearn.pojo.FavoriteSingleSrt;
import com.wnc.srtlearn.setting.SrtSetting;
import common.swing.AlertUtil;
import common.uihelper.MyAppParams;

public class SrtPlayService
{
    private PlayThread playThread;
    private boolean replayCtrl = false;// 复读模式
    boolean autoPlayNextCtrl = true;// 如果播放过程出异常,就不能单靠系统设置的值控制自动播放下一个了,
    public IBaseLearn sBaseLearn;
    private int beginReplayIndex = -1;
    private int endReplayIndex = -1;

    public SrtPlayService(IBaseLearn sBaseLearn)
    {
        this.sBaseLearn = sBaseLearn;
    }

    public boolean favorite() throws SrtException
    {
        List<SrtInfo> currentPlaySrtInfos = getCurrentPlaySrtInfos();
        FavoriteMultiSrt mfav = new FavoriteMultiSrt();
        mfav.setFavTime(BasicDateUtil.getCurrentDateTimeString());
        mfav.setFromTimeStr(currentPlaySrtInfos.get(0).getFromTime().toString());
        mfav.setToTimeStr(currentPlaySrtInfos
                .get(currentPlaySrtInfos.size() - 1).getFromTime().toString());
        mfav.setSrtFile(getCurFile().replace(MyAppParams.SRT_FOLDER, "")
                .replace(File.separator, "/"));
        mfav.setHasChild(currentPlaySrtInfos.size());
        mfav.setTag(getTag().replace("tag<", "").replace(">", ""));

        if(FavDao.isExistMulti(mfav))
        {
            AlertUtil.showLongToast("已经收藏过了!");
            return true;
        }

        if(writeFavoritetxt(currentPlaySrtInfos)
                && saveFavDb(mfav, currentPlaySrtInfos))
        {
            AlertUtil.showLongToast("收藏成功!");
            return true;
        }
        else
        {
            AlertUtil.showLongToast("收藏失败!");
            return false;
        }
    }

    private boolean saveFavDb(FavoriteMultiSrt mfav,
            List<SrtInfo> currentPlaySrtInfos)
    {
        List<FavoriteSingleSrt> sfavs = new ArrayList<FavoriteSingleSrt>();
        for (SrtInfo srtInfo : currentPlaySrtInfos)
        {
            FavoriteSingleSrt sfav = new FavoriteSingleSrt();
            sfav.setFromTimeStr(srtInfo.getFromTime().toString());
            sfav.setToTimeStr(srtInfo.getToTime().toString());
            sfav.setsIndex(srtInfo.getSrtIndex());
            sfav.setEng(srtInfo.getEng());
            sfav.setChs(srtInfo.getChs());
            sfavs.add(sfav);
        }
        return FavDao.insertFavMulti(mfav, sfavs);
    }

    private boolean writeFavoritetxt(List<SrtInfo> currentPlaySrtInfos)
    {
        String favoriteCurrContent = getFavoriteCurrContent(currentPlaySrtInfos);
        return BasicFileUtil.writeFileString(MyAppParams.FAVORITE_TXT,
                favoriteCurrContent, "UTF-8", true);
    }

    public SrtInfo getSrtInfo(SRT_VIEW_TYPE view_type) throws SrtException
    {
        SrtInfo srt = null;
        switch (view_type)
        {
        case VIEW_FIRST:
            srt = DataHolder.getFirst();
            break;
        case VIEW_LAST:
            srt = DataHolder.getLast();
            break;
        case VIEW_LEFT:
            srt = DataHolder.getPre();
            break;
        case VIEW_RIGHT:
            srt = DataHolder.getNext();
            break;
        case VIEW_CURRENT:
            srt = DataHolder.getCurrent();
            break;
        }
        return srt;
    }

    public String getPleyProgress()
    {
        final List<SrtInfo> list = DataHolder.srtInfoMap.get(getCurFile());
        if(list == null)
        {
            return "";
        }
        return "(" + (getCurIndex() + 1) + "/" + list.size() + ")";
    }

    public int getCurIndex()
    {
        return DataHolder.getCurrentSrtIndex();
    }

    public String getCurFile()
    {
        return DataHolder.getFileKey();
    }

    public String getFavoriteCurrContent(List<SrtInfo> currentPlaySrtInfos)
    {
        String tag = getTag();

        return BasicDateUtil.getCurrentDateTimeString() + " \""
                + getCurFile().replace(MyAppParams.SRT_FOLDER, "") + "\" "
                + tag + " " + currentPlaySrtInfos + "\r\n";
    }

    private String getTag()
    {
        String tag = "tag<";
        if(isReplayRunning())
        {
            tag += "replay";
        }
        else
        {
            tag += "normal";
        }

        tag += ">";
        return tag;
    }

    public void showNewSrtFile(String srtFile) throws SrtException
    {
        this.setReplayCtrl(false);
        this.setReplayIndex(-1, -1);
        System.out.println("srtFile:" + srtFile);
        if(BasicFileUtil.isExistFile(srtFile))
        {
            sBaseLearn.stopSrtPlay();
            DataHolder.switchFile(srtFile);
            if(!DataHolder.srtInfoMap.containsKey(srtFile))
            {
                new DataParseThread(getCurFile()).start();
                while (DataHolder.getAllSrtInfos() == null
                        || DataHolder.getAllSrtInfos().size() == 0)
                {
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(50);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                sBaseLearn.play(getSrtInfo(SRT_VIEW_TYPE.VIEW_CURRENT));
            }
            else
            {
                sBaseLearn.play(getSrtInfo(SRT_VIEW_TYPE.VIEW_CURRENT));
            }
        }
        else
        {
            Log.e("srt", "not found " + srtFile);
            throw new SrtNotFoundException();
        }
    }

    /**
     * 控制切换是否复读,快捷设置仅复读本句
     */
    public void switchReplayModel()
    {
        this.setReplayCtrl(isReplayCtrl() ? false : true);
        if(isReplayCtrl())
        {
            setReplayIndex(getCurIndex(), getCurIndex());
        }
        AlertUtil.showShortToast(isReplayCtrl() ? "复读" : "不复读");
    }

    public void stopReplayModel()
    {
        this.setReplayCtrl(false);
        setReplayIndex(-1, -1);
    }

    public void setReplayIndex(int bIndex, int eIndex)
    {
        if(bIndex > eIndex)
        {
            AlertUtil.showLongToast("结束时间不能小于开始时间!");
        }
        else
        {
            setBeginReplayIndex(bIndex);
            setEndReplayIndex(eIndex);
        }
    }

    /**
     * 检查复读模式是否失效:在复读的时候,如果翻页的范围超出了复读范围
     * <p>
     * 注意不能通过取反来表示复读有效
     * 
     * @return
     */
    public boolean isReplayInvalid()
    {
        return isReplayCtrl()
                && (getCurIndex() < getBeginReplayIndex() || getCurIndex() > getEndReplayIndex());
    };

    /**
     * 复读模式是否在进行中
     * 
     * @return
     */
    public boolean isReplayRunning()
    {
        return isReplayCtrl()
                && (getCurIndex() >= getBeginReplayIndex() && getCurIndex() <= getEndReplayIndex());
    };

    public void playSrt()
    {
        // 停止原有的播放线程,播放新字幕
        stopSrt();

        System.gc();

        // 每次播放,先设置自动播放控制为true
        autoPlayNextCtrl = true;
        playThread = new PlayThread(this);
        playThread.start();
    }

    public void stopSrt()
    {
        SrtVoiceHelper.stop();
        if(playThread != null)
        {
            playThread.threadRunning = false;
            playThread = null;
        }
        autoPlayNextCtrl = false;
    }

    public boolean isAutoPlayModel()
    {
        return autoPlayNextCtrl && SrtSetting.isAutoPlayNext();
    }

    /**
     * 是否已经选择了字幕文件
     * 
     * @return
     */
    public boolean isSrtShowing()
    {
        if(BasicFileUtil.isExistFile(getCurFile()))
        {
            return true;
        }
        AlertUtil.showShortToast("请先选择一部剧集");
        return false;
    }

    public boolean isRunning()
    {
        return playThread != null;
    }

    public boolean isReplayCtrl()
    {
        return replayCtrl;
    }

    public void setReplayCtrl(boolean replayCtrl)
    {
        this.replayCtrl = replayCtrl;
    }

    public int getEndReplayIndex()
    {
        return endReplayIndex;
    }

    public void setEndReplayIndex(int endReplayIndex)
    {
        this.endReplayIndex = endReplayIndex;
    }

    public int getBeginReplayIndex()
    {
        return beginReplayIndex;
    }

    public void setBeginReplayIndex(int beginReplayIndex)
    {
        this.beginReplayIndex = beginReplayIndex;
    }

    public List<SrtInfo> getCurrentPlaySrtInfos() throws SrtException
    {
        if(isReplayRunning())
        {
            return getSrtInfos(beginReplayIndex, endReplayIndex);
        }
        else
        {
            return Arrays.asList(DataHolder.getCurrent());
        }
    }

    public List<SrtInfo> getReplaySrtInfos()
    {
        return getSrtInfos(beginReplayIndex, endReplayIndex);
    }

    /**
     * 获取一段区间内的字幕信息
     * 
     * @param bIndex
     * @param eIndex
     * @return
     */
    public List<SrtInfo> getSrtInfos(int bIndex, int eIndex)
    {
        List<SrtInfo> list = new ArrayList<SrtInfo>();
        List<SrtInfo> currentSrtInfos = DataHolder.getAllSrtInfos();
        if(currentSrtInfos != null)
        {
            for (int i = bIndex; i <= eIndex; i++)
            {
                list.add(currentSrtInfos.get(i));
            }
        }
        return list;
    }

    public IBaseLearn getIBaseLearn()
    {
        return sBaseLearn;
    }
}
