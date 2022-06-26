/*    */ package org.jivesoftware.smack;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import org.jivesoftware.smack.util.Objects;
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
/*    */ public abstract class Manager
/*    */ {
/*    */   final WeakReference<XMPPConnection> weakConnection;
/*    */   
/*    */   public Manager(XMPPConnection connection) {
/* 28 */     Objects.requireNonNull(connection, "XMPPConnection must not be null");
/*    */     
/* 30 */     this.weakConnection = new WeakReference<>(connection);
/*    */   }
/*    */   
/*    */   protected final XMPPConnection connection() {
/* 34 */     return this.weakConnection.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\Manager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */