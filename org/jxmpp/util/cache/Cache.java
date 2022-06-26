package org.jxmpp.util.cache;

public interface Cache<K, V> {
  V put(K paramK, V paramV);
  
  V get(Object paramObject);
  
  int getMaxCacheSize();
  
  void setMaxCacheSize(int paramInt);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jxmp\\util\cache\Cache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */