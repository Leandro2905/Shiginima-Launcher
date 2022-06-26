/*      */ package org.xmlpull.mxp1_serializer;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Writer;
/*      */ import org.xmlpull.v1.XmlSerializer;
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
/*      */ public class MXSerializer
/*      */   implements XmlSerializer
/*      */ {
/*      */   protected static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
/*      */   protected static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
/*      */   private static final boolean TRACE_SIZING = false;
/*      */   private static final boolean TRACE_ESCAPING = false;
/*   32 */   protected final String FEATURE_SERIALIZER_ATTVALUE_USE_APOSTROPHE = "http://xmlpull.org/v1/doc/features.html#serializer-attvalue-use-apostrophe";
/*      */   
/*   34 */   protected final String FEATURE_NAMES_INTERNED = "http://xmlpull.org/v1/doc/features.html#names-interned";
/*      */   
/*   36 */   protected final String PROPERTY_SERIALIZER_INDENTATION = "http://xmlpull.org/v1/doc/properties.html#serializer-indentation";
/*      */   
/*   38 */   protected final String PROPERTY_SERIALIZER_LINE_SEPARATOR = "http://xmlpull.org/v1/doc/properties.html#serializer-line-separator";
/*      */   
/*      */   protected static final String PROPERTY_LOCATION = "http://xmlpull.org/v1/doc/properties.html#location";
/*      */   
/*      */   protected boolean namesInterned;
/*      */   
/*      */   protected boolean attributeUseApostrophe;
/*      */   
/*   46 */   protected String indentationString = null;
/*   47 */   protected String lineSeparator = "\n";
/*      */   
/*      */   protected String location;
/*      */   
/*      */   protected Writer out;
/*      */   
/*      */   protected int autoDeclaredPrefixes;
/*   54 */   protected int depth = 0;
/*      */ 
/*      */   
/*   57 */   protected String[] elNamespace = new String[2];
/*   58 */   protected String[] elName = new String[this.elNamespace.length];
/*   59 */   protected String[] elPrefix = new String[this.elNamespace.length];
/*   60 */   protected int[] elNamespaceCount = new int[this.elNamespace.length];
/*      */ 
/*      */   
/*   63 */   protected int namespaceEnd = 0;
/*   64 */   protected String[] namespacePrefix = new String[8];
/*   65 */   protected String[] namespaceUri = new String[this.namespacePrefix.length];
/*      */   
/*      */   protected boolean finished;
/*      */   
/*      */   protected boolean pastRoot;
/*      */   
/*      */   protected boolean setPrefixCalled;
/*      */   
/*      */   protected boolean startTagIncomplete;
/*      */   
/*      */   protected boolean doIndent;
/*      */   protected boolean seenTag;
/*      */   protected boolean seenBracket;
/*      */   protected boolean seenBracketBracket;
/*   79 */   private static final int BUF_LEN = (Runtime.getRuntime().freeMemory() > 1000000L) ? 8192 : 256;
/*   80 */   protected char[] buf = new char[BUF_LEN];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   86 */   protected static final String[] precomputedPrefixes = new String[32]; static {
/*   87 */     for (int i = 0; i < precomputedPrefixes.length; i++)
/*      */     {
/*   89 */       precomputedPrefixes[i] = ("n" + i).intern(); } 
/*      */   }
/*      */   private boolean checkNamesInterned = false;
/*      */   protected int offsetNewLine;
/*      */   protected int indentationJump;
/*      */   
/*      */   private void checkInterning(String name) {
/*   96 */     if (this.namesInterned && name != name.intern())
/*   97 */       throw new IllegalArgumentException("all names passed as arguments must be internedwhen NAMES INTERNED feature is enabled"); 
/*      */   }
/*      */   protected char[] indentationBuf; protected int maxIndentLevel;
/*      */   protected boolean writeLineSepartor;
/*      */   protected boolean writeIndentation;
/*      */   
/*      */   protected void reset() {
/*  104 */     this.location = null;
/*  105 */     this.out = null;
/*  106 */     this.autoDeclaredPrefixes = 0;
/*  107 */     this.depth = 0;
/*      */ 
/*      */     
/*  110 */     for (int i = 0; i < this.elNamespaceCount.length; i++) {
/*      */       
/*  112 */       this.elName[i] = null;
/*  113 */       this.elPrefix[i] = null;
/*  114 */       this.elNamespace[i] = null;
/*  115 */       this.elNamespaceCount[i] = 2;
/*      */     } 
/*      */ 
/*      */     
/*  119 */     this.namespaceEnd = 0;
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
/*  130 */     this.namespacePrefix[this.namespaceEnd] = "xmlns";
/*  131 */     this.namespaceUri[this.namespaceEnd] = "http://www.w3.org/2000/xmlns/";
/*  132 */     this.namespaceEnd++;
/*      */     
/*  134 */     this.namespacePrefix[this.namespaceEnd] = "xml";
/*  135 */     this.namespaceUri[this.namespaceEnd] = "http://www.w3.org/XML/1998/namespace";
/*  136 */     this.namespaceEnd++;
/*      */     
/*  138 */     this.finished = false;
/*  139 */     this.pastRoot = false;
/*  140 */     this.setPrefixCalled = false;
/*  141 */     this.startTagIncomplete = false;
/*      */     
/*  143 */     this.seenTag = false;
/*      */     
/*  145 */     this.seenBracket = false;
/*  146 */     this.seenBracketBracket = false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void ensureElementsCapacity() {
/*  151 */     int elStackSize = this.elName.length;
/*      */ 
/*      */     
/*  154 */     int newSize = ((this.depth >= 7) ? (2 * this.depth) : 8) + 2;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  159 */     boolean needsCopying = (elStackSize > 0);
/*  160 */     String[] arr = null;
/*      */     
/*  162 */     arr = new String[newSize];
/*  163 */     if (needsCopying) System.arraycopy(this.elName, 0, arr, 0, elStackSize); 
/*  164 */     this.elName = arr;
/*      */     
/*  166 */     arr = new String[newSize];
/*  167 */     if (needsCopying) System.arraycopy(this.elPrefix, 0, arr, 0, elStackSize); 
/*  168 */     this.elPrefix = arr;
/*      */     
/*  170 */     arr = new String[newSize];
/*  171 */     if (needsCopying) System.arraycopy(this.elNamespace, 0, arr, 0, elStackSize); 
/*  172 */     this.elNamespace = arr;
/*      */     
/*  174 */     int[] iarr = new int[newSize];
/*  175 */     if (needsCopying) {
/*  176 */       System.arraycopy(this.elNamespaceCount, 0, iarr, 0, elStackSize);
/*      */     } else {
/*      */       
/*  179 */       iarr[0] = 0;
/*      */     } 
/*  181 */     this.elNamespaceCount = iarr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureNamespacesCapacity() {
/*  190 */     int newSize = (this.namespaceEnd > 7) ? (2 * this.namespaceEnd) : 8;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  195 */     String[] newNamespacePrefix = new String[newSize];
/*  196 */     String[] newNamespaceUri = new String[newSize];
/*  197 */     if (this.namespacePrefix != null) {
/*  198 */       System.arraycopy(this.namespacePrefix, 0, newNamespacePrefix, 0, this.namespaceEnd);
/*      */       
/*  200 */       System.arraycopy(this.namespaceUri, 0, newNamespaceUri, 0, this.namespaceEnd);
/*      */     } 
/*      */     
/*  203 */     this.namespacePrefix = newNamespacePrefix;
/*  204 */     this.namespaceUri = newNamespaceUri;
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
/*      */   public void setFeature(String name, boolean state) throws IllegalArgumentException, IllegalStateException {
/*  224 */     if (name == null) {
/*  225 */       throw new IllegalArgumentException("feature name can not be null");
/*      */     }
/*  227 */     if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
/*  228 */       this.namesInterned = state;
/*  229 */     } else if ("http://xmlpull.org/v1/doc/features.html#serializer-attvalue-use-apostrophe".equals(name)) {
/*  230 */       this.attributeUseApostrophe = state;
/*      */     } else {
/*  232 */       throw new IllegalStateException("unsupported feature " + name);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getFeature(String name) throws IllegalArgumentException {
/*  238 */     if (name == null) {
/*  239 */       throw new IllegalArgumentException("feature name can not be null");
/*      */     }
/*  241 */     if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name))
/*  242 */       return this.namesInterned; 
/*  243 */     if ("http://xmlpull.org/v1/doc/features.html#serializer-attvalue-use-apostrophe".equals(name)) {
/*  244 */       return this.attributeUseApostrophe;
/*      */     }
/*  246 */     return false;
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
/*      */   protected void rebuildIndentationBuf() {
/*  263 */     if (!this.doIndent)
/*  264 */       return;  int maxIndent = 65;
/*  265 */     int bufSize = 0;
/*  266 */     this.offsetNewLine = 0;
/*  267 */     if (this.writeLineSepartor) {
/*  268 */       this.offsetNewLine = this.lineSeparator.length();
/*  269 */       bufSize += this.offsetNewLine;
/*      */     } 
/*  271 */     this.maxIndentLevel = 0;
/*  272 */     if (this.writeIndentation) {
/*  273 */       this.indentationJump = this.indentationString.length();
/*  274 */       this.maxIndentLevel = 65 / this.indentationJump;
/*  275 */       bufSize += this.maxIndentLevel * this.indentationJump;
/*      */     } 
/*  277 */     if (this.indentationBuf == null || this.indentationBuf.length < bufSize) {
/*  278 */       this.indentationBuf = new char[bufSize + 8];
/*      */     }
/*  280 */     int bufPos = 0;
/*  281 */     if (this.writeLineSepartor) {
/*  282 */       for (int i = 0; i < this.lineSeparator.length(); i++)
/*      */       {
/*  284 */         this.indentationBuf[bufPos++] = this.lineSeparator.charAt(i);
/*      */       }
/*      */     }
/*  287 */     if (this.writeIndentation) {
/*  288 */       for (int i = 0; i < this.maxIndentLevel; i++) {
/*      */         
/*  290 */         for (int j = 0; j < this.indentationString.length(); j++)
/*      */         {
/*  292 */           this.indentationBuf[bufPos++] = this.indentationString.charAt(j);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void writeIndent() throws IOException {
/*  300 */     int start = this.writeLineSepartor ? 0 : this.offsetNewLine;
/*  301 */     int level = (this.depth > this.maxIndentLevel) ? this.maxIndentLevel : this.depth;
/*  302 */     this.out.write(this.indentationBuf, start, (level - 1) * this.indentationJump + this.offsetNewLine);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperty(String name, Object value) throws IllegalArgumentException, IllegalStateException {
/*  308 */     if (name == null) {
/*  309 */       throw new IllegalArgumentException("property name can not be null");
/*      */     }
/*  311 */     if ("http://xmlpull.org/v1/doc/properties.html#serializer-indentation".equals(name)) {
/*  312 */       this.indentationString = (String)value;
/*  313 */     } else if ("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator".equals(name)) {
/*  314 */       this.lineSeparator = (String)value;
/*  315 */     } else if ("http://xmlpull.org/v1/doc/properties.html#location".equals(name)) {
/*  316 */       this.location = (String)value;
/*      */     } else {
/*  318 */       throw new IllegalStateException("unsupported property " + name);
/*      */     } 
/*  320 */     this.writeLineSepartor = (this.lineSeparator != null && this.lineSeparator.length() > 0);
/*  321 */     this.writeIndentation = (this.indentationString != null && this.indentationString.length() > 0);
/*      */     
/*  323 */     this.doIndent = (this.indentationString != null && (this.writeLineSepartor || this.writeIndentation));
/*      */ 
/*      */     
/*  326 */     rebuildIndentationBuf();
/*  327 */     this.seenTag = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getProperty(String name) throws IllegalArgumentException {
/*  332 */     if (name == null) {
/*  333 */       throw new IllegalArgumentException("property name can not be null");
/*      */     }
/*  335 */     if ("http://xmlpull.org/v1/doc/properties.html#serializer-indentation".equals(name))
/*  336 */       return this.indentationString; 
/*  337 */     if ("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator".equals(name))
/*  338 */       return this.lineSeparator; 
/*  339 */     if ("http://xmlpull.org/v1/doc/properties.html#location".equals(name)) {
/*  340 */       return this.location;
/*      */     }
/*  342 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private String getLocation() {
/*  347 */     return (this.location != null) ? (" @" + this.location) : "";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer getWriter() {
/*  353 */     return this.out;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOutput(Writer writer) {
/*  358 */     reset();
/*  359 */     this.out = writer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOutput(OutputStream os, String encoding) throws IOException {
/*  364 */     if (os == null) throw new IllegalArgumentException("output stream can not be null"); 
/*  365 */     reset();
/*  366 */     if (encoding != null) {
/*  367 */       this.out = new OutputStreamWriter(os, encoding);
/*      */     } else {
/*  369 */       this.out = new OutputStreamWriter(os);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void startDocument(String encoding, Boolean standalone) throws IOException {
/*  375 */     char apos = this.attributeUseApostrophe ? '\'' : '"';
/*  376 */     if (this.attributeUseApostrophe) {
/*  377 */       this.out.write("<?xml version='1.0'");
/*      */     } else {
/*  379 */       this.out.write("<?xml version=\"1.0\"");
/*      */     } 
/*  381 */     if (encoding != null) {
/*  382 */       this.out.write(" encoding=");
/*  383 */       this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*  384 */       this.out.write(encoding);
/*  385 */       this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*      */     } 
/*      */     
/*  388 */     if (standalone != null) {
/*  389 */       this.out.write(" standalone=");
/*  390 */       this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*  391 */       if (standalone.booleanValue()) {
/*  392 */         this.out.write("yes");
/*      */       } else {
/*  394 */         this.out.write("no");
/*      */       } 
/*  396 */       this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  403 */     this.out.write("?>");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void endDocument() throws IOException {
/*  409 */     while (this.depth > 0) {
/*  410 */       endTag(this.elNamespace[this.depth], this.elName[this.depth]);
/*      */     }
/*      */ 
/*      */     
/*  414 */     this.finished = this.pastRoot = this.startTagIncomplete = true;
/*  415 */     this.out.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPrefix(String prefix, String namespace) throws IOException {
/*  420 */     if (this.startTagIncomplete) closeStartTag();
/*      */ 
/*      */     
/*  423 */     if (prefix == null) {
/*  424 */       prefix = "";
/*      */     }
/*  426 */     if (!this.namesInterned) {
/*  427 */       prefix = prefix.intern();
/*  428 */     } else if (this.checkNamesInterned) {
/*  429 */       checkInterning(prefix);
/*  430 */     } else if (prefix == null) {
/*  431 */       throw new IllegalArgumentException("prefix must be not null" + getLocation());
/*      */     } 
/*      */ 
/*      */     
/*  435 */     for (int i = this.elNamespaceCount[this.depth]; i < this.namespaceEnd; i++) {
/*      */       
/*  437 */       if (prefix == this.namespacePrefix[i]) {
/*  438 */         throw new IllegalStateException("duplicated prefix " + printable(prefix) + getLocation());
/*      */       }
/*      */     } 
/*      */     
/*  442 */     if (!this.namesInterned) {
/*  443 */       namespace = namespace.intern();
/*  444 */     } else if (this.checkNamesInterned) {
/*  445 */       checkInterning(namespace);
/*  446 */     } else if (namespace == null) {
/*  447 */       throw new IllegalArgumentException("namespace must be not null" + getLocation());
/*      */     } 
/*      */     
/*  450 */     if (this.namespaceEnd >= this.namespacePrefix.length) {
/*  451 */       ensureNamespacesCapacity();
/*      */     }
/*  453 */     this.namespacePrefix[this.namespaceEnd] = prefix;
/*  454 */     this.namespaceUri[this.namespaceEnd] = namespace;
/*  455 */     this.namespaceEnd++;
/*  456 */     this.setPrefixCalled = true;
/*      */   }
/*      */   
/*      */   protected String lookupOrDeclarePrefix(String namespace) {
/*  460 */     return getPrefix(namespace, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getPrefix(String namespace, boolean generatePrefix) {
/*  465 */     return getPrefix(namespace, generatePrefix, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getPrefix(String namespace, boolean generatePrefix, boolean nonEmpty) {
/*  471 */     if (!this.namesInterned) {
/*      */       
/*  473 */       namespace = namespace.intern();
/*  474 */     } else if (this.checkNamesInterned) {
/*  475 */       checkInterning(namespace);
/*      */     } 
/*      */     
/*  478 */     if (namespace == null)
/*  479 */       throw new IllegalArgumentException("namespace must be not null" + getLocation()); 
/*  480 */     if (namespace.length() == 0) {
/*  481 */       throw new IllegalArgumentException("default namespace cannot have prefix" + getLocation());
/*      */     }
/*      */ 
/*      */     
/*  485 */     for (int i = this.namespaceEnd - 1; i >= 0; i--) {
/*      */       
/*  487 */       if (namespace == this.namespaceUri[i]) {
/*  488 */         String prefix = this.namespacePrefix[i];
/*  489 */         if (!nonEmpty || prefix.length() != 0) {
/*      */           
/*  491 */           for (int p = this.namespaceEnd - 1; p > i; p--) {
/*      */             
/*  493 */             if (prefix == this.namespacePrefix[p]);
/*      */           } 
/*      */           
/*  496 */           return prefix;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  501 */     if (!generatePrefix) {
/*  502 */       return null;
/*      */     }
/*  504 */     return generatePrefix(namespace);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String generatePrefix(String namespace) {
/*  510 */     this.autoDeclaredPrefixes++;
/*      */     
/*  512 */     String prefix = (this.autoDeclaredPrefixes < precomputedPrefixes.length) ? precomputedPrefixes[this.autoDeclaredPrefixes] : ("n" + this.autoDeclaredPrefixes).intern();
/*      */ 
/*      */     
/*  515 */     for (int i = this.namespaceEnd - 1; i >= 0; i--) {
/*      */       
/*  517 */       if (prefix == this.namespacePrefix[i]);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  523 */     if (this.namespaceEnd >= this.namespacePrefix.length) {
/*  524 */       ensureNamespacesCapacity();
/*      */     }
/*  526 */     this.namespacePrefix[this.namespaceEnd] = prefix;
/*  527 */     this.namespaceUri[this.namespaceEnd] = namespace;
/*  528 */     this.namespaceEnd++;
/*      */     
/*  530 */     return prefix;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDepth() {
/*  536 */     return this.depth;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNamespace() {
/*  541 */     return this.elNamespace[this.depth];
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/*  546 */     return this.elName[this.depth];
/*      */   }
/*      */ 
/*      */   
/*      */   public XmlSerializer startTag(String namespace, String name) throws IOException {
/*  551 */     if (this.startTagIncomplete) {
/*  552 */       closeStartTag();
/*      */     }
/*  554 */     this.seenBracket = this.seenBracketBracket = false;
/*  555 */     this.depth++;
/*  556 */     if (this.doIndent && this.depth > 0 && this.seenTag) {
/*  557 */       writeIndent();
/*      */     }
/*  559 */     this.seenTag = true;
/*  560 */     this.setPrefixCalled = false;
/*  561 */     this.startTagIncomplete = true;
/*  562 */     if (this.depth + 1 >= this.elName.length) {
/*  563 */       ensureElementsCapacity();
/*      */     }
/*      */ 
/*      */     
/*  567 */     if (this.checkNamesInterned && this.namesInterned) checkInterning(namespace); 
/*  568 */     this.elNamespace[this.depth] = (this.namesInterned || namespace == null) ? namespace : namespace.intern();
/*      */ 
/*      */     
/*  571 */     if (this.checkNamesInterned && this.namesInterned) checkInterning(name); 
/*  572 */     this.elName[this.depth] = (this.namesInterned || name == null) ? name : name.intern();
/*  573 */     if (this.out == null) {
/*  574 */       throw new IllegalStateException("setOutput() must called set before serialization can start");
/*      */     }
/*  576 */     this.out.write(60);
/*  577 */     if (namespace != null) {
/*  578 */       if (namespace.length() > 0) {
/*      */         
/*  580 */         String prefix = null;
/*  581 */         if (this.depth > 0 && this.namespaceEnd - this.elNamespaceCount[this.depth - 1] == 1) {
/*      */           
/*  583 */           String uri = this.namespaceUri[this.namespaceEnd - 1];
/*  584 */           if (uri == namespace || uri.equals(namespace)) {
/*  585 */             String elPfx = this.namespacePrefix[this.namespaceEnd - 1];
/*      */             
/*  587 */             for (int pos = this.elNamespaceCount[this.depth - 1] - 1; pos >= 2; pos--) {
/*  588 */               String pf = this.namespacePrefix[pos];
/*  589 */               if (pf == elPfx || pf.equals(elPfx)) {
/*  590 */                 String n = this.namespaceUri[pos];
/*  591 */                 if (n == uri || n.equals(uri)) {
/*  592 */                   this.namespaceEnd--;
/*  593 */                   prefix = elPfx;
/*      */                 } 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*  600 */         if (prefix == null) {
/*  601 */           prefix = lookupOrDeclarePrefix(namespace);
/*      */         }
/*      */ 
/*      */         
/*  605 */         if (prefix.length() > 0) {
/*  606 */           this.elPrefix[this.depth] = prefix;
/*  607 */           this.out.write(prefix);
/*  608 */           this.out.write(58);
/*      */         } else {
/*  610 */           this.elPrefix[this.depth] = "";
/*      */         } 
/*      */       } else {
/*      */         
/*  614 */         for (int i = this.namespaceEnd - 1; i >= 0; i--) {
/*  615 */           if (this.namespacePrefix[i] == "") {
/*  616 */             String uri = this.namespaceUri[i];
/*  617 */             if (uri == null) {
/*      */               
/*  619 */               setPrefix("", ""); break;
/*  620 */             }  if (uri.length() > 0) {
/*  621 */               throw new IllegalStateException("start tag can not be written in empty default namespace as default namespace is currently bound to '" + uri + "'" + getLocation());
/*      */             }
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/*  628 */         this.elPrefix[this.depth] = "";
/*      */       } 
/*      */     } else {
/*  631 */       this.elPrefix[this.depth] = "";
/*      */     } 
/*  633 */     this.out.write(name);
/*  634 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public XmlSerializer attribute(String namespace, String name, String value) throws IOException {
/*  640 */     if (!this.startTagIncomplete) {
/*  641 */       throw new IllegalArgumentException("startTag() must be called before attribute()" + getLocation());
/*      */     }
/*      */     
/*  644 */     this.out.write(32);
/*      */     
/*  646 */     if (namespace != null && namespace.length() > 0) {
/*      */       
/*  648 */       if (!this.namesInterned) {
/*  649 */         namespace = namespace.intern();
/*  650 */       } else if (this.checkNamesInterned) {
/*  651 */         checkInterning(namespace);
/*      */       } 
/*  653 */       String prefix = getPrefix(namespace, false, true);
/*      */ 
/*      */       
/*  656 */       if (prefix == null)
/*      */       {
/*      */         
/*  659 */         prefix = generatePrefix(namespace);
/*      */       }
/*  661 */       this.out.write(prefix);
/*  662 */       this.out.write(58);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  669 */     this.out.write(name);
/*  670 */     this.out.write(61);
/*      */     
/*  672 */     this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*  673 */     writeAttributeValue(value, this.out);
/*  674 */     this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*  675 */     return this;
/*      */   }
/*      */   
/*      */   protected void closeStartTag() throws IOException {
/*  679 */     if (this.finished) {
/*  680 */       throw new IllegalArgumentException("trying to write past already finished output" + getLocation());
/*      */     }
/*  682 */     if (this.seenBracket) {
/*  683 */       this.seenBracket = this.seenBracketBracket = false;
/*      */     }
/*  685 */     if (this.startTagIncomplete || this.setPrefixCalled) {
/*  686 */       if (this.setPrefixCalled) {
/*  687 */         throw new IllegalArgumentException("startTag() must be called immediately after setPrefix()" + getLocation());
/*      */       }
/*      */       
/*  690 */       if (!this.startTagIncomplete) {
/*  691 */         throw new IllegalArgumentException("trying to close start tag that is not opened" + getLocation());
/*      */       }
/*      */ 
/*      */       
/*  695 */       writeNamespaceDeclarations();
/*  696 */       this.out.write(62);
/*  697 */       this.elNamespaceCount[this.depth] = this.namespaceEnd;
/*  698 */       this.startTagIncomplete = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeNamespaceDeclarations() throws IOException {
/*  705 */     for (int i = this.elNamespaceCount[this.depth - 1]; i < this.namespaceEnd; i++) {
/*      */       
/*  707 */       if (this.doIndent && this.namespaceUri[i].length() > 40) {
/*  708 */         writeIndent();
/*  709 */         this.out.write(" ");
/*      */       } 
/*  711 */       if (this.namespacePrefix[i] != "") {
/*  712 */         this.out.write(" xmlns:");
/*  713 */         this.out.write(this.namespacePrefix[i]);
/*  714 */         this.out.write(61);
/*      */       } else {
/*  716 */         this.out.write(" xmlns=");
/*      */       } 
/*  718 */       this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*      */ 
/*      */       
/*  721 */       writeAttributeValue(this.namespaceUri[i], this.out);
/*      */       
/*  723 */       this.out.write(this.attributeUseApostrophe ? 39 : 34);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XmlSerializer endTag(String namespace, String name) throws IOException {
/*  734 */     this.seenBracket = this.seenBracketBracket = false;
/*  735 */     if (namespace != null) {
/*  736 */       if (!this.namesInterned) {
/*  737 */         namespace = namespace.intern();
/*  738 */       } else if (this.checkNamesInterned) {
/*  739 */         checkInterning(namespace);
/*      */       } 
/*      */     }
/*      */     
/*  743 */     if (namespace != this.elNamespace[this.depth])
/*      */     {
/*  745 */       throw new IllegalArgumentException("expected namespace " + printable(this.elNamespace[this.depth]) + " and not " + printable(namespace) + getLocation());
/*      */     }
/*      */ 
/*      */     
/*  749 */     if (name == null) {
/*  750 */       throw new IllegalArgumentException("end tag name can not be null" + getLocation());
/*      */     }
/*  752 */     if (this.checkNamesInterned && this.namesInterned) {
/*  753 */       checkInterning(name);
/*      */     }
/*  755 */     String startTagName = this.elName[this.depth];
/*  756 */     if ((!this.namesInterned && !name.equals(startTagName)) || (this.namesInterned && name != startTagName))
/*      */     {
/*      */       
/*  759 */       throw new IllegalArgumentException("expected element name " + printable(this.elName[this.depth]) + " and not " + printable(name) + getLocation());
/*      */     }
/*      */     
/*  762 */     if (this.startTagIncomplete) {
/*  763 */       writeNamespaceDeclarations();
/*  764 */       this.out.write(" />");
/*  765 */       this.depth--;
/*      */     } else {
/*      */       
/*  768 */       if (this.doIndent && this.seenTag) writeIndent(); 
/*  769 */       this.out.write("</");
/*  770 */       String startTagPrefix = this.elPrefix[this.depth];
/*  771 */       if (startTagPrefix.length() > 0) {
/*  772 */         this.out.write(startTagPrefix);
/*  773 */         this.out.write(58);
/*      */       } 
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
/*  785 */       this.out.write(name);
/*  786 */       this.out.write(62);
/*  787 */       this.depth--;
/*      */     } 
/*  789 */     this.namespaceEnd = this.elNamespaceCount[this.depth];
/*  790 */     this.startTagIncomplete = false;
/*  791 */     this.seenTag = true;
/*  792 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public XmlSerializer text(String text) throws IOException {
/*  798 */     if (this.startTagIncomplete || this.setPrefixCalled) closeStartTag(); 
/*  799 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  800 */     writeElementContent(text, this.out);
/*  801 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public XmlSerializer text(char[] buf, int start, int len) throws IOException {
/*  806 */     if (this.startTagIncomplete || this.setPrefixCalled) closeStartTag(); 
/*  807 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  808 */     writeElementContent(buf, start, len, this.out);
/*  809 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void cdsect(String text) throws IOException {
/*  814 */     if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) closeStartTag(); 
/*  815 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  816 */     this.out.write("<![CDATA[");
/*  817 */     this.out.write(text);
/*  818 */     this.out.write("]]>");
/*      */   }
/*      */ 
/*      */   
/*      */   public void entityRef(String text) throws IOException {
/*  823 */     if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) closeStartTag(); 
/*  824 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  825 */     this.out.write(38);
/*  826 */     this.out.write(text);
/*  827 */     this.out.write(59);
/*      */   }
/*      */ 
/*      */   
/*      */   public void processingInstruction(String text) throws IOException {
/*  832 */     if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) closeStartTag(); 
/*  833 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  834 */     this.out.write("<?");
/*  835 */     this.out.write(text);
/*  836 */     this.out.write("?>");
/*      */   }
/*      */ 
/*      */   
/*      */   public void comment(String text) throws IOException {
/*  841 */     if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) closeStartTag(); 
/*  842 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  843 */     this.out.write("<!--");
/*  844 */     this.out.write(text);
/*  845 */     this.out.write("-->");
/*      */   }
/*      */ 
/*      */   
/*      */   public void docdecl(String text) throws IOException {
/*  850 */     if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) closeStartTag(); 
/*  851 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  852 */     this.out.write("<!DOCTYPE");
/*  853 */     this.out.write(text);
/*  854 */     this.out.write(">");
/*      */   }
/*      */ 
/*      */   
/*      */   public void ignorableWhitespace(String text) throws IOException {
/*  859 */     if (this.startTagIncomplete || this.setPrefixCalled || this.seenBracket) closeStartTag(); 
/*  860 */     if (this.doIndent && this.seenTag) this.seenTag = false; 
/*  861 */     if (text.length() == 0) {
/*  862 */       throw new IllegalArgumentException("empty string is not allowed for ignorable whitespace" + getLocation());
/*      */     }
/*      */     
/*  865 */     this.out.write(text);
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() throws IOException {
/*  870 */     if (!this.finished && this.startTagIncomplete) closeStartTag(); 
/*  871 */     this.out.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeAttributeValue(String value, Writer out) throws IOException {
/*  879 */     char quot = this.attributeUseApostrophe ? '\'' : '"';
/*  880 */     String quotEntity = this.attributeUseApostrophe ? "&apos;" : "&quot;";
/*      */     
/*  882 */     int pos = 0;
/*  883 */     for (int i = 0; i < value.length(); i++) {
/*      */       
/*  885 */       char ch = value.charAt(i);
/*  886 */       if (ch == '&') {
/*  887 */         if (i > pos) out.write(value.substring(pos, i)); 
/*  888 */         out.write("&amp;");
/*  889 */         pos = i + 1;
/*  890 */       }  if (ch == '<') {
/*  891 */         if (i > pos) out.write(value.substring(pos, i)); 
/*  892 */         out.write("&lt;");
/*  893 */         pos = i + 1;
/*  894 */       } else if (ch == quot) {
/*  895 */         if (i > pos) out.write(value.substring(pos, i)); 
/*  896 */         out.write(quotEntity);
/*  897 */         pos = i + 1;
/*  898 */       } else if (ch < ' ') {
/*      */ 
/*      */         
/*  901 */         if (ch == '\r' || ch == '\n' || ch == '\t') {
/*  902 */           if (i > pos) out.write(value.substring(pos, i)); 
/*  903 */           out.write("&#");
/*  904 */           out.write(Integer.toString(ch));
/*  905 */           out.write(59);
/*  906 */           pos = i + 1;
/*      */         }
/*      */         else {
/*      */           
/*  910 */           throw new IllegalStateException("character " + printable(ch) + " (" + Integer.toString(ch) + ") is not allowed in output" + getLocation() + " (attr value=" + printable(value) + ")");
/*      */         } 
/*      */       } 
/*      */     } 
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
/*  928 */     if (pos > 0) {
/*  929 */       out.write(value.substring(pos));
/*      */     } else {
/*  931 */       out.write(value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeElementContent(String text, Writer out) throws IOException {
/*  939 */     int pos = 0;
/*  940 */     for (int i = 0; i < text.length(); i++) {
/*      */ 
/*      */       
/*  943 */       char ch = text.charAt(i);
/*  944 */       if (ch == ']') {
/*  945 */         if (this.seenBracket) {
/*  946 */           this.seenBracketBracket = true;
/*      */         } else {
/*  948 */           this.seenBracket = true;
/*      */         } 
/*      */       } else {
/*  951 */         if (ch == '&') {
/*  952 */           if (i > pos) out.write(text.substring(pos, i)); 
/*  953 */           out.write("&amp;");
/*  954 */           pos = i + 1;
/*  955 */         } else if (ch == '<') {
/*  956 */           if (i > pos) out.write(text.substring(pos, i)); 
/*  957 */           out.write("&lt;");
/*  958 */           pos = i + 1;
/*  959 */         } else if (this.seenBracketBracket && ch == '>') {
/*  960 */           if (i > pos) out.write(text.substring(pos, i)); 
/*  961 */           out.write("&gt;");
/*  962 */           pos = i + 1;
/*  963 */         } else if (ch < ' ') {
/*      */           
/*  965 */           if (ch != '\t' && ch != '\n' && ch != '\r')
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  976 */             throw new IllegalStateException("character " + Integer.toString(ch) + " is not allowed in output" + getLocation() + " (text value=" + printable(text) + ")");
/*      */           }
/*      */         } 
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
/*  992 */         if (this.seenBracket) {
/*  993 */           this.seenBracketBracket = this.seenBracket = false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  998 */     if (pos > 0) {
/*  999 */       out.write(text.substring(pos));
/*      */     } else {
/* 1001 */       out.write(text);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeElementContent(char[] buf, int off, int len, Writer out) throws IOException {
/* 1011 */     int end = off + len;
/* 1012 */     int pos = off;
/* 1013 */     for (int i = off; i < end; i++) {
/*      */       
/* 1015 */       char ch = buf[i];
/* 1016 */       if (ch == ']') {
/* 1017 */         if (this.seenBracket) {
/* 1018 */           this.seenBracketBracket = true;
/*      */         } else {
/* 1020 */           this.seenBracket = true;
/*      */         } 
/*      */       } else {
/* 1023 */         if (ch == '&') {
/* 1024 */           if (i > pos) {
/* 1025 */             out.write(buf, pos, i - pos);
/*      */           }
/* 1027 */           out.write("&amp;");
/* 1028 */           pos = i + 1;
/* 1029 */         } else if (ch == '<') {
/* 1030 */           if (i > pos) {
/* 1031 */             out.write(buf, pos, i - pos);
/*      */           }
/* 1033 */           out.write("&lt;");
/* 1034 */           pos = i + 1;
/*      */         }
/* 1036 */         else if (this.seenBracketBracket && ch == '>') {
/* 1037 */           if (i > pos) {
/* 1038 */             out.write(buf, pos, i - pos);
/*      */           }
/* 1040 */           out.write("&gt;");
/* 1041 */           pos = i + 1;
/* 1042 */         } else if (ch < ' ') {
/*      */           
/* 1044 */           if (ch != '\t' && ch != '\n' && ch != '\r')
/*      */           {
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
/* 1060 */             throw new IllegalStateException("character " + printable(ch) + " (" + Integer.toString(ch) + ") is not allowed in output" + getLocation());
/*      */           }
/*      */         } 
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
/* 1075 */         if (this.seenBracket) {
/* 1076 */           this.seenBracketBracket = this.seenBracket = false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1081 */     if (end > pos) {
/* 1082 */       out.write(buf, pos, end - pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final String printable(String s) {
/* 1088 */     if (s == null) return "null"; 
/* 1089 */     StringBuffer retval = new StringBuffer(s.length() + 16);
/* 1090 */     retval.append("'");
/*      */     
/* 1092 */     for (int i = 0; i < s.length(); i++) {
/* 1093 */       addPrintable(retval, s.charAt(i));
/*      */     }
/* 1095 */     retval.append("'");
/* 1096 */     return retval.toString();
/*      */   }
/*      */   
/*      */   protected static final String printable(char ch) {
/* 1100 */     StringBuffer retval = new StringBuffer();
/* 1101 */     addPrintable(retval, ch);
/* 1102 */     return retval.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addPrintable(StringBuffer retval, char ch) {
/* 1107 */     switch (ch) {
/*      */       
/*      */       case '\b':
/* 1110 */         retval.append("\\b");
/*      */         return;
/*      */       case '\t':
/* 1113 */         retval.append("\\t");
/*      */         return;
/*      */       case '\n':
/* 1116 */         retval.append("\\n");
/*      */         return;
/*      */       case '\f':
/* 1119 */         retval.append("\\f");
/*      */         return;
/*      */       case '\r':
/* 1122 */         retval.append("\\r");
/*      */         return;
/*      */       case '"':
/* 1125 */         retval.append("\\\"");
/*      */         return;
/*      */       case '\'':
/* 1128 */         retval.append("\\'");
/*      */         return;
/*      */       case '\\':
/* 1131 */         retval.append("\\\\");
/*      */         return;
/*      */     } 
/* 1134 */     if (ch < ' ' || ch > '~') {
/* 1135 */       String ss = "0000" + Integer.toString(ch, 16);
/* 1136 */       retval.append("\\u" + ss.substring(ss.length() - 4, ss.length()));
/*      */     } else {
/* 1138 */       retval.append(ch);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\mxp1_serializer\MXSerializer.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */