package com.wnc.srtlearn.dao;

import java.awt.Cursor;

import com.wnc.srtlearn.monitor.WorkMgr;
import com.wnc.srtlearn.monitor.work.WORKTYPE;

public class WorkDao
{

    public static boolean log(WORKTYPE worktype, String info)
    {
        // int typeId = WorkMgr.getTypeId(worktype);
        // if(db == null)
        // {
        // initDb(context);
        // }
        // try
        // {
        // db.execSQL(
        // "INSERT INTO LOG(type, info,create_time) VALUES (?,?,?)",
        // new Object[]
        // { typeId, info, BasicDateUtil.getCurrentDateTimeString() });
        // closeDb();
        // }
        // catch (Exception ex)
        // {
        // throw new RuntimeException(ex.getMessage());
        // }
        return true;
    }

    /**
     * 本次运行的各项工作记录
     * 
     * @param run_id
     * @param work_type
     * @param work_count
     * @param work_time
     * @return
     * @throws RuntimeException
     */
    public static boolean insertWorkMgr(int run_id, WORKTYPE work_type,
            int work_count, long work_time) throws RuntimeException
    {
        int typeId = WorkMgr.getTypeId(work_type);
        // if(db == null)
        // {
        // Log.e("dao", "Not opened Db !");
        // return false;
        // }
        // try
        // {
        // db.execSQL(
        // "INSERT INTO WORKMGR(DAY,RUN_ID,WORK_TYPE,WORK_COUNT ,WORK_TIME) VALUES (?,?,?,?,?)",
        // new Object[]
        // { BasicDateUtil.getCurrentDateString(), run_id, typeId,
        // work_count, work_time });
        // // trigger();
        // }
        // catch (Exception ex)
        // {
        // throw new RuntimeException(ex.getMessage());
        // }
        return true;
    }

    /**
     * 每次运行记录
     * 
     * @param entertime
     * @param exittime
     * @param duration
     * @return
     * @throws RuntimeException
     */
    public static int insertRunRecord(String entertime, String exittime,
            String duration) throws RuntimeException
    {
        int runId = 0;
        // if(db == null)
        // {
        // Log.e("dao", "Not opened Db !");
        // return runId;
        // }
        // try
        // {
        // db.execSQL(
        // "INSERT INTO RUN_RECORD(ENTER_TIME, EXIT_TIME,DURATION) VALUES (?,?,?)",
        // new Object[]
        // { entertime, exittime, duration });
        // Cursor c = db
        // .rawQuery("SELECT MAX(ID) MAXID FROM RUN_RECORD", null);// 注意大小写
        // if(c.moveToNext())
        // {
        // runId = BasicNumberUtil.getNumber(getStrValue(c, "MAXID"));
        // }
        // c.close();
        // // trigger();
        // }
        // catch (Exception ex)
        // {
        // throw new RuntimeException(ex.getMessage());
        // }
        return runId;
    }

    private static boolean checkExist()
    {
        // Cursor c = db.rawQuery("SELECT * FROM RUN_RECORD", new String[]
        // {});
        // if(c.moveToNext())
        // {
        // return true;
        // }
        // c.close();
        return false;
    }

    public static int getRunCounts()
    {
        // Cursor c = db.rawQuery("SELECT * FROM RUN_RECORD", new String[]
        // {});
        // int num = 0;
        // while (c.moveToNext())
        // {
        // num++;
        // }
        // c.close();
        return 0;
    }

    // private static void trigger()
    // {
    // BackUpDataUtil.canBackUpDb = true;
    // }

    private static String getStrValue(Cursor c, String columnName)
    {
        return "";
        // return c.getString(c.getColumnIndex(columnName));
    }

}
