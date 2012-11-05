package com.callasst;

import com.callasst.Utils;
import java.io.*;
import net.rim.device.api.system.*;

public final class FindCore {
    
    public static String FindLocateFormNumber(String number,Database data)
    {
        if(number.startsWith("+86"))
        {
           number = number.substring(3);
        }
        if(number.startsWith("+086"))
        {
           number = number.substring(4);
        }
        if(number.charAt(0) == '+')
        {
           number = number.substring(1);
        }
        if(number.startsWith("17951"))
        {
           number = number.substring(5);
        }    
        
        if( number.length() < 7)
            return null;
        
        int num = 0;
        int startnum = 0;
        String result = null;
        
        //fix 4.6 bug
        int index = 0;
        while(index < number.length())
        {
            char c = number.charAt(index);
            if(c >= '0' && c <= '9')
                break;
            index++;    
        }
        if(index == number.length())
        {
            return null;
        }
        if(index > 0)
            number = number.substring(index);
            
        if(number.charAt(0) == '0')
        {
            if(number.charAt(1) == '1' || number.charAt(1) == '2')
                number = number.substring(0,3);
            else
                number = number.substring(0,4);
            
            try{
                num = Integer.parseInt(number);
            }
            catch(NumberFormatException fex){            
                Utils.OutputDebugString(fex.toString());
                return null;   
            }
            //Utils.OutputDebugString("number:" + num);
            result = searchPhone(num,data);
        }
        //else
        else if(number.charAt(0) == '1')
        {  
            if(number.length() > 11 )
                number = number.substring(number.length()-11);
        
            try{
                startnum = Integer.parseInt(number.substring(0,3));
                num = Integer.parseInt(number.substring(3,7));
            }
            catch(NumberFormatException fex){            
                Utils.OutputDebugString(fex.toString());
                return null;   
            }
            
            if(!data.bError)
                result = searchMobile2(number,data);
                //result = searchMobile(startnum,num,data);
            else
                return null;
        }
        else
            return null;
        
       return result;   
    }
    
    
    public static String FindLocateFormNumber(String number,boolean incoming,Database data)
    {
        if(data == null)
            data = Utils.getRuntimeData();
        if(data == null)
            return null;
        if(!data.bShow && incoming)    
            return null;
        else if(!data.bOutShow && !incoming)   
            return null; 
       /*if(data.bError)
       {
             try{
                 data.readMobileData();
                 data.bError = false;
              }
              catch(OutOfMemoryError ex){
                 data.bError = true;
                 Utils.OutputDebugString(ex.toString());
              }
              
              
       }  */

        return FindLocateFormNumber(number,data);
    }
    
   
    
    private static String searchPhone(int num,Database data)
    {
        /*int low = 0;
        int high = data.iPhoneCount;
        int middle = 0;
        
        while (low <= high) {
            middle = (low + high) >> 1;
            
            if(data.tCityNumber[middle] == num)
                break;
            else if(data.tCityNumber[middle] > num)
                high = middle - 1; 
            else
                low = middle + 1; 
        }*/
        int middle = -1;
        for(int i=0;i<data.tPhoneNumber.length;i++)
        {
            if(data.tPhoneNumber[i] == num)
                middle = i;
        }
        
            StringBuffer strstate = new StringBuffer(8);
            StringBuffer strcity = new StringBuffer(8);
            StringBuffer strresult= new StringBuffer(16);
            if(middle >= 0)
            {
                int cityidx = data.tPhoneIndex[middle];
                byte[] outbyte = new byte[2];
                //find city
                if(cityidx < 65535)
                {
                    int wordlen = data.tCity[cityidx+2] >> 1;
                    for (int i=0; i<wordlen; i++)
                    {
                        outbyte[0] = data.tCity[cityidx+4+(i<<1)];
                        outbyte[1] = data.tCity[cityidx+3+(i<<1)];    
                        try{
                            strcity.append( new String( outbyte,"UTF-16BE" ) );
                        }
                        catch(java.io.UnsupportedEncodingException ex){
                            Utils.OutputDebugString(ex.toString());
                        }
                    }
                    
                    int stateidx = Utils.readUShort(data.tCity,cityidx);
                    //find state
                    if(stateidx < 65535)
                    {
                        wordlen = data.tState[stateidx] >> 1;
                        for (int i=0; i<wordlen; i++)
                        {
                            outbyte[0] = data.tState[stateidx+2+(i<<1)];
                            outbyte[1] = data.tState[stateidx+1+(i<<1)];    
                            try{
                                strstate.append( new String( outbyte,"UTF-16BE" ) );
                            }
                            catch(java.io.UnsupportedEncodingException ex){
                                Utils.OutputDebugString(ex.toString());
                            }
                        }
                    }
                }
            
                if(strcity.length() > 0)
                {    
                    if(strstate.length() > 0)
                    {
                            strresult.append(strstate.toString());
                            strresult.append(",");
                    }
                    strresult.append(strcity.toString());
                }
            }   

    
        if(strresult.length() > 0)
            return strresult.toString();
        else
            return null;
        
       /* StringBuffer strresult= new StringBuffer(8);
        if(low<=high)
        {
           // Utils.OutputDebugString("state index:"+data.tStateIndex[middle]);
            int stateidx = data.tStateIndex[middle];
            byte[] outbyte = new byte[2];
            //find city
            if(stateidx < 65535)
            {
                    int wordlen = data.tState[stateidx] >> 1;
                    for (int i=0; i<wordlen; i++)
                    {
                        outbyte[0] = data.tState[stateidx+2+(i<<1)];
                        outbyte[1] = data.tState[stateidx+1+(i<<1)];    
                        try{
                            strresult.append( new String( outbyte,"UTF-16BE" ) );
                        }
                        catch(java.io.UnsupportedEncodingException ex){
                            Utils.OutputDebugString(ex.toString());
                        }
                    }              
            }
        }   
      
        //Utils.OutputDebugString("sss:"+strresult.toString());
        if(strresult.length() > 0)
            return strresult.toString();
        else
            return null;*/
    }
    
    /*private static String searchMobile(int num,Database data)
    {
        StringBuffer strstate = new StringBuffer(8);
        StringBuffer strcity = new StringBuffer(8);
        StringBuffer strresult= new StringBuffer(16);
        
        InputStream is =  data.readMobileData();
            
        if(strresult.length() > 0)
            return strresult.toString();
        else
            return null;
    }*/
    private static int readInt(InputStream is) throws IOException
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

    private static int readUShort (InputStream is)  throws IOException
    {
        int a1 = is.read();
        if(a1<0) a1+=256;
        int a2 = is.read();
        if(a2<0) a2+=256;
        return (int)(a1|a2<<8);
    }     
    
    
    private static String searchMobile2(String number,Database data)
    {
            int cityidx = -1;
            //int index = 0;
            int startnum,midnum,lastnum;
            int mididx=-1,lastidx=-1;
            int startbound=-1,midbound=-1,lastbound=-1;
            try{
                startnum = Integer.parseInt(number.substring(0,3));
                midnum = Integer.parseInt(number.substring(3,5));
                lastnum = Integer.parseInt(number.substring(5,7));
            }
            catch(NumberFormatException fex){            
                Utils.OutputDebugString(fex.toString());
                return null;   
            }
            
            int startlen = Utils.readUShort(data.tMobile,0);
            int midlen = Utils.readUShort(data.tMobile,2);
            int lastlen = Utils.readInt(data.tMobile,4);
            int offset = 8;
            
            startbound = offset+startlen;
            
            while(offset<startbound)
            {
                if(startnum == Utils.readUShort(data.tMobile,offset))
                {
                    mididx = Utils.readInt(data.tMobile,offset+2);
                    if(offset+8 < startbound)
                        midbound = Utils.readInt(data.tMobile,offset+8);
                    else
                        midbound = startbound+midlen;
                    break;
                }   
                
                offset+=6;
            }
            if(mididx == -1)
                return null;
            offset=mididx;    
            while(offset<=midbound)
            {
                if(midnum == data.tMobile[offset])
                {
                    lastidx = Utils.readInt(data.tMobile,offset+1);
                    if(offset+6 < midbound)
                        lastbound = Utils.readInt(data.tMobile,offset+6);
                    else
                        lastbound = midbound+lastlen;
                    break;
                }   

                offset+=5;
            }
            if(lastidx == -1)
                return null;             
            offset=lastidx;  
            
            if(offset >= data.tMobile.length)
            {
                Utils.OutputDebugString("searchMobile2:lastidx out of bound!");
                return null;   
            }    
            int lastnumtmp;        
            while(offset<=lastbound)
            {
                lastnumtmp = Utils.readUShort(data.tMobile,offset) & 0x7F;
                if(lastnum == lastnumtmp)
                {
                    cityidx = Utils.readUShort(data.tMobile,offset) >>7;
                    break;
                }
                offset+=2;
            }
           
           /* for(int i=0;i<data.tMobileStart.length;i+=2)
            {
                if(startnum == data.tMobileStart[i])
                {
                    mididx = data.tMobileStart[i+1];
                    if(i+3 < data.tMobileStart.length)
                        midbound = data.tMobileStart[i+3];
                    else
                        midbound = data.tMobileMid.length;
                    break;
                }
            }
            if(mididx == -1)
                return null;
            for(int i=mididx;i<midbound;i++)
            {
                if(midnum == data.tMobileMid[i])
                {
                    lastidx = data.tMobileMidPoint[i];
                    if(i+1<midbound)
                        lastbound = data.tMobileMidPoint[i+1];
                    else
                        lastbound = data.tMobileLast.length;
                    break;
                }
            }    
            if(lastidx == -1)
                return null;  
            int lastnumtmp;        
            for(int i=lastidx;i<lastbound;i++)
            {
                lastnumtmp = data.tMobileLast[i]& 0x7F;
                if(lastnum == lastnumtmp)
                {
                    cityidx = data.tMobileLast[i]>>7;
                    break;
                }
            }         */        
        
            StringBuffer strresult= new StringBuffer(16);
            if(cityidx >= 0)
            {
                StringBuffer strstate = new StringBuffer(8);
                StringBuffer strcity = new StringBuffer(8);
                int cityoffset = data.tCityIndex[cityidx];
                byte[] outbyte = new byte[2];
                //find city
                if(cityoffset < 65535)
                {
                    int wordlen = data.tCity[cityoffset+2] >> 1;
                    for (int i=0; i<wordlen; i++)
                    {
                        outbyte[0] = data.tCity[cityoffset+4+(i<<1)];
                        outbyte[1] = data.tCity[cityoffset+3+(i<<1)];    
                        try{
                            strcity.append( new String( outbyte,"UTF-16BE" ) );
                        }
                        catch(java.io.UnsupportedEncodingException ex){
                            Utils.OutputDebugString(ex.toString());
                        }
                    }
                    
                    int stateidx = Utils.readUShort(data.tCity,cityoffset);
                    //find state
                    if(stateidx < 65535)
                    {
                        wordlen = data.tState[stateidx] >> 1;
                        for (int i=0; i<wordlen; i++)
                        {
                            outbyte[0] = data.tState[stateidx+2+(i<<1)];
                            outbyte[1] = data.tState[stateidx+1+(i<<1)];    
                            try{
                                strstate.append( new String( outbyte,"UTF-16BE" ) );
                            }
                            catch(java.io.UnsupportedEncodingException ex){
                                Utils.OutputDebugString(ex.toString());
                            }
                        }
                    }
                }
            
                if(strcity.length() > 0)
                {    
                    if(strstate.length() > 0)
                    {
                            strresult.append(strstate.toString());
                            strresult.append(",");
                    }
                    strresult.append(strcity.toString());
                }
            }
            
           if(strresult.length() > 0)
               return strresult.toString();
           else
               return null;
    }

    /*private static String searchMobile(int startnum,int num,Database data)
    {
       InputStream is = data.readMobileData();
       int filelen = 0;
       int headlen = 0;
       int index = 2;
       int blockpos = 0;
       int blocklen = 0;
       int cityidx = -1;  
       try
       {
            filelen = is.available();
            headlen = readUShort(is);
            
            while(index<headlen)
            {
                if(readUShort(is) == startnum)
                {
                    blockpos = readInt(is);
                    index += 6;
                    if(index<headlen)
                    {
                        is.skip(2);
                        blocklen = readInt(is) - blockpos;
                        index+= 6;
                    }
                    else
                        blocklen = filelen - blockpos;
                        
                    break;
                } 
                else
                {
                    is.skip(4);
                    index+=6;
                }
            }
            
            blockpos -= index;
 
            index = 0;   
            int n = 0;
            if(blockpos >0 && blocklen > 0)   
            { 
                is.skip(blockpos);
                //int n=0,tmp=0,tmpcity=0;
                while(index<blocklen)
                {
                    cityidx = readUShort(is);
                    n =readUShort(is);
                    if(n == num)
                        break;    
                    else
                        cityidx = -1;
     
                    index+=8;    
                }
            }
            
            is.close();
        }
        catch(IOException ex)
        {
            Utils.OutputDebugString(ex.toString());
        }    
        finally
        {
            is = null;
        }
        
            StringBuffer strresult= new StringBuffer(16);
            if(cityidx >= 0)
            {
                StringBuffer strstate = new StringBuffer(8);
                StringBuffer strcity = new StringBuffer(8);

                byte[] outbyte = new byte[2];
                //find city
                if(cityidx < 65535)
                {
                    int wordlen = data.tCity[cityidx+2] >> 1;
                    for (int i=0; i<wordlen; i++)
                    {
                        outbyte[0] = data.tCity[cityidx+4+(i<<1)];
                        outbyte[1] = data.tCity[cityidx+3+(i<<1)];    
                        try{
                            strcity.append( new String( outbyte,"UTF-16BE" ) );
                        }
                        catch(java.io.UnsupportedEncodingException ex){
                            Utils.OutputDebugString(ex.toString());
                        }
                    }
                    
                    int stateidx = Utils.readUShort(data.tCity,cityidx);
                    //find state
                    if(stateidx < 65535)
                    {
                        wordlen = data.tState[stateidx] >> 1;
                        for (int i=0; i<wordlen; i++)
                        {
                            outbyte[0] = data.tState[stateidx+2+(i<<1)];
                            outbyte[1] = data.tState[stateidx+1+(i<<1)];    
                            try{
                                strstate.append( new String( outbyte,"UTF-16BE" ) );
                            }
                            catch(java.io.UnsupportedEncodingException ex){
                                Utils.OutputDebugString(ex.toString());
                            }
                        }
                    }
                }
            
                if(strcity.length() > 0)
                {    
                    if(strstate.length() > 0)
                    {
                            strresult.append(strstate.toString());
                            strresult.append(",");
                    }
                    strresult.append(strcity.toString());
                }
            }
            
           if(strresult.length() > 0)
               return strresult.toString();
           else
               return null;
    }
    */
    
    /*private static String searchMobile(int num,Database data)
    {
            int low = 0;
            int high = data.iNumCount;
            int middle = 0;
            while (low <= high) {
                middle = (low + high) >> 1;
                
                if(data.tNumber[middle] == num)
                    break;
                else if(data.tNumber[middle] > num)
                    high = middle - 1; 
                else
                    low = middle + 1; 
            }
            
            StringBuffer strstate = new StringBuffer(8);
            StringBuffer strcity = new StringBuffer(8);
            StringBuffer strresult= new StringBuffer(16);
            if(low<=high)
            {
                int cityidx = data.tMobileIndex[middle];
                byte[] outbyte = new byte[2];
                //find city
                if(cityidx < 65535)
                {
                    int wordlen = data.tCity[cityidx+2] >> 1;
                    for (int i=0; i<wordlen; i++)
                    {
                        outbyte[0] = data.tCity[cityidx+4+(i<<1)];
                        outbyte[1] = data.tCity[cityidx+3+(i<<1)];    
                        try{
                            strcity.append( new String( outbyte,"UTF-16BE" ) );
                        }
                        catch(java.io.UnsupportedEncodingException ex){
                            Utils.OutputDebugString(ex.toString());
                        }
                    }
                    
                    int stateidx = Utils.readUShort(data.tCity,cityidx);
                    //find state
                    if(stateidx < 65535)
                    {
                        wordlen = data.tState[stateidx] >> 1;
                        for (int i=0; i<wordlen; i++)
                        {
                            outbyte[0] = data.tState[stateidx+2+(i<<1)];
                            outbyte[1] = data.tState[stateidx+1+(i<<1)];    
                            try{
                                strstate.append( new String( outbyte,"UTF-16BE" ) );
                            }
                            catch(java.io.UnsupportedEncodingException ex){
                                Utils.OutputDebugString(ex.toString());
                            }
                        }
                    }
                }
            
                if(strcity.length() > 0)
                {    
                if(strstate.length() > 0)
                {
                        strresult.append(strstate.toString());
                        strresult.append(",");
                }
                strresult.append(strcity.toString());
                }
            }   

    
     if(strresult.length() > 0)
        return strresult.toString();
     else
        return null;
    }*/


}
