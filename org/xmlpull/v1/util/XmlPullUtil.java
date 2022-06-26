/*     */ package org.xmlpull.v1.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlPullUtil
/*     */ {
/*     */   public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
/*     */   
/*     */   public static String getAttributeValue(XmlPullParser pp, String name) {
/*  27 */     return pp.getAttributeValue("", name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPITarget(XmlPullParser pp) throws IllegalStateException {
/*     */     int eventType;
/*     */     try {
/*  40 */       eventType = pp.getEventType();
/*  41 */     } catch (XmlPullParserException ex) {
/*     */       
/*  43 */       throw new IllegalStateException("could not determine parser state: " + ex + pp.getPositionDescription());
/*     */     } 
/*     */     
/*  46 */     if (eventType != 8) {
/*  47 */       throw new IllegalStateException("parser must be on processing instruction and not " + XmlPullParser.TYPES[eventType] + pp.getPositionDescription());
/*     */     }
/*     */ 
/*     */     
/*  51 */     String PI = pp.getText();
/*  52 */     for (int i = 0; i < PI.length(); i++) {
/*     */       
/*  54 */       if (isS(PI.charAt(i)))
/*     */       {
/*  56 */         return PI.substring(0, i);
/*     */       }
/*     */     } 
/*  59 */     return PI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPIData(XmlPullParser pp) throws IllegalStateException {
/*     */     int eventType;
/*     */     try {
/*  73 */       eventType = pp.getEventType();
/*  74 */     } catch (XmlPullParserException ex) {
/*     */       
/*  76 */       throw new IllegalStateException("could not determine parser state: " + ex + pp.getPositionDescription());
/*     */     } 
/*     */     
/*  79 */     if (eventType != 8) {
/*  80 */       throw new IllegalStateException("parser must be on processing instruction and not " + XmlPullParser.TYPES[eventType] + pp.getPositionDescription());
/*     */     }
/*     */ 
/*     */     
/*  84 */     String PI = pp.getText();
/*  85 */     int pos = -1;
/*  86 */     for (int i = 0; i < PI.length(); i++) {
/*     */       
/*  88 */       if (isS(PI.charAt(i))) {
/*  89 */         pos = i;
/*  90 */       } else if (pos > 0) {
/*  91 */         return PI.substring(i);
/*     */       } 
/*     */     } 
/*  94 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isS(char ch) {
/* 103 */     return (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void skipSubTree(XmlPullParser pp) throws XmlPullParserException, IOException {
/* 114 */     pp.require(2, null, null);
/* 115 */     int level = 1;
/* 116 */     while (level > 0) {
/* 117 */       int eventType = pp.next();
/* 118 */       if (eventType == 3) {
/* 119 */         level--; continue;
/* 120 */       }  if (eventType == 2) {
/* 121 */         level++;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void nextStartTag(XmlPullParser pp) throws XmlPullParserException, IOException {
/* 132 */     if (pp.nextTag() != 2) {
/* 133 */       throw new XmlPullParserException("expected START_TAG and not " + pp.getPositionDescription());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void nextStartTag(XmlPullParser pp, String name) throws XmlPullParserException, IOException {
/* 144 */     pp.nextTag();
/* 145 */     pp.require(2, null, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void nextStartTag(XmlPullParser pp, String namespace, String name) throws XmlPullParserException, IOException {
/* 154 */     pp.nextTag();
/* 155 */     pp.require(2, namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void nextEndTag(XmlPullParser pp, String namespace, String name) throws XmlPullParserException, IOException {
/* 167 */     pp.nextTag();
/* 168 */     pp.require(3, namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nextText(XmlPullParser pp, String namespace, String name) throws IOException, XmlPullParserException {
/* 180 */     if (name == null) {
/* 181 */       throw new XmlPullParserException("name for element can not be null");
/*     */     }
/* 183 */     pp.require(2, namespace, name);
/* 184 */     return pp.nextText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRequiredAttributeValue(XmlPullParser pp, String namespace, String name) throws IOException, XmlPullParserException {
/* 195 */     String value = pp.getAttributeValue(namespace, name);
/* 196 */     if (value == null) {
/* 197 */       throw new XmlPullParserException("required attribute " + name + " is not present");
/*     */     }
/* 199 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void nextEndTag(XmlPullParser pp) throws XmlPullParserException, IOException {
/* 208 */     if (pp.nextTag() != 3) {
/* 209 */       throw new XmlPullParserException("expected END_TAG and not" + pp.getPositionDescription());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matches(XmlPullParser pp, int type, String namespace, String name) throws XmlPullParserException {
/* 223 */     boolean matches = (type == pp.getEventType() && (namespace == null || namespace.equals(pp.getNamespace())) && (name == null || name.equals(pp.getName())));
/*     */ 
/*     */ 
/*     */     
/* 227 */     return matches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSimpleElement(XmlSerializer serializer, String namespace, String elementName, String elementText) throws IOException, XmlPullParserException {
/* 242 */     if (elementName == null) {
/* 243 */       throw new XmlPullParserException("name for element can not be null");
/*     */     }
/*     */     
/* 246 */     serializer.startTag(namespace, elementName);
/* 247 */     if (elementText == null) {
/* 248 */       serializer.attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
/*     */     } else {
/* 250 */       serializer.text(elementText);
/*     */     } 
/* 252 */     serializer.endTag(namespace, elementName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v\\util\XmlPullUtil.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */