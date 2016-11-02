package test;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import translate.site.iciba.CibaWordTranslate;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;
import com.wnc.srtlearn.dao.DictionaryDao;

import db.DataSource;
import db.DbExecMgr;

/**
 * Book内的单词同步
 * 
 * @author cpr216
 * 
 */
public class GenerateAllMeans
{
    public static void main(String[] args)
    {
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        for (int x = 0; x < 5; x++)
        {
            Map selectAllSqlMap = DbExecMgr
                    .getSelectAllSqlMap("select * from topic_resource res,dictionary dict,books"
                            + " where res.topic=dict.topic_id and books.id=res.book_id order by book_id desc limit "
                            + x * 1000 + ",1000");
            ThreadPoolExecutor newFixedThreadPool = (ThreadPoolExecutor) Executors
                    .newFixedThreadPool(20);
            for (int i = 1; i <= selectAllSqlMap.size(); i++)
            {
                Map rowMap = (Map) selectAllSqlMap.get(i);
                final String word = rowMap.get("TOPIC_WORD").toString();
                final String topic_id = rowMap.get("TOPIC_ID").toString();
                final String old_mean = rowMap.get("MEAN_CN").toString();
                newFixedThreadPool.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        CibaWordTranslate translate = new CibaWordTranslate(
                                word);
                        try
                        {
                            String mean = translate.getBasicInfo();
                            if(BasicStringUtil.isNotNullString(mean))
                            {
                                // System.out.println(word + ":" + old_mean
                                // + "---EEE\n" + mean);
                                DictionaryDao.updateMean(topic_id, mean);
                            }
                        }
                        catch (Exception e)
                        {
                            BasicFileUtil.writeFileString("means_err.txt",
                                    topic_id + word + "> " + e.getMessage()
                                            + "\r\n", null, true);
                            e.printStackTrace();
                        }
                    }

                });

            }
            newFixedThreadPool.shutdown();
        }
    }
}
