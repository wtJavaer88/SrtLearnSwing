package test;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.wnc.basic.BasicFileUtil;

public class Mp3ToWmv
{
    static String season = "Friends.S01";
    static String folder = "D:\\Users\\wnc\\oral\\MP3\\" + season + "\\";
    static String target = "D:\\Users\\wnc\\oral\\WAV\\" + season + "\\";

    public static void main(String[] args)
    {
        if(!BasicFileUtil.isExistFolder(target))
        {
            BasicFileUtil.makeDirectory(target);
        }
        convertFolder(folder);
    }

    private static void convertFolder(String folder)
    {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(5);
        for (File epDir : new File(folder).listFiles())
        {
            if(epDir.isDirectory())
            {
                for (final File file : epDir.listFiles())
                {
                    executor.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            String mp3Path = file.getAbsolutePath();
                            String wavPath = mp3Path.replace(
                                    "MP3" + File.separator,
                                    "WAV" + File.separator).replace(".mp3",
                                    ".wav");
                            System.out.println(mp3Path);
                            System.out.println(wavPath);
                            String wavDir = new File(wavPath).getParent();
                            if(!BasicFileUtil.isExistFolder(wavDir))
                            {
                                BasicFileUtil.makeDirectory(wavDir);
                            }
                            convert(mp3Path, wavPath);
                        }
                    });
                }
            }
        }
        executor.shutdown();
    }

    private static void convert(String mp3, String wav)
    {
        String command = "D:\\Users\\wnc\\必备软件\\ffmpeg\\ffmpeg.exe -i  \""
                + mp3 + "\"  \"" + wav + "\"";
        com.wnc.run.RunCmd.runCommand(command);
    }
}
