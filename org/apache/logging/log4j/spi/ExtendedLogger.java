package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

public interface ExtendedLogger extends Logger {
  boolean isEnabled(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void logIfEnabled(String paramString, Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Throwable paramThrowable);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2);
  
  void logIfEnabled(String paramString1, Level paramLevel, Marker paramMarker, String paramString2, Object... paramVarArgs);
  
  void logMessage(String paramString, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\ExtendedLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */