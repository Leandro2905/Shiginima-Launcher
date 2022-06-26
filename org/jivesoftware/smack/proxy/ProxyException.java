/*    */ package org.jivesoftware.smack.proxy;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class ProxyException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ProxyException(ProxyInfo.ProxyType type, String ex, Throwable cause) {
/* 34 */     super("Proxy Exception " + type.toString() + " : " + ex + ", " + cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProxyException(ProxyInfo.ProxyType type, String ex) {
/* 39 */     super("Proxy Exception " + type.toString() + " : " + ex);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProxyException(ProxyInfo.ProxyType type) {
/* 44 */     super("Proxy Exception " + type.toString() + " : " + "Unknown Error");
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smack\proxy\ProxyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */