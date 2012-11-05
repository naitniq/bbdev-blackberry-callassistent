package com.callasst.findlocation;


public final class  ShowUI{
    

    private final static int _CUSTOM_SIZE_W   = 190;
    private final static int _CUSTOM_SIZE_H   = 80;

    private final static int _ALPHA    = 0xA0; // 60% alpha
    private final static int _X     = ( DxConst._SCREEN_WIDTH - _CUSTOM_SIZE_W ) >> 1;
    private final static int _Y     = ( DxConst._SCREEN_HEIGHT - _CUSTOM_SIZE_H ) >> 1; 
    private final static int _IVL_W = 8;
    private final static int _IVL_H = 8;
    private final static int _RGB_BORDER = 0xA8A8A8;
    private final static int _RGB_BACKGROUND = 0xFFFFFF;  
    private final static int _RGB_FONT_HLIGHT_BG = 0xA8A8A8;
    private final static int _RGB_FONT_HLIGHT = 0xFFFFFF;
    private final static int _RGB_FONT_NOR = 0x000000;  
    private final static int _RGB_LINE = 0xA8A8A8;
    private final static int _RGB_LEFT_ARROW = 0x008800;
    private final static int _RGB_RIGHT_ARROW = 0xAA0000;
    private final static int _RGB_TIPS_SYM = 0xA8A8A8;
    private final static int _FONT_HEIGHT = Font.getDefault().getHeight();
    private final static int _MAX_EN_KEY = 14;
    
    private Screen _cActScreen;
    private DxStatusScreen _cStatusScreen;
    private boolean _bNoError = true;
    private boolean _bShowText = false;
    private  StringBuffer _strEnKey;
    //private  StringBuffer _strChKey;
    private DxDataString _cStrChKey;
    private int _iSelIndex;
    private DxData _cDxData;
    
    private final static int _ARROW_NONE   = 0;
    private final static int _ARROW_LEFT   = 1;
    private final static int _ARROW_RIGHT  = 2;  
    private final static int _ARROW_UP     = 4;
    private final static int _ARROW_DOWN   = 8;      
    //private final int _ARROW_ALL    = 15;  
    //private int _iShowArrow = _ARROW_NONE;
    private static final Bitmap _imgSYM0 =
           Bitmap.getBitmapResource("SYM0.png");
    private static final Bitmap _imgSYM1 =
           Bitmap.getBitmapResource("SYM1.png");
           
    protected DxInputUIScreen _cInputScreen;
    
    
    ShowUI(Screen activescreen,DxStatusScreen statusscreen)
    {    
        super( new VerticalFieldManager( Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR ) );
        _cActScreen = activescreen;
        _cStatusScreen = statusscreen;
        _cInputScreen = this;
        _strEnKey = new StringBuffer();
        //_strChKey = new StringBuffer();
        _cStrChKey = new DxDataString( DxData._BLOCK_DATA_NUMBER );
        _cDxData = new DxData();
        _iSelIndex = _cDxData.getDefaultPos();
    }
    
    protected void sublayout( int width, int height ) 
    {
        //resetExtent( _CUSTOM_SIZE_W, _CUSTOM_SIZE_H ,_X, _Y );
        DxTools.OutputDebugString( "sublayout" );
        int x = _X;
        int y = _Y;
        
        if( _cActScreen != null )
        {
            try{
                   Field filed = _cActScreen.getLeafFieldWithFocus();
                
                   if( filed.isEditable() )
                   {
                        EditField editfiled = (EditField)filed;
                                /*DxTools.OutputDebugString("EditField getHeight:" + editfiled.getHeight());
                                DxTools.OutputDebugString("EditField getContentHeight:" + editfiled.getContentHeight());
                                DxTools.OutputDebugString("EditField getCursorPosition:" + editfiled.getCursorPosition());
                                DxTools.OutputDebugString("EditField getTop:" + editfiled.getTop());
                                DxTools.OutputDebugString("EditField getContentTop:" + editfiled.getContentTop());
                                DxTools.OutputDebugString("EditField getContentRect:" + editfiled.getContentRect().height);
                                DxTools.OutputDebugString("EditField X:" + editfiled.getContentRect().x);
                                DxTools.OutputDebugString("EditField Y:" + editfiled.getContentRect().y);
                                DxTools.OutputDebugString("Manager TOP:" + editfiled.getManager().getTop());
                                DxTools.OutputDebugString("_cActScreen getExtent:" + _cActScreen.getExtent().height);
                                DxTools.OutputDebugString("_cActScreen getExtent:" + _cActScreen.getExtent().height);*/
                        
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
        }

        setExtent( getPreferredWidth(), getPreferredHeight() );
        setPosition( x, y );
        layoutDelegate( getPreferredWidth(), getPreferredHeight() );
    }
    
    
    
    public int getPreferredWidth()
    {
        DxTools.OutputDebugString( "getPreferredWidth" );
        return _cStrChKey.length>0?( _cStrChKey.getAllWidth() + _IVL_W + 12 ):_CUSTOM_SIZE_W;
    }
    
    public int getPreferredHeight()
    {
        DxTools.OutputDebugString( "getPreferredHeight" );
        return _CUSTOM_SIZE_H;
    }   
    
    
    protected void paintBackground( Graphics g ) 
    {
        XYRect myExtent = getExtent();
        int color = g.getColor();
        //int alpha = g.getGlobalAlpha();
        //g.setBackgroundColor(16777215);
        g.clear();
        //g.setGlobalAlpha( _ALPHA );
        g.setColor( _RGB_BORDER );
        g.fillRoundRect( 0, 0, myExtent.width, myExtent.height,20,20 );
        //g.fillRect( 0, 0, myExtent.width, myExtent.height );
        //g.setColor( color );
        //g.setGlobalAlpha( alpha );
        g.setColor( _RGB_BACKGROUND );
        g.fillRoundRect( _IVL_W>>1, _IVL_H>>1, myExtent.width-_IVL_W, myExtent.height-_IVL_H, 10,10 );
        g.setColor( color ); 
    }
    
    protected void paint( Graphics g ) 
    {
          XYRect myExtent = getExtent();
          int color = g.getColor();
          int fontwidth = 0;
          int lineheight= myExtent.height/5*2;
          //g.clear(); 
          g.setColor(_RGB_LINE);
          //g.drawLine( (_IVL_W>>1)+2, _CUSTOM_SIZE_H/3-1 ,_CUSTOM_SIZE_W-(_IVL_W>>1)-2 , _CUSTOM_SIZE_H/3-1 );
          g.drawLine( (_IVL_W>>1)+2, lineheight ,myExtent.width-(_IVL_W>>1)-2 , lineheight  );
          //g.drawLine( (_IVL_W>>1), _CUSTOM_SIZE_H/3+1 ,_CUSTOM_SIZE_W-(_IVL_W>>1) , _CUSTOM_SIZE_H/3+1 );
          
          //draw english text
          g.setColor( _RGB_FONT_NOR );
          g.drawText( _strEnKey.toString(), (_IVL_W>>1)+18, (_IVL_H>>1)+4 );
          
          //draw chinese text
          if( _cStrChKey.length > 0 )
          {
            //fontwidth = javax.microedition.lcdui.Font.getDefaultFont().stringWidth(_strChKey.toString())/_strChKey.length();
            //Font f= new Font();

            //if( fontwidth > 0 )
            //{

            //}
            //g.setColor( _RGB_FONT_NOR );
            g.drawText( _cStrChKey.toString(), (_IVL_W>>1)+8, lineheight + (_IVL_H>>1) );
            g.setColor( _RGB_FONT_HLIGHT_BG );
            g.fillRoundRect( (_IVL_W>>1) + _cStrChKey.getPerWidthByIndex(_iSelIndex) + 8 ,lineheight + (_IVL_H>>1) - 2,
                        _cStrChKey.getWidthByIndex(_iSelIndex)+4,_FONT_HEIGHT+4 ,10,10);
            g.setColor( _RGB_FONT_HLIGHT );
            g.drawText( _cStrChKey.getStringByIndex(_iSelIndex),
                        (_IVL_W>>1)+8+_cStrChKey.getPerWidthByIndex(_iSelIndex),lineheight + (_IVL_H>>1));
          }
          
          //draw symbol
          int pos = ( myExtent.width - _IVL_W )/5;
          paintTriangle(g, (_IVL_W>>1) + 10,myExtent.height - (_IVL_H>>1) - 2,8,8,_ARROW_UP,_RGB_TIPS_SYM);
          
          g.drawBitmap((_IVL_W>>1) + _cStrChKey.getPerWidthByIndex(1)+12,myExtent.height - (_IVL_H>>1) - 13,
                                _imgSYM0.getWidth(),_imgSYM0.getHeight(),_imgSYM0,0,0);
          g.drawBitmap((_IVL_W>>1) + _cStrChKey.getPerWidthByIndex(3)+10,myExtent.height - (_IVL_H>>1) - 11,
                                _imgSYM1.getWidth(),_imgSYM1.getHeight(),_imgSYM1,0,0); 
                                                     
          paintTriangle(g, myExtent.width - (_IVL_W>>1) - 10,myExtent.height - (_IVL_H>>1) - 2,8,8,_ARROW_UP,_RGB_TIPS_SYM);
          
          //draw arrow
          paintArrow(g,myExtent.width);
          
          g.setColor( color );
          
          //super.paint(g);
    }
    
    protected boolean navigationClick(int status, int time)
    {
        
        return super.navigationClick(status,time);
    }
                      
    protected boolean keyDown(int keycode, int time) 
    {
        Util.OutputDebugString("keyDown:" + keycode);
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
    

    
    protected void onDisplay() 
    {

    }
    
} 
