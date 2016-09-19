package common.swing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.wnc.string.PatternUtil;

public class SnapUtil
{
    static Map<String, String> movieMap = new HashMap<String, String>();
    static
    {
        for (File f : new File("D:\\Users\\wnc\\oral\\字幕\\Friends.S01")
                .listFiles())
        {
            String lastPattern = PatternUtil.getLastPattern(
                    f.getAbsolutePath(), "S\\d+E\\d+");
            String movie = "E:\\BaiduYunDownload\\老友记\\老友记.第一季.1994.中英字幕￡CMCT小鱼\\Friends."
                    + lastPattern + ".1994.BluRay.720p.x264.AC3-CMCT.mkv";
            movieMap.put(f.getAbsolutePath(), movie);
        }
    }
    static final String ffmpeg = "\"D:\\Users\\wnc\\必备软件\\ffmpeg-20160731-04da20e-win64-static\\bin\\ffmpeg.exe\"";
    static final String targetFormat = "D:\\temp\\srtlearn\\snap%d.jpg";
    static int width = 640;
    static int height = 480;

    public static String getSnapPic(int startTime, String srtFile)
    {
        String targetPath = String.format(targetFormat,
                System.currentTimeMillis());
        String movie = movieMap.get(srtFile);
        System.out.println(movie);
        String command = ffmpeg + " -ss " + startTime
                + " -loglevel panic -i \"" + movie + "\"  -t 0.0001 -s "
                + width + "x" + height + " " + targetPath;
        com.wnc.run.RunCmd.runCommand(command);
        return targetPath;
    }
}
