package com.wnc.srtlearn.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;

public class SrtFrame extends JFrame
{
    public static String PIC_CONVERT_EXE;

    public static String FFMPEG_EXE;
    public static String MEDIA_INFO_EXE;
    SrtLearnPanel sp;

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
        sp = new SrtLearnPanel(this);
        sp.setBounds(0, 0, 800, 600);
        this.add(sp);
        this.setFocusable(true);
        this.requestFocus();
        this.setLayout(null);
        this.setBounds(0, 0, 800, 600);

        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("美剧学习机By~龙年生");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭时退出进程
        this.setDropTarget(new DropTarget(this, DnDConstants.ACTION_REFERENCE,
                new DropTargetAdapter()
                {

                    @Override
                    public void drop(DropTargetDropEvent dtde)
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
                        Transferable tf = dtde.getTransferable();
                        try
                        {
                            List<File> list = (List<File>) tf
                                    .getTransferData(DataFlavor.javaFileListFlavor);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, true));
    }

}