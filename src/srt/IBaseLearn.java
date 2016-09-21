package srt;

public interface IBaseLearn
{
    public abstract void stopSrtPlay();

    public abstract void play(SrtInfo srtInfo);

    public abstract SrtPlayService getSrtPlayService();

    public abstract void playNext();

    public abstract void playCurrent();

    /**
     * 播放字幕文件
     * 
     * @param strFile
     */
    public abstract void enter(String strFile);
}
