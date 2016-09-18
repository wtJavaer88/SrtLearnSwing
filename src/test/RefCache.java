package test;

import db.DataSource;
import db.DbExecMgr;

public class RefCache
{
    public static void main(String[] args)
    {
        DbExecMgr.refreshCon(DataSource.BUSINESS);
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        DbExecMgr.refreshCon(DataSource.SRT);
        DbExecMgr.refreshCon(DataSource.BUSINESS);
        DbExecMgr.refreshCon(DataSource.DICTIONARY);
        DbExecMgr.refreshCon(DataSource.SRT);
    }
}
