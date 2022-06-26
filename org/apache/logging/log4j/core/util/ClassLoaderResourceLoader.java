/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Enumeration;
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
/*    */ public final class ClassLoaderResourceLoader
/*    */   implements ResourceLoader
/*    */ {
/*    */   private final ClassLoader loader;
/*    */   
/*    */   public ClassLoaderResourceLoader(ClassLoader loader) {
/* 32 */     this.loader = (loader != null) ? loader : getClass().getClassLoader();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/* 37 */     return this.loader.loadClass(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public URL getResource(String name) {
/* 42 */     return this.loader.getResource(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Enumeration<URL> getResources(String name) throws IOException {
/* 47 */     return this.loader.getResources(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return getClass().getCanonicalName() + '(' + this.loader.toString() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\ClassLoaderResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */