/*    */ package org.apache.logging.log4j.core.config.yaml;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.util.Loader;
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
/*    */ @Plugin(name = "YamlConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(7)
/*    */ public class YamlConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 33 */   private static final String[] SUFFIXES = new String[] { ".yml", ".yaml" };
/*    */   
/* 35 */   private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser", "com.fasterxml.jackson.dataformat.yaml.YAMLFactory" };
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean isActive;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public YamlConfigurationFactory() {
/* 45 */     for (String dependency : dependencies) {
/* 46 */       if (!Loader.isClassAvailable(dependency)) {
/* 47 */         LOGGER.debug("Missing dependencies for Yaml support");
/* 48 */         this.isActive = false;
/*    */         return;
/*    */       } 
/*    */     } 
/* 52 */     this.isActive = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isActive() {
/* 57 */     return this.isActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(ConfigurationSource source) {
/* 62 */     if (!this.isActive) {
/* 63 */       return null;
/*    */     }
/* 65 */     return (Configuration)new YamlConfiguration(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 70 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\yaml\YamlConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */