package org.xmlpull.v1.wrapper;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public interface XmlSerializerWrapper extends XmlSerializer {
  public static final String NO_NAMESPACE = "";
  
  public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
  
  public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
  
  String getCurrentNamespaceForElements();
  
  String setCurrentNamespaceForElements(String paramString);
  
  XmlSerializerWrapper attribute(String paramString1, String paramString2) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializerWrapper startTag(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializerWrapper endTag(String paramString) throws IOException, IllegalArgumentException, IllegalStateException;
  
  XmlSerializerWrapper element(String paramString1, String paramString2, String paramString3) throws IOException, XmlPullParserException;
  
  XmlSerializerWrapper element(String paramString1, String paramString2) throws IOException, XmlPullParserException;
  
  void fragment(String paramString) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException;
  
  void event(XmlPullParser paramXmlPullParser) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException;
  
  String escapeText(String paramString) throws IllegalArgumentException;
  
  String escapeAttributeValue(String paramString) throws IllegalArgumentException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\XmlSerializerWrapper.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */