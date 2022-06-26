/*     */ package org.xmlpull.mxp1;
/*     */ 
/*     */ import java.io.Reader;
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
/*     */ public class MXParserCachingStrings
/*     */   extends MXParser
/*     */   implements Cloneable
/*     */ {
/*     */   protected static final boolean CACHE_STATISTICS = false;
/*     */   protected static final boolean TRACE_SIZING = false;
/*     */   protected static final int INITIAL_CAPACITY = 13;
/*     */   protected int cacheStatCalls;
/*     */   protected int cacheStatWalks;
/*     */   protected int cacheStatResets;
/*     */   protected int cacheStatRehash;
/*     */   protected static final int CACHE_LOAD = 77;
/*     */   protected int cacheEntriesCount;
/*     */   protected int cacheEntriesThreshold;
/*     */   protected char[][] keys;
/*     */   protected String[] values;
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  53 */     if (this.reader != null && 
/*  54 */       !(this.reader instanceof Cloneable)) {
/*  55 */       throw new CloneNotSupportedException("reader used in parser must implement Cloneable!");
/*     */     }
/*     */ 
/*     */     
/*  59 */     MXParserCachingStrings cloned = (MXParserCachingStrings)super.clone();
/*     */ 
/*     */     
/*  62 */     if (this.reader != null) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/*  68 */         Object o = this.reader.getClass().getMethod("clone", null).invoke(this.reader, null);
/*  69 */         cloned.reader = (Reader)o;
/*  70 */       } catch (Exception e) {
/*     */         
/*  72 */         CloneNotSupportedException ee = new CloneNotSupportedException("failed to call clone() on reader " + this.reader + ":" + e);
/*     */         
/*  74 */         ee.initCause(e);
/*  75 */         throw ee;
/*     */       } 
/*     */     }
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
/*  88 */     if (this.keys != null)
/*     */     {
/*  90 */       cloned.keys = (char[][])this.keys.clone();
/*     */     }
/*     */ 
/*     */     
/*  94 */     if (this.values != null) {
/*  95 */       cloned.values = (String[])this.values.clone();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     if (this.elRawName != null) {
/* 102 */       cloned.elRawName = cloneCCArr(this.elRawName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 107 */     if (this.elRawNameEnd != null) {
/* 108 */       cloned.elRawNameEnd = (int[])this.elRawNameEnd.clone();
/*     */     }
/*     */ 
/*     */     
/* 112 */     if (this.elRawNameLine != null) {
/* 113 */       cloned.elRawNameLine = (int[])this.elRawNameLine.clone();
/*     */     }
/*     */ 
/*     */     
/* 117 */     if (this.elName != null) {
/* 118 */       cloned.elName = (String[])this.elName.clone();
/*     */     }
/*     */ 
/*     */     
/* 122 */     if (this.elPrefix != null) {
/* 123 */       cloned.elPrefix = (String[])this.elPrefix.clone();
/*     */     }
/*     */ 
/*     */     
/* 127 */     if (this.elUri != null) {
/* 128 */       cloned.elUri = (String[])this.elUri.clone();
/*     */     }
/*     */ 
/*     */     
/* 132 */     if (this.elNamespaceCount != null) {
/* 133 */       cloned.elNamespaceCount = (int[])this.elNamespaceCount.clone();
/*     */     }
/*     */ 
/*     */     
/* 137 */     if (this.attributeName != null) {
/* 138 */       cloned.attributeName = (String[])this.attributeName.clone();
/*     */     }
/*     */ 
/*     */     
/* 142 */     if (this.attributeNameHash != null) {
/* 143 */       cloned.attributeNameHash = (int[])this.attributeNameHash.clone();
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (this.attributePrefix != null) {
/* 148 */       cloned.attributePrefix = (String[])this.attributePrefix.clone();
/*     */     }
/*     */ 
/*     */     
/* 152 */     if (this.attributeUri != null) {
/* 153 */       cloned.attributeUri = (String[])this.attributeUri.clone();
/*     */     }
/*     */ 
/*     */     
/* 157 */     if (this.attributeValue != null) {
/* 158 */       cloned.attributeValue = (String[])this.attributeValue.clone();
/*     */     }
/*     */ 
/*     */     
/* 162 */     if (this.namespacePrefix != null) {
/* 163 */       cloned.namespacePrefix = (String[])this.namespacePrefix.clone();
/*     */     }
/*     */ 
/*     */     
/* 167 */     if (this.namespacePrefixHash != null) {
/* 168 */       cloned.namespacePrefixHash = (int[])this.namespacePrefixHash.clone();
/*     */     }
/*     */ 
/*     */     
/* 172 */     if (this.namespaceUri != null) {
/* 173 */       cloned.namespaceUri = (String[])this.namespaceUri.clone();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (this.entityName != null) {
/* 179 */       cloned.entityName = (String[])this.entityName.clone();
/*     */     }
/*     */ 
/*     */     
/* 183 */     if (this.entityNameBuf != null) {
/* 184 */       cloned.entityNameBuf = cloneCCArr(this.entityNameBuf);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 189 */     if (this.entityNameHash != null) {
/* 190 */       cloned.entityNameHash = (int[])this.entityNameHash.clone();
/*     */     }
/*     */ 
/*     */     
/* 194 */     if (this.entityReplacementBuf != null) {
/* 195 */       cloned.entityReplacementBuf = cloneCCArr(this.entityReplacementBuf);
/*     */     }
/*     */ 
/*     */     
/* 199 */     if (this.entityReplacement != null) {
/* 200 */       cloned.entityReplacement = (String[])this.entityReplacement.clone();
/*     */     }
/*     */ 
/*     */     
/* 204 */     if (this.buf != null) {
/* 205 */       cloned.buf = (char[])this.buf.clone();
/*     */     }
/*     */ 
/*     */     
/* 209 */     if (this.pc != null) {
/* 210 */       cloned.pc = (char[])this.pc.clone();
/*     */     }
/*     */ 
/*     */     
/* 214 */     if (this.charRefOneCharBuf != null) {
/* 215 */       cloned.charRefOneCharBuf = (char[])this.charRefOneCharBuf.clone();
/*     */     }
/*     */     
/* 218 */     return cloned;
/*     */   }
/*     */   
/*     */   private char[][] cloneCCArr(char[][] ccarr) {
/* 222 */     char[][] cca = (char[][])ccarr.clone();
/* 223 */     for (int i = 0; i < cca.length; i++) {
/*     */       
/* 225 */       if (cca[i] != null) {
/* 226 */         cca[i] = (char[])cca[i].clone();
/*     */       }
/*     */     } 
/* 229 */     return cca;
/*     */   }
/*     */ 
/*     */   
/*     */   public MXParserCachingStrings() {
/* 234 */     this.allStringsInterned = true;
/* 235 */     initStringCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean state) throws XmlPullParserException {
/* 244 */     if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
/* 245 */       if (this.eventType != 0) throw new XmlPullParserException("interning names feature can only be changed before parsing", this, null);
/*     */       
/* 247 */       this.allStringsInterned = state;
/* 248 */       if (!state && 
/* 249 */         this.keys != null) resetStringCache();
/*     */     
/*     */     } else {
/* 252 */       super.setFeature(name, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) {
/* 258 */     if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
/* 259 */       return this.allStringsInterned;
/*     */     }
/* 261 */     return super.getFeature(name);
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
/*     */   public void finalize() {}
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
/*     */   protected String newString(char[] cbuf, int off, int len) {
/* 286 */     if (this.allStringsInterned) {
/* 287 */       return newStringIntern(cbuf, off, len);
/*     */     }
/* 289 */     return super.newString(cbuf, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String newStringIntern(char[] cbuf, int off, int len) {
/* 300 */     if (this.cacheEntriesCount >= this.cacheEntriesThreshold) {
/* 301 */       rehash();
/*     */     }
/* 303 */     int offset = MXParser.fastHash(cbuf, off, len) % this.keys.length;
/* 304 */     char[] k = null;
/*     */     
/* 306 */     while ((k = this.keys[offset]) != null && !keysAreEqual(k, 0, k.length, cbuf, off, len))
/*     */     {
/*     */       
/* 309 */       offset = (offset + 1) % this.keys.length;
/*     */     }
/*     */     
/* 312 */     if (k != null) {
/* 313 */       return this.values[offset];
/*     */     }
/* 315 */     k = new char[len];
/* 316 */     System.arraycopy(cbuf, off, k, 0, len);
/* 317 */     String v = (new String(k)).intern();
/* 318 */     this.keys[offset] = k;
/* 319 */     this.values[offset] = v;
/* 320 */     this.cacheEntriesCount++;
/* 321 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initStringCache() {
/* 327 */     if (this.keys == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 335 */       this.cacheEntriesThreshold = 10;
/* 336 */       if (this.cacheEntriesThreshold >= 13) throw new RuntimeException("internal error: threshold must be less than capacity: 13");
/*     */       
/* 338 */       this.keys = new char[13][];
/* 339 */       this.values = new String[13];
/* 340 */       this.cacheEntriesCount = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetStringCache() {
/* 347 */     initStringCache();
/*     */   }
/*     */ 
/*     */   
/*     */   private void rehash() {
/* 352 */     int newSize = 2 * this.keys.length + 1;
/* 353 */     this.cacheEntriesThreshold = newSize * 77 / 100;
/* 354 */     if (this.cacheEntriesThreshold >= newSize) throw new RuntimeException("internal error: threshold must be less than capacity: " + newSize);
/*     */ 
/*     */     
/* 357 */     char[][] newKeys = new char[newSize][];
/* 358 */     String[] newValues = new String[newSize];
/* 359 */     for (int i = 0; i < this.keys.length; i++) {
/* 360 */       char[] k = this.keys[i];
/* 361 */       this.keys[i] = null;
/* 362 */       String v = this.values[i];
/* 363 */       this.values[i] = null;
/* 364 */       if (k != null) {
/* 365 */         int newOffset = MXParser.fastHash(k, 0, k.length) % newSize;
/* 366 */         char[] newk = null;
/* 367 */         while ((newk = newKeys[newOffset]) != null) {
/* 368 */           if (keysAreEqual(newk, 0, newk.length, k, 0, k.length))
/*     */           {
/* 370 */             throw new RuntimeException("internal cache error: duplicated keys: " + new String(newk) + " and " + new String(k));
/*     */           }
/*     */           
/* 373 */           newOffset = (newOffset + 1) % newSize;
/*     */         } 
/*     */         
/* 376 */         newKeys[newOffset] = k;
/* 377 */         newValues[newOffset] = v;
/*     */       } 
/*     */     } 
/* 380 */     this.keys = newKeys;
/* 381 */     this.values = newValues;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean keysAreEqual(char[] a, int astart, int alength, char[] b, int bstart, int blength) {
/* 386 */     if (alength != blength) {
/* 387 */       return false;
/*     */     }
/* 389 */     for (int i = 0; i < alength; i++) {
/* 390 */       if (a[astart + i] != b[bstart + i]) {
/* 391 */         return false;
/*     */       }
/*     */     } 
/* 394 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\mxp1\MXParserCachingStrings.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */