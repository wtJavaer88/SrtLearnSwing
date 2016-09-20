package com.wnc.srtlearn.setting;

/**
 * 关于字幕的全局设置,存入手机本地数据库
 * 
 * @author cpr216
 * 
 */
public class SrtSetting
{

    // 是否自动播放下一条
    private final static String PLAYVOICE = "S001";
    // 是否自动播放下一条
    private final static String AUTOPLAYNEXT = "S002";
    // 是否启动音量键翻页的监控
    private final static String VOLKEYLISTEN = "S003";
    // 是否播放背景声音
    private final static String PLAYBGVOICE = "S004";
    // 是否自动播放下一集
    private final static String AUTONEXTEP = "S005";

    public static boolean isPlayVoice()
    {
        return true;
    }

    public static boolean isPlayBgVoice()
    {
        return false;
    }

    public static boolean isAutoPlayNext()
    {
        return true;
    }

    /**
     * 是否自动下一集
     * 
     * @return
     */
    public static boolean isAutoNextEP()
    {
        return true;
    }

}
