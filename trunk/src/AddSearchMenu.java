package com.callasst;

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


public final class AddSearchMenu extends ApplicationMenuItem {
    
    //const char keyDef[] = "22233344455566677778889999";
    //static PlumCore plumCore;
    
    public static void addMenu()
    {
          //plumCore = new PlumCore();
          //plumCore.PlInit();
          //plumCore.PlSetStat(0, 0, false, false, false); 
                     
          AddSearchMenu asm = new AddSearchMenu();
          ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance(); 

          amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_PHONE, asm); 
          amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MESSAGE_LIST,asm);  
          amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_ADDRESSBOOK_LIST, asm);                
    }
    
    public AddSearchMenu() 
    {
        super(20);
    }

    public String toString()
    {
        return CAResources.getString(CallAssistantResource.CA_MENU_SEARCH);
    }

    public Object run(Object context)
    {
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
        
        if(_value!=null && _value.length() > 6 && Utils.isPhoneNumber(_value.trim()))
        {
            //todo
            Database data = Utils.getRuntimeData();
            final String location =  FindCore.FindLocateFormNumber(_value,data);
            Utils.OutputDebugString("Location:" + location);
            if(location != null)
            {
                UiApplication.getUiApplication().invokeLater(new Runnable()
                {
                    public void run()
                    {
                        Dialog.alert(CAResources.getString(CallAssistantResource.CA_SEARCH_RESULT) + location);
                    }
                });  
            }
 
        }
        else
        {
            UiApplication.getUiApplication().invokeLater(new Runnable()
            {
                public void run()
                {
                    Dialog.alert(CAResources.getString(CallAssistantResource.CA_ALERT_NOTFIND));
                }
            });  
                
        }
        
        return null;
    } 
} 


