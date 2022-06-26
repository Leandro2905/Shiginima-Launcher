/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
/*    */ import org.yaml.snakeyaml.error.YAMLException;
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
/*    */ public abstract class Token
/*    */ {
/*    */   private final Mark startMark;
/*    */   private final Mark endMark;
/*    */   
/*    */   public enum ID
/*    */   {
/* 23 */     Alias, Anchor, BlockEnd, BlockEntry, BlockMappingStart, BlockSequenceStart, Directive, DocumentEnd, DocumentStart, FlowEntry, FlowMappingEnd, FlowMappingStart, FlowSequenceEnd, FlowSequenceStart, Key, Scalar, StreamEnd, StreamStart, Tag, Value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Token(Mark startMark, Mark endMark) {
/* 30 */     if (startMark == null || endMark == null) {
/* 31 */       throw new YAMLException("Token requires marks.");
/*    */     }
/* 33 */     this.startMark = startMark;
/* 34 */     this.endMark = endMark;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 38 */     return "<" + getClass().getName() + "(" + getArguments() + ")>";
/*    */   }
/*    */   
/*    */   public Mark getStartMark() {
/* 42 */     return this.startMark;
/*    */   }
/*    */   
/*    */   public Mark getEndMark() {
/* 46 */     return this.endMark;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 53 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract ID getTokenId();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 68 */     if (obj instanceof Token) {
/* 69 */       return toString().equals(obj.toString());
/*    */     }
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return toString().hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\tokens\Token.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */