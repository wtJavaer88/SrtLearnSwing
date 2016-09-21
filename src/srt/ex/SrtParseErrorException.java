package srt.ex;

public class SrtParseErrorException extends SrtException
{

    /**
     * 
     */
    private static final long serialVersionUID = 4188869110979560840L;

    public SrtParseErrorException()
    {
        super(SrtErrCode.SRT_PARSE_ERROR);
    }

}
