/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
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
/*    */ class Log4jJsonModule
/*    */   extends SimpleModule
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   Log4jJsonModule() {
/* 35 */     super(Log4jJsonModule.class.getName(), new Version(2, 0, 0, null, null, null));
/*    */ 
/*    */ 
/*    */     
/* 39 */     (new Initializers.SimpleModuleInitializer()).initialize(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 45 */     super.setupModule(context);
/* 46 */     (new Initializers.SetupContextInitializer()).setupModule(context);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\Log4jJsonModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */