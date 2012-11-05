package com.callasst;

import java.io.*;
import net.rim.device.api.util.*;
import net.rim.device.api.compress.*;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;

public final class Database {
    
    public final static String RES_FOLDER = "/res/";
    public final static String RES_FOLDER_SD = "file:///SDCard/calldata/";
    ////option data
    public boolean bShow = true;
    public boolean bShark = true;
    public boolean bOutShow = true;
    public int iVTime = 1000;
    public boolean bActive = false;
    public boolean bEndCall = true;
    public boolean bShowSpeakTime = true;
    public int iSpeakTimeColor = 0;
     
    //////////////////////////////////////``
    public boolean bError  = false;
    
    public final int STATE_ORG_SIZE = 182; //00.dat
    public final int CITY_ORG_SIZE = 4998;  //01.dat
    public final int NUMBER_ORG_SIZE = 456996;  //02.dat
    public final int CITYNUM_ORG_SIZE = 1288;   //03.dat
    
    public byte[] tState;
    public byte[] tCity;
    public short[] tCityIndex;
    
    public short[] tPhoneIndex;
    public short[] tPhoneNumber;
           
    public byte[] tMobile;      
    
    public byte os_ver = 45; 
    /*public short[] tMobileStart;
    public byte[] tMobileMid;
    public int[] tMobileMidPoint;
    public short[] tMobileLast;*/
    
    //public byte[] tState;
    //public byte[] tCity;
    
    //byte[] tNumber;
    //public int[] tNumber;
    //public int[] tMobileIndex;
    //public int iNumCount; 
    
    //public short[] tCityIndex;
    //public short[] tCityNumber;
    //int iPhoneCoun;
     
    private int readInt(InputStream is) throws IOException
    {
        int a1 = is.read();
        if(a1<0) a1+=256;
        int a2 = is.read();
        if(a2<0) a2+=256;
        int a3 = is.read();
        if(a3<0) a3+=256;
        int a4 = is.read();
        if(a4<0) a4+=256;   
        return (int)(a1|a2<<8|a3<<16|a4<<24);
    }  

    private int readUShort (InputStream is)  throws IOException
    {
        int a1 = is.read();
        if(a1<0) a1+=256;
        int a2 = is.read();
        if(a2<0) a2+=256;
        return (int)(a1|a2<<8);
    }     
    
    private short readShort (InputStream is)  throws IOException
    {
        int a1 = is.read();
        if(a1<0) a1+=256;
        int a2 = is.read();
        if(a2<0) a2+=256;
        return (short)(a1|a2<<8);
    }  
    
    public Database() 
    {   
       
    }
    
    public void readPhoneData()
    {
        LibData _data = LibData.load();
        
        if(_data==null || _data.tState == null || _data.tCity==null || _data.tMobile == null)
        {
            if(Utils.haveSDCard())
                readPhoneData4sd();
            else
                readPhoneData4res();
        }
        
        loadData();  
    }
    
    public void refreshPhoneData()
    {
        //LibData _data = LibData.load();
        
        if(Utils.haveSDCard())
            readPhoneData4sd();
        else
            readPhoneData4res();   
                 
        loadData();  
    }
    
    public void readPhoneData4sd()
    {
        LibData _data = LibData.load();
      
        FileConnection fc = null;
        InputStream is = null;
        //LibLoad libload = LibLoad.getInstance();
        int size = 0;
        boolean readsuccess = false;
        try
        {
            fc = (FileConnection) Connector.open(RES_FOLDER_SD +"00.dat");
            size = (int)fc.fileSize();
            is = fc.openInputStream();
            //size = is.available();
            _data.tState = new byte[size];
            is.read(_data.tState,0,size);
            is.close();
            fc.close();
            
            fc = (FileConnection) Connector.open(RES_FOLDER_SD +"01.dat");
            size = (int)fc.fileSize();
            is = fc.openInputStream();
            
            short headlen = readShort(is);
            _data.tCityIndex = new short[headlen/2];
            for(int i =0;i<headlen/2;i++)
            {
                _data.tCityIndex[i] = (short)(readShort(is)-headlen-2);
            }
            _data.tCity = new byte[size-headlen-2];
            is.read(_data.tCity);
            is.close();       
            fc.close();
            
            fc = (FileConnection) Connector.open(RES_FOLDER_SD +"02.dat");
            size = (int)fc.fileSize();
            is = fc.openInputStream();
            //is.reset();
            //is = new GZIPInputStream(is);
            //size = is.available();
            _data.tMobile = new byte[size];
            is.read(_data.tMobile);
            is.close();  
            fc.close();
            
            fc = (FileConnection) Connector.open(RES_FOLDER_SD +"03.dat");
            size = (int)fc.fileSize();
            is = fc.openInputStream();
            //size = is.available();
            int iPhoneCount = size/4;
            _data.tPhoneNumber = new short[iPhoneCount];
            _data.tPhoneIndex = new short[iPhoneCount];
            for(int i=0;i<iPhoneCount;i++)
            {
                _data.tPhoneIndex[i] = (short)(readShort(is)-headlen-2);
                _data.tPhoneNumber[i] = readShort(is);
            }
            is.close();
            fc.close();      
            readsuccess = true;  
        }
        catch(IOException ex)
        {
           Utils.OutputDebugString(ex.toString());
           readsuccess = false;
        }
        finally
        {
            is=null;
        }   
        
        if(readsuccess){
            Utils.OutputDebugString("read sd data success.");
            _data.commit();
        }
    }

