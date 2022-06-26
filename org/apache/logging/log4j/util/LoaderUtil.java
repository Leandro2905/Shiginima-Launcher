/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
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
/*    */ 
/*    */ public final class LoaderUtil
/*    */ {
/* 29 */   private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new ThreadContextClassLoaderGetter();
/*    */   
/*    */   static {
/* 32 */     SecurityManager sm = System.getSecurityManager();
/* 33 */     if (sm != null) {
/* 34 */       sm.checkPermission(new RuntimePermission("getClassLoader"));
/*    */     }
/*    */   }
/*    */   
/*    */   public static ClassLoader getThreadContextClassLoader() {
/* 39 */     return (System.getSecurityManager() == null) ? TCCL_GETTER.run() : AccessController.<ClassLoader>doPrivileged(TCCL_GETTER);
/*    */   }
/*    */   
/*    */   private static class ThreadContextClassLoaderGetter
/*    */     implements PrivilegedAction<ClassLoader> {
/*    */     private ThreadContextClassLoaderGetter() {}
/*    */     
/*    */     public ClassLoader run() {
/* 47 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*    */       
/* 49 */       return (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4\\util\LoaderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */