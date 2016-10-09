package db;

import com.wnc.srtlearn.setting.Config;

public class DataSource
{

    public static final String DICTIONARY = "jdbc:sqlite:" + Config.dbFolder
            + "dictionary.db";
    public static final String BUSINESS = "jdbc:sqlite:" + Config.dbFolder
            + "srtlearn.db";
    public static final String SRT = "jdbc:sqlite:" + Config.dbFolder
            + "srt.db";
}
