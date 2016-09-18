package srt;

public interface IBaseLearn
{
    public abstract void stopSrtPlay();

    public abstract void play(SrtInfo srtInfo);

    public abstract SrtPlayService getSrtPlayService();

    public abstract void playNext();

    public abstract void playCurrent();
}
