/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
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
/*     */ @Beta
/*     */ public abstract class AbstractInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/*  47 */   private static final Object[] NO_ARGS = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
/*  65 */     if (args == null) {
/*  66 */       args = NO_ARGS;
/*     */     }
/*  68 */     if (args.length == 0 && method.getName().equals("hashCode")) {
/*  69 */       return Integer.valueOf(hashCode());
/*     */     }
/*  71 */     if (args.length == 1 && method.getName().equals("equals") && method.getParameterTypes()[0] == Object.class) {
/*     */ 
/*     */       
/*  74 */       Object arg = args[0];
/*  75 */       if (arg == null) {
/*  76 */         return Boolean.valueOf(false);
/*     */       }
/*  78 */       if (proxy == arg) {
/*  79 */         return Boolean.valueOf(true);
/*     */       }
/*  81 */       return Boolean.valueOf((isProxyOfSameInterfaces(arg, proxy.getClass()) && equals(Proxy.getInvocationHandler(arg))));
/*     */     } 
/*     */     
/*  84 */     if (args.length == 0 && method.getName().equals("toString")) {
/*  85 */       return toString();
/*     */     }
/*  87 */     return handleInvocation(proxy, method, args);
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
/*     */   protected abstract Object handleInvocation(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 110 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return super.toString();
/*     */   }
/*     */   
/*     */   private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
/* 131 */     return (proxyClass.isInstance(arg) || (Proxy.isProxyClass(arg.getClass()) && Arrays.equals((Object[])arg.getClass().getInterfaces(), (Object[])proxyClass.getInterfaces())));
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\common\reflect\AbstractInvocationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */