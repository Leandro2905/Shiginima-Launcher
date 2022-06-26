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
/*    */ final class ParserRules
/*    */ {
/*    */   static final char HYPHEN_CHAR = '-';
/* 39 */   static final String HYPHEN = String.valueOf('-');
/*    */   static final String DOUBLE_HYPHEN = "--";
/*    */   static final String OPTION_TERMINATOR = "--";
/*    */   static final String RESERVED_FOR_EXTENSIONS = "W";
/*    */   
/*    */   private ParserRules() {
/* 45 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   static boolean isShortOptionToken(String argument) {
/* 49 */     return (argument.startsWith(HYPHEN) && !HYPHEN.equals(argument) && !isLongOptionToken(argument));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isLongOptionToken(String argument) {
/* 55 */     return (argument.startsWith("--") && !isOptionTerminator(argument));
/*    */   }
/*    */   
/*    */   static boolean isOptionTerminator(String argument) {
/* 59 */     return "--".equals(argument);
/*    */   }
/*    */   
/*    */   static void ensureLegalOption(String option) {
/* 63 */     if (option.startsWith(HYPHEN)) {
/* 64 */       throw new IllegalOptionSpecificationException(String.valueOf(option));
/*    */     }
/* 66 */     for (int i = 0; i < option.length(); i++)
/* 67 */       ensureLegalOptionCharacter(option.charAt(i)); 
/*    */   }
/*    */   
/*    */   static void ensureLegalOptions(Collection<String> options) {
/* 71 */     for (String each : options)
/* 72 */       ensureLegalOption(each); 
/*    */   }
/*    */   
/*    */   private static void ensureLegalOptionCharacter(char option) {
/* 76 */     if (!Character.isLetterOrDigit(option) && !isAllowedPunctuation(option))
/* 77 */       throw new IllegalOptionSpecificationException(String.valueOf(option)); 
/*    */   }
/*    */   
/*    */   private static boolean isAllowedPunctuation(char option) {
/* 81 */     String allowedPunctuation = "?.-";
/* 82 */     return (allowedPunctuation.indexOf(option) != -1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\joptsimple\ParserRules.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */