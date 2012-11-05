/*
 * OptionsUI.java
 */

package com.callasst;

import net.rim.blackberry.api.options.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.ui.*;

import com.callasst.Database;
import com.callasst.res.*;

public final class OptionsUI implements OptionsProvider {

    // members
    public final static int _INSHOW         = 0;
    public final static int _OUTSHOW        = 1;
    public final static int _INSHARK        = 2;
    public final static int _ENDCALL        = 3;
    public final static int _OPTMAX         = 4;
    
    private ObjectChoiceField _ocf[] = new ObjectChoiceField[_OPTMAX];
    private EditField _ef;
    private LabelField _lfpin;
    private LabelField _lftotalfee;
    private LabelField _lftotaltime;
    private EditField _efactive;
    private EditField _efeachfee;
    private EditField _efeachoutfee;
    private EditField _efipcall;
    private EditField _efipcall2;
    private ButtonField _btnRefresh;
    
    private ColorChoiceField _colshowtime = null;
    private ObjectChoiceField _ocfspendtime = null;
     
    private OptionsData _data;
    private String _title;
    private static OptionsUI _instance;
    private static Font _font = net.rim.device.api.ui.Font.getDefault();
    
    private final static int SCREEN_WIDTH = Display.getWidth();
    private final static int SCREEN_HEIGHT = Display.getHeight();
    
    private Font tipfont;
    
    // constructors
    private OptionsUI() {

    }
    
    private OptionsUI(String title) 
    {
       _title = title;
       _data = OptionsData.load();
    }

    // Only allow one instance of this class.
    public static OptionsUI getInstance() 
    {
        if (_instance == null) 
        {
           _instance = new OptionsUI(CAResources.getString(CallAssistantResource.CA_TITLE));
        }
        return _instance;
    }

    // On startup, create the instance and register it.
    public static void libMain(Database db) 
    {
       OptionsManager.registerOptionsProvider(getInstance());
       _instance.setGlobalData(db);        
    }
    
    public void setGlobalData(Database db)
    {
       db.bActive = Utils.isActive(_data.getActiveCode());
       
       if(db.bActive == false || (DeviceInfo.isSimulator() && !Const.DEBUG))
       {
           db.bShow = false;
           db.bOutShow = false;
           db.bShark = false;
           db.bEndCall = false;
           db.bShowSpeakTime = false;
       } 
       else
       {
            if(_data.getSelected(_INSHOW) > 0)   
                    db.bShow = false;
            else
                    db.bShow = true;
            if(_data.getSelected(_OUTSHOW) > 0)   
                    db.bOutShow = false;
            else
                    db.bOutShow = true;  
            if(_data.getSelected(_INSHARK) > 0)   
                    db.bShark = false;
            else
                    db.bShark = true;  
                     
            if(_data.getSelected(_ENDCALL) > 0)   
                    db.bEndCall = false;
            else
                    db.bEndCall = true;  
                                           
                    
            db.bShowSpeakTime = _data.isShowSpendTime(); 
        }
       //Utils.OutputDebugString("setGlobalData:" + _data.getTime());    
       db.iVTime = _data.getTime();    
       db.iSpeakTimeColor = _data.getSpendTimeColor(); 
    }
    
    // Get the title for the option item.
    public String getTitle()
    {
       return _title;
    }

    FieldChangeListener listener = new FieldChangeListener() {
         public void fieldChanged(Field field, int context) {
            if(field == _btnRefresh)
            {
                Database data = Utils.getRuntimeData();
                data.refreshPhoneData();
                Dialog.alert(CAResources.getString(CallAssistantResource.CA_REFRESH_SUC));
                /*RuntimeStore store = RuntimeStore.getRuntimeStore();
                try {
                    store.put( Const.LOCATE_DATA_ID, FindLocation.data );
                }catch(IllegalArgumentException e) {
                    Utils.OutputDebugString(e.toString());
                }*/
            } 
        }
    };
    
