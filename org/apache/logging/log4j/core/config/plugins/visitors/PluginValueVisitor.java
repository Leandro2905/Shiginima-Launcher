/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginValue;
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
/*    */ public class PluginValueVisitor
/*    */   extends AbstractPluginVisitor<PluginValue>
/*    */ {
/*    */   public PluginValueVisitor() {
/* 30 */     super(PluginValue.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 36 */     String name = this.annotation.value();
/* 37 */     String rawValue = (node.getValue() != null) ? node.getValue() : removeAttributeValue(node.getAttributes(), "value", new String[0]);
/*    */     
/* 39 */     String value = this.substitutor.replace(event, rawValue);
/* 40 */     log.append(name).append("=\"").append(value).append('"');
/* 41 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginValueVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */