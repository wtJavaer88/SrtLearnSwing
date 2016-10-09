package com.wnc.srtlearn.setting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config
{
    public static String dbFolder;
    public static String defaultSrt;
    static
    {
        Properties p = new Properties();
        try
        {
            InputStream ips = Config.class
                    .getResourceAsStream("/config.properties");
            InputStreamReader isr = new InputStreamReader(ips, "UTF-8");
            BufferedReader ipss = new BufferedReader(isr);

            p.load(ipss);
            dbFolder = p.getProperty("dbFolder");
            defaultSrt = p.getProperty("defaultSrt");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
