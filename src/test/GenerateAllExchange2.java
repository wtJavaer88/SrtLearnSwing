package test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import translate.bean.WordExchange;
import translate.site.baidu.BaiduWordTranslate;

import com.wnc.basic.BasicFileUtil;
import com.wnc.basic.BasicStringUtil;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.string.PatternUtil;
import com.wnc.tools.FileOp;

import db.DataSource;
import db.DbExecMgr;

public class GenerateAllExchange2
{
    static int a = 0;

    public static void main(String[] args)
    {
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        ThreadPoolExecutor newFixedThreadPool = (ThreadPoolExecutor) Executors
                .newFixedThreadPool(20);

        for (String s : FileOp.readFrom("exchange_err_bak.txt", "UTF-8"))
        {
            a = 0;

            final String word = PatternUtil.getFirstPattern(s, "[a-z]+>")
                    .replace(">", "");

            if(word.length() == 0)
            {
                continue;
            }
            final String topic_id = PatternUtil.getFirstPattern(s, "\\d+");
            if(DbExecMgr
                    .isExistData("SELECT * FROM WORD_EXCHANGE WHERE TOPIC_ID="
                            + topic_id))
            {
                continue;
            }
            System.out.println(word + " " + topic_id);
            newFixedThreadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    BaiduWordTranslate translate = new BaiduWordTranslate(word);
                    try
                    {
                        WordExchange wordExchange = translate.getWordExchange();
                        System.out.println(word + " " + wordExchange);
                        if(notEmptyExchange(wordExchange))
                        {
                            a++;
                            DictionaryDao
                                    .insertExchange(topic_id, wordExchange);
                        }
                    }
                    catch (NullPointerException e)
                    {
                        System.out.println(word + "终结" + a);
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
    }
}
