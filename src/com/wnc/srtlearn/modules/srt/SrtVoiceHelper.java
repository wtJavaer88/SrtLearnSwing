package com.wnc.srtlearn.modules.srt;

import java.io.File;
import java.io.FileInputStream;
import java.util.Queue;

import com.wnc.basic.BasicFileUtil;
import common.swing.AudioPlayWave;

public class SrtVoiceHelper
{
    static boolean isPlaying = false;

    public synchronized static void stop()
    {
        try
        {
        }
        catch (Exception e)
        {
            isPlaying = false;
            System.out.println("voiceStopEx." + e.getMessage());
        }
    }

    public synchronized static void play(String voicePath)
    {
        try
        {
            stop();
            AudioPlayWave audioPlayWave = new AudioPlayWave(voicePath);
            audioPlayWave.start(); // 启动线程
        }
        catch (Exception e)
        {
            isPlaying = false;
            System.out.println("voicePlayEx." + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playInList(final Queue<String> queue)
    {
        if(queue == null || queue.size() == 0)
        {
            return;
        }
        try
        {
            stop();
            String voicePath = queue.poll();
            if(!BasicFileUtil.isExistFile(voicePath))
            {
                return;
            }
            File file = new File(voicePath);
            FileInputStream fis = new FileInputStream(file);
            isPlaying = true;
        }
        catch (Exception e)
        {
            isPlaying = false;
            System.out.println("voicePlayEx." + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
