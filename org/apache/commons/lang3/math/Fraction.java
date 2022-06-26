/*     */ package org.apache.commons.lang3.math;
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
/*     */ public final class Fraction
/*     */   extends Number
/*     */   implements Comparable<Fraction>
/*     */ {
/*     */   private static final long serialVersionUID = 65382027393090L;
/*  47 */   public static final Fraction ZERO = new Fraction(0, 1);
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Fraction ONE = new Fraction(1, 1);
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final Fraction ONE_HALF = new Fraction(1, 2);
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final Fraction ONE_THIRD = new Fraction(1, 3);
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Fraction TWO_THIRDS = new Fraction(2, 3);
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final Fraction ONE_QUARTER = new Fraction(1, 4);
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static final Fraction ONE_FIFTH = new Fraction(1, 5);
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int numerator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int denominator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private transient int hashCode = 0;
/*     */ 
/*     */ 
/*     */   
/* 110 */   private transient String toString = null;
/*     */ 
/*     */ 
/*     */   
/* 114 */   private transient String toProperString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Fraction(int numerator, int denominator) {
/* 125 */     this.numerator = numerator;
/* 126 */     this.denominator = denominator;
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
/*     */   public static Fraction getFraction(int numerator, int denominator) {
/* 142 */     if (denominator == 0) {
/* 143 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 145 */     if (denominator < 0) {
/* 146 */       if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
/* 147 */         throw new ArithmeticException("overflow: can't negate");
/*     */       }
/* 149 */       numerator = -numerator;
/* 150 */       denominator = -denominator;
/*     */     } 
/* 152 */     return new Fraction(numerator, denominator);
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
/*     */   public static Fraction getFraction(int whole, int numerator, int denominator) {
/*     */     long numeratorValue;
/* 172 */     if (denominator == 0) {
/* 173 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 175 */     if (denominator < 0) {
/* 176 */       throw new ArithmeticException("The denominator must not be negative");
/*     */     }
/* 178 */     if (numerator < 0) {
/* 179 */       throw new ArithmeticException("The numerator must not be negative");
/*     */     }
/*     */     
/* 182 */     if (whole < 0) {
/* 183 */       numeratorValue = whole * denominator - numerator;
/*     */     } else {
/* 185 */       numeratorValue = whole * denominator + numerator;
/*     */     } 
/* 187 */     if (numeratorValue < -2147483648L || numeratorValue > 2147483647L) {
/* 188 */       throw new ArithmeticException("Numerator too large to represent as an Integer.");
/*     */     }
/* 190 */     return new Fraction((int)numeratorValue, denominator);
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
/*     */   public static Fraction getReducedFraction(int numerator, int denominator) {
/* 208 */     if (denominator == 0) {
/* 209 */       throw new ArithmeticException("The denominator must not be zero");
/*     */     }
/* 211 */     if (numerator == 0) {
/* 212 */       return ZERO;
/*     */     }
/*     */     
/* 215 */     if (denominator == Integer.MIN_VALUE && (numerator & 0x1) == 0) {
/* 216 */       numerator /= 2;
/* 217 */       denominator /= 2;
/*     */     } 
/* 219 */     if (denominator < 0) {
/* 220 */       if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
/* 221 */         throw new ArithmeticException("overflow: can't negate");
/*     */       }
/* 223 */       numerator = -numerator;
/* 224 */       denominator = -denominator;
/*     */     } 
/*     */     
/* 227 */     int gcd = greatestCommonDivisor(numerator, denominator);
/* 228 */     numerator /= gcd;
/* 229 */     denominator /= gcd;
/* 230 */     return new Fraction(numerator, denominator);
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
/*     */   public static Fraction getFraction(double value) {
/*     */     double delta1;
/* 248 */     int sign = (value < 0.0D) ? -1 : 1;
/* 249 */     value = Math.abs(value);
/* 250 */     if (value > 2.147483647E9D || Double.isNaN(value)) {
/* 251 */       throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
/*     */     }
/* 253 */     int wholeNumber = (int)value;
/* 254 */     value -= wholeNumber;
/*     */     
/* 256 */     int numer0 = 0;
/* 257 */     int denom0 = 1;
/* 258 */     int numer1 = 1;
/* 259 */     int denom1 = 0;
/* 260 */     int numer2 = 0;
/* 261 */     int denom2 = 0;
/* 262 */     int a1 = (int)value;
/* 263 */     int a2 = 0;
/* 264 */     double x1 = 1.0D;
/* 265 */     double x2 = 0.0D;
/* 266 */     double y1 = value - a1;
/* 267 */     double y2 = 0.0D;
/* 268 */     double delta2 = Double.MAX_VALUE;
/*     */     
/* 270 */     int i = 1;
/*     */     
/*     */     do {
/* 273 */       delta1 = delta2;
/* 274 */       a2 = (int)(x1 / y1);
/* 275 */       x2 = y1;
/* 276 */       y2 = x1 - a2 * y1;
/* 277 */       numer2 = a1 * numer1 + numer0;
/* 278 */       denom2 = a1 * denom1 + denom0;
/* 279 */       double fraction = numer2 / denom2;
/* 280 */       delta2 = Math.abs(value - fraction);
/*     */       
/* 282 */       a1 = a2;
/* 283 */       x1 = x2;
/* 284 */       y1 = y2;
/* 285 */       numer0 = numer1;
/* 286 */       denom0 = denom1;
/* 287 */       numer1 = numer2;
/* 288 */       denom1 = denom2;
/* 289 */       i++;
/*     */     }
/* 291 */     while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
/* 292 */     if (i == 25) {
/* 293 */       throw new ArithmeticException("Unable to convert double to fraction");
/*     */     }
/* 295 */     return getReducedFraction((numer0 + wholeNumber * denom0) * sign, denom0);
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
/*     */   public static Fraction getFraction(String str) {
/* 317 */     if (str == null) {
/* 318 */       throw new IllegalArgumentException("The string must not be null");
/*     */     }
/*     */     
/* 321 */     int pos = str.indexOf('.');
/* 322 */     if (pos >= 0) {
/* 323 */       return getFraction(Double.parseDouble(str));
/*     */     }
/*     */ 
/*     */     
/* 327 */     pos = str.indexOf(' ');
/* 328 */     if (pos > 0) {
/* 329 */       int whole = Integer.parseInt(str.substring(0, pos));
/* 330 */       str = str.substring(pos + 1);
/* 331 */       pos = str.indexOf('/');
/* 332 */       if (pos < 0) {
/* 333 */         throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
/*     */       }
/* 335 */       int i = Integer.parseInt(str.substring(0, pos));
/* 336 */       int j = Integer.parseInt(str.substring(pos + 1));
/* 337 */       return getFraction(whole, i, j);
/*     */     } 
/*     */ 
/*     */     
/* 341 */     pos = str.indexOf('/');
/* 342 */     if (pos < 0)
/*     */     {
/* 344 */       return getFraction(Integer.parseInt(str), 1);
/*     */     }
/* 346 */     int numer = Integer.parseInt(str.substring(0, pos));
/* 347 */     int denom = Integer.parseInt(str.substring(pos + 1));
/* 348 */     return getFraction(numer, denom);
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
/*     */   public int getNumerator() {
/* 363 */     return this.numerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDenominator() {
/* 372 */     return this.denominator;
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
/*     */   public int getProperNumerator() {
/* 387 */     return Math.abs(this.numerator % this.denominator);
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
/*     */   public int getProperWhole() {
/* 402 */     return this.numerator / this.denominator;
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
/*     */   public int intValue() {
/* 416 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 427 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 438 */     return this.numerator / this.denominator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 449 */     return this.numerator / this.denominator;
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
/*     */   public Fraction reduce() {
/* 465 */     if (this.numerator == 0) {
/* 466 */       return equals(ZERO) ? this : ZERO;
/*     */     }
/* 468 */     int gcd = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
/* 469 */     if (gcd == 1) {
/* 470 */       return this;
/*     */     }
/* 472 */     return getFraction(this.numerator / gcd, this.denominator / gcd);
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
/*     */   public Fraction invert() {
/* 485 */     if (this.numerator == 0) {
/* 486 */       throw new ArithmeticException("Unable to invert zero.");
/*     */     }
/* 488 */     if (this.numerator == Integer.MIN_VALUE) {
/* 489 */       throw new ArithmeticException("overflow: can't negate numerator");
/*     */     }
/* 491 */     if (this.numerator < 0) {
/* 492 */       return new Fraction(-this.denominator, -this.numerator);
/*     */     }
/* 494 */     return new Fraction(this.denominator, this.numerator);
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
/*     */   public Fraction negate() {
/* 506 */     if (this.numerator == Integer.MIN_VALUE) {
/* 507 */       throw new ArithmeticException("overflow: too large to negate");
/*     */     }
/* 509 */     return new Fraction(-this.numerator, this.denominator);
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
/*     */   public Fraction abs() {
/* 522 */     if (this.numerator >= 0) {
/* 523 */       return this;
/*     */     }
/* 525 */     return negate();
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
/*     */   public Fraction pow(int power) {
/* 541 */     if (power == 1)
/* 542 */       return this; 
/* 543 */     if (power == 0)
/* 544 */       return ONE; 
/* 545 */     if (power < 0) {
/* 546 */       if (power == Integer.MIN_VALUE) {
/* 547 */         return invert().pow(2).pow(-(power / 2));
/*     */       }
/* 549 */       return invert().pow(-power);
/*     */     } 
/* 551 */     Fraction f = multiplyBy(this);
/* 552 */     if (power % 2 == 0) {
/* 553 */       return f.pow(power / 2);
/*     */     }
/* 555 */     return f.pow(power / 2).multiplyBy(this);
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
/*     */   private static int greatestCommonDivisor(int u, int v) {
/* 571 */     if (u == 0 || v == 0) {
/* 572 */       if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
/* 573 */         throw new ArithmeticException("overflow: gcd is 2^31");
/*     */       }
/* 575 */       return Math.abs(u) + Math.abs(v);
/*     */     } 
/*     */     
/* 578 */     if (Math.abs(u) == 1 || Math.abs(v) == 1) {
/* 579 */       return 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 585 */     if (u > 0) {
/* 586 */       u = -u;
/*     */     }
/* 588 */     if (v > 0) {
/* 589 */       v = -v;
/*     */     }
/*     */     
/* 592 */     int k = 0;
/* 593 */     while ((u & 0x1) == 0 && (v & 0x1) == 0 && k < 31) {
/* 594 */       u /= 2;
/* 595 */       v /= 2;
/* 596 */       k++;
/*     */     } 
/* 598 */     if (k == 31) {
/* 599 */       throw new ArithmeticException("overflow: gcd is 2^31");
/*     */     }
/*     */ 
/*     */     
/* 603 */     int t = ((u & 0x1) == 1) ? v : -(u / 2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 609 */       while ((t & 0x1) == 0) {
/* 610 */         t /= 2;
/*     */       }
/*     */       
/* 613 */       if (t > 0) {
/* 614 */         u = -t;
/*     */       } else {
/* 616 */         v = t;
/*     */       } 
/*     */       
/* 619 */       t = (v - u) / 2;
/*     */ 
/*     */       
/* 622 */       if (t == 0) {
/* 623 */         return -u * (1 << k);
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
/*     */   private static int mulAndCheck(int x, int y) {
/* 639 */     long m = x * y;
/* 640 */     if (m < -2147483648L || m > 2147483647L) {
/* 641 */       throw new ArithmeticException("overflow: mul");
/*     */     }
/* 643 */     return (int)m;
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
/*     */   private static int mulPosAndCheck(int x, int y) {
/* 657 */     long m = x * y;
/* 658 */     if (m > 2147483647L) {
/* 659 */       throw new ArithmeticException("overflow: mulPos");
/*     */     }
/* 661 */     return (int)m;
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
/*     */   private static int addAndCheck(int x, int y) {
/* 674 */     long s = x + y;
/* 675 */     if (s < -2147483648L || s > 2147483647L) {
/* 676 */       throw new ArithmeticException("overflow: add");
/*     */     }
/* 678 */     return (int)s;
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
/*     */   private static int subAndCheck(int x, int y) {
/* 691 */     long s = x - y;
/* 692 */     if (s < -2147483648L || s > 2147483647L) {
/* 693 */       throw new ArithmeticException("overflow: add");
/*     */     }
/* 695 */     return (int)s;
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
/*     */   public Fraction add(Fraction fraction) {
/* 709 */     return addSub(fraction, true);
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
/*     */   public Fraction subtract(Fraction fraction) {
/* 723 */     return addSub(fraction, false);
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
/*     */   private Fraction addSub(Fraction fraction, boolean isAdd) {
/* 737 */     if (fraction == null) {
/* 738 */       throw new IllegalArgumentException("The fraction must not be null");
/*     */     }
/*     */     
/* 741 */     if (this.numerator == 0) {
/* 742 */       return isAdd ? fraction : fraction.negate();
/*     */     }
/* 744 */     if (fraction.numerator == 0) {
/* 745 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 749 */     int d1 = greatestCommonDivisor(this.denominator, fraction.denominator);
/* 750 */     if (d1 == 1) {
/*     */       
/* 752 */       int i = mulAndCheck(this.numerator, fraction.denominator);
/* 753 */       int j = mulAndCheck(fraction.numerator, this.denominator);
/* 754 */       return new Fraction(isAdd ? addAndCheck(i, j) : subAndCheck(i, j), mulPosAndCheck(this.denominator, fraction.denominator));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 760 */     BigInteger uvp = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf((fraction.denominator / d1)));
/* 761 */     BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf((this.denominator / d1)));
/* 762 */     BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
/*     */ 
/*     */     
/* 765 */     int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
/* 766 */     int d2 = (tmodd1 == 0) ? d1 : greatestCommonDivisor(tmodd1, d1);
/*     */ 
/*     */     
/* 769 */     BigInteger w = t.divide(BigInteger.valueOf(d2));
/* 770 */     if (w.bitLength() > 31) {
/* 771 */       throw new ArithmeticException("overflow: numerator too large after multiply");
/*     */     }
/* 773 */     return new Fraction(w.intValue(), mulPosAndCheck(this.denominator / d1, fraction.denominator / d2));
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
/*     */   public Fraction multiplyBy(Fraction fraction) {
/* 787 */     if (fraction == null) {
/* 788 */       throw new IllegalArgumentException("The fraction must not be null");
/*     */     }
/* 790 */     if (this.numerator == 0 || fraction.numerator == 0) {
/* 791 */       return ZERO;
/*     */     }
/*     */ 
/*     */     
/* 795 */     int d1 = greatestCommonDivisor(this.numerator, fraction.denominator);
/* 796 */     int d2 = greatestCommonDivisor(fraction.numerator, this.denominator);
/* 797 */     return getReducedFraction(mulAndCheck(this.numerator / d1, fraction.numerator / d2), mulPosAndCheck(this.denominator / d2, fraction.denominator / d1));
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
/*     */   public Fraction divideBy(Fraction fraction) {
/* 812 */     if (fraction == null) {
/* 813 */       throw new IllegalArgumentException("The fraction must not be null");
/*     */     }
/* 815 */     if (fraction.numerator == 0) {
/* 816 */       throw new ArithmeticException("The fraction to divide by must not be zero");
/*     */     }
/* 818 */     return multiplyBy(fraction.invert());
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
/*     */   public boolean equals(Object obj) {
/* 834 */     if (obj == this) {
/* 835 */       return true;
/*     */     }
/* 837 */     if (!(obj instanceof Fraction)) {
/* 838 */       return false;
/*     */     }
/* 840 */     Fraction other = (Fraction)obj;
/* 841 */     return (getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 851 */     if (this.hashCode == 0)
/*     */     {
/* 853 */       this.hashCode = 37 * (629 + getNumerator()) + getDenominator();
/*     */     }
/* 855 */     return this.hashCode;
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
/*     */   public int compareTo(Fraction other) {
/* 872 */     if (this == other) {
/* 873 */       return 0;
/*     */     }
/* 875 */     if (this.numerator == other.numerator && this.denominator == other.denominator) {
/* 876 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 880 */     long first = this.numerator * other.denominator;
/* 881 */     long second = other.numerator * this.denominator;
/* 882 */     if (first == second)
/* 883 */       return 0; 
/* 884 */     if (first < second) {
/* 885 */       return -1;
/*     */     }
/* 887 */     return 1;
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
/*     */   public String toString() {
/* 900 */     if (this.toString == null) {
/* 901 */       this.toString = (new StringBuilder(32)).append(getNumerator()).append('/').append(getDenominator()).toString();
/*     */     }
/* 903 */     return this.toString;
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
/*     */   public String toProperString() {
/* 916 */     if (this.toProperString == null) {
/* 917 */       if (this.numerator == 0) {
/* 918 */         this.toProperString = "0";
/* 919 */       } else if (this.numerator == this.denominator) {
/* 920 */         this.toProperString = "1";
/* 921 */       } else if (this.numerator == -1 * this.denominator) {
/* 922 */         this.toProperString = "-1";
/* 923 */       } else if (((this.numerator > 0) ? -this.numerator : this.numerator) < -this.denominator) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 928 */         int properNumerator = getProperNumerator();
/* 929 */         if (properNumerator == 0) {
/* 930 */           this.toProperString = Integer.toString(getProperWhole());
/*     */         } else {
/* 932 */           this.toProperString = (new StringBuilder(32)).append(getProperWhole()).append(' ').append(properNumerator).append('/').append(getDenominator()).toString();
/*     */         } 
/*     */       } else {
/*     */         
/* 936 */         this.toProperString = (new StringBuilder(32)).append(getNumerator()).append('/').append(getDenominator()).toString();
/*     */       } 
/*     */     }
/*     */     
/* 940 */     return this.toProperString;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\math\Fraction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */