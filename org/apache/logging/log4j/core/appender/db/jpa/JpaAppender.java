/*     */ package org.apache.logging.log4j.core.appender.db.jpa;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ @Plugin(name = "JPA", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class JpaAppender
/*     */   extends AbstractDatabaseAppender<JpaDatabaseManager>
/*     */ {
/*     */   private final String description;
/*     */   
/*     */   private JpaAppender(String name, Filter filter, boolean ignoreExceptions, JpaDatabaseManager manager) {
/*  46 */     super(name, filter, ignoreExceptions, manager);
/*  47 */     this.description = getName() + "{ manager=" + getManager() + " }";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  52 */     return this.description;
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
/*     */   @PluginFactory
/*     */   public static JpaAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Filter") Filter filter, @PluginAttribute("bufferSize") String bufferSize, @PluginAttribute("entityClassName") String entityClassName, @PluginAttribute("persistenceUnitName") String persistenceUnitName) {
/*  77 */     if (Strings.isEmpty(entityClassName) || Strings.isEmpty(persistenceUnitName)) {
/*  78 */       LOGGER.error("Attributes entityClassName and persistenceUnitName are required for JPA Appender.");
/*  79 */       return null;
/*     */     } 
/*     */     
/*  82 */     int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
/*  83 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */ 
/*     */     
/*     */     try {
/*  87 */       Class<? extends AbstractLogEventWrapperEntity> entityClass = Loader.loadClass(entityClassName);
/*     */ 
/*     */       
/*  90 */       if (!AbstractLogEventWrapperEntity.class.isAssignableFrom(entityClass)) {
/*  91 */         LOGGER.error("Entity class [{}] does not extend AbstractLogEventWrapperEntity.", new Object[] { entityClassName });
/*  92 */         return null;
/*     */       } 
/*     */       
/*     */       try {
/*  96 */         entityClass.getConstructor(new Class[0]);
/*  97 */       } catch (NoSuchMethodException e) {
/*  98 */         LOGGER.error("Entity class [{}] does not have a no-arg constructor. The JPA provider will reject it.", new Object[] { entityClassName });
/*     */         
/* 100 */         return null;
/*     */       } 
/*     */       
/* 103 */       Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor = entityClass.getConstructor(new Class[] { LogEvent.class });
/*     */ 
/*     */       
/* 106 */       String managerName = "jpaManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", persistenceUnitName=" + persistenceUnitName + ", entityClass=" + entityClass.getName() + '}';
/*     */ 
/*     */       
/* 109 */       JpaDatabaseManager manager = JpaDatabaseManager.getJPADatabaseManager(managerName, bufferSizeInt, entityClass, entityConstructor, persistenceUnitName);
/*     */ 
/*     */       
/* 112 */       if (manager == null) {
/* 113 */         return null;
/*     */       }
/*     */       
/* 116 */       return new JpaAppender(name, filter, ignoreExceptions, manager);
/* 117 */     } catch (ClassNotFoundException e) {
/* 118 */       LOGGER.error("Could not load entity class [{}].", new Object[] { entityClassName, e });
/* 119 */       return null;
/* 120 */     } catch (NoSuchMethodException e) {
/* 121 */       LOGGER.error("Entity class [{}] does not have a constructor with a single argument of type LogEvent.", new Object[] { entityClassName });
/*     */       
/* 123 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\db\jpa\JpaAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */