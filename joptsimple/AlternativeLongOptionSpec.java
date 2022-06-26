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
/*    */ 
/*    */ class AlternativeLongOptionSpec
/*    */   extends ArgumentAcceptingOptionSpec<String>
/*    */ {
/*    */   AlternativeLongOptionSpec() {
/* 39 */     super(Collections.singletonList("W"), true, "Alternative form of long options");
/*    */     
/* 41 */     describedAs("opt=value");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
/* 46 */     if (!arguments.hasMore()) {
/* 47 */       throw new OptionMissingRequiredArgumentException(options());
/*    */     }
/* 49 */     arguments.treatNextAsLongOption();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\AlternativeLongOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */