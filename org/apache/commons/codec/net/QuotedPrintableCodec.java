/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.Charsets;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuotedPrintableCodec
/*     */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*     */ {
/*     */   private final Charset charset;
/*     */   private final boolean strict;
/*  80 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*     */ 
/*     */   
/*     */   private static final byte ESCAPE_CHAR = 61;
/*     */ 
/*     */   
/*     */   private static final byte TAB = 9;
/*     */ 
/*     */   
/*     */   private static final byte SPACE = 32;
/*     */   
/*     */   private static final byte CR = 13;
/*     */   
/*     */   private static final byte LF = 10;
/*     */   
/*     */   private static final int SAFE_LENGTH = 73;
/*     */ 
/*     */   
/*     */   static {
/*     */     int i;
/* 100 */     for (i = 33; i <= 60; i++) {
/* 101 */       PRINTABLE_CHARS.set(i);
/*     */     }
/* 103 */     for (i = 62; i <= 126; i++) {
/* 104 */       PRINTABLE_CHARS.set(i);
/*     */     }
/* 106 */     PRINTABLE_CHARS.set(9);
/* 107 */     PRINTABLE_CHARS.set(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec() {
/* 114 */     this(Charsets.UTF_8, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec(boolean strict) {
/* 125 */     this(Charsets.UTF_8, strict);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec(Charset charset) {
/* 136 */     this(charset, false);
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
/*     */   public QuotedPrintableCodec(Charset charset, boolean strict) {
/* 149 */     this.charset = charset;
/* 150 */     this.strict = strict;
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
/*     */   
/*     */   public QuotedPrintableCodec(String charsetName) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
/* 170 */     this(Charset.forName(charsetName), false);
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
/*     */   private static final int encodeQuotedPrintable(int b, ByteArrayOutputStream buffer) {
/* 183 */     buffer.write(61);
/* 184 */     char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 185 */     char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 186 */     buffer.write(hex1);
/* 187 */     buffer.write(hex2);
/* 188 */     return 3;
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
/*     */   private static int getUnsignedOctet(int index, byte[] bytes) {
/* 202 */     int b = bytes[index];
/* 203 */     if (b < 0) {
/* 204 */       b = 256 + b;
/*     */     }
/* 206 */     return b;
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
/*     */   private static int encodeByte(int b, boolean encode, ByteArrayOutputStream buffer) {
/* 222 */     if (encode) {
/* 223 */       return encodeQuotedPrintable(b, buffer);
/*     */     }
/* 225 */     buffer.write(b);
/* 226 */     return 1;
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
/*     */   private static boolean isWhitespace(int b) {
/* 238 */     return (b == 32 || b == 9);
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
/*     */   public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
/* 254 */     return encodeQuotedPrintable(printable, bytes, false);
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
/*     */   
/*     */   public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes, boolean strict) {
/* 274 */     if (bytes == null) {
/* 275 */       return null;
/*     */     }
/* 277 */     if (printable == null) {
/* 278 */       printable = PRINTABLE_CHARS;
/*     */     }
/* 280 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*     */     
/* 282 */     if (strict) {
/* 283 */       int pos = 1;
/*     */ 
/*     */       
/* 286 */       for (int i = 0; i < bytes.length - 3; i++) {
/* 287 */         int k = getUnsignedOctet(i, bytes);
/* 288 */         if (pos < 73) {
/*     */           
/* 290 */           pos += encodeByte(k, !printable.get(k), buffer);
/*     */         } else {
/*     */           
/* 293 */           encodeByte(k, (!printable.get(k) || isWhitespace(k)), buffer);
/*     */ 
/*     */           
/* 296 */           buffer.write(61);
/* 297 */           buffer.write(13);
/* 298 */           buffer.write(10);
/* 299 */           pos = 1;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 305 */       int b = getUnsignedOctet(bytes.length - 3, bytes);
/* 306 */       boolean encode = (!printable.get(b) || (isWhitespace(b) && pos > 68));
/* 307 */       pos += encodeByte(b, encode, buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 312 */       if (pos > 71) {
/* 313 */         buffer.write(61);
/* 314 */         buffer.write(13);
/* 315 */         buffer.write(10);
/*     */       } 
/* 317 */       for (int j = bytes.length - 2; j < bytes.length; j++) {
/* 318 */         b = getUnsignedOctet(j, bytes);
/*     */         
/* 320 */         encode = (!printable.get(b) || (j > bytes.length - 2 && isWhitespace(b)));
/* 321 */         encodeByte(b, encode, buffer);
/*     */       } 
/*     */     } else {
/* 324 */       for (byte c : bytes) {
/* 325 */         int b = c;
/* 326 */         if (b < 0) {
/* 327 */           b = 256 + b;
/*     */         }
/* 329 */         if (printable.get(b)) {
/* 330 */           buffer.write(b);
/*     */         } else {
/* 332 */           encodeQuotedPrintable(b, buffer);
/*     */         } 
/*     */       } 
/*     */     } 
/* 336 */     return buffer.toByteArray();
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
/*     */   public static final byte[] decodeQuotedPrintable(byte[] bytes) throws DecoderException {
/* 353 */     if (bytes == null) {
/* 354 */       return null;
/*     */     }
/* 356 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 357 */     for (int i = 0; i < bytes.length; i++) {
/* 358 */       int b = bytes[i];
/* 359 */       if (b == 61) {
/*     */         
/*     */         try {
/* 362 */           if (bytes[++i] != 13)
/*     */           
/*     */           { 
/* 365 */             int u = Utils.digit16(bytes[i]);
/* 366 */             int l = Utils.digit16(bytes[++i]);
/* 367 */             buffer.write((char)((u << 4) + l)); } 
/* 368 */         } catch (ArrayIndexOutOfBoundsException e) {
/* 369 */           throw new DecoderException("Invalid quoted-printable encoding", e);
/*     */         } 
/* 371 */       } else if (b != 13 && b != 10) {
/*     */         
/* 373 */         buffer.write(b);
/*     */       } 
/*     */     } 
/* 376 */     return buffer.toByteArray();
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
/*     */   public byte[] encode(byte[] bytes) {
/* 392 */     return encodeQuotedPrintable(PRINTABLE_CHARS, bytes, this.strict);
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
/*     */   public byte[] decode(byte[] bytes) throws DecoderException {
/* 410 */     return decodeQuotedPrintable(bytes);
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
/*     */   
/*     */   public String encode(String str) throws EncoderException {
/* 430 */     return encode(str, getCharset());
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
/*     */   public String decode(String str, Charset charset) throws DecoderException {
/* 447 */     if (str == null) {
/* 448 */       return null;
/*     */     }
/* 450 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
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
/*     */   public String decode(String str, String charset) throws DecoderException, UnsupportedEncodingException {
/* 468 */     if (str == null) {
/* 469 */       return null;
/*     */     }
/* 471 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
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
/* 487 */     return decode(str, getCharset());
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
/*     */   public Object encode(Object obj) throws EncoderException {
/* 502 */     if (obj == null)
/* 503 */       return null; 
/* 504 */     if (obj instanceof byte[])
/* 505 */       return encode((byte[])obj); 
/* 506 */     if (obj instanceof String) {
/* 507 */       return encode((String)obj);
/*     */     }
/* 509 */     throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable encoded");
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
/* 528 */     if (obj == null)
/* 529 */       return null; 
/* 530 */     if (obj instanceof byte[])
/* 531 */       return decode((byte[])obj); 
/* 532 */     if (obj instanceof String) {
/* 533 */       return decode((String)obj);
/*     */     }
/* 535 */     throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable decoded");
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
/* 548 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 557 */     return this.charset.name();
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
/*     */   public String encode(String str, Charset charset) {
/* 575 */     if (str == null) {
/* 576 */       return null;
/*     */     }
/* 578 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
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
/*     */   public String encode(String str, String charset) throws UnsupportedEncodingException {
/* 597 */     if (str == null) {
/* 598 */       return null;
/*     */     }
/* 600 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\net\QuotedPrintableCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */