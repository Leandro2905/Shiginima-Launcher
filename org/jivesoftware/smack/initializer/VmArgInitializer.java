/*    */ package org.jivesoftware.smack.initializer;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class VmArgInitializer
/*    */   extends UrlInitializer
/*    */ {
/*    */   protected String getFilePath() {
/* 35 */     return System.getProperty("smack.provider.file");
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Exception> initialize() {
/* 40 */     if (getFilePath() != null) {
/* 41 */       super.initialize();
/*    */     }
/* 43 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\initializer\VmArgInitializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */