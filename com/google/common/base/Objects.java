/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Objects
/*     */ {
/*     */   @CheckReturnValue
/*     */   public static boolean equal(@Nullable Object a, @Nullable Object b) {
/*  60 */     return (a == b || (a != null && a.equals(b)));
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
/*     */   public static int hashCode(@Nullable Object... objects) {
/*  84 */     return Arrays.hashCode(objects);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static ToStringHelper toStringHelper(Object self) {
/* 130 */     return new ToStringHelper(MoreObjects.simpleName(self.getClass()));
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
/*     */   @Deprecated
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz) {
/* 147 */     return new ToStringHelper(MoreObjects.simpleName(clazz));
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
/*     */   @Deprecated
/*     */   public static ToStringHelper toStringHelper(String className) {
/* 162 */     return new ToStringHelper(className);
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
/*     */   @Deprecated
/*     */   public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
/* 186 */     return MoreObjects.firstNonNull(first, second);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */ 
/*     */     
/* 200 */     private ValueHolder holderHead = new ValueHolder();
/* 201 */     private ValueHolder holderTail = this.holderHead;
/*     */ 
/*     */     
/*     */     private boolean omitNullValues = false;
/*     */ 
/*     */     
/*     */     private ToStringHelper(String className) {
/* 208 */       this.className = Preconditions.<String>checkNotNull(className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper omitNullValues() {
/* 219 */       this.omitNullValues = true;
/* 220 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, @Nullable Object value) {
/* 230 */       return addHolder(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, boolean value) {
/* 240 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, char value) {
/* 250 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, double value) {
/* 260 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, float value) {
/* 270 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, int value) {
/* 280 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, long value) {
/* 290 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(@Nullable Object value) {
/* 300 */       return addHolder(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(boolean value) {
/* 312 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(char value) {
/* 324 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(double value) {
/* 336 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(float value) {
/* 348 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(int value) {
/* 360 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper addValue(long value) {
/* 372 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 387 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 388 */       String nextSeparator = "";
/* 389 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/*     */       
/* 391 */       for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; 
/* 392 */         valueHolder = valueHolder.next) {
/* 393 */         if (!omitNullValuesSnapshot || valueHolder.value != null) {
/* 394 */           builder.append(nextSeparator);
/* 395 */           nextSeparator = ", ";
/*     */           
/* 397 */           if (valueHolder.name != null) {
/* 398 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 400 */           builder.append(valueHolder.value);
/*     */         } 
/*     */       } 
/* 403 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 407 */       ValueHolder valueHolder = new ValueHolder();
/* 408 */       this.holderTail = this.holderTail.next = valueHolder;
/* 409 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(@Nullable Object value) {
/* 413 */       ValueHolder valueHolder = addHolder();
/* 414 */       valueHolder.value = value;
/* 415 */       return this;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(String name, @Nullable Object value) {
/* 419 */       ValueHolder valueHolder = addHolder();
/* 420 */       valueHolder.value = value;
/* 421 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 422 */       return this;
/*     */     }
/*     */     
/*     */     private static final class ValueHolder {
/*     */       String name;
/*     */       Object value;
/*     */       ValueHolder next;
/*     */       
/*     */       private ValueHolder() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\Objects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */