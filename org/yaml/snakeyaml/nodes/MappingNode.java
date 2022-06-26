/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingNode
/*     */   extends CollectionNode
/*     */ {
/*     */   private List<NodeTuple> value;
/*     */   private boolean merged = false;
/*     */   
/*     */   public MappingNode(Tag tag, boolean resolved, List<NodeTuple> value, Mark startMark, Mark endMark, Boolean flowStyle) {
/*  34 */     super(tag, startMark, endMark, flowStyle);
/*  35 */     if (value == null) {
/*  36 */       throw new NullPointerException("value in a Node is required.");
/*     */     }
/*  38 */     this.value = value;
/*  39 */     this.resolved = resolved;
/*     */   }
/*     */   
/*     */   public MappingNode(Tag tag, List<NodeTuple> value, Boolean flowStyle) {
/*  43 */     this(tag, true, value, (Mark)null, (Mark)null, flowStyle);
/*     */   }
/*     */ 
/*     */   
/*     */   public NodeId getNodeId() {
/*  48 */     return NodeId.mapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NodeTuple> getValue() {
/*  57 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(List<NodeTuple> merge) {
/*  61 */     this.value = merge;
/*     */   }
/*     */   
/*     */   public void setOnlyKeyType(Class<? extends Object> keyType) {
/*  65 */     for (NodeTuple nodes : this.value) {
/*  66 */       nodes.getKeyNode().setType(keyType);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTypes(Class<? extends Object> keyType, Class<? extends Object> valueType) {
/*  71 */     for (NodeTuple nodes : this.value) {
/*  72 */       nodes.getValueNode().setType(valueType);
/*  73 */       nodes.getKeyNode().setType(keyType);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     StringBuilder buf = new StringBuilder();
/*  81 */     for (NodeTuple node : getValue()) {
/*  82 */       buf.append("{ key=");
/*  83 */       buf.append(node.getKeyNode());
/*  84 */       buf.append("; value=");
/*  85 */       if (node.getValueNode() instanceof CollectionNode) {
/*     */         
/*  87 */         buf.append(System.identityHashCode(node.getValueNode()));
/*     */       } else {
/*  89 */         buf.append(node.toString());
/*     */       } 
/*  91 */       buf.append(" }");
/*     */     } 
/*  93 */     String values = buf.toString();
/*  94 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMerged(boolean merged) {
/* 102 */     this.merged = merged;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMerged() {
/* 109 */     return this.merged;
/*     */   }
/*     */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\yaml\snakeyaml\nodes\MappingNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */