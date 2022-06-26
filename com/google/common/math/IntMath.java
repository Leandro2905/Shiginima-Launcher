/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.RoundingMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class IntMath
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
/*     */   
/*     */   public static boolean isPowerOfTwo(int x) {
/*  63 */     return ((x > 0)) & (((x & x - 1) == 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static int lessThanBranchFree(int x, int y) {
/*  75 */     return (x - y ^ 0xFFFFFFFF ^ 0xFFFFFFFF) >>> 31;
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
/*     */   public static int log2(int x, RoundingMode mode) {
/*     */     int leadingZeros, cmp, logFloor;
/*  88 */     MathPreconditions.checkPositive("x", x);
/*  89 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  91 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/*  95 */         return 31 - Integer.numberOfLeadingZeros(x);
/*     */       
/*     */       case UP:
/*     */       case CEILING:
/*  99 */         return 32 - Integer.numberOfLeadingZeros(x - 1);
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 105 */         leadingZeros = Integer.numberOfLeadingZeros(x);
/* 106 */         cmp = -1257966797 >>> leadingZeros;
/*     */         
/* 108 */         logFloor = 31 - leadingZeros;
/* 109 */         return logFloor + lessThanBranchFree(cmp, x);
/*     */     } 
/*     */     
/* 112 */     throw new AssertionError();
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
/*     */   @GwtIncompatible("need BigIntegerMath to adequately test")
/*     */   public static int log10(int x, RoundingMode mode) {
/* 129 */     MathPreconditions.checkPositive("x", x);
/* 130 */     int logFloor = log10Floor(x);
/* 131 */     int floorPow = powersOf10[logFloor];
/* 132 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 134 */         MathPreconditions.checkRoundingUnnecessary((x == floorPow));
/*     */       
/*     */       case DOWN:
/*     */       case FLOOR:
/* 138 */         return logFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 141 */         return logFloor + lessThanBranchFree(floorPow, x);
/*     */       
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 146 */         return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
/*     */     } 
/* 148 */     throw new AssertionError();
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
/*     */   private static int log10Floor(int x) {
/* 160 */     int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     return y - lessThanBranchFree(x, powersOf10[y]);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/* 169 */   static final byte[] maxLog10ForLeadingZeros = new byte[] { 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0 };
/*     */   
/*     */   @VisibleForTesting
/* 172 */   static final int[] powersOf10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 176 */   static final int[] halfPowersOf10 = new int[] { 3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int FLOOR_SQRT_MAX_INT = 46340;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("failing tests")
/*     */   public static int pow(int b, int k) {
/* 190 */     MathPreconditions.checkNonNegative("exponent", k);
/* 191 */     switch (b) {
/*     */       case 0:
/* 193 */         return (k == 0) ? 1 : 0;
/*     */       case 1:
/* 195 */         return 1;
/*     */       case -1:
/* 197 */         return ((k & 0x1) == 0) ? 1 : -1;
/*     */       case 2:
/* 199 */         return (k < 32) ? (1 << k) : 0;
/*     */       case -2:
/* 201 */         if (k < 32) {
/* 202 */           return ((k & 0x1) == 0) ? (1 << k) : -(1 << k);
/*     */         }
/* 204 */         return 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 209 */     for (int accum = 1;; k >>= 1) {
/* 210 */       switch (k) {
/*     */         case 0:
/* 212 */           return accum;
/*     */         case 1:
/* 214 */           return b * accum;
/*     */       } 
/* 216 */       accum *= ((k & 0x1) == 0) ? 1 : b;
/* 217 */       b *= b;
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
/*     */   @GwtIncompatible("need BigIntegerMath to adequately test")
/*     */   public static int sqrt(int x, RoundingMode mode) {
/*     */     int halfSquare;
/* 232 */     MathPreconditions.checkNonNegative("x", x);
/* 233 */     int sqrtFloor = sqrtFloor(x);
/* 234 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 236 */         MathPreconditions.checkRoundingUnnecessary((sqrtFloor * sqrtFloor == x));
/*     */       case DOWN:
/*     */       case FLOOR:
/* 239 */         return sqrtFloor;
/*     */       case UP:
/*     */       case CEILING:
/* 242 */         return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
/*     */       case HALF_DOWN:
/*     */       case HALF_UP:
/*     */       case HALF_EVEN:
/* 246 */         halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 258 */         return sqrtFloor + lessThanBranchFree(halfSquare, x);
/*     */     } 
/* 260 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int sqrtFloor(int x) {
/* 267 */     return (int)Math.sqrt(x);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int divide(int p, int q, RoundingMode mode) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: invokestatic checkNotNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   4: pop
/*     */     //   5: iload_1
/*     */     //   6: ifne -> 19
/*     */     //   9: new java/lang/ArithmeticException
/*     */     //   12: dup
/*     */     //   13: ldc '/ by zero'
/*     */     //   15: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   18: athrow
/*     */     //   19: iload_0
/*     */     //   20: iload_1
/*     */     //   21: idiv
/*     */     //   22: istore_3
/*     */     //   23: iload_0
/*     */     //   24: iload_1
/*     */     //   25: iload_3
/*     */     //   26: imul
/*     */     //   27: isub
/*     */     //   28: istore #4
/*     */     //   30: iload #4
/*     */     //   32: ifne -> 37
/*     */     //   35: iload_3
/*     */     //   36: ireturn
/*     */     //   37: iconst_1
/*     */     //   38: iload_0
/*     */     //   39: iload_1
/*     */     //   40: ixor
/*     */     //   41: bipush #31
/*     */     //   43: ishr
/*     */     //   44: ior
/*     */     //   45: istore #5
/*     */     //   47: getstatic com/google/common/math/IntMath$1.$SwitchMap$java$math$RoundingMode : [I
/*     */     //   50: aload_2
/*     */     //   51: invokevirtual ordinal : ()I
/*     */     //   54: iaload
/*     */     //   55: tableswitch default -> 238, 1 -> 100, 2 -> 113, 3 -> 140, 4 -> 119, 5 -> 125, 6 -> 155, 7 -> 155, 8 -> 155
/*     */     //   100: iload #4
/*     */     //   102: ifne -> 109
/*     */     //   105: iconst_1
/*     */     //   106: goto -> 110
/*     */     //   109: iconst_0
/*     */     //   110: invokestatic checkRoundingUnnecessary : (Z)V
/*     */     //   113: iconst_0
/*     */     //   114: istore #6
/*     */     //   116: goto -> 246
/*     */     //   119: iconst_1
/*     */     //   120: istore #6
/*     */     //   122: goto -> 246
/*     */     //   125: iload #5
/*     */     //   127: ifle -> 134
/*     */     //   130: iconst_1
/*     */     //   131: goto -> 135
/*     */     //   134: iconst_0
/*     */     //   135: istore #6
/*     */     //   137: goto -> 246
/*     */     //   140: iload #5
/*     */     //   142: ifge -> 149
/*     */     //   145: iconst_1
/*     */     //   146: goto -> 150
/*     */     //   149: iconst_0
/*     */     //   150: istore #6
/*     */     //   152: goto -> 246
/*     */     //   155: iload #4
/*     */     //   157: invokestatic abs : (I)I
/*     */     //   160: istore #7
/*     */     //   162: iload #7
/*     */     //   164: iload_1
/*     */     //   165: invokestatic abs : (I)I
/*     */     //   168: iload #7
/*     */     //   170: isub
/*     */     //   171: isub
/*     */     //   172: istore #8
/*     */     //   174: iload #8
/*     */     //   176: ifne -> 223
/*     */     //   179: aload_2
/*     */     //   180: getstatic java/math/RoundingMode.HALF_UP : Ljava/math/RoundingMode;
/*     */     //   183: if_acmpeq -> 213
/*     */     //   186: aload_2
/*     */     //   187: getstatic java/math/RoundingMode.HALF_EVEN : Ljava/math/RoundingMode;
/*     */     //   190: if_acmpne -> 197
/*     */     //   193: iconst_1
/*     */     //   194: goto -> 198
/*     */     //   197: iconst_0
/*     */     //   198: iload_3
/*     */     //   199: iconst_1
/*     */     //   200: iand
/*     */     //   201: ifeq -> 208
/*     */     //   204: iconst_1
/*     */     //   205: goto -> 209
/*     */     //   208: iconst_0
/*     */     //   209: iand
/*     */     //   210: ifeq -> 217
/*     */     //   213: iconst_1
/*     */     //   214: goto -> 218
/*     */     //   217: iconst_0
/*     */     //   218: istore #6
/*     */     //   220: goto -> 246
/*     */     //   223: iload #8
/*     */     //   225: ifle -> 232
/*     */     //   228: iconst_1
/*     */     //   229: goto -> 233
/*     */     //   232: iconst_0
/*     */     //   233: istore #6
/*     */     //   235: goto -> 246
/*     */     //   238: new java/lang/AssertionError
/*     */     //   241: dup
/*     */     //   242: invokespecial <init> : ()V
/*     */     //   245: athrow
/*     */     //   246: iload #6
/*     */     //   248: ifeq -> 258
/*     */     //   251: iload_3
/*     */     //   252: iload #5
/*     */     //   254: iadd
/*     */     //   255: goto -> 259
/*     */     //   258: iload_3
/*     */     //   259: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #279	-> 0
/*     */     //   #280	-> 5
/*     */     //   #281	-> 9
/*     */     //   #283	-> 19
/*     */     //   #284	-> 23
/*     */     //   #286	-> 30
/*     */     //   #287	-> 35
/*     */     //   #297	-> 37
/*     */     //   #299	-> 47
/*     */     //   #301	-> 100
/*     */     //   #304	-> 113
/*     */     //   #305	-> 116
/*     */     //   #307	-> 119
/*     */     //   #308	-> 122
/*     */     //   #310	-> 125
/*     */     //   #311	-> 137
/*     */     //   #313	-> 140
/*     */     //   #314	-> 152
/*     */     //   #318	-> 155
/*     */     //   #319	-> 162
/*     */     //   #322	-> 174
/*     */     //   #323	-> 179
/*     */     //   #325	-> 223
/*     */     //   #327	-> 235
/*     */     //   #329	-> 238
/*     */     //   #331	-> 246
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   116	107	6	increment	Z
/*     */     //   162	84	7	absRem	I
/*     */     //   174	72	8	cmpRemToHalfDivisor	I
/*     */     //   0	260	0	p	I
/*     */     //   0	260	1	q	I
/*     */     //   0	260	2	mode	Ljava/math/RoundingMode;
/*     */     //   23	237	3	div	I
/*     */     //   30	230	4	rem	I
/*     */     //   47	213	5	signum	I
/*     */     //   235	25	6	increment	Z
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mod(int x, int m) {
/* 351 */     if (m <= 0) {
/* 352 */       int i = m; throw new ArithmeticException((new StringBuilder(31)).append("Modulus ").append(i).append(" must be > 0").toString());
/*     */     } 
/* 354 */     int result = x % m;
/* 355 */     return (result >= 0) ? result : (result + m);
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
/*     */   public static int gcd(int a, int b) {
/* 370 */     MathPreconditions.checkNonNegative("a", a);
/* 371 */     MathPreconditions.checkNonNegative("b", b);
/* 372 */     if (a == 0)
/*     */     {
/*     */       
/* 375 */       return b; } 
/* 376 */     if (b == 0) {
/* 377 */       return a;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     int aTwos = Integer.numberOfTrailingZeros(a);
/* 384 */     a >>= aTwos;
/* 385 */     int bTwos = Integer.numberOfTrailingZeros(b);
/* 386 */     b >>= bTwos;
/* 387 */     while (a != b) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 395 */       int delta = a - b;
/*     */       
/* 397 */       int minDeltaOrZero = delta & delta >> 31;
/*     */ 
/*     */       
/* 400 */       a = delta - minDeltaOrZero - minDeltaOrZero;
/*     */ 
/*     */       
/* 403 */       b += minDeltaOrZero;
/* 404 */       a >>= Integer.numberOfTrailingZeros(a);
/*     */     } 
/* 406 */     return a << Math.min(aTwos, bTwos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int checkedAdd(int a, int b) {
/* 415 */     long result = a + b;
/* 416 */     MathPreconditions.checkNoOverflow((result == (int)result));
/* 417 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int checkedSubtract(int a, int b) {
/* 426 */     long result = a - b;
/* 427 */     MathPreconditions.checkNoOverflow((result == (int)result));
/* 428 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int checkedMultiply(int a, int b) {
/* 437 */     long result = a * b;
/* 438 */     MathPreconditions.checkNoOverflow((result == (int)result));
/* 439 */     return (int)result;
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
/*     */   public static int checkedPow(int b, int k) {
/* 451 */     MathPreconditions.checkNonNegative("exponent", k);
/* 452 */     switch (b) {
/*     */       case 0:
/* 454 */         return (k == 0) ? 1 : 0;
/*     */       case 1:
/* 456 */         return 1;
/*     */       case -1:
/* 458 */         return ((k & 0x1) == 0) ? 1 : -1;
/*     */       case 2:
/* 460 */         MathPreconditions.checkNoOverflow((k < 31));
/* 461 */         return 1 << k;
/*     */       case -2:
/* 463 */         MathPreconditions.checkNoOverflow((k < 32));
/* 464 */         return ((k & 0x1) == 0) ? (1 << k) : (-1 << k);
/*     */     } 
/*     */ 
/*     */     
/* 468 */     int accum = 1;
/*     */     while (true) {
/* 470 */       switch (k) {
/*     */         case 0:
/* 472 */           return accum;
/*     */         case 1:
/* 474 */           return checkedMultiply(accum, b);
/*     */       } 
/* 476 */       if ((k & 0x1) != 0) {
/* 477 */         accum = checkedMultiply(accum, b);
/*     */       }
/* 479 */       k >>= 1;
/* 480 */       if (k > 0) {
/* 481 */         MathPreconditions.checkNoOverflow(((-46340 <= b)) & ((b <= 46340)));
/* 482 */         b *= b;
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
/*     */   public static int factorial(int n) {
/* 498 */     MathPreconditions.checkNonNegative("n", n);
/* 499 */     return (n < factorials.length) ? factorials[n] : Integer.MAX_VALUE;
/*     */   }
/*     */   
/* 502 */   private static final int[] factorials = new int[] { 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("need BigIntegerMath to adequately test")
/*     */   public static int binomial(int n, int k) {
/* 525 */     MathPreconditions.checkNonNegative("n", n);
/* 526 */     MathPreconditions.checkNonNegative("k", k);
/* 527 */     Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", new Object[] { Integer.valueOf(k), Integer.valueOf(n) });
/* 528 */     if (k > n >> 1) {
/* 529 */       k = n - k;
/*     */     }
/* 531 */     if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
/* 532 */       return Integer.MAX_VALUE;
/*     */     }
/* 534 */     switch (k) {
/*     */       case 0:
/* 536 */         return 1;
/*     */       case 1:
/* 538 */         return n;
/*     */     } 
/* 540 */     long result = 1L;
/* 541 */     for (int i = 0; i < k; i++) {
/* 542 */       result *= (n - i);
/* 543 */       result /= (i + 1);
/*     */     } 
/* 545 */     return (int)result;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 550 */   static int[] biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int mean(int x, int y) {
/* 580 */     return (x & y) + ((x ^ y) >> 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\math\IntMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */