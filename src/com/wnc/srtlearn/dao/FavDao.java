package com.wnc.srtlearn.dao;

import java.awt.Cursor;
import java.util.List;

import srt.SrtInfo;

import com.wnc.srtlearn.pojo.FavoriteMultiSrt;
import com.wnc.srtlearn.pojo.FavoriteSingleSrt;

public class FavDao
{

    public static boolean isExistSingle(SrtInfo mfav, String srtFile)
    {
        // try
        // {
        // Cursor c = db
        // .rawQuery(
        // "SELECT * FROM FAV_MULTI M LEFT JOIN FAV_SINGLE S ON M.ID=S.PID  WHERE SRTFILE=? AND S.FROM_TIME=? AND S.TO_TIME=?",
        // new String[]
        // { srtFile, mfav.getFromTime().toString(),
        // mfav.getToTime().toString() });// 注意大小写
        // if(c.moveToNext())
        // {
        // return true;
        // }
        // c.close();
        // }
        // catch (Exception ex)
        // {
        // throw new RuntimeException(ex.getMessage());
        // }
        // finally
        // {
        // closeDb();
        // }
        return false;
    }

    public static boolean isExistMulti(FavoriteMultiSrt mfav)
    {
        // initDb(context);
        // try
        // {
        // Cursor c = db
        // .rawQuery(
        // "SELECT * FROM FAV_MULTI WHERE SRTFILE=? AND FROM_TIME=? AND TO_TIME=?",
        // new String[]
        // { mfav.getSrtFile(), mfav.getFromTimeStr(),
        // mfav.getToTimeStr() });// 注意大小写
        // if(c.moveToNext())
        // {
        // return true;
        // }
        // c.close();
        // }
        // catch (Exception ex)
        // {
        // throw new RuntimeException(ex.getMessage());
        // }
        // finally
        // {
        // closeDb();
        // }
        return false;
    }

    public static boolean insertFavMulti(FavoriteMultiSrt mfav,
            List<FavoriteSingleSrt> sfavs)
    {
        // try
        // {
        // initDb(context);
        // db.execSQL(
        // "INSERT INTO FAV_MULTI(FAV_TIME,SRTFILE,FROM_TIME ,TO_TIME,HAS_CHILD,TAG) VALUES (?,?,?,?,?,?)",
        // new Object[]
        // { mfav.getFavTimeStr(), mfav.getSrtFile(),
        // mfav.getFromTimeStr(), mfav.getToTimeStr(),
        // mfav.getHasChild(), mfav.getTag() });
        // Cursor c = db.rawQuery("SELECT MAX(ID) MAXID FROM FAV_MULTI",
        // null);// 注意大小写
        // int mfav_Id = 0;
        // if(c.moveToNext())
        // {
        // mfav_Id = BasicNumberUtil.getNumber(getStrValue(c, "MAXID"));
        // }
        // c.close();
        // if(mfav_Id > 0)
        // {
        // return insertFavChilds(mfav_Id, sfavs);
        // }
        // }
        // catch (Exception ex)
        // {
        // ex.printStackTrace();
        // return false;
        // }
        // finally
        // {
        // closeDb();
        // }
        return true;
    }

    private static boolean insertFavChilds(int mfav_Id,
            List<FavoriteSingleSrt> sfavs)
    {
        // try
        // {
        // for (FavoriteSingleSrt sfav : sfavs)
        // {
        //
        // db.execSQL(
        // "INSERT INTO FAV_SINGLE(PID,SINDEX,FROM_TIME ,TO_TIME,ENG,CHS) VALUES (?,?,?,?,?,?)",
        // new Object[]
        // { mfav_Id, sfav.getsIndex(), sfav.getFromTimeStr(),
        // sfav.getToTimeStr(),
        // StringEscapeUtils.escapeSql(sfav.getEng()),
        // StringEscapeUtils.escapeSql(sfav.getChs()) });
        //
        // }
        // }
        // catch (SQLException e)
        // {
        // e.printStackTrace();
        // return false;
        // }
        return true;
    }

    private static String getStrValue(Cursor c, String columnName)
    {
        return "";
        // return c.getString(c.getColumnIndex(columnName));
    }

}
