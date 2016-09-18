package com.wnc.srtlearn.modules.bihua;

import com.wnc.basic.BasicFileUtil;
import com.wnc.srtlearn.modules.srt.DownSwfTask;
import common.uihelper.MyAppParams;
import common.utils.TextFormatUtil;

public class SwfPlayMgr
{

    public final static String SWF_HTML = MyAppParams.SWF_FOLDER
            + "swfplayer.htm";
    private final static String SWF_API = "http://zd.diyifanwen.com/Files/WordSwf/%s.swf";

    public static void reCreateHtml(String hanzi)
    {
        final String localSwfFile = MyAppParams.SWF_FOLDER + hanzi + ".swf";
        if(BasicFileUtil.isExistFile(localSwfFile)
                && BasicFileUtil.getFileSize(localSwfFile) > 0)
        {
            writeSwfData(hanzi, true);
        }
        else
        {
            writeSwfData(hanzi, false);
            downLoadSwf(hanzi);
        }
    }

    private static void downLoadSwf(String hanzi)
    {
        String destSave = MyAppParams.SWF_FOLDER + hanzi + ".swf";
        String swfUrl = getSwfUrl(TextFormatUtil.getUrlEncodeStr(hanzi));
        new Thread(new DownSwfTask(destSave, swfUrl)).start();
    }

    private static void writeSwfData(String hanzi, boolean isLocal)
    {
        StringBuilder accum = new StringBuilder(1024);
        String movie = isLocal ? hanzi + ".swf" : getSwfUrl(hanzi);
        accum.append("<object id=\"forfun\" classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" width=\"600\" height=\"600\" "
                + "codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\">\n");
        accum.append(" <param name=\"movie\" value=\"" + movie + "\">\n");
        accum.append("<param name=\"quality\" value=\"high\">\n");
        accum.append("<param name=\"bgcolor\" value=\"#F0F0F0\">\n");
        accum.append("<param name=\"menu\" value=\"false\">\n");
        accum.append("<param name=\"wmode\" value=\"opaque\">\n");
        accum.append("<param name=\"FlashVars\" value=\"\">\n");
        accum.append("<param name=\"allowScriptAccess\" value=\"sameDomain\">\n");
        accum.append("<embed id=\"forfunex\" src=\""
                + movie
                + "\" width=\"600\" height=\"600\" align=\"middle\" allowScriptAccess=\"sameDomain\" menu=\"false\""
                + " type=\"application/x-shockwave-flash\" pluginspage=\"http://www.adobe.com/go/getflashplayer\">\n");
        accum.append("</object>");
        BasicFileUtil.writeFileString(SWF_HTML, accum.toString(), "UTF-8",
                false);
    }

    private static String getSwfUrl(String hanzi)
    {
        return String.format(SWF_API, hanzi);
    }
}
