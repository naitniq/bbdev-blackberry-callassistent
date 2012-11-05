package com.callasst.ipcall;

import net.rim.device.api.ui.component.Dialog.*;
import net.rim.blackberry.api.menuitem.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.notification.*;
import net.rim.blackberry.api.invoke.*;
import net.rim.blackberry.api.phone.phonelogs.*;
import javax.wireless.messaging.*;
import javax.microedition.pim.*;
import com.callasst.res.*;
import com.callasst.*;

public final class AddIPMenu extends ApplicationMenuItem{
    
    //const char keyDef[] = "22233344455566677778889999";
    
    //static PlumCore plumCore;
    
    public static void addMenu()
    {
          //plumCore = new PlumCore();
          //plumCore.PlInit();
          //plumCore.PlSetStat(0, 0, false, false, false); 
                     
            AddIPMenu asm = new AddIPMenu();
            ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SYSTEM, asm);   

            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_PHONE, asm); 
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MESSAGE_LIST,asm);  
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SMS_EDIT, asm);    
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_SMS_VIEW, asm);  
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSBOOK_LIST, asm); 
            amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSCARD_VIEW, asm); 
                          
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSCARD_EDIT, asm);      
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_TASK_EDIT, asm);     
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MEMO_EDIT, asm);      
          //amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_GROUPADDRESS_EDIT, asm); //4.2.1
    }
    
    public AddIPMenu() 
    {
        super(20);
    }

    public String toString()
    {
        return CAResources.getString(CallAssistantResource.CA_MENU_IPCALL);
    }

    public Object run(Object context)
    {
        OptionsData data = OptionsData.load();
        String ipcall = data.getIPCall();
        String ipcall2 = data.getIPCall2();
        if(ipcall.length() == 0 && ipcall2.length() == 0)
        {
            Dialog.alert(CAResources.getString(CallAssistantResource.CA_ALERT_NOIPCALL));
            return null;
        }

        
        //Utils.OutputDebugString("RUN OBJECT:" + context.getClass().getName());
        
        Screen activescreen = UiApplication.getUiApplication().getActiveScreen();
        Field focusfield = activescreen.getLeafFieldWithFocus();
        if(focusfield == null)
            return null;
        String fieldname = focusfield.getClass().getName();      
        String _value = "";    
        try{
            if(context == null) //phonenumberinput
            {
                if(fieldname != null)
                {
                    if(fieldname.indexOf("PhoneNumber") > 0 && focusfield instanceof TextField)
                    {
                        TextField editfield = (TextField)focusfield;
                        _value = editfield.getText();
                    }
                }
            }
            else
            {
                String contextname = context.getClass().getName();
                
                if(contextname!=null && fieldname!=null)
                {
                    if(context instanceof String)
                    {
                        _value = (String)context;
                    }
                    else if(context instanceof TextMessage)
                    {
                        TextMessage textmsg = (TextMessage)context;
                        _value = textmsg.getAddress();
                        
                        if(_value.length() == 0)
                        //if(contextname.indexOf("TextMessage") > 0)
                        {
                            if(fieldname.indexOf("SmartPhoneNumberLabelField") > 0)
                            {
                                LabelField lblfield = (LabelField)focusfield;
                                _value = lblfield.getText();
                            }
                        }
                    }
                    else if(contextname.indexOf("PhoneCallLog") > 0)
                    {
                        if(fieldname.indexOf("PhoneListFieldManager") > 0
                            || fieldname.indexOf("CallLogView") > 0)
                        {
                            if(context instanceof PhoneCallLog)
                            {
                                PhoneCallLog phonelog =  (PhoneCallLog)context;
                                PhoneCallLogID callid = phonelog.getParticipant();
        
                                _value = callid.getNumber();
                            }
                        }
                    }
                    else if(context instanceof Contact)
                    {
                        Contact contact = (Contact)context;
                        int num = contact.countValues(Contact.TEL);
                        
                        if(num == 1)
                            _value = contact.getString(Contact.TEL,0);
                        else if(num > 1)
                        {
                            Object[] choices = new Object[num];

                            for(int i=0;i<num;i++)
                                   choices[i] = contact.getString(Contact.TEL,i);
                                   
                             Dialog dlg = new Dialog("",choices, null,0,null);  
                             int selindex = dlg.doModal();
                            
                             _value = (String)choices[selindex];                   
                        }
                        
                        /*if(fieldname.indexOf("RichTextField") > 0)
                        {
                             TextField editfield = (TextField)focusfield;
                            _value = editfield.getText();
                        }
                        else if(fieldname.indexOf("AddressBookListField") > 0)
                        {
                            ListField listfield = (ListField)focusfield;
                            int index = listfield.getSelectedIndex();
                            ListFieldCallback callBack = listfield.getCallback();
                            _value = (String)callBack.get(listfield,index);
                        }*/
                    }            
                }
            }
        }
        catch(ClassCastException ex){
            Utils.OutputDebugString( ex.toString() );
            return null;
        }     
        
        _value = _value.trim();
        if(_value.startsWith("+86"))
        {
           _value = _value.substring(3);
        }
        if(_value.startsWith("+086"))
        {
           _value = _value.substring(4);
        }
        if(_value.charAt(0) == '+')
        {
           _value = _value.substring(1);
        }
        
        if(_value!=null && _value.length() > 3 && Utils.isPhoneNumber(_value))
        {
            if(ipcall.length() == 0)
                _value = ipcall2 + _value;
            else if(ipcall2.length() == 0)
                _value = ipcall + _value;
            else
            {
                Object[] choices = new Object[2];
                choices[0] = ipcall;
                choices[1] = ipcall2;

                Dialog dlg = new Dialog("",choices, null,0,null);  
                int selindex = dlg.doModal();
                
                _value = (String)choices[selindex] + _value; 
            }
            Invoke.invokeApplication(Invoke.APP_TYPE_PHONE,
                    new PhoneArguments
                    (PhoneArguments.ARG_CALL, _value));
        }
        
        return null;
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


