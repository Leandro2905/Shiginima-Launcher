package org.xmlpull.v1.wrapper;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public interface XmlPullParserWrapper extends XmlPullParser {
  public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
  
  public static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
  
  String getAttributeValue(String paramString);
  
  String getPITarget() throws IllegalStateException;
  
  String getPIData() throws IllegalStateException;
  
  String getRequiredAttributeValue(String paramString) throws IOException, XmlPullParserException;
  
  String getRequiredAttributeValue(String paramString1, String paramString2) throws IOException, XmlPullParserException;
  
  String getRequiredElementText(String paramString1, String paramString2) throws IOException, XmlPullParserException;
  
  boolean isNil() throws IOException, XmlPullParserException;
  
  boolean matches(int paramInt, String paramString1, String paramString2) throws XmlPullParserException;
  
  void nextStartTag() throws XmlPullParserException, IOException;
  
  void nextStartTag(String paramString) throws XmlPullParserException, IOException;
  
  void nextStartTag(String paramString1, String paramString2) throws XmlPullParserException, IOException;
  
  void nextEndTag() throws XmlPullParserException, IOException;
  
  void nextEndTag(String paramString) throws XmlPullParserException, IOException;
  
  void nextEndTag(String paramString1, String paramString2) throws XmlPullParserException, IOException;
  
  String nextText(String paramString1, String paramString2) throws IOException, XmlPullParserException;
  
  void skipSubTree() throws XmlPullParserException, IOException;
}


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\XmlPullParserWrapper.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */