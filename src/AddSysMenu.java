/*
 * AddSysMenu.java
 * 
 * DianXun, 2003-2008
 * Confidential and proprietary.
 */

package com.callasst;

import net.rim.device.api.ui.component.Dialog.*;
import net.rim.blackberry.api.menuitem.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.notification.*;
import com.callasst.res.*;

public final class AddSysMenu extends ApplicationMenuItem{
    
    //const char keyDef[] = "22233344455566677778889999";
    
    //static PlumCore plumCore;
    
    static void AddMenu()
    {
          //plumCore = new PlumCore();
          //plumCore.PlInit();
          //plumCore.PlSetStat(0, 0, false, false, false); 
                     
          AddSysMenu asm = new AddSysMenu();
          ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, asm);   

          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SMS_EDIT, asm);   //4.2.1
          amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_PHONELOG_VIEW, asm); 
          mir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSBOOK_LIST, asm); 
                   
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSCARD_EDIT, asm);      
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_TASK_EDIT, asm);     
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MEMO_EDIT, asm);      
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_GROUPADDRESS_EDIT, asm); //4.2.1
    }
    
    public AddSysMenu() 
    {
        super(20);
    }

    public String toString()
    {
        return CAResources.getString(CallAssistantResource.CA_FROM);
    }

    public Object run(Object context)
    {
        
       //BlackBerryContact c = (BlackBerryContact)context; //an error if this doesn鈥檛 work
       //System.out.println("#####RUN OBJECT:"+ context.getClass().getName() );
       
        //DxTools.OutputDebugString("RUN OBJECT:" + context.getClass().getName());
        
        //if(context == null)
        //{
        //    DxTools.OutputDebugString("ERROR : context is null!");
        //}
       // else 
       // {
            /*if(context.getClass().getName().equals(DxConst.MESSAGE_OBJ_NAME))
            {
                DxTools.OutputDebugString("It's Message");
               // _msg  = (net.rim.blackberry.api.mail.Message)context;
            }
            else if(context.getClass().getName().equals(DxConst.TEXTMESSAGE_OBJ_NAME))
            {
                DxTools.OutputDebugString("It's Text Message");

                //TextMessageImpl txl  = (TextMessageImpl)context;
                //_textmsg=  (javax.wireless.messaging.TextMessage)context;
        
                //_textmsg=  
               // DxTools.OutputDebugString(_textmsg.getPayloadText());
               // _textmsg.setPayloadText("Text Message");
                
                //_textmsg.setAddress(null);
            }
            else if(context.getClass().getName().equals(DxConst.CONTACT_OBJ_NAME))
            {
                DxTools.OutputDebugString("It's Contact"); 
            }*/
        //}
        
       //Message c = (Message)context.getClass();
      // Message
       
   /*    if ( c != null ) {
           new ContactsDemo().enterEventDispatcher();
           }
           else {
                throw new IllegalStateException( "Context is null, expected a Contact instance");
           }*/
       //net.rim.device.api.ui.component.Dialog.alert("DXINPUT!"); 
         
        
        //uiscreen.enterEventDispatcher();


       } 
} 


