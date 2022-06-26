/*    */ package org.apache.logging.log4j.core.config.plugins.processor;
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
/*    */ public class PluginEntry
/*    */   implements Serializable
/*    */ {
/*    */   public static final long serialVersionUID = 1L;
/*    */   private String key;
/*    */   private String className;
/*    */   private String name;
/*    */   private boolean printable;
/*    */   private boolean defer;
/*    */   private transient String category;
/*    */   
/*    */   public String getKey() {
/* 36 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 40 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getClassName() {
/* 44 */     return this.className;
/*    */   }
/*    */   
/*    */   public void setClassName(String className) {
/* 48 */     this.className = className;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 56 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isPrintable() {
/* 60 */     return this.printable;
/*    */   }
/*    */   
/*    */   public void setPrintable(boolean printable) {
/* 64 */     this.printable = printable;
/*    */   }
/*    */   
/*    */   public boolean isDefer() {
/* 68 */     return this.defer;
/*    */   }
/*    */   
/*    */   public void setDefer(boolean defer) {
/* 72 */     this.defer = defer;
/*    */   }
/*    */   
/*    */   public String getCategory() {
/* 76 */     return this.category;
/*    */   }
/*    */   
/*    */   public void setCategory(String category) {
/* 80 */     this.category = category;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */