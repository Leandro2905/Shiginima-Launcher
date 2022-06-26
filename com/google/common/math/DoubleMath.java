/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import java.math.BigInteger;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class DoubleMath
/*     */ {
/*     */   private static final double MIN_INT_AS_DOUBLE = -2.147483648E9D;
/*     */   private static final double MAX_INT_AS_DOUBLE = 2.147483647E9D;
/*     */   private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18D;
/*     */   private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18D;
/*     */   
/*     */   @GwtIncompatible("#isMathematicalInteger, com.google.common.math.DoubleUtils")
/*     */   static double roundIntermediate(double x, RoundingMode mode) {
/*     */     double z;
/*  58 */     if (!DoubleUtils.isFinite(x)) {
/*  59 */       throw new ArithmeticException("input is infinite or NaN");
/*     */     }
/*  61 */     switch (mode) {
/*     */       case UNNECESSARY:
/*  63 */         MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
/*  64 */         return x;
/*     */       
/*     */       case FLOOR:
/*  67 */         if (x >= 0.0D || isMathematicalInteger(x)) {
/*  68 */           return x;
/*     */         }
/*  70 */         return x - 1.0D;
/*     */ 
/*     */       
/*     */       case CEILING:
/*  74 */         if (x <= 0.0D || isMathematicalInteger(x)) {
/*  75 */           return x;
/*     */         }
/*  77 */         return x + 1.0D;
/*     */ 
/*     */       
/*     */       case DOWN:
/*  81 */         return x;
/*     */       
/*     */       case UP:
/*  84 */         if (isMathematicalInteger(x)) {
/*  85 */           return x;
/*     */         }
/*  87 */         return x + Math.copySign(1.0D, x);
/*     */ 
/*     */       
/*     */       case HALF_EVEN:
/*  91 */         return Math.rint(x);
/*     */       
/*     */       case HALF_UP:
/*  94 */         z = Math.rint(x);
/*  95 */         if (Math.abs(x - z) == 0.5D) {
/*  96 */           return x + Math.copySign(0.5D, x);
/*     */         }
/*  98 */         return z;
/*     */ 
/*     */ 
/*     */       
/*     */       case HALF_DOWN:
/* 103 */         z = Math.rint(x);
/* 104 */         if (Math.abs(x - z) == 0.5D) {
/* 105 */           return x;
/*     */         }
/* 107 */         return z;
/*     */     } 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("#roundIntermediate")
/*     */   public static int roundToInt(double x, RoundingMode mode) {
/* 132 */     double z = roundIntermediate(x, mode);
/* 133 */     MathPreconditions.checkInRange(((z > -2.147483649E9D)) & ((z < 2.147483648E9D)));
/* 134 */     return (int)z;
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
/*     */   @GwtIncompatible("#roundIntermediate")
/*     */   public static long roundToLong(double x, RoundingMode mode) {
/* 156 */     double z = roundIntermediate(x, mode);
/* 157 */     MathPreconditions.checkInRange(((-9.223372036854776E18D - z < 1.0D)) & ((z < 9.223372036854776E18D)));
/* 158 */     return (long)z;
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
/*     */   @GwtIncompatible("#roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
/*     */   public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
/* 182 */     x = roundIntermediate(x, mode);
/* 183 */     if ((((-9.223372036854776E18D - x < 1.0D) ? 1 : 0) & ((x < 9.223372036854776E18D) ? 1 : 0)) != 0) {
/* 184 */       return BigInteger.valueOf((long)x);
/*     */     }
/* 186 */     int exponent = Math.getExponent(x);
/* 187 */     long significand = DoubleUtils.getSignificand(x);
/* 188 */     BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
/* 189 */     return (x < 0.0D) ? result.negate() : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("com.google.common.math.DoubleUtils")
/*     */   public static boolean isPowerOfTwo(double x) {
/* 198 */     return (x > 0.0D && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x)));
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
/*     */   public static double log2(double x) {
/* 217 */     return Math.log(x) / LN_2;
/*     */   }
/*     */   
/* 220 */   private static final double LN_2 = Math.log(2.0D);
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final int MAX_FACTORIAL = 170;
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
/*     */   public static int log2(double x, RoundingMode mode) {
/*     */     boolean increment;
/*     */     int i;
/*     */     double xScaled;
/* 234 */     Preconditions.checkArgument((x > 0.0D && DoubleUtils.isFinite(x)), "x must be positive and finite");
/* 235 */     int exponent = Math.getExponent(x);
/* 236 */     if (!DoubleUtils.isNormal(x)) {
/* 237 */       return log2(x * 4.503599627370496E15D, mode) - 52;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 242 */     switch (mode) {
/*     */       case UNNECESSARY:
/* 244 */         MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
/*     */       
/*     */       case FLOOR:
/* 247 */         increment = false;
/*     */         break;
/*     */       case CEILING:
/* 250 */         increment = !isPowerOfTwo(x);
/*     */         break;
/*     */       case DOWN:
/* 253 */         i = ((exponent < 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case UP:
/* 256 */         i = ((exponent >= 0) ? 1 : 0) & (!isPowerOfTwo(x) ? 1 : 0);
/*     */         break;
/*     */       case HALF_EVEN:
/*     */       case HALF_UP:
/*     */       case HALF_DOWN:
/* 261 */         xScaled = DoubleUtils.scaleNormalize(x);
/*     */ 
/*     */         
/* 264 */         i = (xScaled * xScaled > 2.0D) ? 1 : 0;
/*     */         break;
/*     */       default:
/* 267 */         throw new AssertionError();
/*     */     } 
/* 269 */     return (i != 0) ? (exponent + 1) : exponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
/*     */   public static boolean isMathematicalInteger(double x) {
/* 280 */     return (DoubleUtils.isFinite(x) && (x == 0.0D || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x)));
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
/*     */   public static double factorial(int n) {
/* 295 */     MathPreconditions.checkNonNegative("n", n);
/* 296 */     if (n > 170) {
/* 297 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */ 
/*     */     
/* 301 */     double accum = 1.0D;
/* 302 */     for (int i = 1 + (n & 0xFFFFFFF0); i <= n; i++) {
/* 303 */       accum *= i;
/*     */     }
/* 305 */     return accum * everySixteenthFactorial[n >> 4];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 313 */   static final double[] everySixteenthFactorial = new double[] { 1.0D, 2.0922789888E13D, 2.631308369336935E35D, 1.2413915592536073E61D, 1.2688693218588417E89D, 7.156945704626381E118D, 9.916779348709496E149D, 1.974506857221074E182D, 3.856204823625804E215D, 5.5502938327393044E249D, 4.7147236359920616E284D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean fuzzyEquals(double a, double b, double tolerance) {
/* 352 */     MathPreconditions.checkNonNegative("tolerance", tolerance);
/* 353 */     return (Math.copySign(a - b, 1.0D) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b)));
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
/*     */   public static int fuzzyCompare(double a, double b, double tolerance) {
/* 375 */     if (fuzzyEquals(a, b, tolerance))
/* 376 */       return 0; 
/* 377 */     if (a < b)
/* 378 */       return -1; 
/* 379 */     if (a > b) {
/* 380 */       return 1;
/*     */     }
/* 382 */     return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("com.google.common.math.DoubleUtils")
/*     */   private static final class MeanAccumulator
/*     */   {
/* 389 */     private long count = 0L;
/* 390 */     private double mean = 0.0D;
/*     */     
/*     */     void add(double value) {
/* 393 */       Preconditions.checkArgument(DoubleUtils.isFinite(value));
/* 394 */       this.count++;
/*     */       
/* 396 */       this.mean += (value - this.mean) / this.count;
/*     */     }
/*     */     
/*     */     double mean() {
/* 400 */       Preconditions.checkArgument((this.count > 0L), "Cannot take mean of 0 values");
/* 401 */       return this.mean;
/*     */     }
/*     */ 
/*     */     
/*     */     private MeanAccumulator() {}
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("MeanAccumulator")
/*     */   public static double mean(double... values) {
/* 411 */     MeanAccumulator accumulator = new MeanAccumulator();
/* 412 */     for (double value : values) {
/* 413 */       accumulator.add(value);
/*     */     }
/* 415 */     return accumulator.mean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("MeanAccumulator")
/*     */   public static double mean(int... values) {
/* 424 */     MeanAccumulator accumulator = new MeanAccumulator();
/* 425 */     for (int value : values) {
/* 426 */       accumulator.add(value);
/*     */     }
/* 428 */     return accumulator.mean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("MeanAccumulator")
/*     */   public static double mean(long... values) {
/* 438 */     MeanAccumulator accumulator = new MeanAccumulator();
/* 439 */     for (long value : values) {
/* 440 */       accumulator.add(value);
/*     */     }
/* 442 */     return accumulator.mean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("MeanAccumulator")
/*     */   public static double mean(Iterable<? extends Number> values) {
/* 452 */     MeanAccumulator accumulator = new MeanAccumulator();
/* 453 */     for (Number value : values) {
/* 454 */       accumulator.add(value.doubleValue());
/*     */     }
/* 456 */     return accumulator.mean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("MeanAccumulator")
/*     */   public static double mean(Iterator<? extends Number> values) {
/* 466 */     MeanAccumulator accumulator = new MeanAccumulator();
/* 467 */     while (values.hasNext()) {
/* 468 */       accumulator.add(((Number)values.next()).doubleValue());
/*     */     }
/* 470 */     return accumulator.mean();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\math\DoubleMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */