/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*    */ import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
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
/*    */ final class Log4jXmlModule
/*    */   extends JacksonXmlModule
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   Log4jXmlModule() {
/* 37 */     (new Initializers.SimpleModuleInitializer()).initialize((SimpleModule)this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 43 */     super.setupModule(context);
/* 44 */     (new Initializers.SetupContextInitializer()).setupModule(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\Log4jXmlModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */