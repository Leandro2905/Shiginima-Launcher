/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AtomicDoubleArray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private transient AtomicLongArray longs;
/*     */   
/*     */   public AtomicDoubleArray(int length) {
/*  56 */     this.longs = new AtomicLongArray(length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDoubleArray(double[] array) {
/*  67 */     int len = array.length;
/*  68 */     long[] longArray = new long[len];
/*  69 */     for (int i = 0; i < len; i++) {
/*  70 */       longArray[i] = Double.doubleToRawLongBits(array[i]);
/*     */     }
/*  72 */     this.longs = new AtomicLongArray(longArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int length() {
/*  81 */     return this.longs.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get(int i) {
/*  91 */     return Double.longBitsToDouble(this.longs.get(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(int i, double newValue) {
/* 101 */     long next = Double.doubleToRawLongBits(newValue);
/* 102 */     this.longs.set(i, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(int i, double newValue) {
/* 112 */     set(i, newValue);
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
/*     */   public final double getAndSet(int i, double newValue) {
/* 127 */     long next = Double.doubleToRawLongBits(newValue);
/* 128 */     return Double.longBitsToDouble(this.longs.getAndSet(i, next));
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
/*     */   public final boolean compareAndSet(int i, double expect, double update) {
/* 144 */     return this.longs.compareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(int i, double expect, double update) {
/* 167 */     return this.longs.weakCompareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final double getAndAdd(int i, double delta) {
/*     */     while (true) {
/* 181 */       long current = this.longs.get(i);
/* 182 */       double currentVal = Double.longBitsToDouble(current);
/* 183 */       double nextVal = currentVal + delta;
/* 184 */       long next = Double.doubleToRawLongBits(nextVal);
/* 185 */       if (this.longs.compareAndSet(i, current, next)) {
/* 186 */         return currentVal;
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
/*     */   public double addAndGet(int i, double delta) {
/*     */     while (true) {
/* 200 */       long current = this.longs.get(i);
/* 201 */       double currentVal = Double.longBitsToDouble(current);
/* 202 */       double nextVal = currentVal + delta;
/* 203 */       long next = Double.doubleToRawLongBits(nextVal);
/* 204 */       if (this.longs.compareAndSet(i, current, next)) {
/* 205 */         return nextVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     int iMax = length() - 1;
/* 216 */     if (iMax == -1) {
/* 217 */       return "[]";
/*     */     }
/*     */ 
/*     */     
/* 221 */     StringBuilder b = new StringBuilder(19 * (iMax + 1));
/* 222 */     b.append('[');
/* 223 */     for (int i = 0;; i++) {
/* 224 */       b.append(Double.longBitsToDouble(this.longs.get(i)));
/* 225 */       if (i == iMax) {
/* 226 */         return b.append(']').toString();
/*     */       }
/* 228 */       b.append(',').append(' ');
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
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 240 */     s.defaultWriteObject();
/*     */ 
/*     */     
/* 243 */     int length = length();
/* 244 */     s.writeInt(length);
/*     */ 
/*     */     
/* 247 */     for (int i = 0; i < length; i++) {
/* 248 */       s.writeDouble(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 257 */     s.defaultReadObject();
/*     */ 
/*     */     
/* 260 */     int length = s.readInt();
/* 261 */     this.longs = new AtomicLongArray(length);
/*     */ 
/*     */     
/* 264 */     for (int i = 0; i < length; i++)
/* 265 */       set(i, s.readDouble()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\AtomicDoubleArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */