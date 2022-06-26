/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class UnconfiguredOptionException
/*    */   extends OptionException
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/*    */   
/*    */   UnconfiguredOptionException(String option) {
/* 41 */     this(Collections.singletonList(option));
/*    */   }
/*    */   
/*    */   UnconfiguredOptionException(Collection<String> options) {
/* 45 */     super(options);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 50 */     return "Option " + multipleOptionMessage() + " has not been configured on this parser";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\UnconfiguredOptionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */