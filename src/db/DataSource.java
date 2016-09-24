package db;

public class DataSource
{
	
//	static final String dbFolder = "D:\\Users\\cpr216\\Workspaces"
//			+ "\\MyEclipse 10\\SrtLearnSwing\\db\\";
	static final String dbFolder = "D:\\用户目录\\workspace-sts\\SrtLearnSwing\\db\\";
	
    public static final String DICTIONARY = "jdbc:sqlite:"+dbFolder+"dictionary.db";
    public static final String BUSINESS = "jdbc:sqlite:"+dbFolder+"srtlearn.db";
    public static final String SRT = "jdbc:sqlite:"+dbFolder+"srt.db";
}
