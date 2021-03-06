package com.wnc.srtlearn.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import translate.bean.WordExchange;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;
import com.wnc.srtlearn.modules.translate.Topic;
import com.wnc.string.PatternUtil;

import db.DataSource;
import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;

public class DictionaryDao
{
    static Set<Topic> topics = new HashSet<Topic>();
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
            topic.setTopic_base_word("" + rowMap.get("TOPIC_WORD"));
            topic.setState("BASIC");
            topics.add(topic);
        }

        Map selectAllSqlMap2 = DbExecMgr
                .getSelectAllSqlMap("select * from word_exchange we,topic_resource res,dictionary dict,books "
                        + "where we.topic_id=res.topic and res.topic=dict.topic_id and books.id=res.book_id order by book_id desc");
        for (int i = 1; i <= selectAllSqlMap2.size(); i++)
        {
            Map rowMap = (Map) selectAllSqlMap2.get(i);
            List<Topic> extopics = getExchangeTopics(rowMap);
            topics.addAll(extopics);
        }
    }

    public static void initStatic()
    {

    }

    public static Set<Topic> getCETTopic(String dialog)
    {
        Set<Topic> result = new HashSet<Topic>();
        List<String> splites = PatternUtil.getPatternStrings(dialog, "\\w+");
        for (Topic topic : topics)
        {
            String trim = topic.getTopic_word().trim();

            for (String s : splites)
            {
                if(s.equalsIgnoreCase(trim))
                {
                    result.add(topic);
                }
            }
        }
        splites.clear();
        splites = null;
        return result;
    }

    private static List<Topic> getExchangeTopics(Map rowMap)
    {
        String[] exchanges =
        { "WORD_DONE", "WORD_ER", "WORD_EST", "WORD_ING", "WORD_PAST",
                "WORD_PL", "WORD_THIRD" };
        List<Topic> exTopics = new ArrayList<Topic>();
        String bookName = "" + rowMap.get("NAME");
        String topic_id = "" + rowMap.get("TOPIC_ID");
        String mean_cn = "" + rowMap.get("MEAN_CN");
        for (String ex : exchanges)
        {
            String topic_word = "" + rowMap.get(ex);
            if(BasicStringUtil.isNullString(topic_word))
            {
                continue;
            }
            Topic topic = new Topic();
            topic.setBookName(bookName);
            topic.setTopic_id(topic_id);
            topic.setTopic_word(topic_word);
            topic.setMean_cn(mean_cn);
            topic.setTopic_base_word("" + rowMap.get("TOPIC_WORD"));
            topic.setState(ex);
            exTopics.add(topic);
        }

        return exTopics;
    }

    public static boolean insertExchange(String topic_id,
            WordExchange wordExchange) throws SQLException
    {
        DbFieldSqlUtil util = new DbFieldSqlUtil("WORD_EXCHANGE", "");
        util.addInsertField(new DbField("TOPIC_ID", topic_id));
        util.addInsertField(new DbField("WORD_THIRD", wordExchange
                .getWord_third()));
        util.addInsertField(new DbField("WORD_DONE", wordExchange
                .getWord_done()));
        util.addInsertField(new DbField("WORD_PL", wordExchange.getWord_pl()));
        util.addInsertField(new DbField("WORD_ING", wordExchange.getWord_ing()));
        util.addInsertField(new DbField("WORD_PAST", wordExchange
                .getWord_past()));
        util.addInsertField(new DbField("WORD_EST", wordExchange.getWord_est()));
        util.addInsertField(new DbField("WORD_ER", wordExchange.getWord_er()));
        BasicFileUtil.writeFileString("exchange_sql.txt", util.getInsertSql()
                + "\r\n", null, true);
        System.out.println(util.getInsertSql());
        return DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
    }

    public static boolean insertSimAnt(String topic_id, String sim, String ant,
            String same_analysis) throws SQLException
    {
        DbFieldSqlUtil util = new DbFieldSqlUtil("SIMILAR_ANTONYM", "");
        util.addInsertField(new DbField("TOPIC_ID", topic_id));
        util.addInsertField(new DbField("SIMILAR_WORDS", StringEscapeUtils
                .escapeSql(sim)));
        util.addInsertField(new DbField("ANTONYM_WORDS", StringEscapeUtils
                .escapeSql(ant)));
        util.addInsertField(new DbField("SAME_ANALYSIS", StringEscapeUtils
                .escapeSql(same_analysis)));
        System.out.println(util.getInsertSql());
        // return true;
        return DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
    }

    public static boolean updateMean(String topic_id, String mean)
            throws SQLException
    {
        DbFieldSqlUtil util = new DbFieldSqlUtil("DICTIONARY", "");
        util.addWhereField(new DbField("TOPIC_ID", topic_id));
        util.addUpdateField(new DbField("MEAN_CN", StringEscapeUtils
                .escapeSql(mean)));
        System.out.println(util.getUpdateSql());
        return DbExecMgr.execOnlyOneUpdate(util.getUpdateSql());
    }

    public static boolean updateMean2(String id, String mean)
            throws SQLException
    {
        DbFieldSqlUtil util = new DbFieldSqlUtil("DICTIONARY", "");
        util.addWhereField(new DbField("ID", id));
        util.addUpdateField(new DbField("MEAN_CN", StringEscapeUtils
                .escapeSql(mean)));
        // System.out.println(util.getUpdateSql());
        // return true;
        return DbExecMgr.execOnlyOneUpdate(util.getUpdateSql());
    }
}
