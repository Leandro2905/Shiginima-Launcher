/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UncaughtExceptionHandlers
/*    */ {
/*    */   public static Thread.UncaughtExceptionHandler systemExit() {
/* 50 */     return new Exiter(Runtime.getRuntime());
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/* 54 */   static final class Exiter implements Thread.UncaughtExceptionHandler { private static final Logger logger = Logger.getLogger(Exiter.class.getName());
/*    */     
/*    */     private final Runtime runtime;
/*    */     
/*    */     Exiter(Runtime runtime) {
/* 59 */       this.runtime = runtime;
/*    */     }
/*    */ 
/*    */     
/*    */     public void uncaughtException(Thread t, Throwable e) {
/*    */       try {
/* 65 */         logger.log(Level.SEVERE, String.format("Caught an exception in %s.  Shutting down.", new Object[] { t }), e);
/* 66 */       } catch (Throwable errorInLogging) {
/*    */ 
/*    */         
/* 69 */         System.err.println(e.getMessage());
/* 70 */         System.err.println(errorInLogging.getMessage());
/*    */       } finally {
/* 72 */         this.runtime.exit(1);
/*    */       } 
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\com\google\commo\\util\concurrent\UncaughtExceptionHandlers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */