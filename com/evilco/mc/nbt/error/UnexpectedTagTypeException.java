/*    */ package com.evilco.mc.nbt.error;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnexpectedTagTypeException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -6604963428978583800L;
/*    */   
/*    */   public UnexpectedTagTypeException() {
/* 16 */     super("The tag is not of the expected type");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnexpectedTagTypeException(String message) {
/* 24 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnexpectedTagTypeException(Throwable cause) {
/* 32 */     super(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnexpectedTagTypeException(String message, Throwable cause) {
/* 41 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\error\UnexpectedTagTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */