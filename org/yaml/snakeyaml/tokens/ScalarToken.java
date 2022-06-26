/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public final class ScalarToken
/*    */   extends Token
/*    */ {
/*    */   private final String value;
/*    */   private final boolean plain;
/*    */   private final char style;
/*    */   
/*    */   public ScalarToken(String value, Mark startMark, Mark endMark, boolean plain) {
/* 26 */     this(value, plain, startMark, endMark, false);
/*    */   }
/*    */   
/*    */   public ScalarToken(String value, boolean plain, Mark startMark, Mark endMark, char style) {
/* 30 */     super(startMark, endMark);
/* 31 */     this.value = value;
/* 32 */     this.plain = plain;
/* 33 */     this.style = style;
/*    */   }
/*    */   
/*    */   public boolean getPlain() {
/* 37 */     return this.plain;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 41 */     return this.value;
/*    */   }
/*    */   
/*    */   public char getStyle() {
/* 45 */     return this.style;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 50 */     return "value=" + this.value + ", plain=" + this.plain + ", style=" + this.style;
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 55 */     return Token.ID.Scalar;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\tokens\ScalarToken.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */