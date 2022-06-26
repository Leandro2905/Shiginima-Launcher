/*    */ package com.evilco.mc.nbt.error;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TagNotFoundException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -4631008535746749103L;
/*    */   
/*    */   public TagNotFoundException() {
/* 15 */     super("The tag does not exist");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TagNotFoundException(String message) {
/* 23 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TagNotFoundException(Throwable cause) {
/* 31 */     super(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TagNotFoundException(String message, Throwable cause) {
/* 40 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\evilco\mc\nbt\error\TagNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */