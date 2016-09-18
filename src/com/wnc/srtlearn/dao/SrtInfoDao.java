package com.wnc.srtlearn.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import srt.SearchSrtInfo;

public class SrtInfoDao
{

    public static List<SearchSrtInfo> searchByLan(boolean isEng, String keyWord)
    {
        keyWord = StringEscapeUtils.escapeSql(keyWord);
        List<SearchSrtInfo> list = new ArrayList<SearchSrtInfo>();
        // try
        // {
        // if(isConnect())
        // {
        // final String engOrchs = isEng ? "eng" : "chs";
        // final String engOrchs2 = !isEng ? "eng" : "chs";
        // String sql =
        // "select s.*,t.fromtime,t.totime,e.name from (select min(id) id,min(episode_id) episode_id, min("
        // + engOrchs2
        // + ") "
        // + engOrchs2
        // + ", min(sindex) sindex, "
        // + engOrchs
        // + " from srtinfo group by "
        // + engOrchs
        // +
        // ") s  left join episode e on s.episode_id=e.id left join timeline t on s.id=t.id where  "
        // + engOrchs
        // + " like '%"
        // + keyWord
        // + "%' order by s.episode_id,s.id asc";
        // Cursor c = database.rawQuery(sql, null);
        // c.moveToFirst();
        // while (!c.isAfterLast())
        // {
        // SearchSrtInfo srtInfo = new SearchSrtInfo();
        // srtInfo.setSrtFile(c.getString(c.getColumnIndex("name")));
        //
        // srtInfo.setChs(c.getString(c.getColumnIndex("chs")));
        // srtInfo.setEng(c.getString(c.getColumnIndex("eng")));
        // srtInfo.setFromTime(TimeHelper.parseTimeInfo(c.getString(c
        // .getColumnIndex("fromtime"))));
        // srtInfo.setToTime(TimeHelper.parseTimeInfo(c.getString(c
        // .getColumnIndex("totime"))));
        // srtInfo.setSrtIndex(BasicNumberUtil.getNumber(c.getString(c
        // .getColumnIndex("sindex"))));
        // list.add(srtInfo);
        // c.moveToNext();
        // }
        // }
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        return list;
    }
}
