package test;

import java.util.Map;

import db.DataSource;
import db.DbExecMgr;

public class MultiDataSource
{
    public static void main(String[] args)
    {
        DbExecMgr.refreshCon(DataSource.BUSINESS);
        Map selectAllSqlMap = DbExecMgr
                .getSelectAllSqlMap("SELECT * FROM WORK_TYPE");
        System.out.println(selectAllSqlMap);

        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        Map selectAllSqlMap2 = DbExecMgr
                .getSelectAllSqlMap("SELECT * FROM BOOKS");
        System.out.println(selectAllSqlMap2);

        DbExecMgr.refreshCon(DataSource.SRT);
        Map selectAllSqlMap3 = DbExecMgr
                .getSelectAllSqlMap("SELECT * FROM SRTINFO");
        System.out.println(selectAllSqlMap3.size());
    }
}
