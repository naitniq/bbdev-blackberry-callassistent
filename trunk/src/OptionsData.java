/*
 * OptionsData.java
 *
 * � <your company here>, 2003-2007
 * Confidential and proprietary.
 */

package com.callasst;

import net.rim.blackberry.api.options.*;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;

public  final class OptionsData implements Persistable {

            private static final long ID = 0x6af0b5eb44dd8989L;
            private int _sel[] = new int[OptionsUI._OPTMAX];
            private int _time = 1000;
            private String _activeCode = "";
            
            private long _totalTime = 0;    //second
            private long _totalFee = 0; //fen
            private int []_eachFee = {0,15};  //in.out
            private String _ipcall = "17951";
            private String _ipcall2 = "";
            
            private int _spendTimeColor = 0;
            private boolean _isSpendTime = true;
            
            private OptionsData() {       
        
            }
            
            public int[] getEachFee() { return _eachFee;}
            public void setEachFee(int[] fee) {
                    for(int i=0;i<fee.length;i++)
                    {
                        if(i<_eachFee.length)
                            _eachFee[i] = fee[i];
                        else
                            break;
                    }
            }
                    
            public long getTotalFee() { return _totalFee;}
            public void setTotalFee(long fee) { _totalFee = fee;}
            
            public long getTotalTime() { return _totalTime;}
            public void setTotalTime(long time) { _totalTime = time;}
            
            public String getIPCall(){ return _ipcall;}
            public String getIPCall2(){ return _ipcall2;}
            public void setIPCall(String ipcall,String ipcall2){ _ipcall = ipcall;_ipcall2 = ipcall2;} 
            
            public String getActiveCode(){ return _activeCode;}
            public void setActiveCode(String acode){ _activeCode = acode; }    
            
            public int getTime() { return _time;}
            public void setTime(int time) { _time = time;}
            
            public int getSelected(int index) { return _sel[index];}
            public void setSelected(int value,int index){ _sel[index] = value;}
    
            public int getSpendTimeColor() { return _spendTimeColor;}
            public void setSpendTimeColor(int time) { _spendTimeColor = time;}  
            
            public boolean isShowSpendTime() { return _isSpendTime;}
            public void setShowSpendTime(boolean value) { _isSpendTime = value;}  
            
            public int[] getBoolArray()
            {
                return _sel;
            }
            public void setBoolArray(int []array)
            {
                for(int i=0;i<array.length;i++)
                {
                    if(i<_sel.length)
                        _sel[i] = array[i];
                    else
                        break;
                }
            }    
    
            public void commit() 
            {
                PersistentObject.commit(this);
            }

            public static OptionsData load() 
            {
                PersistentObject persist = PersistentStore.getPersistentObject( OptionsData.ID );
        
                OptionsData contents = (OptionsData)persist.getContents();
        
                synchronized( persist ) {
                    if( contents == null ) {
                        contents = new OptionsData();
                        persist.setContents( contents );
                        persist.commit();
                        }
                }
                return contents;
            }
}
