package org.apache.logging.log4j;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public interface Logger {
  void catching(Level paramLevel, Throwable paramThrowable);
  
  void catching(Throwable paramThrowable);
  
  void debug(Marker paramMarker, Message paramMessage);
  
  void debug(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void debug(Marker paramMarker, Object paramObject);
  
  void debug(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void debug(Marker paramMarker, String paramString);
  
  void debug(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void debug(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void debug(Message paramMessage);
  
  void debug(Message paramMessage, Throwable paramThrowable);
  
  void debug(Object paramObject);
  
  void debug(Object paramObject, Throwable paramThrowable);
  
  void debug(String paramString);
  
  void debug(String paramString, Object... paramVarArgs);
  
  void debug(String paramString, Throwable paramThrowable);
  
  void entry();
  
  void entry(Object... paramVarArgs);
  
  void error(Marker paramMarker, Message paramMessage);
  
  void error(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void error(Marker paramMarker, Object paramObject);
  
  void error(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void error(Marker paramMarker, String paramString);
  
  void error(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void error(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void error(Message paramMessage);
  
  void error(Message paramMessage, Throwable paramThrowable);
  
  void error(Object paramObject);
  
  void error(Object paramObject, Throwable paramThrowable);
  
  void error(String paramString);
  
  void error(String paramString, Object... paramVarArgs);
  
  void error(String paramString, Throwable paramThrowable);
  
  void exit();
  
  <R> R exit(R paramR);
  
  void fatal(Marker paramMarker, Message paramMessage);
  
  void fatal(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, Object paramObject);
  
  void fatal(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, String paramString);
  
  void fatal(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void fatal(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void fatal(Message paramMessage);
  
  void fatal(Message paramMessage, Throwable paramThrowable);
  
  void fatal(Object paramObject);
  
  void fatal(Object paramObject, Throwable paramThrowable);
  
  void fatal(String paramString);
  
  void fatal(String paramString, Object... paramVarArgs);
  
  void fatal(String paramString, Throwable paramThrowable);
  
  Level getLevel();
  
  MessageFactory getMessageFactory();
  
  String getName();
  
  void info(Marker paramMarker, Message paramMessage);
  
  void info(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void info(Marker paramMarker, Object paramObject);
  
  void info(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void info(Marker paramMarker, String paramString);
  
  void info(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void info(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void info(Message paramMessage);
  
  void info(Message paramMessage, Throwable paramThrowable);
  
  void info(Object paramObject);
  
  void info(Object paramObject, Throwable paramThrowable);
  
  void info(String paramString);
  
  void info(String paramString, Object... paramVarArgs);
  
  void info(String paramString, Throwable paramThrowable);
  
  boolean isDebugEnabled();
  
  boolean isDebugEnabled(Marker paramMarker);
  
  boolean isEnabled(Level paramLevel);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker);
  
  boolean isErrorEnabled();
  
  boolean isErrorEnabled(Marker paramMarker);
  
  boolean isFatalEnabled();
  
  boolean isFatalEnabled(Marker paramMarker);
  
  boolean isInfoEnabled();
  
  boolean isInfoEnabled(Marker paramMarker);
  
  boolean isTraceEnabled();
  
  boolean isTraceEnabled(Marker paramMarker);
  
  boolean isWarnEnabled();
  
  boolean isWarnEnabled(Marker paramMarker);
  
  void log(Level paramLevel, Marker paramMarker, Message paramMessage);
  
  void log(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, Object paramObject);
  
  void log(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, String paramString);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void log(Level paramLevel, Message paramMessage);
  
  void log(Level paramLevel, Message paramMessage, Throwable paramThrowable);
  
  void log(Level paramLevel, Object paramObject);
  
  void log(Level paramLevel, Object paramObject, Throwable paramThrowable);
  
  void log(Level paramLevel, String paramString);
  
  void log(Level paramLevel, String paramString, Object... paramVarArgs);
  
  void log(Level paramLevel, String paramString, Throwable paramThrowable);
  
  void printf(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void printf(Level paramLevel, String paramString, Object... paramVarArgs);
  
  <T extends Throwable> T throwing(Level paramLevel, T paramT);
  
  <T extends Throwable> T throwing(T paramT);
  
  void trace(Marker paramMarker, Message paramMessage);
  
  void trace(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void trace(Marker paramMarker, Object paramObject);
  
  void trace(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void trace(Marker paramMarker, String paramString);
  
  void trace(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void trace(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void trace(Message paramMessage);
  
  void trace(Message paramMessage, Throwable paramThrowable);
  
  void trace(Object paramObject);
  
  void trace(Object paramObject, Throwable paramThrowable);
  
  void trace(String paramString);
  
  void trace(String paramString, Object... paramVarArgs);
  
  void trace(String paramString, Throwable paramThrowable);
  
  void warn(Marker paramMarker, Message paramMessage);
  
  void warn(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void warn(Marker paramMarker, Object paramObject);
  
  void warn(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void warn(Marker paramMarker, String paramString);
  
  void warn(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void warn(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void warn(Message paramMessage);
  
  void warn(Message paramMessage, Throwable paramThrowable);
  
  void warn(Object paramObject);
  
  void warn(Object paramObject, Throwable paramThrowable);
  
  void warn(String paramString);
  
  void warn(String paramString, Object... paramVarArgs);
  
  void warn(String paramString, Throwable paramThrowable);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */