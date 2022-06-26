/*      */ package org.xmlpull.mxp1;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import org.xmlpull.v1.XmlPullParser;
/*      */ import org.xmlpull.v1.XmlPullParserException;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MXParser
/*      */   implements XmlPullParser
/*      */ {
/*      */   protected static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
/*      */   protected static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
/*      */   protected static final String FEATURE_XML_ROUNDTRIP = "http://xmlpull.org/v1/doc/features.html#xml-roundtrip";
/*      */   protected static final String FEATURE_NAMES_INTERNED = "http://xmlpull.org/v1/doc/features.html#names-interned";
/*      */   protected static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
/*      */   protected static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
/*      */   protected static final String PROPERTY_XMLDECL_CONTENT = "http://xmlpull.org/v1/doc/properties.html#xmldecl-content";
/*      */   protected static final String PROPERTY_LOCATION = "http://xmlpull.org/v1/doc/properties.html#location";
/*      */   protected boolean allStringsInterned;
/*      */   private static final boolean TRACE_SIZING = false;
/*      */   protected boolean processNamespaces;
/*      */   protected boolean roundtripSupported;
/*      */   protected String location;
/*      */   protected int lineNumber;
/*      */   protected int columnNumber;
/*      */   protected boolean seenRoot;
/*      */   protected boolean reachedEnd;
/*      */   protected int eventType;
/*      */   protected boolean emptyElementTag;
/*      */   protected int depth;
/*      */   protected char[][] elRawName;
/*      */   protected int[] elRawNameEnd;
/*      */   protected int[] elRawNameLine;
/*      */   protected String[] elName;
/*      */   protected String[] elPrefix;
/*      */   protected String[] elUri;
/*      */   protected int[] elNamespaceCount;
/*      */   protected int attributeCount;
/*      */   protected String[] attributeName;
/*      */   protected int[] attributeNameHash;
/*      */   protected String[] attributePrefix;
/*      */   protected String[] attributeUri;
/*      */   protected String[] attributeValue;
/*      */   protected int namespaceEnd;
/*      */   protected String[] namespacePrefix;
/*      */   protected int[] namespacePrefixHash;
/*      */   protected String[] namespaceUri;
/*      */   protected int entityEnd;
/*      */   protected String[] entityName;
/*      */   protected char[][] entityNameBuf;
/*      */   protected String[] entityReplacement;
/*      */   protected char[][] entityReplacementBuf;
/*      */   protected int[] entityNameHash;
/*      */   protected static final int READ_CHUNK_SIZE = 8192;
/*      */   protected Reader reader;
/*      */   protected String inputEncoding;
/*      */   protected InputStream inputStream;
/*      */   
/*      */   protected void resetStringCache() {}
/*      */   
/*      */   protected String newString(char[] cbuf, int off, int len) {
/*   68 */     return new String(cbuf, off, len);
/*      */   }
/*      */   
/*      */   protected String newStringIntern(char[] cbuf, int off, int len) {
/*   72 */     return (new String(cbuf, off, len)).intern();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureElementsCapacity() {
/*  108 */     int elStackSize = (this.elName != null) ? this.elName.length : 0;
/*  109 */     if (this.depth + 1 >= elStackSize) {
/*      */       
/*  111 */       int newSize = ((this.depth >= 7) ? (2 * this.depth) : 8) + 2;
/*      */ 
/*      */ 
/*      */       
/*  115 */       boolean needsCopying = (elStackSize > 0);
/*  116 */       String[] arr = null;
/*      */       
/*  118 */       arr = new String[newSize];
/*  119 */       if (needsCopying) System.arraycopy(this.elName, 0, arr, 0, elStackSize); 
/*  120 */       this.elName = arr;
/*  121 */       arr = new String[newSize];
/*  122 */       if (needsCopying) System.arraycopy(this.elPrefix, 0, arr, 0, elStackSize); 
/*  123 */       this.elPrefix = arr;
/*  124 */       arr = new String[newSize];
/*  125 */       if (needsCopying) System.arraycopy(this.elUri, 0, arr, 0, elStackSize); 
/*  126 */       this.elUri = arr;
/*      */       
/*  128 */       int[] iarr = new int[newSize];
/*  129 */       if (needsCopying) {
/*  130 */         System.arraycopy(this.elNamespaceCount, 0, iarr, 0, elStackSize);
/*      */       } else {
/*      */         
/*  133 */         iarr[0] = 0;
/*      */       } 
/*  135 */       this.elNamespaceCount = iarr;
/*      */ 
/*      */       
/*  138 */       iarr = new int[newSize];
/*  139 */       if (needsCopying) {
/*  140 */         System.arraycopy(this.elRawNameEnd, 0, iarr, 0, elStackSize);
/*      */       }
/*  142 */       this.elRawNameEnd = iarr;
/*      */       
/*  144 */       iarr = new int[newSize];
/*  145 */       if (needsCopying) {
/*  146 */         System.arraycopy(this.elRawNameLine, 0, iarr, 0, elStackSize);
/*      */       }
/*  148 */       this.elRawNameLine = iarr;
/*      */       
/*  150 */       char[][] carr = new char[newSize][];
/*  151 */       if (needsCopying) {
/*  152 */         System.arraycopy(this.elRawName, 0, carr, 0, elStackSize);
/*      */       }
/*  154 */       this.elRawName = carr;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureAttributesCapacity(int size) {
/*  191 */     int attrPosSize = (this.attributeName != null) ? this.attributeName.length : 0;
/*  192 */     if (size >= attrPosSize) {
/*  193 */       int newSize = (size > 7) ? (2 * size) : 8;
/*      */ 
/*      */ 
/*      */       
/*  197 */       boolean needsCopying = (attrPosSize > 0);
/*  198 */       String[] arr = null;
/*      */       
/*  200 */       arr = new String[newSize];
/*  201 */       if (needsCopying) System.arraycopy(this.attributeName, 0, arr, 0, attrPosSize); 
/*  202 */       this.attributeName = arr;
/*      */       
/*  204 */       arr = new String[newSize];
/*  205 */       if (needsCopying) System.arraycopy(this.attributePrefix, 0, arr, 0, attrPosSize); 
/*  206 */       this.attributePrefix = arr;
/*      */       
/*  208 */       arr = new String[newSize];
/*  209 */       if (needsCopying) System.arraycopy(this.attributeUri, 0, arr, 0, attrPosSize); 
/*  210 */       this.attributeUri = arr;
/*      */       
/*  212 */       arr = new String[newSize];
/*  213 */       if (needsCopying) System.arraycopy(this.attributeValue, 0, arr, 0, attrPosSize); 
/*  214 */       this.attributeValue = arr;
/*      */       
/*  216 */       if (!this.allStringsInterned) {
/*  217 */         int[] iarr = new int[newSize];
/*  218 */         if (needsCopying) System.arraycopy(this.attributeNameHash, 0, iarr, 0, attrPosSize); 
/*  219 */         this.attributeNameHash = iarr;
/*      */       } 
/*      */       
/*  222 */       arr = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureNamespacesCapacity(int size) {
/*  234 */     int namespaceSize = (this.namespacePrefix != null) ? this.namespacePrefix.length : 0;
/*  235 */     if (size >= namespaceSize) {
/*  236 */       int newSize = (size > 7) ? (2 * size) : 8;
/*      */ 
/*      */ 
/*      */       
/*  240 */       String[] newNamespacePrefix = new String[newSize];
/*  241 */       String[] newNamespaceUri = new String[newSize];
/*  242 */       if (this.namespacePrefix != null) {
/*  243 */         System.arraycopy(this.namespacePrefix, 0, newNamespacePrefix, 0, this.namespaceEnd);
/*      */         
/*  245 */         System.arraycopy(this.namespaceUri, 0, newNamespaceUri, 0, this.namespaceEnd);
/*      */       } 
/*      */       
/*  248 */       this.namespacePrefix = newNamespacePrefix;
/*  249 */       this.namespaceUri = newNamespaceUri;
/*      */ 
/*      */       
/*  252 */       if (!this.allStringsInterned) {
/*  253 */         int[] newNamespacePrefixHash = new int[newSize];
/*  254 */         if (this.namespacePrefixHash != null) {
/*  255 */           System.arraycopy(this.namespacePrefixHash, 0, newNamespacePrefixHash, 0, this.namespaceEnd);
/*      */         }
/*      */         
/*  258 */         this.namespacePrefixHash = newNamespacePrefixHash;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final int fastHash(char[] ch, int off, int len) {
/*  271 */     if (len == 0) return 0;
/*      */     
/*  273 */     int hash = ch[off];
/*      */     
/*  275 */     hash = (hash << 7) + ch[off + len - 1];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  280 */     if (len > 16) hash = (hash << 7) + ch[off + len / 4]; 
/*  281 */     if (len > 8) hash = (hash << 7) + ch[off + len / 2];
/*      */ 
/*      */ 
/*      */     
/*  285 */     return hash;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureEntityCapacity() {
/*  299 */     int entitySize = (this.entityReplacementBuf != null) ? this.entityReplacementBuf.length : 0;
/*  300 */     if (this.entityEnd >= entitySize) {
/*  301 */       int newSize = (this.entityEnd > 7) ? (2 * this.entityEnd) : 8;
/*      */ 
/*      */ 
/*      */       
/*  305 */       String[] newEntityName = new String[newSize];
/*  306 */       char[][] newEntityNameBuf = new char[newSize][];
/*  307 */       String[] newEntityReplacement = new String[newSize];
/*  308 */       char[][] newEntityReplacementBuf = new char[newSize][];
/*  309 */       if (this.entityName != null) {
/*  310 */         System.arraycopy(this.entityName, 0, newEntityName, 0, this.entityEnd);
/*  311 */         System.arraycopy(this.entityNameBuf, 0, newEntityNameBuf, 0, this.entityEnd);
/*  312 */         System.arraycopy(this.entityReplacement, 0, newEntityReplacement, 0, this.entityEnd);
/*  313 */         System.arraycopy(this.entityReplacementBuf, 0, newEntityReplacementBuf, 0, this.entityEnd);
/*      */       } 
/*  315 */       this.entityName = newEntityName;
/*  316 */       this.entityNameBuf = newEntityNameBuf;
/*  317 */       this.entityReplacement = newEntityReplacement;
/*  318 */       this.entityReplacementBuf = newEntityReplacementBuf;
/*      */       
/*  320 */       if (!this.allStringsInterned) {
/*  321 */         int[] newEntityNameHash = new int[newSize];
/*  322 */         if (this.entityNameHash != null) {
/*  323 */           System.arraycopy(this.entityNameHash, 0, newEntityNameHash, 0, this.entityEnd);
/*      */         }
/*  325 */         this.entityNameHash = newEntityNameHash;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  337 */   protected int bufLoadFactor = 95;
/*      */ 
/*      */   
/*  340 */   protected char[] buf = new char[(Runtime.getRuntime().freeMemory() > 1000000L) ? 8192 : 256];
/*      */   
/*  342 */   protected int bufSoftLimit = this.bufLoadFactor * this.buf.length / 100;
/*      */   
/*      */   protected boolean preventBufferCompaction;
/*      */   
/*      */   protected int bufAbsoluteStart;
/*      */   protected int bufStart;
/*      */   protected int bufEnd;
/*      */   protected int pos;
/*      */   protected int posStart;
/*      */   protected int posEnd;
/*  352 */   protected char[] pc = new char[(Runtime.getRuntime().freeMemory() > 1000000L) ? 8192 : 64];
/*      */   
/*      */   protected int pcStart;
/*      */   
/*      */   protected int pcEnd;
/*      */   
/*      */   protected boolean usePC;
/*      */   
/*      */   protected boolean seenStartTag;
/*      */   
/*      */   protected boolean seenEndTag;
/*      */   
/*      */   protected boolean pastEndTag;
/*      */   
/*      */   protected boolean seenAmpersand;
/*      */   
/*      */   protected boolean seenMarkup;
/*      */   
/*      */   protected boolean seenDocdecl;
/*      */   
/*      */   protected boolean tokenize;
/*      */   
/*      */   protected String text;
/*      */   protected String entityRefName;
/*      */   protected String xmlDeclVersion;
/*      */   protected Boolean xmlDeclStandalone;
/*      */   protected String xmlDeclContent;
/*      */   protected char[] charRefOneCharBuf;
/*      */   
/*      */   protected void reset() {
/*  382 */     this.location = null;
/*  383 */     this.lineNumber = 1;
/*  384 */     this.columnNumber = 0;
/*  385 */     this.seenRoot = false;
/*  386 */     this.reachedEnd = false;
/*  387 */     this.eventType = 0;
/*  388 */     this.emptyElementTag = false;
/*      */     
/*  390 */     this.depth = 0;
/*      */     
/*  392 */     this.attributeCount = 0;
/*      */     
/*  394 */     this.namespaceEnd = 0;
/*      */     
/*  396 */     this.entityEnd = 0;
/*      */     
/*  398 */     this.reader = null;
/*  399 */     this.inputEncoding = null;
/*      */     
/*  401 */     this.preventBufferCompaction = false;
/*  402 */     this.bufAbsoluteStart = 0;
/*  403 */     this.bufEnd = this.bufStart = 0;
/*  404 */     this.pos = this.posStart = this.posEnd = 0;
/*      */     
/*  406 */     this.pcEnd = this.pcStart = 0;
/*      */     
/*  408 */     this.usePC = false;
/*      */     
/*  410 */     this.seenStartTag = false;
/*  411 */     this.seenEndTag = false;
/*  412 */     this.pastEndTag = false;
/*  413 */     this.seenAmpersand = false;
/*  414 */     this.seenMarkup = false;
/*  415 */     this.seenDocdecl = false;
/*      */     
/*  417 */     this.xmlDeclVersion = null;
/*  418 */     this.xmlDeclStandalone = null;
/*  419 */     this.xmlDeclContent = null;
/*      */     
/*  421 */     resetStringCache();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFeature(String name, boolean state) throws XmlPullParserException {
/*  440 */     if (name == null) throw new IllegalArgumentException("feature name should not be null"); 
/*  441 */     if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(name)) {
/*  442 */       if (this.eventType != 0) throw new XmlPullParserException("namespace processing feature can only be changed before parsing", this, null);
/*      */       
/*  444 */       this.processNamespaces = state;
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  449 */     else if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
/*  450 */       if (state) {
/*  451 */         throw new XmlPullParserException("interning names in this implementation is not supported");
/*      */       }
/*      */     }
/*  454 */     else if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
/*  455 */       if (state) {
/*  456 */         throw new XmlPullParserException("processing DOCDECL is not supported");
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  461 */     else if ("http://xmlpull.org/v1/doc/features.html#xml-roundtrip".equals(name)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  466 */       this.roundtripSupported = state;
/*      */     } else {
/*  468 */       throw new XmlPullParserException("unsupported feature " + name);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getFeature(String name) {
/*  475 */     if (name == null) throw new IllegalArgumentException("feature name should not be null"); 
/*  476 */     if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(name)) {
/*  477 */       return this.processNamespaces;
/*      */     }
/*      */     
/*  480 */     if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name))
/*  481 */       return false; 
/*  482 */     if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(name)) {
/*  483 */       return false;
/*      */     }
/*      */     
/*  486 */     if ("http://xmlpull.org/v1/doc/features.html#xml-roundtrip".equals(name))
/*      */     {
/*  488 */       return this.roundtripSupported;
/*      */     }
/*  490 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperty(String name, Object value) throws XmlPullParserException {
/*  497 */     if ("http://xmlpull.org/v1/doc/properties.html#location".equals(name)) {
/*  498 */       this.location = (String)value;
/*      */     } else {
/*  500 */       throw new XmlPullParserException("unsupported property: '" + name + "'");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getProperty(String name) {
/*  507 */     if (name == null) throw new IllegalArgumentException("property name should not be null"); 
/*  508 */     if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-version".equals(name))
/*  509 */       return this.xmlDeclVersion; 
/*  510 */     if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone".equals(name))
/*  511 */       return this.xmlDeclStandalone; 
/*  512 */     if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-content".equals(name))
/*  513 */       return this.xmlDeclContent; 
/*  514 */     if ("http://xmlpull.org/v1/doc/properties.html#location".equals(name)) {
/*  515 */       return this.location;
/*      */     }
/*  517 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInput(Reader in) throws XmlPullParserException {
/*  523 */     reset();
/*  524 */     this.reader = in;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInput(InputStream inputStream, String inputEncoding) throws XmlPullParserException {
/*      */     Reader reader;
/*  530 */     if (inputStream == null) {
/*  531 */       throw new IllegalArgumentException("input stream can not be null");
/*      */     }
/*  533 */     this.inputStream = inputStream;
/*      */ 
/*      */     
/*      */     try {
/*  537 */       if (inputEncoding != null) {
/*  538 */         reader = new InputStreamReader(inputStream, inputEncoding);
/*      */       } else {
/*      */         
/*  541 */         reader = new InputStreamReader(inputStream, "UTF-8");
/*      */       } 
/*  543 */     } catch (UnsupportedEncodingException une) {
/*  544 */       throw new XmlPullParserException("could not create reader for encoding " + inputEncoding + " : " + une, this, une);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  550 */     setInput(reader);
/*      */     
/*  552 */     this.inputEncoding = inputEncoding;
/*      */   }
/*      */   
/*      */   public String getInputEncoding() {
/*  556 */     return this.inputEncoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void defineEntityReplacementText(String entityName, String replacementText) throws XmlPullParserException {
/*  566 */     ensureEntityCapacity();
/*      */ 
/*      */     
/*  569 */     this.entityName[this.entityEnd] = newString(entityName.toCharArray(), 0, entityName.length());
/*  570 */     this.entityNameBuf[this.entityEnd] = entityName.toCharArray();
/*      */     
/*  572 */     this.entityReplacement[this.entityEnd] = replacementText;
/*  573 */     this.entityReplacementBuf[this.entityEnd] = replacementText.toCharArray();
/*  574 */     if (!this.allStringsInterned) {
/*  575 */       this.entityNameHash[this.entityEnd] = fastHash(this.entityNameBuf[this.entityEnd], 0, (this.entityNameBuf[this.entityEnd]).length);
/*      */     }
/*      */     
/*  578 */     this.entityEnd++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNamespaceCount(int depth) throws XmlPullParserException {
/*  586 */     if (!this.processNamespaces || depth == 0) {
/*  587 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  591 */     if (depth < 0 || depth > this.depth) throw new IllegalArgumentException("allowed namespace depth 0.." + this.depth + " not " + depth);
/*      */     
/*  593 */     return this.elNamespaceCount[depth];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNamespacePrefix(int pos) throws XmlPullParserException {
/*  602 */     if (pos < this.namespaceEnd) {
/*  603 */       return this.namespacePrefix[pos];
/*      */     }
/*  605 */     throw new XmlPullParserException("position " + pos + " exceeded number of available namespaces " + this.namespaceEnd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNamespaceUri(int pos) throws XmlPullParserException {
/*  614 */     if (pos < this.namespaceEnd) {
/*  615 */       return this.namespaceUri[pos];
/*      */     }
/*  617 */     throw new XmlPullParserException("position " + pos + " exceeded number of available namespaces " + this.namespaceEnd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNamespace(String prefix) {
/*  626 */     if (prefix != null) {
/*  627 */       for (int i = this.namespaceEnd - 1; i >= 0; i--) {
/*  628 */         if (prefix.equals(this.namespacePrefix[i])) {
/*  629 */           return this.namespaceUri[i];
/*      */         }
/*      */       } 
/*  632 */       if ("xml".equals(prefix))
/*  633 */         return "http://www.w3.org/XML/1998/namespace"; 
/*  634 */       if ("xmlns".equals(prefix)) {
/*  635 */         return "http://www.w3.org/2000/xmlns/";
/*      */       }
/*      */     } else {
/*  638 */       for (int i = this.namespaceEnd - 1; i >= 0; i--) {
/*  639 */         if (this.namespacePrefix[i] == null) {
/*  640 */           return this.namespaceUri[i];
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  645 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDepth() {
/*  651 */     return this.depth;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int findFragment(int bufMinPos, char[] b, int start, int end) {
/*  657 */     if (start < bufMinPos) {
/*  658 */       start = bufMinPos;
/*  659 */       if (start > end) start = end; 
/*  660 */       return start;
/*      */     } 
/*  662 */     if (end - start > 65) {
/*  663 */       start = end - 10;
/*      */     }
/*  665 */     int i = start + 1;
/*  666 */     while (--i > bufMinPos && 
/*  667 */       end - i <= 65) {
/*  668 */       char c = b[i];
/*  669 */       if (c == '<' && start - i > 10)
/*      */         break; 
/*  671 */     }  return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPositionDescription() {
/*  681 */     String fragment = null;
/*  682 */     if (this.posStart <= this.pos) {
/*  683 */       int start = findFragment(0, this.buf, this.posStart, this.pos);
/*      */       
/*  685 */       if (start < this.pos) {
/*  686 */         fragment = new String(this.buf, start, this.pos - start);
/*      */       }
/*  688 */       if (this.bufAbsoluteStart > 0 || start > 0) fragment = "..." + fragment;
/*      */     
/*      */     } 
/*      */ 
/*      */     
/*  693 */     return " " + XmlPullParser.TYPES[this.eventType] + ((fragment != null) ? (" seen " + printable(fragment) + "...") : "") + " " + ((this.location != null) ? this.location : "") + "@" + getLineNumber() + ":" + getColumnNumber();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLineNumber() {
/*  701 */     return this.lineNumber;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getColumnNumber() {
/*  706 */     return this.columnNumber;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWhitespace() throws XmlPullParserException {
/*  712 */     if (this.eventType == 4 || this.eventType == 5) {
/*  713 */       if (this.usePC) {
/*  714 */         for (int j = this.pcStart; j < this.pcEnd; j++) {
/*      */           
/*  716 */           if (!isS(this.pc[j])) return false; 
/*      */         } 
/*  718 */         return true;
/*      */       } 
/*  720 */       for (int i = this.posStart; i < this.posEnd; i++) {
/*      */         
/*  722 */         if (!isS(this.buf[i])) return false; 
/*      */       } 
/*  724 */       return true;
/*      */     } 
/*  726 */     if (this.eventType == 7) {
/*  727 */       return true;
/*      */     }
/*  729 */     throw new XmlPullParserException("no content available to check for white spaces");
/*      */   }
/*      */ 
/*      */   
/*      */   public String getText() {
/*  734 */     if (this.eventType == 0 || this.eventType == 1)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  739 */       return null;
/*      */     }
/*  741 */     if (this.eventType == 6) {
/*  742 */       return this.text;
/*      */     }
/*  744 */     if (this.text == null) {
/*  745 */       if (!this.usePC || this.eventType == 2 || this.eventType == 3) {
/*  746 */         this.text = new String(this.buf, this.posStart, this.posEnd - this.posStart);
/*      */       } else {
/*  748 */         this.text = new String(this.pc, this.pcStart, this.pcEnd - this.pcStart);
/*      */       } 
/*      */     }
/*  751 */     return this.text;
/*      */   }
/*      */ 
/*      */   
/*      */   public char[] getTextCharacters(int[] holderForStartAndLength) {
/*  756 */     if (this.eventType == 4) {
/*  757 */       if (this.usePC) {
/*  758 */         holderForStartAndLength[0] = this.pcStart;
/*  759 */         holderForStartAndLength[1] = this.pcEnd - this.pcStart;
/*  760 */         return this.pc;
/*      */       } 
/*  762 */       holderForStartAndLength[0] = this.posStart;
/*  763 */       holderForStartAndLength[1] = this.posEnd - this.posStart;
/*  764 */       return this.buf;
/*      */     } 
/*      */     
/*  767 */     if (this.eventType == 2 || this.eventType == 3 || this.eventType == 5 || this.eventType == 9 || this.eventType == 6 || this.eventType == 8 || this.eventType == 7 || this.eventType == 10) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  776 */       holderForStartAndLength[0] = this.posStart;
/*  777 */       holderForStartAndLength[1] = this.posEnd - this.posStart;
/*  778 */       return this.buf;
/*  779 */     }  if (this.eventType == 0 || this.eventType == 1) {
/*      */ 
/*      */       
/*  782 */       holderForStartAndLength[1] = -1; holderForStartAndLength[0] = -1;
/*  783 */       return null;
/*      */     } 
/*  785 */     throw new IllegalArgumentException("unknown text eventType: " + this.eventType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNamespace() {
/*  800 */     if (this.eventType == 2)
/*      */     {
/*  802 */       return this.processNamespaces ? this.elUri[this.depth] : ""; } 
/*  803 */     if (this.eventType == 3) {
/*  804 */       return this.processNamespaces ? this.elUri[this.depth] : "";
/*      */     }
/*  806 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  827 */     if (this.eventType == 2)
/*      */     {
/*  829 */       return this.elName[this.depth]; } 
/*  830 */     if (this.eventType == 3)
/*  831 */       return this.elName[this.depth]; 
/*  832 */     if (this.eventType == 6) {
/*  833 */       if (this.entityRefName == null) {
/*  834 */         this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
/*      */       }
/*  836 */       return this.entityRefName;
/*      */     } 
/*  838 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPrefix() {
/*  844 */     if (this.eventType == 2)
/*      */     {
/*  846 */       return this.elPrefix[this.depth]; } 
/*  847 */     if (this.eventType == 3) {
/*  848 */       return this.elPrefix[this.depth];
/*      */     }
/*  850 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmptyElementTag() throws XmlPullParserException {
/*  859 */     if (this.eventType != 2) throw new XmlPullParserException("parser must be on START_TAG to check for empty element", this, null);
/*      */     
/*  861 */     return this.emptyElementTag;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAttributeCount() {
/*  866 */     if (this.eventType != 2) return -1; 
/*  867 */     return this.attributeCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAttributeNamespace(int index) {
/*  872 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes");
/*      */     
/*  874 */     if (!this.processNamespaces) return ""; 
/*  875 */     if (index < 0 || index >= this.attributeCount) throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + index);
/*      */     
/*  877 */     return this.attributeUri[index];
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAttributeName(int index) {
/*  882 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes");
/*      */     
/*  884 */     if (index < 0 || index >= this.attributeCount) throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + index);
/*      */     
/*  886 */     return this.attributeName[index];
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAttributePrefix(int index) {
/*  891 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes");
/*      */     
/*  893 */     if (!this.processNamespaces) return null; 
/*  894 */     if (index < 0 || index >= this.attributeCount) throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + index);
/*      */     
/*  896 */     return this.attributePrefix[index];
/*      */   }
/*      */   
/*      */   public String getAttributeType(int index) {
/*  900 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes");
/*      */     
/*  902 */     if (index < 0 || index >= this.attributeCount) throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + index);
/*      */     
/*  904 */     return "CDATA";
/*      */   }
/*      */   
/*      */   public boolean isAttributeDefault(int index) {
/*  908 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes");
/*      */     
/*  910 */     if (index < 0 || index >= this.attributeCount) throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + index);
/*      */     
/*  912 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAttributeValue(int index) {
/*  917 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes");
/*      */     
/*  919 */     if (index < 0 || index >= this.attributeCount) throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + index);
/*      */     
/*  921 */     return this.attributeValue[index];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAttributeValue(String namespace, String name) {
/*  927 */     if (this.eventType != 2) throw new IndexOutOfBoundsException("only START_TAG can have attributes" + getPositionDescription());
/*      */     
/*  929 */     if (name == null) {
/*  930 */       throw new IllegalArgumentException("attribute name can not be null");
/*      */     }
/*      */     
/*  933 */     if (this.processNamespaces) {
/*  934 */       if (namespace == null) {
/*  935 */         namespace = "";
/*      */       }
/*      */       
/*  938 */       for (int i = 0; i < this.attributeCount; i++) {
/*  939 */         if ((namespace == this.attributeUri[i] || namespace.equals(this.attributeUri[i])) && name.equals(this.attributeName[i]))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  945 */           return this.attributeValue[i];
/*      */         }
/*      */       } 
/*      */     } else {
/*  949 */       if (namespace != null && namespace.length() == 0) {
/*  950 */         namespace = null;
/*      */       }
/*  952 */       if (namespace != null) throw new IllegalArgumentException("when namespaces processing is disabled attribute namespace must be null");
/*      */       
/*  954 */       for (int i = 0; i < this.attributeCount; i++) {
/*  955 */         if (name.equals(this.attributeName[i]))
/*      */         {
/*  957 */           return this.attributeValue[i];
/*      */         }
/*      */       } 
/*      */     } 
/*  961 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getEventType() throws XmlPullParserException {
/*  968 */     return this.eventType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
/*  974 */     if (!this.processNamespaces && namespace != null) {
/*  975 */       throw new XmlPullParserException("processing namespaces must be enabled on parser (or factory) to have possible namespaces declared on elements" + " (position:" + getPositionDescription() + ")");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  980 */     if (type != getEventType() || (namespace != null && !namespace.equals(getNamespace())) || (name != null && !name.equals(getName())))
/*      */     {
/*      */ 
/*      */       
/*  984 */       throw new XmlPullParserException("expected event " + XmlPullParser.TYPES[type] + ((name != null) ? (" with name '" + name + "'") : "") + ((namespace != null && name != null) ? " and" : "") + ((namespace != null) ? (" with namespace '" + namespace + "'") : "") + " but got" + ((type != getEventType()) ? (" " + XmlPullParser.TYPES[getEventType()]) : "") + ((name != null && getName() != null && !name.equals(getName())) ? (" name '" + getName() + "'") : "") + ((namespace != null && name != null && getName() != null && !name.equals(getName()) && getNamespace() != null && !namespace.equals(getNamespace())) ? " and" : "") + ((namespace != null && getNamespace() != null && !namespace.equals(getNamespace())) ? (" namespace '" + getNamespace() + "'") : "") + " (position:" + getPositionDescription() + ")");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void skipSubTree() throws XmlPullParserException, IOException {
/* 1012 */     require(2, null, null);
/* 1013 */     int level = 1;
/* 1014 */     while (level > 0) {
/* 1015 */       int eventType = next();
/* 1016 */       if (eventType == 3) {
/* 1017 */         level--; continue;
/* 1018 */       }  if (eventType == 2) {
/* 1019 */         level++;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextText() throws XmlPullParserException, IOException {
/* 1054 */     if (getEventType() != 2) {
/* 1055 */       throw new XmlPullParserException("parser must be on START_TAG to read next text", this, null);
/*      */     }
/*      */     
/* 1058 */     int eventType = next();
/* 1059 */     if (eventType == 4) {
/* 1060 */       String result = getText();
/* 1061 */       eventType = next();
/* 1062 */       if (eventType != 3) {
/* 1063 */         throw new XmlPullParserException("TEXT must be immediately followed by END_TAG and not " + XmlPullParser.TYPES[getEventType()], this, null);
/*      */       }
/*      */ 
/*      */       
/* 1067 */       return result;
/* 1068 */     }  if (eventType == 3) {
/* 1069 */       return "";
/*      */     }
/* 1071 */     throw new XmlPullParserException("parser must be on START_TAG or TEXT to read text", this, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextTag() throws XmlPullParserException, IOException {
/* 1078 */     next();
/* 1079 */     if (this.eventType == 4 && isWhitespace()) {
/* 1080 */       next();
/*      */     }
/* 1082 */     if (this.eventType != 2 && this.eventType != 3) {
/* 1083 */       throw new XmlPullParserException("expected START_TAG or END_TAG not " + XmlPullParser.TYPES[getEventType()], this, null);
/*      */     }
/*      */     
/* 1086 */     return this.eventType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int next() throws XmlPullParserException, IOException {
/* 1092 */     this.tokenize = false;
/* 1093 */     return nextImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextToken() throws XmlPullParserException, IOException {
/* 1099 */     this.tokenize = true;
/* 1100 */     return nextImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int nextImpl() throws XmlPullParserException, IOException {
/* 1107 */     this.text = null;
/* 1108 */     this.pcEnd = this.pcStart = 0;
/* 1109 */     this.usePC = false;
/* 1110 */     this.bufStart = this.posEnd;
/* 1111 */     if (this.pastEndTag) {
/* 1112 */       this.pastEndTag = false;
/* 1113 */       this.depth--;
/* 1114 */       this.namespaceEnd = this.elNamespaceCount[this.depth];
/*      */     } 
/* 1116 */     if (this.emptyElementTag) {
/* 1117 */       this.emptyElementTag = false;
/* 1118 */       this.pastEndTag = true;
/* 1119 */       return this.eventType = 3;
/*      */     } 
/*      */ 
/*      */     
/* 1123 */     if (this.depth > 0) {
/*      */       char ch;
/* 1125 */       if (this.seenStartTag) {
/* 1126 */         this.seenStartTag = false;
/* 1127 */         return this.eventType = parseStartTag();
/*      */       } 
/* 1129 */       if (this.seenEndTag) {
/* 1130 */         this.seenEndTag = false;
/* 1131 */         return this.eventType = parseEndTag();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1137 */       if (this.seenMarkup) {
/* 1138 */         this.seenMarkup = false;
/* 1139 */         ch = '<';
/* 1140 */       } else if (this.seenAmpersand) {
/* 1141 */         this.seenAmpersand = false;
/* 1142 */         ch = '&';
/*      */       } else {
/* 1144 */         ch = more();
/*      */       } 
/* 1146 */       this.posStart = this.pos - 1;
/*      */ 
/*      */       
/* 1149 */       boolean hadCharData = false;
/*      */ 
/*      */       
/* 1152 */       boolean needsMerging = false;
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/* 1157 */         if (ch == '<') {
/* 1158 */           if (hadCharData)
/*      */           {
/* 1160 */             if (this.tokenize) {
/* 1161 */               this.seenMarkup = true;
/* 1162 */               return this.eventType = 4;
/*      */             } 
/*      */           }
/* 1165 */           ch = more();
/* 1166 */           if (ch == '/') {
/* 1167 */             if (!this.tokenize && hadCharData) {
/* 1168 */               this.seenEndTag = true;
/*      */               
/* 1170 */               return this.eventType = 4;
/*      */             } 
/* 1172 */             return this.eventType = parseEndTag();
/* 1173 */           }  if (ch == '!') {
/* 1174 */             ch = more();
/* 1175 */             if (ch == '-') {
/*      */               
/* 1177 */               parseComment();
/* 1178 */               if (this.tokenize) return this.eventType = 9; 
/* 1179 */               if (!this.usePC && hadCharData) {
/* 1180 */                 needsMerging = true;
/*      */               } else {
/* 1182 */                 this.posStart = this.pos;
/*      */               } 
/* 1184 */             } else if (ch == '[') {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1189 */               parseCDSect(hadCharData);
/* 1190 */               if (this.tokenize) return this.eventType = 5; 
/* 1191 */               int cdStart = this.posStart;
/* 1192 */               int cdEnd = this.posEnd;
/* 1193 */               int cdLen = cdEnd - cdStart;
/*      */ 
/*      */               
/* 1196 */               if (cdLen > 0) {
/* 1197 */                 hadCharData = true;
/* 1198 */                 if (!this.usePC) {
/* 1199 */                   needsMerging = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1241 */               throw new XmlPullParserException("unexpected character in markup " + printable(ch), this, null);
/*      */             }
/*      */           
/* 1244 */           } else if (ch == '?') {
/* 1245 */             parsePI();
/* 1246 */             if (this.tokenize) return this.eventType = 8; 
/* 1247 */             if (!this.usePC && hadCharData) {
/* 1248 */               needsMerging = true;
/*      */             } else {
/* 1250 */               this.posStart = this.pos;
/*      */             } 
/*      */           } else {
/* 1253 */             if (isNameStartChar(ch)) {
/* 1254 */               if (!this.tokenize && hadCharData) {
/* 1255 */                 this.seenStartTag = true;
/*      */                 
/* 1257 */                 return this.eventType = 4;
/*      */               } 
/* 1259 */               return this.eventType = parseStartTag();
/*      */             } 
/* 1261 */             throw new XmlPullParserException("unexpected character in markup " + printable(ch), this, null);
/*      */           
/*      */           }
/*      */         
/*      */         }
/* 1266 */         else if (ch == '&') {
/*      */ 
/*      */           
/* 1269 */           if (this.tokenize && hadCharData) {
/* 1270 */             this.seenAmpersand = true;
/* 1271 */             return this.eventType = 4;
/*      */           } 
/* 1273 */           int oldStart = this.posStart + this.bufAbsoluteStart;
/* 1274 */           int oldEnd = this.posEnd + this.bufAbsoluteStart;
/* 1275 */           char[] resolvedEntity = parseEntityRef();
/* 1276 */           if (this.tokenize) return this.eventType = 6;
/*      */           
/* 1278 */           if (resolvedEntity == null) {
/* 1279 */             if (this.entityRefName == null) {
/* 1280 */               this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
/*      */             }
/* 1282 */             throw new XmlPullParserException("could not resolve entity named '" + printable(this.entityRefName) + "'", this, null);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1288 */           this.posStart = oldStart - this.bufAbsoluteStart;
/* 1289 */           this.posEnd = oldEnd - this.bufAbsoluteStart;
/* 1290 */           if (!this.usePC) {
/* 1291 */             if (hadCharData) {
/* 1292 */               joinPC();
/* 1293 */               needsMerging = false;
/*      */             } else {
/* 1295 */               this.usePC = true;
/* 1296 */               this.pcStart = this.pcEnd = 0;
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/* 1301 */           for (int i = 0; i < resolvedEntity.length; i++) {
/*      */             
/* 1303 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1304 */             this.pc[this.pcEnd++] = resolvedEntity[i];
/*      */           } 
/*      */           
/* 1307 */           hadCharData = true;
/*      */         }
/*      */         else {
/*      */           
/* 1311 */           if (needsMerging) {
/*      */             
/* 1313 */             joinPC();
/*      */             
/* 1315 */             needsMerging = false;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1326 */           hadCharData = true;
/*      */           
/* 1328 */           boolean normalizedCR = false;
/* 1329 */           boolean normalizeInput = (!this.tokenize || !this.roundtripSupported);
/*      */           
/* 1331 */           boolean seenBracket = false;
/* 1332 */           boolean seenBracketBracket = false;
/*      */ 
/*      */           
/*      */           do {
/* 1336 */             if (ch == ']')
/* 1337 */             { if (seenBracket) {
/* 1338 */                 seenBracketBracket = true;
/*      */               } else {
/* 1340 */                 seenBracket = true;
/*      */               }  }
/* 1342 */             else { if (seenBracketBracket && ch == '>') {
/* 1343 */                 throw new XmlPullParserException("characters ]]> are not allowed in content", this, null);
/*      */               }
/*      */               
/* 1346 */               if (seenBracket) {
/* 1347 */                 seenBracketBracket = seenBracket = false;
/*      */               } }
/*      */ 
/*      */             
/* 1351 */             if (normalizeInput)
/*      */             {
/* 1353 */               if (ch == '\r') {
/* 1354 */                 normalizedCR = true;
/* 1355 */                 this.posEnd = this.pos - 1;
/*      */                 
/* 1357 */                 if (!this.usePC) {
/* 1358 */                   if (this.posEnd > this.posStart) {
/* 1359 */                     joinPC();
/*      */                   } else {
/* 1361 */                     this.usePC = true;
/* 1362 */                     this.pcStart = this.pcEnd = 0;
/*      */                   } 
/*      */                 }
/*      */                 
/* 1366 */                 if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1367 */                 this.pc[this.pcEnd++] = '\n';
/* 1368 */               } else if (ch == '\n') {
/*      */                 
/* 1370 */                 if (!normalizedCR && this.usePC) {
/* 1371 */                   if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1372 */                   this.pc[this.pcEnd++] = '\n';
/*      */                 } 
/* 1374 */                 normalizedCR = false;
/*      */               } else {
/* 1376 */                 if (this.usePC) {
/* 1377 */                   if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1378 */                   this.pc[this.pcEnd++] = ch;
/*      */                 } 
/* 1380 */                 normalizedCR = false;
/*      */               } 
/*      */             }
/*      */             
/* 1384 */             ch = more();
/* 1385 */           } while (ch != '<' && ch != '&');
/* 1386 */           this.posEnd = this.pos - 1;
/*      */           continue;
/*      */         } 
/* 1389 */         ch = more();
/*      */       } 
/*      */     } 
/* 1392 */     if (this.seenRoot) {
/* 1393 */       return parseEpilog();
/*      */     }
/* 1395 */     return parseProlog();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int parseProlog() throws XmlPullParserException, IOException {
/*      */     char ch;
/* 1407 */     if (this.seenMarkup) {
/* 1408 */       ch = this.buf[this.pos - 1];
/*      */     } else {
/* 1410 */       ch = more();
/*      */     } 
/*      */     
/* 1413 */     if (this.eventType == 0) {
/*      */ 
/*      */ 
/*      */       
/* 1417 */       if (ch == '￾') {
/* 1418 */         throw new XmlPullParserException("first character in input was UNICODE noncharacter (0xFFFE)- input requires int swapping", this, null);
/*      */       }
/*      */ 
/*      */       
/* 1422 */       if (ch == '﻿')
/*      */       {
/* 1424 */         ch = more();
/*      */       }
/*      */     } 
/* 1427 */     this.seenMarkup = false;
/* 1428 */     boolean gotS = false;
/* 1429 */     this.posStart = this.pos - 1;
/* 1430 */     boolean normalizeIgnorableWS = (this.tokenize == true && !this.roundtripSupported);
/* 1431 */     boolean normalizedCR = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 1437 */       if (ch == '<') {
/* 1438 */         if (gotS && this.tokenize) {
/* 1439 */           this.posEnd = this.pos - 1;
/* 1440 */           this.seenMarkup = true;
/* 1441 */           return this.eventType = 7;
/*      */         } 
/* 1443 */         ch = more();
/* 1444 */         if (ch == '?') {
/*      */ 
/*      */           
/* 1447 */           if (parsePI()) {
/* 1448 */             if (this.tokenize) {
/* 1449 */               return this.eventType = 8;
/*      */             }
/*      */           } else {
/*      */             
/* 1453 */             this.posStart = this.pos;
/* 1454 */             gotS = false;
/*      */           }
/*      */         
/* 1457 */         } else if (ch == '!') {
/* 1458 */           ch = more();
/* 1459 */           if (ch == 'D') {
/* 1460 */             if (this.seenDocdecl) {
/* 1461 */               throw new XmlPullParserException("only one docdecl allowed in XML document", this, null);
/*      */             }
/*      */             
/* 1464 */             this.seenDocdecl = true;
/* 1465 */             parseDocdecl();
/* 1466 */             if (this.tokenize) return this.eventType = 10; 
/* 1467 */           } else if (ch == '-') {
/* 1468 */             parseComment();
/* 1469 */             if (this.tokenize) return this.eventType = 9; 
/*      */           } else {
/* 1471 */             throw new XmlPullParserException("unexpected markup <!" + printable(ch), this, null);
/*      */           } 
/*      */         } else {
/* 1474 */           if (ch == '/') {
/* 1475 */             throw new XmlPullParserException("expected start tag name and not " + printable(ch), this, null);
/*      */           }
/* 1477 */           if (isNameStartChar(ch)) {
/* 1478 */             this.seenRoot = true;
/* 1479 */             return parseStartTag();
/*      */           } 
/* 1481 */           throw new XmlPullParserException("expected start tag name and not " + printable(ch), this, null);
/*      */         }
/*      */       
/* 1484 */       } else if (isS(ch)) {
/* 1485 */         gotS = true;
/* 1486 */         if (normalizeIgnorableWS) {
/* 1487 */           if (ch == '\r') {
/* 1488 */             normalizedCR = true;
/*      */ 
/*      */ 
/*      */             
/* 1492 */             if (!this.usePC) {
/* 1493 */               this.posEnd = this.pos - 1;
/* 1494 */               if (this.posEnd > this.posStart) {
/* 1495 */                 joinPC();
/*      */               } else {
/* 1497 */                 this.usePC = true;
/* 1498 */                 this.pcStart = this.pcEnd = 0;
/*      */               } 
/*      */             } 
/*      */             
/* 1502 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1503 */             this.pc[this.pcEnd++] = '\n';
/* 1504 */           } else if (ch == '\n') {
/* 1505 */             if (!normalizedCR && this.usePC) {
/* 1506 */               if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1507 */               this.pc[this.pcEnd++] = '\n';
/*      */             } 
/* 1509 */             normalizedCR = false;
/*      */           } else {
/* 1511 */             if (this.usePC) {
/* 1512 */               if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1513 */               this.pc[this.pcEnd++] = ch;
/*      */             } 
/* 1515 */             normalizedCR = false;
/*      */           } 
/*      */         }
/*      */       } else {
/* 1519 */         throw new XmlPullParserException("only whitespace content allowed before start tag and not " + printable(ch), this, null);
/*      */       } 
/*      */ 
/*      */       
/* 1523 */       ch = more();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int parseEpilog() throws XmlPullParserException, IOException {
/* 1530 */     if (this.eventType == 1) {
/* 1531 */       throw new XmlPullParserException("already reached end of XML input", this, null);
/*      */     }
/* 1533 */     if (this.reachedEnd) {
/* 1534 */       return this.eventType = 1;
/*      */     }
/* 1536 */     boolean gotS = false;
/* 1537 */     boolean normalizeIgnorableWS = (this.tokenize == true && !this.roundtripSupported);
/* 1538 */     boolean normalizedCR = false;
/*      */     
/*      */     try {
/*      */       char ch;
/* 1542 */       if (this.seenMarkup) {
/* 1543 */         ch = this.buf[this.pos - 1];
/*      */       } else {
/* 1545 */         ch = more();
/*      */       } 
/* 1547 */       this.seenMarkup = false;
/* 1548 */       this.posStart = this.pos - 1;
/* 1549 */       if (!this.reachedEnd) {
/*      */         
/*      */         do {
/*      */           
/* 1553 */           if (ch == '<') {
/* 1554 */             if (gotS && this.tokenize) {
/* 1555 */               this.posEnd = this.pos - 1;
/* 1556 */               this.seenMarkup = true;
/* 1557 */               return this.eventType = 7;
/*      */             } 
/* 1559 */             ch = more();
/* 1560 */             if (this.reachedEnd) {
/*      */               break;
/*      */             }
/* 1563 */             if (ch == '?') {
/*      */ 
/*      */               
/* 1566 */               parsePI();
/* 1567 */               if (this.tokenize) return this.eventType = 8;
/*      */             
/* 1569 */             } else if (ch == '!') {
/* 1570 */               ch = more();
/* 1571 */               if (this.reachedEnd) {
/*      */                 break;
/*      */               }
/* 1574 */               if (ch == 'D') {
/* 1575 */                 parseDocdecl();
/* 1576 */                 if (this.tokenize) return this.eventType = 10; 
/* 1577 */               } else if (ch == '-') {
/* 1578 */                 parseComment();
/* 1579 */                 if (this.tokenize) return this.eventType = 9; 
/*      */               } else {
/* 1581 */                 throw new XmlPullParserException("unexpected markup <!" + printable(ch), this, null);
/*      */               } 
/*      */             } else {
/* 1584 */               if (ch == '/') {
/* 1585 */                 throw new XmlPullParserException("end tag not allowed in epilog but got " + printable(ch), this, null);
/*      */               }
/* 1587 */               if (isNameStartChar(ch)) {
/* 1588 */                 throw new XmlPullParserException("start tag not allowed in epilog but got " + printable(ch), this, null);
/*      */               }
/*      */               
/* 1591 */               throw new XmlPullParserException("in epilog expected ignorable content and not " + printable(ch), this, null);
/*      */             }
/*      */           
/*      */           }
/* 1595 */           else if (isS(ch)) {
/* 1596 */             gotS = true;
/* 1597 */             if (normalizeIgnorableWS) {
/* 1598 */               if (ch == '\r') {
/* 1599 */                 normalizedCR = true;
/*      */ 
/*      */ 
/*      */                 
/* 1603 */                 if (!this.usePC) {
/* 1604 */                   this.posEnd = this.pos - 1;
/* 1605 */                   if (this.posEnd > this.posStart) {
/* 1606 */                     joinPC();
/*      */                   } else {
/* 1608 */                     this.usePC = true;
/* 1609 */                     this.pcStart = this.pcEnd = 0;
/*      */                   } 
/*      */                 } 
/*      */                 
/* 1613 */                 if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1614 */                 this.pc[this.pcEnd++] = '\n';
/* 1615 */               } else if (ch == '\n') {
/* 1616 */                 if (!normalizedCR && this.usePC) {
/* 1617 */                   if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1618 */                   this.pc[this.pcEnd++] = '\n';
/*      */                 } 
/* 1620 */                 normalizedCR = false;
/*      */               } else {
/* 1622 */                 if (this.usePC) {
/* 1623 */                   if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 1624 */                   this.pc[this.pcEnd++] = ch;
/*      */                 } 
/* 1626 */                 normalizedCR = false;
/*      */               } 
/*      */             }
/*      */           } else {
/* 1630 */             throw new XmlPullParserException("in epilog non whitespace content is not allowed but got " + printable(ch), this, null);
/*      */           } 
/*      */ 
/*      */           
/* 1634 */           ch = more();
/* 1635 */         } while (!this.reachedEnd);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1645 */     catch (EOFException ex) {
/* 1646 */       this.reachedEnd = true;
/*      */     } 
/* 1648 */     if (this.reachedEnd) {
/* 1649 */       if (this.tokenize && gotS) {
/* 1650 */         this.posEnd = this.pos;
/* 1651 */         return this.eventType = 7;
/*      */       } 
/* 1653 */       return this.eventType = 1;
/*      */     } 
/* 1655 */     throw new XmlPullParserException("internal error in parseEpilog");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int parseEndTag() throws XmlPullParserException, IOException {
/* 1663 */     char ch = more();
/* 1664 */     if (!isNameStartChar(ch)) {
/* 1665 */       throw new XmlPullParserException("expected name start and not " + printable(ch), this, null);
/*      */     }
/*      */     
/* 1668 */     this.posStart = this.pos - 3;
/* 1669 */     int nameStart = this.pos - 1 + this.bufAbsoluteStart;
/*      */     do {
/* 1671 */       ch = more();
/* 1672 */     } while (isNameChar(ch));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1681 */     int off = nameStart - this.bufAbsoluteStart;
/*      */     
/* 1683 */     int len = this.pos - 1 - off;
/* 1684 */     char[] cbuf = this.elRawName[this.depth];
/* 1685 */     if (this.elRawNameEnd[this.depth] != len) {
/*      */       
/* 1687 */       String startname = new String(cbuf, 0, this.elRawNameEnd[this.depth]);
/* 1688 */       String endname = new String(this.buf, off, len);
/* 1689 */       throw new XmlPullParserException("end tag name </" + endname + "> must match start tag name <" + startname + ">" + " from line " + this.elRawNameLine[this.depth], this, null);
/*      */     } 
/*      */ 
/*      */     
/* 1693 */     for (int i = 0; i < len; i++) {
/*      */       
/* 1695 */       if (this.buf[off++] != cbuf[i]) {
/*      */         
/* 1697 */         String startname = new String(cbuf, 0, len);
/* 1698 */         String endname = new String(this.buf, off - i - 1, len);
/* 1699 */         throw new XmlPullParserException("end tag name </" + endname + "> must be the same as start tag <" + startname + ">" + " from line " + this.elRawNameLine[this.depth], this, null);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1705 */     for (; isS(ch); ch = more());
/* 1706 */     if (ch != '>') {
/* 1707 */       throw new XmlPullParserException("expected > to finish end tag not " + printable(ch) + " from line " + this.elRawNameLine[this.depth], this, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1715 */     this.posEnd = this.pos;
/* 1716 */     this.pastEndTag = true;
/* 1717 */     return this.eventType = 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int parseStartTag() throws XmlPullParserException, IOException {
/* 1724 */     this.depth++;
/*      */     
/* 1726 */     this.posStart = this.pos - 2;
/*      */     
/* 1728 */     this.emptyElementTag = false;
/* 1729 */     this.attributeCount = 0;
/*      */     
/* 1731 */     int nameStart = this.pos - 1 + this.bufAbsoluteStart;
/* 1732 */     int colonPos = -1;
/* 1733 */     char ch = this.buf[this.pos - 1];
/* 1734 */     if (ch == ':' && this.processNamespaces) throw new XmlPullParserException("when namespaces processing enabled colon can not be at element name start", this, null);
/*      */ 
/*      */     
/*      */     while (true) {
/* 1738 */       ch = more();
/* 1739 */       if (!isNameChar(ch))
/* 1740 */         break;  if (ch == ':' && this.processNamespaces) {
/* 1741 */         if (colonPos != -1) throw new XmlPullParserException("only one colon is allowed in name of element when namespaces are enabled", this, null);
/*      */ 
/*      */         
/* 1744 */         colonPos = this.pos - 1 + this.bufAbsoluteStart;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1749 */     ensureElementsCapacity();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1754 */     int elLen = this.pos - 1 - nameStart - this.bufAbsoluteStart;
/* 1755 */     if (this.elRawName[this.depth] == null || (this.elRawName[this.depth]).length < elLen) {
/* 1756 */       this.elRawName[this.depth] = new char[2 * elLen];
/*      */     }
/* 1758 */     System.arraycopy(this.buf, nameStart - this.bufAbsoluteStart, this.elRawName[this.depth], 0, elLen);
/* 1759 */     this.elRawNameEnd[this.depth] = elLen;
/* 1760 */     this.elRawNameLine[this.depth] = this.lineNumber;
/*      */     
/* 1762 */     String name = null;
/*      */ 
/*      */     
/* 1765 */     String prefix = null;
/* 1766 */     if (this.processNamespaces) {
/* 1767 */       if (colonPos != -1) {
/* 1768 */         prefix = this.elPrefix[this.depth] = newString(this.buf, nameStart - this.bufAbsoluteStart, colonPos - nameStart);
/*      */         
/* 1770 */         name = this.elName[this.depth] = newString(this.buf, colonPos + 1 - this.bufAbsoluteStart, this.pos - 2 - colonPos - this.bufAbsoluteStart);
/*      */       }
/*      */       else {
/*      */         
/* 1774 */         prefix = this.elPrefix[this.depth] = null;
/* 1775 */         name = this.elName[this.depth] = newString(this.buf, nameStart - this.bufAbsoluteStart, elLen);
/*      */       } 
/*      */     } else {
/*      */       
/* 1779 */       name = this.elName[this.depth] = newString(this.buf, nameStart - this.bufAbsoluteStart, elLen);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 1786 */       for (; isS(ch); ch = more());
/*      */       
/* 1788 */       if (ch == '>')
/*      */         break; 
/* 1790 */       if (ch == '/') {
/* 1791 */         if (this.emptyElementTag) throw new XmlPullParserException("repeated / in tag declaration", this, null);
/*      */         
/* 1793 */         this.emptyElementTag = true;
/* 1794 */         ch = more();
/* 1795 */         if (ch != '>') throw new XmlPullParserException("expected > to end empty tag not " + printable(ch), this, null); 
/*      */         break;
/*      */       } 
/* 1798 */       if (isNameStartChar(ch)) {
/* 1799 */         ch = parseAttribute();
/* 1800 */         ch = more();
/*      */         continue;
/*      */       } 
/* 1803 */       throw new XmlPullParserException("start tag unexpected character " + printable(ch), this, null);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1810 */     if (this.processNamespaces) {
/* 1811 */       String uri = getNamespace(prefix);
/* 1812 */       if (uri == null) {
/* 1813 */         if (prefix == null) {
/* 1814 */           uri = "";
/*      */         } else {
/* 1816 */           throw new XmlPullParserException("could not determine namespace bound to element prefix " + prefix, this, null);
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1822 */       this.elUri[this.depth] = uri;
/*      */ 
/*      */ 
/*      */       
/*      */       int i;
/*      */ 
/*      */ 
/*      */       
/* 1830 */       for (i = 0; i < this.attributeCount; i++) {
/*      */         
/* 1832 */         String attrPrefix = this.attributePrefix[i];
/* 1833 */         if (attrPrefix != null) {
/* 1834 */           String attrUri = getNamespace(attrPrefix);
/* 1835 */           if (attrUri == null) {
/* 1836 */             throw new XmlPullParserException("could not determine namespace bound to attribute prefix " + attrPrefix, this, null);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1841 */           this.attributeUri[i] = attrUri;
/*      */         } else {
/* 1843 */           this.attributeUri[i] = "";
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1851 */       for (i = 1; i < this.attributeCount; i++)
/*      */       {
/* 1853 */         for (int j = 0; j < i; j++)
/*      */         {
/* 1855 */           if (this.attributeUri[j] == this.attributeUri[i] && ((this.allStringsInterned && this.attributeName[j].equals(this.attributeName[i])) || (!this.allStringsInterned && this.attributeNameHash[j] == this.attributeNameHash[i] && this.attributeName[j].equals(this.attributeName[i]))))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1863 */             String attr1 = this.attributeName[j];
/* 1864 */             if (this.attributeUri[j] != null) attr1 = this.attributeUri[j] + ":" + attr1; 
/* 1865 */             String attr2 = this.attributeName[i];
/* 1866 */             if (this.attributeUri[i] != null) attr2 = this.attributeUri[i] + ":" + attr2; 
/* 1867 */             throw new XmlPullParserException("duplicated attributes " + attr1 + " and " + attr2, this, null);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1878 */       for (int i = 1; i < this.attributeCount; i++) {
/*      */         
/* 1880 */         for (int j = 0; j < i; j++) {
/*      */           
/* 1882 */           if ((this.allStringsInterned && this.attributeName[j].equals(this.attributeName[i])) || (!this.allStringsInterned && this.attributeNameHash[j] == this.attributeNameHash[i] && this.attributeName[j].equals(this.attributeName[i]))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1889 */             String attr1 = this.attributeName[j];
/* 1890 */             String attr2 = this.attributeName[i];
/* 1891 */             throw new XmlPullParserException("duplicated attributes " + attr1 + " and " + attr2, this, null);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1898 */     this.elNamespaceCount[this.depth] = this.namespaceEnd;
/* 1899 */     this.posEnd = this.pos;
/* 1900 */     return this.eventType = 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected char parseAttribute() throws XmlPullParserException, IOException {
/* 1909 */     int prevPosStart = this.posStart + this.bufAbsoluteStart;
/* 1910 */     int nameStart = this.pos - 1 + this.bufAbsoluteStart;
/* 1911 */     int colonPos = -1;
/* 1912 */     char ch = this.buf[this.pos - 1];
/* 1913 */     if (ch == ':' && this.processNamespaces) throw new XmlPullParserException("when namespaces processing enabled colon can not be at attribute name start", this, null);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1918 */     boolean startsWithXmlns = (this.processNamespaces && ch == 'x');
/* 1919 */     int xmlnsPos = 0;
/*      */     
/* 1921 */     ch = more();
/* 1922 */     while (isNameChar(ch)) {
/* 1923 */       if (this.processNamespaces) {
/* 1924 */         if (startsWithXmlns && xmlnsPos < 5) {
/* 1925 */           xmlnsPos++;
/* 1926 */           if (xmlnsPos == 1) { if (ch != 'm') startsWithXmlns = false;  }
/* 1927 */           else if (xmlnsPos == 2) { if (ch != 'l') startsWithXmlns = false;  }
/* 1928 */           else if (xmlnsPos == 3) { if (ch != 'n') startsWithXmlns = false;  }
/* 1929 */           else if (xmlnsPos == 4) { if (ch != 's') startsWithXmlns = false;  }
/* 1930 */           else if (xmlnsPos == 5 && 
/* 1931 */             ch != ':') { throw new XmlPullParserException("after xmlns in attribute name must be colonwhen namespaces are enabled", this, null); }
/*      */         
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1937 */         if (ch == ':') {
/* 1938 */           if (colonPos != -1) throw new XmlPullParserException("only one colon is allowed in attribute name when namespaces are enabled", this, null);
/*      */ 
/*      */           
/* 1941 */           colonPos = this.pos - 1 + this.bufAbsoluteStart;
/*      */         } 
/*      */       } 
/* 1944 */       ch = more();
/*      */     } 
/*      */     
/* 1947 */     ensureAttributesCapacity(this.attributeCount);
/*      */ 
/*      */     
/* 1950 */     String name = null;
/* 1951 */     String prefix = null;
/*      */     
/* 1953 */     if (this.processNamespaces) {
/* 1954 */       if (xmlnsPos < 4) startsWithXmlns = false; 
/* 1955 */       if (startsWithXmlns) {
/* 1956 */         if (colonPos != -1)
/*      */         {
/* 1958 */           int nameLen = this.pos - 2 - colonPos - this.bufAbsoluteStart;
/* 1959 */           if (nameLen == 0) {
/* 1960 */             throw new XmlPullParserException("namespace prefix is required after xmlns:  when namespaces are enabled", this, null);
/*      */           }
/*      */ 
/*      */           
/* 1964 */           name = newString(this.buf, colonPos - this.bufAbsoluteStart + 1, nameLen);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1969 */         if (colonPos != -1) {
/* 1970 */           int prefixLen = colonPos - nameStart;
/* 1971 */           prefix = this.attributePrefix[this.attributeCount] = newString(this.buf, nameStart - this.bufAbsoluteStart, prefixLen);
/*      */ 
/*      */           
/* 1974 */           int nameLen = this.pos - 2 - colonPos - this.bufAbsoluteStart;
/* 1975 */           name = this.attributeName[this.attributeCount] = newString(this.buf, colonPos - this.bufAbsoluteStart + 1, nameLen);
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 1981 */           prefix = this.attributePrefix[this.attributeCount] = null;
/* 1982 */           name = this.attributeName[this.attributeCount] = newString(this.buf, nameStart - this.bufAbsoluteStart, this.pos - 1 - nameStart - this.bufAbsoluteStart);
/*      */         } 
/*      */ 
/*      */         
/* 1986 */         if (!this.allStringsInterned) {
/* 1987 */           this.attributeNameHash[this.attributeCount] = name.hashCode();
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1993 */       name = this.attributeName[this.attributeCount] = newString(this.buf, nameStart - this.bufAbsoluteStart, this.pos - 1 - nameStart - this.bufAbsoluteStart);
/*      */ 
/*      */ 
/*      */       
/* 1997 */       if (!this.allStringsInterned) {
/* 1998 */         this.attributeNameHash[this.attributeCount] = name.hashCode();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2003 */     for (; isS(ch); ch = more());
/* 2004 */     if (ch != '=') throw new XmlPullParserException("expected = after attribute name", this, null);
/*      */     
/* 2006 */     ch = more();
/* 2007 */     for (; isS(ch); ch = more());
/*      */ 
/*      */ 
/*      */     
/* 2011 */     char delimit = ch;
/* 2012 */     if (delimit != '"' && delimit != '\'') throw new XmlPullParserException("attribute value must start with quotation or apostrophe not " + printable(delimit), this, null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2020 */     boolean normalizedCR = false;
/* 2021 */     this.usePC = false;
/* 2022 */     this.pcStart = this.pcEnd;
/* 2023 */     this.posStart = this.pos;
/*      */     
/*      */     while (true) {
/* 2026 */       ch = more();
/* 2027 */       if (ch == delimit)
/*      */         break; 
/* 2029 */       if (ch == '<') {
/* 2030 */         throw new XmlPullParserException("markup not allowed inside attribute value - illegal < ", this, null);
/*      */       }
/* 2032 */       if (ch == '&') {
/*      */         
/* 2034 */         this.posEnd = this.pos - 1;
/* 2035 */         if (!this.usePC) {
/* 2036 */           boolean hadCharData = (this.posEnd > this.posStart);
/* 2037 */           if (hadCharData) {
/*      */             
/* 2039 */             joinPC();
/*      */           } else {
/* 2041 */             this.usePC = true;
/* 2042 */             this.pcStart = this.pcEnd = 0;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2047 */         char[] resolvedEntity = parseEntityRef();
/*      */         
/* 2049 */         if (resolvedEntity == null) {
/* 2050 */           if (this.entityRefName == null) {
/* 2051 */             this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
/*      */           }
/* 2053 */           throw new XmlPullParserException("could not resolve entity named '" + printable(this.entityRefName) + "'", this, null);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2058 */         for (int i = 0; i < resolvedEntity.length; i++) {
/*      */           
/* 2060 */           if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2061 */           this.pc[this.pcEnd++] = resolvedEntity[i];
/*      */         } 
/* 2063 */       } else if (ch == '\t' || ch == '\n' || ch == '\r') {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2068 */         if (!this.usePC) {
/* 2069 */           this.posEnd = this.pos - 1;
/* 2070 */           if (this.posEnd > this.posStart) {
/* 2071 */             joinPC();
/*      */           } else {
/* 2073 */             this.usePC = true;
/* 2074 */             this.pcEnd = this.pcStart = 0;
/*      */           } 
/*      */         } 
/*      */         
/* 2078 */         if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2079 */         if (ch != '\n' || !normalizedCR) {
/* 2080 */           this.pc[this.pcEnd++] = ' ';
/*      */         
/*      */         }
/*      */       }
/* 2084 */       else if (this.usePC) {
/* 2085 */         if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2086 */         this.pc[this.pcEnd++] = ch;
/*      */       } 
/*      */       
/* 2089 */       normalizedCR = (ch == '\r');
/*      */     } 
/*      */ 
/*      */     
/* 2093 */     if (this.processNamespaces && startsWithXmlns) {
/* 2094 */       String ns = null;
/* 2095 */       if (!this.usePC) {
/* 2096 */         ns = newStringIntern(this.buf, this.posStart, this.pos - 1 - this.posStart);
/*      */       } else {
/* 2098 */         ns = newStringIntern(this.pc, this.pcStart, this.pcEnd - this.pcStart);
/*      */       } 
/* 2100 */       ensureNamespacesCapacity(this.namespaceEnd);
/* 2101 */       int prefixHash = -1;
/* 2102 */       if (colonPos != -1) {
/* 2103 */         if (ns.length() == 0) {
/* 2104 */           throw new XmlPullParserException("non-default namespace can not be declared to be empty string", this, null);
/*      */         }
/*      */ 
/*      */         
/* 2108 */         this.namespacePrefix[this.namespaceEnd] = name;
/* 2109 */         if (!this.allStringsInterned) {
/* 2110 */           prefixHash = this.namespacePrefixHash[this.namespaceEnd] = name.hashCode();
/*      */         }
/*      */       } else {
/*      */         
/* 2114 */         this.namespacePrefix[this.namespaceEnd] = null;
/* 2115 */         if (!this.allStringsInterned) {
/* 2116 */           prefixHash = this.namespacePrefixHash[this.namespaceEnd] = -1;
/*      */         }
/*      */       } 
/* 2119 */       this.namespaceUri[this.namespaceEnd] = ns;
/*      */ 
/*      */       
/* 2122 */       int startNs = this.elNamespaceCount[this.depth - 1];
/* 2123 */       for (int i = this.namespaceEnd - 1; i >= startNs; i--) {
/*      */         
/* 2125 */         if (((this.allStringsInterned || name == null) && this.namespacePrefix[i] == name) || (!this.allStringsInterned && name != null && this.namespacePrefixHash[i] == prefixHash && name.equals(this.namespacePrefix[i]))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2131 */           String s = (name == null) ? "default" : ("'" + name + "'");
/* 2132 */           throw new XmlPullParserException("duplicated namespace declaration for " + s + " prefix", this, null);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2137 */       this.namespaceEnd++;
/*      */     } else {
/*      */       
/* 2140 */       if (!this.usePC) {
/* 2141 */         this.attributeValue[this.attributeCount] = new String(this.buf, this.posStart, this.pos - 1 - this.posStart);
/*      */       } else {
/*      */         
/* 2144 */         this.attributeValue[this.attributeCount] = new String(this.pc, this.pcStart, this.pcEnd - this.pcStart);
/*      */       } 
/*      */       
/* 2147 */       this.attributeCount++;
/*      */     } 
/* 2149 */     this.posStart = prevPosStart - this.bufAbsoluteStart;
/* 2150 */     return ch;
/*      */   }
/*      */   public MXParser() {
/* 2153 */     this.charRefOneCharBuf = new char[1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected char[] parseEntityRef() throws XmlPullParserException, IOException {
/* 2162 */     this.entityRefName = null;
/* 2163 */     this.posStart = this.pos;
/* 2164 */     char ch = more();
/* 2165 */     if (ch == '#') {
/*      */       
/* 2167 */       char charRef = Character.MIN_VALUE;
/* 2168 */       ch = more();
/* 2169 */       if (ch == 'x') {
/*      */         
/*      */         while (true) {
/* 2172 */           ch = more();
/* 2173 */           if (ch >= '0' && ch <= '9') {
/* 2174 */             charRef = (char)(charRef * 16 + ch - 48); continue;
/* 2175 */           }  if (ch >= 'a' && ch <= 'f') {
/* 2176 */             charRef = (char)(charRef * 16 + ch - 87); continue;
/* 2177 */           }  if (ch >= 'A' && ch <= 'F')
/* 2178 */           { charRef = (char)(charRef * 16 + ch - 55); continue; }  break;
/* 2179 */         }  if (ch != ';')
/*      */         {
/*      */           
/* 2182 */           throw new XmlPullParserException("character reference (with hex value) may not contain " + printable(ch), this, null);
/*      */         
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*      */         while (true) {
/*      */           
/* 2190 */           if (ch >= '0' && ch <= '9')
/* 2191 */           { charRef = (char)(charRef * 10 + ch - 48); }
/* 2192 */           else { if (ch == ';') {
/*      */               break;
/*      */             }
/* 2195 */             throw new XmlPullParserException("character reference (with decimal value) may not contain " + printable(ch), this, null); }
/*      */ 
/*      */ 
/*      */           
/* 2199 */           ch = more();
/*      */         } 
/*      */       } 
/* 2202 */       this.posEnd = this.pos - 1;
/* 2203 */       this.charRefOneCharBuf[0] = charRef;
/* 2204 */       if (this.tokenize) {
/* 2205 */         this.text = newString(this.charRefOneCharBuf, 0, 1);
/*      */       }
/* 2207 */       return this.charRefOneCharBuf;
/*      */     } 
/*      */ 
/*      */     
/* 2211 */     if (!isNameStartChar(ch)) {
/* 2212 */       throw new XmlPullParserException("entity reference names can not start with character '" + printable(ch) + "'", this, null);
/*      */     }
/*      */ 
/*      */     
/*      */     while (true) {
/* 2217 */       ch = more();
/* 2218 */       if (ch == ';') {
/*      */         break;
/*      */       }
/* 2221 */       if (!isNameChar(ch)) {
/* 2222 */         throw new XmlPullParserException("entity reference name can not contain character " + printable(ch) + "'", this, null);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 2227 */     this.posEnd = this.pos - 1;
/*      */     
/* 2229 */     int len = this.posEnd - this.posStart;
/* 2230 */     if (len == 2 && this.buf[this.posStart] == 'l' && this.buf[this.posStart + 1] == 't') {
/* 2231 */       if (this.tokenize) {
/* 2232 */         this.text = "<";
/*      */       }
/* 2234 */       this.charRefOneCharBuf[0] = '<';
/* 2235 */       return this.charRefOneCharBuf;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2240 */     if (len == 3 && this.buf[this.posStart] == 'a' && this.buf[this.posStart + 1] == 'm' && this.buf[this.posStart + 2] == 'p') {
/*      */       
/* 2242 */       if (this.tokenize) {
/* 2243 */         this.text = "&";
/*      */       }
/* 2245 */       this.charRefOneCharBuf[0] = '&';
/* 2246 */       return this.charRefOneCharBuf;
/* 2247 */     }  if (len == 2 && this.buf[this.posStart] == 'g' && this.buf[this.posStart + 1] == 't') {
/* 2248 */       if (this.tokenize) {
/* 2249 */         this.text = ">";
/*      */       }
/* 2251 */       this.charRefOneCharBuf[0] = '>';
/* 2252 */       return this.charRefOneCharBuf;
/* 2253 */     }  if (len == 4 && this.buf[this.posStart] == 'a' && this.buf[this.posStart + 1] == 'p' && this.buf[this.posStart + 2] == 'o' && this.buf[this.posStart + 3] == 's') {
/*      */ 
/*      */       
/* 2256 */       if (this.tokenize) {
/* 2257 */         this.text = "'";
/*      */       }
/* 2259 */       this.charRefOneCharBuf[0] = '\'';
/* 2260 */       return this.charRefOneCharBuf;
/* 2261 */     }  if (len == 4 && this.buf[this.posStart] == 'q' && this.buf[this.posStart + 1] == 'u' && this.buf[this.posStart + 2] == 'o' && this.buf[this.posStart + 3] == 't') {
/*      */ 
/*      */       
/* 2264 */       if (this.tokenize) {
/* 2265 */         this.text = "\"";
/*      */       }
/* 2267 */       this.charRefOneCharBuf[0] = '"';
/* 2268 */       return this.charRefOneCharBuf;
/*      */     } 
/* 2270 */     char[] result = lookuEntityReplacement(len);
/* 2271 */     if (result != null) {
/* 2272 */       return result;
/*      */     }
/*      */     
/* 2275 */     if (this.tokenize) this.text = null; 
/* 2276 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected char[] lookuEntityReplacement(int entitNameLen) throws XmlPullParserException, IOException {
/* 2284 */     if (!this.allStringsInterned) {
/* 2285 */       int hash = fastHash(this.buf, this.posStart, this.posEnd - this.posStart);
/*      */       int i;
/* 2287 */       label30: for (i = this.entityEnd - 1; i >= 0; i--) {
/*      */         
/* 2289 */         if (hash == this.entityNameHash[i] && entitNameLen == (this.entityNameBuf[i]).length) {
/* 2290 */           char[] entityBuf = this.entityNameBuf[i];
/* 2291 */           for (int j = 0; j < entitNameLen; j++) {
/*      */             
/* 2293 */             if (this.buf[this.posStart + j] != entityBuf[j])
/*      */               continue label30; 
/* 2295 */           }  if (this.tokenize) this.text = this.entityReplacement[i]; 
/* 2296 */           return this.entityReplacementBuf[i];
/*      */         } 
/*      */       } 
/*      */     } else {
/* 2300 */       this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
/* 2301 */       for (int i = this.entityEnd - 1; i >= 0; i--) {
/*      */ 
/*      */         
/* 2304 */         if (this.entityRefName == this.entityName[i]) {
/* 2305 */           if (this.tokenize) this.text = this.entityReplacement[i]; 
/* 2306 */           return this.entityReplacementBuf[i];
/*      */         } 
/*      */       } 
/*      */     } 
/* 2310 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseComment() throws XmlPullParserException, IOException {
/* 2320 */     char ch = more();
/* 2321 */     if (ch != '-') throw new XmlPullParserException("expected <!-- for comment start", this, null);
/*      */     
/* 2323 */     if (this.tokenize) this.posStart = this.pos;
/*      */     
/* 2325 */     int curLine = this.lineNumber;
/* 2326 */     int curColumn = this.columnNumber;
/*      */     try {
/* 2328 */       boolean normalizeIgnorableWS = (this.tokenize == true && !this.roundtripSupported);
/* 2329 */       boolean normalizedCR = false;
/*      */       
/* 2331 */       boolean seenDash = false;
/* 2332 */       boolean seenDashDash = false;
/*      */       
/*      */       while (true) {
/* 2335 */         ch = more();
/* 2336 */         if (seenDashDash && ch != '>') {
/* 2337 */           throw new XmlPullParserException("in comment after two dashes (--) next character must be > not " + printable(ch), this, null);
/*      */         }
/*      */ 
/*      */         
/* 2341 */         if (ch == '-') {
/* 2342 */           if (!seenDash) {
/* 2343 */             seenDash = true;
/*      */           } else {
/* 2345 */             seenDashDash = true;
/* 2346 */             seenDash = false;
/*      */           } 
/* 2348 */         } else if (ch == '>') {
/* 2349 */           if (seenDashDash) {
/*      */             break;
/*      */           }
/* 2352 */           seenDashDash = false;
/*      */           
/* 2354 */           seenDash = false;
/*      */         } else {
/* 2356 */           seenDash = false;
/*      */         } 
/* 2358 */         if (normalizeIgnorableWS) {
/* 2359 */           if (ch == '\r') {
/* 2360 */             normalizedCR = true;
/*      */ 
/*      */ 
/*      */             
/* 2364 */             if (!this.usePC) {
/* 2365 */               this.posEnd = this.pos - 1;
/* 2366 */               if (this.posEnd > this.posStart) {
/* 2367 */                 joinPC();
/*      */               } else {
/* 2369 */                 this.usePC = true;
/* 2370 */                 this.pcStart = this.pcEnd = 0;
/*      */               } 
/*      */             } 
/*      */             
/* 2374 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2375 */             this.pc[this.pcEnd++] = '\n'; continue;
/* 2376 */           }  if (ch == '\n') {
/* 2377 */             if (!normalizedCR && this.usePC) {
/* 2378 */               if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2379 */               this.pc[this.pcEnd++] = '\n';
/*      */             } 
/* 2381 */             normalizedCR = false; continue;
/*      */           } 
/* 2383 */           if (this.usePC) {
/* 2384 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2385 */             this.pc[this.pcEnd++] = ch;
/*      */           } 
/* 2387 */           normalizedCR = false;
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 2392 */     } catch (EOFException ex) {
/*      */       
/* 2394 */       throw new XmlPullParserException("comment started on line " + curLine + " and column " + curColumn + " was not closed", this, ex);
/*      */     } 
/*      */ 
/*      */     
/* 2398 */     if (this.tokenize) {
/* 2399 */       this.posEnd = this.pos - 3;
/* 2400 */       if (this.usePC) {
/* 2401 */         this.pcEnd -= 2;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean parsePI() throws XmlPullParserException, IOException {
/* 2414 */     if (this.tokenize) this.posStart = this.pos; 
/* 2415 */     int curLine = this.lineNumber;
/* 2416 */     int curColumn = this.columnNumber;
/* 2417 */     int piTargetStart = this.pos + this.bufAbsoluteStart;
/* 2418 */     int piTargetEnd = -1;
/* 2419 */     boolean normalizeIgnorableWS = (this.tokenize == true && !this.roundtripSupported);
/* 2420 */     boolean normalizedCR = false;
/*      */     
/*      */     try {
/* 2423 */       boolean seenQ = false;
/* 2424 */       char ch = more();
/* 2425 */       if (isS(ch)) {
/* 2426 */         throw new XmlPullParserException("processing instruction PITarget must be exactly after <? and not white space character", this, null);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/* 2434 */         if (ch == '?') {
/* 2435 */           seenQ = true;
/* 2436 */         } else if (ch == '>') {
/* 2437 */           if (seenQ) {
/*      */             break;
/*      */           }
/* 2440 */           seenQ = false;
/*      */         } else {
/* 2442 */           if (piTargetEnd == -1 && isS(ch)) {
/* 2443 */             piTargetEnd = this.pos - 1 + this.bufAbsoluteStart;
/*      */ 
/*      */             
/* 2446 */             if (piTargetEnd - piTargetStart == 3 && (
/* 2447 */               this.buf[piTargetStart] == 'x' || this.buf[piTargetStart] == 'X') && (this.buf[piTargetStart + 1] == 'm' || this.buf[piTargetStart + 1] == 'M') && (this.buf[piTargetStart + 2] == 'l' || this.buf[piTargetStart + 2] == 'L')) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2452 */               if (piTargetStart > 3) {
/* 2453 */                 throw new XmlPullParserException("processing instruction can not have PITarget with reserveld xml name", this, null);
/*      */               }
/*      */ 
/*      */               
/* 2457 */               if (this.buf[piTargetStart] != 'x' && this.buf[piTargetStart + 1] != 'm' && this.buf[piTargetStart + 2] != 'l')
/*      */               {
/*      */ 
/*      */                 
/* 2461 */                 throw new XmlPullParserException("XMLDecl must have xml name in lowercase", this, null);
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 2466 */               parseXmlDecl(ch);
/* 2467 */               if (this.tokenize) this.posEnd = this.pos - 2; 
/* 2468 */               int off = piTargetStart - this.bufAbsoluteStart + 3;
/* 2469 */               int len = this.pos - 2 - off;
/* 2470 */               this.xmlDeclContent = newString(this.buf, off, len);
/* 2471 */               return false;
/*      */             } 
/*      */           } 
/*      */           
/* 2475 */           seenQ = false;
/*      */         } 
/* 2477 */         if (normalizeIgnorableWS) {
/* 2478 */           if (ch == '\r') {
/* 2479 */             normalizedCR = true;
/*      */ 
/*      */ 
/*      */             
/* 2483 */             if (!this.usePC) {
/* 2484 */               this.posEnd = this.pos - 1;
/* 2485 */               if (this.posEnd > this.posStart) {
/* 2486 */                 joinPC();
/*      */               } else {
/* 2488 */                 this.usePC = true;
/* 2489 */                 this.pcStart = this.pcEnd = 0;
/*      */               } 
/*      */             } 
/*      */             
/* 2493 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2494 */             this.pc[this.pcEnd++] = '\n';
/* 2495 */           } else if (ch == '\n') {
/* 2496 */             if (!normalizedCR && this.usePC) {
/* 2497 */               if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2498 */               this.pc[this.pcEnd++] = '\n';
/*      */             } 
/* 2500 */             normalizedCR = false;
/*      */           } else {
/* 2502 */             if (this.usePC) {
/* 2503 */               if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2504 */               this.pc[this.pcEnd++] = ch;
/*      */             } 
/* 2506 */             normalizedCR = false;
/*      */           } 
/*      */         }
/* 2509 */         ch = more();
/*      */       } 
/* 2511 */     } catch (EOFException ex) {
/*      */       
/* 2513 */       throw new XmlPullParserException("processing instruction started on line " + curLine + " and column " + curColumn + " was not closed", this, ex);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2518 */     if (piTargetEnd == -1) {
/* 2519 */       piTargetEnd = this.pos - 2 + this.bufAbsoluteStart;
/*      */     }
/*      */ 
/*      */     
/* 2523 */     piTargetStart -= this.bufAbsoluteStart;
/* 2524 */     piTargetEnd -= this.bufAbsoluteStart;
/* 2525 */     if (this.tokenize) {
/* 2526 */       this.posEnd = this.pos - 2;
/* 2527 */       if (normalizeIgnorableWS) {
/* 2528 */         this.pcEnd--;
/*      */       }
/*      */     } 
/* 2531 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2540 */   protected static final char[] VERSION = "version".toCharArray();
/* 2541 */   protected static final char[] NCODING = "ncoding".toCharArray();
/* 2542 */   protected static final char[] TANDALONE = "tandalone".toCharArray();
/* 2543 */   protected static final char[] YES = "yes".toCharArray();
/* 2544 */   protected static final char[] NO = "no".toCharArray();
/*      */ 
/*      */   
/*      */   protected static final int LOOKUP_MAX = 1024;
/*      */ 
/*      */   
/*      */   protected static final char LOOKUP_MAX_CHAR = 'Ѐ';
/*      */ 
/*      */   
/*      */   protected void parseXmlDecl(char ch) throws XmlPullParserException, IOException {
/* 2554 */     this.preventBufferCompaction = true;
/* 2555 */     this.bufStart = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2561 */     ch = skipS(ch);
/* 2562 */     ch = requireInput(ch, VERSION);
/*      */     
/* 2564 */     ch = skipS(ch);
/* 2565 */     if (ch != '=') {
/* 2566 */       throw new XmlPullParserException("expected equals sign (=) after version and not " + printable(ch), this, null);
/*      */     }
/*      */     
/* 2569 */     ch = more();
/* 2570 */     ch = skipS(ch);
/* 2571 */     if (ch != '\'' && ch != '"') {
/* 2572 */       throw new XmlPullParserException("expected apostrophe (') or quotation mark (\") after version and not " + printable(ch), this, null);
/*      */     }
/*      */ 
/*      */     
/* 2576 */     char quotChar = ch;
/*      */     
/* 2578 */     int versionStart = this.pos;
/* 2579 */     ch = more();
/*      */     
/* 2581 */     while (ch != quotChar) {
/* 2582 */       if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9') && ch != '_' && ch != '.' && ch != ':' && ch != '-')
/*      */       {
/*      */         
/* 2585 */         throw new XmlPullParserException("<?xml version value expected to be in ([a-zA-Z0-9_.:] | '-') not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 2589 */       ch = more();
/*      */     } 
/* 2591 */     int versionEnd = this.pos - 1;
/* 2592 */     parseXmlDeclWithVersion(versionStart, versionEnd);
/* 2593 */     this.preventBufferCompaction = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseXmlDeclWithVersion(int versionStart, int versionEnd) throws XmlPullParserException, IOException {
/* 2600 */     String oldEncoding = this.inputEncoding;
/*      */ 
/*      */     
/* 2603 */     if (versionEnd - versionStart != 3 || this.buf[versionStart] != '1' || this.buf[versionStart + 1] != '.' || this.buf[versionStart + 2] != '0')
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 2608 */       throw new XmlPullParserException("only 1.0 is supported as <?xml version not '" + printable(new String(this.buf, versionStart, versionEnd - versionStart)) + "'", this, null);
/*      */     }
/*      */ 
/*      */     
/* 2612 */     this.xmlDeclVersion = newString(this.buf, versionStart, versionEnd - versionStart);
/*      */ 
/*      */     
/* 2615 */     char ch = more();
/* 2616 */     ch = skipS(ch);
/* 2617 */     if (ch == 'e') {
/* 2618 */       ch = more();
/* 2619 */       ch = requireInput(ch, NCODING);
/* 2620 */       ch = skipS(ch);
/* 2621 */       if (ch != '=') {
/* 2622 */         throw new XmlPullParserException("expected equals sign (=) after encoding and not " + printable(ch), this, null);
/*      */       }
/*      */       
/* 2625 */       ch = more();
/* 2626 */       ch = skipS(ch);
/* 2627 */       if (ch != '\'' && ch != '"') {
/* 2628 */         throw new XmlPullParserException("expected apostrophe (') or quotation mark (\") after encoding and not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 2632 */       char quotChar = ch;
/* 2633 */       int encodingStart = this.pos;
/* 2634 */       ch = more();
/*      */       
/* 2636 */       if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z'))
/*      */       {
/* 2638 */         throw new XmlPullParserException("<?xml encoding name expected to start with [A-Za-z] not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 2642 */       ch = more();
/* 2643 */       while (ch != quotChar) {
/* 2644 */         if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9') && ch != '.' && ch != '_' && ch != '-')
/*      */         {
/*      */           
/* 2647 */           throw new XmlPullParserException("<?xml encoding value expected to be in ([A-Za-z0-9._] | '-') not " + printable(ch), this, null);
/*      */         }
/*      */ 
/*      */         
/* 2651 */         ch = more();
/*      */       } 
/* 2653 */       int encodingEnd = this.pos - 1;
/*      */ 
/*      */ 
/*      */       
/* 2657 */       this.inputEncoding = newString(this.buf, encodingStart, encodingEnd - encodingStart);
/* 2658 */       ch = more();
/*      */     } 
/*      */     
/* 2661 */     ch = skipS(ch);
/*      */     
/* 2663 */     if (ch == 's') {
/* 2664 */       ch = more();
/* 2665 */       ch = requireInput(ch, TANDALONE);
/* 2666 */       ch = skipS(ch);
/* 2667 */       if (ch != '=') {
/* 2668 */         throw new XmlPullParserException("expected equals sign (=) after standalone and not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 2672 */       ch = more();
/* 2673 */       ch = skipS(ch);
/* 2674 */       if (ch != '\'' && ch != '"') {
/* 2675 */         throw new XmlPullParserException("expected apostrophe (') or quotation mark (\") after encoding and not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 2679 */       char quotChar = ch;
/* 2680 */       int standaloneStart = this.pos;
/* 2681 */       ch = more();
/* 2682 */       if (ch == 'y') {
/* 2683 */         ch = requireInput(ch, YES);
/*      */         
/* 2685 */         this.xmlDeclStandalone = new Boolean(true);
/* 2686 */       } else if (ch == 'n') {
/* 2687 */         ch = requireInput(ch, NO);
/*      */         
/* 2689 */         this.xmlDeclStandalone = new Boolean(false);
/*      */       } else {
/* 2691 */         throw new XmlPullParserException("expected 'yes' or 'no' after standalone and not " + printable(ch), this, null);
/*      */       } 
/*      */ 
/*      */       
/* 2695 */       if (ch != quotChar) {
/* 2696 */         throw new XmlPullParserException("expected " + quotChar + " after standalone value not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 2700 */       ch = more();
/*      */     } 
/*      */ 
/*      */     
/* 2704 */     ch = skipS(ch);
/* 2705 */     if (ch != '?') {
/* 2706 */       throw new XmlPullParserException("expected ?> as last part of <?xml not " + printable(ch), this, null);
/*      */     }
/*      */ 
/*      */     
/* 2710 */     ch = more();
/* 2711 */     if (ch != '>') {
/* 2712 */       throw new XmlPullParserException("expected ?> as last part of <?xml not " + printable(ch), this, null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseDocdecl() throws XmlPullParserException, IOException {
/* 2740 */     char ch = more();
/* 2741 */     if (ch != 'O') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*      */     
/* 2743 */     ch = more();
/* 2744 */     if (ch != 'C') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*      */     
/* 2746 */     ch = more();
/* 2747 */     if (ch != 'T') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*      */     
/* 2749 */     ch = more();
/* 2750 */     if (ch != 'Y') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*      */     
/* 2752 */     ch = more();
/* 2753 */     if (ch != 'P') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*      */     
/* 2755 */     ch = more();
/* 2756 */     if (ch != 'E') throw new XmlPullParserException("expected <!DOCTYPE", this, null);
/*      */     
/* 2758 */     this.posStart = this.pos;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2763 */     int bracketLevel = 0;
/* 2764 */     boolean normalizeIgnorableWS = (this.tokenize == true && !this.roundtripSupported);
/* 2765 */     boolean normalizedCR = false;
/*      */     while (true) {
/* 2767 */       ch = more();
/* 2768 */       if (ch == '[') bracketLevel++; 
/* 2769 */       if (ch == ']') bracketLevel--; 
/* 2770 */       if (ch == '>' && bracketLevel == 0)
/* 2771 */         break;  if (normalizeIgnorableWS) {
/* 2772 */         if (ch == '\r') {
/* 2773 */           normalizedCR = true;
/*      */ 
/*      */ 
/*      */           
/* 2777 */           if (!this.usePC) {
/* 2778 */             this.posEnd = this.pos - 1;
/* 2779 */             if (this.posEnd > this.posStart) {
/* 2780 */               joinPC();
/*      */             } else {
/* 2782 */               this.usePC = true;
/* 2783 */               this.pcStart = this.pcEnd = 0;
/*      */             } 
/*      */           } 
/*      */           
/* 2787 */           if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2788 */           this.pc[this.pcEnd++] = '\n'; continue;
/* 2789 */         }  if (ch == '\n') {
/* 2790 */           if (!normalizedCR && this.usePC) {
/* 2791 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2792 */             this.pc[this.pcEnd++] = '\n';
/*      */           } 
/* 2794 */           normalizedCR = false; continue;
/*      */         } 
/* 2796 */         if (this.usePC) {
/* 2797 */           if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2798 */           this.pc[this.pcEnd++] = ch;
/*      */         } 
/* 2800 */         normalizedCR = false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2805 */     this.posEnd = this.pos - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseCDSect(boolean hadCharData) throws XmlPullParserException, IOException {
/* 2819 */     char ch = more();
/* 2820 */     if (ch != 'C') throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
/*      */     
/* 2822 */     ch = more();
/* 2823 */     if (ch != 'D') throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
/*      */     
/* 2825 */     ch = more();
/* 2826 */     if (ch != 'A') throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
/*      */     
/* 2828 */     ch = more();
/* 2829 */     if (ch != 'T') throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
/*      */     
/* 2831 */     ch = more();
/* 2832 */     if (ch != 'A') throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
/*      */     
/* 2834 */     ch = more();
/* 2835 */     if (ch != '[') throw new XmlPullParserException("expected <![CDATA[ for comment start", this, null);
/*      */ 
/*      */ 
/*      */     
/* 2839 */     int cdStart = this.pos + this.bufAbsoluteStart;
/* 2840 */     int curLine = this.lineNumber;
/* 2841 */     int curColumn = this.columnNumber;
/* 2842 */     boolean normalizeInput = (!this.tokenize || !this.roundtripSupported);
/*      */     try {
/* 2844 */       if (normalizeInput && 
/* 2845 */         hadCharData && 
/* 2846 */         !this.usePC)
/*      */       {
/* 2848 */         if (this.posEnd > this.posStart) {
/* 2849 */           joinPC();
/*      */         } else {
/* 2851 */           this.usePC = true;
/* 2852 */           this.pcStart = this.pcEnd = 0;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 2857 */       boolean seenBracket = false;
/* 2858 */       boolean seenBracketBracket = false;
/* 2859 */       boolean normalizedCR = false;
/*      */       
/*      */       while (true) {
/* 2862 */         ch = more();
/* 2863 */         if (ch == ']') {
/* 2864 */           if (!seenBracket) {
/* 2865 */             seenBracket = true;
/*      */           } else {
/* 2867 */             seenBracketBracket = true;
/*      */           }
/*      */         
/* 2870 */         } else if (ch == '>') {
/* 2871 */           if (seenBracket && seenBracketBracket) {
/*      */             break;
/*      */           }
/* 2874 */           seenBracketBracket = false;
/*      */           
/* 2876 */           seenBracket = false;
/*      */         }
/* 2878 */         else if (seenBracket) {
/* 2879 */           seenBracket = false;
/*      */         } 
/*      */         
/* 2882 */         if (normalizeInput)
/*      */         {
/* 2884 */           if (ch == '\r') {
/* 2885 */             normalizedCR = true;
/* 2886 */             this.posStart = cdStart - this.bufAbsoluteStart;
/* 2887 */             this.posEnd = this.pos - 1;
/* 2888 */             if (!this.usePC) {
/* 2889 */               if (this.posEnd > this.posStart) {
/* 2890 */                 joinPC();
/*      */               } else {
/* 2892 */                 this.usePC = true;
/* 2893 */                 this.pcStart = this.pcEnd = 0;
/*      */               } 
/*      */             }
/*      */             
/* 2897 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2898 */             this.pc[this.pcEnd++] = '\n'; continue;
/* 2899 */           }  if (ch == '\n') {
/* 2900 */             if (!normalizedCR && this.usePC) {
/* 2901 */               if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2902 */               this.pc[this.pcEnd++] = '\n';
/*      */             } 
/* 2904 */             normalizedCR = false; continue;
/*      */           } 
/* 2906 */           if (this.usePC) {
/* 2907 */             if (this.pcEnd >= this.pc.length) ensurePC(this.pcEnd); 
/* 2908 */             this.pc[this.pcEnd++] = ch;
/*      */           } 
/* 2910 */           normalizedCR = false;
/*      */         }
/*      */       
/*      */       } 
/* 2914 */     } catch (EOFException ex) {
/*      */       
/* 2916 */       throw new XmlPullParserException("CDATA section started on line " + curLine + " and column " + curColumn + " was not closed", this, ex);
/*      */     } 
/*      */ 
/*      */     
/* 2920 */     if (normalizeInput && 
/* 2921 */       this.usePC) {
/* 2922 */       this.pcEnd -= 2;
/*      */     }
/*      */     
/* 2925 */     this.posStart = cdStart - this.bufAbsoluteStart;
/* 2926 */     this.posEnd = this.pos - 3;
/*      */   }
/*      */   
/*      */   protected void fillBuf() throws IOException, XmlPullParserException {
/* 2930 */     if (this.reader == null) throw new XmlPullParserException("reader must be set before parsing is started");
/*      */ 
/*      */ 
/*      */     
/* 2934 */     if (this.bufEnd > this.bufSoftLimit) {
/*      */ 
/*      */       
/* 2937 */       boolean compact = (this.bufStart > this.bufSoftLimit);
/* 2938 */       boolean expand = false;
/* 2939 */       if (this.preventBufferCompaction) {
/* 2940 */         compact = false;
/* 2941 */         expand = true;
/* 2942 */       } else if (!compact) {
/*      */         
/* 2944 */         if (this.bufStart < this.buf.length / 2) {
/*      */           
/* 2946 */           expand = true;
/*      */         } else {
/*      */           
/* 2949 */           compact = true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2954 */       if (compact) {
/*      */ 
/*      */         
/* 2957 */         System.arraycopy(this.buf, this.bufStart, this.buf, 0, this.bufEnd - this.bufStart);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 2965 */       else if (expand) {
/* 2966 */         int newSize = 2 * this.buf.length;
/* 2967 */         char[] newBuf = new char[newSize];
/*      */         
/* 2969 */         System.arraycopy(this.buf, this.bufStart, newBuf, 0, this.bufEnd - this.bufStart);
/* 2970 */         this.buf = newBuf;
/* 2971 */         if (this.bufLoadFactor > 0)
/*      */         {
/* 2973 */           this.bufSoftLimit = (int)(this.bufLoadFactor * this.buf.length / 100L);
/*      */         }
/*      */       } else {
/*      */         
/* 2977 */         throw new XmlPullParserException("internal error in fillBuffer()");
/*      */       } 
/* 2979 */       this.bufEnd -= this.bufStart;
/* 2980 */       this.pos -= this.bufStart;
/* 2981 */       this.posStart -= this.bufStart;
/* 2982 */       this.posEnd -= this.bufStart;
/* 2983 */       this.bufAbsoluteStart += this.bufStart;
/* 2984 */       this.bufStart = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2991 */     int len = (this.buf.length - this.bufEnd > 8192) ? 8192 : (this.buf.length - this.bufEnd);
/* 2992 */     int ret = this.reader.read(this.buf, this.bufEnd, len);
/* 2993 */     if (ret > 0) {
/* 2994 */       this.bufEnd += ret;
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 3001 */     if (ret == -1) {
/* 3002 */       if (this.bufAbsoluteStart == 0 && this.pos == 0) {
/* 3003 */         throw new EOFException("input contained no data");
/*      */       }
/* 3005 */       if (this.seenRoot && this.depth == 0) {
/* 3006 */         this.reachedEnd = true;
/*      */         return;
/*      */       } 
/* 3009 */       StringBuffer expectedTagStack = new StringBuffer();
/* 3010 */       if (this.depth > 0) {
/*      */ 
/*      */         
/* 3013 */         expectedTagStack.append(" - expected end tag");
/* 3014 */         if (this.depth > 1) {
/* 3015 */           expectedTagStack.append("s");
/*      */         }
/* 3017 */         expectedTagStack.append(" "); int i;
/* 3018 */         for (i = this.depth; i > 0; i--) {
/*      */           
/* 3020 */           String tagName = new String(this.elRawName[i], 0, this.elRawNameEnd[i]);
/* 3021 */           expectedTagStack.append("</").append(tagName).append('>');
/*      */         } 
/* 3023 */         expectedTagStack.append(" to close");
/* 3024 */         for (i = this.depth; i > 0; i--) {
/*      */           
/* 3026 */           if (i != this.depth) {
/* 3027 */             expectedTagStack.append(" and");
/*      */           }
/* 3029 */           String tagName = new String(this.elRawName[i], 0, this.elRawNameEnd[i]);
/* 3030 */           expectedTagStack.append(" start tag <" + tagName + ">");
/* 3031 */           expectedTagStack.append(" from line " + this.elRawNameLine[i]);
/*      */         } 
/* 3033 */         expectedTagStack.append(", parser stopped on");
/*      */       } 
/* 3035 */       throw new EOFException("no more data available" + expectedTagStack.toString() + getPositionDescription());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 3040 */     throw new IOException("error reading input, returned " + ret);
/*      */   }
/*      */ 
/*      */   
/*      */   protected char more() throws IOException, XmlPullParserException {
/* 3045 */     if (this.pos >= this.bufEnd) {
/* 3046 */       fillBuf();
/*      */       
/* 3048 */       if (this.reachedEnd) return Character.MAX_VALUE; 
/*      */     } 
/* 3050 */     char ch = this.buf[this.pos++];
/*      */     
/* 3052 */     if (ch == '\n') { this.lineNumber++; this.columnNumber = 1; }
/* 3053 */     else { this.columnNumber++; }
/*      */     
/* 3055 */     return ch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensurePC(int end) {
/* 3070 */     int newSize = (end > 8192) ? (2 * end) : 16384;
/* 3071 */     char[] newPC = new char[newSize];
/*      */     
/* 3073 */     System.arraycopy(this.pc, 0, newPC, 0, this.pcEnd);
/* 3074 */     this.pc = newPC;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void joinPC() {
/* 3081 */     int len = this.posEnd - this.posStart;
/* 3082 */     int newEnd = this.pcEnd + len + 1;
/* 3083 */     if (newEnd >= this.pc.length) ensurePC(newEnd);
/*      */     
/* 3085 */     System.arraycopy(this.buf, this.posStart, this.pc, this.pcEnd, len);
/* 3086 */     this.pcEnd += len;
/* 3087 */     this.usePC = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected char requireInput(char ch, char[] input) throws XmlPullParserException, IOException {
/* 3094 */     for (int i = 0; i < input.length; i++) {
/*      */       
/* 3096 */       if (ch != input[i]) {
/* 3097 */         throw new XmlPullParserException("expected " + printable(input[i]) + " in " + new String(input) + " and not " + printable(ch), this, null);
/*      */       }
/*      */ 
/*      */       
/* 3101 */       ch = more();
/*      */     } 
/* 3103 */     return ch;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected char requireNextS() throws XmlPullParserException, IOException {
/* 3109 */     char ch = more();
/* 3110 */     if (!isS(ch)) {
/* 3111 */       throw new XmlPullParserException("white space is required and not " + printable(ch), this, null);
/*      */     }
/*      */     
/* 3114 */     return skipS(ch);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected char skipS(char ch) throws XmlPullParserException, IOException {
/* 3120 */     for (; isS(ch); ch = more());
/* 3121 */     return ch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3129 */   protected static boolean[] lookupNameStartChar = new boolean[1024];
/* 3130 */   protected static boolean[] lookupNameChar = new boolean[1024];
/*      */ 
/*      */   
/*      */   private static final void setName(char ch) {
/* 3134 */     lookupNameChar[ch] = true;
/*      */   }
/*      */   private static final void setNameStart(char ch) {
/* 3137 */     lookupNameStartChar[ch] = true; setName(ch);
/*      */   }
/*      */   static {
/* 3140 */     setNameStart(':'); char ch;
/* 3141 */     for (ch = 'A'; ch <= 'Z'; ) { setNameStart(ch); ch = (char)(ch + 1); }
/* 3142 */      setNameStart('_');
/* 3143 */     for (ch = 'a'; ch <= 'z'; ) { setNameStart(ch); ch = (char)(ch + 1); }
/* 3144 */      for (ch = 'À'; ch <= '˿'; ) { setNameStart(ch); ch = (char)(ch + 1); }
/* 3145 */      for (ch = 'Ͱ'; ch <= 'ͽ'; ) { setNameStart(ch); ch = (char)(ch + 1); }
/* 3146 */      for (ch = 'Ϳ'; ch < 'Ѐ'; ) { setNameStart(ch); ch = (char)(ch + 1); }
/*      */     
/* 3148 */     setName('-');
/* 3149 */     setName('.');
/* 3150 */     for (ch = '0'; ch <= '9'; ) { setName(ch); ch = (char)(ch + 1); }
/* 3151 */      setName('·');
/* 3152 */     for (ch = '̀'; ch <= 'ͯ'; ) { setName(ch); ch = (char)(ch + 1); }
/*      */   
/*      */   }
/*      */   
/*      */   protected boolean isNameStartChar(char ch) {
/* 3157 */     return ((ch < 'Ѐ' && lookupNameStartChar[ch]) || (ch >= 'Ѐ' && ch <= '‧') || (ch >= '‪' && ch <= '↏') || (ch >= '⠀' && ch <= '￯'));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isNameChar(char ch) {
/* 3191 */     return ((ch < 'Ѐ' && lookupNameChar[ch]) || (ch >= 'Ѐ' && ch <= '‧') || (ch >= '‪' && ch <= '↏') || (ch >= '⠀' && ch <= '￯'));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isS(char ch) {
/* 3213 */     return (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String printable(char ch) {
/* 3223 */     if (ch == '\n')
/* 3224 */       return "\\n"; 
/* 3225 */     if (ch == '\r')
/* 3226 */       return "\\r"; 
/* 3227 */     if (ch == '\t')
/* 3228 */       return "\\t"; 
/* 3229 */     if (ch == '\'')
/* 3230 */       return "\\'"; 
/* 3231 */     if (ch > '' || ch < ' ') {
/* 3232 */       return "\\u" + Integer.toHexString(ch);
/*      */     }
/* 3234 */     return "" + ch;
/*      */   }
/*      */   
/*      */   protected String printable(String s) {
/* 3238 */     if (s == null) return null; 
/* 3239 */     int sLen = s.length();
/* 3240 */     StringBuffer buf = new StringBuffer(sLen + 10);
/* 3241 */     for (int i = 0; i < sLen; i++) {
/* 3242 */       buf.append(printable(s.charAt(i)));
/*      */     }
/* 3244 */     s = buf.toString();
/* 3245 */     return s;
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\mxp1\MXParser.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */