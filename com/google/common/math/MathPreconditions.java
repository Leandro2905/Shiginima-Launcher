/*    */ package com.google.common.math;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.math.BigInteger;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class MathPreconditions
/*    */ {
/*    */   static int checkPositive(@Nullable String role, int x) {
/* 31 */     if (x <= 0) {
/* 32 */       String str = String.valueOf(String.valueOf(role)); int i = x; throw new IllegalArgumentException((new StringBuilder(26 + str.length())).append(str).append(" (").append(i).append(") must be > 0").toString());
/*    */     } 
/* 34 */     return x;
/*    */   }
/*    */   
/*    */   static long checkPositive(@Nullable String role, long x) {
/* 38 */     if (x <= 0L) {
/* 39 */       String str = String.valueOf(String.valueOf(role)); long l = x; throw new IllegalArgumentException((new StringBuilder(35 + str.length())).append(str).append(" (").append(l).append(") must be > 0").toString());
/*    */     } 
/* 41 */     return x;
/*    */   }
/*    */   
/*    */   static BigInteger checkPositive(@Nullable String role, BigInteger x) {
/* 45 */     if (x.signum() <= 0) {
/* 46 */       String str1 = String.valueOf(String.valueOf(role)), str2 = String.valueOf(String.valueOf(x)); throw new IllegalArgumentException((new StringBuilder(15 + str1.length() + str2.length())).append(str1).append(" (").append(str2).append(") must be > 0").toString());
/*    */     } 
/* 48 */     return x;
/*    */   }
/*    */   
/*    */   static int checkNonNegative(@Nullable String role, int x) {
/* 52 */     if (x < 0) {
/* 53 */       String str = String.valueOf(String.valueOf(role)); int i = x; throw new IllegalArgumentException((new StringBuilder(27 + str.length())).append(str).append(" (").append(i).append(") must be >= 0").toString());
/*    */     } 
/* 55 */     return x;
/*    */   }
/*    */   
/*    */   static long checkNonNegative(@Nullable String role, long x) {
/* 59 */     if (x < 0L) {
/* 60 */       String str = String.valueOf(String.valueOf(role)); long l = x; throw new IllegalArgumentException((new StringBuilder(36 + str.length())).append(str).append(" (").append(l).append(") must be >= 0").toString());
/*    */     } 
/* 62 */     return x;
/*    */   }
/*    */   
/*    */   static BigInteger checkNonNegative(@Nullable String role, BigInteger x) {
/* 66 */     if (x.signum() < 0) {
/* 67 */       String str1 = String.valueOf(String.valueOf(role)), str2 = String.valueOf(String.valueOf(x)); throw new IllegalArgumentException((new StringBuilder(16 + str1.length() + str2.length())).append(str1).append(" (").append(str2).append(") must be >= 0").toString());
/*    */     } 
/* 69 */     return x;
/*    */   }
/*    */   
/*    */   static double checkNonNegative(@Nullable String role, double x) {
/* 73 */     if (x < 0.0D) {
/* 74 */       String str = String.valueOf(String.valueOf(role)); double d = x; throw new IllegalArgumentException((new StringBuilder(40 + str.length())).append(str).append(" (").append(d).append(") must be >= 0").toString());
/*    */     } 
/* 76 */     return x;
/*    */   }
/*    */   
/*    */   static void checkRoundingUnnecessary(boolean condition) {
/* 80 */     if (!condition) {
/* 81 */       throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
/*    */     }
/*    */   }
/*    */   
/*    */   static void checkInRange(boolean condition) {
/* 86 */     if (!condition) {
/* 87 */       throw new ArithmeticException("not in range");
/*    */     }
/*    */   }
/*    */   
/*    */   static void checkNoOverflow(boolean condition) {
/* 92 */     if (!condition)
/* 93 */       throw new ArithmeticException("overflow"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\math\MathPreconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */