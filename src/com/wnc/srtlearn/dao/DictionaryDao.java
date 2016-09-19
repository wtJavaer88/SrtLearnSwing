package com.wnc.srtlearn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.DataSource;
import db.DbExecMgr;

public class DictionaryDao
{
    static List<Topic> topics = new ArrayList<Topic>();
    static
    {
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        Map selectAllSqlMap = DbExecMgr
                .getSelectAllSqlMap("select * from topic_resource res,dictionary dict,books"
                        + " where res.topic=dict.topic_id and books.id=res.book_id order by book_id desc");
        for (int i = 1; i <= selectAllSqlMap.size(); i++)
        {
            Map rowMap = (Map) selectAllSqlMap.get(i);
            Topic topic = new Topic();
            topic.setBookName("" + rowMap.get("NAME"));
            topic.setTopic_id("" + rowMap.get("TOPIC_ID"));
            topic.setTopic_word("" + rowMap.get("TOPIC_WORD"));
            topic.setMean_cn("" + rowMap.get("MEAN_CN"));
            topics.add(topic);
        }
    }

    public static Topic getCETTopic(String dialog)
    {
        for (Topic topic : topics)
        {
            String trim = topic.getTopic_word().trim();
            String[] splites = dialog.split(" ");
            for (String s : splites)
            {
                if(s.equalsIgnoreCase(trim))
                {
                    return topic;
                }
            }
        }
        return null;
    }
}
