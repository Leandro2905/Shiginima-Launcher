/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
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
/*    */ public class JndiCloser
/*    */ {
/*    */   public static void close(Context context) throws NamingException {
/* 37 */     if (context != null) {
/* 38 */       context.close();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeSilently(Context context) {
/*    */     try {
/* 49 */       close(context);
/* 50 */     } catch (NamingException ignored) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\JndiCloser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */