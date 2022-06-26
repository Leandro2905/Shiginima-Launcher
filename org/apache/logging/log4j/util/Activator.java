/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.security.Permission;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.osgi.framework.AdaptPermission;
/*     */ import org.osgi.framework.AdminPermission;
/*     */ import org.osgi.framework.Bundle;
/*     */ import org.osgi.framework.BundleActivator;
/*     */ import org.osgi.framework.BundleContext;
/*     */ import org.osgi.framework.BundleEvent;
/*     */ import org.osgi.framework.BundleListener;
/*     */ import org.osgi.framework.SynchronousBundleListener;
/*     */ import org.osgi.framework.wiring.BundleWire;
/*     */ import org.osgi.framework.wiring.BundleWiring;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Activator
/*     */   implements BundleActivator, SynchronousBundleListener
/*     */ {
/*  44 */   private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
/*     */   
/*  46 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static void checkPermission(Permission permission) {
/*  49 */     if (SECURITY_MANAGER != null) {
/*  50 */       SECURITY_MANAGER.checkPermission(permission);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadProvider(Bundle bundle) {
/*  55 */     if (bundle.getState() == 1) {
/*     */       return;
/*     */     }
/*     */     try {
/*  59 */       checkPermission((Permission)new AdminPermission(bundle, "resource"));
/*  60 */       checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
/*  61 */       loadProvider((BundleWiring)bundle.adapt(BundleWiring.class));
/*  62 */     } catch (SecurityException e) {
/*  63 */       LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", new Object[] { bundle.getSymbolicName(), e });
/*  64 */     } catch (Exception e) {
/*  65 */       LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", new Object[] { bundle.getSymbolicName(), e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadProvider(BundleWiring provider) {
/*  70 */     List<URL> urls = provider.findEntries("META-INF", "log4j-provider.properties", 0);
/*  71 */     for (URL url : urls) {
/*  72 */       ProviderUtil.loadProvider(url, provider.getClassLoader());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(BundleContext context) throws Exception {
/*  78 */     BundleWiring self = (BundleWiring)context.getBundle().adapt(BundleWiring.class);
/*  79 */     List<BundleWire> required = self.getRequiredWires(LoggerContextFactory.class.getName());
/*  80 */     for (BundleWire wire : required) {
/*  81 */       loadProvider(wire.getProviderWiring());
/*     */     }
/*  83 */     context.addBundleListener((BundleListener)this);
/*  84 */     Bundle[] bundles = context.getBundles();
/*  85 */     for (Bundle bundle : bundles) {
/*  86 */       loadProvider(bundle);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(BundleContext context) throws Exception {
/*  92 */     context.removeBundleListener((BundleListener)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bundleChanged(BundleEvent event) {
/*  97 */     switch (event.getType()) {
/*     */       
/*     */       case 2:
/* 100 */         loadProvider(event.getBundle());
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4\\util\Activator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */