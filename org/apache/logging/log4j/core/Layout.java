package org.apache.logging.log4j.core;

import java.util.Map;

public interface Layout<T extends java.io.Serializable> {
  byte[] getFooter();
  
  byte[] getHeader();
  
  byte[] toByteArray(LogEvent paramLogEvent);
  
  T toSerializable(LogEvent paramLogEvent);
  
  String getContentType();
  
  Map<String, String> getContentFormat();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\Layout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */