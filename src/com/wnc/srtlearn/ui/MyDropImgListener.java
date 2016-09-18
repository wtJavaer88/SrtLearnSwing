package com.wnc.srtlearn.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyDropImgListener extends DropTargetAdapter
{
    private SrtFrame sf;

    public MyDropImgListener(SrtFrame sf)
    {
        this.sf = sf;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void drop(DropTargetDropEvent dtde)
    {
        System.out
                .println("drop________________________________________________________________________________---");
        dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
        Transferable tf = dtde.getTransferable();
        try
        {
            List<File> list = (List<File>) tf
                    .getTransferData(DataFlavor.javaFileListFlavor);
        }
        catch (UnsupportedFlavorException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
