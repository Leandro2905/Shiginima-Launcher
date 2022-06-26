/*    */ package org.apache.logging.log4j.core.appender.rolling;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "Policies", category = "Core", printObject = true)
/*    */ public final class CompositeTriggeringPolicy
/*    */   implements TriggeringPolicy
/*    */ {
/*    */   private final TriggeringPolicy[] policies;
/*    */   
/*    */   private CompositeTriggeringPolicy(TriggeringPolicy... policies) {
/* 33 */     this.policies = policies;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initialize(RollingFileManager manager) {
/* 42 */     for (TriggeringPolicy policy : this.policies) {
/* 43 */       policy.initialize(manager);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTriggeringEvent(LogEvent event) {
/* 54 */     for (TriggeringPolicy policy : this.policies) {
/* 55 */       if (policy.isTriggeringEvent(event)) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     StringBuilder sb = new StringBuilder("CompositeTriggeringPolicy{");
/* 65 */     boolean first = true;
/* 66 */     for (TriggeringPolicy policy : this.policies) {
/* 67 */       if (!first) {
/* 68 */         sb.append(", ");
/*    */       }
/* 70 */       sb.append(policy.toString());
/* 71 */       first = false;
/*    */     } 
/* 73 */     sb.append('}');
/* 74 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") TriggeringPolicy... policies) {
/* 85 */     return new CompositeTriggeringPolicy(policies);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\appender\rolling\CompositeTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */