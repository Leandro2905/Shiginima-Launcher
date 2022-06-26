/*    */ package org.apache.logging.log4j.core.jmx;
/*    */ 
/*    */ import javax.management.ObjectName;
/*    */ import org.apache.logging.log4j.core.Appender;
/*    */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*    */ import org.apache.logging.log4j.core.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AppenderAdmin
/*    */   implements AppenderAdminMBean
/*    */ {
/*    */   private final String contextName;
/*    */   private final Appender appender;
/*    */   private final ObjectName objectName;
/*    */   
/*    */   public AppenderAdmin(String contextName, Appender appender) {
/* 43 */     this.contextName = (String)Assert.requireNonNull(contextName, "contextName");
/* 44 */     this.appender = (Appender)Assert.requireNonNull(appender, "appender");
/*    */     try {
/* 46 */       String ctxName = Server.escape(this.contextName);
/* 47 */       String configName = Server.escape(appender.getName());
/* 48 */       String name = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { ctxName, configName });
/* 49 */       this.objectName = new ObjectName(name);
/* 50 */     } catch (Exception e) {
/* 51 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectName getObjectName() {
/* 62 */     return this.objectName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 67 */     return this.appender.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLayout() {
/* 72 */     return String.valueOf(this.appender.getLayout());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isIgnoreExceptions() {
/* 77 */     return this.appender.ignoreExceptions();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getErrorHandler() {
/* 82 */     return String.valueOf(this.appender.getHandler());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFilter() {
/* 87 */     if (this.appender instanceof AbstractFilterable) {
/* 88 */       return String.valueOf(((AbstractFilterable)this.appender).getFilter());
/*    */     }
/* 90 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jmx\AppenderAdmin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */