/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.ThreadContext;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
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
/*    */ class Initializers
/*    */ {
/*    */   static class SetupContextInitializer
/*    */   {
/*    */     void setupModule(Module.SetupContext context) {
/* 44 */       context.setMixInAnnotations(StackTraceElement.class, StackTraceElementMixIn.class);
/*    */       
/* 46 */       context.setMixInAnnotations(Marker.class, MarkerMixIn.class);
/* 47 */       context.setMixInAnnotations(Level.class, LevelMixIn.class);
/* 48 */       context.setMixInAnnotations(LogEvent.class, LogEventMixIn.class);
/*    */       
/* 50 */       context.setMixInAnnotations(ExtendedStackTraceElement.class, ExtendedStackTraceElementMixIn.class);
/* 51 */       context.setMixInAnnotations(ThrowableProxy.class, ThrowableProxyMixIn.class);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class SimpleModuleInitializer
/*    */   {
/*    */     void initialize(SimpleModule simpleModule) {
/* 62 */       simpleModule.addDeserializer(StackTraceElement.class, (JsonDeserializer)new Log4jStackTraceElementDeserializer());
/* 63 */       simpleModule.addDeserializer(ThreadContext.ContextStack.class, (JsonDeserializer)new MutableThreadContextStackDeserializer());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\jackson\Initializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */