/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
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
/*     */ abstract class RFC1522Codec
/*     */ {
/*     */   protected static final char SEP = '?';
/*     */   protected static final String POSTFIX = "?=";
/*     */   protected static final String PREFIX = "=?";
/*     */   
/*     */   protected String encodeText(String text, Charset charset) throws EncoderException {
/*  69 */     if (text == null) {
/*  70 */       return null;
/*     */     }
/*  72 */     StringBuilder buffer = new StringBuilder();
/*  73 */     buffer.append("=?");
/*  74 */     buffer.append(charset);
/*  75 */     buffer.append('?');
/*  76 */     buffer.append(getEncoding());
/*  77 */     buffer.append('?');
/*  78 */     byte[] rawData = doEncoding(text.getBytes(charset));
/*  79 */     buffer.append(StringUtils.newStringUsAscii(rawData));
/*  80 */     buffer.append("?=");
/*  81 */     return buffer.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected String encodeText(String text, String charsetName) throws EncoderException, UnsupportedEncodingException {
/* 104 */     if (text == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     return encodeText(text, Charset.forName(charsetName));
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
/*     */   protected String decodeText(String text) throws DecoderException, UnsupportedEncodingException {
/* 126 */     if (text == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     if (!text.startsWith("=?") || !text.endsWith("?=")) {
/* 130 */       throw new DecoderException("RFC 1522 violation: malformed encoded content");
/*     */     }
/* 132 */     int terminator = text.length() - 2;
/* 133 */     int from = 2;
/* 134 */     int to = text.indexOf('?', from);
/* 135 */     if (to == terminator) {
/* 136 */       throw new DecoderException("RFC 1522 violation: charset token not found");
/*     */     }
/* 138 */     String charset = text.substring(from, to);
/* 139 */     if (charset.equals("")) {
/* 140 */       throw new DecoderException("RFC 1522 violation: charset not specified");
/*     */     }
/* 142 */     from = to + 1;
/* 143 */     to = text.indexOf('?', from);
/* 144 */     if (to == terminator) {
/* 145 */       throw new DecoderException("RFC 1522 violation: encoding token not found");
/*     */     }
/* 147 */     String encoding = text.substring(from, to);
/* 148 */     if (!getEncoding().equalsIgnoreCase(encoding)) {
/* 149 */       throw new DecoderException("This codec cannot decode " + encoding + " encoded content");
/*     */     }
/* 151 */     from = to + 1;
/* 152 */     to = text.indexOf('?', from);
/* 153 */     byte[] data = StringUtils.getBytesUsAscii(text.substring(from, to));
/* 154 */     data = doDecoding(data);
/* 155 */     return new String(data, charset);
/*     */   }
/*     */   
/*     */   protected abstract String getEncoding();
/*     */   
/*     */   protected abstract byte[] doEncoding(byte[] paramArrayOfbyte) throws EncoderException;
/*     */   
/*     */   protected abstract byte[] doDecoding(byte[] paramArrayOfbyte) throws DecoderException;
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\net\RFC1522Codec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */