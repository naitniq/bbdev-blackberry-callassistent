/*
 * KeyMaster.java
 * bbdev, 2008
 */

package com.callasst.searchlocation;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.blackberry.api.homescreen.*;
import com.callasst.*;

class SearchLocation extends UiApplication{

        SearchLocation()
        {
            SearchScreen ss = new SearchScreen();
            this.pushScreen(ss);
        }
    
        public static void main(String[] args) 
        {
            new SearchLocation().enterEventDispatcher();
        }
        
} 
