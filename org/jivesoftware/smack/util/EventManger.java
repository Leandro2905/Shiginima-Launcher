/*    */ package org.jivesoftware.smack.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ public class EventManger<K, R, E extends Exception>
/*    */ {
/* 37 */   private final Map<K, Reference<R>> events = new ConcurrentHashMap<>();
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
/*    */   public R performActionAndWaitForEvent(K eventKey, long timeout, Callback<E> action) throws InterruptedException, E {
/* 53 */     Reference<R> reference = new Reference<>();
/* 54 */     this.events.put(eventKey, reference);
/*    */     try {
/* 56 */       synchronized (reference) {
/* 57 */         action.action();
/* 58 */         reference.wait(timeout);
/*    */       } 
/* 60 */       return (R)reference.eventResult;
/*    */     } finally {
/*    */       
/* 63 */       this.events.remove(eventKey);
/*    */     } 
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
/*    */   
/*    */   public boolean signalEvent(K eventKey, R eventResult) {
/* 79 */     Reference<R> reference = this.events.get(eventKey);
/* 80 */     if (reference == null) {
/* 81 */       return false;
/*    */     }
/* 83 */     reference.eventResult = (V)eventResult;
/* 84 */     synchronized (reference) {
/* 85 */       reference.notifyAll();
/*    */     } 
/* 87 */     return true;
/*    */   }
/*    */   
/*    */   public static interface Callback<E extends Exception> {
/*    */     void action() throws E;
/*    */   }
/*    */   
/*    */   private static class Reference<V> {
/*    */     volatile V eventResult;
/*    */     
/*    */     private Reference() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\jivesoftware\smac\\util\EventManger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */