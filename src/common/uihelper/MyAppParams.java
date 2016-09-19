package common.uihelper;

import com.wnc.basic.BasicFileUtil;

public class MyAppParams
{
    private String packageName;
    private String appPath;
    private final static String root = "D:\\Users\\wnc\\oral\\";
    private final static String workPath = "D:\\Users\\cpr216\\Workspaces\\MyEclipse 10\\SrtLearnSwing\\";
    private String localLogPath;
    public final static String SWF_FOLDER = workPath + "swf\\";
    public static final String SRT_FOLDER = root + "字幕\\";
    public final static String THUMB_PICFOLDER = root + "图片\\";
    public final static String FAVORITE_TXT = workPath + "favorite.txt";

    private String backupDbPath;

    private String zipPath;

    private static MyAppParams singletonMyAppParams = new MyAppParams();

    private MyAppParams()
    {
        this.localLogPath = this.workPath + "log\\";
        this.backupDbPath = this.workPath + "backupdb\\";

        this.zipPath = this.workPath + "zip\\";

        BasicFileUtil.makeDirectory(this.localLogPath);
        BasicFileUtil.makeDirectory(this.backupDbPath);
        BasicFileUtil.makeDirectory(SWF_FOLDER);
        BasicFileUtil.makeDirectory(SRT_FOLDER);
        BasicFileUtil.makeDirectory(THUMB_PICFOLDER);
        BasicFileUtil.makeDirectory(this.zipPath);
    }

    public static MyAppParams getInstance()
    {
        return singletonMyAppParams;
    }

    public String getZipPath()
    {
        return this.zipPath;
    }

    public String getBackupDbPath()
    {
        return this.backupDbPath;
    }

    public String getWorkPath()
    {
        return workPath;
    }

    public String getLocalLogPath()
    {
        return this.localLogPath;
    }

    public String getAppPath()
    {
        return this.appPath;
    }

}
