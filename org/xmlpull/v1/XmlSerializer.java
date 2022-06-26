package org.xmlpull.v1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public interface XmlSerializer {
  void setFeature(String paramString, boolean paramBoolean) throws IllegalArgumentException, IllegalStateException;
  
  boolean getFeature(String paramString);
  
  void setProperty(String paramString, Object paramObject) throws IllegalArgumentException, IllegalStateException;
  
  Object getProperty(String paramString);
  
  void setOutput(OutputStream paramOutputStream, String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void setOutput(Writer paramWriter) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void startDocument(String paramString, Boolean paramBoolean) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void endDocument() throws IOException, IllegalArgumentException, IllegalStateException;
  
  void setPrefix(String paramString1, String paramString2) throws IOException, IllegalArgumentException, IllegalStateException;
  
  String getPrefix(String paramString, boolean paramBoolean) throws IllegalArgumentException;
  
  int getDepth();
  
  String getNamespace();
  
  String getName();
  
  XmlSerializer startTag(String paramString1, String paramString2) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializer attribute(String paramString1, String paramString2, String paramString3) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializer endTag(String paramString1, String paramString2) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializer text(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializer text(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void cdsect(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void entityRef(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void processingInstruction(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void comment(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void docdecl(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void ignorableWhitespace(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void flush() throws IOException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\XmlSerializer.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */