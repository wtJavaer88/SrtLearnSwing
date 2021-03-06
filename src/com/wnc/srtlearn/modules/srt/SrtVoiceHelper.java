package com.wnc.srtlearn.modules.srt;

import java.io.File;
import java.util.Queue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SrtVoiceHelper
{
    static Position curPosition = Position.NORMAL; // 声道
    static final int EXTERNAL_BUFFER_SIZE = 524288; // 128k

    enum Position
    { // 声道
        LEFT, RIGHT, NORMAL
    };

    static Thread thread;

    public static void stop()
    {
        if(thread != null)
        {
            thread.stop();
        }
        // do nothing to the player, because the Task has done before play
    }

    public synchronized static void play(final String voicePath)
    {
        System.out.println("voicePath:" + voicePath);
        try
        {
            thread = new Thread(new Task(voicePath));
            thread.start();
        }
        catch (Exception e)
        {
            System.out.println("voicePlayEx." + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playInList(final Queue<String> queue)
    {

    }

    static class Task implements Runnable
    {
        String fileName;
        private AudioInputStream stream = null;
        private AudioFormat format = null;
        private Clip clip = null;
        private static SourceDataLine auline;

        public Task(String fileName)
        {
            this.fileName = fileName;
        }

        @Override
        public void run()
        {
            try
            {
                stop();
                File soundFile = new File(fileName); // 播放音乐的文件名
                // From file
                stream = AudioSystem.getAudioInputStream(soundFile);
                format = stream.getFormat();
                if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)
                {
                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                            format.getSampleRate(), 16, format.getChannels(),
                            format.getChannels() * 2, format.getSampleRate(),
                            false); // big
                                    // endian
                    stream = AudioSystem.getAudioInputStream(format, stream);
                }
                // Create the clip
                DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                        stream.getFormat(), AudioSystem.NOT_SPECIFIED);
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open();
                auline.open(stream.getFormat(), auline.getBufferSize());
                auline.start();
                int numRead = 0;
                byte[] buf = new byte[auline.getBufferSize()];
                while ((numRead = stream.read(buf, 0, buf.length)) >= 0)
                {
                    int offset = 0;
                    while (offset < numRead)
                    {
                        offset += auline.write(buf, offset, numRead - offset);
                    }
                }
                auline.drain();
                auline.stop();
                auline.close();
                stream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void stop()
        {
            try
            {
                if(auline != null)
                {
                    auline.stop();
                    auline.close();
                }

            }
            catch (Exception e)
            {
                System.out.println("voiceStopEx." + e.getMessage());
            }
        }

    }

}
