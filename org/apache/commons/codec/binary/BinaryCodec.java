/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BinaryCodec
/*     */   implements BinaryDecoder, BinaryEncoder
/*     */ {
/*  42 */   private static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*     */ 
/*     */   
/*  45 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */   
/*     */   private static final int BIT_0 = 1;
/*     */ 
/*     */   
/*     */   private static final int BIT_1 = 2;
/*     */ 
/*     */   
/*     */   private static final int BIT_2 = 4;
/*     */ 
/*     */   
/*     */   private static final int BIT_3 = 8;
/*     */ 
/*     */   
/*     */   private static final int BIT_4 = 16;
/*     */ 
/*     */   
/*     */   private static final int BIT_5 = 32;
/*     */ 
/*     */   
/*     */   private static final int BIT_6 = 64;
/*     */ 
/*     */   
/*     */   private static final int BIT_7 = 128;
/*     */   
/*  71 */   private static final int[] BITS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] raw) {
/*  83 */     return toAsciiBytes(raw);
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
/*     */   public Object encode(Object raw) throws EncoderException {
/*  98 */     if (!(raw instanceof byte[])) {
/*  99 */       throw new EncoderException("argument not a byte array");
/*     */     }
/* 101 */     return toAsciiChars((byte[])raw);
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
/*     */   public Object decode(Object ascii) throws DecoderException {
/* 116 */     if (ascii == null) {
/* 117 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 119 */     if (ascii instanceof byte[]) {
/* 120 */       return fromAscii((byte[])ascii);
/*     */     }
/* 122 */     if (ascii instanceof char[]) {
/* 123 */       return fromAscii((char[])ascii);
/*     */     }
/* 125 */     if (ascii instanceof String) {
/* 126 */       return fromAscii(((String)ascii).toCharArray());
/*     */     }
/* 128 */     throw new DecoderException("argument not a byte array");
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
/*     */   public byte[] decode(byte[] ascii) {
/* 141 */     return fromAscii(ascii);
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
/*     */   public byte[] toByteArray(String ascii) {
/* 153 */     if (ascii == null) {
/* 154 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 156 */     return fromAscii(ascii.toCharArray());
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
/*     */   public static byte[] fromAscii(char[] ascii) {
/* 172 */     if (ascii == null || ascii.length == 0) {
/* 173 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 176 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
/* 182 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 183 */         if (ascii[jj - bits] == '1') {
/* 184 */           l_raw[ii] = (byte)(l_raw[ii] | BITS[bits]);
/*     */         }
/*     */       } 
/*     */     } 
/* 188 */     return l_raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] fromAscii(byte[] ascii) {
/* 199 */     if (isEmpty(ascii)) {
/* 200 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 203 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
/* 209 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 210 */         if (ascii[jj - bits] == 49) {
/* 211 */           l_raw[ii] = (byte)(l_raw[ii] | BITS[bits]);
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     return l_raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEmpty(byte[] array) {
/* 226 */     return (array == null || array.length == 0);
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
/*     */   public static byte[] toAsciiBytes(byte[] raw) {
/* 239 */     if (isEmpty(raw)) {
/* 240 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 243 */     byte[] l_ascii = new byte[raw.length << 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 248 */     for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
/* 249 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 250 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 251 */           l_ascii[jj - bits] = 48;
/*     */         } else {
/* 253 */           l_ascii[jj - bits] = 49;
/*     */         } 
/*     */       } 
/*     */     } 
/* 257 */     return l_ascii;
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
/*     */   public static char[] toAsciiChars(byte[] raw) {
/* 269 */     if (isEmpty(raw)) {
/* 270 */       return EMPTY_CHAR_ARRAY;
/*     */     }
/*     */     
/* 273 */     char[] l_ascii = new char[raw.length << 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
/* 279 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 280 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 281 */           l_ascii[jj - bits] = '0';
/*     */         } else {
/* 283 */           l_ascii[jj - bits] = '1';
/*     */         } 
/*     */       } 
/*     */     } 
/* 287 */     return l_ascii;
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
/*     */   public static String toAsciiString(byte[] raw) {
/* 299 */     return new String(toAsciiChars(raw));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\binary\BinaryCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */