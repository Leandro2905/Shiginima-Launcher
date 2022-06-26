/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.core.appender.AsyncAppender;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncAppenderAdmin
/*     */   implements AsyncAppenderAdminMBean
/*     */ {
/*     */   private final String contextName;
/*     */   private final AsyncAppender asyncAppender;
/*     */   private final ObjectName objectName;
/*     */   
/*     */   public AsyncAppenderAdmin(String contextName, AsyncAppender appender) {
/*  42 */     this.contextName = (String)Assert.requireNonNull(contextName, "contextName");
/*  43 */     this.asyncAppender = (AsyncAppender)Assert.requireNonNull(appender, "async appender");
/*     */     try {
/*  45 */       String ctxName = Server.escape(this.contextName);
/*  46 */       String configName = Server.escape(appender.getName());
/*  47 */       String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", new Object[] { ctxName, configName });
/*  48 */       this.objectName = new ObjectName(name);
/*  49 */     } catch (Exception e) {
/*  50 */       throw new IllegalStateException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName() {
/*  61 */     return this.objectName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  66 */     return this.asyncAppender.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLayout() {
/*  71 */     return String.valueOf(this.asyncAppender.getLayout());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoreExceptions() {
/*  76 */     return this.asyncAppender.ignoreExceptions();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getErrorHandler() {
/*  81 */     return String.valueOf(this.asyncAppender.getHandler());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilter() {
/*  86 */     return String.valueOf(this.asyncAppender.getFilter());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAppenderRefs() {
/*  91 */     return this.asyncAppender.getAppenderRefStrings();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 102 */     return this.asyncAppender.isIncludeLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlocking() {
/* 112 */     return this.asyncAppender.isBlocking();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorRef() {
/* 121 */     return this.asyncAppender.getErrorRef();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueueCapacity() {
/* 126 */     return this.asyncAppender.getQueueCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueueRemainingCapacity() {
/* 131 */     return this.asyncAppender.getQueueRemainingCapacity();
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\AsyncAppenderAdmin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */