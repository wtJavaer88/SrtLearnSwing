package test;

import translate.site.baidu.BaiduWordTranslate;

public class WordExchangeTest
{
    public static void main(String[] args) throws Exception
    {
        BaiduWordTranslate translate = new BaiduWordTranslate("crash");
        System.out.println(translate.getWordExchange());
    }
}