    // Add fields to the screen.
    public void populateMainScreen( MainScreen screen ) 
    {   
       if(DeviceInfo.isSimulator()  && !Const.DEBUG) 
       {
            LabelField lfalert = new LabelField(CAResources.getString(CallAssistantResource.CA_NOT_EMU));
            screen.add(lfalert);   
       }
       else
       {
            String actstr = CAResources.getString(CallAssistantResource.CA_NOT_ACTIVE);
            if(Utils.isActive(_data.getActiveCode()))
                    actstr = CAResources.getString(CallAssistantResource.CA_ACTIVE);
                    
            LabelField lfact = new LabelField(actstr);
            screen.add(lfact);    
                
            _lfpin = new LabelField(CAResources.getString(CallAssistantResource.CA_PINCODE) + DeviceInfo.getDeviceId());
            screen.add(_lfpin);
            _efactive = new EditField(CAResources.getString(CallAssistantResource.CA_ACTIVECODE),_data.getActiveCode(),8,EditField.EDITABLE);
            screen.add(_efactive);
        
            //screen.add(new LabelField("    "));
            screen.add(new SeparatorField()); 
        
            //String[] choices = new String[2];
            //choices[0] = CAResources.getString(CallAssistantResource.CA_OPEN);
            //choices[1] = CAResources.getString(CallAssistantResource.CA_CLOSE);
            String[] choices = {CAResources.getString(CallAssistantResource.CA_OPEN), 
                            CAResources.getString(CallAssistantResource.CA_CLOSE)};
            
            int index = _data.getSelected(_INSHOW);
            _ocf[_INSHOW] = new ObjectChoiceField(CAResources.getString(CallAssistantResource.CA_INCOMING_SHOW), choices, index);
            screen.add(_ocf[_INSHOW]);
            
            index = _data.getSelected(_OUTSHOW);
            _ocf[_OUTSHOW] = new ObjectChoiceField(CAResources.getString(CallAssistantResource.CA_OUTCOMING_SHOW), choices, index);
            screen.add(_ocf[_OUTSHOW]);
            
            index = _data.getSelected(_INSHARK);
            _ocf[_INSHARK] = new ObjectChoiceField(CAResources.getString(CallAssistantResource.CA_OUTCOMING_SHARK), choices, index);
            screen.add(_ocf[_INSHARK]);
            
            index = _data.getSelected(_ENDCALL);
            _ocf[_ENDCALL] = new ObjectChoiceField(CAResources.getString(CallAssistantResource.CA_DISCONNECT_ALERT), choices, index);
            screen.add(_ocf[_ENDCALL]);
            
            _ef = new EditField(CAResources.getString(CallAssistantResource.CA_SHARK_TIME),Integer.toString(_data.getTime()),5, BasicEditField.FILTER_INTEGER);
            screen.add(_ef);
            
            screen.add(new SeparatorField()); 
            
            _ocfspendtime = new ObjectChoiceField(CAResources.getString(CallAssistantResource.CA_IS_SHOW_TIME), choices, _data.isShowSpendTime()?0:1);
            _colshowtime = new ColorChoiceField(CAResources.getString(CallAssistantResource.CA_SHOW_TIME_COLOR),
                                             _data.getSpendTimeColor());
            screen.add(_ocfspendtime);
            screen.add(_colshowtime);
            
            screen.add(new SeparatorField()); 
            int[] eachfee = _data.getEachFee();
            _efeachfee = new EditField(CAResources.getString(CallAssistantResource.CA_EACH_FEE),Integer.toString(eachfee[0]),6, BasicEditField.FILTER_INTEGER);
            screen.add(_efeachfee);    
            _efeachoutfee = new EditField(CAResources.getString(CallAssistantResource.CA_EACH_OUT_FEE),Integer.toString(eachfee[1]),6, BasicEditField.FILTER_INTEGER);
            screen.add(_efeachoutfee);    
            _lftotaltime = new LabelField(CAResources.getString(CallAssistantResource.CA_TOTAL_TIME) + Long.toString(_data.getTotalTime()) + "s");
            screen.add(_lftotaltime);   
            _lftotalfee = new LabelField(CAResources.getString(CallAssistantResource.CA_TOTAL_FEE) + Float.toString((float)_data.getTotalFee()/100));
            screen.add(_lftotalfee);      
   
            //screen.add(new LabelField("    "));
            screen.add(new SeparatorField()); 

            _efipcall = new EditField(CAResources.getString(CallAssistantResource.CA_IPCALL_NUMBER),_data.getIPCall(),12, BasicEditField.FILTER_DEFAULT);
            _efipcall2 = new EditField(CAResources.getString(CallAssistantResource.CA_IPCALL_NUMBER2),_data.getIPCall2(),12, BasicEditField.FILTER_DEFAULT);
            screen.add(_efipcall);     
            screen.add(_efipcall2);  
            
            screen.add(new SeparatorField()); 
            
            tipfont = Font.getDefault().derive(Font.ITALIC, 6, Ui.UNITS_pt); 
            LabelField lblRefreshField = new LabelField(CAResources.getString(CallAssistantResource.CA_REFRESH_TIPS),LabelField.NON_FOCUSABLE);
            lblRefreshField.setFont(tipfont);
            screen.add(lblRefreshField); 
            _btnRefresh = new ButtonField(CAResources.getString(CallAssistantResource.CA_REFRESH_DATA),ButtonField.NEVER_DIRTY|ButtonField.CONSUME_CLICK);
            _btnRefresh.setChangeListener(listener); 
            screen.add(_btnRefresh);
            
            screen.add(new SeparatorField()); 
            String poweredstr = "Powered By BBDev.org";
            LabelField lf = new LabelField(poweredstr);
            try{
                int s = SCREEN_WIDTH - _font.getAdvance(poweredstr)- 5;
                if( s >= 0 && s < SCREEN_WIDTH )
                    lf.setPosition(s);
            }catch(Exception ex){
                   Utils.OutputDebugString(ex.toString());
            }
            screen.add(lf);
            

        }
       //Utils.OutputDebugString("populateMainScreen:" + _data.getTime());    
    }

    // Save the data.
    public void save() 
    {
        for(int i=0;i<_OPTMAX;i++)
        {
            _data.setSelected(_ocf[i].getSelectedIndex(),i);
        }
        
        _data.setTime(Integer.parseInt(_ef.getText()));
        int[] eachfee = new int[2];
        eachfee[0] = Integer.parseInt(_efeachfee.getText());
        eachfee[1] = Integer.parseInt(_efeachoutfee.getText());
        _data.setEachFee(eachfee);
        _data.setActiveCode(_efactive.getText());
        _data.setIPCall(_efipcall.getText(),_efipcall2.getText());
        
        _data.setSpendTimeColor(_colshowtime.getSelectedColor());
        _data.setShowSpendTime(_ocfspendtime.getSelectedIndex()==0?true:false);
        
        Database db = Utils.getRuntimeData();
        if(db!=null)
            setGlobalData(db);
       _data.commit();
    }

    // Retrieve the data. Used by other applications to access options data.
    public OptionsData getData() 
    {
       return _data;
    }

    public static OptionsData getStaicData() 
    {
       return OptionsData.load();
    }

    // Inner class to store selected option values.
   
}

