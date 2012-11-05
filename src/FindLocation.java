
package com.callasst.findlocation;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.blackberry.api.homescreen.*;
import net.rim.device.api.notification.*;
import net.rim.blackberry.api.phone.Phone; 
import com.callasst.listener.*;
import com.callasst.ipcall.AddIPMenu;
import net.rim.device.api.synchronization.*;
import com.callasst.*;
import javax.microedition.pim.*;


public final class FindLocation extends UiApplication  {
        
        public static Database data;
        
        
        //FindLocation(boolean rollerIcon)
       // {
            //if(rollerIcon)
                //RollerApplicationIcon();
       // }
    
        public static void main(String[] args) 
        {
            if ( args.length == 1 && args[0].equals( "autostartup" ) ) {
                try {
                    data = new Database();
                    OptionsUI.libMain(data);
                    data.readPhoneData();
                    
                    String SoftVersion = UtilsOS.getSoftwareVersion();
                    if(SoftVersion.charAt(0) == '5'){
                        data.os_ver = 50;
                    }else{
                        data.os_ver = 40;
                    }
                    //data.os_ver = 40;
                    
                    //test contact
                    //if(data.bShow)
                    //{
                  // Contact c = Utils.appendContact("BBDEV","88889999");
                  //  Utils.removeContact(c);
                    //}
                    //String activecode = Utils.TestActiveCode();
                    //Utils.OutputDebugString(activecode);
                    //data.readMobileData();
                   /*try{
                        System.gc();
                        data.readMobileData();
                        System.gc();
                    }
                    catch(OutOfMemoryError ex){
                         data.bError = true;
                         Utils.OutputDebugString(ex.toString());
                    }*/
                    RuntimeStore store = RuntimeStore.getRuntimeStore();
                    try {
                        store.put( Const.LOCATE_DATA_ID, FindLocation.data );
                    }
                    catch(IllegalArgumentException e) {
                        Utils.OutputDebugString(e.toString());
                    }
                    
                    Phone.addPhoneListener( new ConcretePhoneListener(new FindLocation()) );
                    
                    AddIPMenu.addMenu();
                    AddSearchMenu.addMenu();
                    
                    SyncManager.getInstance().enableSynchronization(SyncData.getInstance());
                    //new FindLocation(true).enterEventDispatcher();
                    
                    System.exit(0);
                    //Util.OutputDebugString("addPhoneListener");
                }catch ( ControlledAccessException e ) {
                    //Dialog.alert( "Access to Phone API restricted by system administrator." );
                    Utils.OutputDebugString("Access to Phone API restricted by system administrator.");
                    System.exit( 1 );
                }
            } else if( args.length == 1 && args[0].equals( "gui" ) ){
           //     new FindLocation(false).enterEventDispatcher();
            }
        }
        /*
        public void RollerApplicationIcon()
        {
                //Setup the rollover icons.
                final Bitmap regIcon = Bitmap.getBitmapResource("1.png");
                final Bitmap icon = Bitmap.getBitmapResource("2.png");
        
                invokeLater(new Runnable()
                {
                    public void run()
                    {
                        ApplicationManager theApp =ApplicationManager.getApplicationManager();
                        boolean isStartup = true;
                        //因为程序启动需要时间，所以只能等启动完毕后才能修改。
                        while (isStartup)
                        {
                            //Check if the BlackBerry has completed its startup process.
                            if (theApp.inStartup())
                            {
                                //The BlackBerry is still starting up, sleep for 1 second.
                                try
                                {
                                    Thread.sleep(1000);
                                }
                                catch (Exception ex)
                                {
                                //Couldn't sleep, handle exception.
                                }
                            }
                            else
                            {
                                //The BlackBerry has finished its  startup process.
                                //Set the rollover icons.
                                HomeScreen.updateIcon(regIcon, 0);
                                HomeScreen.setRolloverIcon(icon, 0);
                                isStartup = false;
                            }
                        }
                    }
                });
        }*/

} 

