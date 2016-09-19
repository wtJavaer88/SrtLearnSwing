package translate.site.iciba;

import translate.abs.IJSONResource;
import translate.abs.ITranslate;
import translate.util.JsoupHelper;

import com.alibaba.fastjson.JSONObject;

public class CibaTranslate implements ITranslate, IJSONResource
{
    protected final String API = "http://www.iciba.com/index.php?a=getWordMean&c=search&word=%s";
    protected String engKeyword;
    private JSONObject jsonObject;

    public CibaTranslate(String engKeyword)
    {
        this.engKeyword = engKeyword;
    }

    public String getApiLink()
    {
        return String.format(API, this.engKeyword);
    }

    public JSONObject getJsonObject() throws Exception
    {
        if(jsonObject != null)
        {
            return jsonObject;
        }
        return JSONObject.parseObject(JsoupHelper.getJsonResult(getApiLink()));
    }

    public String getWebUrlForMobile()
    {
        return "http://m.iciba.com/" + engKeyword;
    }

    public String getWebUrlForPC()
    {
        return "http://www.iciba.com/" + engKeyword;
    }

}
