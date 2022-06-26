/*    */ package org.apache.logging.log4j.core.config.json;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "JsonConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(6)
/*    */ public class JsonConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 36 */   private static final String[] SUFFIXES = new String[] { ".json", ".jsn" };
/*    */   
/* 38 */   private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean isActive;
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonConfigurationFactory() {
/* 47 */     for (String dependency : dependencies) {
/* 48 */       if (!Loader.isClassAvailable(dependency)) {
/* 49 */         LOGGER.debug("Missing dependencies for Json support");
/* 50 */         this.isActive = false;
/*    */         return;
/*    */       } 
/*    */     } 
/* 54 */     this.isActive = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isActive() {
/* 59 */     return this.isActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(ConfigurationSource source) {
/* 64 */     if (!this.isActive) {
/* 65 */       return null;
/*    */     }
/* 67 */     return (Configuration)new JsonConfiguration(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 72 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\json\JsonConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */