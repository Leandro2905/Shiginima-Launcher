/*    */ package org.jivesoftware.smack.iqrequest;
/*    */ 
/*    */ import org.jivesoftware.smack.packet.IQ;
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
/*    */ public abstract class AbstractIqRequestHandler
/*    */   implements IQRequestHandler
/*    */ {
/*    */   private final String element;
/*    */   private final String namespace;
/*    */   private final IQ.Type type;
/*    */   private final IQRequestHandler.Mode mode;
/*    */   
/*    */   protected AbstractIqRequestHandler(String element, String namespace, IQ.Type type, IQRequestHandler.Mode mode) {
/* 33 */     switch (type) {
/*    */       case set:
/*    */       case get:
/*    */         break;
/*    */       default:
/* 38 */         throw new IllegalArgumentException("Only get and set IQ type allowed");
/*    */     } 
/* 40 */     this.element = element;
/* 41 */     this.namespace = namespace;
/* 42 */     this.type = type;
/* 43 */     this.mode = mode;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract IQ handleIQRequest(IQ paramIQ);
/*    */ 
/*    */   
/*    */   public IQRequestHandler.Mode getMode() {
/* 51 */     return this.mode;
/*    */   }
/*    */ 
/*    */   
/*    */   public IQ.Type getType() {
/* 56 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getElement() {
/* 61 */     return this.element;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNamespace() {
/* 66 */     return this.namespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\iqrequest\AbstractIqRequestHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */