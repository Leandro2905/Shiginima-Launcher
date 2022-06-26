/*    */ package org.apache.logging.log4j.core.config.plugins.osgi;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*    */ import org.apache.logging.log4j.core.util.BundleResourceLoader;
/*    */ import org.apache.logging.log4j.core.util.ResourceLoader;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.osgi.framework.BundleActivator;
/*    */ import org.osgi.framework.BundleContext;
/*    */ import org.osgi.framework.BundleEvent;
/*    */ import org.osgi.framework.BundleListener;
/*    */ import org.osgi.framework.SynchronousBundleListener;
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
/*    */ public final class Activator
/*    */   implements BundleActivator, SynchronousBundleListener
/*    */ {
/* 37 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/* 39 */   private final AtomicReference<BundleContext> context = new AtomicReference<BundleContext>();
/*    */ 
/*    */   
/*    */   public void start(BundleContext context) throws Exception {
/* 43 */     if (this.context.compareAndSet(null, context)) {
/* 44 */       context.addBundleListener((BundleListener)this);
/*    */       
/* 46 */       scanInstalledBundlesForPlugins(context);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void scanInstalledBundlesForPlugins(BundleContext context) {
/* 51 */     Bundle[] bundles = context.getBundles();
/* 52 */     for (Bundle bundle : bundles) {
/* 53 */       if (bundle.getState() == 32)
/*    */       {
/* 55 */         scanBundleForPlugins(bundle);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void scanBundleForPlugins(Bundle bundle) {
/* 61 */     LOGGER.trace("Scanning bundle [{}] for plugins.", new Object[] { bundle.getSymbolicName() });
/* 62 */     PluginManager.loadPlugins((ResourceLoader)new BundleResourceLoader(bundle));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop(BundleContext context) throws Exception {
/* 68 */     this.context.compareAndSet(context, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void bundleChanged(BundleEvent event) {
/* 73 */     switch (event.getType()) {
/*    */       case 2:
/* 75 */         scanBundleForPlugins(event.getBundle());
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\osgi\Activator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */