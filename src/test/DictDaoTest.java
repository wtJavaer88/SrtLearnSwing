package test;

import java.util.Set;

import com.wnc.basic.BasicRunTimeUtil;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.srtlearn.modules.translate.Topic;

public class DictDaoTest
{
    public static void main(String[] args)
    {
        BasicRunTimeUtil basicRunTimeUtil = new BasicRunTimeUtil("");
        basicRunTimeUtil.beginRun();
        Set<Topic> topics = DictionaryDao
                .getCETTopic("abiding you despite the damages");
        System.out.println("find:" + topics);
        basicRunTimeUtil.finishRun();
        System.out.println(basicRunTimeUtil.getRunMilliSecond());
    }
}