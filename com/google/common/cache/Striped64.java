/*     */ package com.google.common.cache;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Random;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Striped64
/*     */   extends Number
/*     */ {
/*     */   static final class Cell
/*     */   {
/*     */     volatile long p0;
/*     */     volatile long p1;
/*     */     volatile long p2;
/*     */     volatile long p3;
/*     */     volatile long p4;
/*     */     volatile long p5;
/*     */     volatile long p6;
/*     */     volatile long value;
/*     */     volatile long q0;
/*     */     volatile long q1;
/*     */     volatile long q2;
/*     */     volatile long q3;
/*     */     volatile long q4;
/*     */     volatile long q5;
/*     */     volatile long q6;
/*     */     private static final Unsafe UNSAFE;
/*     */     private static final long valueOffset;
/*     */     
/*     */     Cell(long x) {
/*  97 */       this.value = x;
/*     */     }
/*     */     final boolean cas(long cmp, long val) {
/* 100 */       return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */       try {
/* 108 */         UNSAFE = Striped64.getUnsafe();
/* 109 */         Class<?> ak = Cell.class;
/* 110 */         valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
/*     */       }
/* 112 */       catch (Exception e) {
/* 113 */         throw new Error(e);
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
/* 125 */   static final ThreadLocal<int[]> threadHashCode = (ThreadLocal)new ThreadLocal<int>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   static final Random rng = new Random();
/*     */ 
/*     */   
/* 133 */   static final int NCPU = Runtime.getRuntime().availableProcessors();
/*     */ 
/*     */ 
/*     */   
/*     */   volatile transient Cell[] cells;
/*     */ 
/*     */ 
/*     */   
/*     */   volatile transient long base;
/*     */ 
/*     */ 
/*     */   
/*     */   volatile transient int busy;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Unsafe UNSAFE;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long baseOffset;
/*     */ 
/*     */   
/*     */   private static final long busyOffset;
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean casBase(long cmp, long val) {
/* 161 */     return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean casBusy() {
/* 168 */     return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
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
/*     */   final void retryUpdate(long x, int[] hc, boolean wasUncontended) {
/*     */     int h;
/* 195 */     if (hc == null) {
/* 196 */       threadHashCode.set(hc = new int[1]);
/* 197 */       int r = rng.nextInt();
/* 198 */       h = hc[0] = (r == 0) ? 1 : r;
/*     */     } else {
/*     */       
/* 201 */       h = hc[0];
/* 202 */     }  boolean collide = false; while (true) {
/*     */       Cell[] as;
/*     */       int n;
/* 205 */       if ((as = this.cells) != null && (n = as.length) > 0) {
/* 206 */         Cell a; if ((a = as[n - 1 & h]) == null)
/* 207 */         { if (this.busy == 0) {
/* 208 */             Cell r = new Cell(x);
/* 209 */             if (this.busy == 0 && casBusy()) {
/* 210 */               boolean created = false; try {
/*     */                 Cell[] rs; int m;
/*     */                 int j;
/* 213 */                 if ((rs = this.cells) != null && (m = rs.length) > 0 && rs[j = m - 1 & h] == null) {
/*     */ 
/*     */                   
/* 216 */                   rs[j] = r;
/* 217 */                   created = true;
/*     */                 } 
/*     */               } finally {
/* 220 */                 this.busy = 0;
/*     */               } 
/* 222 */               if (created)
/*     */                 break; 
/*     */               continue;
/*     */             } 
/*     */           } 
/* 227 */           collide = false; }
/*     */         
/* 229 */         else if (!wasUncontended)
/* 230 */         { wasUncontended = true; }
/* 231 */         else { long l; if (a.cas(l = a.value, fn(l, x)))
/*     */             break; 
/* 233 */           if (n >= NCPU || this.cells != as) {
/* 234 */             collide = false;
/* 235 */           } else if (!collide) {
/* 236 */             collide = true;
/* 237 */           } else if (this.busy == 0 && casBusy()) {
/*     */             try {
/* 239 */               if (this.cells == as) {
/* 240 */                 Cell[] rs = new Cell[n << 1];
/* 241 */                 for (int i = 0; i < n; i++)
/* 242 */                   rs[i] = as[i]; 
/* 243 */                 this.cells = rs;
/*     */               } 
/*     */             } finally {
/* 246 */               this.busy = 0;
/*     */             } 
/* 248 */             collide = false; continue;
/*     */           }  }
/*     */         
/* 251 */         h ^= h << 13;
/* 252 */         h ^= h >>> 17;
/* 253 */         h ^= h << 5;
/* 254 */         hc[0] = h; continue;
/*     */       } 
/* 256 */       if (this.busy == 0 && this.cells == as && casBusy()) {
/* 257 */         boolean init = false;
/*     */         try {
/* 259 */           if (this.cells == as) {
/* 260 */             Cell[] rs = new Cell[2];
/* 261 */             rs[h & 0x1] = new Cell(x);
/* 262 */             this.cells = rs;
/* 263 */             init = true;
/*     */           } 
/*     */         } finally {
/* 266 */           this.busy = 0;
/*     */         } 
/* 268 */         if (init)
/*     */           break;  continue;
/*     */       }  long v;
/* 271 */       if (casBase(v = this.base, fn(v, x))) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void internalReset(long initialValue) {
/* 280 */     Cell[] as = this.cells;
/* 281 */     this.base = initialValue;
/* 282 */     if (as != null) {
/* 283 */       int n = as.length;
/* 284 */       for (int i = 0; i < n; i++) {
/* 285 */         Cell a = as[i];
/* 286 */         if (a != null) {
/* 287 */           a.value = initialValue;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 298 */       UNSAFE = getUnsafe();
/* 299 */       Class<?> sk = Striped64.class;
/* 300 */       baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
/*     */       
/* 302 */       busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
/*     */     }
/* 304 */     catch (Exception e) {
/* 305 */       throw new Error(e);
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
/*     */   private static Unsafe getUnsafe() {
/*     */     try {
/* 318 */       return Unsafe.getUnsafe();
/* 319 */     } catch (SecurityException tryReflectionInstead) {
/*     */       try {
/* 321 */         return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */             {
/*     */               public Unsafe run() throws Exception {
/* 324 */                 Class<Unsafe> k = Unsafe.class;
/* 325 */                 for (Field f : k.getDeclaredFields()) {
/* 326 */                   f.setAccessible(true);
/* 327 */                   Object x = f.get((Object)null);
/* 328 */                   if (k.isInstance(x))
/* 329 */                     return k.cast(x); 
/*     */                 } 
/* 331 */                 throw new NoSuchFieldError("the Unsafe"); }
/*     */             });
/* 333 */       } catch (PrivilegedActionException e) {
/* 334 */         throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract long fn(long paramLong1, long paramLong2);
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\cache\Striped64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */