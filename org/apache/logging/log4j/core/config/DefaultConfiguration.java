/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.core.Appender;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.appender.ConsoleAppender;
/*    */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultConfiguration
/*    */   extends AbstractConfiguration
/*    */ {
/*    */   public static final String DEFAULT_NAME = "Default";
/*    */   public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";
/*    */   public static final String DEFAULT_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
/*    */   
/*    */   public DefaultConfiguration() {
/* 53 */     super(ConfigurationSource.NULL_SOURCE);
/*    */     
/* 55 */     setName("Default");
/* 56 */     PatternLayout patternLayout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
/*    */ 
/*    */ 
/*    */     
/* 60 */     ConsoleAppender consoleAppender = ConsoleAppender.createAppender((Layout)patternLayout, null, "SYSTEM_OUT", "Console", "false", "true");
/*    */     
/* 62 */     consoleAppender.start();
/* 63 */     addAppender((Appender)consoleAppender);
/* 64 */     LoggerConfig root = getRootLogger();
/* 65 */     root.addAppender((Appender)consoleAppender, null, null);
/*    */     
/* 67 */     String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
/* 68 */     Level level = (levelName != null && Level.valueOf(levelName) != null) ? Level.valueOf(levelName) : Level.ERROR;
/*    */     
/* 70 */     root.setLevel(level);
/*    */   }
/*    */   
/*    */   protected void doConfigure() {}
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\DefaultConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */