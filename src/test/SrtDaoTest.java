package test;

import java.util.List;

import srt.SearchSrtInfo;

import com.wnc.srtlearn.dao.SrtInfoDao;

public class SrtDaoTest
{
    public static void main(String[] args)
    {
        List<SearchSrtInfo> searchByLan = SrtInfoDao.searchByLan(true,
                "despite");
        System.out.println(searchByLan);
    }
}
