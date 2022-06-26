/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import com.lmax.disruptor.Sequence;
/*    */ import com.lmax.disruptor.SequenceReportingEventHandler;
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
/*    */ public class RingBufferLogEventHandler
/*    */   implements SequenceReportingEventHandler<RingBufferLogEvent>
/*    */ {
/*    */   private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
/*    */   private Sequence sequenceCallback;
/*    */   private int counter;
/*    */   
/*    */   public void setSequenceCallback(Sequence sequenceCallback) {
/* 37 */     this.sequenceCallback = sequenceCallback;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEvent(RingBufferLogEvent event, long sequence, boolean endOfBatch) throws Exception {
/* 43 */     event.execute(endOfBatch);
/* 44 */     event.clear();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     if (++this.counter > 50) {
/* 50 */       this.sequenceCallback.set(sequence);
/* 51 */       this.counter = 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Leandro\Desktop\Decompiler\shiginima-launcher-se.v4400.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */