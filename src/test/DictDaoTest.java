package test;

import com.wnc.basic.BasicRunTimeUtil;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.srtlearn.dao.Topic;

public class DictDaoTest
{
    public static void main(String[] args)
    {
        BasicRunTimeUtil basicRunTimeUtil = new BasicRunTimeUtil("");
        basicRunTimeUtil.beginRun();
        Topic topic = DictionaryDao.getCETTopic("you despite the damages");
        System.out.println(topic);
        basicRunTimeUtil.finishRun();
        System.out.println(basicRunTimeUtil.getRunMilliSecond());
    }
}