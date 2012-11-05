package com.callasst.findlocation;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import com.callasst.*;

public final class ShowScreen extends Screen{

    private final static int _CUSTOM_SIZE_W   = 190;
    private final static int _CUSTOM_SIZE_H   = 80;

    private final static int _ALPHA    = 0xA0; // 60% alpha
    private final static int _IVL_W = 8;
    private final static int _IVL_H = 8;
    //private final static int _RGB_BORDER = 0xA8A8A8;
    private final static int _RGB_BACKGROUND = 0xA8A8A8;
    private final static int _RGB_FONT = 0x000000;  
    

    private final static int _FONT_HEIGHT = Font.getDefault().getHeight();
    
    private Font _font = Font.getDefault();
    private String _strArea = "";
    private Screen _cShowScreen;
    //private int _iPhoneSeed = 0;
    
    
    public ShowScreen(String phonenumber)
    {    
        super( new VerticalFieldManager( Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR ) );
      
        //String phoneseed = phonenumber.substring(0,6);
        //_iPhoneSeed = phoneseed.
        _cShowScreen = this;
        _strArea = "ÉÏº£ÊÐ";
    }
    
    protected void sublayout( int width, int height ) 
    {
        //resetExtent( _CUSTOM_SIZE_W, _CUSTOM_SIZE_H ,_X, _Y );

        int w = getPreferredWidth();
        int h = getPreferredHeight();
        int x = (Const.SCREEN_WIDTH-w) >> 1;
        int y = (Const.SCREEN_HEIGHT-h) >> 1;
        /*if( _cActScreen != null )
        {
            try{
                   Field filed = _cActScreen.getLeafFieldWithFocus();
                
                   if( filed.isEditable() )
                   {
                        EditField editfiled = (EditField)filed;

                        int heigth = editfiled.getContentHeight();
                        int fontheight = editfiled.getFont().getHeight();
                        int top = editfiled.getTop()+editfiled.getManager().getTop();
                        
                        if( heigth+top > DxConst._SCREEN_HEIGHT - fontheight )
                            heigth = DxConst._SCREEN_HEIGHT - top;
                                
                        if( top + heigth > (DxConst._SCREEN_HEIGHT>>1) )
                           y = top + heigth - _CUSTOM_SIZE_H - fontheight - 5;
                        else
                           y = top + heigth + 5;  
                     }
            }
            catch( Exception ex ){
                 DxTools.OutputDebugString(ex.toString());
            }
        }*/

        setExtent( w, h );
        setPosition( x, y );
        layoutDelegate( w, h );
    }
    
    
    
    public int getPreferredWidth()
    {
        Utils.OutputDebugString( "getPreferredWidth" );
        return _strArea.length() >0?( _font.getAdvance(_strArea) + _IVL_W):_CUSTOM_SIZE_W;
    }
    
    public int getPreferredHeight()
    {
        Utils.OutputDebugString( "getPreferredHeight" );
        return _FONT_HEIGHT+_IVL_H;
    }   
    
    
    protected void paintBackground( Graphics g ) 
    {
        XYRect myExtent = getExtent();
        int color = g.getColor();
        //int alpha = g.getGlobalAlpha();
        //g.setBackgroundColor(16777215);
        g.clear();
        //g.setGlobalAlpha( _ALPHA );
        //g.setColor( _RGB_BORDER );
        //g.fillRoundRect( 0, 0, myExtent.width, myExtent.height,20,20 );
        //g.fillRect( 0, 0, myExtent.width, myExtent.height );
        //g.setColor( color );
        //g.setGlobalAlpha( alpha );
        g.setColor( _RGB_BACKGROUND );
        g.fillRoundRect( _IVL_W>>1, _IVL_H>>1, myExtent.width-_IVL_W, myExtent.height-_IVL_H, 10,10 );
        g.setColor( color ); 
    }
    
    protected void paint( Graphics g ) 
    {
         // XYRect myExtent = getExtent();
          int color = g.getColor();
          
          g.setColor( _RGB_FONT );
          
          if( _strArea.length() > 0 ){
            g.drawText( _strArea, _IVL_W>>1, _IVL_H>>1);
          }

          g.setColor( color );
    }
    
    /*
    protected boolean navigationClick(int status, int time)
    {
        
        return super.navigationClick(status,time);
    }
    */
    
    protected void onObscured() 
    {
        Utils.OutputDebugString("onObscured");
        invalidateMe();
    }  
   
    private void invalidateMe()
    {
            UiApplication.getUiApplication().invokeLater(new Runnable()
            {
                public void run()
                {
                // Add new fields to the mainscreen already pushed.
                    UiApplication.getUiApplication().popScreen(_cShowScreen);
                    UiApplication.getUiApplication().pushGlobalScreen(_cShowScreen,999,UiEngine.GLOBAL_QUEUE);
                }
            });   
    }
                    
    /*
    protected boolean keyDown(int keycode, int time) 
    {
        Utils.OutputDebugString("keyDown:" + keycode);
         //DxTools.OutputDebugString("status:" + status);
        int key = Keypad.key(keycode);
       
        switch(key)
        {
            case Keypad.KEY_ESCAPE:
            case Keypad.KEY_ENTER:
            {

                return true;
            }     
            case   Keypad.KEY_END:
            {
              
            }
            break;
            case  Keypad.KEY_SEND:
            {
              
            }
            break;
            default:
            {
            }
            break;
        }

        return super.keyDown( keycode, time );
    }
    */

    
    protected void onDisplay() 
    {

    }
    
} 
