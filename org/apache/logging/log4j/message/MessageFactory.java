package org.apache.logging.log4j.message;

public interface MessageFactory {
  Message newMessage(Object paramObject);
  
  Message newMessage(String paramString);
  
  Message newMessage(String paramString, Object... paramVarArgs);
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\MessageFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */