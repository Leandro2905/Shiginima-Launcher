/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.codec.Charsets;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCodec
/*     */   extends RFC1522Codec
/*     */   implements StringEncoder, StringDecoder
/*     */ {
/*     */   private final Charset charset;
/*     */   
/*     */   public BCodec() {
/*  56 */     this(Charsets.UTF_8);
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
/*     */   public BCodec(Charset charset) {
/*  69 */     this.charset = charset;
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
/*     */   public BCodec(String charsetName) {
/*  83 */     this(Charset.forName(charsetName));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getEncoding() {
/*  88 */     return "B";
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doEncoding(byte[] bytes) {
/*  93 */     if (bytes == null) {
/*  94 */       return null;
/*     */     }
/*  96 */     return Base64.encodeBase64(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doDecoding(byte[] bytes) {
/* 101 */     if (bytes == null) {
/* 102 */       return null;
/*     */     }
/* 104 */     return Base64.decodeBase64(bytes);
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
/*     */   public String encode(String value, Charset charset) throws EncoderException {
/* 120 */     if (value == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     return encodeText(value, charset);
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
/*     */   public String encode(String value, String charset) throws EncoderException {
/* 138 */     if (value == null) {
/* 139 */       return null;
/*     */     }
/*     */     try {
/* 142 */       return encodeText(value, charset);
/* 143 */     } catch (UnsupportedEncodingException e) {
/* 144 */       throw new EncoderException(e.getMessage(), e);
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
/*     */   public String encode(String value) throws EncoderException {
/* 159 */     if (value == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     return encode(value, getCharset());
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
/*     */   public String decode(String value) throws DecoderException {
/* 177 */     if (value == null) {
/* 178 */       return null;
/*     */     }
/*     */     try {
/* 181 */       return decodeText(value);
/* 182 */     } catch (UnsupportedEncodingException e) {
/* 183 */       throw new DecoderException(e.getMessage(), e);
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
/*     */   public Object encode(Object value) throws EncoderException {
/* 198 */     if (value == null)
/* 199 */       return null; 
/* 200 */     if (value instanceof String) {
/* 201 */       return encode((String)value);
/*     */     }
/* 203 */     throw new EncoderException("Objects of type " + value.getClass().getName() + " cannot be encoded using BCodec");
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
/*     */   public Object decode(Object value) throws DecoderException {
/* 222 */     if (value == null)
/* 223 */       return null; 
/* 224 */     if (value instanceof String) {
/* 225 */       return decode((String)value);
/*     */     }
/* 227 */     throw new DecoderException("Objects of type " + value.getClass().getName() + " cannot be decoded using BCodec");
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
/* 240 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 249 */     return this.charset.name();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\net\BCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */