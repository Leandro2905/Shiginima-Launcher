/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.Layout;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractLayout<T extends Serializable>
/*    */   implements Layout<T>
/*    */ {
/*    */   public AbstractLayout(byte[] header, byte[] footer) {
/* 43 */     this.header = header;
/* 44 */     this.footer = footer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final byte[] header;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final byte[] footer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getHeader() {
/* 69 */     return this.header;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getFooter() {
/* 79 */     return this.footer;
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\layout\AbstractLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */