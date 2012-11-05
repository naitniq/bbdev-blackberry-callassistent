/*
 * ShowDialog.java
 *
 * � <your company here>, 2003-2007
 * Confidential and proprietary.
 */

package com.callasst;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.*;

public class ShowDialog extends Screen {
    
    public ShowDialog() {
        //super();    
        super( new VerticalFieldManager( Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR ) );
        
        
        
    }
    
    public int getPreferredWidth()
    {
       // DxTools.OutputDebugString( "getPreferredWidth" );
       return 100;
       // return _cStrChKey.length>0?( _cStrChKey.getAllWidth() + _IVL_W + 12 ):_CUSTOM_SIZE_W;
    }
    
    public int getPreferredHeight()
    {
        //DxTools.OutputDebugString( "getPreferredHeight" );
        //int line = calcEnKeyLine();
        int h = 30;
 
        return h;    
        //return _CUSTOM_SIZE_H>h?_CUSTOM_SIZE_H:h;
    }   
    
    protected void sublayout( int width, int height ) 
    {
        int x = 10;
        int y = 10;
        
        int w = getPreferredWidth();
        int h = getPreferredHeight();
      
        
        setExtent( w, h );
        
        setPosition( x, y );
        layoutDelegate( w, h );
    }
    
    
    protected void onExposed(){             
       UiApplication.getUiApplication().popScreen(this);
    }
    
    protected void onObscured(){
        //Screen activeScreen = UiApplication.getUiApplication().getActiveScreen();  
        //activeScreen.getUiEngine().doPaint();
        
                    Screen screen = UiApplication.getUiApplication().getActiveScreen();
                    //screen.doPaint();
                    screen.getUiEngine().suspendPainting(true);
                    //this.invalidate();
                    //this.updateDisplay();
                    UiApplication.getApplication().requestForeground();
                    
                    //UiApplication.getUiApplication().popScreen(screen);
                    //screen.doPaint();
                    //screen.getApplication().requestBackground();
                    //(bIncoming)
                    //
                    //UiApplication.getUiApplication().popScreen(screen);
                    //UiApplication.getUiApplication().pushModalScreen(screen);
                   //
                    //Screen screen = actscreen.getScreenAbove();
                    //screen.invalidate();
                    //screen.doPaint();
      
                    
                    /*if(screen.getUiEngine().isPaintingSuspended()){
                        screen.getUiEngine().suspendPainting(false);
                    }   */                    
                    //screen.invalidateLayout();
                    //screen.invalidate();
                    //screen.getUiEngine().updateDisplay();
                    
                    
                
                      
        //activeScreen.getUiEngine().suspendPainting(true);
        //UiApplication.getUiApplication().popScreen(this);
        //UiApplication.getApplication().requestForeground();
    }

} 
