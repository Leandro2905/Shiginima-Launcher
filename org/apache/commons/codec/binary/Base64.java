/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 6;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 4;
/*  74 */   static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final byte[] STANDARD_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_6BITS = 63;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] encodeTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private final byte[] decodeTable = DECODE_TABLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] lineSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int decodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int encodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64() {
/* 173 */     this(0);
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
/*     */   public Base64(boolean urlSafe) {
/* 192 */     this(76, CHUNK_SEPARATOR, urlSafe);
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
/*     */   public Base64(int lineLength) {
/* 215 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64(int lineLength, byte[] lineSeparator) {
/* 242 */     this(lineLength, lineSeparator, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
/* 273 */     super(3, 4, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     if (lineSeparator != null) {
/* 279 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 280 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 281 */         throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + sep + "]");
/*     */       } 
/* 283 */       if (lineLength > 0) {
/* 284 */         this.encodeSize = 4 + lineSeparator.length;
/* 285 */         this.lineSeparator = new byte[lineSeparator.length];
/* 286 */         System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */       } else {
/* 288 */         this.encodeSize = 4;
/* 289 */         this.lineSeparator = null;
/*     */       } 
/*     */     } else {
/* 292 */       this.encodeSize = 4;
/* 293 */       this.lineSeparator = null;
/*     */     } 
/* 295 */     this.decodeSize = this.encodeSize - 1;
/* 296 */     this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUrlSafe() {
/* 306 */     return (this.encodeTable == URL_SAFE_ENCODE_TABLE);
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
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 332 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 337 */     if (inAvail < 0) {
/* 338 */       context.eof = true;
/* 339 */       if (0 == context.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 342 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 343 */       int savedPos = context.pos;
/* 344 */       switch (context.modulus) {
/*     */         case 0:
/*     */           break;
/*     */         
/*     */         case 1:
/* 349 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 2 & 0x3F];
/*     */           
/* 351 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 4 & 0x3F];
/*     */           
/* 353 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 354 */             buffer[context.pos++] = this.pad;
/* 355 */             buffer[context.pos++] = this.pad;
/*     */           } 
/*     */           break;
/*     */         
/*     */         case 2:
/* 360 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 10 & 0x3F];
/* 361 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 4 & 0x3F];
/* 362 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea << 2 & 0x3F];
/*     */           
/* 364 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 365 */             buffer[context.pos++] = this.pad;
/*     */           }
/*     */           break;
/*     */         default:
/* 369 */           throw new IllegalStateException("Impossible modulus " + context.modulus);
/*     */       } 
/* 371 */       context.currentLinePos += context.pos - savedPos;
/*     */       
/* 373 */       if (this.lineLength > 0 && context.currentLinePos > 0) {
/* 374 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 375 */         context.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 378 */       for (int i = 0; i < inAvail; i++) {
/* 379 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 380 */         context.modulus = (context.modulus + 1) % 3;
/* 381 */         int b = in[inPos++];
/* 382 */         if (b < 0) {
/* 383 */           b += 256;
/*     */         }
/* 385 */         context.ibitWorkArea = (context.ibitWorkArea << 8) + b;
/* 386 */         if (0 == context.modulus) {
/* 387 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 18 & 0x3F];
/* 388 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 12 & 0x3F];
/* 389 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea >> 6 & 0x3F];
/* 390 */           buffer[context.pos++] = this.encodeTable[context.ibitWorkArea & 0x3F];
/* 391 */           context.currentLinePos += 4;
/* 392 */           if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
/* 393 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 394 */             context.pos += this.lineSeparator.length;
/* 395 */             context.currentLinePos = 0;
/*     */           } 
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 429 */     if (context.eof) {
/*     */       return;
/*     */     }
/* 432 */     if (inAvail < 0) {
/* 433 */       context.eof = true;
/*     */     }
/* 435 */     for (int i = 0; i < inAvail; i++) {
/* 436 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 437 */       byte b = in[inPos++];
/* 438 */       if (b == this.pad) {
/*     */         
/* 440 */         context.eof = true;
/*     */         break;
/*     */       } 
/* 443 */       if (b >= 0 && b < DECODE_TABLE.length) {
/* 444 */         int result = DECODE_TABLE[b];
/* 445 */         if (result >= 0) {
/* 446 */           context.modulus = (context.modulus + 1) % 4;
/* 447 */           context.ibitWorkArea = (context.ibitWorkArea << 6) + result;
/* 448 */           if (context.modulus == 0) {
/* 449 */             buffer[context.pos++] = (byte)(context.ibitWorkArea >> 16 & 0xFF);
/* 450 */             buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
/* 451 */             buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     if (context.eof && context.modulus != 0) {
/* 462 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/*     */ 
/*     */ 
/*     */       
/* 466 */       switch (context.modulus) {
/*     */         case 1:
/*     */           return;
/*     */ 
/*     */         
/*     */         case 2:
/* 472 */           context.ibitWorkArea >>= 4;
/* 473 */           buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */         
/*     */         case 3:
/* 476 */           context.ibitWorkArea >>= 2;
/* 477 */           buffer[context.pos++] = (byte)(context.ibitWorkArea >> 8 & 0xFF);
/* 478 */           buffer[context.pos++] = (byte)(context.ibitWorkArea & 0xFF);
/*     */       } 
/*     */       
/* 481 */       throw new IllegalStateException("Impossible modulus " + context.modulus);
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
/*     */   @Deprecated
/*     */   public static boolean isArrayByteBase64(byte[] arrayOctet) {
/* 498 */     return isBase64(arrayOctet);
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
/*     */   public static boolean isBase64(byte octet) {
/* 510 */     return (octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1));
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
/*     */   public static boolean isBase64(String base64) {
/* 524 */     return isBase64(StringUtils.getBytesUtf8(base64));
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
/*     */   public static boolean isBase64(byte[] arrayOctet) {
/* 538 */     for (int i = 0; i < arrayOctet.length; i++) {
/* 539 */       if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
/* 540 */         return false;
/*     */       }
/*     */     } 
/* 543 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64(byte[] binaryData) {
/* 554 */     return encodeBase64(binaryData, false);
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
/*     */   public static String encodeBase64String(byte[] binaryData) {
/* 569 */     return StringUtils.newStringUtf8(encodeBase64(binaryData, false));
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
/*     */   public static byte[] encodeBase64URLSafe(byte[] binaryData) {
/* 582 */     return encodeBase64(binaryData, false, true);
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
/*     */   public static String encodeBase64URLSafeString(byte[] binaryData) {
/* 595 */     return StringUtils.newStringUtf8(encodeBase64(binaryData, false, true));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64Chunked(byte[] binaryData) {
/* 606 */     return encodeBase64(binaryData, true);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
/* 621 */     return encodeBase64(binaryData, isChunked, false);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
/* 640 */     return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
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
/*     */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
/* 662 */     if (binaryData == null || binaryData.length == 0) {
/* 663 */       return binaryData;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 668 */     Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
/* 669 */     long len = b64.getEncodedLength(binaryData);
/* 670 */     if (len > maxResultSize) {
/* 671 */       throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maximum size of " + maxResultSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 677 */     return b64.encode(binaryData);
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
/*     */   public static byte[] decodeBase64(String base64String) {
/* 692 */     return (new Base64()).decode(base64String);
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
/*     */   public static byte[] decodeBase64(byte[] base64Data) {
/* 706 */     return (new Base64()).decode(base64Data);
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
/*     */   public static BigInteger decodeInteger(byte[] pArray) {
/* 721 */     return new BigInteger(1, decodeBase64(pArray));
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
/*     */   public static byte[] encodeInteger(BigInteger bigInt) {
/* 735 */     if (bigInt == null) {
/* 736 */       throw new NullPointerException("encodeInteger called with null parameter");
/*     */     }
/* 738 */     return encodeBase64(toIntegerBytes(bigInt), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toIntegerBytes(BigInteger bigInt) {
/* 749 */     int bitlen = bigInt.bitLength();
/*     */     
/* 751 */     bitlen = bitlen + 7 >> 3 << 3;
/* 752 */     byte[] bigBytes = bigInt.toByteArray();
/*     */     
/* 754 */     if (bigInt.bitLength() % 8 != 0 && bigInt.bitLength() / 8 + 1 == bitlen / 8) {
/* 755 */       return bigBytes;
/*     */     }
/*     */     
/* 758 */     int startSrc = 0;
/* 759 */     int len = bigBytes.length;
/*     */ 
/*     */     
/* 762 */     if (bigInt.bitLength() % 8 == 0) {
/* 763 */       startSrc = 1;
/* 764 */       len--;
/*     */     } 
/* 766 */     int startDst = bitlen / 8 - len;
/* 767 */     byte[] resizedBytes = new byte[bitlen / 8];
/* 768 */     System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
/* 769 */     return resizedBytes;
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
/*     */   protected boolean isInAlphabet(byte octet) {
/* 781 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\binary\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */