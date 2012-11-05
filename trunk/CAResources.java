package com.callasst.res;

import net.rim.device.api.i18n.*;


/**
 * Generated resource handling helper
 * (c) Research In Motion Limited, 2003-2004. Confidential and proprietary.
 */
 public final class CAResources
 {

  private static ResourceBundle _strings = ResourceBundle.getBundle(CallAssistantResource.BUNDLE_ID, CallAssistantResource.BUNDLE_NAME);

  private CAResources() { }
  
 /**
  * Get a string, given an id.
  *
  * @param id The id of the string.
  * @return The String corresponding to the given id.
  */
  public static String getString( int id )
  {
      return _strings.getString( id );
  }

 /**
  * Get a string array, given an id.
  *
  * @param id The id of the string array.
  * @return The String[] corresponding to the given id.
  */
  public static String[] getStringArray(int id)
  {
      return _strings.getStringArray( id );
  }

}
