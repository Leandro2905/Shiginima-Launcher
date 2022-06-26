/*     */ package org.xmlpull.v1.sax2;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xmlpull.v1.XmlPullParser;
/*     */ import org.xmlpull.v1.XmlPullParserException;
/*     */ import org.xmlpull.v1.XmlPullParserFactory;
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
/*     */ public class Driver
/*     */   implements Locator, XMLReader, Attributes
/*     */ {
/*     */   protected static final String DECLARATION_HANDLER_PROPERTY = "http://xml.org/sax/properties/declaration-handler";
/*     */   protected static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
/*     */   protected static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */   protected static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
/*     */   protected static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
/*     */   protected static final String APACHE_SCHEMA_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/schema";
/*     */   protected static final String APACHE_DYNAMIC_VALIDATION_FEATURE = "http://apache.org/xml/features/validation/dynamic";
/*  68 */   protected ContentHandler contentHandler = new DefaultHandler();
/*  69 */   protected ErrorHandler errorHandler = new DefaultHandler();
/*     */ 
/*     */   
/*     */   protected String systemId;
/*     */ 
/*     */   
/*     */   protected XmlPullParser pp;
/*     */ 
/*     */ 
/*     */   
/*     */   public Driver() throws XmlPullParserException {
/*  80 */     XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
/*  81 */     factory.setNamespaceAware(true);
/*  82 */     this.pp = factory.newPullParser();
/*     */   }
/*     */   
/*     */   public Driver(XmlPullParser pp) throws XmlPullParserException {
/*  86 */     this.pp = pp;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  91 */     return this.pp.getAttributeCount(); }
/*  92 */   public String getURI(int index) { return this.pp.getAttributeNamespace(index); } public String getLocalName(int index) {
/*  93 */     return this.pp.getAttributeName(index);
/*     */   } public String getQName(int index) {
/*  95 */     String prefix = this.pp.getAttributePrefix(index);
/*  96 */     if (prefix != null) {
/*  97 */       return prefix + ':' + this.pp.getAttributeName(index);
/*     */     }
/*  99 */     return this.pp.getAttributeName(index);
/*     */   }
/*     */   
/* 102 */   public String getType(int index) { return this.pp.getAttributeType(index); } public String getValue(int index) {
/* 103 */     return this.pp.getAttributeValue(index);
/*     */   }
/*     */   public int getIndex(String uri, String localName) {
/* 106 */     for (int i = 0; i < this.pp.getAttributeCount(); i++) {
/*     */       
/* 108 */       if (this.pp.getAttributeNamespace(i).equals(uri) && this.pp.getAttributeName(i).equals(localName))
/*     */       {
/*     */         
/* 111 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 115 */     return -1;
/*     */   }
/*     */   
/*     */   public int getIndex(String qName) {
/* 119 */     for (int i = 0; i < this.pp.getAttributeCount(); i++) {
/*     */       
/* 121 */       if (this.pp.getAttributeName(i).equals(qName))
/*     */       {
/* 123 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return -1;
/*     */   }
/*     */   
/*     */   public String getType(String uri, String localName) {
/* 131 */     for (int i = 0; i < this.pp.getAttributeCount(); i++) {
/*     */       
/* 133 */       if (this.pp.getAttributeNamespace(i).equals(uri) && this.pp.getAttributeName(i).equals(localName))
/*     */       {
/*     */         
/* 136 */         return this.pp.getAttributeType(i);
/*     */       }
/*     */     } 
/*     */     
/* 140 */     return null;
/*     */   }
/*     */   public String getType(String qName) {
/* 143 */     for (int i = 0; i < this.pp.getAttributeCount(); i++) {
/*     */       
/* 145 */       if (this.pp.getAttributeName(i).equals(qName))
/*     */       {
/* 147 */         return this.pp.getAttributeType(i);
/*     */       }
/*     */     } 
/*     */     
/* 151 */     return null;
/*     */   }
/*     */   public String getValue(String uri, String localName) {
/* 154 */     return this.pp.getAttributeValue(uri, localName);
/*     */   }
/*     */   public String getValue(String qName) {
/* 157 */     return this.pp.getAttributeValue(null, qName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPublicId() {
/* 162 */     return null; }
/* 163 */   public String getSystemId() { return this.systemId; }
/* 164 */   public int getLineNumber() { return this.pp.getLineNumber(); } public int getColumnNumber() {
/* 165 */     return this.pp.getColumnNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 172 */     if ("http://xml.org/sax/features/namespaces".equals(name))
/* 173 */       return this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces"); 
/* 174 */     if ("http://xml.org/sax/features/namespace-prefixes".equals(name))
/* 175 */       return this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes"); 
/* 176 */     if ("http://xml.org/sax/features/validation".equals(name)) {
/* 177 */       return this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#validation");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     return this.pp.getFeature(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*     */     try {
/* 192 */       if ("http://xml.org/sax/features/namespaces".equals(name)) {
/* 193 */         this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", value);
/* 194 */       } else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/* 195 */         if (this.pp.getFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes") != value) {
/* 196 */           this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#report-namespace-prefixes", value);
/*     */         }
/* 198 */       } else if ("http://xml.org/sax/features/validation".equals(name)) {
/* 199 */         this.pp.setFeature("http://xmlpull.org/v1/doc/features.html#validation", value);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 210 */         this.pp.setFeature(name, value);
/*     */       }
/*     */     
/* 213 */     } catch (XmlPullParserException ex) {
/* 214 */       throw new SAXNotSupportedException("problem with setting feature " + name + ": " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 221 */     if ("http://xml.org/sax/properties/declaration-handler".equals(name))
/* 222 */       return null; 
/* 223 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 224 */       return null;
/*     */     }
/* 226 */     return this.pp.getProperty(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 235 */     if ("http://xml.org/sax/properties/declaration-handler".equals(name))
/* 236 */       throw new SAXNotSupportedException("not supported setting property " + name); 
/* 237 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 238 */       throw new SAXNotSupportedException("not supported setting property " + name);
/*     */     }
/*     */     try {
/* 241 */       this.pp.setProperty(name, value);
/* 242 */     } catch (XmlPullParserException ex) {
/* 243 */       throw new SAXNotSupportedException("not supported set property " + name + ": " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {}
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 251 */     return null;
/*     */   }
/*     */   public void setDTDHandler(DTDHandler handler) {}
/*     */   public DTDHandler getDTDHandler() {
/* 255 */     return null;
/*     */   }
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/* 259 */     this.contentHandler = handler;
/*     */   }
/*     */   public ContentHandler getContentHandler() {
/* 262 */     return this.contentHandler;
/*     */   }
/*     */   public void setErrorHandler(ErrorHandler handler) {
/* 265 */     this.errorHandler = handler;
/*     */   }
/*     */   public ErrorHandler getErrorHandler() {
/* 268 */     return this.errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(InputSource source) throws SAXException, IOException {
/* 273 */     this.systemId = source.getSystemId();
/* 274 */     this.contentHandler.setDocumentLocator(this);
/*     */     
/* 276 */     Reader reader = source.getCharacterStream();
/*     */     try {
/* 278 */       if (reader == null) {
/* 279 */         InputStream stream = source.getByteStream();
/* 280 */         String encoding = source.getEncoding();
/*     */         
/* 282 */         if (stream == null) {
/* 283 */           this.systemId = source.getSystemId();
/* 284 */           if (this.systemId == null) {
/* 285 */             SAXParseException saxException = new SAXParseException("null source systemId", this);
/*     */             
/* 287 */             this.errorHandler.fatalError(saxException);
/*     */             
/*     */             return;
/*     */           } 
/*     */           try {
/* 292 */             URL url = new URL(this.systemId);
/* 293 */             stream = url.openStream();
/* 294 */           } catch (MalformedURLException nue) {
/*     */             try {
/* 296 */               stream = new FileInputStream(this.systemId);
/* 297 */             } catch (FileNotFoundException fnfe) {
/* 298 */               SAXParseException saxException = new SAXParseException("could not open file with systemId " + this.systemId, this, fnfe);
/*     */               
/* 300 */               this.errorHandler.fatalError(saxException);
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/* 305 */         this.pp.setInput(stream, encoding);
/*     */       } else {
/* 307 */         this.pp.setInput(reader);
/*     */       } 
/* 309 */     } catch (XmlPullParserException ex) {
/* 310 */       SAXParseException saxException = new SAXParseException("parsing initialization error: " + ex, this, (Exception)ex);
/*     */ 
/*     */       
/* 313 */       this.errorHandler.fatalError(saxException);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 319 */       this.contentHandler.startDocument();
/*     */       
/* 321 */       this.pp.next();
/*     */       
/* 323 */       if (this.pp.getEventType() != 2) {
/* 324 */         SAXParseException saxException = new SAXParseException("expected start tag not" + this.pp.getPositionDescription(), this);
/*     */ 
/*     */         
/* 327 */         this.errorHandler.fatalError(saxException);
/*     */         return;
/*     */       } 
/* 330 */     } catch (XmlPullParserException ex) {
/* 331 */       SAXParseException saxException = new SAXParseException("parsing initialization error: " + ex, this, (Exception)ex);
/*     */ 
/*     */       
/* 334 */       this.errorHandler.fatalError(saxException);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 340 */     parseSubTree(this.pp);
/*     */ 
/*     */ 
/*     */     
/* 344 */     this.contentHandler.endDocument();
/*     */   }
/*     */   
/*     */   public void parse(String systemId) throws SAXException, IOException {
/* 348 */     parse(new InputSource(systemId));
/*     */   }
/*     */ 
/*     */   
/*     */   public void parseSubTree(XmlPullParser pp) throws SAXException, IOException {
/* 353 */     this.pp = pp;
/* 354 */     boolean namespaceAware = pp.getFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces");
/*     */     try {
/* 356 */       if (pp.getEventType() != 2) {
/* 357 */         throw new SAXException("start tag must be read before skiping subtree" + pp.getPositionDescription());
/*     */       }
/*     */       
/* 360 */       int[] holderForStartAndLength = new int[2];
/* 361 */       StringBuffer rawName = new StringBuffer(16);
/* 362 */       String prefix = null;
/* 363 */       String name = null;
/* 364 */       int level = pp.getDepth() - 1;
/* 365 */       int type = 2;
/*     */       
/*     */       do {
/*     */         char[] chars;
/* 369 */         switch (type) {
/*     */           case 2:
/* 371 */             if (namespaceAware) {
/* 372 */               int depth = pp.getDepth() - 1;
/* 373 */               int countPrev = (level > depth) ? pp.getNamespaceCount(depth) : 0;
/*     */ 
/*     */               
/* 376 */               int count = pp.getNamespaceCount(depth + 1);
/* 377 */               for (int i = countPrev; i < count; i++)
/*     */               {
/* 379 */                 this.contentHandler.startPrefixMapping(pp.getNamespacePrefix(i), pp.getNamespaceUri(i));
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 384 */               name = pp.getName();
/* 385 */               prefix = pp.getPrefix();
/* 386 */               if (prefix != null) {
/* 387 */                 rawName.setLength(0);
/* 388 */                 rawName.append(prefix);
/* 389 */                 rawName.append(':');
/* 390 */                 rawName.append(name);
/*     */               } 
/* 392 */               startElement(pp.getNamespace(), name, (prefix != null) ? rawName.toString() : name);
/*     */               
/*     */               break;
/*     */             } 
/* 396 */             startElement(pp.getNamespace(), pp.getName(), pp.getName());
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 4:
/* 404 */             chars = pp.getTextCharacters(holderForStartAndLength);
/* 405 */             this.contentHandler.characters(chars, holderForStartAndLength[0], holderForStartAndLength[1]);
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case 3:
/* 412 */             if (namespaceAware) {
/* 413 */               name = pp.getName();
/* 414 */               prefix = pp.getPrefix();
/* 415 */               if (prefix != null) {
/* 416 */                 rawName.setLength(0);
/* 417 */                 rawName.append(prefix);
/* 418 */                 rawName.append(':');
/* 419 */                 rawName.append(name);
/*     */               } 
/* 421 */               this.contentHandler.endElement(pp.getNamespace(), name, (prefix != null) ? rawName.toString() : name);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 426 */               int depth = pp.getDepth();
/* 427 */               int countPrev = (level > depth) ? pp.getNamespaceCount(pp.getDepth()) : 0;
/*     */               
/* 429 */               int count = pp.getNamespaceCount(pp.getDepth() - 1);
/*     */               
/* 431 */               for (int i = count - 1; i >= countPrev; i--)
/*     */               {
/* 433 */                 this.contentHandler.endPrefixMapping(pp.getNamespacePrefix(i));
/*     */               }
/*     */               
/*     */               break;
/*     */             } 
/* 438 */             this.contentHandler.endElement(pp.getNamespace(), pp.getName(), pp.getName());
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 1:
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/* 448 */         type = pp.next();
/* 449 */       } while (pp.getDepth() > level);
/* 450 */     } catch (XmlPullParserException ex) {
/* 451 */       SAXParseException saxException = new SAXParseException("parsing error: " + ex, this, (Exception)ex);
/* 452 */       ex.printStackTrace();
/* 453 */       this.errorHandler.fatalError(saxException);
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
/*     */   protected void startElement(String namespace, String localName, String qName) throws SAXException {
/* 465 */     this.contentHandler.startElement(namespace, localName, qName, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\sax2\Driver.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */