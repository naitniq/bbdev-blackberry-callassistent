package com.callasst;

import java.io.*;
import javax.microedition.io.file.*;
import javax.microedition.io.*;

//import net.rim.device.api.util.*;
//import net.rim.device.api.compress.*;

final class LibLoad {
    
    static LibLoad myInstance = null;
    
    private LibLoad()
    {
        
        
    }
    
    public static LibLoad getInstance()
    {
        if(myInstance == null)
            myInstance = new LibLoad();
        return myInstance;
    }
    
    
    public InputStream getData(final String resname)
    {
        return getClass().getResourceAsStream(resname);
    }
    
}

