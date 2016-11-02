package test;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.nodes.Document;

import translate.util.JsoupHelper;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;
import com.wnc.srtlearn.dao.DictionaryDao;

import db.DataSource;
import db.DbExecMgr;

/**
 * Book之外的单词同步
 * 
 * @author cpr216
 * 
 */
public class GenerateAllMeans2
{
    public static void main(String[] args)
    {
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        ThreadPoolExecutor newFixedThreadPool = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(20);
        Map selectAllSqlMap = DbExecMgr
                .getSelectAllSqlMap("select * from dictionary where topic_id not in (select distinct topic from topic_resource)  and topic_word not like '% %'");
        for (int i = 1; i <= selectAllSqlMap.size(); i++)
        {
            Map rowMap = (Map) selectAllSqlMap.get(i);
            final String word = rowMap.get("TOPIC_WORD").toString();
            final String id = rowMap.get("ID").toString();
            final String old_mean = rowMap.get("MEAN_CN").toString();
            newFixedThreadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    // CibaWordTranslate translate = new
                    // CibaWordTranslate(word);
                    try
                    {
                        Document documentResult = JsoupHelper
                                .getDocumentResult("http://www.iciba.com/"
                                        + word);
                        String mean = documentResult.select(".info-base ul")
                                .text();
                        if(BasicStringUtil.isNotNullString(mean))
                        {
                            // System.out.println(word + ":" + old_mean
                            // + "---EEE\n" + mean);
                            DictionaryDao.updateMean2(id, mean);
                        }
                    }
                    catch (Exception e)
                    {
                        BasicFileUtil.writeFileString("means_err.txt", id + " "
                                + word + "> " + e.getMessage() + "\r\n", null,
                                true);
                        e.printStackTrace();
                    }
                }

            });

        }
        newFixedThreadPool.shutdown();
    }
}
