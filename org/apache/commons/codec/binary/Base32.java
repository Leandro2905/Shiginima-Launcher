/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base32
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 5;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 8;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
/*  60 */   private static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final byte[] ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final byte[] HEX_DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private static final byte[] HEX_ENCODE_TABLE = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_5BITS = 31;
/*     */ 
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
/*     */   private final byte[] decodeTable;
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
/*     */   private final byte[] encodeTable;
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
/*     */   
/*     */   public Base32() {
/* 159 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32(byte pad) {
/* 170 */     this(false, pad);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32(boolean useHex) {
/* 181 */     this(0, (byte[])null, useHex, (byte)61);
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
/*     */   public Base32(boolean useHex, byte pad) {
/* 193 */     this(0, (byte[])null, useHex, pad);
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
/*     */   public Base32(int lineLength) {
/* 208 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator) {
/* 230 */     this(lineLength, lineSeparator, false, (byte)61);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex) {
/* 255 */     this(lineLength, lineSeparator, useHex, (byte)61);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex, byte pad) {
/* 281 */     super(5, 8, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length, pad);
/*     */     
/* 283 */     if (useHex) {
/* 284 */       this.encodeTable = HEX_ENCODE_TABLE;
/* 285 */       this.decodeTable = HEX_DECODE_TABLE;
/*     */     } else {
/* 287 */       this.encodeTable = ENCODE_TABLE;
/* 288 */       this.decodeTable = DECODE_TABLE;
/*     */     } 
/* 290 */     if (lineLength > 0) {
/* 291 */       if (lineSeparator == null) {
/* 292 */         throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
/*     */       }
/*     */       
/* 295 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 296 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 297 */         throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + sep + "]");
/*     */       } 
/* 299 */       this.encodeSize = 8 + lineSeparator.length;
/* 300 */       this.lineSeparator = new byte[lineSeparator.length];
/* 301 */       System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */     } else {
/* 303 */       this.encodeSize = 8;
/* 304 */       this.lineSeparator = null;
/*     */     } 
/* 306 */     this.decodeSize = this.encodeSize - 1;
/*     */     
/* 308 */     if (isInAlphabet(pad) || isWhiteSpace(pad)) {
/* 309 */       throw new IllegalArgumentException("pad must not be in alphabet or whitespace");
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
/*     */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 339 */     if (context.eof) {
/*     */       return;
/*     */     }
/* 342 */     if (inAvail < 0) {
/* 343 */       context.eof = true;
/*     */     }
/* 345 */     for (int i = 0; i < inAvail; i++) {
/* 346 */       byte b = in[inPos++];
/* 347 */       if (b == this.pad) {
/*     */         
/* 349 */         context.eof = true;
/*     */         break;
/*     */       } 
/* 352 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 353 */       if (b >= 0 && b < this.decodeTable.length) {
/* 354 */         int result = this.decodeTable[b];
/* 355 */         if (result >= 0) {
/* 356 */           context.modulus = (context.modulus + 1) % 8;
/*     */           
/* 358 */           context.lbitWorkArea = (context.lbitWorkArea << 5L) + result;
/* 359 */           if (context.modulus == 0) {
/* 360 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 32L & 0xFFL);
/* 361 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 24L & 0xFFL);
/* 362 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 363 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 364 */             buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     if (context.eof && context.modulus >= 2) {
/* 375 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/*     */ 
/*     */       
/* 378 */       switch (context.modulus) {
/*     */         case 2:
/* 380 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 2L & 0xFFL);
/*     */           return;
/*     */         case 3:
/* 383 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 7L & 0xFFL);
/*     */           return;
/*     */         case 4:
/* 386 */           context.lbitWorkArea >>= 4L;
/* 387 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 388 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 5:
/* 391 */           context.lbitWorkArea >>= 1L;
/* 392 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 393 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 394 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 6:
/* 397 */           context.lbitWorkArea >>= 6L;
/* 398 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 399 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 400 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */         case 7:
/* 403 */           context.lbitWorkArea >>= 3L;
/* 404 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 24L & 0xFFL);
/* 405 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 16L & 0xFFL);
/* 406 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea >> 8L & 0xFFL);
/* 407 */           buffer[context.pos++] = (byte)(int)(context.lbitWorkArea & 0xFFL);
/*     */           return;
/*     */       } 
/*     */       
/* 411 */       throw new IllegalStateException("Impossible modulus " + context.modulus);
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
/*     */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context) {
/* 435 */     if (context.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 440 */     if (inAvail < 0) {
/* 441 */       context.eof = true;
/* 442 */       if (0 == context.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 445 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 446 */       int savedPos = context.pos;
/* 447 */       switch (context.modulus) {
/*     */         case 0:
/*     */           break;
/*     */         case 1:
/* 451 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 3L) & 0x1F];
/* 452 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 2L) & 0x1F];
/* 453 */           buffer[context.pos++] = this.pad;
/* 454 */           buffer[context.pos++] = this.pad;
/* 455 */           buffer[context.pos++] = this.pad;
/* 456 */           buffer[context.pos++] = this.pad;
/* 457 */           buffer[context.pos++] = this.pad;
/* 458 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         case 2:
/* 461 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 11L) & 0x1F];
/* 462 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 6L) & 0x1F];
/* 463 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 1L) & 0x1F];
/* 464 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 4L) & 0x1F];
/* 465 */           buffer[context.pos++] = this.pad;
/* 466 */           buffer[context.pos++] = this.pad;
/* 467 */           buffer[context.pos++] = this.pad;
/* 468 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         case 3:
/* 471 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 19L) & 0x1F];
/* 472 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 14L) & 0x1F];
/* 473 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 9L) & 0x1F];
/* 474 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 4L) & 0x1F];
/* 475 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 1L) & 0x1F];
/* 476 */           buffer[context.pos++] = this.pad;
/* 477 */           buffer[context.pos++] = this.pad;
/* 478 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         case 4:
/* 481 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 27L) & 0x1F];
/* 482 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 22L) & 0x1F];
/* 483 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 17L) & 0x1F];
/* 484 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 12L) & 0x1F];
/* 485 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 7L) & 0x1F];
/* 486 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 2L) & 0x1F];
/* 487 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea << 3L) & 0x1F];
/* 488 */           buffer[context.pos++] = this.pad;
/*     */           break;
/*     */         default:
/* 491 */           throw new IllegalStateException("Impossible modulus " + context.modulus);
/*     */       } 
/* 493 */       context.currentLinePos += context.pos - savedPos;
/*     */       
/* 495 */       if (this.lineLength > 0 && context.currentLinePos > 0) {
/* 496 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 497 */         context.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 500 */       for (int i = 0; i < inAvail; i++) {
/* 501 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 502 */         context.modulus = (context.modulus + 1) % 5;
/* 503 */         int b = in[inPos++];
/* 504 */         if (b < 0) {
/* 505 */           b += 256;
/*     */         }
/* 507 */         context.lbitWorkArea = (context.lbitWorkArea << 8L) + b;
/* 508 */         if (0 == context.modulus) {
/* 509 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 35L) & 0x1F];
/* 510 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 30L) & 0x1F];
/* 511 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 25L) & 0x1F];
/* 512 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 20L) & 0x1F];
/* 513 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 15L) & 0x1F];
/* 514 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 10L) & 0x1F];
/* 515 */           buffer[context.pos++] = this.encodeTable[(int)(context.lbitWorkArea >> 5L) & 0x1F];
/* 516 */           buffer[context.pos++] = this.encodeTable[(int)context.lbitWorkArea & 0x1F];
/* 517 */           context.currentLinePos += 8;
/* 518 */           if (this.lineLength > 0 && this.lineLength <= context.currentLinePos) {
/* 519 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 520 */             context.pos += this.lineSeparator.length;
/* 521 */             context.currentLinePos = 0;
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
/*     */   public boolean isInAlphabet(byte octet) {
/* 537 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\codec\binary\Base32.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */