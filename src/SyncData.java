/*
 * SyncData.java
 *
 * bbdev, 2003-2008
 * Confidential and proprietary.
 */
package com.callasst;

import net.rim.device.api.synchronization.*;
import net.rim.device.api.system.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.util.*;

public class SyncData extends SyncItem{
    
    private static final int TYPE_BOOL_ARRAY            = 1;
    private static final int TYPE_INT_TIME              = 2;
    private static final int TYPE_LONG_TOTALTIME        = 3;
    private static final int TYPE_LONG_TOTALFEE         = 4;
    private static final int TYPE_INT_FEE               = 5;
    private static final int TYPE_STR_IPCALL            = 6;
    private static final int TYPE_STR_ACTIVECODE        = 7;
    //private static final int TYPE_STR_IPCALL2           = 8;
    
    private static SyncData _syncData;
    
    public SyncData() 
    {   
    
    
    }
    
    public static SyncData getInstance()
    {
        if (_syncData == null) {
            _syncData = new SyncData();
        }
        return _syncData;
    }
    
    public int getSyncVersion()
    {
        return 1;
    }
    
    public String getSyncName()
    {
        return "CallAssistant";
    }
    
    public String getSyncName(Locale locale)
    {
        return null;
    }
    
    public boolean getSyncData(DataBuffer buffer, int version)
    {
        try{
            OptionsData _data = OptionsData.load(); 
            
            ConverterUtilities.writeIntArray(buffer,TYPE_BOOL_ARRAY,_data.getBoolArray());
            ConverterUtilities.writeInt(buffer,TYPE_INT_TIME,_data.getTime());
            ConverterUtilities.writeLong(buffer,TYPE_LONG_TOTALTIME,_data.getTotalTime());
            ConverterUtilities.writeLong(buffer,TYPE_LONG_TOTALFEE,_data.getTotalFee());
            ConverterUtilities.writeIntArray(buffer,TYPE_INT_FEE,_data.getEachFee());
            ConverterUtilities.writeString(buffer,TYPE_STR_IPCALL,_data.getIPCall()+"|"+_data.getIPCall2());
            ConverterUtilities.writeString(buffer,TYPE_STR_ACTIVECODE,_data.getActiveCode());
            return true;
        }catch(Exception ex){
            return false;
        }

    }
    
    public boolean setSyncData(DataBuffer buffer, int version)
    {
        try{
            OptionsData _data = OptionsData.load();
            
            while(buffer.available() > 0)
            {
                if(ConverterUtilities.isType(buffer,TYPE_BOOL_ARRAY))
                {
                    _data.setBoolArray(ConverterUtilities.readIntArray(buffer));
                }
                else if(ConverterUtilities.isType(buffer,TYPE_INT_TIME))
                {
                    _data.setTime(ConverterUtilities.readInt(buffer));
                }
                else if(ConverterUtilities.isType(buffer,TYPE_LONG_TOTALTIME))
                {
                    _data.setTotalTime(ConverterUtilities.readLong(buffer));
                }
                else if(ConverterUtilities.isType(buffer,TYPE_LONG_TOTALFEE))
                {
                    _data.setTotalFee(ConverterUtilities.readLong(buffer));
                }
                else if(ConverterUtilities.isType(buffer,TYPE_INT_FEE))
                {
                    _data.setEachFee(ConverterUtilities.readIntArray(buffer));
                }
                else if(ConverterUtilities.isType(buffer,TYPE_STR_IPCALL))
                {
                    String ipcall = new String(ConverterUtilities.readByteArray(buffer)).trim();
                    String[] ipcallarr = Utils.split(ipcall,"|");
                    if(ipcallarr.length == 1)
                        _data.setIPCall(ipcallarr[0],"");
                    else if(ipcallarr.length == 2)
                        _data.setIPCall(ipcallarr[0],ipcallarr[1]);
                }               
                else if(ConverterUtilities.isType(buffer,TYPE_STR_ACTIVECODE))
                {
                    _data.setActiveCode(new String(ConverterUtilities.readByteArray(buffer)).trim());
                }
            }
            
            _data.commit();
            return true;
        }catch(Exception ex){
            return false;
        }
    }
} 
