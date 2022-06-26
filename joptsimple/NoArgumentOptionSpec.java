/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NoArgumentOptionSpec
/*    */   extends AbstractOptionSpec<Void>
/*    */ {
/*    */   NoArgumentOptionSpec(String option) {
/* 40 */     this(Collections.singletonList(option), "");
/*    */   }
/*    */   
/*    */   NoArgumentOptionSpec(Collection<String> options, String description) {
/* 44 */     super(options, description);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
/* 51 */     detectedOptions.add(this);
/*    */   }
/*    */   
/*    */   public boolean acceptsArguments() {
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public boolean requiresArgument() {
/* 59 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public String argumentDescription() {
/* 67 */     return "";
/*    */   }
/*    */   
/*    */   public String argumentTypeIndicator() {
/* 71 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   protected Void convert(String argument) {
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   public List<Void> defaultValues() {
/* 80 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\NoArgumentOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */