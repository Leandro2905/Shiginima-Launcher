/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.Charsets;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
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
/*     */ public class QCodec
/*     */   extends RFC1522Codec
/*     */   implements StringEncoder, StringDecoder
/*     */ {
/*     */   private final Charset charset;
/*  61 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*     */   private static final byte BLANK = 32;
/*     */   
/*     */   static {
/*  65 */     PRINTABLE_CHARS.set(32);
/*  66 */     PRINTABLE_CHARS.set(33);
/*  67 */     PRINTABLE_CHARS.set(34);
/*  68 */     PRINTABLE_CHARS.set(35);
/*  69 */     PRINTABLE_CHARS.set(36);
/*  70 */     PRINTABLE_CHARS.set(37);
/*  71 */     PRINTABLE_CHARS.set(38);
/*  72 */     PRINTABLE_CHARS.set(39);
/*  73 */     PRINTABLE_CHARS.set(40);
/*  74 */     PRINTABLE_CHARS.set(41);
/*  75 */     PRINTABLE_CHARS.set(42);
/*  76 */     PRINTABLE_CHARS.set(43);
/*  77 */     PRINTABLE_CHARS.set(44);
/*  78 */     PRINTABLE_CHARS.set(45);
/*  79 */     PRINTABLE_CHARS.set(46);
/*  80 */     PRINTABLE_CHARS.set(47); int i;
/*  81 */     for (i = 48; i <= 57; i++) {
/*  82 */       PRINTABLE_CHARS.set(i);
/*     */     }
/*  84 */     PRINTABLE_CHARS.set(58);
/*  85 */     PRINTABLE_CHARS.set(59);
/*  86 */     PRINTABLE_CHARS.set(60);
/*  87 */     PRINTABLE_CHARS.set(62);
/*  88 */     PRINTABLE_CHARS.set(64);
/*  89 */     for (i = 65; i <= 90; i++) {
/*  90 */       PRINTABLE_CHARS.set(i);
/*     */     }
/*  92 */     PRINTABLE_CHARS.set(91);
/*  93 */     PRINTABLE_CHARS.set(92);
/*  94 */     PRINTABLE_CHARS.set(93);
/*  95 */     PRINTABLE_CHARS.set(94);
/*  96 */     PRINTABLE_CHARS.set(96);
/*  97 */     for (i = 97; i <= 122; i++) {
/*  98 */       PRINTABLE_CHARS.set(i);
/*     */     }
/* 100 */     PRINTABLE_CHARS.set(123);
/* 101 */     PRINTABLE_CHARS.set(124);
/* 102 */     PRINTABLE_CHARS.set(125);
/* 103 */     PRINTABLE_CHARS.set(126);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final byte UNDERSCORE = 95;
/*     */ 
/*     */   
/*     */   private boolean encodeBlanks = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public QCodec() {
/* 116 */     this(Charsets.UTF_8);
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
/*     */   public QCodec(Charset charset) {
/* 130 */     this.charset = charset;
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
/*     */   public QCodec(String charsetName) {
/* 144 */     this(Charset.forName(charsetName));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getEncoding() {
/* 149 */     return "Q";
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doEncoding(byte[] bytes) {
/* 154 */     if (bytes == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
/* 158 */     if (this.encodeBlanks) {
/* 159 */       for (int i = 0; i < data.length; i++) {
/* 160 */         if (data[i] == 32) {
/* 161 */           data[i] = 95;
/*     */         }
/*     */       } 
/*     */     }
/* 165 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doDecoding(byte[] bytes) throws DecoderException {
/* 170 */     if (bytes == null) {
/* 171 */       return null;
/*     */     }
/* 173 */     boolean hasUnderscores = false;
/* 174 */     for (byte b : bytes) {
/* 175 */       if (b == 95) {
/* 176 */         hasUnderscores = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 180 */     if (hasUnderscores) {
/* 181 */       byte[] tmp = new byte[bytes.length];
/* 182 */       for (int i = 0; i < bytes.length; i++) {
/* 183 */         byte b = bytes[i];
/* 184 */         if (b != 95) {
/* 185 */           tmp[i] = b;
/*     */         } else {
/* 187 */           tmp[i] = 32;
/*     */         } 
/*     */       } 
/* 190 */       return QuotedPrintableCodec.decodeQuotedPrintable(tmp);
/*     */     } 
/* 192 */     return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
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
/*     */   public String encode(String str, Charset charset) throws EncoderException {
/* 208 */     if (str == null) {
/* 209 */       return null;
/*     */     }
/* 211 */     return encodeText(str, charset);
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
/*     */   public String encode(String str, String charset) throws EncoderException {
/* 226 */     if (str == null) {
/* 227 */       return null;
/*     */     }
/*     */     try {
/* 230 */       return encodeText(str, charset);
/* 231 */     } catch (UnsupportedEncodingException e) {
/* 232 */       throw new EncoderException(e.getMessage(), e);
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
/*     */   
/*     */   public String encode(String str) throws EncoderException {
/* 247 */     if (str == null) {
/* 248 */       return null;
/*     */     }
/* 250 */     return encode(str, getCharset());
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
/*     */   public String decode(String str) throws DecoderException {
/* 265 */     if (str == null) {
/* 266 */       return null;
/*     */     }
/*     */     try {
/* 269 */       return decodeText(str);
/* 270 */     } catch (UnsupportedEncodingException e) {
/* 271 */       throw new DecoderException(e.getMessage(), e);
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
/*     */   
/*     */   public Object encode(Object obj) throws EncoderException {
/* 286 */     if (obj == null)
/* 287 */       return null; 
/* 288 */     if (obj instanceof String) {
/* 289 */       return encode((String)obj);
/*     */     }
/* 291 */     throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be encoded using Q codec");
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
/*     */   
/*     */   public Object decode(Object obj) throws DecoderException {
/* 310 */     if (obj == null)
/* 311 */       return null; 
/* 312 */     if (obj instanceof String) {
/* 313 */       return decode((String)obj);
/*     */     }
/* 315 */     throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be decoded using Q codec");
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
/*     */   public Charset getCharset() {
/* 328 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 337 */     return this.charset.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEncodeBlanks() {
/* 346 */     return this.encodeBlanks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodeBlanks(boolean b) {
/* 356 */     this.encodeBlanks = b;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\net\QCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */