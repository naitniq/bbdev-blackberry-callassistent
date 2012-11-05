package com.callasst;

import javax.microedition.pim.*;
import java.io.*;
import net.rim.device.api.system.*;
import net.rim.device.api.util.*;
import net.rim.device.api.ui.component.*;
import net.rim.blackberry.api.pdap.*;
import net.rim.blackberry.api.phone.phonelogs.*;
import net.rim.device.api.crypto .*;
import com.callasst.*;
import com.callasst.res.*;
import net.rim.device.api.ui.UiApplication;
//import net.rim.blackberry.api.phone.phonegui.*;
import net.rim.device.api.applicationcontrol.*;

import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;
import java.util.Vector;
//import com.callasst.findlocation.*;

 public final class Utils {
    
    public static void OutputDebugString(String obj)
    {
        if(Const.DEBUG){
            System.out.println("#####DEBUG :"+ obj); 
        }      
    } 
    
    public static boolean haveSDCard()
    {
        FileConnection fc = null;
        //OutputStream os = null;
        boolean ret = false;
        try{
            fc = (FileConnection) Connector.open("file:///SDCard/calldata");
            ret = fc.exists();
            fc.close();
        }catch(Exception ex){
            
        }finally{
            if(fc != null){
                fc = null;
            }
        }
        
        return ret;
    }
    
    public static void setPermissions()
    {
        try{
            ApplicationPermissionsManager appPM = ApplicationPermissionsManager.getInstance();
            ApplicationPermissions requestedPermissions = appPM.getApplicationPermissions();
            //requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_CODE_MODULE_MANAGEMENT);
            requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_FILE_API);    
            requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_PHONE);
            requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_PIM); 
            //requestedPermissions.addPermission(ApplicationPermissions.PERMISSION_CHANGE_DEVICE_SETTINGS);                
            appPM.invokePermissionsRequest(requestedPermissions);   
        }catch(Exception ex){
               ;           
        }
    }
    /*
    public static void addPhoneScreen(int callid,String location,Application app)
    {
        PhoneScreen ps = new PhoneScreen(callid, app);
        ps.add(new LabelField(location));
        ps.sendDataToScreen();
    }
    */
    public synchronized static Contact appendContact(String location,String phone)
    {
       // Verify that a first or last name and email has been entered.
       try {
                ContactList contactList = (ContactList)PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.WRITE_ONLY);
                Contact contact = contactList.createContact();
                String[] name = new String[contactList.stringArraySize(Contact.NAME)];
              // Add values to PIM item.
                name[Contact.NAME_GIVEN] = location;
                name[Contact.NAME_FAMILY] = "";

                contact.addStringArray(Contact.NAME, Contact.ATTR_NONE, name);
                contact.addString(Contact.TEL, Contact.ATTR_NONE, phone);
                
              // Save data to address book.
                if(contact.isModified()) {
                    contact.commit();
                }
                
                return contact;
           } catch (PIMException e) {
                OutputDebugString("PIMException:" + e.toString());
                return null;
           }
    }
    
    
    public synchronized static void removeContact(Contact contact)
    {
       // Verify that a first or last name and email has been entered.
       try {
                ContactList contactList = (ContactList)PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.WRITE_ONLY);
                if(contact != null)
                    contactList.removeContact(contact);
                
           } catch (PIMException e) {
                OutputDebugString("PIMException:" + e.toString());
           }
    } 
    
    public synchronized static void changePhoneLogs(String locate)
    {
        if(locate == null)
            return;
        
        PhoneLogs _logs = PhoneLogs.getInstance();
        int lognum = _logs.numberOfCalls(PhoneLogs.FOLDER_MISSED_CALLS);
        PhoneCallLog phoneLog=null;
        PhoneCallLogID participant=null;
        String name=null;
        if(lognum > 0)
        {
            phoneLog = (PhoneCallLog)_logs.callAt(lognum-1,PhoneLogs.FOLDER_MISSED_CALLS);
            participant = phoneLog.getParticipant();
            name = participant.getName();
        }
        
        if(name != null && locate.equals(name))
        {
           participant.setName(null);
            if(phoneLog!=null)
            {
                String notes = phoneLog.getNotes();
                if(notes != null)
                    notes = notes + "\n" + CAResources.getString(CallAssistantResource.CA_FROM) + " " + name;
                 else
                    notes =  CAResources.getString(CallAssistantResource.CA_FROM) + " " + name;
                phoneLog.setNotes(notes);
            }
        }
        else
        {
            lognum = _logs.numberOfCalls(PhoneLogs.FOLDER_NORMAL_CALLS);
            if(lognum > 0)
            {
                phoneLog = (PhoneCallLog)_logs.callAt(lognum-1,PhoneLogs.FOLDER_NORMAL_CALLS);
                participant = phoneLog.getParticipant();
                name = participant.getName();
            }
            if(name != null && locate.equals(name))
            {
                participant.setName(null);
                //if(phoneLog!=null)
                   // phoneLog.setNotes(CAResources.getString(CallAssistantResource.CA_FROM) + " " + name);
            }
        }
        
       // OutputDebugString( participant.getType() );
       // OutputDebugString( participant.getNumber());
       // OutputDebugString( participant.getName());
    }
    
    public static int readUByte(byte b)
    {
        return b<0?b+256:b;
    }

    public static long readUInt(byte[] b,int start)
    {
        long a1 = b[start];
        if(a1<0) a1+=256;
        long a2 = b[start+1];
        if(a2<0) a2+=256;
        long a3 = b[start+2];
        if(a3<0) a3+=256;
        long a4 = b[start+3];
        if(a4<0) a4+=256;         
        return (long)(a1|a2<<8|a3<<16|a4<<24);
    }
     
    public static int readInt(byte[] b,int start)
    {
        int a1 = b[start];
        if(a1<0) a1+=256;
        int a2 = b[start+1];
        if(a2<0) a2+=256;
        int a3 = b[start+2];
        if(a3<0) a3+=256;
        int a4 = b[start+3];
        if(a4<0) a4+=256;   
        return (int)(a1|a2<<8|a3<<16|a4<<24);
    }  

    public static int readUShort(byte[] b,int start)
    {
        int a1 = b[start];
        if(a1<0) a1+=256;
        int a2 = b[start+1];
        if(a2<0) a2+=256;
        return (int)(a1|a2<<8);
    }     
    
    public static Database getRuntimeData()
    { 
        RuntimeStore store = RuntimeStore.getRuntimeStore(); 
        try {
            Database data = (Database)store.get(Const.LOCATE_DATA_ID);
            return data;
        }catch(ControlledAccessException e) {
            Utils.OutputDebugString(e.toString());
            return null;
        }
    }
    
    public static boolean isActive(String activeCode)
    {
        if(Const.DEBUG)
            return true;
        /*try{
            Dialog.alert(activeCode);
        }catch(Exception ex)
        {
            ;
        }*/
        activeCode = activeCode.trim();
        if(activeCode.length() != 8)
            return false;
        /*try{
            Dialog.alert(calcActiveCode());
        }catch(Exception ex)
        {
            ;
        }     */ 
       
        return StringUtilities.strEqualIgnoreCase(activeCode,calcActiveCode());
    }
    
    private static String calcActiveCode()
    {
        int pinid = DeviceInfo.getDeviceId();
        
        int fn1 = pinid&0xFF000000;
        int fn2 = pinid&0xFF0000;
        int fn3 = pinid&0xFF00;
        int fn4 = pinid&0xFF;
              
        int re = (fn1>>1) + (fn2>>2) + (fn3>>3) + (fn4>>4) + (pinid>>4);    
        re|=0x8FB69A0;  
 
        byte []pl = (re + Const.NET_ADD).getBytes();
        
        String encstr = "";
        try{
            encstr = sampleMD5Digest(pl);
            //encstr = new String(dig,0,8);
        }catch(Exception ex){
            Utils.OutputDebugString(ex.toString()); 
        }
        return encstr;
    }    
    
    public static String TestActiveCode()
    {
        return calcActiveCode();
    }
        
    static public String sampleMD5Digest( byte[] plainText ) throws CryptoException, IOException
    {       
        byte[] digestData = new byte[32]; 
        // Create an instance of the digest algorithm
        MD5Digest digest = new MD5Digest();
        
        // Create the digest output stream for easy use
        DigestOutputStream digestStream = new DigestOutputStream( digest, null );
        
        // Write the text to the stream
        digestStream.write( plainText );
        
        // Copy the digest data to the digestData byte array and
        // return the length
        digest.getDigest(digestData, 0);
        
        StringBuffer md5s = new StringBuffer(10);
        for (int i=0;i < 4 ; i++)
        {
            if (((digestData[i]) & 0xFF) < 16) //toHexString returns single digit!!
                md5s.append("0");
            md5s.append(Integer.toHexString((digestData[i]) & 0xFF));
        }
        
        return md5s.toString();
    }

    public static void setTimeAndFee(long spendTime,boolean incoming)
    {
       OptionsData data = OptionsData.load();
       
       int[] eachFeeArray = data.getEachFee();
       int eachFee = eachFeeArray[0];
       if(!incoming)
            eachFee = eachFeeArray[1];
            
       long spendFee = (spendTime/60+1)*eachFee;
       data.setTotalTime(data.getTotalTime()+spendTime);
       data.setTotalFee(data.getTotalFee()+spendFee);
       data.commit();
    }
    
    public static boolean isPhoneNumber(final String value)
    {
        return Utils.isNumeric(value.replace('+','0'));
    }
    
    public static boolean isNumeric(final String value)
    {
        int len = value.length();
        for(int i=0;i<len;i++)
        {
            if(!Character.isDigit(value.charAt(i)))
                return false;
        }
                
        return true;
    }
    
    public static String[] split(String inString, String delimeter) 
    {
              inString=delSpace(inString);
              String[] retAr;
                try {
                    Vector vec = new Vector();
                     int indexA = 0;
                        int indexB = inString.indexOf(delimeter);

                     while (indexB != -1) {
                         vec.addElement(new String(inString.substring(indexA, indexB)));
                                indexA = indexB + delimeter.length();
                          indexB = inString.indexOf(delimeter, indexA);
                  }
                      vec.addElement(new String(inString.substring(indexA, inString
                                  .length())));
                  retAr = new String[vec.size()];
                        for (int i = 0; i < vec.size(); i++) {
                         retAr[i] = vec.elementAt(i).toString();
                        }
              } catch (Exception e) {
                        String[] ar = { e.toString() };
                        return ar;
             }
              return retAr;
    }

    private static String delSpace(String str){
       if(str==null||str.equals("")){
            return "";
       }
       int length=str.length();
       StringBuffer sb=new StringBuffer();
       boolean b=false;
       char c;
       for(int i=0;i<length;i++){
            c=str.charAt(i);
            if(c==' '){
                if(b){
                    continue;
                }
                b=true;
            }else{
                b=false;
            }
            sb.append(c);
       }
       return sb.toString();
    }
    /*
    private static int sampleMD5Digest( byte[] plainText, byte[] digestData ) throws CryptoException, IOException
    {
        // Create an instance of the digest algorithm
        MD5Digest digest = new MD5Digest();
        // Create the digest output stream for easy use
        DigestOutputStream digestStream = new DigestOutputStream( digest, null );
                
        // Write the text to the stream
        digestStream.write( plainText );
        
        // Copy the digest data to the digestData byte array and
        // return the length
        digest.getDigest( digestData, 0 );
        return digest.getDigestLength();
    }*/
    
    /*
    private static int sampleSHA1HMAC( byte[] hashKey, byte[] plainText, byte[] digestData ) 
        throws CryptoException, IOException
    {
        // Create the keyed hash function key
        HMACKey key = new HMACKey( hashKey );

        // Create an instance of an HMAC
        HMAC hMac = new HMAC( key, new SHA1Digest() );
        
        // Now create the MAC output stream for easy use
        MACOutputStream macStream = new MACOutputStream( hMac, null );
                
        // Write the text to the stream
        macStream.write( plainText );
        
        // Copy the digest data to the digestData byte array and
        // return the length
        hMac.getMAC( digestData, 0 );
        return hMac.getLength();
    } */
    
    
} 
