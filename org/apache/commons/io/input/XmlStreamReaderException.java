/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ public class XmlStreamReaderException
/*     */   extends IOException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String bomEncoding;
/*     */   private final String xmlGuessEncoding;
/*     */   private final String xmlEncoding;
/*     */   private final String contentTypeMime;
/*     */   private final String contentTypeEncoding;
/*     */   
/*     */   public XmlStreamReaderException(String msg, String bomEnc, String xmlGuessEnc, String xmlEnc) {
/*  61 */     this(msg, null, null, bomEnc, xmlGuessEnc, xmlEnc);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReaderException(String msg, String ctMime, String ctEnc, String bomEnc, String xmlGuessEnc, String xmlEnc) {
/*  79 */     super(msg);
/*  80 */     this.contentTypeMime = ctMime;
/*  81 */     this.contentTypeEncoding = ctEnc;
/*  82 */     this.bomEncoding = bomEnc;
/*  83 */     this.xmlGuessEncoding = xmlGuessEnc;
/*  84 */     this.xmlEncoding = xmlEnc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBomEncoding() {
/*  93 */     return this.bomEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXmlGuessEncoding() {
/* 102 */     return this.xmlGuessEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXmlEncoding() {
/* 111 */     return this.xmlEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentTypeMime() {
/* 122 */     return this.contentTypeMime;
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
/*     */   public String getContentTypeEncoding() {
/* 134 */     return this.contentTypeEncoding;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\io\input\XmlStreamReaderException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */