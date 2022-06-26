/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public final class Charsets
/*    */ {
/* 31 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Charset getSupportedCharset(String charsetName) {
/* 42 */     return getSupportedCharset(charsetName, Charset.defaultCharset());
/*    */   }
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
/*    */   public static Charset getSupportedCharset(String charsetName, Charset defaultCharset) {
/* 56 */     Charset charset = null;
/* 57 */     if (charsetName != null && Charset.isSupported(charsetName)) {
/* 58 */       charset = Charset.forName(charsetName);
/*    */     }
/* 60 */     if (charset == null) {
/* 61 */       charset = defaultCharset;
/* 62 */       if (charsetName != null) {
/* 63 */         StatusLogger.getLogger().error("Charset " + charsetName + " is not supported for layout, using " + charset.displayName());
/*    */       }
/*    */     } 
/*    */     
/* 67 */     return charset;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\cor\\util\Charsets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */