package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ThreadContextMap {
  void put(String paramString1, String paramString2);
  
  String get(String paramString);
  
  void remove(String paramString);
  
  void clear();
  
  boolean containsKey(String paramString);
  
  Map<String, String> getCopy();
  
  Map<String, String> getImmutableMapOrNull();
  
  boolean isEmpty();
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\spi\ThreadContextMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */