/*
 * SpendTimeTask.java
 *
 * bbdev, 2003-2009
 * Confidential and proprietary.
 */

package com.callasst;

import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import java.util.*;

public class SpendTimeTask extends TimerTask{
    
    private int iCounter;
    private Screen spDialog;
    public SpendTimeTask(int aStart,Screen screen) {
        super();
        iCounter = aStart;
        spDialog = screen;
    }
 
    public void run() {
        iCounter--;
        //System.out.println("Counter is now " + iCounter);
        if (iCounter > 0)
        {
            if(spDialog!=null)
            {
                try{
                    UiApplication.getUiApplication().invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            if(spDialog.isDisplayed())
                                spDialog.close();
                            else
                                spDialog = null;
                        }
                    });         
                    cancel();
                }catch(Exception ex){
                    Utils.OutputDebugString(ex.toString());
                }
            }
            /*Screen actScreen = UiApplication.getUiApplication().getActiveScreen();
            if(actScreen instanceof SpendDialog)
            {

            }*/
        }
        else
            cancel();
    }

} 
