package com.wnc.srtlearn.ui;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

import javax.swing.JFrame;

public class SrtFrame extends JFrame
{
    public static String PIC_CONVERT_EXE;

    public static String FFMPEG_EXE;
    public static String MEDIA_INFO_EXE;
    SnapPanel sp;

    public SrtFrame()
    {
        init();
    }

    public static void main(String[] args)
    {
        new SrtFrame();
    }

    public void init()
    {
        sp = new SnapPanel(this);
        sp.setBounds(0, 0, 800, 600);
        this.add(sp);
        this.setFocusable(true);
        this.requestFocus();
        this.setLayout(null);
        this.setBounds(0, 0, 800, 600);

        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("电影预览生成器By~龙年生");
        this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_REFERENCE,
                new MyDropImgListener(this), true));
    }

}