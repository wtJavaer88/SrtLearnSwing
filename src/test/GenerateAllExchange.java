package test;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import translate.bean.WordExchange;
import translate.site.baidu.BaiduWordTranslate;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;
import com.wnc.srtlearn.dao.DictionaryDao;

import db.DataSource;
import db.DbExecMgr;

public class GenerateAllExchange
{
    static int a = 0;

    public static void main(String[] args)
    {
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        for (int x = 3; x < 5; x++)
        {
            a = 0;
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
                        .isExistData("SELECT * FROM WORD_EXCHANGE WHERE TOPIC_ID="
                                + topic_id))
                {
                    continue;
                }
                newFixedThreadPool.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        BaiduWordTranslate translate = new BaiduWordTranslate(
                                word);
                        try
                        {
                            WordExchange wordExchange = translate
                                    .getWordExchange();
                            if(notEmptyExchange(wordExchange))
                            {
                                System.out.println(word + " " + wordExchange);
                                a++;
                                DictionaryDao.insertExchange(topic_id,
                                        wordExchange);
                            }
                        }
                        catch (NullPointerException e)
                        {
                            System.out.println("终结" + a);
                        }
                        catch (Exception e)
                        {
                            BasicFileUtil.writeFileString("exchange_err.txt",
                                    topic_id + word + "> " + e.getMessage()
                                            + "\r\n", null, true);
                            e.printStackTrace();
                        }
                    }

                    private boolean notEmptyExchange(WordExchange wordExchange)
                    {
                        return BasicStringUtil.isNotNullString(wordExchange
                                .getWord_done())
                                || BasicStringUtil.isNotNullString(wordExchange
                                        .getWord_pl())
                                || BasicStringUtil.isNotNullString(wordExchange
                                        .getWord_past())
                                || BasicStringUtil.isNotNullString(wordExchange
                                        .getWord_ing())
                                || BasicStringUtil.isNotNullString(wordExchange
                                        .getWord_third())
                                || BasicStringUtil.isNotNullString(wordExchange
                                        .getWord_er())
                                || BasicStringUtil.isNotNullString(wordExchange
                                        .getWord_est());
                    }
                });

            }
            newFixedThreadPool.shutdown();
            try
            {
                Thread.sleep(20000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }
}
