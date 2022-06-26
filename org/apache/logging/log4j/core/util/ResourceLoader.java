package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public interface ResourceLoader {
  Class<?> loadClass(String paramString) throws ClassNotFoundException;
  
  URL getResource(String paramString);
  
  Enumeration<URL> getResources(String paramString) throws IOException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\ResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */