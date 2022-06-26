/*     */ package org.apache.commons.lang3.math;
/*     */ 
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IEEE754rUtils
/*     */ {
/*     */   public static double min(double... array) {
/*  42 */     if (array == null) {
/*  43 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/*  45 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */ 
/*     */     
/*  49 */     double min = array[0];
/*  50 */     for (int i = 1; i < array.length; i++) {
/*  51 */       min = min(array[i], min);
/*     */     }
/*     */     
/*  54 */     return min;
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
/*     */   public static float min(float... array) {
/*  68 */     if (array == null) {
/*  69 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/*  71 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/*  74 */     float min = array[0];
/*  75 */     for (int i = 1; i < array.length; i++) {
/*  76 */       min = min(array[i], min);
/*     */     }
/*     */     
/*  79 */     return min;
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
/*     */   public static double min(double a, double b, double c) {
/*  93 */     return min(min(a, b), c);
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
/*     */   public static double min(double a, double b) {
/* 106 */     if (Double.isNaN(a)) {
/* 107 */       return b;
/*     */     }
/* 109 */     if (Double.isNaN(b)) {
/* 110 */       return a;
/*     */     }
/* 112 */     return Math.min(a, b);
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
/*     */   public static float min(float a, float b, float c) {
/* 127 */     return min(min(a, b), c);
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
/*     */   public static float min(float a, float b) {
/* 140 */     if (Float.isNaN(a)) {
/* 141 */       return b;
/*     */     }
/* 143 */     if (Float.isNaN(b)) {
/* 144 */       return a;
/*     */     }
/* 146 */     return Math.min(a, b);
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
/*     */   public static double max(double... array) {
/* 161 */     if (array == null) {
/* 162 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/* 164 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/* 167 */     double max = array[0];
/* 168 */     for (int j = 1; j < array.length; j++) {
/* 169 */       max = max(array[j], max);
/*     */     }
/*     */     
/* 172 */     return max;
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
/*     */   public static float max(float... array) {
/* 186 */     if (array == null) {
/* 187 */       throw new IllegalArgumentException("The Array must not be null");
/*     */     }
/* 189 */     Validate.isTrue((array.length != 0), "Array cannot be empty.", new Object[0]);
/*     */ 
/*     */     
/* 192 */     float max = array[0];
/* 193 */     for (int j = 1; j < array.length; j++) {
/* 194 */       max = max(array[j], max);
/*     */     }
/*     */     
/* 197 */     return max;
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
/*     */   public static double max(double a, double b, double c) {
/* 211 */     return max(max(a, b), c);
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
/*     */   public static double max(double a, double b) {
/* 224 */     if (Double.isNaN(a)) {
/* 225 */       return b;
/*     */     }
/* 227 */     if (Double.isNaN(b)) {
/* 228 */       return a;
/*     */     }
/* 230 */     return Math.max(a, b);
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
/*     */   public static float max(float a, float b, float c) {
/* 245 */     return max(max(a, b), c);
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
/*     */   public static float max(float a, float b) {
/* 258 */     if (Float.isNaN(a)) {
/* 259 */       return b;
/*     */     }
/* 261 */     if (Float.isNaN(b)) {
/* 262 */       return a;
/*     */     }
/* 264 */     return Math.max(a, b);
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\commons\lang3\math\IEEE754rUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */