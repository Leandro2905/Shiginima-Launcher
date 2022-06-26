package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.core.LogEventListener;

public interface LogEventBridge<T extends InputStream> {
  void logEvents(T paramT, LogEventListener paramLogEventListener) throws IOException;
  
  T wrapStream(InputStream paramInputStream) throws IOException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\LogEventBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */