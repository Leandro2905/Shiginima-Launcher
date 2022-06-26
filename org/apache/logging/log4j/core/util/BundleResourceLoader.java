/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Enumeration;
/*    */ import org.osgi.framework.Bundle;
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
/*    */ public final class BundleResourceLoader
/*    */   implements ResourceLoader
/*    */ {
/*    */   private final Bundle bundle;
/*    */   
/*    */   public BundleResourceLoader(Bundle bundle) {
/* 34 */     this.bundle = bundle;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/* 39 */     return this.bundle.loadClass(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public URL getResource(String name) {
/* 44 */     return this.bundle.getResource(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Enumeration<URL> getResources(String name) throws IOException {
/* 49 */     Enumeration<URL> enumeration = this.bundle.getResources(name);
/* 50 */     return enumeration;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return getClass().getCanonicalName() + ": " + this.bundle.getSymbolicName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\BundleResourceLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */