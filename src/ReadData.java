/*
 * ReadData.java
 *
 * � <your company here>, 2003-2007
 * Confidential and proprietary.
 */

package com.callasst.findlocation;

//import com.callasst.*;

import java.io.*;
import net.rim.device.api.util.*;
import net.rim.device.api.compress.*;


public final class ReadData {
    
    public final int STATE_ORG_SIZE = 139;
    public final int CITY_ORG_SIZE = 2615;
    public final int NUMBER_ORG_SIZE = 885576;
    
    byte[] tState;
    byte[] tCity;
    byte[] tNumber;
    
    ReadData() 
    {   
        InputStream is;
        try
        {
            is = getClass().getResourceAsStream(Const.RES_FOLDER +"00.dat");
            tState = new byte[STATE_ORG_SIZE];
            is.read(tState,0,STATE_ORG_SIZE);
            is.close();
            
            is = getClass().getResourceAsStream(Const.RES_FOLDER +"01.dat");
            is = new GZIPInputStream(is);
            tCity = new byte[CITY_ORG_SIZE];
            is.read(tCity,0,CITY_ORG_SIZE);
            is.close();       
            
            is = getClass().getResourceAsStream(Const.RES_FOLDER +"02.dat");
            is = new GZIPInputStream(is);
            tNumber = new byte[NUMBER_ORG_SIZE];
            is.read(tNumber,0,NUMBER_ORG_SIZE);
            is.close();        
        }
        catch(IOException ex)
        {
            //e.printStackTrace();
            Utils.OutputDebugString(ex.toString());
        }
        finally
        {
            is=null;
        }
    }
    
    
    
} 
