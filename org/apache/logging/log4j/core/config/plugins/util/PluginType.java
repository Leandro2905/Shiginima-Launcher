/*    */ package org.apache.logging.log4j.core.config.plugins.util;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class PluginType<T>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4743255148794846612L;
/*    */   private final Class<T> pluginClass;
/*    */   private final String elementName;
/*    */   private final boolean printObject;
/*    */   private final boolean deferChildren;
/*    */   
/*    */   public PluginType(Class<T> clazz, String name, boolean printObj, boolean deferChildren) {
/* 38 */     this.pluginClass = clazz;
/* 39 */     this.elementName = name;
/* 40 */     this.printObject = printObj;
/* 41 */     this.deferChildren = deferChildren;
/*    */   }
/*    */   
/*    */   public Class<T> getPluginClass() {
/* 45 */     return this.pluginClass;
/*    */   }
/*    */   
/*    */   public String getElementName() {
/* 49 */     return this.elementName;
/*    */   }
/*    */   
/*    */   public boolean isObjectPrintable() {
/* 53 */     return this.printObject;
/*    */   }
/*    */   
/*    */   public boolean isDeferChildren() {
/* 57 */     return this.deferChildren;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "PluginType [pluginClass=" + this.pluginClass + ", elementName=" + this.elementName + ", printObject=" + this.printObject + ", deferChildren=" + this.deferChildren + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */