package srt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wnc.basic.BasicStringUtil;
import com.wnc.srtlearn.ex.ErrCode;
import com.wnc.srtlearn.ex.ReachFileHeadException;
import com.wnc.srtlearn.ex.ReachFileTailException;
import com.wnc.srtlearn.ex.SrtException;
import com.wnc.srtlearn.ex.SrtNotFoundException;

public class DataHolder
{
    private static String fileKey = "";
    private static int srtIndex = -1;// 正常的浏览从0开始

    public static Map<String, List<SrtInfo>> srtInfoMap = new HashMap<String, List<SrtInfo>>();
    public static Map<String, Integer> indexMap = new HashMap<String, Integer>();

    public static Map<String, SrtTimeArr> srtTimesMap = new HashMap<String, SrtTimeArr>();

    public static boolean isExist(String file)
    {
        return srtInfoMap.containsKey(file);
    }

    public static SrtTimeArr getSrtTimeArr(String file)
    {
        return srtTimesMap.get(file);
    }

    /**
     * 获取当前字幕的所有SrtInfo
     * 
     * @return
     */
    public static List<SrtInfo> getAllSrtInfos()
    {
        if(BasicStringUtil.isNullString(fileKey))
        {
            return null;
        }
        return srtInfoMap.get(fileKey);
    }

    public static int getCurrentSrtIndex()
    {
        return srtIndex;
    }

    /**
     * 设置播放位置,注意现在只能在复读模式下起作用
     * 
     * @param replayBeginIndex
     */
    public static void setCurrentSrtIndex(int replayBeginIndex)
    {
        srtIndex = replayBeginIndex;
    }

    public static String getFileKey()
    {
        return fileKey;
    }

    public static SrtInfo getNext() throws SrtException
    {
        srtIndex++;
        // System.out.println("next:srtIndex.." + srtIndex);
        return getSrtByIndex();
    }

    public static SrtInfo getPre() throws SrtException
    {
        srtIndex--;
        return getSrtByIndex();
    }

    public static SrtInfo getFirst() throws SrtException
    {
        srtIndex = 0;
        return getSrtByIndex();
    }

    public static SrtInfo getCurrent() throws SrtException
    {
        return getSrtByIndex();
    }

    public static SrtInfo getLast() throws SrtException
    {
        checkExist();
        List<SrtInfo> list = srtInfoMap.get(fileKey);
        srtIndex = list.size() - 1;
        return getSrtByIndex();
    }

    public static SrtInfo getSrtInfoByIndex(int selIndex) throws SrtException
    {
        checkExist();
        List<SrtInfo> list = srtInfoMap.get(fileKey);
        if(selIndex == -1)
        {
            throw new ReachFileHeadException(ErrCode.SRT_REACH_HEAD);
        }
        if(selIndex >= list.size())
        {
            throw new ReachFileTailException(ErrCode.SRT_REACH_TAIL);
        }
        return list.get(selIndex);
    }

    private static SrtInfo getSrtByIndex() throws SrtException
    {
        checkExist();
        List<SrtInfo> list = srtInfoMap.get(fileKey);
        if(srtIndex == -1)
        {
            srtIndex = 0;
            indexMap.put(fileKey, srtIndex);
            throw new ReachFileHeadException(ErrCode.SRT_REACH_HEAD);
        }
        if(srtIndex >= list.size())
        {
            srtIndex = list.size() - 1;
            indexMap.put(fileKey, srtIndex);
            throw new ReachFileTailException(ErrCode.SRT_REACH_TAIL);
        }
        indexMap.put(fileKey, srtIndex);
        return list.get(srtIndex);
    }

    public static void switchFile(String file)
    {
        fileKey = file;
        srtIndex = indexMap.get(fileKey) == null ? -1 : indexMap.get(fileKey);
    }

    private static void checkExist() throws SrtNotFoundException
    {
        if(!srtInfoMap.containsKey(fileKey))
        {
            throw new SrtNotFoundException(ErrCode.SRT_NOT_FOUND);
        }
    }

    public static SrtInfo getClosestSrt(int hour, int minute, int second)
            throws SrtNotFoundException
    {
        checkExist();
        long l = TimeHelper.getTime(hour, minute, second, 0);
        List<SrtInfo> list = srtInfoMap.get(fileKey);
        SrtInfo srtInfo = null;
        for (int i = 0; i < list.size(); i++)
        {
            SrtInfo info = list.get(i);
            if(formatSeconds(info.getFromTime()) >= l
                    || formatSeconds(info.getToTime()) >= l)
            {
                srtInfo = info;
                srtIndex = i;
                break;
            }
        }
        // 返回最后一个
        if(srtInfo == null)
        {
            srtInfo = list.get(list.size() - 1);
            srtIndex = list.size() - 1;
        }
        return srtInfo;
    }

    private static long formatSeconds(TimeInfo timeInfo)
    {
        return TimeHelper.getTime(timeInfo.getHour(), timeInfo.getMinute(),
                timeInfo.getSecond(), timeInfo.getMillSecond());
    }

    public static void appendData(String srtFile, List<SrtInfo> srtInfos)
    {
        if(!srtInfoMap.containsKey(srtFile))
        {
            srtInfoMap.put(srtFile, srtInfos);
        }
        else if(!srtInfos.isEmpty())
        {
            srtInfoMap.get(srtFile).addAll(srtInfos);
        }
    }

    /**
     * 使用这个参数,而不是fileKey,因为fileKey只是代表最新的那个
     * 
     * @param srtFile
     */
    public static void resortList(String srtFile)
    {
        List<SrtInfo> list = srtInfoMap.get(srtFile);
        Collections.sort(list, new java.util.Comparator<SrtInfo>()
        {
            @Override
            public int compare(SrtInfo lhs, SrtInfo rhs)
            {
                return lhs.getFromTime().toString()
                        .compareTo(rhs.getFromTime().toString());
            }
        });
        // System.out.println("list:srtIndex.." + srtIndex);
    }

    public static void setFileKey(String srtFile)
    {
        fileKey = srtFile;
    }
}
