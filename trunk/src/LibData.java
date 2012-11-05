/*
 * LibData.java
 *
 * DianXun, 2003-2008
 * Confidential and proprietary.
 */
package com.callasst;

import net.rim.device.api.util.*;
import net.rim.device.api.system.*;

public final class LibData implements Persistable {

            //com.callasst.LibData$1
            private static final long ID = 0xaf3d70d6ee02e4f9L;
            public byte[] tState;

            public byte[] tCity;
            public short[] tCityIndex;
            
            public short[] tPhoneIndex;
            public short[] tPhoneNumber;
            
            //public byte[] tCity;
            public byte[] tMobile;
            /*public short[] tMobileStart;
            public byte[] tMobileMid;
            public int[] tMobileMidPoint;
            public short[] tMobileLast;*/
        
            private LibData() {       
        
            }
            
            public void commit() 
            {
                PersistentObject.commit(this);
            }

            public static LibData load() 
            {
                PersistentObject persist = PersistentStore.getPersistentObject( LibData.ID );
                LibData contents = null;
                
                try
                {
                    contents = (LibData) persist.getContents();
                }
                catch(Exception ex)
                {
                    contents = null;
                }
                
                synchronized( persist ) {
                        if( contents == null ) {
                                contents = new LibData();
                                persist.setContents( contents );
                                persist.commit();
                            }
                }
            
                return contents;
            }
}

