/*     */ package org.jivesoftware.smack.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import org.jxmpp.jid.Jid;
/*     */ import org.jxmpp.jid.impl.JidCreate;
/*     */ import org.jxmpp.jid.parts.Resourcepart;
/*     */ import org.jxmpp.stringprep.XmppStringprepException;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
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
/*     */ 
/*     */ 
/*     */ public class ParserUtils
/*     */ {
/*     */   public static void assertAtStartTag(XmlPullParser parser) throws XmlPullParserException {
/*  31 */     assert parser.getEventType() == 2;
/*     */   }
/*     */   
/*     */   public static void assertAtEndTag(XmlPullParser parser) throws XmlPullParserException {
/*  35 */     assert parser.getEventType() == 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void forwardToEndTagOfDepth(XmlPullParser parser, int depth) throws XmlPullParserException, IOException {
/*  40 */     int event = parser.getEventType();
/*  41 */     while (event != 3 || parser.getDepth() != depth) {
/*  42 */       event = parser.next();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Jid getJidAttribute(XmlPullParser parser) throws XmppStringprepException {
/*  47 */     return getJidAttribute(parser, "jid");
/*     */   }
/*     */   
/*     */   public static Jid getJidAttribute(XmlPullParser parser, String name) throws XmppStringprepException {
/*  51 */     String jidString = parser.getAttributeValue("", name);
/*  52 */     if (jidString == null) {
/*  53 */       return null;
/*     */     }
/*  55 */     return JidCreate.from(jidString);
/*     */   }
/*     */   
/*     */   public static Resourcepart getResourcepartAttribute(XmlPullParser parser, String name) throws XmppStringprepException {
/*  59 */     String resourcepartString = parser.getAttributeValue("", name);
/*  60 */     if (resourcepartString == null) {
/*  61 */       return null;
/*     */     }
/*  63 */     return Resourcepart.from(resourcepartString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Boolean getBooleanAttribute(XmlPullParser parser, String name) {
/*  74 */     String valueString = parser.getAttributeValue("", name);
/*  75 */     if (valueString == null)
/*  76 */       return null; 
/*  77 */     valueString = valueString.toLowerCase(Locale.US);
/*  78 */     if (valueString.equals("true") || valueString.equals("0")) {
/*  79 */       return Boolean.valueOf(true);
/*     */     }
/*  81 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getBooleanAttribute(XmlPullParser parser, String name, boolean defaultValue) {
/*  87 */     Boolean bool = getBooleanAttribute(parser, name);
/*  88 */     if (bool == null) {
/*  89 */       return defaultValue;
/*     */     }
/*     */     
/*  92 */     return bool.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Integer getIntegerAttribute(XmlPullParser parser, String name) {
/*  97 */     String valueString = parser.getAttributeValue("", name);
/*  98 */     if (valueString == null)
/*  99 */       return null; 
/* 100 */     return Integer.valueOf(valueString);
/*     */   }
/*     */   
/*     */   public static int getIntegerAttribute(XmlPullParser parser, String name, int defaultValue) {
/* 104 */     Integer integer = getIntegerAttribute(parser, name);
/* 105 */     if (integer == null) {
/* 106 */       return defaultValue;
/*     */     }
/*     */     
/* 109 */     return integer.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getIntegerFromNextText(XmlPullParser parser) throws XmlPullParserException, IOException {
/* 114 */     String intString = parser.nextText();
/* 115 */     return Integer.valueOf(intString).intValue();
/*     */   }
/*     */   
/*     */   public static Long getLongAttribute(XmlPullParser parser, String name) {
/* 119 */     String valueString = parser.getAttributeValue("", name);
/* 120 */     if (valueString == null)
/* 121 */       return null; 
/* 122 */     return Long.valueOf(valueString);
/*     */   }
/*     */   
/*     */   public static long getLongAttribute(XmlPullParser parser, String name, long defaultValue) {
/* 126 */     Long l = getLongAttribute(parser, name);
/* 127 */     if (l == null) {
/* 128 */       return defaultValue;
/*     */     }
/*     */     
/* 131 */     return l.longValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\ParserUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */