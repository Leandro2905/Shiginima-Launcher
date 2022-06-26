/*    */ package org.apache.logging.log4j.message;
/*    */ 
/*    */ import java.util.ResourceBundle;
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
/*    */ public class LocalizedMessageFactory
/*    */   extends AbstractMessageFactory
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final ResourceBundle resourceBundle;
/*    */   private final String baseName;
/*    */   
/*    */   public LocalizedMessageFactory(ResourceBundle resourceBundle) {
/* 35 */     this.resourceBundle = resourceBundle;
/* 36 */     this.baseName = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public LocalizedMessageFactory(String baseName) {
/* 41 */     this.resourceBundle = null;
/* 42 */     this.baseName = baseName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBaseName() {
/* 52 */     return this.baseName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResourceBundle getResourceBundle() {
/* 62 */     return this.resourceBundle;
/*    */   }
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
/*    */   public Message newMessage(String key, Object... params) {
/* 77 */     if (this.resourceBundle == null) {
/* 78 */       return new LocalizedMessage(this.baseName, key, params);
/*    */     }
/* 80 */     return new LocalizedMessage(this.resourceBundle, key, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\message\LocalizedMessageFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */