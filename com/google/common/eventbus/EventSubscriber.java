/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ class EventSubscriber
/*     */ {
/*     */   private final Object target;
/*     */   private final Method method;
/*     */   
/*     */   EventSubscriber(Object target, Method method) {
/*  54 */     Preconditions.checkNotNull(target, "EventSubscriber target cannot be null.");
/*     */     
/*  56 */     Preconditions.checkNotNull(method, "EventSubscriber method cannot be null.");
/*     */     
/*  58 */     this.target = target;
/*  59 */     this.method = method;
/*  60 */     method.setAccessible(true);
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
/*     */   public void handleEvent(Object event) throws InvocationTargetException {
/*  72 */     Preconditions.checkNotNull(event);
/*     */     try {
/*  74 */       this.method.invoke(this.target, new Object[] { event });
/*  75 */     } catch (IllegalArgumentException e) {
/*  76 */       String str = String.valueOf(String.valueOf(event)); throw new Error((new StringBuilder(33 + str.length())).append("Method rejected target/argument: ").append(str).toString(), e);
/*  77 */     } catch (IllegalAccessException e) {
/*  78 */       String str = String.valueOf(String.valueOf(event)); throw new Error((new StringBuilder(28 + str.length())).append("Method became inaccessible: ").append(str).toString(), e);
/*  79 */     } catch (InvocationTargetException e) {
/*  80 */       if (e.getCause() instanceof Error) {
/*  81 */         throw (Error)e.getCause();
/*     */       }
/*  83 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/*  88 */     String str = String.valueOf(String.valueOf(this.method)); return (new StringBuilder(10 + str.length())).append("[wrapper ").append(str).append("]").toString();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  92 */     int PRIME = 31;
/*  93 */     return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/*  98 */     if (obj instanceof EventSubscriber) {
/*  99 */       EventSubscriber that = (EventSubscriber)obj;
/*     */ 
/*     */ 
/*     */       
/* 103 */       return (this.target == that.target && this.method.equals(that.method));
/*     */     } 
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public Object getSubscriber() {
/* 109 */     return this.target;
/*     */   }
/*     */   
/*     */   public Method getMethod() {
/* 113 */     return this.method;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\eventbus\EventSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */