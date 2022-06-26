/*    */ package org.apache.logging.log4j.core.config.xml;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "XmlConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(5)
/*    */ public class XmlConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 35 */   public static final String[] SUFFIXES = new String[] { ".xml", "*" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(ConfigurationSource source) {
/* 44 */     return (Configuration)new XmlConfiguration(source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 53 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\xml\XmlConfigurationFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */