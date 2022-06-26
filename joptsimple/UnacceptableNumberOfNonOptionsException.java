/*    */ package joptsimple;
/*    */ 
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
/*    */ class UnacceptableNumberOfNonOptionsException
/*    */   extends OptionException
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/*    */   private final int minimum;
/*    */   private final int maximum;
/*    */   private final int actual;
/*    */   
/*    */   UnacceptableNumberOfNonOptionsException(int minimum, int maximum, int actual) {
/* 43 */     super(Collections.singletonList("[arguments]"));
/*    */     
/* 45 */     this.minimum = minimum;
/* 46 */     this.maximum = maximum;
/* 47 */     this.actual = actual;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 52 */     return String.format("actual = %d, minimum = %d, maximum = %d", new Object[] { Integer.valueOf(this.actual), Integer.valueOf(this.minimum), Integer.valueOf(this.maximum) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\UnacceptableNumberOfNonOptionsException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */