package com.wnc.srtlearn.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import srt.SrtInfo;

import com.wnc.srtlearn.pojo.FavoriteMultiSrt;
import com.wnc.srtlearn.pojo.FavoriteSingleSrt;
import common.utils.UUIDUtil;

import db.DataSource;
import db.DbExecMgr;
import db.DbField;
import db.DbFieldSqlUtil;

public class FavDao
{

    public static boolean isExistSingle(SrtInfo mfav, String srtFile)
    {
        DbExecMgr.refreshCon(DataSource.BUSINESS);
        return DbExecMgr
                .isExistData("SELECT * FROM FAV_MULTI M LEFT JOIN FAV_SINGLE S ON M.UUID=S.P_UUID  WHERE SRTFILE='"
                        + srtFile
                        + "' AND S.FROM_TIME='"
                        + mfav.getFromTime().toString()
                        + "' AND S.TO_TIME='"
                        + mfav.getToTime().toString() + "'");
    }

    public static boolean isExistMulti(FavoriteMultiSrt mfav)
    {
        DbExecMgr.refreshCon(DataSource.BUSINESS);
        DbFieldSqlUtil util = new DbFieldSqlUtil("FAV_MULTI", "");
        util.addWhereField(new DbField("SRTFILE", mfav.getSrtFile()));
        util.addWhereField(new DbField("FROM_TIME", mfav.getFromTimeStr()));
        util.addWhereField(new DbField("TO_TIME", mfav.getToTimeStr()));
        return DbExecMgr.isExistData(util.getSelectSql());
    }

    public static boolean insertFavMulti(FavoriteMultiSrt mfav,
            List<FavoriteSingleSrt> sfavs)
    {
        DbExecMgr.refreshCon(DataSource.BUSINESS);
        DbFieldSqlUtil util = new DbFieldSqlUtil("FAV_MULTI", "");
        util.addInsertField(new DbField("SRTFILE", mfav.getSrtFile()));
        String uuid = UUIDUtil.getUUID();
        util.addInsertField(new DbField("UUID", uuid));
        util.addInsertField(new DbField("FROM_TIME", mfav.getFromTimeStr()));
        util.addInsertField(new DbField("TO_TIME", mfav.getToTimeStr()));
        util.addInsertField(new DbField("FAV_TIME", mfav.getFavTimeStr()));
        util.addInsertField(new DbField("HAS_CHILD", mfav.getHasChild() + "",
                "NUMBER"));
        util.addInsertField(new DbField("TAG", mfav.getTag()));
        try
        {
            DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
            return insertFavChilds(uuid, sfavs);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean insertFavChilds(String p_uuid,
            List<FavoriteSingleSrt> sfavs)
    {
        for (FavoriteSingleSrt sfav : sfavs)
        {
            DbExecMgr.refreshCon(DataSource.BUSINESS);
            DbFieldSqlUtil util = new DbFieldSqlUtil("FAV_SINGLE", "");
            util.addInsertField(new DbField("P_UUID", p_uuid));
            util.addInsertField(new DbField("FROM_TIME", sfav.getFromTimeStr()));
            util.addInsertField(new DbField("TO_TIME", sfav.getToTimeStr()));
            util.addInsertField(new DbField("SINDEX", sfav.getsIndex() + "",
                    "NUMBER"));
            util.addInsertField(new DbField("ENG", StringEscapeUtils
                    .escapeSql(sfav.getEng())));
            util.addInsertField(new DbField("CHS", StringEscapeUtils
                    .escapeSql(sfav.getChs())));
            try
            {
                DbExecMgr.execOnlyOneUpdate(util.getInsertSql());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