    public void readPhoneData4res()
    {
        LibData _data = LibData.load();
        //loadData();
        InputStream is;
        LibLoad libload = LibLoad.getInstance();
        try
        {
            is = libload.getData(RES_FOLDER +"00.dat");
            //is = getClass().getResourceAsStream(RES_FOLDER +"00.dat");
            _data.tState = new byte[STATE_ORG_SIZE];
            is.read(_data.tState,0,STATE_ORG_SIZE);
            is.close();
            
            //is = getClass().getResourceAsStream(RES_FOLDER +"01.dat");
            is = libload.getData(RES_FOLDER +"01.dat");
            short headlen = readShort(is);
            _data.tCityIndex = new short[headlen/2];
            for(int i =0;i<headlen/2;i++)
            {
                _data.tCityIndex[i] = (short)(readShort(is)-headlen-2);
            }
            //is = new GZIPInputStream(is);
            _data.tCity = new byte[CITY_ORG_SIZE-headlen-2];
            //is.read(tCity,0,CITY_ORG_SIZE-headlen-2);
            is.read(_data.tCity);
            is.close();       
            
            //is = getClass().getResourceAsStream(Const.RES_FOLDER +"02.dat");
            is = libload.getData(RES_FOLDER +"02.dat");
            is = new GZIPInputStream(is);
            _data.tMobile = new byte[NUMBER_ORG_SIZE];
            is.read(_data.tMobile);
            is.close();  
    
            /*
            {
                headlen = 8;
                int index = 0;
                int offset = 0;
                int filelen = NUMBER_ORG_SIZE;
                int startlen = readUShort(is);
                int midlen = readUShort(is);
                int lastlen = readInt(is);
                
                int preoffset = startlen+headlen;
                int startnum = (startlen)/6;
                int pos = 0;
                _data.tMobileStart = new short[startnum*2];
                while(pos<startlen)
                {
                    _data.tMobileStart[index++] = readShort(is);
                    offset = readInt(is);
                    _data.tMobileStart[index++] = (short)((offset - preoffset)/5);
                    //preoffset = offset;
                    
                    pos+=6;
                }
                
                int midnum = (midlen)/5;
                index = 0;
                pos = 0;
                preoffset =  startlen+midlen+headlen;
                _data.tMobileMid = new byte[midnum];
                _data.tMobileMidPoint = new int[midnum];
                while(pos<midlen)
                {
                    _data.tMobileMid[index] = (byte)is.read();
                    offset = readInt(is);
                    _data.tMobileMidPoint[index] = (offset - preoffset)/2;
                    if((offset - preoffset) > lastlen)
                        Utils.OutputDebugString("data overflow!");
                    //preoffset = offset;
                    index++;
                    pos+=5;       
                }
                
                int lastnum = lastlen/2;
                index = 0;
                pos = 0;   
                _data.tMobileLast = new short[lastnum];
                while(pos<lastlen)
                {     
                    _data.tMobileLast[index++] = readShort(is);
                    pos+=2;
                }      
                
                is.close();
            }
            */

            //is = getClass().getResourceAsStream(RES_FOLDER +"03.dat");
            is = libload.getData(RES_FOLDER +"03.dat");
            //is = new GZIPInputStream(is);
            int iPhoneCount = CITYNUM_ORG_SIZE/4;
            _data.tPhoneNumber = new short[iPhoneCount];
            _data.tPhoneIndex = new short[iPhoneCount];
            for(int i=0;i<iPhoneCount;i++)
            {
                _data.tPhoneIndex[i] = (short)(readShort(is)-headlen-2);
                _data.tPhoneNumber[i] = readShort(is);
            }
            //is.read(tNumber,0,NUMBER_ORG_SIZE);
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
        
        _data.commit();
        
        //loadData();
        
        /*tMobileStart =    _data.tMobileStart;
        tMobileMid =    _data.tMobileMid;
        tMobileMidPoint =   _data.tMobileMidPoint;
        tMobileLast =    _data.tMobileLast;*/
    }

    public void loadData()
    {
        LibData _data = LibData.load();
        
        tState = _data.tState;
        tCity = _data.tCity;
        tCityIndex = _data.tCityIndex;
        
        tPhoneNumber =    _data.tPhoneNumber;
        tPhoneIndex =    _data.tPhoneIndex;      
        
        tMobile = _data.tMobile;   
    }
    
    /*public void releaseData()
    {
        tState =null;
        tCity = null;
        tCityIndex = null;
        
        tPhoneNumber =    null;
        tPhoneIndex =    null;     
        tMobile = null; 
    }*/
    
    /*
    public InputStream readMobileData()
    {
        InputStream is = null;
        try
        {
            is = getClass().getResourceAsStream(RES_FOLDER +"02.dat");
            //is = new GZIPInputStream(is);
            iNumCount = NUMBER_ORG_SIZE/6;
    
            //tNumber = new int[iNumCount];
            //tCityIndex = new int[iNumCount];
            //for(int i=0;i<iNumCount;i++)
            //{
            //    tCityIndex[i] = readUShort(is);
            //    tNumber[i] = readInt(is);
            //}  
        } 
        catch(IOException ex)
        {
            //e.printStackTrace();
            //Utils.OutputDebugString(ex.toString());
        }
        finally
        {
            return is;
        }
    }  */
    
    /*public InputStream readMobileData()
    {
        return LibLoad.getInstance().getData(RES_FOLDER +"02.dat"); 
    }*/
   
    /*public void readMobileData()
    {
        InputStream is;
        try
        {
            is = getClass().getResourceAsStream(RES_FOLDER +"02.dat");
            is = new GZIPInputStream(is);
            iNumCount = NUMBER_ORG_SIZE/6;
            tNumber = new int[iNumCount];
            tMobileIndex = new int[iNumCount];
            for(int i=0;i<iNumCount;i++)
            {
                tMobileIndex[i] = readUShort(is);
                tNumber[i] = readInt(is);
            }  
        } 
        catch(IOException ex)
        {
            //e.printStackTrace();
            //Utils.OutputDebugString(ex.toString());
        }
        finally
        {
            is=null;
        }
    }*/
} 
