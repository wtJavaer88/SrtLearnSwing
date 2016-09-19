package com.wnc.srtlearn.ui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import srt.DataHolder;
import srt.SRT_VIEW_TYPE;
import srt.SrtInfo;
import srt.SrtPlayService;
import srt.SrtTextHelper;
import srt.TimeHelper;

import com.wnc.basic.BasicFileUtil;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.srtlearn.dao.FavDao;
import com.wnc.srtlearn.dao.Topic;
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

        enter("D:\\Users\\wnc\\oral\\字幕\\Friends.S01\\S01E01.ass");
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
        dictLabel = new JLabel("<html>hello <br> world!</html>");
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
        dictLabel.setBounds(480, 160, 300, 300);

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
                        "D:\\Users\\wnc\\oral\\字幕\\Transformers.Prime.S01\\");
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
                boolean favorite = srtPlayService.favorite();
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
                int time = 1 + (int) (TimeHelper.getTime(DataHolder
                        .getCurrent().getFromTime()) / 1000);
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
        srtPlayService.showNewSrtFile(srtFilePath);
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
        catch (Exception ex)
        {
            stopSrtPlay();
            AlertUtil.showShortToast(ex.getMessage());
        }
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
        Topic topic = DictionaryDao.getCETTopic(srtInfo.getEng());
        if(topic != null)
        {
            StringBuilder accum = new StringBuilder(128);
            accum.append("<html>");
            accum.append(topic.getTopic_word());
            accum.append("<br>");
            if(!topic.getTopic_base_word().equals(topic.getTopic_word()))
            {
                accum.append("<font color=\"red\">");
                accum.append("原型:" + topic.getTopic_base_word());
                accum.append("</font>");
                accum.append("<br>");
            }
            accum.append(topic.getMean_cn());
            accum.append("<br>");
            accum.append("<br>");
            accum.append(topic.getBookName());
            accum.append("</html>");
            this.dictLabel.setText(accum.toString());
        }
        else
        {
            this.dictLabel.setText("");
        }
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
        if(srt.getFromTime() != null && srt.getToTime() != null)
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
