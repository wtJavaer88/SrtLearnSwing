package com.wnc.srtlearn.ui;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

public class ImplAWTEventListener implements AWTEventListener
{
    KeyCallBack callBack;

    public ImplAWTEventListener(KeyCallBack callBack)
    {
        this.callBack = callBack;
    }

    @Override
    public void eventDispatched(AWTEvent event)
    {
        if(event.getClass() == KeyEvent.class)
        {
            // 被处理的事件是键盘事件.
            KeyEvent keyEvent = (KeyEvent) event;
            if(keyEvent.getID() == KeyEvent.KEY_PRESSED)
            {
                // 按下时你要做的事情
                keyPressed(keyEvent);
            }
            else if(keyEvent.getID() == KeyEvent.KEY_RELEASED)
            {
                // 放开时你要做的事情
                keyReleased(keyEvent);
            }
        }
    }

    private void keyPressed(KeyEvent e)
    {
        callBack.callByKeyCode(e.getKeyCode());
    }

    private void keyReleased(KeyEvent event)
    {
    }
}
