package com.wnc.srtlearn.ui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import srt.DataHolder;
import srt.Log;
import srt.SRT_VIEW_TYPE;
import srt.SrtInfo;
import srt.SrtPlayService;
import srt.SrtTextHelper;
import srt.TimeHelper;
import translate.site.iciba.CibaWordTranslate;

import com.wnc.basic.BasicFileUtil;
import com.wnc.run.RunCmd;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.srtlearn.dao.FavDao;
import com.wnc.srtlearn.ex.ReachFileTailException;
import com.wnc.srtlearn.ex.SrtException;
import com.wnc.srtlearn.modules.translate.Topic;
import com.wnc.srtlearn.setting.SrtSetting;
import common.swing.AlertUtil;
import common.swing.INewFrame;
import common.swing.ImplAWTEventListener;
import common.swing.KeyCallBack;
import common.swing.SnapUtil;
import common.uihelper.MyAppParams;
import common.utils.TextFormatUtil;

public class SnapPanel extends JPanel implements INewFrame, srt.IBaseLearn,
        KeyCallBack
{
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

    public JTextField jtfSrtFile;
    public JTextArea jtaEng;
    public JTextArea jtaChs;

    private final String SRT_PLAY_TEXT = "播放";
    private final String SRT_STOP_TEXT = "停止";

    public SnapPanel(SrtFrame mf)
    {
        m = mf;
        init();
        setBounds();
        addComp();
        listen();
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.addAWTEventListener(new ImplAWTEventListener(this),
                AWTEvent.KEY_EVENT_MASK);

        enter("D:\\Users\\wnc\\oral\\字幕\\Friends.S01\\S01E24.ass");
        System.out.println(getNextEp());
    }

    @Override
    public void init()
    {
        this.setLayout(null);

        Font font = new Font("微软雅黑", Font.BOLD, 20);

        chooseBt = new JButton("选择字幕");// 选择
        startBt = new JButton("Start!");// 开始
        nextBt = new JButton("Next");// 下一个
        preBt = new JButton("Pre");// 上一个
        favoriteBt = new JButton("喜欢");
        snapBt = new JButton("截图");
        processLabel = new JLabel();// 进度标签
        dictLabel = new JLabel("");
        dictLabel.setFont(font);

        jtfSrtFile = new JTextField(200);
        jtfSrtFile.setToolTipText("请选择一集字幕，或者手动拖拽！");
        jtaEng = getTextArea(font);
        jtaChs = getTextArea(font);
        // 创建复选框按键，并设置快捷键，和选定
        unComplete = new JCheckBox("unComplete");
        unComplete.setMnemonic(KeyEvent.VK_C);
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
        dictLabel.setBounds(480, 160, 300, 400);

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
        this.add(dictLabel);

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

    private void enter(String srtFilePath)
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
        String srtFile = this.jtfSrtFile.getText();
        File folder = new File(BasicFileUtil.getFileParent(srtFile));
        folder.listFiles();
        List<File> asList = Arrays.asList(folder.listFiles());
        Collections.sort(asList);
        for (int i = 0; i < asList.size(); i++)
        {
            String absolutePath = asList.get(i).getAbsolutePath();
            if(absolutePath.equals(srtFile))
            {
                if(i < asList.size() - 1)
                {
                    return asList.get(i + 1).getAbsolutePath();
                }
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
            for (Topic topic : topics)
            {
                accum.append(topic.getTopic_word());
                if(!topic.getTopic_base_word().equals(topic.getTopic_word()))
                {
                    accum.append("<font size=\"3\" color=\"red\">");
                    accum.append(" (原型:" + topic.getTopic_base_word() + ")");
                    accum.append("</font>");
                }
                accum.append("<br>");
                accum.append(topic.getMean_cn());
                accum.append("<br>");
                accum.append("<font size=\"3\" color=\"red\">");
                accum.append(topic.getBookName());
                accum.append("</font>");
                accum.append("<br>");
                accum.append("<br>");
            }
            accum.append("</html>");
            this.dictLabel.setText(accum.toString());
        }
        else
        {
            this.dictLabel.setText("");
        }
        topics.clear();
        topics = null;
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
