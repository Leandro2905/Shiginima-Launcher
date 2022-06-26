/*    */ package org.yaml.snakeyaml.nodes;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScalarNode
/*    */   extends Node
/*    */ {
/*    */   private Character style;
/*    */   private String value;
/*    */   
/*    */   public ScalarNode(Tag tag, String value, Mark startMark, Mark endMark, Character style) {
/* 31 */     this(tag, true, value, startMark, endMark, style);
/*    */   }
/*    */ 
/*    */   
/*    */   public ScalarNode(Tag tag, boolean resolved, String value, Mark startMark, Mark endMark, Character style) {
/* 36 */     super(tag, startMark, endMark);
/* 37 */     if (value == null) {
/* 38 */       throw new NullPointerException("value in a Node is required.");
/*    */     }
/* 40 */     this.value = value;
/* 41 */     this.style = style;
/* 42 */     this.resolved = resolved;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Character getStyle() {
/* 53 */     return this.style;
/*    */   }
/*    */ 
/*    */   
/*    */   public NodeId getNodeId() {
/* 58 */     return NodeId.scalar;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 67 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 71 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\nodes\ScalarNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */