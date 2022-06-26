/*     */ package net.minecraft.launcher.ui.tabs;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import javax.swing.JPanel;
/*     */ import net.minecraft.launcher.Launcher;
/*     */ import net.minecraft.launcher.ui.tabs.website.Browser;
/*     */ import net.minecraft.launcher.ui.tabs.website.LegacySwingBrowser;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebsiteTab
/*     */   extends JPanel
/*     */ {
/*  24 */   private static final Logger LOGGER = LogManager.getLogger();
/*  25 */   private final Browser browser = selectBrowser();
/*     */   
/*     */   private final Launcher minecraftLauncher;
/*     */   
/*     */   public WebsiteTab(Launcher minecraftLauncher) {
/*  30 */     this.minecraftLauncher = minecraftLauncher;
/*     */     
/*  32 */     setLayout(new BorderLayout());
/*  33 */     add(this.browser.getComponent(), "Center");
/*  34 */     this.browser.resize(getSize());
/*     */     
/*  36 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e)
/*     */           {
/*  40 */             WebsiteTab.this.browser.resize(e.getComponent().getSize());
/*     */           }
/*     */         });
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
/*     */   private Browser selectBrowser() {
/*  71 */     return (Browser)new LegacySwingBrowser();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPage(String url) {
/*  76 */     this.browser.loadUrl(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public Launcher getMinecraftLauncher() {
/*  81 */     return this.minecraftLauncher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addToSystemClassLoader(File file) throws IntrospectionException {
/*  87 */     if (ClassLoader.getSystemClassLoader() instanceof URLClassLoader) {
/*     */       
/*  89 */       URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
/*     */       
/*     */       try {
/*  92 */         Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
/*  93 */         method.setAccessible(true);
/*  94 */         method.invoke(classLoader, new Object[] { file.toURI().toURL() });
/*     */       }
/*  96 */       catch (Throwable t) {
/*     */         
/*  98 */         LOGGER.warn("Couldn't add " + file + " to system classloader", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasJFX() {
/*     */     try {
/* 107 */       getClass().getClassLoader().loadClass("javafx.embed.swing.JFXPanel");
/* 108 */       return true;
/*     */     }
/* 110 */     catch (ClassNotFoundException classNotFoundException) {
/* 111 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\net\minecraft\launche\\ui\tabs\WebsiteTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */