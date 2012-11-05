package com.callasst.listener;

import java.util.*;
import javax.microedition.pim.*;
import net.rim.blackberry.api.phone.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.*;

import com.callasst.*;
import com.callasst.findlocation.*;
import com.callasst.res.*;
//import net.rim.blackberry.api.phone.phonegui.*;

public final class ConcretePhoneListener extends AbstractPhoneListener {
    
    private Hashtable _phoneNumberTable = new Hashtable();  // maps call IDs to their phone numbers
    //private Hashtable _phoneUiTable = new Hashtable();  // maps call IDs to their ui
    private Hashtable _locateTable = new Hashtable();  
    private Hashtable _phoneObjectTable = new Hashtable(); 
    private boolean bAnswer;
    private boolean bIncoming;
    private boolean bEndByUser;
    private static long lStartTime;
    private boolean bStart;
    
    private Application _app;
    private byte os_ver;
                              
    public ConcretePhoneListener(Application app)
    {
        _app = app;                     
    }
    
    private void invalidateScreen()
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                   UiApplication.getUiApplication().repaint();
            }
        });   
    }
    /*
    private void invalidateScreen()
    {
       // UiApplication.getUiApplication().pushGlobalScreen(new ShowDialog(), 0, UiEngine.GLOBAL_MODAL);  
        
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                   UiApplication.getUiApplication().repaint();
                    //Screen screen = UiApplication.getUiApplication().getActiveScreen();
                    
                    //UiApplication.getUiApplication().popScreen(screen);
                    //screen.doPaint();
                    //screen.getApplication().requestBackground();
                    //(bIncoming)
                    //
                    //UiApplication.getUiApplication().popScreen(screen);
                    //UiApplication.getUiApplication().pushModalScreen(screen);
                   //
                    //Screen screen = actscreen.getScreenAbove();
                    //screen.invalidate();
                    //screen.doPaint();
                    
                    /*Graphics g = screen.getGraphics();
                    XYRect xy = g.getClippingRect();
                    g.pushContext(xy,0,0);
                   
                    //g.pushRegion(xy);
                    g.setColor(0xFF8080);
                    g.fillRect(10, 10, 100, 100);
                    g.setColor(0x000000);
                    g.drawText("22222",20,20);
                    g.popContext();  
                    
                    if(screen.getUiEngine().isPaintingSuspended()){
                        screen.getUiEngine().suspendPainting(false);
                    }                
                    //screen.invalidateLayout();
                    //screen.invalidate();
                    //screen.getUiEngine().updateDisplay();
                    //screen.updateDisplay();
                    
                    //UiApplication.getUiApplication().pushGlobalScreen(screen,0,UiApplication.GLOBAL_MODAL);
                   
                    
                    //UiApplication.getUiApplication().repaint();
                    //UiApplication.getUiApplication().updateDisplay();
                    // Add new fields to the mainscreen already pushed.
                    //UiApplication.getUiApplication().repaint();
                    
                   //Screen scr = UiApplication.getUiApplication().getActiveScreen();
                   //scr.repaint();
            }
        },1,false);   
    }*/
    
    public void callIncoming(int callId) 
    {
        bIncoming = true;
        _callstart(callId);
         
        invalidateScreen();
    }
    
    //public void callConnected(int callId) 
    //{

    //}
    
    /*public void  callInitiated(int callid) 
    {
        Utils.OutputDebugString("callInitiated");
    }*/
    
    /*public void  callHeld(int callId)
    {
         Utils.OutputDebugString("callHeld");
    }
    
    public void  callWaiting(int callid) 
    {
        Utils.OutputDebugString("callWaiting");
    }*/
    
    public void callDisconnected(int callId) 
    {   
        Utils.OutputDebugString("callDisconnected");
        
        Database db = Utils.getRuntimeData();
        if(!bEndByUser && !bIncoming)
        {
            if(db != null)
            {
                if(db.bEndCall)
                {
                    if(Alert.isVibrateSupported())
                        Alert.startVibrate(600);  
                    else if(Alert.isAudioSupported())
                        Alert.startAudio(Const.ALERT_TUNE_DATA,80);  
                    else if(Alert.isBuzzerSupported())
                        Alert.startBuzzer(Const.ALERT_TUNE_DATA,80,true);        
                }
            }
        }
        
        _callend(callId);
        
        long spendTime = 0;
        if(lStartTime > 0)
        {
            if(db != null && db.bShowSpeakTime)
            {
                spendTime = (System.currentTimeMillis() - lStartTime)/1000;
                Utils.setTimeAndFee(spendTime,bIncoming);
                lStartTime = 0;
                Backlight.enable(true,Const.END_ALERT_TIME);
                //Dialog.alert(CAResources.getString(CallAssistantResource.CA_SPEND_TIME) + spendTime + " s");
                SpendDialog spScreen = new SpendDialog(CAResources.getString(CallAssistantResource.CA_SPEND_TIME) + spendTime + " s",db.iSpeakTimeColor);
                SpendTimeTask myTask = new SpendTimeTask(2,spScreen);
                new Timer().schedule(myTask,5000);       
                UiApplication.getUiApplication().pushGlobalScreen(spScreen,99,UiEngine.GLOBAL_MODAL);
            }
            //Timer myTimer = new Timer();
            //myTimer.schedule(myTask, 5000, 5000);
            
        }
        
        bEndByUser = false;
        bIncoming = false;
    }
    
    // User ended call
    public void callEndedByUser(int callId)
    {
        Utils.OutputDebugString("callEndedByUser");
        bEndByUser = true;
    }
    
    public void callAnswered(int callId) 
    {
       bAnswer = true;
    }
    
    public void callConnected(int callId)
    {       
        _callend(callId);  
        lStartTime = System.currentTimeMillis();
        
        if(!bAnswer)
        {
            Database db = Utils.getRuntimeData();
            Utils.OutputDebugString("Alert!");
            if(db != null)
            {
                if(db.bShark)
                {
                    if(Alert.isVibrateSupported())
                        Alert.startVibrate(db.iVTime);
                // else
                    //    Backlight.enable(true,2);
                }
            } 
        }
        else
            bAnswer = false;
    }
    
    /*public void callWaiting(int callId) 
    {
        _callstart(callId);   
    }*/
    
    public void callFailed(int callId,int reason)
    {
        _callend(callId);
    }
    
    public void callInitiated(int callId)
    {
       // Utils.OutputDebugString("callInitiated");
       bIncoming = false;
       _callstart(callId);
       invalidateScreen();
       //Dialog.alert("alert");
    }
    
    /*private void addcantact()
    {
        _locateTable.put(new Integer(callId),locate);
        Contact contact = Utils.appendContact(locate,phoneNumber);
        _phoneObjectTable.put(new Integer(callId),contact);   
    }*/
   
    private void _callstart(int callId)
    {
        bStart = true;
        PhoneCall phoneCall = Phone.getCall(callId);
        String phoneNumber = phoneCall.getDisplayPhoneNumber();
        //phoneNumber = phoneNumber.trim();
        _phoneNumberTable.put(new Integer(callId),phoneNumber);
        
        //String locate = "…œ∫£ –";
        Database data = Utils.getRuntimeData();
        String locate = FindCore.FindLocateFormNumber(phoneNumber,bIncoming,data);
        os_ver = data.os_ver;
        if(locate != null)
        {
            if(os_ver < 50 || !UtilsOS.psSupported()) {
                _locateTable.put(new Integer(callId),locate);
                Contact contact = Utils.appendContact(locate,phoneNumber);
                _phoneObjectTable.put(new Integer(callId),contact);
            }else{
                UtilsOS.addPhoneScreen(callId,locate,_app);
            } 
        }
        
        
                                /*Screen activescreen = UiApplication.getUiApplication().getActiveScreen();
                               
                                Graphics g = activescreen.getGraphics();
                                XYRect xy = g.getClippingRect();
                                g.pushContext(xy,0,0);
                                g.clear();
                                g.setColor(0xffffff);
                                g.fillRect(xy.x, xy.y, xy.width, xy.height);
                                g.setColor(0x000000);
                                g.drawText("22222",20,20);
                                g.popContext();        
                                
                                activescreen.addScreenUiEngineAttachedListener(HackScreen.getInstance());*/
        /*
        new Thread(new Runnable() {
            public void run() {
                            while(bStart)
                            {
                                Screen activescreen = UiApplication.getUiApplication().getActiveScreen();
                               
                                Graphics g = activescreen.getGraphics();
                                XYRect xy = g.getClippingRect();
                                g.pushContext(xy,0,0);
                                g.clear();
                                g.setColor(0xffffff);
                                g.fillRect(xy.x, xy.y, xy.width, xy.height);
                                g.setColor(0x000000);
                                g.drawText("22222",20,20);
                                g.popContext();
                                //activescreen.updateDisplay();
                                try{
                                        Thread.sleep(1000);
                                }
                                catch(InterruptedException e){
                                        System.out.println("Interrupted");
                                }
                            }
                        }
           }).start();
                               // g.popContext();
        
        //new Thread(new Runnable() {
         //public void run() {
                  /*  UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            while(bStart)
                            {
                                Screen activescreen = UiApplication.getUiApplication().getActiveScreen();
                               
                                Graphics g = activescreen.getGraphics();
                                XYRect xy = g.getClippingRect();
                                g.pushContext(xy,0,0);
                                g.clear();
                                g.setColor(0xffffff);
                                g.fillRect(xy.x, xy.y, xy.width, xy.height);
                                g.setColor(0x000000);
                                g.drawText("22222",20,20);
                                g.popContext();
                                try{
                                        Thread.sleep(500);
                                }
                                catch(InterruptedException e){
                                        System.out.println("Interrupted");
                                }
                            }
                        }
                    });*/
              //  }
        //}).start();
        
        //activescreen.updateDisplay();
        //invalidateScreen();
        //UiApplication.getUiApplication().popScreen();
        
        //UiApplication.getUiApplication().pushGlobalScreen(new ShowDialog(),0,UiEngine.GLOBAL_MODAL);      
        //UiApplication.getUiApplication().popScreen(activescreen);
        //Dialog.alert("boom~");
        
        
       /* new Thread(new Runnable() {

        public void run() {
                    UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                            Screen activescreen = UiApplication.getUiApplication().getActiveScreen();
                       
                            UiApplication.getUiApplication().popScreen(activescreen);
                            Dialog.alert("boom~");
                            //UiApplication.getUiApplication().pushScreen(statusscreen);
                            //UiApplication.getUiApplication().pushScreen(new DxInputUIScreen(activescreen,statusscreen));  
                            //statusscreen.hideInputUI();
                        }
                    });
               }
        }).start();*/
        
        //UiApplication.getUiApplication().pushGlobalScreen(new ShowDialog(), 0, UiEngine.GLOBAL_MODAL);   
    } 

    private void _callend(int callId)
    {
        //bStart = false;
        //Screen.removeScreenUiEngineAttachedListener(this);
        
        /*Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();
        if(activeScreen.getUiEngine().isPaintingSuspended()){
            activeScreen.getUiEngine().suspendPainting(false);
        }                       
        activeScreen.doPaint();*/

        
        String phoneNumber =  (String)_phoneNumberTable.get(new Integer(callId));
        Utils.OutputDebugString("phoneNumber:"+phoneNumber);
        if(phoneNumber != null)
            _phoneNumberTable.remove(new Integer(callId));
            
        if(os_ver < 50 || !UtilsOS.psSupported())    
        {
            String locate = (String)_locateTable.get(new Integer(callId));
            Utils.OutputDebugString("locate:"+locate);
            if(locate != null)
            {
                Utils.removeContact((Contact)_phoneObjectTable.get(new Integer(callId)));  
                _locateTable.remove(new Integer(callId));
                _phoneObjectTable.remove(new Integer(callId));    
                Utils.changePhoneLogs(locate);
            }
        }
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

