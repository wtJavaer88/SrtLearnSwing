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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import srt.SRT_VIEW_TYPE;
import srt.SrtInfo;
import srt.SrtPlayService;

import com.wnc.srtlearn.dao.FavDao;
import common.swing.AlertUtil;
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
    JCheckBox unComplete;
    public JTextField jtfSrtFile;
    public JTextArea jtfEng;
    public JTextArea jtfChs;

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

        enter("D:\\Users\\wnc\\oral\\字幕\\Gravity.Falls.S02\\S02E01.ass");
    }

    @Override
    public void init()
    {
        this.setLayout(null);
        chooseBt = new JButton("选择字幕");// 选择
        startBt = new JButton("Start!");// 开始
        nextBt = new JButton("Next");// 下一个
        preBt = new JButton("Pre");// 下一个
        jtfSrtFile = new JTextField(200);
        jtfEng = new JTextArea("", 20, 60);
        jtfChs = new JTextArea("", 20, 60);
        jtfEng.setSelectedTextColor(Color.RED);
        jtfEng.setLineWrap(true); // 激活自动换行功能
        jtfEng.setWrapStyleWord(true); // 激活断行不断字功能
        jtfChs.setSelectedTextColor(Color.RED);
        jtfChs.setLineWrap(true); // 激活自动换行功能
        jtfChs.setWrapStyleWord(true); // 激活断行不断字功能

        Font font = new Font("宋体", Font.PLAIN, 18);
        jtfEng.setFont(font);
        jtfChs.setFont(font);

        // 创建复选框按键，并设置快捷键，和选定
        unComplete = new JCheckBox("unComplete");
        unComplete.setMnemonic(KeyEvent.VK_C);
        jtfSrtFile.setToolTipText("请选择一集字幕，或者手动拖拽！");
    }

    @Override
    public void setBounds()
    {
        chooseBt.setBounds(50, 50, 100, 30);
        unComplete.setBounds(170, 50, 120, 30);
        startBt.setBounds(50, 150, 100, 30);
        preBt.setBounds(150, 150, 100, 30);
        nextBt.setBounds(250, 150, 100, 30);
        jtfSrtFile.setBounds(50, 100, 300, 30);
        jtfEng.setBounds(50, 200, 300, 60);
        jtfChs.setBounds(50, 260, 300, 60);
    }

    @Override
    public void addComp()
    {
        this.add(unComplete);
        this.add(chooseBt);
        this.add(startBt);
        this.add(nextBt);
        this.add(preBt);
        this.add(jtfSrtFile);
        this.add(jtfEng);
        this.add(jtfChs);
        // this.add(tareaTest);
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
                        "D:\\Users\\wnc\\oral\\字幕\\");
                fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Srt Files", "srt", "ass", "cnpy", "lrc", "saa");
                fChooser.setFileFilter(filter);
                fChooser.setMultiSelectionEnabled(false);

                fChooser.showOpenDialog(null);
                File tmpFile = fChooser.getSelectedFile();

                jtfSrtFile.setText(tmpFile.getAbsolutePath());
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
        // Picker picker = PickerFactory.getPicker(srtFilePath);
        // int srtLineCounts = picker.getSrtLineCounts();
        // System.out.println(srtLineCounts);
        // System.out.println(picker.getSrtInfos(0, srtLineCounts));
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
        System.out.println("Srt停止播放...");
        startBt.setText(SRT_PLAY_TEXT);
        srtPlayService.stopSrt();
    }

    @Override
    public void play(SrtInfo srtInfo)
    {
        System.out.println("play: " + " " + srtInfo);
        setSrtContentAndPlay(srtInfo);
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
        }
        else
        {
        }
    }

    int[] defaultTimePoint =
    { 0, 0, 0 };

    private void setSrtContent(SrtInfo srt)
    {
        if(TextFormatUtil.containsChinese(srt.getEng())
                && !TextFormatUtil.containsChinese(srt.getChs()))
        {
            jtfChs.setText(srt.getEng() == null ? "NULL" : srt.getEng());
            jtfEng.setText(srt.getChs() == null ? "NULL" : srt.getChs());
        }
        else
        {
            // System.out.println("setContent:" + srt);
            jtfChs.setText(srt.getChs() == null ? "NULL" : srt.getChs());
            jtfEng.setText(srt.getEng() == null ? "NULL" : srt.getEng());
        }
        if(srt.getFromTime() != null && srt.getToTime() != null)
        {
            // timelineTv.setText(SrtTextHelper.concatTimeline(srt.getFromTime(),
            // srt.getToTime()));

            defaultTimePoint[0] = srt.getFromTime().getHour();
            defaultTimePoint[1] = srt.getFromTime().getMinute();
            defaultTimePoint[2] = srt.getFromTime().getSecond();
        }
        System.out.println("进度:" + srtPlayService.getPleyProgress());
        // ((TextView) findViewById(R.id.progress_tv)).setText(srtPlayService
        // .getPleyProgress());
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
