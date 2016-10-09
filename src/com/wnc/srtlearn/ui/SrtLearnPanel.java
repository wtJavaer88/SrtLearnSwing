package com.wnc.srtlearn.ui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import srt.DataHolder;
import srt.Log;
import srt.SRT_VIEW_TYPE;
import srt.SrtInfo;
import srt.SrtPlayService;
import srt.SrtTextHelper;
import srt.TimeHelper;
import srt.ex.ReachFileTailException;
import srt.ex.SrtException;
import translate.site.iciba.CibaWordTranslate;

import com.wnc.basic.BasicFileUtil;
import com.wnc.run.RunCmd;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.srtlearn.dao.FavDao;
import com.wnc.srtlearn.modules.translate.Topic;
import com.wnc.srtlearn.setting.Config;
import com.wnc.srtlearn.setting.SrtSetting;
import common.swing.AlertUtil;
import common.swing.INewFrame;
import common.swing.ImplAWTEventListener;
import common.swing.KeyCallBack;
import common.swing.SnapUtil;
import common.uihelper.MyAppParams;
import common.utils.MyFileUtil;
import common.utils.TextFormatUtil;

public class SrtLearnPanel extends JPanel implements INewFrame, srt.IBaseLearn,
        KeyCallBack
{
    private static final int _SCROLLBAR_WIDTH = 20;// 滚动条的宽度
    private static final int _FONT_SIZE = 20;// 字体大小
    private static final int _FONT_HEIGHT = 35;// 字体实际像素高度
    private static final int _SCROLLPANEL_WIDTH = 300;// 滚动条宽度
    private static final int _SCROLLPANEL_HEIGHT = 200;// 滚动条高度
    SrtFrame m;
    JButton chooseBt;// 选择
    JButton startBt;// 开始
    JButton nextBt;// 下一个
    JButton preBt;// 上一个
    JButton favoriteBt;// 收藏
    JButton snapBt;// 截图
    JCheckBox unComplete;

    JLabel processLabel;
    JLabel dictLabel;// 显示字典信息
    JScrollPane scrollPane;// 包裹dictLabel
    public JTextField jtfSrtFile;
    public JTextArea jtaEng;
    public JTextArea jtaChs;

    private final String SRT_PLAY_TEXT = "播放";
    private final String SRT_STOP_TEXT = "停止";

    public SrtLearnPanel(SrtFrame mf)
    {
        m = mf;
        init();
        setBounds();
        addComp();
        listen();
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.addAWTEventListener(new ImplAWTEventListener(this),
                AWTEvent.KEY_EVENT_MASK);

        // String defaultSrt =
        // "D:\\Users\\wnc\\oral\\字幕\\Friends.S01\\S01E24.ass";
        // defaultSrt = "E:\\资源\\oral\\字幕\\Friends.S01\\S01E24.ass";
        // defaultSrt = "D:\\用户目录\\workspace\\SrtLearn\\res2\\10.ao ou iu.lrc";
        enter(Config.defaultSrt);
    }

    @Override
    public void init()
    {
        this.setLayout(null);

        Font font = new Font("微软雅黑", Font.BOLD, _FONT_SIZE);

        chooseBt = new JButton("选择字幕");// 选择
        startBt = new JButton("Start!");// 开始
        nextBt = new JButton("Next");// 下一个
        preBt = new JButton("Pre");// 上一个
        favoriteBt = new JButton("喜欢");
        snapBt = new JButton("截图");
        processLabel = new JLabel();// 进度标签
        dictLabel = new JLabel();
        dictLabel.setFont(font);
        dictLabel.setHorizontalAlignment(SwingConstants.LEFT);
        setLabelAndAutoSize("", 0);
        scrollPane = new JScrollPane(dictLabel);
        scrollPane.setLayout(new ScrollPaneLayout());
        // scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtfSrtFile = new JTextField(200);
        jtfSrtFile.setToolTipText("请选择一集字幕，或者手动拖拽！");
        jtaEng = getTextArea(font);
        jtaChs = getTextArea(font);
    }

    public JTextArea getTextArea(Font font)
    {
        JTextArea area = new JTextArea("", 20, 80);
        area = new JTextArea("", 20, 80);
        area.setSelectedTextColor(Color.RED);
        area.setLineWrap(true); // 激活自动换行功能
        area.setWrapStyleWord(true); // 激活断行不断字功能
        area.setFont(font);
        area.setEditable(false);
        return area;
    }

    @Override
    public void setBounds()
    {
        chooseBt.setBounds(50, 50, 100, 30);
        jtfSrtFile.setBounds(160, 50, 300, 30);
        // unComplete.setBounds(170, 50, 120, 30);

        startBt.setBounds(50, 120, 100, 30);
        preBt.setBounds(150, 120, 100, 30);
        nextBt.setBounds(250, 120, 100, 30);
        favoriteBt.setBounds(350, 120, 100, 30);
        snapBt.setBounds(450, 120, 100, 30);

        processLabel.setBounds(50, 160, 300, 30);
        scrollPane.setBounds(480, 160, _SCROLLPANEL_WIDTH, _SCROLLPANEL_HEIGHT);

        jtaEng.setBounds(50, 200, 400, 90);
        jtaChs.setBounds(50, 300, 400, 90);
    }

    @Override
    public void addComp()
    {
        // this.add(unComplete);
        this.add(chooseBt);
        this.add(startBt);
        this.add(nextBt);
        this.add(preBt);
        this.add(favoriteBt);
        this.add(snapBt);

        this.add(processLabel);
        this.add(scrollPane);

        this.add(jtfSrtFile);
        this.add(jtaEng);
        this.add(jtaChs);
    }

    public boolean getIfComplet()
    {
        return !this.unComplete.isSelected();
    }

    @Override
    public void listen()
    {
        chooseBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                JFileChooser fChooser = new JFileChooser(
                        "D:\\Users\\wnc\\oral\\字幕\\Friends.S01\\");
                fChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Srt Files", "srt", "ass", "cnpy", "lrc", "saa");
                fChooser.setFileFilter(filter);
                fChooser.setMultiSelectionEnabled(false);

                fChooser.showOpenDialog(null);
                File tmpFile = fChooser.getSelectedFile();
                enter(tmpFile.getAbsolutePath());
            }

        });
        startBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                clickPlayBtn();
            }

        });
        nextBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                getSrtInfoAndPlay(SRT_VIEW_TYPE.VIEW_RIGHT);
            }
        });
        preBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                getSrtInfoAndPlay(SRT_VIEW_TYPE.VIEW_LEFT);
            }

        });
        favoriteBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                boolean favorite = false;
                try
                {
                    favorite = srtPlayService.favorite();
                }
                catch (SrtException e)
                {
                    e.printStackTrace();
                }
                if(favorite)
                {
                    favoriteBt.setForeground(Color.RED);
                }
            }
        });
        snapBt.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                // 截图所处时间点
                int time = 0;
                try
                {
                    time = 1 + (int) (TimeHelper.getTime(DataHolder
                            .getCurrent().getFromTime()) / 1000);
                }
                catch (SrtException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String file = DataHolder.getFileKey();
                String snapPic = SnapUtil.getSnapPic(time, file);
                if(BasicFileUtil.isExistFile(snapPic)
                        && BasicFileUtil.getFileSize(snapPic) > 0)
                {
                    com.wnc.run.RunCmd.runCommand("cmd /c start " + snapPic);
                }
                else
                {
                    AlertUtil.showShortToast("截图失败!");
                }
            }
        });

        jtaEng.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount() == 1)
                {

                }
                if(e.getClickCount() == 2)
                {// 双击
                    int start = jtaEng.getSelectionStart();
                    int end = jtaEng.getSelectionEnd();
                    final String selWord = jtaEng.getText().substring(start,
                            end);
                    System.out.println("双击选词:" + selWord);
                    new Thread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            RunCmd.runCommand("\"C:\\Program Files\\Internet Explorer\\iexplore.exe\" "
                                    + "\""
                                    + new CibaWordTranslate(selWord)
                                            .getWebUrlForPC() + "\"");
                        }
                    }).start();

                }
            }
        });
    }

    public void clickPlayBtn()
    {
        if(srtPlayService.isRunning())
        {
            stopSrtPlay();
        }
        else
        {
            beginSrtPlay();
        }
    }

    private void beginSrtPlay()
    {
        startBt.setText(SRT_STOP_TEXT);
        srtPlayService.playSrt();
    }

    public void enter(String srtFilePath)
    {
        this.jtfSrtFile.setText(srtFilePath);
        try
        {
            srtPlayService.showNewSrtFile(srtFilePath);
        }
        catch (SrtException e)
        {
            e.printStackTrace();
            AlertUtil.showShortToast(e.getMessage());
        }
    }

    protected SrtPlayService srtPlayService = new SrtPlayService(this);

    @Override
    public void refresh()
    {
    }

    public void getSrtInfoAndPlay(SRT_VIEW_TYPE view_type)
    {
        try
        {
            SrtInfo srt = srtPlayService.getSrtInfo(view_type);
            play(srt);
        }
        catch (ReachFileTailException ex)
        {
            if(SrtSetting.isAutoNextEP())
            {
                final long tip_time = 2000;

                String nextEp = getNextEp();
                if(nextEp != null)
                {
                    AlertUtil.showShortToast("将为你自动播放下一集", tip_time);
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(tip_time);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    enter(nextEp);
                }
                else
                {
                    AlertUtil.showLongToast("已经没有更多字幕了!");
                }
            }
            else
            {
                stopSrtPlay();
                AlertUtil.showShortToast(ex.getMessage());
            }
        }
        catch (Exception ex)
        {
            stopSrtPlay();
            // startNew();
            Log.e("Panel", ex.getMessage());
        }
    }

    private String getNextEp()
    {
        String srtFile = srtPlayService.getCurFile();
        File folder = new File(BasicFileUtil.getFileParent(srtFile));
        List<File> sortedList = MyFileUtil.getSortFiles(folder.listFiles());

        List<File> validFiles = new ArrayList<File>();
        for (File f : sortedList)
        {
            if(SrtTextHelper.isSrtfile(f))
            {
                validFiles.add(f);
            }
        }
        for (int i = 0; i < validFiles.size(); i++)
        {
            String absolutePath = validFiles.get(i).getAbsolutePath();
            if(i < validFiles.size() - 1 && absolutePath.equals(srtFile))
            {
                String nextFile = validFiles.get(i + 1).getAbsolutePath();
                return nextFile;
            }
        }
        return null;
    }

    @Override
    public void stopSrtPlay()
    {
        startBt.setText(SRT_PLAY_TEXT);
        srtPlayService.stopSrt();
    }

    @Override
    public void play(SrtInfo srtInfo)
    {
        System.out.println("play: " + " " + srtInfo);
        genetateDict(srtInfo);
        setSrtContentAndPlay(srtInfo);
    }

    private void genetateDict(SrtInfo srtInfo)
    {
        Set<Topic> topics = DictionaryDao.getCETTopic(srtInfo.getEng());
        if(topics.size() > 0)
        {
            StringBuilder accum = new StringBuilder(128);
            accum.append("<html>");
            int lines = 0;
            for (Topic topic : topics)
            {
                accum.append(topic.getTopic_word());
                lines++;
                if(!topic.getTopic_base_word().equals(topic.getTopic_word()))
                {
                    accum.append("<font size=\"3\" color=\"red\">");
                    accum.append(" (原型:" + topic.getTopic_base_word() + ")");
                    accum.append("</font>");
                }
                accum.append("<br>");
                accum.append(topic.getMean_cn());
                double hzCount = 0;
                for (int i = 0; i < topic.getMean_cn().length(); i++)
                {
                    if(TextFormatUtil.isChineseChar(topic.getMean_cn()
                            .charAt(i)))
                    {
                        hzCount++;
                    }
                    else
                    {
                        hzCount += 0.5;
                    }
                }
                lines += Math.ceil(hzCount / 15);
                accum.append("<br>");
                accum.append("<font size=\"3\" color=\"red\">");
                accum.append(topic.getBookName());
                lines++;
                accum.append("</font>");
                accum.append("<br>");
                accum.append("<br>");
                lines++;
            }
            accum.append("</html>");
            setLabelAndAutoSize(accum.toString(), lines - 1);
        }
        else
        {
            setLabelAndAutoSize("", 0);
        }
        topics.clear();
        topics = null;
    }

    /**
     * 自动调节Label的高度,不让它隐藏了
     * 
     * @param accum
     * @param lines
     */
    private void setLabelAndAutoSize(String text, int lines)
    {
        this.dictLabel.setText(text);
        dictLabel.setPreferredSize(new Dimension(_SCROLLPANEL_WIDTH
                - _SCROLLBAR_WIDTH, _FONT_HEIGHT * lines));
    }

    private void setSrtContentAndPlay(SrtInfo srt)
    {
        checkFav(srt);
        setSrtContent(srt);
        beginSrtPlay();
    }

    private void checkFav(SrtInfo srt)
    {
        boolean exist = FavDao.isExistSingle(srt, srtPlayService.getCurFile()
                .replace(MyAppParams.SRT_FOLDER, ""));
        if(exist)
        {
            favoriteBt.setForeground(Color.RED);
        }
        else
        {
            favoriteBt.setForeground(Color.BLACK);
        }
    }

    int[] defaultTimePoint =
    { 0, 0, 0 };

    private void setSrtContent(SrtInfo srt)
    {
        if(TextFormatUtil.containsChinese(srt.getEng())
                && !TextFormatUtil.containsChinese(srt.getChs()))
        {
            jtaChs.setText(srt.getEng() == null ? "NULL" : srt.getEng());
            jtaEng.setText(srt.getChs() == null ? "NULL" : srt.getChs());
        }
        else
        {
            jtaChs.setText(srt.getChs() == null ? "NULL" : srt.getChs());
            jtaEng.setText(srt.getEng() == null ? "NULL" : srt.getEng());
        }
        if(srt.getFromTime() != null)
        {
            defaultTimePoint[0] = srt.getFromTime().getHour();
            defaultTimePoint[1] = srt.getFromTime().getMinute();
            defaultTimePoint[2] = srt.getFromTime().getSecond();
        }
        processLabel.setText(SrtTextHelper.concatTimeline(srt.getFromTime(),
                srt.getToTime()) + "  " + srtPlayService.getPleyProgress());
    }

    @Override
    public SrtPlayService getSrtPlayService()
    {
        return this.srtPlayService;
    }

    @Override
    public void playNext()
    {
        getSrtInfoAndPlay(SRT_VIEW_TYPE.VIEW_RIGHT);
    }

    @Override
    public void playCurrent()
    {
        getSrtInfoAndPlay(SRT_VIEW_TYPE.VIEW_CURRENT);
    }

    @Override
    public void callByKeyCode(int keycode)
    {
        switch (keycode)
        {
        case KeyEvent.VK_LEFT:
            doLeft();
            break;
        case KeyEvent.VK_RIGHT:
            doRight();
            break;
        case KeyEvent.VK_SPACE:
            clickPlayBtn();
            break;
        }

    }

    private void doLeft()
    {
        getSrtInfoAndPlay(SRT_VIEW_TYPE.VIEW_LEFT);
    }

    public void doRight()
    {
        getSrtInfoAndPlay(SRT_VIEW_TYPE.VIEW_RIGHT);
    }

}
