/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.Node;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.util.NameUtil;
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
/*    */ public class PluginAttributeVisitor
/*    */   extends AbstractPluginVisitor<PluginAttribute>
/*    */ {
/*    */   public PluginAttributeVisitor() {
/* 33 */     super(PluginAttribute.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object visit(Configuration configuration, Node node, LogEvent event, StringBuilder log) {
/* 39 */     String name = this.annotation.value();
/* 40 */     Map<String, String> attributes = node.getAttributes();
/* 41 */     String rawValue = removeAttributeValue(attributes, name, this.aliases);
/* 42 */     String replacedValue = this.substitutor.replace(event, rawValue);
/* 43 */     Object defaultValue = findDefaultValue(event);
/* 44 */     Object value = convert(replacedValue, defaultValue);
/* 45 */     Object debugValue = this.annotation.sensitive() ? NameUtil.md5(value + getClass().getName()) : value;
/* 46 */     log.append(name).append("=\"").append(debugValue).append('"');
/* 47 */     return value;
/*    */   }
/*    */   
/*    */   private Object findDefaultValue(LogEvent event) {
/* 51 */     if (this.conversionType == int.class || this.conversionType == Integer.class) {
/* 52 */       return Integer.valueOf(this.annotation.defaultInt());
/*    */     }
/* 54 */     if (this.conversionType == long.class || this.conversionType == Long.class) {
/* 55 */       return Long.valueOf(this.annotation.defaultLong());
/*    */     }
/* 57 */     if (this.conversionType == boolean.class || this.conversionType == Boolean.class) {
/* 58 */       return Boolean.valueOf(this.annotation.defaultBoolean());
/*    */     }
/* 60 */     if (this.conversionType == float.class || this.conversionType == Float.class) {
/* 61 */       return Float.valueOf(this.annotation.defaultFloat());
/*    */     }
/* 63 */     if (this.conversionType == double.class || this.conversionType == Double.class) {
/* 64 */       return Double.valueOf(this.annotation.defaultDouble());
/*    */     }
/* 66 */     if (this.conversionType == byte.class || this.conversionType == Byte.class) {
/* 67 */       return Byte.valueOf(this.annotation.defaultByte());
/*    */     }
/* 69 */     if (this.conversionType == char.class || this.conversionType == Character.class) {
/* 70 */       return Character.valueOf(this.annotation.defaultChar());
/*    */     }
/* 72 */     if (this.conversionType == short.class || this.conversionType == Short.class) {
/* 73 */       return Short.valueOf(this.annotation.defaultShort());
/*    */     }
/* 75 */     if (this.conversionType == Class.class) {
/* 76 */       return this.annotation.defaultClass();
/*    */     }
/* 78 */     return this.substitutor.replace(event, this.annotation.defaultString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginAttributeVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */