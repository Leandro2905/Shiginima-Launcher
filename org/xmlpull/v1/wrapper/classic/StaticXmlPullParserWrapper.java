/*     */ package org.xmlpull.v1.wrapper.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.util.XmlPullUtil;
/*     */ import org.xmlpull.v1.wrapper.XmlPullParserWrapper;
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
/*     */ public class StaticXmlPullParserWrapper
/*     */   extends XmlPullParserDelegate
/*     */   implements XmlPullParserWrapper
/*     */ {
/*     */   public StaticXmlPullParserWrapper(XmlPullParser pp) {
/*  23 */     super(pp);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeValue(String name) {
/*  28 */     return XmlPullUtil.getAttributeValue(this.pp, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequiredAttributeValue(String name) throws IOException, XmlPullParserException {
/*  34 */     return XmlPullUtil.getRequiredAttributeValue(this.pp, null, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequiredAttributeValue(String namespace, String name) throws IOException, XmlPullParserException {
/*  41 */     return XmlPullUtil.getRequiredAttributeValue(this.pp, namespace, name);
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
/*     */   public String getRequiredElementText(String namespace, String name) throws IOException, XmlPullParserException {
/*  56 */     if (name == null) {
/*  57 */       throw new XmlPullParserException("name for element can not be null");
/*     */     }
/*     */     
/*  60 */     String text = null;
/*  61 */     nextStartTag(namespace, name);
/*  62 */     if (isNil()) {
/*  63 */       nextEndTag(namespace, name);
/*     */     } else {
/*     */       
/*  66 */       text = this.pp.nextText();
/*     */     } 
/*  68 */     this.pp.require(3, namespace, name);
/*  69 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNil() throws IOException, XmlPullParserException {
/*  76 */     boolean result = false;
/*  77 */     String value = this.pp.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
/*  78 */     if ("true".equals(value)) {
/*  79 */       result = true;
/*     */     }
/*     */     
/*  82 */     return result;
/*     */   }
/*     */   
/*     */   public String getPITarget() throws IllegalStateException {
/*  86 */     return XmlPullUtil.getPITarget(this.pp);
/*     */   }
/*     */   
/*     */   public String getPIData() throws IllegalStateException {
/*  90 */     return XmlPullUtil.getPIData(this.pp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(int type, String namespace, String name) throws XmlPullParserException {
/*  96 */     return XmlPullUtil.matches(this.pp, type, namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextStartTag() throws XmlPullParserException, IOException {
/* 102 */     if (this.pp.nextTag() != 2) {
/* 103 */       throw new XmlPullParserException("expected START_TAG and not " + this.pp.getPositionDescription());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextStartTag(String name) throws XmlPullParserException, IOException {
/* 111 */     this.pp.nextTag();
/* 112 */     this.pp.require(2, null, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextStartTag(String namespace, String name) throws XmlPullParserException, IOException {
/* 118 */     this.pp.nextTag();
/* 119 */     this.pp.require(2, namespace, name);
/*     */   }
/*     */   
/*     */   public void nextEndTag() throws XmlPullParserException, IOException {
/* 123 */     XmlPullUtil.nextEndTag(this.pp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextEndTag(String name) throws XmlPullParserException, IOException {
/* 129 */     XmlPullUtil.nextEndTag(this.pp, null, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextEndTag(String namespace, String name) throws XmlPullParserException, IOException {
/* 135 */     XmlPullUtil.nextEndTag(this.pp, namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String nextText(String namespace, String name) throws IOException, XmlPullParserException {
/* 141 */     return XmlPullUtil.nextText(this.pp, namespace, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void skipSubTree() throws XmlPullParserException, IOException {
/* 146 */     XmlPullUtil.skipSubTree(this.pp);
/*     */   }
/*     */   public double readDouble() throws XmlPullParserException, IOException {
/*     */     double d;
/* 150 */     String value = this.pp.nextText();
/*     */     
/*     */     try {
/* 153 */       d = Double.parseDouble(value);
/* 154 */     } catch (NumberFormatException ex) {
/* 155 */       if (value.equals("INF") || value.toLowerCase().equals("infinity")) {
/* 156 */         d = Double.POSITIVE_INFINITY;
/* 157 */       } else if (value.equals("-INF") || value.toLowerCase().equals("-infinity")) {
/*     */         
/* 159 */         d = Double.NEGATIVE_INFINITY;
/* 160 */       } else if (value.equals("NaN")) {
/* 161 */         d = Double.NaN;
/*     */       } else {
/* 163 */         throw new XmlPullParserException("can't parse double value '" + value + "'", this, ex);
/*     */       } 
/*     */     } 
/* 166 */     return d;
/*     */   }
/*     */   public float readFloat() throws XmlPullParserException, IOException {
/*     */     float f;
/* 170 */     String value = this.pp.nextText();
/*     */     
/*     */     try {
/* 173 */       f = Float.parseFloat(value);
/* 174 */     } catch (NumberFormatException ex) {
/* 175 */       if (value.equals("INF") || value.toLowerCase().equals("infinity")) {
/* 176 */         f = Float.POSITIVE_INFINITY;
/* 177 */       } else if (value.equals("-INF") || value.toLowerCase().equals("-infinity")) {
/*     */         
/* 179 */         f = Float.NEGATIVE_INFINITY;
/* 180 */       } else if (value.equals("NaN")) {
/* 181 */         f = Float.NaN;
/*     */       } else {
/* 183 */         throw new XmlPullParserException("can't parse float value '" + value + "'", this, ex);
/*     */       } 
/*     */     } 
/* 186 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int parseDigits(String text, int offset, int length) throws XmlPullParserException {
/* 195 */     int value = 0;
/* 196 */     if (length > 9) {
/*     */ 
/*     */       
/*     */       try {
/* 200 */         value = Integer.parseInt(text.substring(offset, offset + length));
/* 201 */       } catch (NumberFormatException ex) {
/* 202 */         throw new XmlPullParserException(ex.getMessage());
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 208 */       int limit = offset + length;
/* 209 */       while (offset < limit) {
/* 210 */         char chr = text.charAt(offset++);
/* 211 */         if (chr >= '0' && chr <= '9') {
/* 212 */           value = value * 10 + chr - 48; continue;
/*     */         } 
/* 214 */         throw new XmlPullParserException("non-digit in number value", this, null);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 219 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int parseInt(String text) throws XmlPullParserException {
/* 226 */     int offset = 0;
/* 227 */     int limit = text.length();
/* 228 */     if (limit == 0) {
/* 229 */       throw new XmlPullParserException("empty number value", this, null);
/*     */     }
/*     */ 
/*     */     
/* 233 */     boolean negate = false;
/* 234 */     char chr = text.charAt(0);
/* 235 */     if (chr == '-') {
/* 236 */       if (limit > 9) {
/*     */         
/*     */         try {
/*     */           
/* 240 */           return Integer.parseInt(text);
/* 241 */         } catch (NumberFormatException ex) {
/* 242 */           throw new XmlPullParserException(ex.getMessage(), this, null);
/*     */         } 
/*     */       }
/*     */       
/* 246 */       negate = true;
/* 247 */       offset++;
/*     */     }
/* 249 */     else if (chr == '+') {
/* 250 */       offset++;
/*     */     } 
/* 252 */     if (offset >= limit) {
/* 253 */       throw new XmlPullParserException("Invalid number format", this, null);
/*     */     }
/*     */ 
/*     */     
/* 257 */     int value = parseDigits(text, offset, limit - offset);
/* 258 */     if (negate) {
/* 259 */       return -value;
/*     */     }
/* 261 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readInt() throws XmlPullParserException, IOException {
/*     */     try {
/* 268 */       int i = parseInt(this.pp.nextText());
/* 269 */       return i;
/* 270 */     } catch (NumberFormatException ex) {
/* 271 */       throw new XmlPullParserException("can't parse int value", this, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String readString() throws XmlPullParserException, IOException {
/* 276 */     String xsiNil = this.pp.getAttributeValue("http://www.w3.org/2001/XMLSchema", "nil");
/* 277 */     if ("true".equals(xsiNil)) {
/* 278 */       nextEndTag();
/* 279 */       return null;
/*     */     } 
/* 281 */     return this.pp.nextText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double readDoubleElement(String namespace, String name) throws XmlPullParserException, IOException {
/* 287 */     this.pp.require(2, namespace, name);
/* 288 */     return readDouble();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float readFloatElement(String namespace, String name) throws XmlPullParserException, IOException {
/* 294 */     this.pp.require(2, namespace, name);
/* 295 */     return readFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readIntElement(String namespace, String name) throws XmlPullParserException, IOException {
/* 301 */     this.pp.require(2, namespace, name);
/* 302 */     return readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String readStringElemet(String namespace, String name) throws XmlPullParserException, IOException {
/* 308 */     this.pp.require(2, namespace, name);
/* 309 */     return readString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\classic\StaticXmlPullParserWrapper.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */