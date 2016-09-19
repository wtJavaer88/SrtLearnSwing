package com.wnc.srtlearn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import srt.SearchSrtInfo;
import srt.TimeHelper;

import com.wnc.basic.BasicNumberUtil;

import db.DataSource;
import db.DbExecMgr;

public class SrtInfoDao
{

    public static List<SearchSrtInfo> searchByLan(boolean isEng, String keyWord)
    {
        DbExecMgr.refreshCon(DataSource.SRT);
        keyWord = StringEscapeUtils.escapeSql(keyWord);
        List<SearchSrtInfo> list = new ArrayList<SearchSrtInfo>();
        final String engOrchs = isEng ? "eng" : "chs";
        final String engOrchs2 = !isEng ? "eng" : "chs";
        String sql = "select s.*,t.fromtime,t.totime,e.name from (select min(id) id,min(episode_id) episode_id, min("
                + engOrchs2
                + ") "
                + engOrchs2
                + ", min(sindex) sindex, "
                + engOrchs
                + " from srtinfo group by "
                + engOrchs
                + ") s  left join episode e on s.episode_id=e.id left join timeline t on s.id=t.id where  "
                + engOrchs
                + " like '%"
                + keyWord
                + "%' order by s.episode_id,s.id asc";
        System.out.println(sql);
        Map selectAllSqlMap = DbExecMgr.getSelectAllSqlMap(sql);

        for (int i = 1; i <= selectAllSqlMap.size(); i++)
        {
            Map rowMap = (Map) selectAllSqlMap.get(i);
            SearchSrtInfo srtInfo = new SearchSrtInfo();
            srtInfo.setSrtFile("" + rowMap.get("NAME"));
            srtInfo.setChs("" + rowMap.get("CHS"));
            srtInfo.setEng("" + rowMap.get("ENG"));
            srtInfo.setFromTime(TimeHelper.parseTimeInfo(""
                    + rowMap.get("FROMTIME")));
            srtInfo.setToTime(TimeHelper.parseTimeInfo(""
                    + rowMap.get("TOTIME")));
            srtInfo.setSrtIndex(BasicNumberUtil.getNumber(""
                    + rowMap.get("sindex")));
            list.add(srtInfo);
        }
        return list;
    }
}
