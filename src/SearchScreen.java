/*
 * SearchScreen.java
 *
 * bbdev, 2003-2008
 * Confidential and proprietary.
 */

package com.callasst.searchlocation;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.util.*;
import net.rim.device.api.system.*;
import com.callasst.*;
import com.callasst.res.*;


/*class FixEditField extends EditField{
    
    private int width;
 
    FixEditField(String label,
                 String initialValue,
                 int maxNumChars,
                 long style, int Width){
        super( label,initialValue,maxNumChars,style );
        width = Width;
    }
 
    public int getPreferredWidth(){
        return width;
    }
}*/

class SearchScreen extends MainScreen {
    
    private EditField _eNumber;
    private LabelField _lfResult;
    private ButtonField _btnSearch;
    
    SearchScreen() {   
        _eNumber = new EditField(CAResources.getString(CallAssistantResource.CA_PHONE_NUMBER),"",11,EditField.FILTER_NUMERIC);
        _btnSearch = new ButtonField(CAResources.getString(CallAssistantResource.CA_SEARCH_BUTTON),
        ButtonField.NEVER_DIRTY|ButtonField.CONSUME_CLICK|ButtonField.FIELD_HCENTER);
        _btnSearch.setChangeListener(listener);
        //_lfResult = new LabelField(CAResources.getString(CallAssistantResource.CA_SEARCH_RESULT));
        _lfResult = new LabelField("");
        
        //HorizontalFieldManager hfm = new HorizontalFieldManager(HorizontalFieldManager.USE_ALL_WIDTH);
        
        this.add(_eNumber);
        this.add(_btnSearch);
        //this.add(hfm);
        this.add(new SeparatorField()); 
        this.add(_lfResult);
        //this.add(vfm);
        this.setTitle(CAResources.getString(CallAssistantResource.CA_SEARCH_TITLE) + " "   
                                        + CAResources.getString(CallAssistantResource.CA_VERSION));
                                        
        MenuItem aboutitem = new MenuItem(CAResources.getString(CallAssistantResource.CA_ABOUT), 99, 100) {
                public void run() {
                    Dialog.alert(CAResources.getString(CallAssistantResource.CA_TITLE) + " "   
                                        + CAResources.getString(CallAssistantResource.CA_VERSION));
                }
            };
        this.addMenuItem(aboutitem);
    }

     FieldChangeListener listener = new FieldChangeListener() {
         public void fieldChanged(Field field, int context) {
                ButtonField buttonField = (ButtonField)field;
                if(_btnSearch == buttonField)
                {
                    String number = _eNumber.getText();
                    if(number!=null && number.length() > 6)
                    {
                        Database data = Utils.getRuntimeData();
                        String location =  FindCore.FindLocateFormNumber(number,data);
                        Utils.OutputDebugString("Location:" + location);
                        if(location!= null)
                            _lfResult.setText(CAResources.getString(CallAssistantResource.CA_SEARCH_RESULT) + location);
                    }
                    //UiApplication.getUiApplication().pushScreen(new DxCustomUI(_data.tabCiNew,_data.tabZi)); 
                } 
         }
     };
     
     
     
     protected boolean onSavePrompt()
     {
         return true;
     }
} 
