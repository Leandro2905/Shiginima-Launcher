package org.apache.logging.log4j.status;

import java.io.Closeable;
import java.util.EventListener;
import org.apache.logging.log4j.Level;

public interface StatusListener extends Closeable, EventListener {
  void log(StatusData paramStatusData);
  
  Level getStatusLevel();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\status\StatusListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */