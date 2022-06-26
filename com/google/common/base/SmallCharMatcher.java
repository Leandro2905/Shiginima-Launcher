/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible("no precomputation is done in GWT")
/*     */ final class SmallCharMatcher
/*     */   extends CharMatcher.FastMatcher
/*     */ {
/*     */   static final int MAX_SIZE = 1023;
/*     */   private final char[] table;
/*     */   private final boolean containsZero;
/*     */   private final long filter;
/*     */   private static final int C1 = -862048943;
/*     */   private static final int C2 = 461845907;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.5D;
/*     */   
/*     */   private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
/*  40 */     super(description);
/*  41 */     this.table = table;
/*  42 */     this.filter = filter;
/*  43 */     this.containsZero = containsZero;
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
/*     */   static int smear(int hashCode) {
/*  58 */     return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
/*     */   }
/*     */   
/*     */   private boolean checkFilter(int c) {
/*  62 */     return (1L == (0x1L & this.filter >> c));
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
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/*  78 */     if (setSize == 1) {
/*  79 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*  83 */     int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/*  84 */     while (tableSize * 0.5D < setSize) {
/*  85 */       tableSize <<= 1;
/*     */     }
/*  87 */     return tableSize;
/*     */   }
/*     */ 
/*     */   
/*     */   static CharMatcher from(BitSet chars, String description) {
/*  92 */     long filter = 0L;
/*  93 */     int size = chars.cardinality();
/*  94 */     boolean containsZero = chars.get(0);
/*     */     
/*  96 */     char[] table = new char[chooseTableSize(size)];
/*  97 */     int mask = table.length - 1; int c;
/*  98 */     for (c = chars.nextSetBit(0); c != -1; ) {
/*     */       
/* 100 */       filter |= 1L << c;
/* 101 */       int index = smear(c) & mask;
/*     */       
/*     */       for (;; c = chars.nextSetBit(c + 1)) {
/* 104 */         if (table[index] == '\000') {
/* 105 */           table[index] = (char)c;
/*     */         }
/*     */         else {
/*     */           
/* 109 */           index = index + 1 & mask; continue;
/*     */         } 
/*     */       } 
/* 112 */     }  return new SmallCharMatcher(table, filter, containsZero, description);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(char c) {
/* 117 */     if (c == '\000') {
/* 118 */       return this.containsZero;
/*     */     }
/* 120 */     if (!checkFilter(c)) {
/* 121 */       return false;
/*     */     }
/* 123 */     int mask = this.table.length - 1;
/* 124 */     int startingIndex = smear(c) & mask;
/* 125 */     int index = startingIndex;
/*     */     
/*     */     while (true) {
/* 128 */       if (this.table[index] == '\000') {
/* 129 */         return false;
/*     */       }
/* 131 */       if (this.table[index] == c) {
/* 132 */         return true;
/*     */       }
/*     */       
/* 135 */       index = index + 1 & mask;
/*     */ 
/*     */       
/* 138 */       if (index == startingIndex)
/* 139 */         return false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void setBits(BitSet table) {
/* 144 */     if (this.containsZero) {
/* 145 */       table.set(0);
/*     */     }
/* 147 */     for (char c : this.table) {
/* 148 */       if (c != '\000')
/* 149 */         table.set(c); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\SmallCharMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */