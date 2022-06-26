/*      */ package org.apache.commons.lang3;
/*      */ 
/*      */ import java.util.UUID;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Conversion
/*      */ {
/*   69 */   private static final boolean[] TTTT = new boolean[] { true, true, true, true };
/*   70 */   private static final boolean[] FTTT = new boolean[] { false, true, true, true };
/*   71 */   private static final boolean[] TFTT = new boolean[] { true, false, true, true };
/*   72 */   private static final boolean[] FFTT = new boolean[] { false, false, true, true };
/*   73 */   private static final boolean[] TTFT = new boolean[] { true, true, false, true };
/*   74 */   private static final boolean[] FTFT = new boolean[] { false, true, false, true };
/*   75 */   private static final boolean[] TFFT = new boolean[] { true, false, false, true };
/*   76 */   private static final boolean[] FFFT = new boolean[] { false, false, false, true };
/*   77 */   private static final boolean[] TTTF = new boolean[] { true, true, true, false };
/*   78 */   private static final boolean[] FTTF = new boolean[] { false, true, true, false };
/*   79 */   private static final boolean[] TFTF = new boolean[] { true, false, true, false };
/*   80 */   private static final boolean[] FFTF = new boolean[] { false, false, true, false };
/*   81 */   private static final boolean[] TTFF = new boolean[] { true, true, false, false };
/*   82 */   private static final boolean[] FTFF = new boolean[] { false, true, false, false };
/*   83 */   private static final boolean[] TFFF = new boolean[] { true, false, false, false };
/*   84 */   private static final boolean[] FFFF = new boolean[] { false, false, false, false };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexDigitToInt(char hexDigit) {
/*   99 */     int digit = Character.digit(hexDigit, 16);
/*  100 */     if (digit < 0) {
/*  101 */       throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */     }
/*  103 */     return digit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexDigitMsb0ToInt(char hexDigit) {
/*  119 */     switch (hexDigit) {
/*      */       case '0':
/*  121 */         return 0;
/*      */       case '1':
/*  123 */         return 8;
/*      */       case '2':
/*  125 */         return 4;
/*      */       case '3':
/*  127 */         return 12;
/*      */       case '4':
/*  129 */         return 2;
/*      */       case '5':
/*  131 */         return 10;
/*      */       case '6':
/*  133 */         return 6;
/*      */       case '7':
/*  135 */         return 14;
/*      */       case '8':
/*  137 */         return 1;
/*      */       case '9':
/*  139 */         return 9;
/*      */       case 'A':
/*      */       case 'a':
/*  142 */         return 5;
/*      */       case 'B':
/*      */       case 'b':
/*  145 */         return 13;
/*      */       case 'C':
/*      */       case 'c':
/*  148 */         return 3;
/*      */       case 'D':
/*      */       case 'd':
/*  151 */         return 11;
/*      */       case 'E':
/*      */       case 'e':
/*  154 */         return 7;
/*      */       case 'F':
/*      */       case 'f':
/*  157 */         return 15;
/*      */     } 
/*  159 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] hexDigitToBinary(char hexDigit) {
/*  177 */     switch (hexDigit) {
/*      */       case '0':
/*  179 */         return (boolean[])FFFF.clone();
/*      */       case '1':
/*  181 */         return (boolean[])TFFF.clone();
/*      */       case '2':
/*  183 */         return (boolean[])FTFF.clone();
/*      */       case '3':
/*  185 */         return (boolean[])TTFF.clone();
/*      */       case '4':
/*  187 */         return (boolean[])FFTF.clone();
/*      */       case '5':
/*  189 */         return (boolean[])TFTF.clone();
/*      */       case '6':
/*  191 */         return (boolean[])FTTF.clone();
/*      */       case '7':
/*  193 */         return (boolean[])TTTF.clone();
/*      */       case '8':
/*  195 */         return (boolean[])FFFT.clone();
/*      */       case '9':
/*  197 */         return (boolean[])TFFT.clone();
/*      */       case 'A':
/*      */       case 'a':
/*  200 */         return (boolean[])FTFT.clone();
/*      */       case 'B':
/*      */       case 'b':
/*  203 */         return (boolean[])TTFT.clone();
/*      */       case 'C':
/*      */       case 'c':
/*  206 */         return (boolean[])FFTT.clone();
/*      */       case 'D':
/*      */       case 'd':
/*  209 */         return (boolean[])TFTT.clone();
/*      */       case 'E':
/*      */       case 'e':
/*  212 */         return (boolean[])FTTT.clone();
/*      */       case 'F':
/*      */       case 'f':
/*  215 */         return (boolean[])TTTT.clone();
/*      */     } 
/*  217 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] hexDigitMsb0ToBinary(char hexDigit) {
/*  235 */     switch (hexDigit) {
/*      */       case '0':
/*  237 */         return (boolean[])FFFF.clone();
/*      */       case '1':
/*  239 */         return (boolean[])FFFT.clone();
/*      */       case '2':
/*  241 */         return (boolean[])FFTF.clone();
/*      */       case '3':
/*  243 */         return (boolean[])FFTT.clone();
/*      */       case '4':
/*  245 */         return (boolean[])FTFF.clone();
/*      */       case '5':
/*  247 */         return (boolean[])FTFT.clone();
/*      */       case '6':
/*  249 */         return (boolean[])FTTF.clone();
/*      */       case '7':
/*  251 */         return (boolean[])FTTT.clone();
/*      */       case '8':
/*  253 */         return (boolean[])TFFF.clone();
/*      */       case '9':
/*  255 */         return (boolean[])TFFT.clone();
/*      */       case 'A':
/*      */       case 'a':
/*  258 */         return (boolean[])TFTF.clone();
/*      */       case 'B':
/*      */       case 'b':
/*  261 */         return (boolean[])TFTT.clone();
/*      */       case 'C':
/*      */       case 'c':
/*  264 */         return (boolean[])TTFF.clone();
/*      */       case 'D':
/*      */       case 'd':
/*  267 */         return (boolean[])TTFT.clone();
/*      */       case 'E':
/*      */       case 'e':
/*  270 */         return (boolean[])TTTF.clone();
/*      */       case 'F':
/*      */       case 'f':
/*  273 */         return (boolean[])TTTT.clone();
/*      */     } 
/*  275 */     throw new IllegalArgumentException("Cannot interpret '" + hexDigit + "' as a hexadecimal digit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigit(boolean[] src) {
/*  294 */     return binaryToHexDigit(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigit(boolean[] src, int srcPos) {
/*  313 */     if (src.length == 0) {
/*  314 */       throw new IllegalArgumentException("Cannot convert an empty array.");
/*      */     }
/*  316 */     if (src.length > srcPos + 3 && src[srcPos + 3]) {
/*  317 */       if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  318 */         if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  319 */           return src[srcPos] ? 'f' : 'e';
/*      */         }
/*  321 */         return src[srcPos] ? 'd' : 'c';
/*      */       } 
/*  323 */       if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  324 */         return src[srcPos] ? 'b' : 'a';
/*      */       }
/*  326 */       return src[srcPos] ? '9' : '8';
/*      */     } 
/*  328 */     if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  329 */       if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  330 */         return src[srcPos] ? '7' : '6';
/*      */       }
/*  332 */       return src[srcPos] ? '5' : '4';
/*      */     } 
/*  334 */     if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  335 */       return src[srcPos] ? '3' : '2';
/*      */     }
/*  337 */     return src[srcPos] ? '1' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigitMsb0_4bits(boolean[] src) {
/*  356 */     return binaryToHexDigitMsb0_4bits(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryToHexDigitMsb0_4bits(boolean[] src, int srcPos) {
/*  377 */     if (src.length > 8) {
/*  378 */       throw new IllegalArgumentException("src.length>8: src.length=" + src.length);
/*      */     }
/*  380 */     if (src.length - srcPos < 4) {
/*  381 */       throw new IllegalArgumentException("src.length-srcPos<4: src.length=" + src.length + ", srcPos=" + srcPos);
/*      */     }
/*  383 */     if (src[srcPos + 3]) {
/*  384 */       if (src[srcPos + 2]) {
/*  385 */         if (src[srcPos + 1]) {
/*  386 */           return src[srcPos] ? 'f' : '7';
/*      */         }
/*  388 */         return src[srcPos] ? 'b' : '3';
/*      */       } 
/*  390 */       if (src[srcPos + 1]) {
/*  391 */         return src[srcPos] ? 'd' : '5';
/*      */       }
/*  393 */       return src[srcPos] ? '9' : '1';
/*      */     } 
/*  395 */     if (src[srcPos + 2]) {
/*  396 */       if (src[srcPos + 1]) {
/*  397 */         return src[srcPos] ? 'e' : '6';
/*      */       }
/*  399 */       return src[srcPos] ? 'a' : '2';
/*      */     } 
/*  401 */     if (src[srcPos + 1]) {
/*  402 */       return src[srcPos] ? 'c' : '4';
/*      */     }
/*  404 */     return src[srcPos] ? '8' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryBeMsb0ToHexDigit(boolean[] src) {
/*  423 */     return binaryBeMsb0ToHexDigit(src, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char binaryBeMsb0ToHexDigit(boolean[] src, int srcPos) {
/*  443 */     if (src.length == 0) {
/*  444 */       throw new IllegalArgumentException("Cannot convert an empty array.");
/*      */     }
/*  446 */     int beSrcPos = src.length - 1 - srcPos;
/*  447 */     int srcLen = Math.min(4, beSrcPos + 1);
/*  448 */     boolean[] paddedSrc = new boolean[4];
/*  449 */     System.arraycopy(src, beSrcPos + 1 - srcLen, paddedSrc, 4 - srcLen, srcLen);
/*  450 */     src = paddedSrc;
/*  451 */     srcPos = 0;
/*  452 */     if (src[srcPos]) {
/*  453 */       if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  454 */         if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  455 */           return (src.length > srcPos + 3 && src[srcPos + 3]) ? 'f' : 'e';
/*      */         }
/*  457 */         return (src.length > srcPos + 3 && src[srcPos + 3]) ? 'd' : 'c';
/*      */       } 
/*  459 */       if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  460 */         return (src.length > srcPos + 3 && src[srcPos + 3]) ? 'b' : 'a';
/*      */       }
/*  462 */       return (src.length > srcPos + 3 && src[srcPos + 3]) ? '9' : '8';
/*      */     } 
/*  464 */     if (src.length > srcPos + 1 && src[srcPos + 1]) {
/*  465 */       if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  466 */         return (src.length > srcPos + 3 && src[srcPos + 3]) ? '7' : '6';
/*      */       }
/*  468 */       return (src.length > srcPos + 3 && src[srcPos + 3]) ? '5' : '4';
/*      */     } 
/*  470 */     if (src.length > srcPos + 2 && src[srcPos + 2]) {
/*  471 */       return (src.length > srcPos + 3 && src[srcPos + 3]) ? '3' : '2';
/*      */     }
/*  473 */     return (src.length > srcPos + 3 && src[srcPos + 3]) ? '1' : '0';
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char intToHexDigit(int nibble) {
/*  495 */     char c = Character.forDigit(nibble, 16);
/*  496 */     if (c == '\000') {
/*  497 */       throw new IllegalArgumentException("nibble value not between 0 and 15: " + nibble);
/*      */     }
/*  499 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char intToHexDigitMsb0(int nibble) {
/*  521 */     switch (nibble) {
/*      */       case 0:
/*  523 */         return '0';
/*      */       case 1:
/*  525 */         return '8';
/*      */       case 2:
/*  527 */         return '4';
/*      */       case 3:
/*  529 */         return 'c';
/*      */       case 4:
/*  531 */         return '2';
/*      */       case 5:
/*  533 */         return 'a';
/*      */       case 6:
/*  535 */         return '6';
/*      */       case 7:
/*  537 */         return 'e';
/*      */       case 8:
/*  539 */         return '1';
/*      */       case 9:
/*  541 */         return '9';
/*      */       case 10:
/*  543 */         return '5';
/*      */       case 11:
/*  545 */         return 'd';
/*      */       case 12:
/*  547 */         return '3';
/*      */       case 13:
/*  549 */         return 'b';
/*      */       case 14:
/*  551 */         return '7';
/*      */       case 15:
/*  553 */         return 'f';
/*      */     } 
/*  555 */     throw new IllegalArgumentException("nibble value not between 0 and 15: " + nibble);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long intArrayToLong(int[] src, int srcPos, long dstInit, int dstPos, int nInts) {
/*  578 */     if ((src.length == 0 && srcPos == 0) || 0 == nInts) {
/*  579 */       return dstInit;
/*      */     }
/*  581 */     if ((nInts - 1) * 32 + dstPos >= 64) {
/*  582 */       throw new IllegalArgumentException("(nInts-1)*32+dstPos is greather or equal to than 64");
/*      */     }
/*  584 */     long out = dstInit;
/*  585 */     int shift = 0;
/*  586 */     for (int i = 0; i < nInts; i++) {
/*  587 */       shift = i * 32 + dstPos;
/*  588 */       long bits = (0xFFFFFFFFL & src[i + srcPos]) << shift;
/*  589 */       long mask = 4294967295L << shift;
/*  590 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  592 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long shortArrayToLong(short[] src, int srcPos, long dstInit, int dstPos, int nShorts) {
/*  614 */     if ((src.length == 0 && srcPos == 0) || 0 == nShorts) {
/*  615 */       return dstInit;
/*      */     }
/*  617 */     if ((nShorts - 1) * 16 + dstPos >= 64) {
/*  618 */       throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 64");
/*      */     }
/*  620 */     long out = dstInit;
/*  621 */     int shift = 0;
/*  622 */     for (int i = 0; i < nShorts; i++) {
/*  623 */       shift = i * 16 + dstPos;
/*  624 */       long bits = (0xFFFFL & src[i + srcPos]) << shift;
/*  625 */       long mask = 65535L << shift;
/*  626 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  628 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shortArrayToInt(short[] src, int srcPos, int dstInit, int dstPos, int nShorts) {
/*  650 */     if ((src.length == 0 && srcPos == 0) || 0 == nShorts) {
/*  651 */       return dstInit;
/*      */     }
/*  653 */     if ((nShorts - 1) * 16 + dstPos >= 32) {
/*  654 */       throw new IllegalArgumentException("(nShorts-1)*16+dstPos is greather or equal to than 32");
/*      */     }
/*  656 */     int out = dstInit;
/*  657 */     int shift = 0;
/*  658 */     for (int i = 0; i < nShorts; i++) {
/*  659 */       shift = i * 16 + dstPos;
/*  660 */       int bits = (0xFFFF & src[i + srcPos]) << shift;
/*  661 */       int mask = 65535 << shift;
/*  662 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  664 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long byteArrayToLong(byte[] src, int srcPos, long dstInit, int dstPos, int nBytes) {
/*  686 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  687 */       return dstInit;
/*      */     }
/*  689 */     if ((nBytes - 1) * 8 + dstPos >= 64) {
/*  690 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 64");
/*      */     }
/*  692 */     long out = dstInit;
/*  693 */     int shift = 0;
/*  694 */     for (int i = 0; i < nBytes; i++) {
/*  695 */       shift = i * 8 + dstPos;
/*  696 */       long bits = (0xFFL & src[i + srcPos]) << shift;
/*  697 */       long mask = 255L << shift;
/*  698 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  700 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int byteArrayToInt(byte[] src, int srcPos, int dstInit, int dstPos, int nBytes) {
/*  722 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  723 */       return dstInit;
/*      */     }
/*  725 */     if ((nBytes - 1) * 8 + dstPos >= 32) {
/*  726 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 32");
/*      */     }
/*  728 */     int out = dstInit;
/*  729 */     int shift = 0;
/*  730 */     for (int i = 0; i < nBytes; i++) {
/*  731 */       shift = i * 8 + dstPos;
/*  732 */       int bits = (0xFF & src[i + srcPos]) << shift;
/*  733 */       int mask = 255 << shift;
/*  734 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  736 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short byteArrayToShort(byte[] src, int srcPos, short dstInit, int dstPos, int nBytes) {
/*  758 */     if ((src.length == 0 && srcPos == 0) || 0 == nBytes) {
/*  759 */       return dstInit;
/*      */     }
/*  761 */     if ((nBytes - 1) * 8 + dstPos >= 16) {
/*  762 */       throw new IllegalArgumentException("(nBytes-1)*8+dstPos is greather or equal to than 16");
/*      */     }
/*  764 */     short out = dstInit;
/*  765 */     int shift = 0;
/*  766 */     for (int i = 0; i < nBytes; i++) {
/*  767 */       shift = i * 8 + dstPos;
/*  768 */       int bits = (0xFF & src[i + srcPos]) << shift;
/*  769 */       int mask = 255 << shift;
/*  770 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  772 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long hexToLong(String src, int srcPos, long dstInit, int dstPos, int nHex) {
/*  792 */     if (0 == nHex) {
/*  793 */       return dstInit;
/*      */     }
/*  795 */     if ((nHex - 1) * 4 + dstPos >= 64) {
/*  796 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 64");
/*      */     }
/*  798 */     long out = dstInit;
/*  799 */     int shift = 0;
/*  800 */     for (int i = 0; i < nHex; i++) {
/*  801 */       shift = i * 4 + dstPos;
/*  802 */       long bits = (0xFL & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  803 */       long mask = 15L << shift;
/*  804 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  806 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int hexToInt(String src, int srcPos, int dstInit, int dstPos, int nHex) {
/*  825 */     if (0 == nHex) {
/*  826 */       return dstInit;
/*      */     }
/*  828 */     if ((nHex - 1) * 4 + dstPos >= 32) {
/*  829 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 32");
/*      */     }
/*  831 */     int out = dstInit;
/*  832 */     int shift = 0;
/*  833 */     for (int i = 0; i < nHex; i++) {
/*  834 */       shift = i * 4 + dstPos;
/*  835 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  836 */       int mask = 15 << shift;
/*  837 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  839 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short hexToShort(String src, int srcPos, short dstInit, int dstPos, int nHex) {
/*  859 */     if (0 == nHex) {
/*  860 */       return dstInit;
/*      */     }
/*  862 */     if ((nHex - 1) * 4 + dstPos >= 16) {
/*  863 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 16");
/*      */     }
/*  865 */     short out = dstInit;
/*  866 */     int shift = 0;
/*  867 */     for (int i = 0; i < nHex; i++) {
/*  868 */       shift = i * 4 + dstPos;
/*  869 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  870 */       int mask = 15 << shift;
/*  871 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  873 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte hexToByte(String src, int srcPos, byte dstInit, int dstPos, int nHex) {
/*  893 */     if (0 == nHex) {
/*  894 */       return dstInit;
/*      */     }
/*  896 */     if ((nHex - 1) * 4 + dstPos >= 8) {
/*  897 */       throw new IllegalArgumentException("(nHexs-1)*4+dstPos is greather or equal to than 8");
/*      */     }
/*  899 */     byte out = dstInit;
/*  900 */     int shift = 0;
/*  901 */     for (int i = 0; i < nHex; i++) {
/*  902 */       shift = i * 4 + dstPos;
/*  903 */       int bits = (0xF & hexDigitToInt(src.charAt(i + srcPos))) << shift;
/*  904 */       int mask = 15 << shift;
/*  905 */       out = (byte)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/*  907 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binaryToLong(boolean[] src, int srcPos, long dstInit, int dstPos, int nBools) {
/*  929 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  930 */       return dstInit;
/*      */     }
/*  932 */     if (nBools - 1 + dstPos >= 64) {
/*  933 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 64");
/*      */     }
/*  935 */     long out = dstInit;
/*  936 */     int shift = 0;
/*  937 */     for (int i = 0; i < nBools; i++) {
/*  938 */       shift = i + dstPos;
/*  939 */       long bits = (src[i + srcPos] ? 1L : 0L) << shift;
/*  940 */       long mask = 1L << shift;
/*  941 */       out = out & (mask ^ 0xFFFFFFFFFFFFFFFFL) | bits;
/*      */     } 
/*  943 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binaryToInt(boolean[] src, int srcPos, int dstInit, int dstPos, int nBools) {
/*  965 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/*  966 */       return dstInit;
/*      */     }
/*  968 */     if (nBools - 1 + dstPos >= 32) {
/*  969 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 32");
/*      */     }
/*  971 */     int out = dstInit;
/*  972 */     int shift = 0;
/*  973 */     for (int i = 0; i < nBools; i++) {
/*  974 */       shift = i + dstPos;
/*  975 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/*  976 */       int mask = 1 << shift;
/*  977 */       out = out & (mask ^ 0xFFFFFFFF) | bits;
/*      */     } 
/*  979 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short binaryToShort(boolean[] src, int srcPos, short dstInit, int dstPos, int nBools) {
/* 1001 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/* 1002 */       return dstInit;
/*      */     }
/* 1004 */     if (nBools - 1 + dstPos >= 16) {
/* 1005 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 16");
/*      */     }
/* 1007 */     short out = dstInit;
/* 1008 */     int shift = 0;
/* 1009 */     for (int i = 0; i < nBools; i++) {
/* 1010 */       shift = i + dstPos;
/* 1011 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/* 1012 */       int mask = 1 << shift;
/* 1013 */       out = (short)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/* 1015 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte binaryToByte(boolean[] src, int srcPos, byte dstInit, int dstPos, int nBools) {
/* 1037 */     if ((src.length == 0 && srcPos == 0) || 0 == nBools) {
/* 1038 */       return dstInit;
/*      */     }
/* 1040 */     if (nBools - 1 + dstPos >= 8) {
/* 1041 */       throw new IllegalArgumentException("nBools-1+dstPos is greather or equal to than 8");
/*      */     }
/* 1043 */     byte out = dstInit;
/* 1044 */     int shift = 0;
/* 1045 */     for (int i = 0; i < nBools; i++) {
/* 1046 */       shift = i + dstPos;
/* 1047 */       int bits = (src[i + srcPos] ? 1 : 0) << shift;
/* 1048 */       int mask = 1 << shift;
/* 1049 */       out = (byte)(out & (mask ^ 0xFFFFFFFF) | bits);
/*      */     } 
/* 1051 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] longToIntArray(long src, int srcPos, int[] dst, int dstPos, int nInts) {
/* 1073 */     if (0 == nInts) {
/* 1074 */       return dst;
/*      */     }
/* 1076 */     if ((nInts - 1) * 32 + srcPos >= 64) {
/* 1077 */       throw new IllegalArgumentException("(nInts-1)*32+srcPos is greather or equal to than 64");
/*      */     }
/* 1079 */     int shift = 0;
/* 1080 */     for (int i = 0; i < nInts; i++) {
/* 1081 */       shift = i * 32 + srcPos;
/* 1082 */       dst[dstPos + i] = (int)(0xFFFFFFFFFFFFFFFFL & src >> shift);
/*      */     } 
/* 1084 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] longToShortArray(long src, int srcPos, short[] dst, int dstPos, int nShorts) {
/* 1106 */     if (0 == nShorts) {
/* 1107 */       return dst;
/*      */     }
/* 1109 */     if ((nShorts - 1) * 16 + srcPos >= 64) {
/* 1110 */       throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 64");
/*      */     }
/* 1112 */     int shift = 0;
/* 1113 */     for (int i = 0; i < nShorts; i++) {
/* 1114 */       shift = i * 16 + srcPos;
/* 1115 */       dst[dstPos + i] = (short)(int)(0xFFFFL & src >> shift);
/*      */     } 
/* 1117 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static short[] intToShortArray(int src, int srcPos, short[] dst, int dstPos, int nShorts) {
/* 1139 */     if (0 == nShorts) {
/* 1140 */       return dst;
/*      */     }
/* 1142 */     if ((nShorts - 1) * 16 + srcPos >= 32) {
/* 1143 */       throw new IllegalArgumentException("(nShorts-1)*16+srcPos is greather or equal to than 32");
/*      */     }
/* 1145 */     int shift = 0;
/* 1146 */     for (int i = 0; i < nShorts; i++) {
/* 1147 */       shift = i * 16 + srcPos;
/* 1148 */       dst[dstPos + i] = (short)(0xFFFF & src >> shift);
/*      */     } 
/* 1150 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] longToByteArray(long src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1172 */     if (0 == nBytes) {
/* 1173 */       return dst;
/*      */     }
/* 1175 */     if ((nBytes - 1) * 8 + srcPos >= 64) {
/* 1176 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 64");
/*      */     }
/* 1178 */     int shift = 0;
/* 1179 */     for (int i = 0; i < nBytes; i++) {
/* 1180 */       shift = i * 8 + srcPos;
/* 1181 */       dst[dstPos + i] = (byte)(int)(0xFFL & src >> shift);
/*      */     } 
/* 1183 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] intToByteArray(int src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1205 */     if (0 == nBytes) {
/* 1206 */       return dst;
/*      */     }
/* 1208 */     if ((nBytes - 1) * 8 + srcPos >= 32) {
/* 1209 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 32");
/*      */     }
/* 1211 */     int shift = 0;
/* 1212 */     for (int i = 0; i < nBytes; i++) {
/* 1213 */       shift = i * 8 + srcPos;
/* 1214 */       dst[dstPos + i] = (byte)(0xFF & src >> shift);
/*      */     } 
/* 1216 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] shortToByteArray(short src, int srcPos, byte[] dst, int dstPos, int nBytes) {
/* 1238 */     if (0 == nBytes) {
/* 1239 */       return dst;
/*      */     }
/* 1241 */     if ((nBytes - 1) * 8 + srcPos >= 16) {
/* 1242 */       throw new IllegalArgumentException("(nBytes-1)*8+srcPos is greather or equal to than 16");
/*      */     }
/* 1244 */     int shift = 0;
/* 1245 */     for (int i = 0; i < nBytes; i++) {
/* 1246 */       shift = i * 8 + srcPos;
/* 1247 */       dst[dstPos + i] = (byte)(0xFF & src >> shift);
/*      */     } 
/* 1249 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String longToHex(long src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1270 */     if (0 == nHexs) {
/* 1271 */       return dstInit;
/*      */     }
/* 1273 */     if ((nHexs - 1) * 4 + srcPos >= 64) {
/* 1274 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 64");
/*      */     }
/* 1276 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1277 */     int shift = 0;
/* 1278 */     int append = sb.length();
/* 1279 */     for (int i = 0; i < nHexs; i++) {
/* 1280 */       shift = i * 4 + srcPos;
/* 1281 */       int bits = (int)(0xFL & src >> shift);
/* 1282 */       if (dstPos + i == append) {
/* 1283 */         append++;
/* 1284 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1286 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1289 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String intToHex(int src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1310 */     if (0 == nHexs) {
/* 1311 */       return dstInit;
/*      */     }
/* 1313 */     if ((nHexs - 1) * 4 + srcPos >= 32) {
/* 1314 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 32");
/*      */     }
/* 1316 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1317 */     int shift = 0;
/* 1318 */     int append = sb.length();
/* 1319 */     for (int i = 0; i < nHexs; i++) {
/* 1320 */       shift = i * 4 + srcPos;
/* 1321 */       int bits = 0xF & src >> shift;
/* 1322 */       if (dstPos + i == append) {
/* 1323 */         append++;
/* 1324 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1326 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1329 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String shortToHex(short src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1350 */     if (0 == nHexs) {
/* 1351 */       return dstInit;
/*      */     }
/* 1353 */     if ((nHexs - 1) * 4 + srcPos >= 16) {
/* 1354 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 16");
/*      */     }
/* 1356 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1357 */     int shift = 0;
/* 1358 */     int append = sb.length();
/* 1359 */     for (int i = 0; i < nHexs; i++) {
/* 1360 */       shift = i * 4 + srcPos;
/* 1361 */       int bits = 0xF & src >> shift;
/* 1362 */       if (dstPos + i == append) {
/* 1363 */         append++;
/* 1364 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1366 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1369 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String byteToHex(byte src, int srcPos, String dstInit, int dstPos, int nHexs) {
/* 1390 */     if (0 == nHexs) {
/* 1391 */       return dstInit;
/*      */     }
/* 1393 */     if ((nHexs - 1) * 4 + srcPos >= 8) {
/* 1394 */       throw new IllegalArgumentException("(nHexs-1)*4+srcPos is greather or equal to than 8");
/*      */     }
/* 1396 */     StringBuilder sb = new StringBuilder(dstInit);
/* 1397 */     int shift = 0;
/* 1398 */     int append = sb.length();
/* 1399 */     for (int i = 0; i < nHexs; i++) {
/* 1400 */       shift = i * 4 + srcPos;
/* 1401 */       int bits = 0xF & src >> shift;
/* 1402 */       if (dstPos + i == append) {
/* 1403 */         append++;
/* 1404 */         sb.append(intToHexDigit(bits));
/*      */       } else {
/* 1406 */         sb.setCharAt(dstPos + i, intToHexDigit(bits));
/*      */       } 
/*      */     } 
/* 1409 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] longToBinary(long src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1431 */     if (0 == nBools) {
/* 1432 */       return dst;
/*      */     }
/* 1434 */     if (nBools - 1 + srcPos >= 64) {
/* 1435 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 64");
/*      */     }
/* 1437 */     int shift = 0;
/* 1438 */     for (int i = 0; i < nBools; i++) {
/* 1439 */       shift = i + srcPos;
/* 1440 */       dst[dstPos + i] = ((0x1L & src >> shift) != 0L);
/*      */     } 
/* 1442 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] intToBinary(int src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1464 */     if (0 == nBools) {
/* 1465 */       return dst;
/*      */     }
/* 1467 */     if (nBools - 1 + srcPos >= 32) {
/* 1468 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 32");
/*      */     }
/* 1470 */     int shift = 0;
/* 1471 */     for (int i = 0; i < nBools; i++) {
/* 1472 */       shift = i + srcPos;
/* 1473 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1475 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] shortToBinary(short src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1497 */     if (0 == nBools) {
/* 1498 */       return dst;
/*      */     }
/* 1500 */     if (nBools - 1 + srcPos >= 16) {
/* 1501 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 16");
/*      */     }
/* 1503 */     int shift = 0;
/* 1504 */     assert nBools - 1 < 16 - srcPos;
/* 1505 */     for (int i = 0; i < nBools; i++) {
/* 1506 */       shift = i + srcPos;
/* 1507 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1509 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] byteToBinary(byte src, int srcPos, boolean[] dst, int dstPos, int nBools) {
/* 1531 */     if (0 == nBools) {
/* 1532 */       return dst;
/*      */     }
/* 1534 */     if (nBools - 1 + srcPos >= 8) {
/* 1535 */       throw new IllegalArgumentException("nBools-1+srcPos is greather or equal to than 8");
/*      */     }
/* 1537 */     int shift = 0;
/* 1538 */     for (int i = 0; i < nBools; i++) {
/* 1539 */       shift = i + srcPos;
/* 1540 */       dst[dstPos + i] = ((0x1 & src >> shift) != 0);
/*      */     } 
/* 1542 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] uuidToByteArray(UUID src, byte[] dst, int dstPos, int nBytes) {
/* 1562 */     if (0 == nBytes) {
/* 1563 */       return dst;
/*      */     }
/* 1565 */     if (nBytes > 16) {
/* 1566 */       throw new IllegalArgumentException("nBytes is greather than 16");
/*      */     }
/* 1568 */     longToByteArray(src.getMostSignificantBits(), 0, dst, dstPos, (nBytes > 8) ? 8 : nBytes);
/* 1569 */     if (nBytes >= 8) {
/* 1570 */       longToByteArray(src.getLeastSignificantBits(), 0, dst, dstPos + 8, nBytes - 8);
/*      */     }
/* 1572 */     return dst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UUID byteArrayToUuid(byte[] src, int srcPos) {
/* 1589 */     if (src.length - srcPos < 16) {
/* 1590 */       throw new IllegalArgumentException("Need at least 16 bytes for UUID");
/*      */     }
/* 1592 */     return new UUID(byteArrayToLong(src, srcPos, 0L, 0, 8), byteArrayToLong(src, srcPos + 8, 0L, 0, 8));
/*      */   }
/*      */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\Conversion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */