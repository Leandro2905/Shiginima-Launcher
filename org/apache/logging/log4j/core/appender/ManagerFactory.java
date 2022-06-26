package org.apache.logging.log4j.core.appender;

public interface ManagerFactory<M, T> {
  M createManager(String paramString, T paramT);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\ManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */