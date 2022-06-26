/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class SignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = 64;
/*     */   
/*     */   public static byte checkedCast(long value) {
/*  61 */     byte result = (byte)(int)value;
/*  62 */     if (result != value) {
/*     */       
/*  64 */       long l = value; throw new IllegalArgumentException((new StringBuilder(34)).append("Out of range: ").append(l).toString());
/*     */     } 
/*  66 */     return result;
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
/*     */   public static byte saturatedCast(long value) {
/*  78 */     if (value > 127L) {
/*  79 */       return Byte.MAX_VALUE;
/*     */     }
/*  81 */     if (value < -128L) {
/*  82 */       return Byte.MIN_VALUE;
/*     */     }
/*  84 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/* 102 */     return a - b;
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
/*     */   public static byte min(byte... array) {
/* 114 */     Preconditions.checkArgument((array.length > 0));
/* 115 */     byte min = array[0];
/* 116 */     for (int i = 1; i < array.length; i++) {
/* 117 */       if (array[i] < min) {
/* 118 */         min = array[i];
/*     */       }
/*     */     } 
/* 121 */     return min;
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
/*     */   public static byte max(byte... array) {
/* 133 */     Preconditions.checkArgument((array.length > 0));
/* 134 */     byte max = array[0];
/* 135 */     for (int i = 1; i < array.length; i++) {
/* 136 */       if (array[i] > max) {
/* 137 */         max = array[i];
/*     */       }
/*     */     } 
/* 140 */     return max;
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
/*     */   public static String join(String separator, byte... array) {
/* 153 */     Preconditions.checkNotNull(separator);
/* 154 */     if (array.length == 0) {
/* 155 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 159 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 160 */     builder.append(array[0]);
/* 161 */     for (int i = 1; i < array.length; i++) {
/* 162 */       builder.append(separator).append(array[i]);
/*     */     }
/* 164 */     return builder.toString();
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
/*     */   public static Comparator<byte[]> lexicographicalComparator() {
/* 184 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<byte[]> {
/* 188 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(byte[] left, byte[] right) {
/* 192 */       int minLength = Math.min(left.length, right.length);
/* 193 */       for (int i = 0; i < minLength; i++) {
/* 194 */         int result = SignedBytes.compare(left[i], right[i]);
/* 195 */         if (result != 0) {
/* 196 */           return result;
/*     */         }
/*     */       } 
/* 199 */       return left.length - right.length;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\primitives\SignedBytes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */