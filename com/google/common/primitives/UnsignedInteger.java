/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class UnsignedInteger
/*     */   extends Number
/*     */   implements Comparable<UnsignedInteger>
/*     */ {
/*  46 */   public static final UnsignedInteger ZERO = fromIntBits(0);
/*  47 */   public static final UnsignedInteger ONE = fromIntBits(1);
/*  48 */   public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
/*     */   
/*     */   private final int value;
/*     */ 
/*     */   
/*     */   private UnsignedInteger(int value) {
/*  54 */     this.value = value & 0xFFFFFFFF;
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
/*     */   public static UnsignedInteger fromIntBits(int bits) {
/*  70 */     return new UnsignedInteger(bits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(long value) {
/*  78 */     Preconditions.checkArgument(((value & 0xFFFFFFFFL) == value), "value (%s) is outside the range for an unsigned integer value", new Object[] { Long.valueOf(value) });
/*     */     
/*  80 */     return fromIntBits((int)value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(BigInteger value) {
/*  90 */     Preconditions.checkNotNull(value);
/*  91 */     Preconditions.checkArgument((value.signum() >= 0 && value.bitLength() <= 32), "value (%s) is outside the range for an unsigned integer value", new Object[] { value });
/*     */     
/*  93 */     return fromIntBits(value.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(String string) {
/* 104 */     return valueOf(string, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(String string, int radix) {
/* 115 */     return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public UnsignedInteger plus(UnsignedInteger val) {
/* 126 */     return fromIntBits(this.value + ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public UnsignedInteger minus(UnsignedInteger val) {
/* 137 */     return fromIntBits(this.value - ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @GwtIncompatible("Does not truncate correctly")
/*     */   public UnsignedInteger times(UnsignedInteger val) {
/* 150 */     return fromIntBits(this.value * ((UnsignedInteger)Preconditions.checkNotNull(val)).value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public UnsignedInteger dividedBy(UnsignedInteger val) {
/* 161 */     return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public UnsignedInteger mod(UnsignedInteger val) {
/* 172 */     return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value));
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
/*     */   public int intValue() {
/* 184 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 192 */     return UnsignedInts.toLong(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 201 */     return (float)longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 210 */     return longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/* 217 */     return BigInteger.valueOf(longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedInteger other) {
/* 227 */     Preconditions.checkNotNull(other);
/* 228 */     return UnsignedInts.compare(this.value, other.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 238 */     if (obj instanceof UnsignedInteger) {
/* 239 */       UnsignedInteger other = (UnsignedInteger)obj;
/* 240 */       return (this.value == other.value);
/*     */     } 
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 250 */     return toString(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString(int radix) {
/* 259 */     return UnsignedInts.toString(this.value, radix);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\UnsignedInteger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */