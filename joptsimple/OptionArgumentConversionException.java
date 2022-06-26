/*    */ package joptsimple;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ class OptionArgumentConversionException
/*    */   extends OptionException
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/*    */   private final String argument;
/*    */   private final Class<?> valueType;
/*    */   
/*    */   OptionArgumentConversionException(Collection<String> options, String argument, Class<?> valueType, Throwable cause) {
/* 44 */     super(options, cause);
/*    */     
/* 46 */     this.argument = argument;
/* 47 */     this.valueType = valueType;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 52 */     return "Cannot convert argument '" + this.argument + "' of option " + multipleOptionMessage() + " to " + this.valueType;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\OptionArgumentConversionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */