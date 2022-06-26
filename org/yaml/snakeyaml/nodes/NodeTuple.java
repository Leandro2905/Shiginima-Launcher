/*    */ package org.yaml.snakeyaml.nodes;
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
/*    */ public final class NodeTuple
/*    */ {
/*    */   private Node keyNode;
/*    */   private Node valueNode;
/*    */   
/*    */   public NodeTuple(Node keyNode, Node valueNode) {
/* 27 */     if (keyNode == null || valueNode == null) {
/* 28 */       throw new NullPointerException("Nodes must be provided.");
/*    */     }
/* 30 */     this.keyNode = keyNode;
/* 31 */     this.valueNode = valueNode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Node getKeyNode() {
/* 38 */     return this.keyNode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Node getValueNode() {
/* 47 */     return this.valueNode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return "<NodeTuple keyNode=" + this.keyNode.toString() + "; valueNode=" + this.valueNode.toString() + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\nodes\NodeTuple.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */