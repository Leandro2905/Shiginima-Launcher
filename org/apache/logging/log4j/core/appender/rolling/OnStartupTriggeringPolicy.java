/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ @Plugin(name = "OnStartupTriggeringPolicy", category = "Core", printObject = true)
/*     */ public class OnStartupTriggeringPolicy
/*     */   implements TriggeringPolicy
/*     */ {
/*  34 */   private static long JVM_START_TIME = initStartTime();
/*     */ 
/*     */   
/*     */   private boolean evaluated = false;
/*     */ 
/*     */   
/*     */   private RollingFileManager manager;
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager manager) {
/*  45 */     this.manager = manager;
/*  46 */     if (JVM_START_TIME == 0L) {
/*  47 */       this.evaluated = true;
/*     */     }
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
/*     */   private static long initStartTime() {
/*     */     try {
/*  63 */       Class<?> factoryClass = Loader.loadSystemClass("java.lang.management.ManagementFactory");
/*  64 */       Method getRuntimeMXBean = factoryClass.getMethod("getRuntimeMXBean", new Class[0]);
/*  65 */       Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
/*     */       
/*  67 */       Class<?> runtimeMXBeanClass = Loader.loadSystemClass("java.lang.management.RuntimeMXBean");
/*  68 */       Method getStartTime = runtimeMXBeanClass.getMethod("getStartTime", new Class[0]);
/*  69 */       Long result = (Long)getStartTime.invoke(runtimeMXBean, new Object[0]);
/*     */       
/*  71 */       return result.longValue();
/*  72 */     } catch (Throwable t) {
/*  73 */       StatusLogger.getLogger().error("Unable to call ManagementFactory.getRuntimeMXBean().getStartTime(), using system time for OnStartupTriggeringPolicy", t);
/*     */ 
/*     */       
/*  76 */       return System.currentTimeMillis();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/*  87 */     if (this.evaluated) {
/*  88 */       return false;
/*     */     }
/*  90 */     this.evaluated = true;
/*  91 */     return (this.manager.getFileTime() < JVM_START_TIME);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     return "OnStartupTriggeringPolicy";
/*     */   }
/*     */   
/*     */   @PluginFactory
/*     */   public static OnStartupTriggeringPolicy createPolicy() {
/* 101 */     return new OnStartupTriggeringPolicy();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\OnStartupTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */