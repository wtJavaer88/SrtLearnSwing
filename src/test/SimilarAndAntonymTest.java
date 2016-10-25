package test;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import translate.site.iciba.CibaWordTranslate;

import com.wnc.basic.BasicFileUtil;
import com.wnc.srtlearn.dao.DictionaryDao;

import db.DataSource;
import db.DbExecMgr;

public class SimilarAndAntonymTest
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
                if(DbExecMgr
                        .isExistData("SELECT * FROM SIMILAR_ANTONYM WHERE TOPIC_ID="
                                + topic_id))
                {
                    continue;
                }
                newFixedThreadPool.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        CibaWordTranslate translate = new CibaWordTranslate(
                                word);
                        try
                        {
                            String antonym = translate.getAntonym();
                            String sameAnalysis = translate.getsameAnalysis();
                            String similar = translate.getSimilar();
                            if(similar != null || antonym != null
                                    || sameAnalysis != null)
                                DictionaryDao.insertSimAnt(topic_id, similar,
                                        antonym, sameAnalysis);
                        }
                        catch (Exception e)
                        {
                            BasicFileUtil.writeFileString("sim_ant_err.txt",
                                    topic_id + word + "> " + e.getMessage()
                                            + "\r\n", null, true);
                            e.printStackTrace();
                        }
                    }

                });

            }
            newFixedThreadPool.shutdown();
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }
}
