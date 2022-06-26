package org.apache.logging.log4j;

import java.io.Serializable;

public interface Marker extends Serializable {
  String getName();
  
  Marker[] getParents();
  
  boolean hasParents();
  
  boolean isInstanceOf(Marker paramMarker);
  
  boolean isInstanceOf(String paramString);
  
  Marker addParents(Marker... paramVarArgs);
  
  Marker setParents(Marker... paramVarArgs);
  
  boolean remove(Marker paramMarker);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\Marker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */