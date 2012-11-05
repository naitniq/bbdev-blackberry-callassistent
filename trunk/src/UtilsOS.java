package com.callasst;

import javax.microedition.pim.*;
import java.io.*;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.ui.component.*;
import net.rim.blackberry.api.pdap.*;
import net.rim.blackberry.api.phone.phonelogs.*;
import net.rim.device.api.crypto .*;
import com.callasst.*;
import com.callasst.res.*;
import net.rim.device.api.ui.UiApplication;
//import net.rim.blackberry.api.phone.phonegui.*;
import net.rim.device.api.applicationcontrol.*;

import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;


public final class UtilsOS {
        
    public static void addPhoneScreen(int callid,String location,Application app)
    {
        /*PhoneScreen ps = new PhoneScreen(callid, app);
        ps.add(new LabelField(location));
        ps.sendDataToScreen();*/
    }
    
    public static boolean psSupported()
    {  
        return false;
        //return PhoneScreen.isSupported();
    }
    
    public static String getSoftwareVersion()
    { 
           return "42";
        //return DeviceInfo.getSoftwareVersion();
    }
} 


/*
class HackScreen implements ScreenUiEngineAttachedListener
{
    private static HackScreen _instance = null;
    HackScreen(){}
    
    public static HackScreen getInstance()
    {
        if (_instance == null) {
            _instance = new HackScreen();
        }
        return _instance;
    }
    
    
    public void onScreenUiEngineAttached(Screen screen, boolean attached)
    {
                                Graphics g = screen.getGraphics();
                                XYRect xy = g.getClippingRect();
                                g.pushContext(xy,0,0);
                                g.clear();
                                g.setColor(0xffffff);
                                g.fillRect(xy.x, xy.y, xy.width, xy.height);
                                g.setColor(0x000000);
                                g.drawText("22222",20,20);
                                g.popContext();        
        
    }
}*/

