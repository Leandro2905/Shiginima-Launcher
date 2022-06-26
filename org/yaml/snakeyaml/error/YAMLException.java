/*    */ package org.yaml.snakeyaml.error;
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
/*    */ public class YAMLException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -4738336175050337570L;
/*    */   
/*    */   public YAMLException(String message) {
/* 22 */     super(message);
/*    */   }
/*    */   
/*    */   public YAMLException(Throwable cause) {
/* 26 */     super(cause);
/*    */   }
/*    */   
/*    */   public YAMLException(String message, Throwable cause) {
/* 30 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\error\YAMLException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */