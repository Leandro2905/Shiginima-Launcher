/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ import org.apache.commons.codec.binary.StringUtils;
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
/*     */ public class URLCodec
/*     */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*     */ {
/*     */   static final int RADIX = 16;
/*     */   @Deprecated
/*     */   protected String charset;
/*     */   protected static final byte ESCAPE_CHAR = 37;
/*  70 */   protected static final BitSet WWW_FORM_URL = new BitSet(256);
/*     */ 
/*     */   
/*     */   static {
/*     */     int i;
/*  75 */     for (i = 97; i <= 122; i++) {
/*  76 */       WWW_FORM_URL.set(i);
/*     */     }
/*  78 */     for (i = 65; i <= 90; i++) {
/*  79 */       WWW_FORM_URL.set(i);
/*     */     }
/*     */     
/*  82 */     for (i = 48; i <= 57; i++) {
/*  83 */       WWW_FORM_URL.set(i);
/*     */     }
/*     */     
/*  86 */     WWW_FORM_URL.set(45);
/*  87 */     WWW_FORM_URL.set(95);
/*  88 */     WWW_FORM_URL.set(46);
/*  89 */     WWW_FORM_URL.set(42);
/*     */     
/*  91 */     WWW_FORM_URL.set(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLCodec() {
/*  99 */     this("UTF-8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLCodec(String charset) {
/* 109 */     this.charset = charset;
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
/*     */   public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) {
/* 122 */     if (bytes == null) {
/* 123 */       return null;
/*     */     }
/* 125 */     if (urlsafe == null) {
/* 126 */       urlsafe = WWW_FORM_URL;
/*     */     }
/*     */     
/* 129 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 130 */     for (byte c : bytes) {
/* 131 */       int b = c;
/* 132 */       if (b < 0) {
/* 133 */         b = 256 + b;
/*     */       }
/* 135 */       if (urlsafe.get(b)) {
/* 136 */         if (b == 32) {
/* 137 */           b = 43;
/*     */         }
/* 139 */         buffer.write(b);
/*     */       } else {
/* 141 */         buffer.write(37);
/* 142 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 143 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 144 */         buffer.write(hex1);
/* 145 */         buffer.write(hex2);
/*     */       } 
/*     */     } 
/* 148 */     return buffer.toByteArray();
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
/*     */   public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
/* 162 */     if (bytes == null) {
/* 163 */       return null;
/*     */     }
/* 165 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 166 */     for (int i = 0; i < bytes.length; i++) {
/* 167 */       int b = bytes[i];
/* 168 */       if (b == 43) {
/* 169 */         buffer.write(32);
/* 170 */       } else if (b == 37) {
/*     */         try {
/* 172 */           int u = Utils.digit16(bytes[++i]);
/* 173 */           int l = Utils.digit16(bytes[++i]);
/* 174 */           buffer.write((char)((u << 4) + l));
/* 175 */         } catch (ArrayIndexOutOfBoundsException e) {
/* 176 */           throw new DecoderException("Invalid URL encoding: ", e);
/*     */         } 
/*     */       } else {
/* 179 */         buffer.write(b);
/*     */       } 
/*     */     } 
/* 182 */     return buffer.toByteArray();
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
/*     */   public byte[] encode(byte[] bytes) {
/* 194 */     return encodeUrl(WWW_FORM_URL, bytes);
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
/*     */   public byte[] decode(byte[] bytes) throws DecoderException {
/* 210 */     return decodeUrl(bytes);
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
/*     */   public String encode(String str, String charset) throws UnsupportedEncodingException {
/* 225 */     if (str == null) {
/* 226 */       return null;
/*     */     }
/* 228 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
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
/*     */   public String encode(String str) throws EncoderException {
/* 244 */     if (str == null) {
/* 245 */       return null;
/*     */     }
/*     */     try {
/* 248 */       return encode(str, getDefaultCharset());
/* 249 */     } catch (UnsupportedEncodingException e) {
/* 250 */       throw new EncoderException(e.getMessage(), e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String decode(String str, String charset) throws DecoderException, UnsupportedEncodingException {
/* 270 */     if (str == null) {
/* 271 */       return null;
/*     */     }
/* 273 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
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
/*     */   public String decode(String str) throws DecoderException {
/* 289 */     if (str == null) {
/* 290 */       return null;
/*     */     }
/*     */     try {
/* 293 */       return decode(str, getDefaultCharset());
/* 294 */     } catch (UnsupportedEncodingException e) {
/* 295 */       throw new DecoderException(e.getMessage(), e);
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
/* 310 */     if (obj == null)
/* 311 */       return null; 
/* 312 */     if (obj instanceof byte[])
/* 313 */       return encode((byte[])obj); 
/* 314 */     if (obj instanceof String) {
/* 315 */       return encode((String)obj);
/*     */     }
/* 317 */     throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be URL encoded");
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
/*     */   public Object decode(Object obj) throws DecoderException {
/* 335 */     if (obj == null)
/* 336 */       return null; 
/* 337 */     if (obj instanceof byte[])
/* 338 */       return decode((byte[])obj); 
/* 339 */     if (obj instanceof String) {
/* 340 */       return decode((String)obj);
/*     */     }
/* 342 */     throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be URL decoded");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 353 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getEncoding() {
/* 365 */     return this.charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\net\URLCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */