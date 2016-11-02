package translate.site.iciba;

import translate.abs.IWordTranslate;
import translate.bean.WordExchange;

import com.alibaba.fastjson.JSONArray;

public class CibaWordTranslate extends CibaTranslate implements IWordTranslate
{
    public CibaWordTranslate(String engKeyword)
    {
        super(engKeyword);
    }

    public String getSoundStr() throws Exception
    {
        return getJsonObject().getJSONObject("baesInfo")
                .getJSONArray("symbols").getJSONObject(0)
                .getString("ph_am_mp3");
    }

    public String getBasicInfo() throws Exception
    {
        String ret = "";
        JSONArray jsonArray = getJsonObject().getJSONObject("baesInfo")
                .getJSONArray("symbols").getJSONObject(0).getJSONArray("parts");
        for (int i = 0; i < jsonArray.size(); i++)
        {
            String part = jsonArray.getJSONObject(i).getString("part");
            JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray(
                    "means");
            String means = "";
            for (int j = 0; j < jsonArray2.size(); j++)
            {
                means += jsonArray2.get(j) + "; ";
            }
            ret += part + " " + means + "\n";
        }
        return ret;
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

    public WordExchange getWordExchange() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}
