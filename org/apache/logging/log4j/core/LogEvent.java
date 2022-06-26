package org.apache.logging.log4j.core;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;

public interface LogEvent extends Serializable {
  Map<String, String> getContextMap();
  
  ThreadContext.ContextStack getContextStack();
  
  String getLoggerFqcn();
  
  Level getLevel();
  
  String getLoggerName();
  
  Marker getMarker();
  
  Message getMessage();
  
  long getTimeMillis();
  
  StackTraceElement getSource();
  
  String getThreadName();
  
  Throwable getThrown();
  
  ThrowableProxy getThrownProxy();
  
  boolean isEndOfBatch();
  
  boolean isIncludeLocation();
  
  void setEndOfBatch(boolean paramBoolean);
  
  void setIncludeLocation(boolean paramBoolean);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\LogEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */