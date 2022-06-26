/*    */ package org.apache.logging.log4j.core.config.yaml;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonFactory;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.json.JsonConfiguration;
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
/*    */ public class YamlConfiguration
/*    */   extends JsonConfiguration
/*    */ {
/*    */   public YamlConfiguration(ConfigurationSource configSource) {
/* 29 */     super(configSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ObjectMapper getObjectMapper() {
/* 34 */     return (new ObjectMapper((JsonFactory)new YAMLFactory())).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\yaml\YamlConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */