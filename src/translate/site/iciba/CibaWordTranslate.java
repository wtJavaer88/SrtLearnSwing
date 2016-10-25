package translate.site.iciba;

import translate.abs.IWordTranslate;
import translate.bean.WordExchange;

public class CibaWordTranslate extends CibaTranslate implements IWordTranslate
{
    public CibaWordTranslate(String engKeyword)
    {
        super(engKeyword);
    }

    public String getBasicInfo() throws Exception
    {
        return getJsonObject().getJSONArray("synonym").toString();
    }

    public String getExampleBasic() throws Exception
    {
        return null;
    }

    public String getExampleAdvance() throws Exception
    {
        return null;
    }

    public String getEngEng() throws Exception
    {
        return null;
    }

    public String getCollocation() throws Exception
    {
        return null;
    }

    public String getSyntax() throws Exception
    {
        return null;
    }

    public String getsameAnalysis() throws Exception
    {
        if(!getJsonObject().containsKey("sameAnalysis"))
        {
            return null;
        }
        return getJsonObject().getJSONArray("sameAnalysis").toString();
    }

    public String getSimilar() throws Exception
    {
        if(!getJsonObject().containsKey("synonym"))
        {
            return null;
        }
        return getJsonObject().getJSONArray("synonym").toString();
    }

    public String getAntonym() throws Exception
    {
        if(!getJsonObject().containsKey("antonym"))
        {
            return null;
        }
        return getJsonObject().getJSONArray("antonym").toString();
    }

    @Override
    public WordExchange getWordExchange()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
