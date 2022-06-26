/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ @GwtCompatible
/*     */ public final class MoreObjects
/*     */ {
/*     */   public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
/*  52 */     return (first != null) ? first : Preconditions.<T>checkNotNull(second);
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
/*     */   public static ToStringHelper toStringHelper(Object self) {
/*  95 */     return new ToStringHelper(simpleName(self.getClass()));
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
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz) {
/* 109 */     return new ToStringHelper(simpleName(clazz));
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
/*     */   public static ToStringHelper toStringHelper(String className) {
/* 121 */     return new ToStringHelper(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String simpleName(Class<?> clazz) {
/* 130 */     String name = clazz.getName();
/*     */ 
/*     */ 
/*     */     
/* 134 */     name = name.replaceAll("\\$[0-9]+", "\\$");
/*     */ 
/*     */     
/* 137 */     int start = name.lastIndexOf('$');
/*     */ 
/*     */ 
/*     */     
/* 141 */     if (start == -1) {
/* 142 */       start = name.lastIndexOf('.');
/*     */     }
/* 144 */     return name.substring(start + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/* 155 */     private ValueHolder holderHead = new ValueHolder();
/* 156 */     private ValueHolder holderTail = this.holderHead;
/*     */ 
/*     */     
/*     */     private boolean omitNullValues = false;
/*     */ 
/*     */     
/*     */     private ToStringHelper(String className) {
/* 163 */       this.className = Preconditions.<String>checkNotNull(className);
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
/* 174 */       this.omitNullValues = true;
/* 175 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, @Nullable Object value) {
/* 185 */       return addHolder(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, boolean value) {
/* 195 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, char value) {
/* 205 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, double value) {
/* 215 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, float value) {
/* 225 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, int value) {
/* 235 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper add(String name, long value) {
/* 245 */       return addHolder(name, String.valueOf(value));
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
/*     */     public ToStringHelper addValue(@Nullable Object value) {
/* 257 */       return addHolder(value);
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
/*     */     public ToStringHelper addValue(boolean value) {
/* 271 */       return addHolder(String.valueOf(value));
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
/* 283 */       return addHolder(String.valueOf(value));
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
/* 295 */       return addHolder(String.valueOf(value));
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
/* 307 */       return addHolder(String.valueOf(value));
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
/* 319 */       return addHolder(String.valueOf(value));
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
/* 331 */       return addHolder(String.valueOf(value));
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
/* 346 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 347 */       String nextSeparator = "";
/* 348 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/*     */       
/* 350 */       for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; 
/* 351 */         valueHolder = valueHolder.next) {
/* 352 */         if (!omitNullValuesSnapshot || valueHolder.value != null) {
/* 353 */           builder.append(nextSeparator);
/* 354 */           nextSeparator = ", ";
/*     */           
/* 356 */           if (valueHolder.name != null) {
/* 357 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 359 */           builder.append(valueHolder.value);
/*     */         } 
/*     */       } 
/* 362 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 366 */       ValueHolder valueHolder = new ValueHolder();
/* 367 */       this.holderTail = this.holderTail.next = valueHolder;
/* 368 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(@Nullable Object value) {
/* 372 */       ValueHolder valueHolder = addHolder();
/* 373 */       valueHolder.value = value;
/* 374 */       return this;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(String name, @Nullable Object value) {
/* 378 */       ValueHolder valueHolder = addHolder();
/* 379 */       valueHolder.value = value;
/* 380 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 381 */       return this;
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


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\base\MoreObjects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */