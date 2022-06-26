/*    */ package org.xmlpull.v1.builder.impl;
/*    */ 
/*    */ import org.xmlpull.v1.builder.XmlComment;
/*    */ import org.xmlpull.v1.builder.XmlContainer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlCommentImpl
/*    */   implements XmlComment
/*    */ {
/*    */   private XmlContainer owner_;
/*    */   private String content_;
/*    */   
/*    */   XmlCommentImpl(XmlContainer owner, String content) {
/* 18 */     this.owner_ = owner;
/* 19 */     this.content_ = content;
/* 20 */     if (content == null) throw new IllegalArgumentException("comment content can not be null"); 
/*    */   }
/*    */   
/*    */   public String getContent() {
/* 24 */     return this.content_;
/*    */   }
/*    */   
/*    */   public XmlContainer getParent() {
/* 28 */     return this.owner_;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\xmlpull\v1\builder\impl\XmlCommentImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */