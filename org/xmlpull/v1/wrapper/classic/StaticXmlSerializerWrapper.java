/*     */ package org.xmlpull.v1.wrapper.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlSerializer;
/*     */ import org.xmlpull.v1.wrapper.XmlPullParserWrapper;
/*     */ import org.xmlpull.v1.wrapper.XmlPullWrapperFactory;
/*     */ import org.xmlpull.v1.wrapper.XmlSerializerWrapper;
/*     */ 
/*     */ public class StaticXmlSerializerWrapper extends XmlSerializerDelegate implements XmlSerializerWrapper {
/*     */   private static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/features.html#xmldecl-standalone";
/*     */   private static final boolean TRACE_SIZING = false;
/*     */   protected String currentNs;
/*     */   protected XmlPullWrapperFactory wf;
/*     */   protected XmlPullParserWrapper fragmentParser;
/*     */   protected int namespaceEnd;
/*     */   protected String[] namespacePrefix;
/*     */   protected String[] namespaceUri;
/*     */   protected int[] namespaceDepth;
/*     */   
/*     */   public String getCurrentNamespaceForElements() {
/*     */     return this.currentNs;
/*     */   }
/*     */   
/*     */   public String setCurrentNamespaceForElements(String value) {
/*     */     String old = this.currentNs;
/*     */     this.currentNs = value;
/*     */     return old;
/*     */   }
/*     */   
/*  33 */   public StaticXmlSerializerWrapper(XmlSerializer xs, XmlPullWrapperFactory wf) { super(xs);
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
/*  98 */     this.namespaceEnd = 0;
/*  99 */     this.namespacePrefix = new String[8];
/* 100 */     this.namespaceUri = new String[this.namespacePrefix.length];
/* 101 */     this.namespaceDepth = new int[this.namespacePrefix.length];
/*     */     this.wf = wf; } public XmlSerializerWrapper attribute(String name, String value) throws IOException, IllegalArgumentException, IllegalStateException { this.xs.attribute(null, name, value);
/*     */     return this; }
/* 104 */   private void ensureNamespacesCapacity() { int newSize = (this.namespaceEnd > 7) ? (2 * this.namespaceEnd) : 8;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     String[] newNamespacePrefix = new String[newSize];
/* 110 */     String[] newNamespaceUri = new String[newSize];
/* 111 */     int[] newNamespaceDepth = new int[newSize];
/* 112 */     if (this.namespacePrefix != null) {
/* 113 */       System.arraycopy(this.namespacePrefix, 0, newNamespacePrefix, 0, this.namespaceEnd);
/* 114 */       System.arraycopy(this.namespaceUri, 0, newNamespaceUri, 0, this.namespaceEnd);
/* 115 */       System.arraycopy(this.namespaceDepth, 0, newNamespaceDepth, 0, this.namespaceEnd);
/*     */     } 
/* 117 */     this.namespacePrefix = newNamespacePrefix;
/* 118 */     this.namespaceUri = newNamespaceUri;
/* 119 */     this.namespaceDepth = newNamespaceDepth; }
/*     */   
/*     */   public XmlSerializerWrapper startTag(String name) throws IOException, IllegalArgumentException, IllegalStateException {
/*     */     this.xs.startTag(this.currentNs, name);
/*     */     return this;
/*     */   }
/*     */   public XmlSerializerWrapper endTag(String name) throws IOException, IllegalArgumentException, IllegalStateException {
/*     */     endTag(this.currentNs, name);
/*     */     return this;
/*     */   }
/*     */   public XmlSerializerWrapper element(String elementName, String elementText) throws IOException, XmlPullParserException {
/*     */     return element(this.currentNs, elementName, elementText);
/*     */   }
/* 132 */   public void setPrefix(String prefix, String namespace) throws IOException, IllegalArgumentException, IllegalStateException { this.xs.setPrefix(prefix, namespace);
/*     */     
/* 134 */     int depth = getDepth();
/* 135 */     for (int pos = this.namespaceEnd - 1; pos >= 0 && 
/* 136 */       this.namespaceDepth[pos] > depth; pos--)
/*     */     {
/*     */       
/* 139 */       this.namespaceEnd--;
/*     */     }
/*     */     
/* 142 */     if (this.namespaceEnd >= this.namespacePrefix.length) {
/* 143 */       ensureNamespacesCapacity();
/*     */     }
/* 145 */     this.namespacePrefix[this.namespaceEnd] = prefix;
/* 146 */     this.namespaceUri[this.namespaceEnd] = namespace;
/* 147 */     this.namespaceEnd++; }
/*     */   public XmlSerializerWrapper element(String namespace, String elementName, String elementText) throws IOException, XmlPullParserException { if (elementName == null)
/*     */       throw new XmlPullParserException("name for element can not be null");  this.xs.startTag(namespace, elementName); if (elementText == null) {
/*     */       this.xs.attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
/*     */     } else {
/*     */       this.xs.text(elementText);
/*     */     }  this.xs.endTag(namespace, elementName);
/* 154 */     return this; } public void fragment(String xmlFragment) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException { StringBuffer buf = new StringBuffer(xmlFragment.length() + this.namespaceEnd * 30);
/* 155 */     buf.append("<fragment");
/*     */     int pos;
/* 157 */     label30: for (pos = this.namespaceEnd - 1; pos >= 0; pos--) {
/* 158 */       String prefix = this.namespacePrefix[pos];
/* 159 */       for (int i = this.namespaceEnd - 1; i > pos; i--) {
/* 160 */         if (prefix.equals(this.namespacePrefix[i])) {
/*     */           continue label30;
/*     */         }
/*     */       } 
/* 164 */       buf.append(" xmlns");
/* 165 */       if (prefix.length() > 0) {
/* 166 */         buf.append(':').append(prefix);
/*     */       }
/* 168 */       buf.append("='");
/* 169 */       buf.append(escapeAttributeValue(this.namespaceUri[pos]));
/* 170 */       buf.append("'");
/*     */     } 
/*     */     
/* 173 */     buf.append(">");
/* 174 */     buf.append(xmlFragment);
/* 175 */     buf.append("</fragment>");
/*     */     
/* 177 */     if (this.fragmentParser == null) {
/* 178 */       this.fragmentParser = this.wf.newPullParserWrapper();
/*     */     }
/* 180 */     String s = buf.toString();
/*     */     
/* 182 */     this.fragmentParser.setInput(new StringReader(s));
/* 183 */     this.fragmentParser.nextTag();
/* 184 */     this.fragmentParser.require(2, null, "fragment");
/*     */     while (true) {
/* 186 */       this.fragmentParser.nextToken();
/* 187 */       if (this.fragmentParser.getDepth() == 1 && this.fragmentParser.getEventType() == 3) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 192 */       event((XmlPullParser)this.fragmentParser);
/*     */     } 
/* 194 */     this.fragmentParser.require(3, null, "fragment"); }
/*     */    public void event(XmlPullParser pp) throws XmlPullParserException, IOException {
/*     */     Boolean standalone;
/*     */     String s;
/* 198 */     int eventType = pp.getEventType();
/* 199 */     switch (eventType) {
/*     */       
/*     */       case 0:
/* 202 */         standalone = (Boolean)pp.getProperty("http://xmlpull.org/v1/doc/features.html#xmldecl-standalone");
/* 203 */         startDocument(pp.getInputEncoding(), standalone);
/*     */         break;
/*     */       
/*     */       case 1:
/* 207 */         endDocument();
/*     */         break;
/*     */       
/*     */       case 2:
/* 211 */         writeStartTag(pp);
/*     */         break;
/*     */       
/*     */       case 3:
/* 215 */         endTag(pp.getNamespace(), pp.getName());
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 220 */         s = pp.getText();
/* 221 */         ignorableWhitespace(s);
/*     */         break;
/*     */       
/*     */       case 4:
/* 225 */         if (pp.getDepth() > 0) {
/* 226 */           text(pp.getText()); break;
/*     */         } 
/* 228 */         ignorableWhitespace(pp.getText());
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 233 */         entityRef(pp.getName());
/*     */         break;
/*     */       
/*     */       case 5:
/* 237 */         cdsect(pp.getText());
/*     */         break;
/*     */       
/*     */       case 8:
/* 241 */         processingInstruction(pp.getText());
/*     */         break;
/*     */       
/*     */       case 9:
/* 245 */         comment(pp.getText());
/*     */         break;
/*     */       
/*     */       case 10:
/* 249 */         docdecl(pp.getText());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeStartTag(XmlPullParser pp) throws XmlPullParserException, IOException {
/* 255 */     if (!pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes")) {
/* 256 */       int nsStart = pp.getNamespaceCount(pp.getDepth() - 1);
/* 257 */       int nsEnd = pp.getNamespaceCount(pp.getDepth());
/* 258 */       for (int j = nsStart; j < nsEnd; j++) {
/* 259 */         String prefix = pp.getNamespacePrefix(j);
/* 260 */         String ns = pp.getNamespaceUri(j);
/* 261 */         setPrefix(prefix, ns);
/*     */       } 
/*     */     } 
/* 264 */     startTag(pp.getNamespace(), pp.getName());
/*     */     
/* 266 */     for (int i = 0; i < pp.getAttributeCount(); i++) {
/* 267 */       attribute(pp.getAttributeNamespace(i), pp.getAttributeName(i), pp.getAttributeValue(i));
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
/*     */   public String escapeAttributeValue(String value) {
/* 279 */     int posLt = value.indexOf('<');
/* 280 */     int posAmp = value.indexOf('&');
/* 281 */     int posQuot = value.indexOf('"');
/* 282 */     int posApos = value.indexOf('\'');
/* 283 */     if (posLt == -1 && posAmp == -1 && posQuot == -1 && posApos == -1) {
/* 284 */       return value;
/*     */     }
/* 286 */     StringBuffer buf = new StringBuffer(value.length() + 10);
/*     */ 
/*     */     
/* 289 */     for (int pos = 0, len = value.length(); pos < len; pos++) {
/* 290 */       char ch = value.charAt(pos);
/* 291 */       switch (ch) {
/*     */         case '<':
/* 293 */           buf.append("&lt;");
/*     */           break;
/*     */         case '&':
/* 296 */           buf.append("&amp;");
/*     */           break;
/*     */         case '\'':
/* 299 */           buf.append("&apos;");
/*     */           break;
/*     */         case '"':
/* 302 */           buf.append("&quot;");
/*     */           break;
/*     */         default:
/* 305 */           buf.append(ch); break;
/*     */       } 
/*     */     } 
/* 308 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String escapeText(String text) {
/* 316 */     int posLt = text.indexOf('<');
/* 317 */     int posAmp = text.indexOf('&');
/* 318 */     if (posLt == -1 && posAmp == -1) {
/* 319 */       return text;
/*     */     }
/* 321 */     StringBuffer buf = new StringBuffer(text.length() + 10);
/*     */     
/* 323 */     int pos = 0;
/*     */     while (true) {
/* 325 */       if (posLt == -1 && posAmp == -1) {
/* 326 */         buf.append(text.substring(pos)); break;
/*     */       } 
/* 328 */       if (posLt == -1 || (posLt != -1 && posAmp != -1 && posAmp < posLt)) {
/*     */ 
/*     */         
/* 331 */         if (pos < posAmp) buf.append(text.substring(pos, posAmp)); 
/* 332 */         buf.append("&amp;");
/* 333 */         pos = posAmp + 1;
/* 334 */         posAmp = text.indexOf('&', pos); continue;
/* 335 */       }  if (posAmp == -1 || (posLt != -1 && posAmp != -1 && posLt < posAmp)) {
/*     */ 
/*     */         
/* 338 */         if (pos < posLt) buf.append(text.substring(pos, posLt)); 
/* 339 */         buf.append("&lt;");
/* 340 */         pos = posLt + 1;
/* 341 */         posLt = text.indexOf('<', pos); continue;
/*     */       } 
/* 343 */       throw new IllegalStateException("wrong state posLt=" + posLt + " posAmp=" + posAmp + " for " + text);
/*     */     } 
/*     */ 
/*     */     
/* 347 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeDouble(double d) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 354 */     if (d == Double.POSITIVE_INFINITY) {
/* 355 */       this.xs.text("INF");
/* 356 */     } else if (d == Double.NEGATIVE_INFINITY) {
/* 357 */       this.xs.text("-INF");
/*     */     } else {
/* 359 */       this.xs.text(Double.toString(d));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeFloat(float f) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 366 */     if (f == Float.POSITIVE_INFINITY) {
/* 367 */       this.xs.text("INF");
/* 368 */     } else if (f == Float.NEGATIVE_INFINITY) {
/* 369 */       this.xs.text("-INF");
/*     */     } else {
/* 371 */       this.xs.text(Float.toString(f));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInt(int i) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 378 */     this.xs.text(Integer.toString(i));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeString(String s) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 384 */     if (s == null) {
/* 385 */       throw new IllegalArgumentException("null string can not be written");
/*     */     }
/* 387 */     this.xs.text(s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeDoubleElement(String namespace, String name, double d) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 393 */     this.xs.startTag(namespace, name);
/* 394 */     writeDouble(d);
/* 395 */     this.xs.endTag(namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeFloatElement(String namespace, String name, float f) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 401 */     this.xs.startTag(namespace, name);
/* 402 */     writeFloat(f);
/* 403 */     this.xs.endTag(namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeIntElement(String namespace, String name, int i) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 409 */     this.xs.startTag(namespace, name);
/* 410 */     writeInt(i);
/* 411 */     this.xs.endTag(namespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeStringElement(String namespace, String name, String s) throws XmlPullParserException, IOException, IllegalArgumentException {
/* 417 */     this.xs.startTag(namespace, name);
/* 418 */     if (s == null) {
/* 419 */       this.xs.attribute("http://www.w3.org/2001/XMLSchema", "nil", "true");
/*     */     } else {
/* 421 */       writeString(s);
/*     */     } 
/* 423 */     this.xs.endTag(namespace, name);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\wrapper\classic\StaticXmlSerializerWrapper.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */