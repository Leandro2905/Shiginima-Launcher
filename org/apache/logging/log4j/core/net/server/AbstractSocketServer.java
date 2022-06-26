/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEventListener;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
/*     */ import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSocketServer<T extends InputStream>
/*     */   extends LogEventListener
/*     */   implements Runnable
/*     */ {
/*     */   protected static final int MAX_PORT = 65534;
/*     */   
/*     */   protected static class ServerConfigurationFactory
/*     */     extends XmlConfigurationFactory
/*     */   {
/*     */     private final String path;
/*     */     
/*     */     public ServerConfigurationFactory(String path) {
/*  54 */       this.path = path;
/*     */     }
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(String name, URI configLocation) {
/*  59 */       if (this.path != null && this.path.length() > 0) {
/*  60 */         File file = null;
/*  61 */         ConfigurationSource source = null;
/*     */         try {
/*  63 */           file = new File(this.path);
/*  64 */           FileInputStream is = new FileInputStream(file);
/*  65 */           source = new ConfigurationSource(is, file);
/*  66 */         } catch (FileNotFoundException ex) {}
/*     */ 
/*     */         
/*  69 */         if (source == null) {
/*     */           try {
/*  71 */             URL url = new URL(this.path);
/*  72 */             source = new ConfigurationSource(url.openStream(), url);
/*  73 */           } catch (MalformedURLException mue) {
/*     */           
/*  75 */           } catch (IOException ioe) {}
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  81 */           if (source != null) {
/*  82 */             return (Configuration)new XmlConfiguration(source);
/*     */           }
/*  84 */         } catch (Exception ex) {}
/*     */ 
/*     */         
/*  87 */         System.err.println("Unable to process configuration at " + this.path + ", using default.");
/*     */       } 
/*  89 */       return super.getConfiguration(name, configLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean active = true;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final LogEventBridge<T> logEventInput;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Logger logger;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractSocketServer(int port, LogEventBridge<T> logEventInput) {
/* 108 */     this.logger = LogManager.getLogger(getClass().getName() + '.' + port);
/* 109 */     this.logEventInput = (LogEventBridge<T>)Assert.requireNonNull(logEventInput, "LogEventInput");
/*     */   }
/*     */   
/*     */   protected boolean isActive() {
/* 113 */     return this.active;
/*     */   }
/*     */   
/*     */   protected void setActive(boolean isActive) {
/* 117 */     this.active = isActive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Thread startNewThread() {
/* 126 */     Thread thread = new Thread(this);
/* 127 */     thread.start();
/* 128 */     return thread;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\net\server\AbstractSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */